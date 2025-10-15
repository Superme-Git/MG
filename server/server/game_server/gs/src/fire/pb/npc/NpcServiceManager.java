package fire.pb.npc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.circletask.CircleTask;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.clan.ClanUtils;
import fire.pb.compensation.CompensationManager;
import fire.pb.fushi.FushiManager;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.instance.InstanceManager;
import fire.pb.npc.NpcServiceCond.Condition;
import fire.pb.school.shouxi.ProfessionLeaderManager;
import mkdb.Procedure;

import org.apache.log4j.Logger;


public class NpcServiceManager {
	
	private static NpcServiceManager _instance = new NpcServiceManager();
	private NpcServiceManager(){};

	public synchronized static NpcServiceManager getInstance()
	{
		return _instance;
	}
	
	public static void reload() throws Exception
	{
		NpcServiceManager instance = new NpcServiceManager();
		instance.init();
		synchronized (NpcServiceManager.class)
		{
			_instance = instance;
		}
	}
	
	private static final Logger logger = Logger.getLogger("MAPMAIN");
	//public static java.util.Properties npcprop = fire.pb.main.ConfigManager.getInstance().getPropConf("npc");
	
	private java.util.NavigableMap<Integer, SNpcServiceConfig>  npcServiceMap 
					= new java.util.TreeMap<Integer, SNpcServiceConfig>();
	
	private java.util.NavigableMap<Integer, SServiceConds> serviceDetailMap
					= new java.util.TreeMap<Integer, SServiceConds>();
	
	private java.util.NavigableMap<Integer, SNpcServiceMapping> npcServiceMappingMap
					= new java.util.TreeMap<Integer, SNpcServiceMapping>();
	
	private java.util.Map<Integer, Condition> allcondsmap 
					= new java.util.HashMap<Integer,Condition>();
	
	
	
	
	private java.util.Vector<Integer> gmclosedservices = new java.util.Vector<Integer>();
	
	//added by mtgserv.导入花仙子答题的题库.题库放入数据库中

