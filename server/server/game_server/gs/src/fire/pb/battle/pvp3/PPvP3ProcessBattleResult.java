package fire.pb.battle.pvp3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mkdb.Procedure;

import com.locojoy.base.Octets;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.battle.pvp.EPvPType;
import fire.pb.battle.pvp.PvPHelper;
import fire.pb.circletask.PVPScoreAwardConfig;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;

public class PPvP3ProcessBattleResult extends Procedure {

	public final int gradeId;
	public final List<Long> winnerIds;
	public final List<Long> loserIds;
	public final long winnerLeaderId; // 战斗结束时,胜利方的队长ID
	public final long loserLeaderId; // 战斗结束时,失败方的队长ID
	public final long winnerTeamId;
	public final long loserTeamId;

	public PPvP3ProcessBattleResult(int gradeId, List<Long> winnerIds, List<Long> loserIds,
			long winnerLeaderId, long loserLeaderId, long winnerTeamId, long loserTeamId) {
		this.gradeId = gradeId;
		this.winnerIds = winnerIds;
		this.loserIds = loserIds;
		this.winnerLeaderId = winnerLeaderId;
		this.loserLeaderId = loserLeaderId;
		this.winnerTeamId = winnerTeamId;
		this.loserTeamId = loserTeamId;
	}

