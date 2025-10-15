package fire.pb.option;

import java.util.LinkedList;

import fire.pb.battle.BattleField;
import fire.pb.battle.BattleType;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffConstant;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.skill.SkillConstant;
import fire.pb.util.Misc;

/**逃跑*/
public class Runaway extends BasicOperation {

	public static final int DEFAULT_ESCAPE_ODDS = 65;
	private int escapeOdds = DEFAULT_ESCAPE_ODDS;//默认逃跑成功率为65%
	public Runaway(final xbean.BattleInfo battle, final int operator){
		super(battle, operator, operator);
	}
	
	public Runaway(final xbean.BattleInfo battle, final int operator, final int escapeOdds){
		super(battle, operator, operator);
		if(escapeOdds >= 0 )
			this.escapeOdds = escapeOdds;
	}
	@Override
	public int getOperateID() {
		return 0;
	}

	@Override
	public  java.util.LinkedList<fire.pb.battle.DemoResult> getDemoResult(){
		final LinkedList<DemoResult> list = new LinkedList<DemoResult>();
		final DemoResult r = new DemoResult();
		r.targetid = getAim();
		r.targetresult = getResultType();
		processActiveHidden();
		list.add(r);
		if(success)
		{
			Fighter fighter = getBattle().getFighterobjects().get(getOperator());
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
							list.add(demoresult);
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	public int getResultType() {
		//return ResultType.RESULT_RUNAWAY;
		return success ? ResultType.RESULT_RUNAWAY : 0;
	}

	@Override
	public int getType() {return OperationType.ACTION_ESCAPE;}

	@Override
	public void process(){
		
		final xbean.Fighter f = getBattle().getFighters().get(getOperator());
		if(null == f)
			return;
		
		if(! success)
			return ;
		
		if( f.getFightertype() == xbean.Fighter.FIGHTER_ROLE){
			Integer result = getBattle().getRoundresult().get(f.getBattleindex());
			result = (result == null)? 0 : result;
			getBattle().getRoundresult().put(f.getBattleindex(),result | ResultType.RESULT_RUNAWAY);
			
			final int petIndex = f.getBattleindex() + 5;
			if(!BattleField.checkOutBattle(getBattle(), petIndex))
			{
				Integer result2 = getBattle().getRoundresult().get(petIndex);
				result2 = (result2 == null) ? 0 : result2;
				getBattle().getRoundresult().put(petIndex, result2 | ResultType.RESULT_RUNAWAY);
				int petkey = xtable.Properties.selectFightpetkey(f.getUniqueid());
				f.getFightedpets().put(petkey, ResultType.RESULT_RUNAWAY);
			}
		}else if (f.getFightertype() == xbean.Fighter.FIGHTER_PET){
			final int roleIndex = f.getBattleindex() -5;
			if ( !getBattle().getFighters().containsKey(roleIndex))
				return ;
			
			xbean.Fighter owner = getBattle().getFighters().get(roleIndex);
			final long roleid = owner.getUniqueid();
			Integer result = getBattle().getRoundresult().get(f.getBattleindex());
			result = (result == null)? 0 : result;
			getBattle().getRoundresult().put(f.getBattleindex(), result | ResultType.RESULT_RUNAWAY);
			int petkey = xtable.Properties.selectFightpetkey(roleid);
			owner.getFightedpets().put(petkey, ResultType.RESULT_RUNAWAY);
			// 不需要修改战斗宠物设定,把fighter删除就可以了,fighter删除是在handleOutBattle中完成的
//			final PSetFightPetProc setproc = new PSetFightPetProc(roleid, 0, false);
//			setproc.call();
		}else if(f.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER){
			Integer result = getBattle().getRoundresult().get(f.getBattleindex());
			result = (result == null)? 0 : result;
			getBattle().getRoundresult().put(f.getBattleindex(), result | ResultType.RESULT_RUNAWAY);
		}		
	}
	
	public boolean checkEffective(){
		if(!checkOperator())
			return false;
		//如果是瞬时逃跑，或者几率大于等于100，就不再进行后面的计算
		if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.SPECIAL_SKILL_ESCAPE_EXPERT))
			escapeOdds = 100;
		
		if(escapeOdds >= 100)
		{
			success = true;
			return true;
		}
		
		if(!getBattle().getCanrunaway())
		{
			if(opfighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE || opfighter.getFightertype() == xbean.Fighter.FIGHTER_PET)
				return true;//该战斗不允许逃跑，逃跑失败
		}

		if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(opfighter.getFighterId() +"选择逃跑， TA有百分之" + escapeOdds+"的几率能跑掉");}
		if(Misc.checkRatePercent(escapeOdds)){
			success = true;
			return true;
		}
		
		return true;
	}
	public String toString(){
		return getOperator() + "\t选择逃跑";
	}
	
	@Override
	public boolean canChangeAim(){return false;}
	
	private boolean success = false;
}
