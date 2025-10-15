package fire.pb.battle.pvp1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.locojoy.base.Octets;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.battle.pvp.EPvPType;
import fire.pb.battle.pvp.PvPHelper;
import fire.pb.circletask.PVPScoreAwardConfig;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;

/**
 * 处理战斗结果
 */
public class PPvP1ProcessBattleResult extends mkdb.Procedure {

	public final int gradeId;
	public final long winnerId;
	public final long loserId;
	public final boolean loserRunaway;

	public PPvP1ProcessBattleResult(int gradeId, long winnerId, long loserId, boolean loserRunaway) {
		this.gradeId = gradeId;
		this.winnerId = winnerId;
		this.loserId = loserId;
		this.loserRunaway = loserRunaway;
	}

	public boolean process() {

		PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(gradeId);
		if (race.getXRace() == null)
			return false;

		Set<Long> roleids = new HashSet<Long>();
		roleids.add(winnerId);
		roleids.add(loserId);
		Set<Long> broadcastset = race.getBroadcastRoles(new HashSet<Long>());

		// 锁角色
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));

		PvP1RoleProxy winner = PvP1RoleProxy.getPvP1RoleProxy(winnerId, false);
		PvP1RoleProxy loser = PvP1RoleProxy.getPvP1RoleProxy(loserId, false);

		// 积分等级
		int winnerScoreLv = winner.getScoreLevel();
		int loserScoreLv = loser.getScoreLevel();
		// 计算积分增减
		int winScore = getWinScore(winnerScoreLv, loserScoreLv);
		int loseScore = getLoseScore(winnerScoreLv, loserScoreLv);
		// 失败者如果是逃跑的,不给积分
		if (loserRunaway) {
			loseScore = 0;
		}

		// 发奖励,发提示
		processBattleWinType(race, winner, winScore, loser, loseScore, broadcastset);

		return true;
	}

	private void processBattleWinType(PvP1RaceProxy race, PvP1RoleProxy winner, int winScore, PvP1RoleProxy loser, int loseScore, Set<Long> broadcastSet) {

		int winnerLevel = xtable.Properties.selectLevel(winner.getRoleId());
		int loserLevel = xtable.Properties.selectLevel(loser.getRoleId());

		int winAwardId = PvPHelper.getAwardIdByRoleLevel(EPvPType.PVP1, winnerLevel, true, 0);
		int loseAwardId = PvPHelper.getAwardIdByRoleLevel(EPvPType.PVP1, loserLevel, false, 0);

		awardAndSendMsg(winner, winAwardId, loser, loseAwardId, broadcastSet,
				winner.getContinueWinNum() + 1, loser.getContinueWinNum());

		int winRongYu = winScore;
		int loseRongYu = loseScore;

		// 积分
		if (winner.getBattleNum() + 1 > PvP1Helper.SCORE_MAX_BATTLE_NUM)
			winScore = 0;
		if (loser.getBattleNum() + 1 > PvP1Helper.SCORE_MAX_BATTLE_NUM)
			loseScore = 0;

		if (winScore > 0)
			winner.addScore(winScore, false);
		if (loseScore > 0)
			loser.addScore(loseScore, false);

		// 荣誉
		if (winRongYu > 0) {
			fire.pb.item.Pack bag = new fire.pb.item.Pack(winnerId, false);
			bag.addSysCurrency(winRongYu, fire.pb.game.MoneyType.MoneyType_RongYu, "PVP1Winer", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP1, 0);
			MessageUtil.pSendAddOrRemoveCurrency(winnerId, winRongYu, fire.pb.game.MoneyType.MoneyType_RongYu);
		}
		if (loseRongYu > 0) {
			fire.pb.item.Pack bag = new fire.pb.item.Pack(loserId, false);
			bag.addSysCurrency(loseRongYu, fire.pb.game.MoneyType.MoneyType_RongYu, "PVP1Loser", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP1, 0);
			MessageUtil.pSendAddOrRemoveCurrency(loserId, loseRongYu, fire.pb.game.MoneyType.MoneyType_RongYu);
		}

		// 刷新个人数据
		winner.onBattleEnd(true, loserId, winScore, race);
		loser.onBattleEnd(false, winnerId, loseScore, race);
	}

	private void awardAndSendMsg(PvP1RoleProxy winner, int winAward,
			PvP1RoleProxy loser, int loseAward, Set<Long> broadcastSet,
			int continueWinNum, int endContinueWinNum) {

		String winnername = xtable.Properties.selectRolename(winner.getRoleId());
		String losername = xtable.Properties.selectRolename(loser.getRoleId());
		continueWinNum = Math.min(PvP1Helper.RACE_MAX_BATTLE_NUM, continueWinNum);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(RewardMgr.PVP_TIMES, continueWinNum);
		params.put(RewardMgr.PVP_TARGET_TIMES, endContinueWinNum);

		// 给胜利者奖励
		if (winAward > 0) {
			if (winner.getBattleNum() + 1 <= PvP1Helper.SCORE_MAX_BATTLE_NUM) {
				RewardMgr.getInstance().distributeAllAward(winner.getRoleId(), winAward, params,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP1, 0, PAddExpProc.OTHER_QUEST, "PVP1胜利");
			}
			// 给胜利者的提示
			sendRaceBattleInfo(winner.getRoleId(), 160387, losername, winner.getBattleNum() + 1);
		}

		// 给失败者奖励
		if (loseAward > 0) {
			if (loser.getBattleNum() + 1 <= PvP1Helper.SCORE_MAX_BATTLE_NUM) {
				RewardMgr.getInstance().distributeAllAward(loser.getRoleId(), loseAward, params,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP1, 0, PAddExpProc.OTHER_QUEST, "PVP1失败");
			}
			// 给失败者的提示
			sendRaceBattleInfo(loser.getRoleId(), 160388, winnername, loser.getBattleNum() + 1);
		}

		// 给别人的提示
		// 赢得5连胜
		if (continueWinNum >= PvP1Helper.CONTINUE_WIN_MIN_NUM) {
			sendRaceBattleInfo(broadcastSet, 160389, winnername, continueWinNum);
		}
		// 终结别人的5连胜
		if (endContinueWinNum >= PvP1Helper.CONTINUE_WIN_MIN_NUM) {
			sendRaceBattleInfo(broadcastSet, 160390, winnername, losername, endContinueWinNum);
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

		SPvP1BattleInfo snd = new SPvP1BattleInfo();
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

		SPvP1BattleInfo snd = new SPvP1BattleInfo();
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
