

package fire.pb.timer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import fire.msp.npc.GAddNpcToMiniMap;
import fire.msp.npc.GRemoveNpcFromMiniMap;
import fire.msp.npc.Pos;
import fire.pb.GsClient;
import fire.pb.util.MessageUtil;


public abstract class AbstractScheduledActivity implements IScheduledActivity {

	/**
	 * 有时候有的任务会有一些定时发通告的循环任务,如果在stop或者end的时候,这些future需要被cancel的话,可以
	 * 先调用MessageUtil.sendRepeatableMsg,获得一个long值,然后把long加入到这个list中.在stop
	 * 或end的时候,list里面所有的long对应的future都会被cancel.出于安全的考虑,子类不能访问到这个list,只能 往里面添加
	 */
	private List<Long> futures = new ArrayList<Long>();
	/**
	 * 有些活动的npc是动态招出来的,若需要在小地图上显示,则需要用到这个map,所有的npc按mapid为key存在map中,当玩家进入这张
	 * 地图或本来就在这张地图的时候,会给他们发消息,把这些npc加入小地图中
	 */
	private static Map<Integer, Set<MiniMapNpc>> showNpcMaps = new HashMap<Integer, Set<MiniMapNpc>>();

	protected final int id;
	
	protected final int baseid;
	
	protected long activityStartTime; 

	protected long activityEndTime;
	
	protected ScheduledFuture<?> startActivityFuture;
	
	protected ScheduledFuture<?> endActivityFuture;
	
	protected ScheduledFuture<?> resumeActivityFuture;

	protected ScheduledFuture<?> tmpstartActivityFuture;
	
	protected ScheduledFuture<?> tmpendActivityFuture;
	
	public AbstractScheduledActivity(int baseid,int id, long startTime, long endTime) {

		super();
		this.id = id;
		this.baseid = baseid;
		this.activityStartTime = startTime;
		this.activityEndTime = endTime;
	}

