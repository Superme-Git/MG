package fire.pb.option;

import java.util.LinkedList;
import java.util.Map;

import fire.pb.battle.BattleType;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffConstant;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.util.Parser.ID2Odds;

/** 瞬时逃跑*/
public class InstantRunaway extends BasicOperation {

	private final Map<Integer, ID2Odds> summons;//一起逃跑的怪ID，key为fighterId,value为monsterId
	private final LinkedList<DemoResult> demoresults = new LinkedList<DemoResult>();
	
	/**
	 * 
	 * @param battle
	 * @param operator
	 * @param summons
	 */
	public InstantRunaway(final xbean.BattleInfo battle, final int operator,final Map<Integer, ID2Odds> summons ){
		super(battle, operator, operator);
		this.summons = summons;
	}
	
	@Override
	public int getOperateID() {
		return 0;
	}

	@Override
	public  java.util.LinkedList<fire.pb.battle.DemoResult> getDemoResult(){
		return demoresults;
	}

	@Override
	public int getResultType() {
		//return ResultType.RESULT_RUNAWAY;
		return success ? ResultType.RESULT_RUNAWAY : 0;
	}

	@Override
	public int getType() {return OperationType.ACTION_ESCAPE_INSTANT ;}

	@Override
	public void process(){
		
		final xbean.Fighter f = getBattle().getFighters().get(getOperator());
		if(null == f)
			return;
		
		for(int fighterId : summons.keySet())
		{
			xbean.Fighter xfighter = getBattle().getFighters().get(fighterId);
			if(xfighter == null) continue;
			if(xfighter.getFightertype() < xbean.Fighter.FIGHTER_PARTNER) continue;
			DemoResult demo = new DemoResult();
			demo.targetid = fighterId;
			demo.targetresult = ResultType.RESULT_RUNAWAY;
			demoresults.add(demo);
			Integer result = getBattle().getRoundresult().get(f.getBattleindex());
			result = (result == null)? 0 : result;
			getBattle().getRoundresult().put(fighterId, result + ResultType.RESULT_RUNAWAY);
			Fighter fighter = getBattle().getFighterobjects().get(fighterId);
			if(getBattle().getBattletype() == BattleType.BATTLE_CLANBOSS && fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CLANBOSS))
			{
				InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(getBattle().getHostroleid(), false);
				if (instzone != null) {
					if (instzone instanceof FactionInstZone) {
						xbean.ClanInstances clanInst = ((FactionInstZone) instzone).getClanInstData();
						if (((FactionInstZone) instzone).checkBattleid(getBattle().getConfigid())) {
							xbean.ClanBossInfo bossinfo = clanInst.getBossinfo().get(getBattle().getConfigid());
							Fighter boss = getBattle().getFighterobjects().get(bossinfo.getFighteindx());
							DemoResult demoresult = new DemoResult();
							demoresult.targetid = bossinfo.getFighteindx();
							demoresult.targetresult = ResultType.RESULT_HPCHANGE;													
							boss.getEffectRole().setHp(boss.getEffectRole().getHp() - fighter.getFighterBean().getInihp());													
							demoresult.hpchange = -fighter.getFighterBean().getInihp();													
							demoresults.add(demoresult);
						}
					}
				}
			}
		}
		
	}
	
	/** （非 Javadoc）
	 * @see fire.pb.option.BasicOperation#checkEffective()
	 */
	public boolean checkEffective(){
		//瞬时逃跑
		success = true;
		return true;
		
	}
	
	/**
	 * （非 Javadoc）
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return getOperator() + "\t选择逃跑";
	}
	
	@Override
	public boolean canChangeAim(){return false;}
	
	private boolean success = false;
}
