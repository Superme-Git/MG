package fire.pb.activity.timernpc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import mkdb.TTableCache;

import org.apache.log4j.Logger;

import xbean.eventNpcInfoCol;
import fire.msp.role.GSetNpcBattleInfo;
import fire.pb.GsClient;
import fire.pb.main.ConfigManager;
import fire.pb.map.EventTimerNpc;
import fire.pb.map.MapConfig;
import fire.pb.map.SEventTimerNpcDianKa;
import fire.pb.map.SceneManager;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.treasuremap.BaoTuMapManager;
import fire.pb.mission.treasuremap.EventTimerGroupData;
import fire.pb.move.SceneState;
import fire.pb.npc.NpcServices;
import fire.pb.npc.SNpcShare;
import fire.pb.npc.SRefreshTimerNpc;
import fire.pb.npc.SRefreshTimerNpcDianKa;
import fire.pb.scene.movable.NPC;
import fire.pb.talk.MessageMgr;
import fire.pb.util.CalendarUtil;
import fire.pb.util.DateValidate;

/**
 * 定时任务的NPC刷新管理
 *
 */

public class TimerNpcService {
	
	/** 按npcId存储的活动id对应表,每次活动启动时修改该信息 */
	private Map<Integer,Integer> npc2act = new ConcurrentHashMap<Integer,Integer>();
	/** 按活动id存储的基础活动信息 */
	private Map<Integer,ActivityInfo> actInfos = new ConcurrentHashMap<Integer,ActivityInfo>();
	
	/** 按npcId存储的事件刷怪的对应表id,每次事件刷怪修改该信息 */
	public Map<Integer,Integer> npc2eventId = new ConcurrentHashMap<Integer,Integer>();
	
	/** 按npckey存储的事件刷怪的对应表,每次事件刷怪修改该信息*/
//	private Map<Long, NpcInfo> eventNpcInfos = new ConcurrentHashMap<Long, NpcInfo>();
	
	/** 按刷新组存储的npc数量  key 为事件刷新表.xlsx 表的编号, value为npc数量*/
//	private Map<Integer, Integer> eventNpcGroupCnt = new ConcurrentHashMap<Integer, Integer>();
	
	/** 按活动id存储的补怪定时任务*/
	private Map<Integer, ScheduledFuture<?>> actAddFuture = new ConcurrentHashMap<Integer, ScheduledFuture<?>>();
	
	/** 按活动id存储的持续刷怪的定时任务*/
	private Map<Integer, ScheduledFuture<?>> actFuture = new ConcurrentHashMap<Integer, ScheduledFuture<?>>();

	/**
	 * state 0表示失败, 1战斗中, 2战斗胜利 
	 */
	public static final int LOSE_BATTLE = 0;
	public static final int IN_BATTLE = 1;
	public static final int WIN_BATTLE = 2;
	
	/**
	 * 0,能进入战斗; 1,不能进入战斗
	 */
	public static final int CAN_BATTLE = 0;
	public static final int CANNT_BATTLE = 1;
	
	private static TimerNpcService _instance = new TimerNpcService();
	
	public static TimerNpcService getInstance() {
		return _instance;
	}
	
	//放到副本相关的日志文件里了
	public static Logger logger = Logger.getLogger("INSTANCEZONE");
	
	private Map<Integer, TimerNpcData> TIMERNPCDATA = new HashMap<Integer, TimerNpcData>();
	
