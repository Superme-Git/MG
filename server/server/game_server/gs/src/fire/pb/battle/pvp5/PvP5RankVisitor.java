package fire.pb.battle.pvp5;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.battle.pvp.IPvPVisitor;
import fire.pb.battle.pvp.PvPRaceProxy;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.RankListManager;

/**
 * PvP5排行榜访问
 * @author XGM
 */
public class PvP5RankVisitor implements IPvPVisitor {

	@Override
	public void visit(PvPRaceProxy pvpRaceProxy) {
		// 访问排行榜
		PvP5RaceProxy raceProxy = (PvP5RaceProxy)pvpRaceProxy;
		updateLastRankList(raceProxy);
		updateHistoryRankList(raceProxy);
	}

	/**
	 * 更新上届排行榜
	 * @param raceProxy
	 */
	public static void updateLastRankList(PvP5RaceProxy raceProxy) {

		int rankType = getLastRankType(raceProxy.getGrade());
		if (rankType == -1) {
			if (PvP5Control.getLogger().isInfoEnabled()) {
				PvP5Control.getLogger().info("PVP5::[PvP5RankVisitor.updateLastRankList]"
								+ " error race grade:" + raceProxy.getGrade());
			}
			return;
		}

		// 榜单需要记录的人数
		int recordMax = getLastRankRecordMax(rankType);

		// 获得本届排行榜
		List<xbean.PvP5RaceRole> tempRoleList = getCurrentRankListAndSort(raceProxy, recordMax);

		// 更新
		xbean.PvP5ScoreRankList rankList = xtable.Pvp5scorelist.get(raceProxy.getGrade());
		if (rankList == null) {
			rankList = xbean.Pod.newPvP5ScoreRankList();
			xtable.Pvp5scorelist.add(raceProxy.getGrade(), rankList);
		} else {
			rankList.getRecords().clear();
		}
		for (int i = 0; i < tempRoleList.size() && i < recordMax; i++) {
			xbean.PvP5RaceRole raceRole = tempRoleList.get(i);
			// new
			xbean.PvP5ScoreRecord record = xbean.Pod.newPvP5ScoreRecord();
			record.setRoleid(raceRole.getRoleid());
			record.setRolename(raceRole.getName());
			record.setScore(raceRole.getScore());
			int school = xtable.Properties.selectSchool(raceRole.getRoleid());
			record.setSchool(school);
			// add
			rankList.getRecords().add(record);
		}

	}

	/**
	 * 更新历史排行榜
	 * @param raceProxy
	 */
	public static void updateHistoryRankList(PvP5RaceProxy raceProxy) {

		int rankType = getHistoryRankType(raceProxy.getGrade());
		if (rankType == -1) {
			if (PvP5Control.getLogger().isInfoEnabled()) {
				PvP5Control.getLogger().info("PVP5::[PvP5RankVisitor.updateHistoryRankList]"
								+ " error race grade:" + raceProxy.getGrade());
			}
			return;
		}

		// 榜单需要显示的人数
		int recordShowMax = getHistoryRankRecordShowMax(rankType);
		// 榜单需要记录的人数
		int recordMax = getHistoryRankRecordMax(rankType);

		// 获得本届排行榜
		List<xbean.PvP5RaceRole> tempRoleList = getCurrentRankListAndSort(raceProxy, recordMax);

		// 更新
		xbean.PvP5ScoreRankList rankList = xtable.Pvp5historyscorelist.get(raceProxy.getGrade());
		if (rankList == null) {
			rankList = xbean.Pod.newPvP5ScoreRankList();
			xtable.Pvp5historyscorelist.add(raceProxy.getGrade(), rankList);
		}
		// 1:累加积分
		do {
			for (xbean.PvP5ScoreRecord record : rankList.getRecords()) {
				// 累加后从临时列表删除
				Iterator<xbean.PvP5RaceRole> it = tempRoleList.iterator();
				while (it.hasNext()) {
					xbean.PvP5RaceRole r = it.next();
					if (r.getRoleid() == record.getRoleid()) {
						record.setScore(record.getScore() + r.getScore());
						it.remove();
						break;
					}
				}
			}
		} while (false);
		// 2:合并列表
		do {
			List<xbean.PvP5ScoreRecord> records = rankList.getRecords();
			for (int i = 0; i < tempRoleList.size(); ++i) {
				xbean.PvP5RaceRole raceRole = tempRoleList.get(i);
				// new
				xbean.PvP5ScoreRecord record = xbean.Pod.newPvP5ScoreRecord();
				record.setRoleid(raceRole.getRoleid());
				record.setRolename(raceRole.getName());
				record.setScore(raceRole.getScore());
				// add
				records.add(record);
			}
			tempRoleList.clear();
		} while (false);
		// 3:排序
		sort(rankList.getRecords(), getCurrentRankMap(raceProxy));
		// 4:删除末尾多余的排名,这意味着排名在一定数量之后的角色积分不累计
		resize(rankList.getRecords(), recordMax);
		// 5:记录门派属性,用于客户端显示
		updateSchool(rankList.getRecords(), recordShowMax);
	}

