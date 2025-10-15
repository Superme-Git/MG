package fire.pb.activity.winner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;

import fire.pb.PropRole;
import fire.pb.circletask.ActiveQuestData;
import fire.pb.circletask.SRefreshActiveQuest;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.map.GridPos;
import fire.pb.map.Npc;
import fire.pb.map.Position;
import fire.pb.map.SceneManager;
import fire.pb.map.SceneNpcManager;
import fire.pb.map.Transfer;
import fire.pb.mission.Module;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.move.SRoleEnterScene;
import fire.pb.npc.SGeneralSummonCommand;
import fire.pb.npc.SNpcPos;
import fire.pb.npc.TransmitTypes;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.timer.AbstractScheduledActivity;
import fire.pb.timer.MiniMapNpc;
import fire.pb.title.Title;
import fire.pb.util.MapUtil;
import fire.pb.util.TaskDlgUtil;
import mkdb.Procedure;


public class WinnerManager {
	public static final Logger logger = Logger.getLogger("ACTIVE");
	
	public static final int TEAM_MIN_NUM = 3;//冠军试炼队伍参加的最小人数
	public static final int MIN_LEVEL = 40;//冠军试炼开启等级
	public static final int WINNER_TITLE_ID = 24;//冠军试炼获得的称号
	public static final int TEAM_TOP_MAX=3;//队伍排名最大人数
	public static final int ROLE_TOP_MAX=10;//玩家排名最大人数
	public static final int TASK_MAX_NUM=9;//最大任务环数
	public static final int WINNER_TASK_ID_ACCEPT = 701001;//领取活动的任务
	public static final int WINNER_TASK_ID = 701002;
	public static final long WINNER_BROAD_PERIOD1 = 5*60*1000;//开始的准备时间
	public static final long WINNER_BROAD_DELAY1 = 60*1000;
	public static final long WINNER_BROAD_DELAY2 = 20*1000;
	public static final long WINNER_BROAD_PERIOD2 = 60*1000;
	public static final long WINNER_PERIOD = 2*60*60*1000;
	
	
	public ScheduledFuture<?> scheduledFuture = null;

	private static WinnerManager instance = null;
	private WinnerManager() {

	}
	public static WinnerManager getInstance() {
		if(instance==null){
			instance=new WinnerManager();
		}
		return instance;
	}
	
	/**
	 * 判断是否是冠军试炼的战斗
	 * @param fightconfig
	 * @return
	 */
	public static boolean isWinnerFight(int fightconfig){
		if(fightconfig>=3093&&fightconfig<=3101){
			return true;
		}
		return false;
	}
	
	
	// 冠军试炼活动的开始时间
	private long startTime;
	
	// 冠军试炼活动的结束时间
	private long endTime;
	
	// 冠军试炼活动的npc key
	public static long winnerActiveNpc;
	
	public MiniMapNpc winnerNpc;
	
	
	private List<MiniMapNpc> npclist = new ArrayList<MiniMapNpc>();

	public List<SNpcPos> activenpcdata = fire.pb.timer.ActivityManager.getActivityNpcPosMap().get(4).getNpcPoses();//读取策划数据表的信息，id是4

	public static List<Long> npc_keys=null;
	
	public ConcurrentHashMap<Long, WinnerRecord> teams=new ConcurrentHashMap<Long, WinnerRecord>();//活动参加的队伍
	public static ConcurrentHashMap<Long, WinnerRoleRecord> winnerrolerecords=new ConcurrentHashMap<Long, WinnerRoleRecord>();//活动参加的玩家成绩
	
	public ConcurrentHashMap<Long, Long> winnerRoleids= new ConcurrentHashMap<Long, Long>();//参与冠军试炼的玩家
	
	public ConcurrentHashMap<Integer, WinnerRecord> top3Teams=new ConcurrentHashMap<Integer, WinnerRecord>();// 前三名的队伍
	