	void init() throws Exception {
		final fire.pb.main.ConfigManager cm=fire.pb.main.ConfigManager.getInstance();
		
		npcServiceMap = cm.getConf(fire.pb.npc.SNpcServiceConfig.class);
		logger.info("NPC服务配置表加载完毕。一共载入服务NPC" + npcServiceMap.size() + "个");
		
		serviceDetailMap = cm.getConf(fire.pb.npc.SServiceConds.class);
		logger.info("NPC服务描述表加载完毕。一共载入服务 " + serviceDetailMap.size() + "条");
		
		npcServiceMappingMap = cm.getConf(fire.pb.npc.SNpcServiceMapping.class);
		logger.info("NPC服务映射表加载完毕。一共载入服务映射" + npcServiceMappingMap.size() + "条");
	
		registerAllConds(cm.getConf(fire.pb.npc.SAllConds.class));
	
		//getGatherconfigMap();
				
		
	}
	
	
	
	
	public void registerAllConds(java.util.NavigableMap<Integer, SAllConds> condsmap){
		if (condsmap == null)
			return;
		Iterator<Integer> iter = condsmap.keySet().iterator();
		while (iter.hasNext()){
			int key = iter.next();
			SAllConds cond = condsmap.get(key);
			try {
				Condition condclass = (Condition) Class.forName("fire.pb.npc.NpcServiceCond."+cond.condname).getConstructor().newInstance();
				allcondsmap.put(key, condclass);	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public final  SNpcServiceConfig getServiceConfigByNpcID(int npcid){
		return npcServiceMap.get(npcid);
	}
	
	public final SNpcServiceMapping getServiceMappingByServiceID(int serviceid){
		return npcServiceMappingMap.get(serviceid);
	}
	
	
	
	public void openService(Integer serviceid){
		gmclosedservices.remove(serviceid);
	}
	
	public void closeService(Integer serviceid){
		gmclosedservices.add(serviceid);
	}
	
	public boolean ingmclosedservice(Integer serviceid){
		return gmclosedservices.contains(serviceid);
	}
	
	
	public static int getNpcIDByKey(long key){
		final Npc npc = SceneNpcManager.selectNpcByKey(key);
		if (null == npc )
			return -1;
		else return npc.getNpcID();
	}
	

	
	private static final ArrayList<Integer> getDynamicServicesIDS(long npckey,long roleid){
		ArrayList<Integer> result = new ArrayList<Integer>();
		fire.pb.map.Npc curnpc = SceneNpcManager.selectNpcByKey(npckey);
		if(curnpc == null){
			logger.warn("role:" + roleid + "SceneNpcManager select npc=" + npckey + ",is null!");
			return result;
		}
		int npcid = curnpc.getNpcID();
		try {
			CircleTask.addCTDynamicServices(roleid, npckey, result);
			//RoleAnYeTask.addDynamicServices(roleid, npckey, result);
			// 首席弟子添加动态服务
			ProfessionLeaderManager.addDynamicServices(roleid, npckey, npcid, result);
			// 组队副本的动态服务
//			InstanceManager.getInstance().addDynamicServices(roleid, npckey,
//					npcid, result);
			// 系统补偿的动态服务
			CompensationManager.getInstance().addDynamicServices(roleid, npcid,
					result);
			//fire.pb.mission.Module.getInstance().getDynamicServices(roleid,
					//npckey, result);
			// 公会副本动态服务
			ClanUtils.addDynamicServices(roleid, npckey, npcid, result);
			
			//明雷npc动态服务
			TimerNpcService.addCTDynamicServices(roleid, npcid, npckey, result);
			// 动态服务
			Module.addDynamicServices(npcid, result);
		} catch (Exception e) {
			logger.error("exception when add dynamic service", e);
		}
		return result;
	}
	
	
	public final java.util.List<Integer> getServicesIDSByNpcKey(long roleid, long npckey){
		return getServicesIDS(roleid, npckey, true);
	}
	
	public final java.util.List<Integer> getShowServicesIDSByNpcKey(long roleid, long npckey) {
		return getServicesIDS(roleid, npckey, false);
	}
	
	
	private final java.util.List<Integer> getServicesIDS(long roleid, long npckey, boolean getChildService) {
		java.util.List<Integer> results = new ArrayList<Integer>();
		java.util.List<Integer> dynamicResults = getDynamicServicesIDS(npckey, roleid);
		results.addAll(dynamicResults);//先加上动态服务,动态服务是优先显示的
		final int npcid = getNpcIDByKey(npckey);
		final SNpcServiceConfig config = npcServiceMap.get(npcid);
		if (null != config && null != config.getServices() ) {
			results.addAll(config.getServices());
		}
		
		List<Integer> toAdd = new ArrayList<Integer>();
		List<Integer> toRemove = new ArrayList<Integer>();
		for (int serviceid : results) {
			if (!getChildService && !isServiceShow(roleid, serviceid)) {
				toRemove.add(serviceid);
				continue;
			}
			
			SServiceConds allconds = getServiceConds(serviceid);
			if (getChildService) {
				if (allconds != null && allconds.childservice != null) {
					toAdd.addAll(allconds.childservice);
				}
			}
		}
		if (getChildService)
			results.addAll(toAdd);
		if (!getChildService)
			results.removeAll(toRemove);
		
		if(npcid == 10964){
			dealReturnFuShi(roleid, results);
		}
		
		results.removeAll(gmclosedservices);
		//暗夜马戏团任务相关动态服务
		RoleAnYeTask.addDynamicServices(roleid, npckey, results);
		return results;
	}
	
	private void dealReturnFuShi(long roleid, List<Integer> validlist){
		int userid = new PropRole(roleid, true).getUserid();
		int returnFuShi = FushiManager.getInstance().getFuShiFanHuan(userid);
		if(returnFuShi > 0){
			validlist.add(1820);
		}
		xbean.WeiBoNotify notify = xtable.Role2weibonotify.select(roleid);
		if(notify != null && notify.getTakeawardflag() == 1){
			validlist.add(0,1987);
		}
	}
	
	private boolean isServiceShow(long roleid, int serviceId){
		SServiceConds allconds = getServiceConds(serviceId);
		//有填表条件先判断填表条件
		if (allconds != null && !allconds.conditionids.isEmpty())
		{
			for (SNpcCond cond:allconds.conditionids){
				Condition condclass = allcondsmap.get(cond.condid);
				if (condclass == null){
					logger.debug("没有配置此id的条件  id=" + cond.condid);
					return false;
				}
				if (!condclass.CheckCond(roleid, cond.args1, cond.args2)){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public SServiceConds getServiceConds(int serviceid){
		return serviceDetailMap.get(serviceid);
	}
	
	public boolean hasServiceByNpcKey(long roleid, long npckey, int... serviceids){
		List<Integer> services = getServicesIDSByNpcKey(roleid, npckey);
		for (int serviceid : serviceids) {
			if (!services.contains(serviceid)) {
				return false;
			}
		}
		return true;
	}
	
	public static void sendNpcDialog(boolean proctrue, long roleid, long npckey, int msgid, java.util.ArrayList<Long> args){
		if (null == args)
			return;
		
		final fire.pb.map.Npc npc = fire.pb.map.SceneNpcManager.selectNpcByKey(npckey);
		if (null == npc)
			return;
		final fire.pb.npc.SSendNpcMsg send = new fire.pb.npc.SSendNpcMsg();
		send.npcid = npc.getNpcID();
		send.npckey = npckey;
		send.msgid = msgid;
		send.args = args;
		if (proctrue){
			Procedure.psendWhileCommit(roleid, send);
		} else {
			Procedure.psendWhileRollback(roleid, send);
		}
	}
	
	public static void sendNpcDialog(long roleid, long npckey, int msgid, java.util.ArrayList<Long> args){
		if (null == args)
			return;
		
		final fire.pb.map.Npc npc = fire.pb.map.SceneNpcManager.selectNpcByKey(npckey);
		if (null == npc)
			return;
		final fire.pb.npc.SSendNpcMsg send = new fire.pb.npc.SSendNpcMsg();
		send.npcid = npc.getNpcID();
		send.npckey = npckey;
		send.msgid = msgid;
		send.args = args;
		gnet.link.Onlines.getInstance().send(roleid, send);
	}
	
	
	public static int transfNpcIDByKey(long npcKey){
		fire.pb.map.Npc npc = SceneNpcManager.selectNpcByKey(npcKey);
		if(null == npc)
			return -1;
		
		return npc.getNpcID();
	}


}
