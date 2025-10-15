package fire.pb.skill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xbean.BattleInfo;
import xbean.PracticeSkill;
import fire.pb.PropRole;
import fire.pb.battle.Fighter;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffMonsterImpl;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.item.Equip;
import fire.pb.item.ItemBase;
import fire.pb.school.SchoolConst;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.script.FightJSEngine;
import fire.script.JavaScript;


//宠物技能代理的实现
public class SkillMonster extends SkillAgent
{
	
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("SKILL");
	private final xbean.Monster monsterbean;
	private final long roleid; // 老大的ID 当这个怪物是伙伴的时候,能用到 by changhao

	public SkillMonster(xbean.Monster monsterbean, long roleid)
	{
		this.monsterbean = monsterbean;
		this.agentType = monsterbean.getFightnpctype();//xbean.Fighter.FIGHTER_MONSTER;
		this.roleid = roleid;
	}
	
	@Override
	public boolean hasEffectSkill(int skillId)
	{
		return getLevel(skillId) > 0;
	}
	
	@Override
	public boolean hasSkill(int skillId)
	{
		return getLevel(skillId) > 0;
	}
	
	//检查多个技能，当所有都拥有时才返回true
	@Override
	public boolean hasAllSkills(int... skillIds)
	{
		for(int skillId : skillIds)
		{
			if(!hasSkill(skillId))
				return false;
		}
		return true;
	}
	
	//检查多个技能，当拥有一个就返回true
	@Override
	public boolean hasAnySkill(int... skillIds)
	{
		for(int skillId : skillIds)
		{
			if(hasSkill(skillId))
				return true;
		}
		return false;
	}
	
	@Override
	public int getLevel(int skillId)
	{
		
		for(xbean.MonsterSkill skill : monsterbean.getSkills())
		{
			if(skill.getId() == skillId)
				return skill.getSkilllevel();
		}
		
		if (roleid != 0)
		{
			SkillRole skillrole = new SkillRole(roleid, true);
			
			int skillType = skillId / 100000;
			if (skillType == SkillConstant.TYPE_ROLE_PRACTICE_SKILL) //修炼技能等级 by changhao
			{
				int id = LiveSkillManager.getInstance().GetPracticePassivenessSkillLevelBattleSkill(skillId);	
				PracticeSkill skill = skillrole.getPracticeSkill(id);
				if (skill != null)
				{
					return skill.getLevel();
				}
				else
				{
					return 0;
				}			
			}				
		}	
		
		return 0;
	}
	
	@Override
	public List<Integer> getAllBattleSkills()
	{
		List<Integer> skillIds = new ArrayList<Integer>();
		for(xbean.MonsterSkill skill : monsterbean.getSkills())
		{
			skillIds.add(skill.getId());
		}
		
		//如果是伙伴 by changhao
		if (monsterbean.getFightnpctype() == xbean.Fighter.FIGHTER_PARTNER && roleid != 0) //队长的修炼技能 应用到伙伴身上 by changhao
		{
			//增加修炼技能对应的被动技能 by changhao
			SkillRole skillrole = new SkillRole(roleid, true);
			skillIds.addAll(LiveSkillManager.getInstance().GetAllPracticeSkillBattleSkill(skillrole, 2));			
		}
		
		return skillIds;
	}
	
	public boolean addSkill(int skillId)
	{
		return true;
	}
	
	
	public xbean.Monster getMonsterbean()
	{
		return monsterbean;
	}
	
	

	//战斗外加载被动技能
	public Result addPetSkillBuffsWhileOnline(xbean.BattleInfo battle)
	{
		Result result = new Result(true);
		FightJSEngine fightJSEngine = null;
		boolean binitJsEngine = false;
		BuffAgent buffpet = new BuffMonsterImpl(monsterbean);
		//找出需要上线加载的buff
		for(int skillId : getAllBattleSkills())
		{
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
			if(sconf == null)
			{
				Module.logger.error("怪物 "+monsterbean.getMonsterid()+" 的FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());
				continue;
			}
			if(sconf.isActiveSkill()||sconf.isUseInBattle()||!hasEffectSkill(skillId))
				continue;
			for (SubSkillConfig subskill : sconf.getSubSkills())
			{
				for (BuffUnit buffarg : subskill.getBuffUnits())
				{
					if (buffarg == null)
						continue;
					if(buffarg.buffIndex/10000 == 51)
						continue;

					ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);;
					if(binitJsEngine == false)
					{
						if(battle == null)
						{
							fightJSEngine = new FightJSEngine();
						}
						else
							fightJSEngine = battle.getEngine();	
						binitJsEngine = true;
					}
					
					if(fightJSEngine!=null){
						fightJSEngine.setSkillLevel(getLevel(skillId));
						fightJSEngine.setGrade(monsterbean.getLevel());
					}

					for (Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
					{// buff效果
						float addvalue = entry.getValue().eval(fightJSEngine,null,null).floatValue();
						if (addvalue != 0)
							buff.getEffects().put(entry.getKey(), addvalue);
					}
					result.updateResult(buffpet.addCBuff(buff));
				}
			}
		}
		return result;
	}
	
	//进战斗加载宠物被动技能
	public Result addPetSkillBuffWhileBattle(xbean.BattleInfo battle, Fighter ofighter)
	{
		battle.getEngine().setGrade(monsterbean.getLevel());
		BuffAgent buffpet = new BuffMonsterImpl(monsterbean);
		Result result = new Result(true);
		boolean bregfighter = false;
		for(int skillId : getAllBattleSkills())
		{
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
			if(sconf == null)
			{
				if (logger.isDebugEnabled()) {logger.debug("怪物 "+monsterbean.getMonsterid()+" FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());}
				continue;
			}
			if(!sconf.isActiveSkill() && sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_PASSIVE 
					&& sconf.getSubSkills()!=null && sconf.getSubSkills()[0]!=null && sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
			{//被动技能，且有buff(一定是ConstantlyBuff)
				if(!hasEffectSkill(skillId))
					continue;
				if(bregfighter == false)
				{
					battle.getEngine().setOpFighter(ofighter,sconf.getUsedAttrTypea());// 设置技能释放者参数
					bregfighter = true;
					
				}
				battle.getEngine().setSkillLevel(getLevel(skillId));
				
				for (SubSkillConfig subskill : sconf.getSubSkills())
				{
					for (BuffUnit buffarg : subskill.getBuffUnits())
					{
						if(buffarg==null) continue;
						ConstantlyBuff buff =fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);
						if(buff==null) continue;
						int nround = buffarg.roundJavascript.eval(battle.getEngine(),ofighter,null).intValue();
						if(nround != 0 )
							buff.setRound(nround);
						for(Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
						{
							buff.getEffects().put(entry.getKey(), entry.getValue().eval(battle.getEngine(),ofighter,null).floatValue());
						}
						result.updateResult(buffpet.addCBuff(buff));
					}
				}
			}				
		}
		if(monsterbean.getSchool() == SchoolConst.HUNTER)
		{
			ConstantlyBuff buff =fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_BEHUNTER_BOW);
			buff.setRound(-1);//被动战斗技能buff只有回合，不设置时间和量
			result.updateResult(buffpet.addCBuff(buff));
		}
		return result;
	}

	@Override
	public Result addSkillBuffWhileBattle(BattleInfo battle, fire.pb.battle.Fighter ofighter)
	{
		return addPetSkillBuffWhileBattle(battle,ofighter);
	}

	@Override
	public Result addSkillBuffWhileOnline(xbean.BattleInfo battle)
	{
		return addPetSkillBuffsWhileOnline(battle);
	}
}