	public boolean process() {

		PvP3RaceProxy race = PvP3RaceProxy.getPvP3RaceProxy(gradeId);
		if (race.getXRace() == null)
			return false;

		Set<Long> roleIds = new HashSet<Long>();
		roleIds.addAll(winnerIds);
		roleIds.addAll(loserIds);
		if (loserIds.size() == 0) {
			// 如果失败方全跑了,取失败方的队长
			roleIds.add(loserLeaderId);
		}
		Set<Long> broadcastSet = race.getBroadcastRoles(new HashSet<Long>());

		// 锁队伍
		List<Long> teamIds = new LinkedList<Long>();
		if (winnerTeamId > 0) {
			teamIds.add(winnerTeamId);
		}
		if (loserTeamId > 0) {
			teamIds.add(loserTeamId);
		}
		if(teamIds.size() > 0) {
			lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, teamIds));
		}

		// 锁角色
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));

		int winnerScoreLv = 0;
		int loserScoreLv = 0;

		List<PvP3RoleProxy> winners = new LinkedList<PvP3RoleProxy>();
		for (long id : winnerIds) {
			PvP3RoleProxy winner = PvP3RoleProxy.getPvP3RoleProxy(id, false);
			winnerScoreLv += winner.getScoreLevel();

			if (winner.getRoleId() == winnerLeaderId) {
				winners.add(0, winner);
			} else {
				winners.add(winner);
			}
		}

		List<PvP3RoleProxy> losers = new LinkedList<PvP3RoleProxy>();
		List<Long> tempLoserIds = new ArrayList<Long>();
		tempLoserIds.addAll(loserIds);
		if (tempLoserIds.size() == 0) {
			// 如果失败方全跑了,取失败方的队长
			tempLoserIds.add(loserLeaderId);
		}
		for (long id : tempLoserIds) {
			PvP3RoleProxy loser = PvP3RoleProxy.getPvP3RoleProxy(id, false);
			loserScoreLv += loser.getScoreLevel();

			if (loser.getRoleId() == loserLeaderId) {
				// 队长在前
				losers.add(0, loser);
			} else {
				losers.add(loser);
			}
		}

		// 算出平均积分等级
		if (winnerIds.size() > 0) {
			winnerScoreLv /= winnerIds.size();
		}
		if (loserIds.size() > 0) {
			loserScoreLv /= loserIds.size();
		}
		// 最小1级
		winnerScoreLv = Math.max(1, winnerScoreLv);
		loserScoreLv = Math.max(1, loserScoreLv);
		// 计算积分增减
		int winScore = getWinScore(winnerScoreLv, loserScoreLv);
		int loseScore = getLoseScore(winnerScoreLv, loserScoreLv);
		// 失败者如果是逃跑的,不给积分
		if (loserIds.size() == 0) {
			loseScore = 0;
		}

		// 发奖励,发提示
		processBattleWinType(race, winners, winScore, losers, loseScore, broadcastSet);

		return true;
	}

	private void processBattleWinType(PvP3RaceProxy race, List<PvP3RoleProxy> winners, int winScore, List<PvP3RoleProxy> losers, int loseScore, Set<Long> broadcastSet) {

		PvP3RoleProxy winLeader = (winners.size() > 0 ? winners.get(0) : null);
		PvP3RoleProxy loseLeader = (losers.size() > 0 ? losers.get(0) : null);
		if (winLeader == null || loseLeader == null) {
			return;
		}

		int winnerLevel = 0;
		int loserLevel = 0;
		for (PvP3RoleProxy roleProxy : winners) {
			winnerLevel += xtable.Properties.selectLevel(roleProxy.getRoleId());
		}
		for (PvP3RoleProxy roleProxy : losers) {
			loserLevel += xtable.Properties.selectLevel(roleProxy.getRoleId());
		}
		// 取平均等级
		winnerLevel /= winners.size();
		loserLevel /= losers.size();

		int winAwardId = PvPHelper.getAwardIdByRoleLevel(EPvPType.PVP3, winnerLevel, true, 0);
		int loseAwardId = PvPHelper.getAwardIdByRoleLevel(EPvPType.PVP3, loserLevel, false, 0);

		awardAndSendMsg(winners, winAwardId, losers, loseAwardId, broadcastSet,
				winLeader.getContinueWinNum() + 1, loseLeader.getContinueWinNum());

		int winRongYu = winScore;
		int loseRongYu = loseScore;

		// 胜利者奖励的积分,荣誉
		for (PvP3RoleProxy roleProxy : winners) {
			int score = winScore;
			if (roleProxy.getBattleNum() + 1 > PvP3Helper.SCORE_MAX_BATTLE_NUM)
				score = 0;
			if (score > 0)
				roleProxy.addScore(score, false);
			if (winRongYu > 0) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleProxy.getRoleId(), false);
				bag.addSysCurrency(winRongYu, fire.pb.game.MoneyType.MoneyType_RongYu, "PVP3Winer", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP3, 0);
				MessageUtil.pSendAddOrRemoveCurrency(roleProxy.getRoleId(), winRongYu, fire.pb.game.MoneyType.MoneyType_RongYu);
			}
			// 刷新个人数据
			roleProxy.onBattleEnd(true, loserLeaderId, score, race);
		}
		// 失败者奖励的积分,荣誉
		for (PvP3RoleProxy roleProxy : losers) {
			int score = loseScore;
			if (roleProxy.getBattleNum() + 1 > PvP3Helper.SCORE_MAX_BATTLE_NUM)
				score = 0;
			if (score > 0)
				roleProxy.addScore(score, false);
			if (loseRongYu > 0) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleProxy.getRoleId(), false);
				bag.addSysCurrency(loseRongYu, fire.pb.game.MoneyType.MoneyType_RongYu, "PVP3Loser", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP3, 0);
				MessageUtil.pSendAddOrRemoveCurrency(roleProxy.getRoleId(), loseRongYu, fire.pb.game.MoneyType.MoneyType_RongYu);
			}
			// 刷新个人数据
			roleProxy.onBattleEnd(false, winnerLeaderId, score, race);
		}
	}

	private void awardAndSendMsg(List<PvP3RoleProxy> winners, int winAward,
			List<PvP3RoleProxy> losers, int loseAward, Set<Long> broadcastSet,
			int continueWinNum, int endContinueWinNum) {

		PvP3RoleProxy winLeader = (winners.size() > 0 ? winners.get(0) : null);
		PvP3RoleProxy loseLeader = (losers.size() > 0 ? losers.get(0) : null);
		if (winLeader == null || loseLeader == null) {
			return;
		}

		String winnerName = xtable.Properties.selectRolename(winLeader.getRoleId());
		String loserName = xtable.Properties.selectRolename(loseLeader.getRoleId());

		continueWinNum = Math.min(PvP3Helper.RACE_MAX_BATTLE_NUM, continueWinNum);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(RewardMgr.PVP_TIMES, continueWinNum);
		params.put(RewardMgr.PVP_TARGET_TIMES, endContinueWinNum);

		// 给胜利者奖励
		if (winAward > 0) {
			for (PvP3RoleProxy roleProxy : winners) {
				if (roleProxy.getBattleNum() + 1 <= PvP3Helper.SCORE_MAX_BATTLE_NUM) {
					RewardMgr.getInstance().distributeAllAward(roleProxy.getRoleId(), winAward, params,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP3, 0, PAddExpProc.OTHER_QUEST, "PVP3胜利");
				}
				// 给胜利者的提示
				sendRaceBattleInfo(roleProxy.getRoleId(), 160387, loserName, roleProxy.getBattleNum() + 1);
			}
		}

		// 给失败者奖励
		if (loseAward > 0) {
			for (PvP3RoleProxy roleProxy : losers) {
				if (roleProxy.getBattleNum() + 1 <= PvP3Helper.SCORE_MAX_BATTLE_NUM) {
					RewardMgr.getInstance().distributeAllAward(roleProxy.getRoleId(), loseAward, params,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP3, 0, PAddExpProc.OTHER_QUEST, "PVP3失败");
				}
				// 给失败者的提示
				sendRaceBattleInfo(roleProxy.getRoleId(), 160388, winnerName, roleProxy.getBattleNum() + 1);
			}
		}

		// 给别人的提示
		for (PvP3RoleProxy roleProxy : winners) {
			// 赢得5连胜
			int roleContinueWinNum = roleProxy.getContinueWinNum() + 1;
			if (roleContinueWinNum >= PvP3Helper.CONTINUE_WIN_MIN_NUM) {
				String roleName = xtable.Properties.selectRolename(roleProxy.getRoleId());
				sendRaceBattleInfo(broadcastSet, 160389, roleName, roleContinueWinNum);
			}
		}

		// 给别人的提示
		// 终结别人的5连胜
		if (endContinueWinNum >= PvP3Helper.CONTINUE_WIN_MIN_NUM) {
			sendRaceBattleInfo(broadcastSet, 160390, winnerName, loserName, endContinueWinNum);
		}
	}

	private void sendRaceBattleInfo(Set<Long> broadcastSet, int msgId, Object... params) {

		if (msgId <= 0)
			return;

		List<Octets> octetses = new LinkedList<Octets>();
		if (params != null) {
			for (Object parameter : params)
				octetses.add(MessageMgr.convertString2Octets(parameter.toString()));
		}

		SPvP3BattleInfo snd = new SPvP3BattleInfo();
		snd.ismine = 0;
		snd.msgid = msgId;
		snd.parameters.addAll(octetses);
		mkdb.Procedure.psendWhileCommit(broadcastSet, snd);
	}

	private void sendRaceBattleInfo(long roleId, int msgId, Object... params) {

		if (msgId <= 0)
			return;

		List<Octets> octetses = new LinkedList<Octets>();
		if (params != null) {
			for (Object parameter : params)
				octetses.add(MessageMgr.convertString2Octets(parameter.toString()));
		}

		SPvP3BattleInfo snd = new SPvP3BattleInfo();
		snd.ismine = 1;
		snd.msgid = msgId;
		snd.parameters.addAll(octetses);
		mkdb.Procedure.psendWhileCommit(roleId, snd);
	}

	private int getWinScore(int winLevel, int loseLevel) {

		PVPScoreAwardConfig config = PvPHelper.getScoreAwardConfig(winLevel);
		int addScore = config.win;

		// 衰减
		int levelSub = Math.abs(loseLevel - winLevel);
		if (levelSub == 0)
			return addScore;

		PVPScoreAwardConfig subConfig = PvPHelper.getScoreAwardConfig(levelSub);
		if (subConfig == null)
			return addScore;
		if (winLevel > loseLevel)
			addScore += subConfig.adwin;
		else
			addScore += subConfig.diswin;

		return addScore;
	}

	private int getLoseScore(int winLevel, int loseLevel) {

		PVPScoreAwardConfig config = PvPHelper.getScoreAwardConfig(loseLevel);
		int addScore = config.lose;

		// 衰减
		int levelSub = Math.abs(loseLevel - winLevel);
		if (levelSub == 0)
			return addScore;

		PVPScoreAwardConfig subConfig = PvPHelper.getScoreAwardConfig(levelSub);
		if (subConfig == null)
			return addScore;
		if (loseLevel > winLevel)
			addScore += subConfig.adlose;
		else
			addScore += subConfig.dislose;

		return addScore;
	}
}
