package fire.pb.battle;

import java.util.ArrayList;
import java.util.List;

import mkdb.Procedure;

public class PRoleOperation extends Procedure {
	
	private final long roleID;
	private final Operation operation;
	private final boolean isauto;
	private final boolean isrole;
	PRoleOperation(final long roleID, final boolean isrole, final Operation operation,final boolean isauto ){
		this.roleID = roleID;
		this.isrole = isrole;
		this.operation = operation;
		this.isauto = isauto;
	}
	public boolean process(){
		final Long battleID = xtable.Roleid2battleid.select(roleID);
		if(null == battleID)
			return false;
		
		final xbean.BattleInfo battle = xtable.Battle.get(battleID);
		if(null == battle)
			return false;
		
		if(battle.getRound() < 1)//预防客户端直接发消息偷第一回合
		{
			BattleField.logger.info(new StringBuilder("第0回合，收到客户端发来的操作。丫使用了外挂了！。 operationtype: ").append(operation.operationtype).append(" 执行者：").append(roleID).append(" 目标：").append(operation.aim).append(" 操作的值：").append(operation.operationid));
			return false;
		}
		
		if(battle.getPhase() != xbean.BattleInfo.PHASE_WAITOPERATION)
			return false;
		final Integer index = battle.getRoleids().get(roleID);
		if(null == index)
			return false ;
		if(BattleField.checkOutBattle(battle, index))
			return false;
			
		//锁rolelock
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, battle.getRoleids().keySet()));
		
		if(isrole)
		{
			//if (BattleField.logger.isDebugEnabled()) {
			//}
			DecisionProcesser.addRoleDecision(battle, index, operation,isauto);
		}
		else
			DecisionProcesser.addPetDecision(battle, index + 5, operation,isauto);
		BattleField.logger.debug(new StringBuilder("收到客户端发来的操作 operationtype: ").append(operation.operationtype).append(" 执行者：").append(roleID).append(" 目标：").append(operation.aim).append(" 操作的值：").append(operation.operationid));
		//addDecision(battle,index, ro, po);
		
		if(checkPlay(battle)){
			BattleField.cancelTimeOutCheck(battle);
			pexecuteWhileCommit(new PPlayScript(battle.getBattleid()));
		}
		
		return true;
	}
	public boolean checkPlay(final xbean.BattleInfo battle){
		//List<Integer> liveRoleids=BattleField.getLiveFighterIds(battle);
		//直接判断还在场上的战斗者数量，因为死亡等各种状态也会有指令
		
		if(battle.getDecisions().size() >= battle.getFighters().size())
			return true;
		
		//检查是否有托管战斗者
		int offlineFighterNum = 0;
		List<Long> roleIds = new ArrayList<Long>();
		for(long roleId : battle.getRoleids().keySet())
		{
			//if(StateManager.selectStateIdByRoleId(roleId) == fire.pb.state.State.TRUSTEESHIP_STATE)
			if(battle.getOfflineroleids().contains(roleId))
			{//有托管中的玩家
				int fid = battle.getRoleids().get(roleId);
				if(battle.getDecisions().containsKey(fid))//如果已经下达了指令就不用统计了
					continue;
				roleIds.add(roleId);
				offlineFighterNum++;
				if(battle.getFighters().containsKey(fid+5))//宠物存在
					offlineFighterNum++;
			}
		}
		if( (battle.getDecisions().size() + offlineFighterNum) >= battle.getFighters().size())
		{
			//未掉线玩家的所有指令都已下达,替托管玩家下达指令
			for(long roleId : roleIds)
			{
				// 战斗托管中，构造角色和宠物的默认攻击操作 TODO 将来可能会放入托管操作（例如自动时）
				int fighterId = battle.getRoleids().get(roleId);
				DecisionProcesser.addRoleDecision(battle, fighterId, DecisionProcesser.getRoleDefaultOperation(roleId),true);
				if(!BattleField.checkOutBattle(battle, fighterId + 5))
					DecisionProcesser.addPetDecision(battle, fighterId + 5, DecisionProcesser.getPetDefaultOperation(roleId),true);
			}
			return true;
		}
		else
			return false;//还有正常的战斗者，未发来战斗指令
	}
}
