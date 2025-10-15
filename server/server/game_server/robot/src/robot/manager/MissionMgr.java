package robot.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import robot.ConfigMgr;
import fire.pb.item.EquipItemShuXing;
import fire.pb.item.SItemToItem;
import fire.pb.mission.SMissionData;
import fire.pb.mission.SSpecialScenarioQuestConfig;
import fire.pb.shop.MarketThreeTable;
import fire.pb.circletask.CircTaskConf;

/**
 * 任务管理器
 */
public class MissionMgr {

	public class RobotMarketBuyInfo {

		public Integer firstno;

		public Integer twono;

		public ArrayList<MarketThreeTable> threenos = new ArrayList<MarketThreeTable>();
	}

	private static MissionMgr instance;

	public static MissionMgr getInstance() {
		if (instance == null) {
			instance = new MissionMgr(ConfigMgr.getInstance());
		}
		return instance;
	}

	Map<Integer, Set<Integer>> postlineMap = new HashMap<Integer, Set<Integer>>();

	public final Map<Integer, MissionCfgMgr> questMap = new HashMap<Integer, MissionCfgMgr>();

	public static Map<Integer, SItemToItem> itemToItemConfMap;

	public static Map<Integer, EquipItemShuXing> equipConfMap;

	public static Map<Integer, CircTaskConf> circTaskConfMap;

	public static Map<Integer, MarketThreeTable> marketThreeConfMap;

	public static ArrayList<RobotMarketBuyInfo> marketBuyList = new ArrayList<RobotMarketBuyInfo>();

	private MissionMgr(ConfigMgr cm) {
		init(cm);
	}

	void init(ConfigMgr cm) {
		questMap.clear();
		postlineMap.clear();
		marketBuyList.clear();
		final java.util.NavigableMap<Integer, SMissionData> missionMap = cm.getConfig(SMissionData.class);
		Set<MissionCfgMgr> headTasks = new HashSet<MissionCfgMgr>();
		Set<MissionCfgMgr> hasBrothers = new HashSet<MissionCfgMgr>();
		Map<Integer, SSpecialScenarioQuestConfig> specailquestconfig = cm.getConfig(SSpecialScenarioQuestConfig.class);
		itemToItemConfMap = cm.getConfig(SItemToItem.class);
		equipConfMap = cm.getConfig(EquipItemShuXing.class);
		circTaskConfMap = cm.getConfig(CircTaskConf.class);
		marketThreeConfMap = cm.getConfig(MarketThreeTable.class);
		for (final MarketThreeTable mtcf : marketThreeConfMap.values()) {
			boolean isNeedAddNew = true;
			for (RobotMarketBuyInfo marketBuyConf : marketBuyList) {
				if (marketBuyConf.firstno == mtcf.firstno && marketBuyConf.twono == mtcf.twono) {
					marketBuyConf.threenos.add(mtcf);
					isNeedAddNew = false;
					break;
				}
			}
			if (isNeedAddNew) {
				RobotMarketBuyInfo jcmbi = new RobotMarketBuyInfo();
				jcmbi.firstno = mtcf.firstno;
				jcmbi.twono = mtcf.twono;
				jcmbi.threenos.add(mtcf);
				marketBuyList.add(jcmbi);
			}
		}
		for (final SMissionData mmi : missionMap.values()) {
			MissionCfgMgr conf = new MissionCfgMgr(mmi, specailquestconfig);
			questMap.put(mmi.id, conf);
			if (mmi.NextMissionList != null && mmi.NextMissionList.size() > 1) {
				hasBrothers.add(conf);
			}
		}
		for (MissionCfgMgr mission : hasBrothers) {
			for (final int postMission : mission.missionRewardsInfo.postMissionList) {
				final MissionCfgMgr postMissionConf = questMap.get(postMission);
				if (postMissionConf == null)
					continue;
				for (final int pm : mission.missionRewardsInfo.postMissionList) {
					if (pm != postMissionConf.missionId)
						postMissionConf.missionLimitedInfo.brotherMissions.add(pm);
				}
			}
		}
		for (MissionCfgMgr mission : headTasks) {
			if (mission.missionLimitedInfo.minLv != 0 || mission.missionLimitedInfo.maxLv != 0) {
				Set<Integer> set = postlineMap.get(0);
				if (set == null) {
					set = new HashSet<Integer>();
					postlineMap.put(0, set);
				}
				set.add(mission.missionId);
			}
			if (mission.missionLimitedInfo.requiredMissionList != null) {
				for (int postmission : mission.missionLimitedInfo.requiredMissionList) {
					Set<Integer> set = postlineMap.get(postmission);
					if (set == null) {
						set = new HashSet<Integer>();
						postlineMap.put(postmission, set);
					}
					set.add(mission.missionId);
				}
			}
		}
	}

	public MissionCfgMgr getTaskConfig(int taskid) {
		return questMap.get(taskid);
	}

	// 获取一定数量可以摆摊的物品,用于摆摊
	public List<MarketThreeTable> getSomeMarketItemIds(int num) {
		if (marketThreeConfMap == null || marketThreeConfMap.size() <= 0) {
			return null;
		}
		if (num > marketThreeConfMap.size() && marketThreeConfMap.size() > 0) {
			return new ArrayList<MarketThreeTable>(marketThreeConfMap.values());
		}
		List<MarketThreeTable> result = new ArrayList<MarketThreeTable>();
		List<Entry<Integer, MarketThreeTable>> list = new ArrayList<Entry<Integer, MarketThreeTable>>(
				marketThreeConfMap.entrySet());
		java.util.Collections.shuffle(list);
		Iterator<Entry<Integer, MarketThreeTable>> it = list.iterator();
		while (it.hasNext()) {
			Entry<Integer, MarketThreeTable> itNow = it.next();
			if (itNow.getValue().israrity != 1 && itNow.getValue().itemtype == 2) {
				continue;
			}
			result.add(marketThreeConfMap.get(itNow.getKey()));
			if (result.size() >= num) {
				break;
			}
		}
		return result;
	}

	public String getMarketItemName(int itemId) {
		MarketThreeTable mt = marketThreeConfMap.get(itemId);
		if (mt == null)
			return null;
		return mt.getItemname();
	}

	public RobotMarketBuyInfo getRandRobotMarketBuyInfo() {
		int size = marketBuyList.size();
		if (size <= 0) {
			return null;
		}
		Random rand = new Random();
		int index = rand.nextInt(size);
		return marketBuyList.get(index);
	}
}
