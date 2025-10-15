package fire.pb.battle.pvp3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import mkdb.logs.LogList;
import fire.pb.battle.pvp.IPvPVisitor;
import fire.pb.battle.pvp.PvPRaceProxy;

/**
 * PvP3排行榜访问
 * @author XGM
 */
public class PvP3RankVisitor implements IPvPVisitor {

	private final long roleId;
	private final int addScore;

	public PvP3RankVisitor(long roleId, int addScore) {
		this.roleId = roleId;
		this.addScore = addScore;
	}

	@Override
	public void visit(PvPRaceProxy pvpRaceProxy) {
		// 访问排行榜
		PvP3RaceProxy raceProxy = (PvP3RaceProxy)pvpRaceProxy;
		if (roleId != 0) {
			updateHistoryRankListOfRole(raceProxy.getGrade(), roleId, addScore);
		} else {
			updateHistoryRankList(raceProxy);
		}
	}

	/**
	 * 更新历史排行榜(更新一个人的数据)
	 * @param roleId
	 * @param addScore
	 */
	public static void updateHistoryRankListOfRole(int grade, long roleId, int addScore) {

		if (addScore <= 0) {
			return;
		}

		// 需要保存的记录
		int recordMax = getRecordMax();

		// 更新
		xbean.PvP3ScoreRankList rankList = xtable.Pvp3historyscorelist.get(grade);
		if (rankList == null) {
			rankList = xbean.Pod.newPvP3ScoreRankList();
			xtable.Pvp3historyscorelist.add(grade, rankList);
		}
		// 1:先删除
		int score = addScore;
		xbean.PvP3ScoreRecord existRecord = null;
		for (Iterator<xbean.PvP3ScoreRecord> it = rankList.getRecords().iterator(); it.hasNext();) {
			xbean.PvP3ScoreRecord record = it.next();
			if (record.getRoleid() == roleId) {
				it.remove();
				existRecord = record;
				score += existRecord.getScore();
				break;
			}
		}
		if (existRecord == null) {
			String roleName = xtable.Properties.selectRolename(roleId);
			existRecord = xbean.Pod.newPvP3ScoreRecord();
			existRecord.setRoleid(roleId);
			existRecord.setRolename(roleName);
		}
		existRecord.setScore(score);
		// 2:再添加
		boolean isAdd = false;
		ListIterator<xbean.PvP3ScoreRecord> listIt = ((LogList<xbean.PvP3ScoreRecord>) rankList.getRecords()).listIterator();
		while (listIt.hasNext()) {
			xbean.PvP3ScoreRecord record = listIt.next();
			if (score > record.getScore()) {
				listIt.previous();
				listIt.add(existRecord);
				isAdd = true;
				break;
			}
		}
		if (isAdd == false) {
			rankList.getRecords().add(existRecord);
		} 
		// 3:删除末尾多余的排名,这意味着排名在一定数量之后的角色积分不累计
		resize(rankList.getRecords(), recordMax);
	}

	/**
	 * 更新历史排行榜(更新整个榜单)
	 * @param raceProxy
	 */
	public static void updateHistoryRankList(PvP3RaceProxy raceProxy) {

		// 需要保存的记录
		int recordMax = getRecordMax();

		// 获得本届排行榜
		List<xbean.PvP3RaceRole> tempRoleList = getCurrentRankListAndSort(raceProxy, recordMax);

		// 更新
		xbean.PvP3ScoreRankList rankList = xtable.Pvp3historyscorelist.get(raceProxy.getGrade());
		if (rankList == null) {
			rankList = xbean.Pod.newPvP3ScoreRankList();
			xtable.Pvp3historyscorelist.add(raceProxy.getGrade(), rankList);
		}
		// 1:累加积分
		do {
			for (xbean.PvP3ScoreRecord record : rankList.getRecords()) {
				// 累加后从临时列表删除
				Iterator<xbean.PvP3RaceRole> it = tempRoleList.iterator();
				while (it.hasNext()) {
					xbean.PvP3RaceRole r = it.next();
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
			List<xbean.PvP3ScoreRecord> records = rankList.getRecords();
			for (int i = 0; i < tempRoleList.size(); ++i) {
				xbean.PvP3RaceRole raceRole = tempRoleList.get(i);
				// new
				xbean.PvP3ScoreRecord record = xbean.Pod.newPvP3ScoreRecord();
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
	}

	/**
	 * 获得本届排行榜的角色排名映射
	 */
	public static Map<Long, Integer> getCurrentRankMap(PvP3RaceProxy raceProxy) {
		Map<Long, Integer> rankMap = new HashMap<Long, Integer>();
		int i = 0;
		for (xbean.PvP3RaceRole raceRole : raceProxy.getXRace().getAllroles()) {
			rankMap.put(raceRole.getRoleid(), ++i);
		}
		return rankMap;
	}

	/**
	 * 获得排序好的本届排行榜
	 */
	public static List<xbean.PvP3RaceRole> getCurrentRankListAndSort(PvP3RaceProxy raceProxy, int recordMax) {
		// 1:先把排行榜需要的人加到临时列表
		List<xbean.PvP3RaceRole> tempRoleList = new LinkedList<xbean.PvP3RaceRole>();
		List<xbean.PvP3RaceRole> roleList = raceProxy.getXRace().getAllroles();
		int max = Math.min(recordMax, roleList.size());
		for (int i = 0; i < max; i++) {
			tempRoleList.add(roleList.get(i));
		}
		// 2:排序
		// 这里不用排序,因为进入到这里的榜单已经排好序了
		return tempRoleList;
	}

	/**
	 * 排序
	 * @param records
	 */
	public static void sort(List<xbean.PvP3ScoreRecord> records, Map<Long, Integer> currentRankMap) {

		List<xbean.PvP3ScoreRecord> copyRankList = new LinkedList<xbean.PvP3ScoreRecord>();
		for (xbean.PvP3ScoreRecord record : records) {
			copyRankList.add(record);
		}
		records.clear();
		copyRankList.sort(new IPvPVisitor.Util.ComparatorRank<xbean.PvP3ScoreRecord>(currentRankMap) {
			@Override
			public int compare(xbean.PvP3ScoreRecord o1, xbean.PvP3ScoreRecord o2) {
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
	public static void resize(List<xbean.PvP3ScoreRecord> records, int recordMax) {
		while (records.size() > recordMax) {
			records.remove(records.size() - 1);
		}
	}

	/**
	 * 清空历史排行榜
	 */
	public static void clearHistoryRankList() {
		for (int grade : PvP3Helper.getAllRaceIds()) {
			mkdb.Procedure proc = new mkdb.Procedure() {
				@Override
				protected boolean process() {
					xtable.Pvp3historyscorelist.remove(grade);
					return true;
				}
			};
			if (mkdb.Transaction.current() != null) {
				mkdb.Procedure.pexecuteWhileCommit(proc);
			} else {
				proc.submit();
			}
		}
	}

	/**
	 * 获得需要保存的记录上限
	 * @return
	 */
	public static int getRecordMax() {
		// 榜单需要显示的人数
		int recordShowMax = 50;
		// 历史榜单需要记录更多的人,以便每次积分递增,从而导致后面的人可以超前
		int recordMax = recordShowMax * 120; // 6000
		recordMax = Math.max(recordMax, 0);
		return recordMax;
	}
}
