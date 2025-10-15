package fire.pb.activity.timernpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.npc.RefreshTimerNpc;
import fire.pb.npc.SRefreshTimerNpc;
import fire.pb.npc.SRefreshTimerNpcDianKa;
import fire.pb.util.Misc;
import fire.script.AwardJSEngine;
import fire.script.JavaScript;

public class TimerNpcData {
	public final int id;
	public final int firstRefreshTime;
	public final String startTime;
	public final int intervalRefresh;
	public final int messageId;
	public final int freshType;
	public final int npcGroup;
	public final int maxNpcCount;
	public final String npcCount;
//	public final int npcRandomType;
	public final String npcAllCount;
	/**
	 * 1 为npc个控制，2为总数控制
	 */
	public final int freshControl;
	public final int sceneCnt;
//	public final int sceneRandomType;
	public final int addNpcTime;
	public final int npcLastTime;
	public final int awardCnt;
	public final int awardId;
	public final int awardOverStepNotice;
	public final int teamReq;
	public final int teamCount;
	public final int levelMin;
	public final int levelMax;
	public final int battletime;
	public final int addnpctype;
	public final int eventbiaoid;
	
	public final int canwatchbattle;
	public final int xiaoshi;
	public final int randomtrigger;
	
	public final int actid;
	
	public final int matchlevel;
	
	/**
	 * 打怪时是否需要跟其他玩家随机
	 */
	public final int match;
	
	public final int matchsec;
	
	/**
	 * 是否是狩猎,只有点卡服才有的行为
	 */
	public final int shoulie;
	
	/**
	 * 是否需要判断奖励次数
	 */
	public final int havetimes;
	
	public List<String> timingList;
	public List<TimerNpcGroupData> timerNpcGroupData;
	public List<Integer> sceneList;
	public List<Integer> sceneListRate;
	public List<LevelGroupAward> levelAward;
	public List<TimesLevel> timesLevel;
	/** 按npcId存储的活动id对应表,每次活动启动时修改该信息 */
	public Map<Integer,Integer> npc2act;
	
	public JavaScript npcCountScript;
	
	public final int delaySec;
	
	public TimerNpcData(final RefreshTimerNpc config) {
		RefreshTimerNpc npcConfig = null;
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) {  //免费服
			npcConfig = (SRefreshTimerNpc)config;
			this.shoulie = 0;
		}
		else { //点卡服
			npcConfig = (SRefreshTimerNpcDianKa)config;
			this.shoulie = ((SRefreshTimerNpcDianKa)npcConfig).getShoulie();
		}
		
		timingList = new ArrayList<String>();
		timerNpcGroupData = new ArrayList<TimerNpcGroupData>();
		timesLevel = new ArrayList<TimesLevel>();
		levelAward = new ArrayList<LevelGroupAward>();
		sceneList = new ArrayList<Integer>();
		sceneListRate = new ArrayList<Integer>();
		npc2act = new HashMap<Integer,Integer>();
		
		this.id = npcConfig.getId();
		this.startTime = npcConfig.getStartTime();
		this.firstRefreshTime = npcConfig.getFirstRefresh();
		this.intervalRefresh = npcConfig.getIntervalRefresh();
		this.messageId = npcConfig.getMessageId();
		this.freshType = npcConfig.getFreshType();
		this.npcGroup = npcConfig.getNpcGroup();
		this.maxNpcCount = npcConfig.getMaxNpcCount();
		this.npcCount = npcConfig.getNpcCount();
		this.npcAllCount = npcConfig.getNpcAllCount();
		this.freshControl = npcConfig.getFreshControl();
//		this.npcRandomType = npcConfig.getNpcRandomType();
		this.sceneCnt = npcConfig.getSceneCnt();
//		this.sceneRandomType = npcConfig.getSceneRandomType();
		this.addNpcTime = npcConfig.getAddNpcTime();
		this.npcLastTime = npcConfig.getNpcLastTime();
		this.awardCnt = npcConfig.getAwardCnt();
		this.awardId = npcConfig.getAwardId();
		this.awardOverStepNotice = npcConfig.getAwardOverStepNotice();
		this.teamReq = npcConfig.getTeamRequire();
		this.teamCount = npcConfig.getTeamCount();
		this.levelMin = npcConfig.getLevelMin();
		this.levelMax = npcConfig.getLevelMax();
		this.battletime = npcConfig.getBattletime();
		this.addnpctype = npcConfig.getAddnpctype();
		this.eventbiaoid = npcConfig.getEventbiaoid();
		this.canwatchbattle = npcConfig.getCanwatchbattle();
		this.xiaoshi = npcConfig.getXiaoshi();
		this.randomtrigger = npcConfig.getRandomtrigger();
		this.actid = npcConfig.getActid();
		