	/**
	 * 用于排序的对象
	 * @author XGM
	 */
	public static class SortObject {
		public xbean.PvP5RaceRole obj;
		public byte camp;
		public int index;
		public SortObject(xbean.PvP5RaceRole obj, byte camp, int index) {
			this.obj = obj;
			this.camp = camp;
			this.index = index;
		}
	}

	/**
	 * 获得本届排行榜的角色排名映射
	 */
	public static Map<Long, Integer> getCurrentRankMap(PvP5RaceProxy raceProxy) {
		Map<Long, Integer> rankMap = new HashMap<Long, Integer>();

		// 方案1:
		List<SortObject> list = new LinkedList<SortObject>();
		int index = 0;
		for (xbean.PvP5RaceRole raceRole : raceProxy.getXRace().getAllrolescampa()) {
			SortObject obj = new SortObject(raceRole, (byte) 0, ++index);
			list.add(obj);
		}
		index = 0;
		for (xbean.PvP5RaceRole raceRole : raceProxy.getXRace().getAllrolescampb()) {
			SortObject obj = new SortObject(raceRole, (byte) 1, ++index);
			list.add(obj);
		}
		list.sort(new Comparator<SortObject>() {
			@Override
			public int compare(SortObject o1, SortObject o2) {
				int v = o2.obj.getScore() - o1.obj.getScore();
				if (v == 0) {
					v = o1.index - o2.index;
				}
				if (v == 0) {
					v = o1.camp - o2.camp;
				}
				if (v == 0) {
					v = (o1.obj.getRoleid() - o2.obj.getRoleid() > 0 ? 1 : -1);
				}
				return v;
			}
		});
		for (int i = 0; i < list.size(); ++i) {
			rankMap.put(list.get(i).obj.getRoleid(), i + 1);
		}

		// 方案2:
		/*
		ListIterator<xbean.PvP5RaceRole> itA = ((LogList<xbean.PvP5RaceRole>) raceProxy.getXRace().getAllrolescampa()).listIterator();
		ListIterator<xbean.PvP5RaceRole> itB = ((LogList<xbean.PvP5RaceRole>) raceProxy.getXRace().getAllrolescampb()).listIterator();

		// 先看看积分最高的在哪边
		ListIterator<xbean.PvP5RaceRole> itFront = null;
		ListIterator<xbean.PvP5RaceRole> itBack = null;
		do {
			xbean.PvP5RaceRole a = itA.hasNext() ? itA.next() : null;
			xbean.PvP5RaceRole b = itB.hasNext() ? itB.next() : null;
			if (a == null) {
				itFront = itB;
				itBack = itA;
			} else if (b == null) {
				itFront = itA;
				itBack = itB;
			} else {
				if (a.getScore() >= b.getScore()) {
					itFront = itA;
					itBack = itB;
				} else {
					itFront = itB;
					itBack = itA;
				}
			}
		} while (false);

		// 开始排序,同榜单积分相同的排在一起
		int i = 0;
		int prevScore = 0;
		while (itFront.hasNext()) {
			xbean.PvP5RaceRole front = itFront.next();
			int frontScore = front.getScore();
			if (prevScore > frontScore) {
				while (itBack.hasNext()) {
					xbean.PvP5RaceRole back = itBack.next();
					int backScore = back.getScore();
					if (prevScore > backScore && frontScore >= backScore) {
						itBack.previous();
						break;
					} else {
						rankMap.put(back.getRoleid(), ++i);
					}
				}
			}
			prevScore = frontScore;
			rankMap.put(front.getRoleid(), ++i);
		}
		// 剩余的
		while (itBack.hasNext()) {
			xbean.PvP5RaceRole back = itBack.next();
			rankMap.put(back.getRoleid(), ++i);
		}
		*/
		return rankMap;
	}

	/**
	 * 获得排序好的本届排行榜
	 * @return
	 */
	public static List<xbean.PvP5RaceRole> getCurrentRankListAndSort(PvP5RaceProxy raceProxy, int recordMax) {

		// 1:先把排行榜需要的人加到临时列表
		List<xbean.PvP5RaceRole> tempRoleList = new LinkedList<xbean.PvP5RaceRole>();
		do {
			List<xbean.PvP5RaceRole> roleListA = raceProxy.getXRace().getAllrolescampa();
			List<xbean.PvP5RaceRole> roleListB = raceProxy.getXRace().getAllrolescampb();
			int max = Math.max(roleListA.size(), roleListB.size());
			max = Math.min(recordMax, max);
			for (int i = 0; i < max; i++) {
				if (i < roleListA.size()) {
					tempRoleList.add(roleListA.get(i));
				}
				if (i < roleListB.size()) {
					tempRoleList.add(roleListB.get(i));
				}
			}
		} while (false);
		// 2:排序
		Map<Long, Integer> currentRankMap = getCurrentRankMap(raceProxy);
		tempRoleList.sort(new IPvPVisitor.Util.ComparatorRank<xbean.PvP5RaceRole>(currentRankMap) {
			@Override
			public int compare(xbean.PvP5RaceRole o1, xbean.PvP5RaceRole o2) {
				int v = o2.getScore() - o1.getScore(); // 积分高的靠前
				if (v == 0) {
					v = compareRank(o1.getRoleid(), o2.getRoleid());
				}
				if (v == 0) {
					v = (o1.getRoleid() - o2.getRoleid() > 0 ? 1 : -1); // 角色id小的靠前
				}
				return v;
			}
		});
		return tempRoleList;
	}