	public ConcurrentHashMap<Integer, List<Long>> top3RoleidMaps = new ConcurrentHashMap<Integer, List<Long>>();// 前三名的所有队员id
	
	public ConcurrentHashMap<Long, Long> winnerRoleidsTeamid= new ConcurrentHashMap<Long, Long>();//参与冠军试炼的玩家上次的队伍信息     roleid teamid
	
	
	/**
	 * 处理队伍前三名
	 */
	public void dealTop3Team(){
		top3Teams.clear();
		top3RoleidMaps.clear();
		List<WinnerRecord> list=new ArrayList<WinnerRecord>();
		if(teams.size()>0){
			for(WinnerRecord record:teams.values()){
				if(record==null){
					continue;
				}
				//过滤掉解散的队伍
				Team team = TeamManager.selectTeamByTeamID(record.getTeamid());
				if(team==null){
					continue;
				}
				//过滤到成绩为0的
				if(record.getTeamScore()==0){
					continue;
				}
				if(team.getAllMemberIds().size()<=0){
					continue;
				}
				list.add(record);
			}
			Collections.sort(list,new WinnerComparator());//排序
			int num=TEAM_TOP_MAX;
			if(num>list.size()){
				num=list.size();
			}
			for(int i=0;i<num;i++){
				if(list.get(i)==null){
					continue;
				}
				top3Teams.put(i+1, list.get(i));
				//这里存一下前三名队伍信息，方便发公告和奖励
				List<Long> members=new ArrayList<Long>();
				members.addAll(list.get(i).getTeamMembers().values());
				top3RoleidMaps.put(i+1,members);
			}
		}
	}
	
	/**
	 * 根据名次获得消息号
	 * @param index
	 * @return
	 */
	public static int getMessageListByIndex(int index){
		if(index==1){
			return 160391;
		}else if(index==2){
			return 160392;
		}else{
			return 160393;
		}
	}
	
	
	/**
	 * 根据排名获得奖励
	 * @param index
	 * @return
	 */
	public static int getAwardListByIndex(int index){
		if(index==1){
			return getAwardListOne();
		}else if(index==2){
			return getAwardListTow();
		}else{
			return getAwardListThree();
		}
	}
	
