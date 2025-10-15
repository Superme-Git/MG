package fire.script;

import java.util.List;

import fire.pb.PropConf;

public class FightJSEngine extends AbstractJSEngine
{
	boolean isInitrand = false;
	
	public void setBattleType(int battleType)
	{
		if(battleType % 10 == 0)//pve
			put("pve", true);
		else if(battleType % 10 == 1)//pvp
			put("pve", false);
		else
		{
			put("pve", true);
			logger.error("FightJSEngine.setBattleType中传入参数为无法找到的类型:"+ battleType);
		}
	}

	public void setOpFighter(fire.pb.battle.Fighter opfighter, List<Integer> usedattrtypea)
	{
  		put("gradea", opfighter.getEffectRole().getLevel()); 		// 等级
	  	if(PropConf.Battle.DEBUG_FORMULA_MODULE == 1)
	  	{
			for (int attrtype: usedattrtypea)
			{
				if(attrtype == fire.pb.effect.Module.extfightAttrTypeIds[0])
				{
					put("curhpa", opfighter.getEffectRole().getHp());				// hp
				}
				else if(attrtype == fire.pb.effect.Module.extfightAttrTypeIds[1])
				{
					put("curmpa", opfighter.getEffectRole().getMp());				// mp 
				}
				else if(attrtype == fire.pb.effect.Module.extfightAttrTypeIds[2])
				{
					put("curspa", opfighter.getEffectRole().getSp());				// mp 
				}
				else
				{
					String name = fire.pb.effect.Module.getInstance().getEffectNameById(attrtype);
					float value = opfighter.getEffectRole().getAttrById(attrtype);
					if(name != null)
						put(name+"a", value); 		// 等级
				}
			}
	  	}
	}
	
	public void setOpFighter(fire.pb.battle.Fighter opfighter)
	{
	  	put("gradea", opfighter.getEffectRole().getLevel()); 		// 等级

	  	if(PropConf.Battle.DEBUG_FORMULA_MODULE == 1)
	  	{
			for (int i = 0; i < fire.pb.effect.Module.fightAttrTypeIds.length; i++)
			{
				String name = fire.pb.effect.Module.getInstance().getEffectNameById(fire.pb.effect.Module.fightAttrTypeIds[i]);
				float value = opfighter.getEffectRole().getAttrById(fire.pb.effect.Module.fightAttrTypeIds[i]);
				if(name != null)
					put(name+"a", value); 		// 等级
			}
			put("curhpa", opfighter.getEffectRole().getHp());				// hp
			put("curmpa", opfighter.getEffectRole().getMp());				// mp 
			put("curspa", opfighter.getEffectRole().getSp());				// mp 
	  	} 
	}
	
	public void setSkillLevel(int skilllv)
	{
		put("skilllevela", skilllv);
		put("skilllevel", skilllv);
		put("mineffect", 0);
	}

	public void setSkillPreAimCount(int num)
	{
		put("preaimcount", num);
		put("preaimcount", num);
	}
	
	public void setSkillTargetNum(int num)
	{
		put("effectnuma", num);
		put("effectnum", num);
	}
	
	public void setMainDamage(int maindamage)
	{
		put("maindamage", maindamage);
	}

	public void setSurvival(int friends,int friendnopets,int enemys, int enemynopets)
	{
		put("survivala", friends);
		put("survivalnopeta", friendnopets);
		put("survivalb", enemys);
		put("survivalnopetb", enemynopets);
	}

	
	public void setSkillComboCurCount(int curCount)
	{
		put("attacknuma", curCount);
		put("attacknum", curCount);
	}
	
	public void setAimFighter(fire.pb.battle.Fighter aimfighter, List<Integer> usedattrtypeb)
	{
 		put("gradeb", aimfighter.getEffectRole().getLevel());
	  	if(PropConf.Battle.DEBUG_FORMULA_MODULE == 1)
	  	{
			for (int attrtype: usedattrtypeb)
			{
				if(attrtype == fire.pb.effect.Module.extfightAttrTypeIds[0])
				{
					put("curhpb", aimfighter.getEffectRole().getHp());				// hp
				}
				else if(attrtype == fire.pb.effect.Module.extfightAttrTypeIds[1])
				{
					put("curmpb", aimfighter.getEffectRole().getMp());				// mp 
				}
				else if(attrtype == fire.pb.effect.Module.extfightAttrTypeIds[2])
				{
					put("curspb", aimfighter.getEffectRole().getSp());				// sp 
				}
				else
				{
					String name = fire.pb.effect.Module.getInstance().getEffectNameById(attrtype);
					float value = aimfighter.getEffectRole().getAttrById(attrtype);
					if(name != null)
						put(name+"b", value); 		// 等级
				}
			}
	  	}
	}	
	
	public void setAimFighter(fire.pb.battle.Fighter aimfighter, boolean isNight)
	{
 		put("gradeb", aimfighter.getEffectRole().getLevel());
	  	if(PropConf.Battle.DEBUG_FORMULA_MODULE == 1)
	  	{
			for (int i = 0; i < fire.pb.effect.Module.fightAttrTypeIds.length; i++)
			{
				String name = fire.pb.effect.Module.getInstance().getEffectNameById(fire.pb.effect.Module.fightAttrTypeIds[i]);
				float value = aimfighter.getEffectRole().getAttrById(fire.pb.effect.Module.fightAttrTypeIds[i]);
				if(name != null)
				put(name+"b", value); 		// 等级
			}
			put("curhpb", aimfighter.getEffectRole().getHp());				// hp
			put("curmpb", aimfighter.getEffectRole().getMp());				// mp 
			put("curspb", aimfighter.getEffectRole().getSp());				// sp
	  	}
	}
	
	public void setItemQuality(Integer quality)
	{
		if(quality != null)
			put("quality", quality);
	}
	
	
	public void setGrade(int grade)
	{
		put("gradea", grade);
	}
	
	public void setHostRoleNum(int num)
	{
		put("TeamNum",num);
	}
	
	public void setFriendLv(int friendlv)
	{
		put("friendlva", friendlv);
	}
	
	public void setBattleRound(int battleRound)
	{
		put("battleround", battleRound);//战斗回合数
	}
	public void setFighterRound(int fighterRound)
	{
		put("fighterround", fighterRound);//战斗者出现数
	}
	public void setAiRound(int aiRound)
	{
		put("airound", aiRound);//ai生效回合数
	}
	public void setTunaLevel(int tunalv)
	{
		put("tunaa", tunalv);
	}
	public void setJianshenLevel(int jianshenlv)
	{
		put("jianshena", jianshenlv);
	}
	
}
