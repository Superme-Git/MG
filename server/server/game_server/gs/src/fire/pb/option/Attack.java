package fire.pb.option;

import java.util.LinkedList;

import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.buff.BuffConstant;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.fight.FightMonsterSkill;
import fire.pb.skill.fight.FightPetSkill;
import fire.pb.skill.fight.FightRoleSkill;
import fire.pb.skill.fight.FightSkill;

/**普攻*/
public class Attack extends BasicOperation {

	public Attack(final xbean.BattleInfo battle, final int operator, final int aim){
		super(battle, operator, aim);
	}
	
	@Override
	public int getOperateID() {return 0;}

	@Override
	public LinkedList<DemoResult> getDemoResult(){
		//此方法无用
		final LinkedList<DemoResult> list = new LinkedList<DemoResult>();
		return list;
	}

	@Override
	public int getResultType() {
		return aimResult;
	}

	@Override
	public int getType() {return OperationType.ACTION_ATTACK;}

	public String toString(){return getOperator() + "\t攻击\t" + getAim();}
	

	
	public int getProtectorResult(){
		return proResult;
	}
	
	int getHPChange(){
		return 100;
	}
	
	@Override
	public boolean canChangeAim(){return true;} // 攻击会换对象
	
	private int aimResult = 0;
	private int proResult = 0;
	
	
	@Override
	protected boolean checkAim()
	{
		if(BattleField.checkOutBattle(getBattle(), getAim()))
			return false;
		
		Fighter fighter = getBattle().getFighterobjects().get(getAim());
		
		if(!fighter.canBeAction(getType()))
			return false;
		
		return true;
	}
	
	
	
	//private ResultItem resultItem;
	
	public ResultItem getResultItem(){
		if(resultItem.execute.operationtype == OperationType.ACTION_FAILURE||resultItem.execute.operationtype == OperationType.ACTION_FAILURE_NO_WONDER)
			return resultItem;
		processActiveHidden();
		return resultItem;
	}
	@Override
	public void process(){
		if(opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FORGET_AIM))
			setAim(0);

		Fighter opfighter = getBattle().getFighterobjects().get(getOperator());
		Fighter aimfighter = getBattle().getFighterobjects().get(getAim());
		if(opfighter == null)
		{
			resultItem = null;
			return;
		}
		FightSkill skill = null;
		if(aimfighter ==null ||opfighter.getFighterBean().getIshost() != aimfighter.getFighterBean().getIshost())
			skill = new fire.pb.skill.fight.FightAttack(getBattle(),getOperator(),getAim(),SkillConstant.BATTLE_SKILL_BASIC_ATTACK,OperationType.ACTION_ATTACK);
		else
		{
			//强制攻击时相当于释放技能
			switch (opfighter.getFighterBean().getFightertype())
			{
			case xbean.Fighter.FIGHTER_ROLE:
				skill = new FightRoleSkill(getBattle(), getOperator(), getAim(), SkillConstant.BATTLE_SKILL_BASIC_CTRL_ATTACK, OperationType.ACTION_ATTACK);
				break;
			case xbean.Fighter.FIGHTER_PET:
				skill = new FightPetSkill(getBattle(), getOperator(), getAim(), SkillConstant.BATTLE_SKILL_BASIC_CTRL_ATTACK, OperationType.ACTION_ATTACK);
				break;
		//	default:// xbean.Fighter.FIGHTER_MONSTER:
			case xbean.Fighter.FIGHTER_PARTNER:
			case xbean.Fighter.FIGHTER_MONSTER_HIDE:
			case xbean.Fighter.FIGHTER_MONSTER_NPC:
			case xbean.Fighter.FIGHTER_SYSTEM_PARTNER:			
				skill = new FightMonsterSkill(getBattle(), getOperator(), getAim(), SkillConstant.BATTLE_SKILL_BASIC_CTRL_ATTACK, OperationType.ACTION_ATTACK);
				break;
			}
		}
		if(skill != null)
		{
			resultItem = skill.process();
			if (resultItem == null)
				return;
			if (resultItem.execute.operationtype == OperationType.ACTION_FAILURE||resultItem.execute.operationtype == OperationType.ACTION_FAILURE_NO_WONDER)
				return;
			if (resultItem != null)
			{	
				resultItem.execute.operationtype = getType();
				//resultItem.execute.operationid = 0;
			}
		}
		else
			resultItem = null;
	}

}




