package fire.pb.battle;

import java.util.HashSet;
import java.util.Set;

import mkdb.Procedure;

/*战后奖励分配等处理 by changhao*/

public class PBattleEndHandler extends Procedure
{
	final int result;
	
	protected boolean needRoll;
	
	xbean.BattleInfo battle;

	public PBattleEndHandler(final long battleID, final int result, xbean.BattleInfo battle)
	{
		this.battleID = battleID;
		this.result = result;
		this.battle = battle;
		needRoll = false;
	}

	/*加锁顺序battle->team->roleids by changhao*/
	public boolean process()
	{
		if (battle == null)
			return false;
		
		battle.setBattleresult(result);
		// 锁所有队伍的锁
		Set<Long> teamids = new HashSet<Long>(); 
		for(long rid :  battle.getRoleids().keySet())
		{
			Long tid = xtable.Roleid2teamid.select(rid);
			if(tid != null)
				teamids.add(tid);
		}
		lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, teamids));
		// 锁所有角色的锁
		Set<Long> rolesets = new HashSet<Long>();
		rolesets.addAll(battle.getRoleids().keySet());
		rolesets.addAll(battle.getAudience().keySet());
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, rolesets));
				
		// 战斗结束后计算惩罚,比如死亡惩罚等,改用battleEndHandler来做
		// 每场特殊的battle都会有至少一个battleendhandler来处理,统一调用process方法
		// 战斗奖励分配有可能触发ROLL点操作,如果触发ROLL点,战斗状态的清理要推迟到ROLL点结束  by changhao
		try
		{
			for (BattleEndHandler handler : battle.getBattleendhandlers())
			{
				handler.process(battleID, result);
				
				if (handler.getNeedRoll())
				{
					needRoll = true;
				}
			}
			
		} catch (Exception e)
		{
			BattleField.logger.debug("BattleEndHandler handle failed", e);
			return false;
		}

		return true;
	}

	private final long battleID;
}
