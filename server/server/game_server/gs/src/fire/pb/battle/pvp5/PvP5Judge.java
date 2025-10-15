package fire.pb.battle.pvp5;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fire.pb.talk.MessageMgr;
import fire.pb.title.STitleConfig;
import fire.pb.title.Title;
import fire.pb.util.BagUtil;

/**
 * 5v5判决结果
 * @author XGM
 */
public class PvP5Judge {

	public enum EResult {
		AWIN, BWIN, NULL
	}

	private final PvP5RaceProxy raceProxy;

	public PvP5Judge(PvP5RaceProxy race) {
		this.raceProxy = race;
	}

	/**
	 * 预测结果(活动进行中)
	 */
	public EResult predict() {
		int roleNumA = getExcellentRoleNum(raceProxy.getXRace().getAllrolesidcampa());
		int roleNumB = getExcellentRoleNum(raceProxy.getXRace().getAllrolesidcampb());
		if (roleNumA != roleNumB) {
			if (roleNumA == 0) {
				return EResult.BWIN;
			} else if (roleNumB == 0) {
				return EResult.AWIN;
			}
		}
		return EResult.NULL;
	}

	/**
	 * 判定结果(活动结束时)
	 * 1:优胜场人数多的阵营获胜
	 * 2:人数相同时,积分前5名人数多的阵营获胜(如果人数还是相同的情况下,第一名所在阵营获胜)
	 */
	public void judge() {

		EResult result = EResult.NULL;
		int roleNumA = getExcellentRoleNum(raceProxy.getXRace().getAllrolesidcampa());
		int roleNumB = getExcellentRoleNum(raceProxy.getXRace().getAllrolesidcampb());
		if (roleNumA > roleNumB) {
			result = EResult.AWIN;
		} else if (roleNumA < roleNumB) {
			result = EResult.BWIN;
		} else {
			Integer[] titles = PvP5Helper.getTitleAward(raceProxy.getGrade());
			if (titles != null) {
				List<xbean.PvP5RaceRole> tempRoleList = PvP5RankVisitor.getCurrentRankListAndSort(raceProxy, titles.length);
				Set<Long> idsA = new HashSet<Long>(), idsB = new HashSet<Long>();
				getCampRankListRoleIdSet(raceProxy, titles.length, idsA, idsB);

				int numA = 0, numB = 0;
				for (int i = 0; i < titles.length && i < tempRoleList.size(); i++) {
					xbean.PvP5RaceRole raceRole = tempRoleList.get(i);
					if (idsA.contains(raceRole.getRoleid())) {
						numA++;
					} else if (idsB.contains(raceRole.getRoleid())) {
						numB++;
					}
				}
				if (numA != numB) {
					result = numA > numB ? EResult.AWIN : EResult.BWIN;
				} else if (tempRoleList.size() > 0) {
					long firstRoleId = tempRoleList.get(0).getRoleid();
					result = idsA.contains(firstRoleId) ? EResult.AWIN : EResult.BWIN;
				}
			}
		}

		if (result != EResult.NULL) {
			// 提示
			do {
				int camp = (result == EResult.AWIN ? 0 : 1);
				List<String> strings = new LinkedList<String>();
				strings.add(PvP5Helper.getGradeName(raceProxy.getGrade()));
				strings.add(String.valueOf(roleNumA));
				strings.add(String.valueOf(roleNumB));
				strings.add(PvP5Helper.getCampName(camp));
				if (roleNumA != roleNumB) {
					// 奥山战场$精英组$的比赛已结束，双方优胜场剩余人数对比为：雷毛($5$) vs 双狼($0)$雷毛$阵营取得了最后的胜利！
					MessageMgr.broadcastMsgNotify(172037, -1, strings);
				} else {
					// 奥山战场$精英组$的比赛已结束，双方优胜场剩余人数对比为：雷毛($5$) vs 双狼($5$)根据场内积分表现，$雷毛$阵营取得了最后的胜利！
					MessageMgr.broadcastMsgNotify(172036, -1, strings);
				}
			} while (false);

			// 奖励
			award(result);
		}
	}

	/**
	 * 奖励
	 * @param result
	 */
	public void award(EResult result) {

		int camp = 0;
		List<xbean.PvP5RaceRole> roles = null;
		if (result == EResult.AWIN) {
			camp = 0;
			roles = raceProxy.getXRace().getAllrolescampa();
		} else if (result == EResult.BWIN) {
			camp = 1;
			roles = raceProxy.getXRace().getAllrolescampb();
		}

		if (roles != null) {
			award(roles, camp);
		}
	}