		this.matchlevel = npcConfig.getMatchlevel();
		this.match = npcConfig.getMatch();
		this.matchsec = npcConfig.getMatchtime();
		this.havetimes = npcConfig.getHavetimes();
		this.delaySec = npcConfig.getDelaysec();
		
		String [] strs = npcConfig.npcList.split(";");
		int nIndex = 0;
		for (String str : strs) {
			if (str != null) {
				TimerNpcGroupData groupData = new TimerNpcGroupData(nIndex, str);
				this.timerNpcGroupData.add(groupData);
			}
		}
		
		strs = npcConfig.sceneList.split(";");
		for (String str : strs) {
			if (str != null) {
				String[] tmp = str.split("@");
				this.sceneList.add(Integer.parseInt(tmp[0]));
				this.sceneListRate.add(Integer.parseInt(tmp[1]));
			}
		}
		
		if (npcConfig.levelaward != null && npcConfig.levelaward != "") {
			strs = npcConfig.levelaward.split(";");
			for (String str : strs) {
				String [] awardGroup = str.split(",");
				LevelGroupAward group = new LevelGroupAward(this.id, Integer.parseInt(awardGroup[0]), 
						Integer.parseInt(awardGroup[1]), Integer.parseInt(awardGroup[2]));
				levelAward.add(group);
			}
		}
		
		if (npcConfig.leveltimes != null && npcConfig.leveltimes != "") {
			strs = npcConfig.leveltimes.split(";");
			for (String str : strs) {
				String [] timelevel = str.split(",");
				TimesLevel group = new TimesLevel(this.id, Integer.parseInt(timelevel[0]), 
						Integer.parseInt(timelevel[1]), Integer.parseInt(timelevel[2]));
				timesLevel.add(group);
			}
		}
		
