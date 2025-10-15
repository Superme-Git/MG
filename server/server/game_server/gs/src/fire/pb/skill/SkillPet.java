package fire.pb.skill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;











import xbean.PracticeSkill;
import fire.pb.battle.BattleField;
import fire.pb.battle.Fighter;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffPetImpl;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.main.ConfigManager;
import fire.pb.pet.Pet;
import fire.pb.skill.SkillConstant.BasicSkillBelongs;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.script.FightJSEngine;
import fire.script.JavaScript;
//pet skill
public class SkillPet extends SkillAgent
{
	private long roleid; //主人的id by changhao
	private Pet pet;
	
	public SkillPet(xbean.PetInfo petinfo, long roleid)
	{
		pet = Pet.getPet(petinfo);
		this.agentType = xbean.Fighter.FIGHTER_PET;
		this.roleid = roleid;
	}
	
	//检查生效的技能（对于宠物来说，高级存在时，低级不生效）
	@Override
	public boolean hasEffectSkill(int skillId)
	{
		SSkillConfig cfg = ConfigManager.getInstance().getConf(SSkillConfig.class).get(skillId);
		if(cfg == null)
			return false;
		if(cfg.isActive)
			return getLevel(skillId) > 0;		
		return getLevel(skillId) > 0;
	}
	
	@Override
	public boolean hasSkill(int skillId){
		return getLevel(skillId) > 0;
	}
	
	//检查多个技能，当所有都拥有时才返回true
	@Override
	public boolean hasAllSkills(int... skillIds)
	{
		for(int skillId : skillIds)
		{
			if(!pet.hasSkill(skillId))
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
			if(pet.hasAnySkill(skillId))
				return true;
		}
		return false;
	}
	
	@Override
	public int getLevel(int skillId)
	{
		SkillRole srole = new SkillRole(roleid, true);
		
		if(skillId>=100000 && skillId < 110000){
			BasicSkillBelongs user = SkillConstant.basicSkillUsers.get(skillId);
			if(user == null)
				return 0;
			if(user.isBelongToPet())
				return 1;
			else 
				return 0;
		}else if(skillId > 200000 && skillId < 210000){
			return pet.getSkillLevel(skillId);
		}
		else
		{
			int skillType = skillId / 100000;
			if (skillType == SkillConstant.TYPE_ROLE_PRACTICE_SKILL) //修炼技能等级 by changhao
			{
				int id = LiveSkillManager.getInstance().GetPracticePassivenessSkillLevelBattleSkill(skillId);				
				PracticeSkill skill = srole.getPracticeSkill(id);
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
		
		if(pet.hasSkill(skillId))
			return 1;
		else
			return 0;
	}
	
	@Override
	public List<Integer> getAllBattleSkills()
	{
		List<Integer> skillIds = new ArrayList<Integer>();
		//给宠物加上对应的修炼被动技能 by changhao
		SkillRole srole = new SkillRole(roleid, true);
		skillIds.addAll(fire.pb.skill.liveskill.LiveSkillManager.getInstance().GetAllPracticeSkillBattleSkill(srole, 1));
		skillIds.addAll(pet.getBattleskillIds());
		return skillIds;
	}
	
	//在战场外面更新宠物被动技能 by changhao
	public Result updateSkillBuffWhileOut(xbean.BattleInfo battle)
	{
		Result result = new Result(true);
		FightJSEngine fightJSEngine = null;
		boolean binitJsEngine = false;
		BuffAgent buffpet = new BuffPetImpl(pet.getPetInfo());
		
		//找出需要上线加载的buff
		for(int skillId : getAllBattleSkills())
		{
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
			if(sconf == null)
			{
				Module.logger.error("宠物的FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());
				continue;
			}
			if(sconf.isActiveSkill()
					|| sconf.getType() != SkillConstant.BATTLE_SKILL_TYPE_PASSIVE
					|| sconf.getSubSkills()[0]==null 
					|| sconf.getSubSkills()[0].getBuffUnits()[0]==null)
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
					if (buff == null)
					{
						BattleField.logger.error("updateSkillBuffWhileOut 技能ID = " + skillId+"中有buff无法生成实例，buffid = "+ buffarg.buffIndex);
						continue;
					}
					if(binitJsEngine == false)
					{
						if(battle == null)
						{
							fightJSEngine = new FightJSEngine();
						}else{
							fightJSEngine = battle.getEngine();	
						}
						if(fightJSEngine!=null){
							fightJSEngine.setGrade(pet.getLevel());
						}
						binitJsEngine = true;
					}
					if(fightJSEngine!=null){
						fightJSEngine.setSkillLevel(getLevel(skillId));	
					}
					int nround = buffarg.roundJavascript.eval(fightJSEngine,null,null).intValue();
					if(nround != 0 )
						buff.setRound(nround);//被动战斗技能buff只有回合，时间和量不设置
					if(buffarg.effectJavascriptMap.size() > 0)
					{
						for (Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
						{// buff效果
							buff.getEffects().put(entry.getKey(), entry.getValue().eval(fightJSEngine,null,null).floatValue());
						}
					}
					result.updateResult(buffpet.addCBuff(buff));
				}
			}
		}
		return result;
	}
	
	//战斗外加载宠物被动技能
	public Result addSkillBuffWhileOnline(xbean.BattleInfo battle)
	{
		return updateSkillBuffWhileOut(battle);
	}
	
	public Result addSkillBuffWhileBattle(xbean.BattleInfo battle, Fighter ofighter)
	{
		BuffAgent buffpet = new BuffPetImpl(pet.getPetInfo());
		Result result = new Result(true);
		for(int skillId : getAllBattleSkills())
		{			
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
			if(sconf == null)
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());}
				continue;
			}
			boolean bregfighter = false;
/*			if(!sconf.isActiveSkill() && sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_PASSIVE 
					&& sconf.getSubSkills()!=null && sconf.getSubSkills()[0]!=null && sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
			{//被动技能，且有buff(一定是ConstantlyBuff)
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
						if(buffarg==null)
							continue;
						ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);
						int nround = buffarg.roundJavascript.eval(battle.getEngine(),ofighter,null).intValue();
						if(nround != 0 )
							buff.setRound(nround);//被动战斗技能buff只有回合，时间和量不设置
						for(Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet())
						{//buff效果
							buff.getEffects().put(entry.getKey(), entry.getValue().eval(battle.getEngine(),ofighter,null).floatValue());
						}
						result.updateResult(buffpet.addCBuff(buff));
					}
				}
			}	*/			
		}
		return result;
		//将战斗被动技能Buff发给客户端
	}
}
