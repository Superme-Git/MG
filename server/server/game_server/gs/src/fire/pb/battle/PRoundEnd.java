package fire.pb.battle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.PropRole;
import mkdb.Procedure;

public class PRoundEnd extends Procedure {
	PRoundEnd(final long battleID, final int round) {
		this.battleID = battleID;
		this.round = round;
	}
		
	public boolean process() {
		final xbean.BattleInfo battle = xtable.Battle.get(battleID);
		if (null == battle)
			return false;
		if(battle.getRound() != round)
			return false;
		Set<Long> teamids = new HashSet<Long>(); 
		for(long rid :  battle.getRoleids().keySet())
		{
			Long tid = xtable.Roleid2teamid.select(rid);
			if(tid != null)
				teamids.add(tid);
		}
		lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, teamids));
		//handleoutbattle方法根本不会被调用  modified by cn
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, battle.getRoleids().keySet()));
		
		List<Long> roleids = new LinkedList<Long>();
		roleids.addAll(battle.getRoleids().keySet());
		for(long rid : roleids)
		{
			if(battle.getFeedbacks().contains(rid))
				continue;
			new PRoundPLayEnd(rid, new LinkedList<Integer>(),false).call();
		}
		
		List<Integer> rmIndex = new ArrayList<Integer>();
		//处理失败的怪物（包括主客方）
		BattleField.processFaileMonsters(battle,true);
		
		for (Map.Entry<Integer, Integer> e : battle.getRoundresult().entrySet() )
		{
			final int i = e.getKey();
			xbean.Fighter fighter = battle.getFighters().get( i );
			if(fighter == null)
			{//可能的情况：人逃跑后，宠物的fighter都删掉了，但是roundresult还在
				rmIndex.add(i);
				continue;
			}
			if (BattleField.checkOutBattle(battle, i))
			{
				//角色逃跑在PRoundPlayEnd中已处理，这里只有对中途出战斗的其他单位的处理
				try
				{   
					int petkey = -1;
					if (fighter.getFightertype() == xbean.Fighter.FIGHTER_PET) {
						int masterIndex = i - 5;
						petkey = new PropRole(battle.getFighters().get(masterIndex).getUniqueid(), true).getFightpetkey();
						
					}
					for (BattleEndHandler handler : battle.getBattleendhandlers())
					{
						handler.processOutBattle(i,petkey);
						rmIndex.add(i);
					}
				} catch (Exception ex)
				{
					BattleField.logger.debug("Runaway handle failed", ex);
				}
			}else{
				//宠物护身符——清晰雕纹效果
				if (fighter.getFightertype() == xbean.Fighter.FIGHTER_PET) {
					long masterId = battle.getFighters().get(i - 5).getUniqueid();
					int petkey = new PropRole(masterId, true).getFightpetkey();
					if (petkey < 0) {
						continue;
					}
				}
			}
		}
		for(int i : rmIndex)
		{
			battle.getRoundresult().remove(i);
		}
		if (battle.getBattleresult() != BattleField.BATTLE_NOT_END)
		{
			pexecuteWhileCommit(new PBattleEnd(battleID, battle.getBattleresult()));
			
			return true;
		}
		
		battle.getDecisions().clear();
		battle.getFeedbacks().clear();
		battle.getSkipeddeadfighters().clear();
		clearResult(battle);
		BattleField.sendRoundChangeResult(battle);
		battle.getProtectormap().clear();
		if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("当前回合结束了,还有战斗者数量:\t"	+ battle.getFighters().size());}
		//只要在场上，不管死亡/鬼魂/昏睡/混乱/封印，都跟正常活着一样
		List<Long> liveRoleids=new ArrayList<Long>();
		liveRoleids.addAll(battle.getRoleids().keySet());
		new PRoundStart(battleID, liveRoleids).call();
		return true;
	}

	/**除离开战斗状态外,全部清除*/

	private void clearResult(xbean.BattleInfo battle) {
		Map<Integer, Integer> roundresult = battle.getRoundresult();
		for (Integer i : roundresult.keySet()) {
			// 只要不是触发离开战斗的状态,统统清0
			if (BattleField.checkLiveInBattle(battle, i)) {
				roundresult.put(i, 0);
			}
			else
			{
				final Integer val = battle.getRoundresult().get(i);
				if (null == val)
					return;
				int result = 0;
				if ((val & ResultType.RESULT_DEATH) != 0)
				{
					result += ResultType.RESULT_DEATH;
				}
				if ((val & ResultType.RESULT_GHOST) != 0)
				{
					result += ResultType.RESULT_GHOST;
				}
				roundresult.put(i, result);//鬼魂与死亡状态要留下，跨回合的状态
			}

		}

	}
	
	private final long battleID;
	private final int round;
}