	Map<Integer, SRefreshTimerNpc> npcConfigData = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.npc.SRefreshTimerNpc.class);
	
	private TimerNpcService() {
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) {
			for (SRefreshTimerNpc timernpc : npcConfigData.values()) {
				TimerNpcData tData = new TimerNpcData(timernpc);
				TIMERNPCDATA.put(timernpc.getId(), tData);
			}
		}
		else if (fire.pb.fushi.Module.GetPayServiceType() == 1) {
			Map<Integer, SRefreshTimerNpcDianKa> npcConfigDianKaData = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.npc.SRefreshTimerNpcDianKa.class);
			for (SRefreshTimerNpcDianKa timernpc : npcConfigDianKaData.values()) {
				TimerNpcData tData = new TimerNpcData(timernpc);
				TIMERNPCDATA.put(timernpc.getId(), tData);
			}
		}
	}
	
	/**
	 * 活动开始
	 */
	public void activityStart(final long startTime, final long endTime, final int actId, final int baseId) {
//		if (actStarted.containsKey(baseId)) {
//			boolean isStart = actStarted.get(baseId);
//			if (isStart)
//				return;
//		}
//		else {
//			actStarted.put(baseId, true);
//		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("开始刷怪").append(actId).append(" ").append(baseId);
		logger.info(sb.toString());
		//设置actinfo
		ActivityInfo info = null;
		if (!actInfos.containsKey(actId)) {
			info = new ActivityInfo();
			actInfos.put(actId, info);
		} else {
			info = actInfos.get(actId);
		}

		info.setId(actId);
		info.setBaseId(baseId);
		info.setStartTime(startTime);
		info.setEndTime(endTime);
		
		TimerNpcData timerData = TIMERNPCDATA.get(actId);
		if (timerData == null) {
			logger.error("activityStart, TimerNpcData 信息为空: " + actId);
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		String[] hhmmss = timerData.startTime.split(":");
		if (hhmmss.length != 3)
			return;
		int hour = Integer.parseInt(hhmmss[0]);
		int min = Integer.parseInt(hhmmss[1]);
		int sec = Integer.parseInt(hhmmss[2]);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		
		final long realStartTime = cal.getTimeInMillis();
		long passtime = 0;
		
		//建立刷怪的定时任务
		TimerNpcTask task = new TimerNpcTask(actId);
		
		final long now = System.currentTimeMillis();
		if (now > realStartTime) { //开始时间已过
			passtime = now - realStartTime;
		}
		
		if (now != realStartTime) {
			mkdb.Executor.getInstance().schedule(task, 1, TimeUnit.SECONDS);
		}
		
		logger.info(new StringBuffer().append("now time:").append(now).append(" real start time:").append(realStartTime).append(" actid:").append(actId));
		
		if (timerData.firstRefreshTime > 0) { //第一次刷怪有延时
			if (timerData.intervalRefresh > 0) { //有间隔的刷新
				if (now > realStartTime + timerData.firstRefreshTime*1000) {
					//不延时,已经过了延时时间了
					final long lasttime = realStartTime + (timerData.intervalRefresh*1000)*(passtime/(timerData.intervalRefresh*1000));
					final long sdelay = lasttime + (timerData.intervalRefresh*1000) - now;
					logger.info("delay time1:" + sdelay + " actid:" + actId);
					ScheduledFuture<?> actTask = mkdb.Mkdb.executor().scheduleAtFixedRate(task, sdelay, timerData.intervalRefresh*1000, TimeUnit.MILLISECONDS);
					actFuture.put(actId, actTask);
				}
				else {
					final long lasttime = realStartTime + (timerData.intervalRefresh*1000)*(passtime/(timerData.intervalRefresh*1000)) + timerData.firstRefreshTime*1000;
					final long sdelay = lasttime + (timerData.intervalRefresh*1000) - now;
					logger.info("delay time2:" + sdelay + " actid:" + actId);
					ScheduledFuture<?> actTask = mkdb.Mkdb.executor().scheduleAtFixedRate(task, sdelay, timerData.intervalRefresh*1000, TimeUnit.MILLISECONDS);
					actFuture.put(actId, actTask);
				}
			}
			else { //只刷新一次
				mkdb.Executor.getInstance().schedule(task, timerData.firstRefreshTime/60, TimeUnit.MINUTES);
			}
		}
		else {
			if (timerData.intervalRefresh > 0) {//有间隔的刷新
				final long lasttime = realStartTime + (timerData.intervalRefresh*1000)*(passtime/(timerData.intervalRefresh*1000));
				long sdelay = 0;
				if (lasttime != now) {
					sdelay = lasttime + (timerData.intervalRefresh * 1000) - now;
				}
				logger.info("delay time3:" + sdelay + " actid:" + actId);
				ScheduledFuture<?> actTask = mkdb.Mkdb.executor().scheduleAtFixedRate(task, sdelay, timerData.intervalRefresh*1000, TimeUnit.MILLISECONDS);
				actFuture.put(actId, actTask);
			}
			else {//只刷新一次
				mkdb.Executor.getInstance().schedule(task, 60, TimeUnit.SECONDS);
			}
		}
	}
	
	/**
	 * 检查活动是否开启
	 * @param id
	 * @return
	 */
	public boolean isActStart(int actId){
		//活动信息里没有,说明活动已经关闭了
		if(!actInfos.containsKey(actId)){
			return false;
		}
		ActivityInfo info = actInfos.get(actId);
		if(info == null){
			return false;
		}
		//不在活动时间内,不刷新
		long now = System.currentTimeMillis();
		if(now < info.getStartTime() || now > info.getEndTime()){
			return false;
		}
		return true;
	}
	
	/**
	 * 处理隔段时间刷新怪的逻辑
	 * @param actId
	 */
	public void refreshTimerNpc(int actId) {
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actId);
		if (npcInfo != null) {
			//每次刷新都要先清怪
			this.clearMonster(npcInfo);
		}
		
		if (npcInfo == null){
			npcInfo = xbean.Pod.newtimerNpcInfoCol();
			xtable.Timernpcinfotable.insert(actId, npcInfo);
		}
		
		TimerNpcData timerData = TIMERNPCDATA.get(actId);
		if (timerData == null) {
			logger.error("refreshTimerNpc, TimerNpcData 信息为空: " + actId);
			return;
		}
		
		ActivityInfo info = null;
		if(!actInfos.containsKey(actId)){
			return;
		}else{
			info = actInfos.get(actId);
		}
		
		Map<Integer, List<Integer>> npcMap = timerData.getNpcMap();
		info.setMapInfo(npcMap);
		
		for (Map.Entry<Integer, List<Integer>> entry : npcMap.entrySet()) {
			List<Integer> npcList = entry.getValue();
			int mapId = entry.getKey();
			
			for (Integer npcId : npcList) {
				SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(npcId);
				String npcname = npc.getName();
				if (npcname.length() == 0) {
					logger.error("大萌萌没有名字");
				}
				
				long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();
				xbean.timerNpcInfo tInfo = npcInfo.getNpcinfo().get(npcKey);
				if (tInfo == null) {
					tInfo = xbean.Pod.newtimerNpcInfo();
				}
				tInfo.setNpcid(npcId);
				tInfo.setNpcstatus(0);
				npcInfo.getNpcinfo().put(npcKey, tInfo);
				fire.pb.map.SceneNpcManager.createNpcByMap(npcKey, npcId, npcname, mapId, 3, timerData.npcLastTime*1000);
				System.out.println("生成大萌萌定时任务NPC" + npcname + "地图" + mapId);
				
				npc2act.put(npcId, actId);
			}
		}
		
		//建立下次刷怪的定时任务
		
	}
	
	/**
	 * 在定时器里定时判断是怪物数量是否需要补充
	 * @param actId
	 */
	public void addTimerNpc(int actId) {
		StringBuilder strtmp = new StringBuilder();
		strtmp.append("开始补怪,任务:").append(actId).append(" 时间:").append(CalendarUtil.getCurrentStringFormat2Second());
		logger.info(strtmp.toString());
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actId);
		if (npcInfo == null) {
			logger.error("timerNpcInfoCol 信息为空: " + actId);
			return;
		}
		
		TimerNpcData timerData = TIMERNPCDATA.get(actId);
		if (timerData == null) {
			logger.error("TimerNpcData 信息为空: " + actId);
			return;
		}
		
		ActivityInfo activityInfo = null;
		if (!actInfos.containsKey(actId)) {
			logger.error("ActivityInfo 信息为空: " + actId);
			return;
		}

		activityInfo = actInfos.get(actId);
		
		List<Long> removeNpc = new ArrayList<Long>();
		int nAddCnt = 0;
		for (Map.Entry<Long, xbean.timerNpcInfo> npcinfos : npcInfo.getNpcinfo().entrySet()) {
			//只有当怪物状态不为0时才进行补怪,防止一下子很多怪都进入战斗中,没有空闲的怪物
			if (npcinfos.getValue().getNpcstatus() == 0) { //没有消失
				continue;
			}
			
			if (npcinfos.getValue().getNpcstatus() == 2) {
				//怪物已经消失,从列表中删除
				removeNpc.add(npcinfos.getKey());
			}
			
			nAddCnt ++;
		}
		
		for (Long npcId : removeNpc) {
			npcInfo.getNpcinfo().remove(npcId);
		}
		
		if (nAddCnt == 0) return;
		
		if ((nAddCnt + npcInfo.getNpcinfo().size()) > timerData.maxNpcCount) {
			nAddCnt = timerData.maxNpcCount - npcInfo.getNpcinfo().size();
			if (nAddCnt == 0) return;
		}
		
		logger.info(new StringBuffer().append("补怪数量:").append(nAddCnt));
		List<Integer> npcList = activityInfo.getRndNpcId(nAddCnt);
		
		for(int npcId : npcList) {
			SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(npcId);
			String npcname = npc.getName();
			if (npcname.length() == 0) {
				logger.error("大萌萌没有名字");
			}
			
			int mapId = activityInfo.getRndMapId();
			if (mapId == 0) return;
			
			long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();
			xbean.timerNpcInfo info = npcInfo.getNpcinfo().get(npcKey);
			if (info == null) {
				info = xbean.Pod.newtimerNpcInfo();
			}
			info.setNpcid(npcId);
			info.setNpcstatus(0);
			npcInfo.getNpcinfo().put(npcKey, info);
			fire.pb.map.SceneNpcManager.createNpcByMap(npcKey, npcId, npcname, mapId, 3, timerData.npcLastTime*1000);
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("补充大萌萌定时任务NPC:").append(npcname).append(" 地图:").append(mapId);
			strBuilder.append(" 活动Id:").append(actId);
			strBuilder.append(" 活动类型Id:").append(activityInfo.getBaseId());
			
			logger.info(strBuilder.toString());
		}
		
		npc2act.putAll(timerData.npc2act);
	}
	
	/**
	 * 在定时器里定时判断是怪物数量是否需要补充
	 * @param actId
	 */
	public void addTimerNpc(int actId, int npccnt, final int mapid) {
		StringBuilder strtmp = new StringBuilder();
		strtmp.append("开始补怪,任务:").append(actId).append(" 时间:").append(CalendarUtil.getCurrentStringFormat2Second());
		logger.info(strtmp.toString());
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actId);
		if (npcInfo == null) {
			logger.error("timerNpcInfoCol 信息为空: " + actId);
			return;
		}
		
		TimerNpcData timerData = TIMERNPCDATA.get(actId);
		if (timerData == null) {
			logger.error("TimerNpcData 信息为空: " + actId);
			return;
		}
		
		ActivityInfo activityInfo = null;
		if (!actInfos.containsKey(actId)) {
			logger.error("ActivityInfo 信息为空: " + actId);
			return;
		}

		activityInfo = actInfos.get(actId);
		
		List<Long> removeNpc = new ArrayList<Long>();
		for (Map.Entry<Long, xbean.timerNpcInfo> npcinfos : npcInfo.getNpcinfo().entrySet()) {
			//只有当怪物状态不为0时才进行补怪,防止一下子很多怪都进入战斗中,没有空闲的怪物
			if (npcinfos.getValue().getNpcstatus() == 0) { //没有消失
				continue;
			}
			
			if (npcinfos.getValue().getNpcstatus() == 2) {
				//怪物已经消失,从列表中删除
				removeNpc.add(npcinfos.getKey());
			}
		}
		
		for (Long npcId : removeNpc) {
			npcInfo.getNpcinfo().remove(npcId);
		}
		
		logger.info(new StringBuffer().append("补怪数量:").append(npccnt));
		List<Integer> npcList = activityInfo.getRndNpcId(npccnt);