	/**
	 * 判断是否存在npc
	 * @param npccode
	 * @return
	 */
	public boolean haveNpcBycode(int npccode){
		for(MiniMapNpc miniMapNpc:npclist){
			if(miniMapNpc!=null&&miniMapNpc.getBaseid()==npccode){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 排名奖励1 //	219	11017		冠军试炼额外奖励第一名
	 * @return
	 */
	public static int getAwardListOne(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(219);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+219);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 排名奖励2 //	220	11017		冠军试炼额外奖励第二名
	 * @return
	 */
	public static int getAwardListTow(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(220);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+220);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * 排名奖励3 //	221	11017		冠军试炼额外奖励第三名
	 * @return
	 */
	public static int getAwardListThree(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(221);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+221);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	/**
	 * //	222	11801		冠军试炼普通奖励
	 * @return
	 */
	public static int getAwardTaskComplete(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(222);
		if(conf==null){
			logger.error("通用数据表数据错误，不存在编号"+222);
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	public void init(long time,boolean gmStart) {
		if (scheduledFuture != null){
			scheduledFuture.cancel(false);
		}
		if(npc_keys==null){
			npc_keys=new ArrayList<Long>();
		}else{
			if(npc_keys.size()>0){
				for(Long npckey:npc_keys){
					if(npckey!=null){
						SceneNpcManager.removeNpc(npckey);
					}
				}
			}
			npc_keys.clear();
		}
		//清理相关数据
		top3Teams.clear();
		top3RoleidMaps.clear();
		teams.clear();//清除参加队伍的信息
		winnerrolerecords.clear();//清除参加玩家成绩的信息
		winnerRoleids.clear();
		winnerRoleidsTeamid.clear();
		for(int i=0;i<activenpcdata.size();i++){
			SNpcPos snpcpos=activenpcdata.get(i);
			if(snpcpos!=null){
				long npckey=SceneNpcManager.getInstance().getNextId();
				npc_keys.add(npckey);//存放npcid
				SceneNpcManager.getInstance().getNextId();
				Position pos = new GridPos(snpcpos.getNpcPosx(), snpcpos.getNpcPosy()).toPosition();
				SceneNpcManager.createNpcByPos(npckey,snpcpos.getNpcid() , snpcpos.getNpcName(), snpcpos.getNpcMap(), pos.getX(), pos.getY(), 3,0);
				MiniMapNpc miniMapNpc = new MiniMapNpc(snpcpos.getNpcid(), snpcpos.getNpcMap(), pos.getX(), pos.getY());
				//设置任务开始的npc
				if(i==0){
					winnerNpc=miniMapNpc;
				}
				if (!npclist.contains(miniMapNpc)) 
					npclist.add(miniMapNpc);
				logger.debug("冠军试炼活动生成: " + snpcpos.getNpcName()+"\tnpcid "+snpcpos.getNpcid()+" mapid="+snpcpos.getNpcMap()+"\t x="+pos.getX()/16+" y="+pos.getY()/16);
			}
		}
		
		this.startTime = time;
		this.endTime = this.startTime + WINNER_PERIOD;
		
		AbstractScheduledActivity.addNpcToMiniMap(npclist);
		// 发消息
		if (gmStart){
			STransChatMessageNotify2Client ssmg = MessageMgr.getMsgNotify(140664, 0, null);
			SceneManager.sendAll(ssmg);
		}
		logger.debug("冠军试炼初始化完成.");
	}

	/**
	 * 活动结束
	 */
	public void stopwinner() {
		long time1=System.currentTimeMillis();
        if (scheduledFuture!=null){
        	scheduledFuture.cancel(false);
        }
        //移除npc
        if(npc_keys!=null&&npc_keys.size()>0){
        	for(Long npckey:npc_keys){
				if(npckey!=null){
					SceneNpcManager.removeNpc(npckey);
				}
			}
        	npc_keys.clear();
		}
        long time2=System.currentTimeMillis();
        
        dealTop3Team();//选出team前三名
        
        long time3=System.currentTimeMillis();
		//给获胜队伍成员发放称号
		for(int i=1;i<=TEAM_TOP_MAX;i++){
			List<Long> list=top3RoleidMaps.get(i);
			if(list==null){
				continue;
			}
			List<String> paras = new ArrayList<String>();
			for(final Long roleid :list){
				if(roleid!=null&&roleid>0){
					PropRole role = new PropRole(roleid, true);
					paras.add(role.getName());
					//发称号
					Procedure.pexecute(new Procedure(){
						@Override
						protected boolean process() throws Exception {		
							Title title = new Title(roleid, true);
							if (!title.roleHaveTitle(WINNER_TITLE_ID))
								Title.addTitle(roleid, WINNER_TITLE_ID, "", -1);
							MessageMgr.psendMsgNotifyWhileCommit(roleid, 140690, null);
							return true;
						}
						
					});
					//发奖
					Procedure.pexecute(new PReceiveWinnerAward(roleid,i));
				}
			}
			StringBuffer sb = new StringBuffer();
			//这里发送名次公告
			int num=paras.size();
			if(num==0){
				continue;
			}
			if(num<5){
				for(int n=0;n<5-num;n++){
					paras.add("");
				}
			}
			WinnerRecord winnerRecord=top3Teams.get(i);
			//添加关数
			if(winnerRecord!=null){
				paras.add(winnerRecord.getTeamScore()+"");
				sb.append("当前名次\t"+i+"\t当前队伍成绩\t"+winnerRecord.getTeamScore()+"\t队伍id\t"+winnerRecord.getTeamid()+"\t队伍成员id"+winnerRecord.getTeamMembers().values());
			}else{
				paras.add("");
			}
			paras.add(i+"");//添加名次
			
			int msg=getMessageListByIndex(i);//这里参数有7个
			// 发公告
			Procedure.pexecute(new Procedure(){
				@Override
				protected boolean process() throws Exception {
					STransChatMessageNotify2Client ssmg = MessageMgr.getMsgNotify(msg, 0, paras);
					SceneManager.sendAll(ssmg);
					return true;
				}
				
			});
			logger.info(sb.toString());
		}
		long time4=System.currentTimeMillis();
		
		//发送结束公告
		Procedure.pexecute(new Procedure(){
			@Override
			protected boolean process() throws Exception {
				STransChatMessageNotify2Client ssmg = MessageMgr.getMsgNotify(140676, 0, null);
				SceneManager.sendAll(ssmg);
				return true;
			}
			
		});
		
		// 起始时间的清0要放到最后,否则会影响到top3的选取
		startTime = 0;
		endTime = 0;
		npclist.clear();
		//所有还有冠军试炼任务的人清空任务
		clearWinnerRoles();
		long time5=System.currentTimeMillis();
		logger.info("1冠军试炼活动结束消耗时间\t1="+(time2-time1));
		logger.info("2冠军试炼活动结束消耗时间\t2="+(time3-time2));
		logger.info("3冠军试炼活动结束消耗时间\t3="+(time4-time3));
		logger.info("4冠军试炼活动结束消耗时间\t4="+(time5-time4));
		logger.info("5冠军试炼活动结束消耗时间\t5="+(time5-time1));
		
	}

	

	/**
	 * 判断是否是在冠军试炼的活动时间
	 * @param time
	 * @return
	 */
	public boolean isInWinnerActiveTime(long time) {

		return startTime < time && time < endTime;
	}


	/**
	 * 创建冠军试炼任务
	 * @param taskId
	 * @param roleId
	 * @param npc
	 * @param state
	 * @param pass
	 * @param current
	 */
	public void createWinnerTask(int taskId, long roleId, Npc npc, int state, int pass, int current) {
		xbean.TaskInfos taskInfos = xtable.Roletasks.get(roleId);
		if (taskInfos == null) {
			taskInfos = xbean.Pod.newTaskInfos();
			xtable.Roletasks.insert(roleId, taskInfos);
		}
		xbean.TaskDlgInfo taskDlgInfo = taskInfos.getTasksmap().get(taskId);
		if (taskDlgInfo == null) {
			taskDlgInfo = xbean.Pod.newTaskDlgInfo();
			taskInfos.getTasksmap().put(taskId, taskDlgInfo);
		}
        Module.getInstance().trackAccpetMission(roleId, taskId);
		taskDlgInfo.setTaskid(taskId);
		taskDlgInfo.setDstmapid(MapUtil.getBaseMapIdBySceneId(npc.getScene()));
		taskDlgInfo.setDstnpcid(npc.getNpcID());
		taskDlgInfo.setDstnpckey(npc.getUniqueID());
		taskDlgInfo.setDstx(npc.getPos().toGridPos().getX());
		taskDlgInfo.setDsty(npc.getPos().toGridPos().getY());
		taskDlgInfo.setTaskstate(state);
		taskDlgInfo.setDstitemid(pass);
		taskDlgInfo.setSumnum(current);//当前次数

		ActiveQuestData activeQuestData = new ActiveQuestData();
		activeQuestData.questid = taskId;
		activeQuestData.dstmapid = MapUtil.getBaseMapIdBySceneId(npc.getScene());
		activeQuestData.dstnpcid = npc.getNpcID();
		activeQuestData.dstnpckey = npc.getUniqueID();
		activeQuestData.dstx = npc.getPos().toGridPos().getX();
		activeQuestData.dsty = npc.getPos().toGridPos().getY();
		activeQuestData.queststate = state;
		activeQuestData.dstitemid = pass;
		activeQuestData.sumnum = current;
		SRefreshActiveQuest sRefreshActiveQuest = new SRefreshActiveQuest();
		sRefreshActiveQuest.questdata = activeQuestData;
		Procedure.psendWhileCommit(roleId, sRefreshActiveQuest);
	}


	/**
	 * 刷新闯关任务
	 * @param taskId
	 * @param roleId
	 * @param npc
	 * @param state
	 * @param pass
	 * @param current
	 */
	public void refreshWinnerTask(int taskId, long roleId, Npc npc, int state, int pass, int current) {
		if (state==SpecialQuestState.SUCCESS||state==SpecialQuestState.ABANDONED){
			Module.getInstance().untrackMission(roleId, taskId);
		}
		xbean.TaskInfos taskInfos = xtable.Roletasks.get(roleId);
		xbean.TaskDlgInfo taskDlgInfo = taskInfos.getTasksmap().get(taskId);
		taskDlgInfo.setTaskid(taskId);
		taskDlgInfo.setDstmapid(MapUtil.getBaseMapIdBySceneId(npc.getScene()));
		taskDlgInfo.setDstnpcid(npc.getNpcID());
		taskDlgInfo.setDstnpckey(npc.getUniqueID());
		taskDlgInfo.setDstx(npc.getPos().toGridPos().getX());
		taskDlgInfo.setDsty(npc.getPos().toGridPos().getY());
		taskDlgInfo.setTaskstate(state);
		taskDlgInfo.setDstitemid(pass);
		taskDlgInfo.setSumnum(current);

		ActiveQuestData activeQuestData = new ActiveQuestData();
		activeQuestData.questid = taskId;
		activeQuestData.dstmapid = MapUtil.getBaseMapIdBySceneId(npc.getScene());
		activeQuestData.dstnpcid = npc.getNpcID();
		activeQuestData.dstnpckey = npc.getUniqueID();
		activeQuestData.dstx = npc.getPos().toGridPos().getX();
		activeQuestData.dsty = npc.getPos().toGridPos().getY();
		activeQuestData.queststate = state;
		activeQuestData.dstitemid = pass;
		activeQuestData.sumnum = current;
		SRefreshActiveQuest sRefreshActiveQuest = new SRefreshActiveQuest();
		sRefreshActiveQuest.questdata = activeQuestData;
		Procedure.psendWhileCommit(roleId, sRefreshActiveQuest);
	}

	/**
	 * 放弃冠军试炼相关任务
	 * @param roleid
	 * @return
	 */
	public boolean abandonWinnerTask(long roleid) {
		//清除2类任务
		mkdb.Procedure proc=new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				TaskDlgUtil.refreshTaskState(roleid, WinnerManager.WINNER_TASK_ID, SpecialQuestState.ABANDONED);
				TaskDlgUtil.refreshTaskState(roleid, WinnerManager.WINNER_TASK_ID_ACCEPT, SpecialQuestState.ABANDONED);
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
		return true;
	}
	
	/**
	 * 清除活动面板的队伍成绩
	 * @param roleid
	 * @return
	 */
	public static boolean clearActiveTeamWinnerScore(long roleid) {
		mkdb.Procedure proc=new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
				if (actrole != null){
					actrole.setActiveNum2(RoleLiveness.WINNER, 0);
				}
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
		return true;
	}

	public static boolean canTransform(long roleid,int transType) {
	  //不管能不能拉人，就只拉一次，返回true
       boolean canTrans=Transfer.canChangeMap(roleid,SRoleEnterScene.SYSTEM_DRAG, 0);
		if (!canTrans){
//			SGeneralSummonCommand sgsc = new SGeneralSummonCommand();
//			sgsc.summontype = transType;
//			sgsc.minimal = 1;
//			Onlines.getInstance().send(roleid, sgsc);
			return false;
		}
		return true;
	}
	public void removeCgNpc(){
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				if (winnerActiveNpc==0)
					return false;
				SceneNpcManager.removeNpc(winnerActiveNpc);
				winnerActiveNpc =0;
				List<MiniMapNpc> npcList = new ArrayList<MiniMapNpc>();
				npcList.add(winnerNpc);
				AbstractScheduledActivity.removeNpcFromMiniMap(npcList);
				return true;
			}
			
		}.submit();
	}
	
	/**
	 * 添加冠军试炼的玩家
	 * @param roleid
	 */
	public void addWinnerRole(long roleid){
		winnerRoleids.put(roleid, roleid);
	}

	/**
	 * 清除冠军试炼玩家的任务
	 */
	public void clearWinnerRoles(){
		for (Long rid : winnerRoleids.values()) {
			abandonWinnerTask(rid);
		}
		winnerRoleids.clear();
	}
	
	/**
	 * 在冠军试炼队伍中移除玩家信息，并且添加到新的冠军试炼队伍信息中
	 * @param roleid
	 */
	public void removeLastTeamidAddNew(WinnerRecord newrecord,long roleid){
		Long teamid=winnerRoleidsTeamid.get(roleid);
		if(teamid!=null){
			WinnerRecord winnerRecord=teams.get(teamid);
			if(winnerRecord!=null){
				winnerRecord.getTeamMembers().remove(roleid);
			}
		}
		newrecord.getTeamMembers().put(roleid, roleid);
	}
	
	/**
	 * 移除原来的队伍信息
	 * @param roleid
	 */
	public void removeLastTeamid(long roleid){
		Long teamid=winnerRoleidsTeamid.get(roleid);
		if(teamid!=null){
			WinnerRecord winnerRecord=teams.get(teamid);
			if(winnerRecord!=null){
				winnerRecord.getTeamMembers().remove(roleid);
			}
		}
	}
	
	/**
	 * 重新加载一下队伍信息
	 * @param teamid
	 * @param record
	 */
	public void reloadTeamInfo(long teamid,WinnerRecord record){
		//这里同步一下当前的玩家
		//这里需要移除这些玩家在其他队伍的信息
		for(long rid:record.getTeamMembers().values()){
			WinnerManager.getInstance().removeLastTeamid(rid);
		}
		record.getTeamMembers().clear();
		Team team = TeamManager.selectTeamByTeamID(teamid);
		if(team!=null){
			for (Long member : team.getNormalMemberIds()) {
				record.getTeamMembers().put(member, member);
				WinnerManager.getInstance().winnerRoleidsTeamid.put(member, teamid);//记录玩家参加活动对应的队伍信息
			}
		}
	}
	
	/**
	 * 拉人
	 * @param roleid
	 */
	public void sendWinnerCallPlayer(long roleid){
//		if(!WinnerManager.getInstance().isInWinnerActiveTime(System.currentTimeMillis())){
//			logger.error(roleid+"错误，冠军试炼的时候拉人,活动没有开启");
//			return ;
//		}
		if(WinnerManager.npc_keys==null){
//			logger.error(roleid+"错误，冠军试炼的时候拉人,目标npc不存在1");
			return ;
		}
		if(WinnerManager.npc_keys.size()==0){
			return ;
		}
		
		final Npc npc=SceneNpcManager.selectNpcByKey(WinnerManager.npc_keys.get(0));
		if(null == npc){
			logger.error(roleid+"错误，冠军试炼的时候拉人,目标npc不存在");
			return ;
		}
		if (!WinnerManager.canTransform(roleid,TransmitTypes.winnercall)){
			logger.error(roleid+"错误，冠军试炼的时候拉人,玩家所在地图不能传送");
			return ;
		}
		SGeneralSummonCommand snd = new SGeneralSummonCommand();
		snd.roleid = roleid;
//		snd.npckey=npc.getNpcKey();
//		snd.mapid = npc.getMapId();
//		snd.minimal = 0;
		snd.summontype = TransmitTypes.winnercall;
		if (mkdb.Transaction.current() != null)
			mkdb.Procedure.psendWhileCommit(roleid, snd);
		else{
			gnet.link.Onlines.getInstance().send(roleid, snd);
		}
		
	}
	
	
}
