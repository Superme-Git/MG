package fire.pb.option;

import fire.pb.battle.BattleField;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffConstant;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.fight.FightFieldSkill;
import fire.pb.skill.fight.FightMonsterSkill;
import fire.pb.skill.fight.FightPetSkill;
import fire.pb.skill.fight.FightRoleSkill;
import fire.pb.skill.fight.FightSkill;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.skill.fight.FightSpecialSkill;

/**使用技能*/
public class UseSkill extends BasicOperation {
	
	private final int skillID;
	private int skilllevel;//暂时只用于AI怪放技能时指定等级
	
	public UseSkill(final xbean.BattleInfo battle, final int operator, final int aim, final int skillID,final int skilllevel){
		this(battle, operator, aim,skillID);
		this.skilllevel = skilllevel;
		
	}
	public UseSkill(final xbean.BattleInfo battle, final int operator, final int aim, final int skillID){
		super(battle, operator, aim);
		this.skillID = skillID;
		this.skilllevel = 0;//默认为0
	}
	@Override
	public int getOperateID() {return skillID;}

	@Override
	public ResultItem getResultItem(){
		
		if(resultItem != null)
		{
			if(resultItem.execute.operationtype == OperationType.ACTION_FAILURE||resultItem.execute.operationtype == OperationType.ACTION_FAILURE_NO_WONDER)
				return resultItem;
		
			FightSkillConfig skillConfig = fire.pb.skill.Module.getInstance().getSkillId2configs().get(skillID);
			
			processActiveHidden();
			
			resultItem.execute.operationtype = OperationType.ACTION_SKILL;
			
			if(skillConfig.getType() == SkillConstant.BATTLE_SKILL_TYPE_CTRL_ATTACK)
			{
				resultItem.execute.operationtype = OperationType.ACTION_ATTACK;
				resultItem.execute.operationid = 0;
			}
			else if(skillConfig.getType() == SkillConstant.BATTLE_SKILL_TYPE_SUMMON)
				resultItem.execute.operationtype = OperationType.ACTION_SUMMON;
			else
				resultItem.execute.operationid = skillID;
		}
		return resultItem;
	}
	
	@Override
	public  java.util.LinkedList<fire.pb.battle.DemoResult> getDemoResult(){
		final java.util.LinkedList<fire.pb.battle.DemoResult> list = new java.util.LinkedList<fire.pb.battle.DemoResult>();
		
		return list;
	}

	@Override
	public boolean checkEffective()
	{
		//验证目标和换目标在技能执行时再验证
		return true;
	}
	
	@Override
	public int getResultType() {
		FightSkillConfig skillConfig = fire.pb.skill.Module.getInstance().getSkillId2configs().get(skillID);
		if(skillConfig.getType() == SkillConstant.BATTLE_SKILL_TYPE_SUMMON)
			return ResultType.RESULT_SUMMONPET;
		return 0;
	}

	@Override
	public int getType() 
	{
		return OperationType.ACTION_SKILL;
	}

	@Override
	public void process(){
		resultItem =null;
		if(!fire.pb.skill.Module.getInstance().isBattleSkill(skillID))
			return;
		try
		{
			if(opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FORGET_AIM))
				setAim(0);
			FightSkill skill = null;
			switch (opfighter.getFighterBean().getFightertype())
			{
			case xbean.Fighter.FIGHTER_ROLE:
				int firstnum = skillID/100000;
				if(opfighter.getSkillAgent().hasSkill(skillID))
				{
					if(firstnum == 1 || firstnum == 4 || firstnum==8)
					{
						FightSkillConfig skillConfig = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillID);
						if(skillConfig.getSkillBelongType() == 1)
							skill = new FightRoleSkill(getBattle(), getOperator(), getAim(), skillID, OperationType.ACTION_SKILL);
						else if(skillConfig.getSkillBelongType() == 2)
							skill = new FightRoleSkill(getBattle(), getOperator(), getAim(), skillID, OperationType.ACTION_SPECIAL_SKILL);
						else if(skillConfig.getSkillBelongType() == 4)
							skill = new FightRoleSkill(getBattle(), getOperator(), getAim(), skillID, OperationType.ACTION_UNIQUE_SKILL);
						else
							break;
					}
					else
						skill = new FightRoleSkill(getBattle(), getOperator(), getAim(), skillID, OperationType.ACTION_SKILL);
				}
				else
					break;
				resultItem = skill.process();
				break;
			case xbean.Fighter.FIGHTER_PET:
				if(opfighter.getSkillAgent().hasSkill(skillID)
					||(skillID == SkillConstant.BATTLE_PET_HIDDEN_FIRST && opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PASSIVE_HIDDEN_FIRST ))
					||(skillID == SkillConstant.BATTLE_PET_HIDDEN_SECOND && opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PASSIVE_HIDDEN_SECOND ))
					)
				{
					skill = new FightPetSkill(getBattle(), getOperator(), getAim(), skillID, OperationType.ACTION_SKILL);
					resultItem = skill.process();
				}
				break;
			case xbean.Fighter.FIGHTER_PARTNER:
			case xbean.Fighter.FIGHTER_MONSTER_HIDE:
			case xbean.Fighter.FIGHTER_MONSTER_NPC:
			case xbean.Fighter.FIGHTER_SYSTEM_PARTNER:			
			case 0: //战场场景
				if (skillID / 100000 == 8)// 战场技能
				{
					if (skilllevel == 0)
						skilllevel = 1;
					skill = new FightFieldSkill(getBattle(), 0, getAim(), skillID, OperationType.ACTION_SKILL, skilllevel);
				} else
				{

					if (skilllevel == 0)
					{
						xbean.Monster xmonster = getBattle().getMonsters().get(getOperator());
						for (xbean.MonsterSkill mskill : xmonster.getSkills())
						{
							if (mskill.getId() == skillID)
							{
								skilllevel = mskill.getSkilllevel();
								break;
							}
						}
						if (skilllevel == 0)
							skilllevel = 1;
					}
					skill = new FightMonsterSkill(getBattle(), getOperator(), getAim(), skillID, OperationType.ACTION_SKILL, skilllevel);
				}
				resultItem = skill.process();
				break;
			}
		}
		catch(IllegalArgumentException e)
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("Skill Error! SkillConfig not exist. SkillId = ").append(getOperateID()).append(";Operater = ").append(getOperator()).append(";Aim = ").append(getAim()),e);}
		}
		catch(Exception e)
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("Skill Error! Unknown reason. SkillId = ").append(getOperateID()).append(";Operater = ").append(getOperator()).append(";Aim = ").append(getAim()),e);}
		}
	}	
	
	@Override
	public boolean canChangeAim(){
		// TODO 根据技能返回
		return false;
	}
	@Override
	public String toString(){return getOperator() + "\t使用技能（id = \t" +skillID +")给\t"+ getAim();}
	

	
	
}