	/**
	 * 排序
	 * @param records
	 */
	public static void sort(List<xbean.PvP5ScoreRecord> records, Map<Long, Integer> currentRankMap) {

		List<xbean.PvP5ScoreRecord> copyRankList = new LinkedList<xbean.PvP5ScoreRecord>();
		for (xbean.PvP5ScoreRecord record : records) {
			copyRankList.add(record);
		}
		records.clear();
		copyRankList.sort(new IPvPVisitor.Util.ComparatorRank<xbean.PvP5ScoreRecord>(currentRankMap) {
			@Override
			public int compare(xbean.PvP5ScoreRecord o1, xbean.PvP5ScoreRecord o2) {
				int v = o2.getScore() - o1.getScore(); // 积分高的靠前
				if (v == 0) {
					v = compareRank(o1.getRoleid(), o2.getRoleid());
				}
				if (v == 0) {
					v = (o1.getRoleid() - o2.getRoleid() > 0 ? 1 : -1); // 角色id小的靠前
				}
				return v;
			}
		});
		records.addAll(copyRankList);
	}

	/**
	 * 调整排行榜数量
	 * @param records
	 * @param recordMax
	 */
	public static void resize(List<xbean.PvP5ScoreRecord> records, int recordMax) {
		while (records.size() > recordMax) {
			records.remove(records.size() - 1);
		}
	}

	/**
	 * 更新门派属性
	 * @param records
	 * @param recordShowMax
	 */
	public static void updateSchool(List<xbean.PvP5ScoreRecord> records, int recordShowMax) {
		int recordSize = records.size();
		for (int i = 0; i < recordSize && i < recordShowMax; i++) {
			xbean.PvP5ScoreRecord record = records.get(i);
			if (record.getSchool() == 0) {
				int school = xtable.Properties.selectSchool(record.getRoleid());
				record.setSchool(school);
			}
		}
	}

	/**
	 * 获得上届排行榜需要保存的记录数
	 * @param rankType
	 * @return
	 */
	public static int getLastRankRecordMax(int rankType) {
		// 榜单需要记录的人数
		int recordMax = RankListManager.getInstance().getPersonNumMaxByType(rankType);
		recordMax = Math.max(recordMax, 0);
		return recordMax;
	}

	/**
	 * 获得历史排行榜需要保存的记录数
	 * @param rankType
	 * @return
	 */
	public static int getHistoryRankRecordMax(int rankType) {
		// 榜单需要显示的人数
		int recordShowMax = 100; //getHistoryRankRecordShowMax(rankType); // 合服时不读配置 [7/1/2016 XGM]
		// 历史榜单需要记录更多的人,以便每次积分递增,从而导致后面的人可以超前
		int recordMax = recordShowMax * 60; // 6000
		recordMax = Math.max(recordMax, 0);
		return recordMax;
	}

	/**
	 * 获得历史排行榜需要显示的记录数
	 * @param rankType
	 * @return
	 */
	public static int getHistoryRankRecordShowMax(int rankType) {
		int recordShowMax = RankListManager.getInstance().getPersonNumMaxByType(rankType);
		recordShowMax = Math.max(recordShowMax, 0);
		return recordShowMax;
	}

	/**
	 * 获得上届排行榜类型
	 * @param grade
	 * @return
	 */
	public static int getLastRankType(int grade) {
		switch (grade) {
		case 1: return RankType.PVP5_LAST_GRADE1;
		case 2: return RankType.PVP5_LAST_GRADE2;
		case 3: return RankType.PVP5_LAST_GRADE3;
		}
		return -1;
	}

	/**
	 * 获得上届排行榜等级
	 * @param rankType
	 * @return
	 */
	public static int getLastRankGrade(int rankType) {
		switch (rankType) {
		case RankType.PVP5_LAST_GRADE1: return 1;
		case RankType.PVP5_LAST_GRADE2: return 2;
		case RankType.PVP5_LAST_GRADE3: return 3;
		}
		return -1;
	}

	/**
	 * 获得历史排行榜类型
	 * @param grade
	 * @return
	 */
	public static int getHistoryRankType(int grade) {
		switch (grade) {
		case 1: return RankType.PVP5_HISTORY_GRADE1;
		case 2: return RankType.PVP5_HISTORY_GRADE2;
		case 3: return RankType.PVP5_HISTORY_GRADE3;
		}
		return -1;
	}

	/**
	 * 获得历史排行榜等级
	 * @param rankType
	 * @return
	 */
	public static int getHistoryRankGrade(int rankType) {
		switch (rankType) {
		case RankType.PVP5_HISTORY_GRADE1: return 1;
		case RankType.PVP5_HISTORY_GRADE2: return 2;
		case RankType.PVP5_HISTORY_GRADE3: return 3;
		}
		return -1;
	}

}