	@Override
	public final boolean start() throws Exception {

		logger.info("try to start activity.activity id:"+id);
		//活动开始之前先清空奖励表的物品获得上限,并且把这个已开始的活动加入活动表中
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
                xbean.ActivityItemLimit itemLimit= xtable.Awarditemlimit.get(baseid);
                if (itemLimit!=null){
                	for (xbean.LimitItemInfo info : itemLimit.getLimititemmap().values()) {
						info.setNum(0);
					}
                }
                xbean.ActivityStatus activityStatus = xtable.Activitytable.get(id);
                if (activityStatus==null){
                	activityStatus = xbean.Pod.newActivityStatus();
                    xtable.Activitytable.insert(id, activityStatus);
                }
                activityStatus.setBaseid(baseid);
                activityStatus.setId(id);
                activityStatus.setLaststarttime(System.currentTimeMillis());
                if (activityStatus.getInistarttime()==0) 
                activityStatus.setInistarttime(activityStartTime);
                if (activityStatus.getIniendtime()==0) 
                activityStatus.setIniendtime(activityEndTime);
                
				return true;
			}
            			
		}.submit().get();
		if (init()){
            logger.info("activity started.activity id:"+id);			
			return true;
		}
		return false;
	}

	protected abstract boolean init() throws Exception;

	@Override
	public final boolean stop(boolean cancelFuture) throws Exception {

		 logger.info("try to stop activity.activity id:"+id);	
		if (stopActivity()) {
			MessageUtil.cancelRepeatablemsg(futures);
			if (cancelFuture){
				if (startActivityFuture!=null)
					startActivityFuture.cancel(true);
				if (endActivityFuture!=null)
					endActivityFuture.cancel(true);
				if (resumeActivityFuture!=null)
					resumeActivityFuture.cancel(true);
			}

			//把暂停的时间计入活动表中
			new mkdb.Procedure(){
				@Override
				protected boolean process() throws Exception {
					xbean.ActivityStatus activityStatus = xtable.Activitytable.get(id);
					if (activityStatus!=null){
						activityStatus.setLastendtime(System.currentTimeMillis());
						long period = activityStatus.getLastendtime() - activityStatus.getLaststarttime();
						if (period>0) 
						activityStatus.setCosttime(activityStatus.getCosttime()+period);
					}
					return true;
				}
				
			}.submit().get();
			logger.info("activity stopped.activity id:"+id);
			return true;
		}
		return false;
	}

	//提前终止活动,可能在活动进行中,也可能在活动还没开始的时候就终止.
	protected abstract boolean stopActivity();

	@Override
	public final boolean end(boolean cancelFuture) throws Exception {

		logger.info("try to end activity.activity id:"+id);	
		new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
                //从活动表里面把这次活动删掉
				xbean.ActivityStatus activityStatus = xtable.Activitytable.get(id);
				if (activityStatus!=null)
					xtable.Activitytable.remove(id);
				return true;
			}
			
		}.submit().get();
		MessageUtil.cancelRepeatablemsg(futures);
		if (cancelFuture){
			if (startActivityFuture!=null)
				startActivityFuture.cancel(true);
			if (endActivityFuture!=null)
				endActivityFuture.cancel(true);
			if (resumeActivityFuture!=null)
				resumeActivityFuture.cancel(true);
		}
		if (endActivity()){
			 logger.info("activity ended.activity id:"+id);	
			 return true;
		}
		return false;
	}

	protected abstract boolean endActivity();

	public void addScheduledTask(long taskKey) {

		this.futures.add(taskKey);
	}
	/**
	 * 把一个npc加入小地图中,活动开始的时候最好把生成的npc都放入这个map中,之所以用map作为参数是为了节省
	 * 发消息的条数,避免每来一个npc就发一条消息.每个活动的owner也争取把要放在小地图上的npc一次性地放入map中,
	 * 再调用这个方法.
	 * @param 活动中要放在小地图上显示的npc的map,key为mapid,value是该地图上的npcs
	 */
	public synchronized static final boolean addNpcToMiniMap(Collection<MiniMapNpc> npcsets){
		//先把npc按照mapid存入一个map中
		Map<Integer, Set<MiniMapNpc>> activityNpcs = sortNpcByMapid(npcsets);

		for (Entry<Integer, Set<MiniMapNpc>> entry : activityNpcs.entrySet()) {
			int mapid = entry.getKey();
			Set<MiniMapNpc> npcs = showNpcMaps.get(mapid);
			if (npcs==null){
				npcs=new HashSet<MiniMapNpc>();
				showNpcMaps.put(mapid, npcs);
			}
			npcs.addAll(entry.getValue());
			
			//发送协议
			GAddNpcToMiniMap gAddNpcToMiniMap = new GAddNpcToMiniMap();
			for (MiniMapNpc npc:entry.getValue()) {
				gAddNpcToMiniMap.baseids.add(npc.getBaseid());
				gAddNpcToMiniMap.poses.add(new Pos(npc.getPosx(),npc.getPosy()));
			}
			gAddNpcToMiniMap.sceneid = mapid;
			gAddNpcToMiniMap.roleid = 0;
			GsClient.sendToScene(gAddNpcToMiniMap);
		}
		return true;
		
	}

	//活动结束,清除npc的时候,调用这个方法,把那些要在小地图去掉的npc加进来
	public synchronized static final boolean removeNpcFromMiniMap(Collection<MiniMapNpc> npcsets){
		if (npcsets == null || npcsets.isEmpty())
			return false;
		//先把npc按照mapid存入一个map中
		Map<Integer, Set<MiniMapNpc>> activityNpcs = sortNpcByMapid(npcsets);
		if (!activityNpcs.isEmpty()) 
		for (Entry<Integer, Set<MiniMapNpc>> entry : activityNpcs.entrySet()) {
			int mapid = entry.getKey();
			Set<MiniMapNpc> npcs = showNpcMaps.get(mapid);
			if (npcs!=null){
				npcs.removeAll(entry.getValue());
				//发送协议
				GRemoveNpcFromMiniMap gRemoveNpcFromMiniMap = new GRemoveNpcFromMiniMap();
				for (MiniMapNpc npc:entry.getValue()){
					gRemoveNpcFromMiniMap.baseids.add(npc.getBaseid());
				}
				gRemoveNpcFromMiniMap.sceneid = mapid;
				GsClient.sendToScene(gRemoveNpcFromMiniMap);
			}
		}
		return true;
	}
	//当玩家跳转进入一张地图的时候,把地图上的活动npc刷新在小地图上
	public static void sendActivityNpcToMiniMap(long sceneid,long roleid){
		try {
			Set<MiniMapNpc> npcs=showNpcMaps.get((int)sceneid);
			if (npcs!=null&&npcs.size()>0){
				//发送协议
				GAddNpcToMiniMap gAddNpcToMiniMap = new GAddNpcToMiniMap();
				for (MiniMapNpc npc:npcs) {
					gAddNpcToMiniMap.baseids.add(npc.getBaseid());
					gAddNpcToMiniMap.poses.add(new Pos(npc.getPosx(),npc.getPosy()));
				}
				gAddNpcToMiniMap.sceneid = (int)sceneid;
				gAddNpcToMiniMap.roleid = roleid;
				GsClient.sendToScene(gAddNpcToMiniMap);
			}
		} catch (Exception e) {
			logger.info("send minimap npc failed.sceneid:"+sceneid+"roleid:"+roleid);
		}
	}
	static Map<Integer, Set<MiniMapNpc>> sortNpcByMapid(Collection<MiniMapNpc> npcsets){
		Map<Integer, Set<MiniMapNpc>> activityNpcs = new HashMap<Integer, Set<MiniMapNpc>>();
		for (MiniMapNpc npc : npcsets) {
			if (npc == null)
				continue;
			int mapid = npc.getSceneid();
			Set<MiniMapNpc> npcs = activityNpcs.get(mapid);
			if (npcs==null){
				npcs = new HashSet<MiniMapNpc>();
				activityNpcs.put(mapid, npcs);
			}
			npcs.add(npc);
		}
		return activityNpcs;
	}

	@Override
	public long resume(boolean gm, long lastStartTime, long lastEndTime, long costTime) throws Exception {

		//把本次的开始时间放入数据库中
        new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {
               xbean.ActivityStatus activityStatus = xtable.Activitytable.get(id);
               if (activityStatus!=null){
            	   activityStatus.setLaststarttime(System.currentTimeMillis());
               }
				return true;
			}
        	
        }.submit().get();
		return resumeActivity(gm,lastStartTime,lastEndTime,costTime);
	}

	
	
	/**
	 * resumeActivity:重新开始活动,有两种,一种是gm指令继续该活动,还有一种是服务器停服之后继续该活动.在
	 * AbstractScheduledActivity里默认的实现是什么都不干(return 0不会触发任何操作).子类可以覆盖这个方法.
	 * 
	 * @param gm true:是gm指令继续活动. false:服务器重启继续该活动
	 * @param lastStartTime 上次活动的开始时间
	 * @param lastEndTime 上次活动的结束时间
	 * @param costTime 在resume之前活动已经举行了多长时间.注意costTime不一定等于lastEndTime-lastStartTime,因为可能
	 * 活动时间很长,在这期间服务器重启过不只一次,lastEndTime,lastStartTime只是记录最近一次的起止时间,costTime是活动从开始之后
	 * 所耗费的总时间
	 * @return 返回这个活动的结束时间.因为活动是暂停后继续的,所以原来配的结束时间肯定不适用了,需要自己根据各个活动的情况返回结束时间,结束时间一到,
	 * 会自动调用endActivity 
	 * @throws Exception
	 */
	protected long resumeActivity(boolean gm, long lastStartTime, long lastEndTime, long costTime) throws Exception{
		
		return 0;
	}

	
	public void setStartActivityFuture(ScheduledFuture<?> startActivityFuture) {
	
		this.startActivityFuture = startActivityFuture;
	}

	
	public void setEndActivityFuture(ScheduledFuture<?> endActivityFuture) {
	
		this.endActivityFuture = endActivityFuture;
	}

	
	public void setResumeActivityFuture(ScheduledFuture<?> resumeActivityFuture) {
	
		this.resumeActivityFuture = resumeActivityFuture;
	}

	
	public void setActivityStartTime(long activityStartTime) {
	
		this.activityStartTime = activityStartTime;
	}

	
	public void setActivityEndTime(long activityEndTime) {
	
		this.activityEndTime = activityEndTime;
	}

	
	public void setTmpstartActivityFuture(ScheduledFuture<?> tmpstartActivityFuture) {
	
		this.tmpstartActivityFuture = tmpstartActivityFuture;
	}

	
	public void setTmpendActivityFuture(ScheduledFuture<?> tmpendActivityFuture) {
	
		this.tmpendActivityFuture = tmpendActivityFuture;
	}

}