//		int mapId = activityInfo.getRndMapId();
//		if (mapId == 0) return;
		
		for(int npcId : npcList) {
			SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(npcId);
			String npcname = npc.getName();
			if (npcname.length() == 0) {
				logger.error("大萌萌没有名字");
			}
			
			long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();
			xbean.timerNpcInfo info = npcInfo.getNpcinfo().get(npcKey);
			if (info == null) {
				info = xbean.Pod.newtimerNpcInfo();
			}
			info.setNpcid(npcId);
			info.setNpcstatus(0);
			npcInfo.getNpcinfo().put(npcKey, info);
			fire.pb.map.SceneNpcManager.createNpcByMap(npcKey, npcId, npcname, mapid, 3, timerData.npcLastTime*1000);
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("补充大萌萌定时任务NPC:").append(npcname).append(" 地图:").append(mapid);
			strBuilder.append(" 活动Id:").append(actId);
			strBuilder.append(" 活动类型Id:").append(activityInfo.getBaseId());
			
			logger.info(strBuilder.toString());
		}
		
		npc2act.putAll(timerData.npc2act);
	}
	
	public void createTimerNpc(int actId) {
		StringBuilder strtmp = new StringBuilder();
		strtmp.append("开始刷新一轮的怪,任务:").append(actId).append(" 时间:").append(CalendarUtil.getCurrentStringFormat2Second());
		logger.info(strtmp.toString());
		
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actId);
		if (npcInfo != null) {
			//每次刷新都要先清怪
			this.clearMonster(npcInfo);
		}
		
		if (npcInfo == null){
			npcInfo = xbean.Pod.newtimerNpcInfoCol();
			xtable.Timernpcinfotable.insert(actId, npcInfo);
		}
		
		TimerNpcData timerData = TIMERNPCDATA.get(actId);
		if (timerData == null) {
			logger.error("timerData 信息为空: " + actId);
			return;
		}
		
		ActivityInfo activityInfo = null;
		if (!actInfos.containsKey(actId)) {
			logger.error("activityInfo 信息为空: " + actId);
			return;
		}
		else {
			activityInfo = actInfos.get(actId);
		}
		
		Map<Integer, List<Integer>> npcMap = timerData.getNpcMap();
		activityInfo.setMapInfo(npcMap);
		
		List<Integer> mapIdList = new ArrayList<Integer>();
		
		for (Map.Entry<Integer, List<Integer>> entry : npcMap.entrySet()) {
			List<Integer> npcList = entry.getValue();
			int mapId = entry.getKey();
			mapIdList.add(mapId);
			
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("活动Id:").append(actId);
			strBuilder.append(",活动类型Id:").append(activityInfo.getBaseId());
			strBuilder.append(",在地图:").append(mapId).append(",共刷出npc数量为:")
			.append(npcList.size());
			logger.info(strBuilder.toString());
			
			for (Integer npcId : npcList) {
				SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(npcId);
				if (npc == null) {
					logger.error("npc没有配置 --------------------------:" + npcId);
					continue;
				}
				
				String npcname = fire.pb.npc.NpcManager.getInstance().getNpcName(npc);
				if (npcname.length() == 0) {
					npcname = npc.getName();
					logger.error("大萌萌没有名字");
				}
				
				long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();
				xbean.timerNpcInfo tInfo = npcInfo.getNpcinfo().get(npcKey);
				if (tInfo == null) {
					tInfo = xbean.Pod.newtimerNpcInfo();
				}
				tInfo.setNpcid(npcId);
				tInfo.setNpcstatus(0);
				tInfo.setCreatetime(System.currentTimeMillis());
				
				npcInfo.getNpcinfo().put(npcKey, tInfo);
				fire.pb.map.SceneNpcManager.createNpcByMap(npcKey, npcId, npcname, mapId, 3, timerData.npcLastTime*1000);
				
//				StringBuilder strBuilder = new StringBuilder();
//				strBuilder.append("生成大萌萌定时任务NPC:").append(npcname).append(" 地图:").append(mapId);
//				strBuilder.append(" 活动Id:").append(actId);
//				strBuilder.append(" 活动类型Id:").append(activityInfo.getBaseId());
//				
//				logger.info(strBuilder.toString());
				npc2act.put(npcId, actId);
				
				if (timerData.match == 1) {
					//创建定时器,匹配进入战斗的角色
					mkdb.Executor.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							new PEnterNpcBattle(npcId, npcKey, actId).submit();
						}
					}, timerData.matchsec, TimeUnit.SECONDS);
				}
			}
		}
		
		npc2act.putAll(timerData.npc2act);
		
		if (timerData.freshType == 1) {
			if (actAddFuture.containsKey(actId)) {
				ScheduledFuture<?> endFuture = actAddFuture.get(actId);
				if (endFuture != null) {
					endFuture.cancel(false);
				}
			}
			
			//建立补怪的定时任务
			ScheduledFuture<?> addTask = mkdb.Mkdb.executor().scheduleAtFixedRate(new TimerNpcAddTask(actId), 1, 1, TimeUnit.MINUTES);
			actAddFuture.put(actId, addTask);
			
			mkdb.Executor.getInstance().schedule(new StopAddNpcTask(actId), timerData.addNpcTime + 10, TimeUnit.SECONDS);
		}
		
		List<String> param = new ArrayList<String>(1);
		StringBuffer strParam = new StringBuffer();
		int nIndex = 0;
		for (Integer mapId : mapIdList) {
			MapConfig conf =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class).get(mapId);
			if (conf != null) {
				if (nIndex != (mapIdList.size() - 1)) {
					strParam.append(conf.getMapName()).append(",");
				}
				else {
					strParam.append(conf.getMapName());
				}
				nIndex ++;
//				param.add(conf.getMapName());
			}
		}
		param.add(strParam.toString());
		SceneManager.sendAll(MessageMgr.getMsgNotify(timerData.messageId, 0, param));
	}
	
	public void createTimerNpcByData(final EventTimerGroupData timerData, final int noticeId, final long roleid) {
		PCreateTimerNpcByData createNpc = new PCreateTimerNpcByData(timerData, noticeId, roleid);
		if (mkdb.Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(createNpc);
		}
		else {
			createNpc.submit();
		}
	}
	
	public void createBestowTimerNpc(long roleId, String rolename, final EventTimerGroupData timerData) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.eventNpcInfoCol npcInfo = xtable.Eventnpcinfotable.get(timerData.id);
				if (npcInfo == null) {
					npcInfo = xbean.Pod.neweventNpcInfoCol();
					xtable.Eventnpcinfotable.insert(timerData.id, npcInfo);
				}
				
				Map<Integer, List<Integer>> npcMap = timerData.getNpcMap();	
				for (Map.Entry<Integer, List<Integer>> entry : npcMap.entrySet()) {
					List<Integer> npcList = entry.getValue();
					int mapId = entry.getKey();
					
					for (Integer npcId : npcList) {
						String npcname = rolename + "的宝箱";
						if (npcname.length() == 0) {
							logger.error("大萌萌没有名字");
						}
						
						long npcKey = fire.pb.map.SceneNpcManager.getInstance().getNextId();
						
						xbean.eventNpcInfo eInfo = npcInfo.getNpcinfo().get(npcKey);
						if (eInfo == null) {
							eInfo = xbean.Pod.neweventNpcInfo();
						}
						eInfo.setNpcid(npcId);
						eInfo.setNpcstatus(0);
						eInfo.setBattleendtime(timerData.battletime);
						eInfo.setBattletime(timerData.battletime);

						npcInfo.getNpcinfo().put(npcKey, eInfo);
						
						fire.pb.map.SceneNpcManager.createNpcByMap(npcKey, npcId, npcname, mapId, 3, timerData.npcLastTime*1000);
						
						xbean.BestowNpcInfo bestownpcInfo = xbean.Pod.newBestowNpcInfo();
						bestownpcInfo.setRoleid(roleId);
						xtable.Bestownpc.insert(npcKey, bestownpcInfo);
							
						StringBuilder strBuilder = new StringBuilder();
						strBuilder.append("生成事件触发NPC:").append(npcname).append(" 地图:").append(mapId);
						
						logger.info(strBuilder.toString());
					}
				}
				npc2eventId.putAll(timerData.npc2eventId);
				
				return true;
			}	
		}.submit();	
	}
	
	public int getTeamNumberCount(int actId) {
		TimerNpcData timerData = TIMERNPCDATA.get(actId);
		if (timerData == null)
			return 0;
		
		if (timerData.teamReq == 0)
			return 0;
		
		return timerData.teamCount;
	}
	
	/**
	 * 通过npcId获取事件id
	 * @param npcId
	 * @return
	 */
	public int getEventId(int npcId) {
		if (npc2eventId.containsKey(npcId)) {
			return npc2eventId.get(npcId);
		}
		
		return -1;
	}
	
	/**
	 * 返回npc是否事件刷怪npc
	 * @param npcId
	 * @return
	 */
	public boolean isEventNpcId(int npcId) {
		if (npc2eventId.containsKey(npcId)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 通过npcId获取活动id
	 * @param npcId
	 * @return
	 */
	public int getActId(int npcId) {
		Integer actId = npc2act.get(npcId);
		if (actId != null) {
			return actId;
		}
		return -1;
	}
	
	/**
	 * 返回npc是否定时刷怪npc
	 * @param npcId
	 * @return
	 */
	public boolean isTimerNpc(int npcId) {
		if (npc2act.containsKey(npcId))
			return true;
		return false;
	}
	
	/**
	 * 更新怪物状态
	 * @param npcInfo
	 * @param status
	 */
	public void changeNpcStatus(xbean.timerNpcInfoCol npcInfo, int status, long npcKey) {
		if (npcInfo.getNpcinfo().containsKey(npcKey)) {
			xbean.timerNpcInfo tInfo = npcInfo.getNpcinfo().get(npcKey);
			tInfo.setNpcstatus(status);
		}
	}
	
	/**
	 * 删除怪物
	 * @param npcInfo
	 * @param npcKey
	 */
	public void removeMonster(xbean.timerNpcInfoCol npcInfo,long npcKey){
		if(npcInfo.getNpcinfo().containsKey(npcKey)){
			SceneNpcManager.premoveNpcWhileCommit(npcKey);
			npcInfo.getNpcinfo().get(npcKey).setNpcstatus(2); //设置npc状态为已经消失
		}
	}
	
	/**
	 * 清除所有怪物
	 * @param npcInfo
	 */
	public void clearMonster(xbean.timerNpcInfoCol npcInfo){
		if(npcInfo.getNpcinfo().size() > 0){
			for(long npcKey : npcInfo.getNpcinfo().keySet()){
				SceneNpcManager.removeNpc(npcKey);
			}
		}
		npcInfo.getNpcinfo().clear();
	}
	
	/**
	 * 根据活动和npcId获取奖励id
	 * @param actId
	 * @param npcId
	 * @return
	 */
	public int getAwardId(int actId,int npcId) {
		TimerNpcData timerData = TIMERNPCDATA.get(actId);
		if (timerData != null) {
			return timerData.awardId;
		}
		return -1;
	}
	
	/**
	 * 活动结束,清除活动信息
	 * @param id
	 */
	public void activityStop(int actId) {
		if (actAddFuture.containsKey(actId)) {
			ScheduledFuture<?> endFuture = actAddFuture.get(actId);
			if (endFuture != null) {
				endFuture.cancel(true);
			}
		}
		
		if (actFuture.containsKey(actId)) {
			ScheduledFuture<?> future = actFuture.get(actId);
			if (future != null) {
				future.cancel(true);
			}
		}
		
		actInfos.remove(actId);
		
		List<Integer> npcIds = new ArrayList<Integer>();
		for (Integer npcId : npc2act.keySet()) {
			Integer idval = npc2act.get(npcId);
			if (idval != null) {
				if (idval == actId) {
					npcIds.add(npcId);
				}
			}
		}
		
		for (Integer npcId : npcIds) {
			npc2act.remove(npcId);
		}
		
		new PTimerNpcActivityEndProc(actId).submit();
		logger.info("TimerNpcActivity end actId=" + actId);
	}
	
	public void stopAddNpcTask(int actId) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("结束补怪任务:").append(actId).append(" 时间:").append(CalendarUtil.getCurrentStringFormat2Second());
		logger.info(strBuilder.toString());
		if (actAddFuture.containsKey(actId)) {
			ScheduledFuture<?> endFuture = actAddFuture.get(actId);
			if (endFuture != null) {
				endFuture.cancel(false);
			}
		}
	}
	
	public TimerNpcData getNpcData(int actId) {
		TimerNpcData timerData = TIMERNPCDATA.get(actId);
		if (timerData == null)
			return null;
		
		return timerData;
	}
	
	public Map<Integer, TimerNpcData> getTimerData() {
		return this.TIMERNPCDATA;
	}
	
	public ActivityInfo getActiveInfo(int actId) {
		if (!actInfos.containsKey(actId)) {
			return null;
		} else {
			return actInfos.get(actId);
		}
	}
	
	/**
	 * 设置事件npc现在的状态
	 * @param npckey
	 * @param state 0表示失败, 1战斗中, 2战斗胜利 
	 */
	public void setEventNpcState(xbean.eventNpcInfoCol npcInfo, Long npckey, long roleid, int state, int xiaoshi) {
		if (npcInfo == null)
			return;
		
		xbean.eventNpcInfo eInfo = npcInfo.getNpcinfo().get(npckey);
		if (eInfo == null)
			return;
		
		if (state == LOSE_BATTLE)
			eInfo.setBattletime(eInfo.getBattletime() + 1);
		else if (state == IN_BATTLE) {
			eInfo.setBattletime(eInfo.getBattletime() - 1);
			if (eInfo.getBattletime() <= 0) {
				if (xiaoshi == 1) {//怪物消失
					npcInfo.getNpcinfo().remove(npckey);
					SceneNpcManager.removeNpc(npckey);
				}
				else {
					GsClient.pSendWhileCommit(new GSetNpcBattleInfo(npckey, 1));
				}
			}
		}
		else if (state == WIN_BATTLE) {
			eInfo.setBattleendtime(eInfo.getBattleendtime() - 1);
			
			//放到此处删除npc
			if (eInfo.getBattleendtime() <= 0) {
				npcInfo.getNpcinfo().remove(npckey);
				SceneNpcManager.removeNpc(npckey);
			}
		}
		
		eInfo.setNpcstatus(state);
		eInfo.setRoleid(roleid);
	}
	
	/**
	 * 获取事件npc的状态
	 * @param npckey
	 * @return 返回1时表示Npc不能再战斗
	 */
	public int getEventNpcState(xbean.eventNpcInfoCol npcInfo, long npckey) {
		xbean.eventNpcInfo eInfo = npcInfo.getNpcinfo().get(npckey);
		if (eInfo == null)
			return CANNT_BATTLE;
		
		if (eInfo.getBattletime() <= 0)
			return CANNT_BATTLE;
		else
			return CAN_BATTLE;
	}
	
	/**
	 * 获取事件npc的战斗角色id
	 * @param npckey
	 * @return
	 */
	public long getEventNpcBattleRole(int groupid, long npckey) {
		xbean.eventNpcInfoCol npcInfo = xtable.Eventnpcinfotable.select(groupid);
		if (npcInfo == null)
			return -1;
		
		xbean.eventNpcInfo eInfo = npcInfo.getNpcinfo().get(npckey);
		if (eInfo == null)
			return -1;

		return eInfo.getRoleid();
	}
	
	/**
	 * 删除一个Npc
	 * @param npckey
	 */
	public void removeEventNpc(int groupid, long npckey) {
		xbean.eventNpcInfoCol npcInfo = xtable.Eventnpcinfotable.get(groupid);
		if (npcInfo == null)
			return;
		
		xbean.eventNpcInfo eInfo = npcInfo.getNpcinfo().get(npckey);
		if (eInfo == null)
			return;
		
		if (eInfo.getBattleendtime() <= 0) {
			npcInfo.getNpcinfo().remove(Long.valueOf(npckey));
			SceneNpcManager.premoveNpcWhileCommit(npckey);
		}
	}
	
	/**
	 * 获取现在事件怪的数量
	 */
	public int getEventNpcSize() {
		final Set<Long> npcInfos = new HashSet<Long>();
		try
		{
			new mkdb.Procedure()
			{
				protected boolean process() throws Exception {
					TTableCache<Integer, eventNpcInfoCol> btable = xtable.Eventnpcinfotable.getCache();
					btable.walk(new mkdb.CacheQuery<Integer, xbean.eventNpcInfoCol>()
					{
						@Override
						public void onQuery(Integer k, xbean.eventNpcInfoCol v)
						{
							npcInfos.addAll(v.getNpcinfo().keySet());
						}

					});
					return true;
				};
			}.submit().get();
			
			return npcInfos.size();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 重新刷新活动
	 */
	public void resetAllActNpc(int giftid){
		int useActId = -1;
		for (Integer actId : actInfos.keySet()) {
			useActId = actId;
			if(actId >= 200001 && actId <= 200007){
				break;
			}else if (actId >= 201001 && actId <= 201007){
				break;
			}else if(actId >= 201008 && actId <= 201014){
				break;
			}else {
				continue;
			}
		}
		
		if(useActId != -1){
			createTimerNpc(useActId);
		}
	}

	/**
	 * GM刷新活动
	 */
	public boolean GMResetActNpc(int needActId) {
		int useActId = -1;
		switch (needActId) {
		case 4:
			needActId = 201;
			break;
		case 5:
			needActId = 251;
			break;
		case 6:
			needActId = 261;
			break;
		}
		
		for (Integer actId : actInfos.keySet()) {
			int type = actId / 1000;
			if(needActId == type){
				useActId = actId;
				break;
			}
		}

		if (useActId != -1) {
			createTimerNpc(useActId);
			return true;
		}
		
		return false;
	}
	
	public static void addCTDynamicServices(long roleid, int npcid, long npckey, java.util.List<Integer> result) {
		NPC npc = fire.pb.scene.manager.SceneNpcManager.getInstance().getNpcByKey(npckey);
		if (npc == null)
			return;
		
		if (npc.checkSceneState(SceneState.BATTLE_FIGHTER)) {
			if (TimerNpcService.getInstance().isTimerNpc(npcid)) {
				int actId = TimerNpcService.getInstance().getActId(npcid);
				//活动错误
				if (actId == -1) {
					return;
				}
				
				TimerNpcData timerData = TimerNpcService.getInstance().getNpcData(actId);
				if (timerData == null)
					return;
				
				if (timerData.canwatchbattle == 1) {
					result.add(Integer.valueOf(NpcServices.WATCH_NPC_BATTLE));
				}
			}
			else if (TimerNpcService.getInstance().isEventNpcId(npcid)) {
				int biaoId = TimerNpcService.getInstance().getEventId(npcid);
				//事件错误
				if (biaoId == -1) {
					return;
				}
				
				EventTimerNpc eventTimer = BaoTuMapManager.getInstance().getTimerNpc(biaoId);
				if (eventTimer == null)
					return;
				
				if (eventTimer.canwatchbattle == 1) {
					result.add(Integer.valueOf(NpcServices.WATCH_EVENTNPC_BATTLE));
				}
			}
		}
		else {
			if (TimerNpcService.getInstance().isTimerNpc(npc.getNpcID())) {
				result.add(Integer.valueOf(NpcServices.TIMER_NPC_Battle));
			}
			else if (TimerNpcService.getInstance().isEventNpcId(npc.getNpcID())) {
				result.add(Integer.valueOf(NpcServices.EVENT_NPC_Battle));
			}
		}
	}
	
	/**
	 * 检查npc战斗是否还有奖励次数
	 * @return
	 */
	public static boolean checkBattleAwardTimes(long roleid, int actId) {
		TimerNpcData timerData = TimerNpcService.getInstance().getNpcData(actId);
		if (timerData == null)
			return false;
		
		ActivityInfo activeInfo = TimerNpcService.getInstance().getActiveInfo(actId);
		if (activeInfo == null)
			return false;
		int awardcnt = timerData.awardCnt;
		
		//分级次数
		int rolelevel = xtable.Properties.selectLevel(roleid);
		TimesLevel timeLevel = timerData.getTimesLevel(rolelevel);
		if (timeLevel != null) {
			awardcnt = timeLevel.times;
		}
		
		//现在时间
		long now = System.currentTimeMillis();
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			xbean.RoleActiveTimerNpcInfo roleActiveInfo = xtable.Roletimernpcinfos.select(roleid);
			if (roleActiveInfo == null) {
				//没有打过
				return true;
			}
			
			xbean.RoleTimerNpcInfo roleTimerNpcActiveInfo = roleActiveInfo.getActinfo().get(activeInfo.getBaseId());
			if (roleTimerNpcActiveInfo == null) {
				//没有打过
				return true;
			}
			
			if (!DateValidate.inTheSameDay(now,	roleTimerNpcActiveInfo.getLastawardtime())) {
				//跨天了
				return true;
			}
			//高于10次不能获取奖励
			if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
				//次数够了
				return false;
			}
			return true;
		}
		else { //点卡服处理狩猎活动
			if (timerData.shoulie == 0) { //不是狩猎活动
				xbean.RoleActiveTimerNpcInfo roleActiveInfo = xtable.Roletimernpcinfos.select(roleid);
				if (roleActiveInfo == null) {
					return true;
				}
				
				xbean.RoleTimerNpcInfo roleTimerNpcActiveInfo = roleActiveInfo.getActinfo().get(activeInfo.getBaseId());
				if (roleTimerNpcActiveInfo == null) {
					return true;
				}
				
				if (!DateValidate.inTheSameDay(now,	roleTimerNpcActiveInfo.getLastawardtime())) {
					return true;
				}
				//高于10次不能获取奖励
				if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
					//次数够了
					return false;
				}
				return true;
			}
			else { //是狩猎活动,根据狩猎id记次
				xbean.RoleActiveTimerNpcInfo roleActiveInfo = xtable.Roletimernpcinfos.select(roleid);
				if (roleActiveInfo == null) {
					return true;
				}
				
				xbean.RoleTimerNpcInfo roleTimerNpcActiveInfo = roleActiveInfo.getActinfo().get(timerData.shoulie);
				if (roleTimerNpcActiveInfo == null) {
					return true;
				}
				
				if (!DateValidate.inTheSameDay(now,	roleTimerNpcActiveInfo.getLastawardtime())) {
					return true;
				}
				//高于10次不能获取奖励
				if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
					//次数够了
					return false;
				}
				return true;
			}
		}
	}
	
	/**
	 * 判断事件npc的奖励次数
	 * @param roleid
	 * @param biaoid
	 * @param eventId
	 * @param shoulie
	 * @return
	 */
	public static boolean checkEventNpcBattleTimes(EventTimerGroupData eventTimerGroupData, long roleid, int biaoid, int eventId) {
		EventTimerNpc eventTimer = BaoTuMapManager.getInstance().getTimerNpc(biaoid);
		if (eventTimer == null)
			return false;
		int awardcnt = eventTimer.awardCnt;
		int shoulie = 0;
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) { //点卡服
			shoulie = ((SEventTimerNpcDianKa)eventTimer).getShoulie();
		}
		
		//分级次数
		int rolelevel = xtable.Properties.selectLevel(roleid);
		TimesLevel timeLevel = eventTimerGroupData.getTimesLevel(rolelevel);
		if (timeLevel != null) {
			awardcnt = timeLevel.times;
		}
		
		//现在时间
		long now = System.currentTimeMillis();
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			 //普通服处理
			xbean.RoleEventNpcInfoColumn roleEventCol = xtable.Roleeventnpcinfos.select(roleid);
			if (roleEventCol == null) {
				return true;
			}
			
			xbean.RoleEventNpcInfo roleEventNpcInfo = roleEventCol.getEventinfo().get(eventId);
			if (roleEventNpcInfo == null) {
				return true;
			}
			
			if (!DateValidate.inTheSameDay(now, roleEventNpcInfo.getLastawardtime())) {
				return true;
			}
			
			//高于10次不能获取奖励
			if (roleEventNpcInfo.getAwardtimes() >= eventTimer.awardCnt) {
				return false;
			}
			
			return true;
		}
		else { // 点卡服
			if (shoulie == 0) {
				return true;
			}
			else {
				xbean.RoleActiveTimerNpcInfo roleActiveInfo = xtable.Roletimernpcinfos.select(roleid);
				if (roleActiveInfo == null) {
					return true;
				}
				
				xbean.RoleTimerNpcInfo roleTimerNpcActiveInfo = roleActiveInfo.getActinfo().get(shoulie);
				if (roleTimerNpcActiveInfo == null) {
					return true;
				}
				
				if (!DateValidate.inTheSameDay(now,	roleTimerNpcActiveInfo.getLastawardtime())) {
					return true;
				}
				
				//高于10次不能获取奖励
				if (roleTimerNpcActiveInfo.getAwardtimes() >= awardcnt) {
					return false;
				}
				
				return true;
			}
		}
	}
}



