package fire.pb.buff.single;

import fire.pb.attr.EffectType;
import fire.pb.attr.FightAttrType;
import fire.pb.battle.BattleField;
import fire.pb.skill.SkillConstant;
import fire.script.JavaScript;

/**使用物品治疗*/
public class ItemCure extends Cure
{

	private boolean checked = false;
	private float improve = 1f;
	
	public ItemCure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
		
	}
	@Override
	protected void handleAddHp()
	{
		//治疗量=（治疗强度+技能等级*技能伤害系数）*（1+治疗暴击量/1000）*（1+治疗效果修正+被治疗效果修正）*（1+治疗加深/1000+被治疗加深/1000）
		// 禁疗检查
		if (!isProhibitHeal)
		{
			JavaScript addhpscript = effects.get(EffectType.HP_ABL);
			JavaScript addhppctscript = effects.get(EffectType.HP_PCT);
			float faddhp = 0;	
			if (addhpscript != null)
				faddhp = (addhpscript.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue());
			faddhp= (faddhp*critpct);
			
			if (addhppctscript != null)
				faddhp= (float) (faddhp+ (addhppctscript.eval(battleInfo.getEngine(),opfighter,aimfighter) * aimfighter.getEffectRole().getMaxHp()));
			if (faddhp < 0)
			{
				if(addhpscript != null)
				{
					if(addhpscript.GetfunID() == -1)
						BattleField.logger.error("Error:addhp value is minus: " + faddhp +" addhpscript: " + addhpscript);
					else
						BattleField.logger.error("Error:addhp value is minus: " + faddhp +" funID: " + addhpscript.GetfunID());
				}
				else
					BattleField.logger.error("Error:addhp value is minus: " + faddhp +" addhpscript == null");
			}

			int addhp = amendAddHp(faddhp);
			aimfighter.healHp(addhp);
			demoresult.hpchange = addhp;
		}
	}
	
	@Override
	protected void handleChangeMp()
	{
		JavaScript changempscript = effects.get(EffectType.MP_ABL);
		JavaScript changemppctscript = effects.get(EffectType.MP_PCT);
		// 禁疗检查
		if (!isProhibitHeal)
		{
			float changemp = 0.0f;
			if (changempscript != null)
			{
				changemp = changempscript.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
			}
			changemp= (changemp*critpct);
			
			if (changemppctscript != null)
				changemp= (float) (changemp +changemppctscript.eval(battleInfo.getEngine(),opfighter,aimfighter) * aimfighter.getEffectRole().getMaxMp());

			int addmp = amendAddMp(changemp);
			demoresult.mpchange = aimfighter.attachMpChange(addmp);
		}
	}
	
	protected int amendChangeWound(int changewound)
	{
		checkImprove();
		return (int)(changewound * improve);
	}
	@Override
	protected int amendAddHp(float addhp)
	{
		checkImprove();
		return (int)(addhp * improve);
	}
	
	@Override
	protected void handleCrit(){}
	
	@Override
	protected int amendAddMp(float addmp)
	{
/*		if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.SPECIAL_SKILL_SHEN_NONG))
			return (int)(addmp * 1.1f);
		else*/
			return (int) addmp;
	}
	
	protected void checkImprove()
	{
		if (!checked)
		{
/*			if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.SPECIAL_SKILL_SHEN_NONG))
				improve = 1.1f;
*/			checked = true;
		}
	}
	
}
