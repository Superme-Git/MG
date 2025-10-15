package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.activity.timernpc.TimerNpcGroupData;
import fire.pb.activity.timernpc.TimesLevel;
import fire.pb.map.EventTimerNpc;
import fire.pb.map.SEventTimerNpc;
import fire.pb.map.SEventTimerNpcDianKa;
import fire.pb.util.Misc;

public class EventTimerGroupData {
	public final int id;
	public final int levelMin;
	public final int levelMax;
	public final int refreshGroup;
	public final int messageId;
	public final int npcGroupCount;
	public final int npcCount;
	/**
	 * 1 为npc个控制，2为总数控制
	 */
	public final int freshControl;
	public final int npcAllCount;
	public final int sceneCnt;
	public final int npcLastTime;
	public final int awardCnt;
	public final int awardId;
	public final int teamRequire;
	public final int teamCount;
	public final int teamLevelMin;
	public final int teamLevelMax;
	public final int battletime;
	public final int battlewintime;
	
	public final int catwatchbattle;
	public final int xiaoshi;
	public final int actid;
	
	public final int match;
	public final int matchsec;
	
	public final int delaysec;
	public final int delaynoticeid;
	
	/**
	 * 狩猎活动,点卡服才有,表示为1
	 */
	public final int shoulie;
	
	/**
	 * 是否需要判断奖励次数
	 */
	public final int havetimes;
	
	public List<Integer> sceneList;
	public List<Integer> sceneListRate;
	public List<TimerNpcGroupData> timerNpcGroupData;
	
	public List<TimesLevel> timesLevel;
	public Map<Integer, Integer> npc2eventId;
	
	public EventTimerGroupData(final EventTimerNpc config) {
		EventTimerNpc npcConfig = null;
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) {// 普通服
			npcConfig = (SEventTimerNpc)config;
			this.shoulie = 0;
		}
		else {
			npcConfig = (SEventTimerNpcDianKa)config;
			this.shoulie = ((SEventTimerNpcDianKa)npcConfig).getShoulie();
		}
		
		sceneList = new ArrayList<Integer>();
		sceneListRate = new ArrayList<Integer>();
		timerNpcGroupData = new ArrayList<TimerNpcGroupData>();
		timesLevel = new ArrayList<TimesLevel>();
		npc2eventId = new HashMap<Integer, Integer>();
		
		this.id = npcConfig.id;
		this.messageId = npcConfig.getMessageId();
		this.refreshGroup = npcConfig.getRefreshGroup();
		this.npcCount = npcConfig.getNpcCount();
		this.freshControl = npcConfig.getFreshControl();
		this.npcAllCount = npcConfig.getNpcAllCount();
		this.sceneCnt = npcConfig.getSceneCnt();
		this.npcLastTime = npcConfig.getNpcLastTime();
		this.awardCnt = npcConfig.getAwardCnt();
		this.awardId = npcConfig.getAwardId();
		this.teamRequire = npcConfig.getTeamRequire();
		this.teamCount = npcConfig.getTeamCount();
		this.levelMin = npcConfig.getLevelMin();
		this.levelMax = npcConfig.getLevelMax();
		this.teamLevelMin = npcConfig.getTeamLevelMin();
		this.teamLevelMax = npcConfig.getTeamLevelMax();
		this.npcGroupCount = npcConfig.getNpcGroupCount();
		this.battletime = npcConfig.getBattletime();
		this.battlewintime = npcConfig.getBattletime();
		this.catwatchbattle = npcConfig.getCanwatchbattle();
		this.xiaoshi = npcConfig.getXiaoshi();
		this.actid = npcConfig.getActid();
		
		this.match = npcConfig.getMatch();
		this.matchsec = npcConfig.getMatchtime();
		this.havetimes = npcConfig.getHavetimes();
		
		this.delaysec = npcConfig.getDelaysec();
		this.delaynoticeid = npcConfig.getDelaynoticeid();
		
		if (npcConfig.npcList != null) {
			String [] strs = npcConfig.npcList.split(";");
			int nIndex = 0;
			for (String str : strs) {
				if (str != null) {
					TimerNpcGroupData groupData = new TimerNpcGroupData(nIndex, str);
					this.timerNpcGroupData.add(groupData);
				}
			}
		}
		
		if (npcConfig.sceneList != null) {
			String [] strs = npcConfig.sceneList.split(";");
			for (String str : strs) {
				if (str != null) {
					String[] tmp = str.split("@");
					this.sceneList.add(Integer.parseInt(tmp[0]));
					this.sceneListRate.add(Integer.parseInt(tmp[1]));
				}
			}
		}
		
		if (npcConfig.leveltimes != null) {
			String [] strs = npcConfig.leveltimes.split(";");
			if (npcConfig.leveltimes != null && npcConfig.leveltimes != "") {
				for (String str : strs) {
					String [] timelevel = str.split(",");
					TimesLevel group = new TimesLevel(this.id, Integer.parseInt(timelevel[0]), 
							Integer.parseInt(timelevel[1]), Integer.parseInt(timelevel[2]));
					timesLevel.add(group);
				}
			}
		}
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
		
		//NPC总数控制
		if (freshControl == 2) {
			if (npcAllCount <= 0)
				return npcTmpList;
			
			List<TimerNpcGroupData> npcTmpGroup = new ArrayList<TimerNpcGroupData>();
			for (int i = 0; i < npcGroupCount; i ++) {
				int nIndex = Misc.getRandomBetween(0, npcGroupDataList.size() - 1);
				if (nIndex < npcGroupDataList.size()) { 
					npcTmpGroup.add(npcGroupDataList.get(nIndex));
					npcGroupDataList.remove(nIndex);
				}
			}
			
			//随机分配npc数量
			int npcCnt = npcAllCount / npcTmpGroup.size();
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
			
			int npcCntLast = npcAllCount % npcTmpGroup.size();
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
			for (int i = 0; i < npcGroupCount; i ++) {
				int nSize = npcGroupDataList.size();
				if (nSize == 0)
					return npcTmpList;
				int nIndex = Misc.getRandomBetween(0, nSize - 1);
				TimerNpcGroupData tmpData = npcGroupDataList.get(nIndex);
				for (Integer npcIdd : tmpData.NpcList) {
					for (int m = 0; m < npcCount; m ++) {
						npcTmpList.add(npcIdd);
					}
				}
				
				npcGroupDataList.remove(tmpData);
			}
		}
		
		for (Integer npcIdd : npcTmpList) {
			npc2eventId.put(npcIdd, this.id);
		}
		
		return npcTmpList;
	}
	
	public Map<Integer, List<Integer>> getNpcMap() {
		Map<Integer, List<Integer>> npcRetList = new HashMap<Integer, List<Integer>>();
		
		//随机场景
		List<Integer> sceneList = getSceneList();
		
		//随机NPC
		List<Integer> npcList = getNpcList();
		
		//计算每个场景里需要放多少个NPC
		if (npcList.size() > sceneList.size()) {
			int nMapNpcCnt = npcList.size() / sceneList.size();
			for (Integer sceneId : sceneList) {
				List<Integer> rnd = Misc.getRandomList(npcList, nMapNpcCnt);
				npcRetList.put(sceneId, rnd);
				for (Integer n : rnd) {
					npcList.remove(n);
				}
			}
		}
		else {
			for (Integer sceneId : sceneList) {
				if (npcList.size() == 0)
					break;
				List<Integer> rnd = Misc.getRandomList(npcList, 1);
				npcRetList.put(sceneId, rnd);
				for (Integer n : rnd) {
					npcList.remove(n);
				}
			}
		}
		
		return npcRetList;
	}
}