	/**
	 * 奖励
	 * @param roleList
	 * @param camp
	 */
	private void award(List<xbean.PvP5RaceRole> roleList, int camp) {

		// 恭喜本届奥山战场$精英组$$雷毛$阵营获得了胜利！
		do {
			List<String> strings = new LinkedList<String>();
			strings.add(PvP5Helper.getGradeName(raceProxy.getGrade()));
			strings.add(PvP5Helper.getCampName(camp));
			MessageMgr.broadcastMsgNotify(172020, -1, strings);
		} while (false);

		// 称号奖励,1-5名奖励
		Integer[] titles = PvP5Helper.getTitleAward(raceProxy.getGrade());
		if (titles != null) {
			for (int i = 0; i < titles.length && i < roleList.size(); i++) {
				Integer titleId = titles[i];
				STitleConfig titleConfig = fire.pb.title.TitleManager.getTitleConfigById(titleId);
				if (titleConfig != null) {

					xbean.PvP5RaceRole raceRole = roleList.get(i);
					long roleId = raceRole.getRoleid();
					int rank = i + 1;

					// 发奖时每个人单独处理,避免锁一群人
					mkdb.Procedure proc = createAwardProc(roleId, titleId, rank, PvP5Helper.RANK_01_05_AWARD_ID);
					mkdb.Procedure.pexecuteWhileCommit(proc);

					// $某人$在奥山战场$精英组$$雷毛$阵营英勇战斗，获得积分第$1$名，得到了$奥山精英组第一$的称号以及大宝箱的奖励
					List<String> strings = new LinkedList<String>();
					strings.add(xtable.Properties.selectRolename(roleId));
					strings.add(PvP5Helper.getGradeName(raceProxy.getGrade()));
					strings.add(PvP5Helper.getCampName(camp));
					strings.add(String.valueOf(rank));
					strings.add(titleConfig.titlename);
					MessageMgr.broadcastMsgNotify(172021, -1, strings);
				}
			}
		}
		// 6-15名奖励
		for (int i = 5; i < 15 && i < roleList.size(); i++) {

			xbean.PvP5RaceRole raceRole = roleList.get(i);
			long roleId = raceRole.getRoleid();
			int rank = i + 1;

			int awardId = -1;
			if (rank >= 6 && rank <= 10) {
				// 6-10名奖励
				awardId = PvP5Helper.RANK_06_10_AWARD_ID;
			} else if (rank >= 11 && rank <= 15) {
				// 11-15名奖励
				awardId = PvP5Helper.RANK_11_15_AWARD_ID;
			} else {
				continue;
			}
			if (awardId > 0) {
				mkdb.Procedure proc = createAwardProc(roleId, -1, rank, awardId);
				mkdb.Procedure.pexecuteWhileCommit(proc);
			}
		}
	}

	/**
	 * 创建角色奖励的处理
	 * @param roleId
	 * @param titleId
	 * @param rank
	 * @param awardId
	 * @return
	 */
	private mkdb.Procedure createAwardProc(long roleId, int titleId, int rank, int awardId) {
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				// add title
				STitleConfig titleConfig = fire.pb.title.TitleManager.getTitleConfigById(titleId);
				if (titleConfig != null) {
					Title title = new Title(roleId, false);
					if (title.roleHaveTitle(titleId)) {
						title.removeTitle(titleId);
					}
					title.addTitle(titleId, titleConfig.getTitlename(), -1);
				}
				// add award
				int itemId = awardId;
				int itemNum = 1;
				int num = BagUtil.addItem(roleId, itemId, itemNum, "PVP5排名奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP5, itemId);
				if (num < itemNum) {
					// 添加失败不作处理
				}
				return true;
			}
		};
		return proc;
	}

	/**
	 * 获得优胜场人数
	 * @param roleIds
	 * @return
	 */
	private static int getExcellentRoleNum(List<Long> roleIds) {
		int num = 0;
		for (Long roleId : roleIds) {
			PvP5RoleProxy roleProxy = PvP5RoleProxy.getPvP5RoleProxy(roleId, true);
			if (roleProxy.getExcellent() == 1) {
				num++;
			}
		}
		return num;
	}

	/**
	 * 获得每个阵营排行榜前几名的角色id
	 * @param raceProxy
	 * @param recordMax
	 * @param outIdsA
	 * @param outIdsB
	 */
	private static void getCampRankListRoleIdSet(PvP5RaceProxy raceProxy, int recordMax, Set<Long> outIdsA, Set<Long> outIdsB) {
		List<xbean.PvP5RaceRole> roleListA = raceProxy.getXRace().getAllrolescampa();
		List<xbean.PvP5RaceRole> roleListB = raceProxy.getXRace().getAllrolescampb();
		int max = Math.max(roleListA.size(), roleListB.size());
		max = Math.min(recordMax, max);
		for (int i = 0; i < max; i++) {
			if (i < roleListA.size()) {
				outIdsA.add(roleListA.get(i).getRoleid());
			}
			if (i < roleListB.size()) {
				outIdsB.add(roleListB.get(i).getRoleid());
			}
		}
	}
}
