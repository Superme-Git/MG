package fire.pb.battle;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fire.pb.battle.ai.BattleAIManager;
import mkdb.Procedure;
import xbean.BattleInfo;

public class PRoundStart extends Procedure {
	final private List<Long> liveRoleids;

	PRoundStart(final long battleID, List<Long> liveRoleids) {
		this.battleID = battleID;
		this.liveRoleids = liveRoleids;
	}

	@Override
	public boolean process() {
		final xbean.BattleInfo battle = xtable.Battle.get(battleID);
		if (null == battle)
			return false;
		// 注掉:锁所有角色的锁
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, battle.getRoleids().keySet()));

		battle.setRoundtime(new java.util.Date().getTime());

		BattleField.logger.info(new StringBuilder("战场号为：").append(battle.getBattleid()).append("的战斗---新回合开始"));

		DecisionProcesser.setFieldfighterAction(battle);
		DecisionProcesser.setMonsterAction(battle);
		// 去掉：回合开始前处理buff，并帮不能自主出招的人出招（例如强制防御、混乱、昏睡等）
		List<Long> roleids = new LinkedList<Long>();
		for (long roleId : liveRoleids) {
			if (!battle.getOfflineroleids().contains(roleId))
				roleids.add(roleId);
		}

		addRound(battle);
		battle.setPhase(BattleInfo.PHASE_WAITOPERATION);
		battle.setTimeout(
				mkdb.Mkdb.executor().schedule(new OperationChek(battle.getBattleid(), battle.getRound()),
						(int) ((roleids.size() < 1 ? BattleField.ACTION_AUTO_TIMEOUT_DELAY
								: BattleField.ACTION_TIMEOUT_DELAY) * battle.getGmactiontimetimes() / 10.0),
						TimeUnit.SECONDS));
		// 通知客户端进行操作选择
		final SSendRoundStart snd = new SSendRoundStart();
		snd.time = (int) (BattleField.ACTION_TIMEOUT_DELAY * battle.getGmactiontimetimes() / 10.0);

		if (battle.getRound() == 1) // 第一回合，也就是战斗开始
		{
			snd.aiactions = BattleAIManager.getInstance().getActionWhileBattleStart(battle);
			snd.aiactions.putAll(BattleAIManager.getInstance().getActionBeforeRoundStart(battle));
		} else
			snd.aiactions = BattleAIManager.getInstance().getActionBeforeRoundStart(battle);
		snd.environment = battle.getEnvironment();

		psendWhileCommit(roleids, snd);
		// 发给观战的人开始操作选择
		psendWhileCommit(battle.getAudience().keySet(), snd);

		BattleField.logger.info(new StringBuilder("战场号为：").append(battle.getBattleid()).append("的战斗--通知客户端进行操作选择"));
		return true;
	}

	private final long battleID;

	// 回合数增长
	private void addRound(xbean.BattleInfo battle) {
		battle.setRound(battle.getRound() + 1);
		battle.getEngine().setBattleRound(battle.getRound());
		for (xbean.Fighter fighterb : battle.getFighters().values())
			fighterb.setRound(fighterb.getRound() + 1);
	}
}