		if (this.freshControl == 2) {
			if (this.npcAllCount != null) {
				npcCountScript = new JavaScript(this.npcAllCount);
			}
		}
		else {
			if (this.npcCount != null) {
				npcCountScript = new JavaScript(this.npcCount);
			}
		}
	}
	
	public LevelGroupAward getLevelAward(final int level) {
		for (LevelGroupAward gp: levelAward) {
			if (level >= gp.levelmin && level <= gp.levelmax) {
				return gp;
			}
		}
		return null;
	}
	
	public TimesLevel getTimesLevel(int level) {
		for (TimesLevel gp : timesLevel) {
			if (level >= gp.minlevel && level <= gp.maxlevel)
				return gp;
		}
		return null;
	}
	
	/**
	 * 随机出在哪些场景里刷NPC
	 * @return
	 */
	public List<Integer> getSceneList() {
		//随机场景
		List<Integer> sceneTmpList = new ArrayList<Integer>();
		List<Integer> tmpRate = new ArrayList<Integer>();
		List<Integer> tmpMap = new ArrayList<Integer>();
		tmpRate.addAll(sceneListRate);
		tmpMap.addAll(sceneList);
		
		for (int i = 0; i < sceneCnt; i ++) {
			//在场景列表里随机出不同的场景
			int mapIndex = Misc.getProbability(tmpRate);
			int mapId = 0;
			if (mapIndex < tmpMap.size()) {
				mapId = tmpMap.get(mapIndex);
				sceneTmpList.add(mapId);
				
				tmpRate.remove(mapIndex);
				tmpMap.remove(mapIndex);
			}
		}
		
		return sceneTmpList;
	}
	
	/**
	 * 随机出哪些NPC会出场
	 * @return
	 */
	public List<Integer> getNpcList() {
		//随机NPC
		List<Integer> npcTmpList = new ArrayList<Integer>();
		List<TimerNpcGroupData> npcGroupDataList = new ArrayList<TimerNpcGroupData>();
		npcGroupDataList.addAll(timerNpcGroupData);
		
		//通过公式计算出npc的数量
		int intNpcCount = 0;
		Double value2 = npcCountScript.GetDoubleValue();
		if (value2 != null) {
			intNpcCount = value2.intValue();
		}
		else {
			final int val = fire.pb.map.RoleManager.getInstance().getRoles().size();
			AwardJSEngine aw = new AwardJSEngine();
			//服务器等级
			int serverLv = fire.pb.timer.Module.getServerLevel();
			aw.put("rolenum", val);
			aw.put("ServerLv", serverLv);
			Double value = npcCountScript.eval(aw, null, null);
			intNpcCount = value.intValue();
		}
		
		//NPC总数控制
		if (freshControl == 2) {
			if (intNpcCount <= 0)
				return npcTmpList;
			
			List<TimerNpcGroupData> npcTmpGroup = new ArrayList<TimerNpcGroupData>();
			for (int i = 0; i < npcGroup; i ++) {
				int nSize = npcGroupDataList.size();
				int nIndex = Misc.getRandomBetween(0, nSize - 1);
				if (nIndex < nSize) {
					npcTmpGroup.add(npcGroupDataList.get(nIndex));
					npcGroupDataList.remove(nIndex);
				}
			}
			
			//随机分配npc数量
			int npcCnt = intNpcCount / npcTmpGroup.size();
			for (TimerNpcGroupData tmpData : npcTmpGroup) {
				npcTmpList.addAll(Misc.getRandomList(tmpData.NpcList, npcCnt));
				
				if (npcTmpList.size() < npcCnt) {
					int nAddCnt = npcCnt - npcTmpList.size();
					for (int i = 0; i < nAddCnt; i ++) {
						int nIndex = Misc.getRandomBetween(0, tmpData.NpcList.size() - 1);
						if (nIndex < tmpData.NpcList.size()) {
							Integer npcidd = tmpData.NpcList.get(nIndex);
							npcTmpList.add(npcidd);
						}
					}
				}
			}
			
			int npcCntLast = intNpcCount % npcTmpGroup.size();
			if (npcCntLast > 0) {
				int nIndex = Misc.getRandomBetween(0, npcTmpGroup.size() - 1);
				if (nIndex < npcTmpGroup.size()) {
					TimerNpcGroupData tmpData = npcTmpGroup.get(nIndex);
					npcTmpList.addAll(Misc.getRandomList(tmpData.NpcList, npcCntLast));
				}
			}
		}
		else {
			//单个NPC数量控制
			for (int i = 0; i < npcGroup; i ++) {
				int nSize = npcGroupDataList.size();
				if (nSize == 0)
					return npcTmpList;
				int nIndex = Misc.getRandomBetween(0, nSize - 1);
				TimerNpcGroupData tmpData = npcGroupDataList.get(nIndex);
				for (Integer npcIdd : tmpData.NpcList) {
					for (int m = 0; m < intNpcCount; m ++) {
						npcTmpList.add(npcIdd);
					}
				}
				
				npcGroupDataList.remove(tmpData);
			}
		}
		
		for (Integer npcIdd : npcTmpList) {
			npc2act.put(npcIdd, this.id);
		}
		
		return npcTmpList;
	}
	
	/**
	 * 根据规则获取每个地图里的NPC
	 * Key为地图的id, Value为NPC的id
	 * @return
	 */
	public Map<Integer, List<Integer>> getNpcMap() {
		Map<Integer, List<Integer>> npcRetList = new HashMap<Integer, List<Integer>>();
		
		//NPC所在的场景
		List<Integer> sceneList = getSceneList();
		
		//都有哪些NPC会出场
		List<Integer> npcList = getNpcList();
		
		//计算每个场景里需要放多少个NPC
		int nMapNpcCnt = npcList.size() / sceneList.size();
		if (nMapNpcCnt > 0) {
			for (Integer sceneId : sceneList) {
				List<Integer> rnd = Misc.getRandomList(npcList, nMapNpcCnt);
				npcRetList.put(sceneId, rnd);
				for (Integer n : rnd) {
					npcList.remove(n);
				}
			}
			
			//分配剩余的NPC
			List<Integer> sceneTmpList = new ArrayList<Integer>();
			sceneTmpList.addAll(npcRetList.keySet());
			for (Integer n : npcList) {
				int nIndex = Misc.getRandomBetween(0, sceneTmpList.size() - 1);
				if (nIndex < sceneList.size()) {
					Integer sceneId = sceneTmpList.get(nIndex);
					if (sceneId != null) {
						List<Integer> npcLL = npcRetList.get(sceneId);
						if (npcLL != null) {
							npcLL.add(n);
						}
					}
				}
				
			}
		}
		else {
			int nIndex = Misc.getRandomBetween(0, sceneList.size() - 1);
			if (nIndex < sceneList.size()) {
				Integer sceneId = sceneList.get(nIndex);
				npcRetList.put(sceneId, npcList);
			}
		}
		
		return npcRetList;
	}
}
