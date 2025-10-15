package fire.pb.battle;

import mkdb.Procedure;

/**
 * 有的定时任务在战斗中到时，但需要将任务执行放在战斗结束后
 * 这个任务必须与某个角色挂钩（将来可能与宠物挂钩？）
 * 注意：如果这个角色已经不在战斗中了，则直接执行
 * @author nobody
 *
 */
public class PSetBattleEndProcedure extends mkdb.Procedure
{
	final private long roleId;
	final private mkdb.Procedure procedure;
	public PSetBattleEndProcedure(long roleId, Procedure procedure)
	{
		this.roleId = roleId;
		this.procedure = procedure;
	}
	
	protected boolean process()
	{
		Long battleId = xtable.Roleid2battleid.select(roleId);
		if(battleId == null)
		{
			procedure.call();
			return true;
		}
		xbean.BattleInfo battle = xtable.Battle.get(battleId);
		if(battle == null)
		{
			procedure.call();
			return true;
		}
		Integer fighterId = battle.getRoleids().get(roleId);
		if(fighterId == null)
		{
			pexecuteWhileCommit(procedure);
			return true;
		}
		xbean.BattleEndProcedure endp = xbean.Pod.newBattleEndProcedure();
		endp.setFighterid(fighterId);
		endp.setEndprocedure(procedure);
		battle.getEndprocedures().add(endp);
		return true;
	}
}
