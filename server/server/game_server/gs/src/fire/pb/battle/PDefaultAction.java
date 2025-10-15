package fire.pb.battle;

import mkdb.Procedure;
import xbean.BattleInfo;

/**为没有选择操作的战斗单元 设置默认的操作*/
public class PDefaultAction extends Procedure {
	PDefaultAction(final long battleID, final int round){
		this.battleID = battleID;
		this.round = round;
	}
	
	public boolean process(){
		
		final xbean.BattleInfo battle = xtable.Battle.get(battleID);
		if(null == battle)
			return false;
		if(battle.getPhase() != BattleInfo.PHASE_WAITOPERATION)
			return false;
		if(battle.getRound() != this.round)
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("PDefaultAction round不相等，round = " + this.round);}
			return false;
		}
		if(battle.getTimeout() == null)
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("PDefaultAction timeout=null");}
			return false;
		}
		battle.setTimeout(null);
		for(xbean.Fighter fighter : battle.getFighters().values()){
			int index=fighter.getBattleindex();
			if(BattleField.checkOutBattle(battle, index))
				continue;
			xbean.Decision decision = battle.getDecisions().get(fighter.getBattleindex());
			if(null == decision){
				decision = DecisionProcesser.getDefaultDecision(battle, fighter);
				battle.getDecisions().put(decision.getOperator(), decision);
			}
			BattleField.dealBabyFight(battle,battle.getFighterobjects().get(fighter.getBattleindex()),decision,false);
		}
		new PPlayScript(battleID).call();//在这里做，防止出现与PRoleOperation的冲突，进入PDefaultAction就直接执行PPlayScript
		return true;
	}
	
	private final long battleID;
	private final int round;
}
