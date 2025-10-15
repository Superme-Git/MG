package fire.pb.buff.single;

import java.util.HashMap;
import java.util.Map;

import fire.pb.PropConf;
import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.FightAttrType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffConstant;
import fire.pb.effect.PetImpl;
import fire.pb.effect.RoleImpl;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;

public class Cure extends SingleBuff
{//治疗
	
	protected boolean isProhibitHeal= false;
	float DamageFloatValue = PropConf.Battle.DAMAGE_FLOAT_VALUE;
	
	public Cure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}

	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter,FightSkill battleskill, Map<Integer, JavaScript> effects)
	{
		init(battleInfo, opfighter, aimfighter, effects);
		
		// 治疗
		cure();

		end();
		return demoresult;
	}

	protected void handleProhibitHeal()
	{
		if(	aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH)
				|| aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST)
				|| aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_FIRST))
			isProhibitHeal = true;
	}

	protected void cure()
	{
		if(effects==null)
			return;
		handleProhibitHeal();
		
		handleComboCount();
		
		handleCrit();
		// 加血
		handleAddHp();
		// 改变蓝
		handleChangeMp();
		// 改变怒气
		handleChangeSp();
		// 百分比改变怒气
		handleChangeSpPct();

	}

	private void handleComboCount()
	{
		Double o = battleInfo.getEngine().getDouble("attacknuma");
		if(o == null)
			battleInfo.getEngine().setSkillComboCurCount(1);
		else
		{
			int curComboCount = o.intValue();
			curComboCount++;
			battleInfo.getEngine().setSkillComboCurCount(curComboCount);
		}
	}
	
	protected void handleCrit()
	{
		//治疗暴击率=（治疗暴击-治疗抗暴）/1000+(治疗暴击几率修正-被治疗暴击几率修正）  
		float critRate = (opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_RATE)+aimfighter.getEffectRole().getAttrById(FightAttrType.DEEP_HEAL_CRITC_LEVEL))/1000+
				(opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_CRIT_LEVEL) +aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_HEAL_CRIT_LEVEL));
		critRate = Math.max(critRate, PropConf.Battle.MIX_HEAL_CRIT_RATE);
		if(Math.random() < critRate){
			critpct = (float) (1.0 + opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_DEGREE)/1000);
			demoresult.targetresult |= ResultType.RESULT_CRITIC;
		}
	}
	
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
			faddhp= (faddhp*(1+opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_EFFECT_RATE) +aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_HEAL_EFFECT_RATE))
					*(1+opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_DEEP_RATE)/1000+
							aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_HEAL_DEEP_RATE)/1000));
			
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
			/*addhp = */aimfighter.healHp(addhp);
			demoresult.hpchange = addhp;
		}
	}

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
			changemp= (changemp*(1+opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_EFFECT_RATE) +aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_HEAL_EFFECT_RATE))
					*(1+opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_DEEP_RATE)/1000+
							aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_HEAL_DEEP_RATE)/1000));
			
			if (changemppctscript != null)
				changemp= (float) (changemp +changemppctscript.eval(battleInfo.getEngine(),opfighter,aimfighter) * aimfighter.getEffectRole().getMaxMp());

			int addmp = amendAddMp(changemp);
			demoresult.mpchange = aimfighter.attachMpChange(addmp);
		}
	}

	protected void handleChangeSp()
	{
		JavaScript changespscript = effects.get(EffectType.SP_ABL);
		if (changespscript != null)
		{
			int changesp = changespscript.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
			aimfighter.getEffectRole().addSp(changesp, 0);
			demoresult.spchange += changesp;
			aimfighter.getRoundBuffResult().getChangedAttrs().put(AttrType.SP, (float) aimfighter.getEffectRole().getSp());
		}
	}

	protected void handleChangeSpPct()
	{
		JavaScript changesppctscript = effects.get(EffectType.SP_PCT);
		if (changesppctscript != null)
		{
			int changesp = (int) (changesppctscript.eval(battleInfo.getEngine(),opfighter,aimfighter) * aimfighter.getEffectRole().getMaxSp());
			aimfighter.getEffectRole().addSp(changesp, 0);
			demoresult.spchange += changesp;
			aimfighter.getRoundBuffResult().getChangedAttrs().put(AttrType.SP, (float) aimfighter.getEffectRole().getSp());
		}
	}

	protected int amendChangeWound(int changewound)
	{
		return changewound;
	}

	protected int amendAddHp(float addhp)
	{
		DamageFloatValue = PropConf.Battle.DAMAGE_FLOAT_VALUE + opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_FLOAT_VALUE);
		float damgefloat =(float) (1.0f+ randomValueInScale(DamageFloatValue, -1.0f,1.0f));	
		addhp = (int)(addhp*damgefloat); 
		return (int) (addhp);		
	}
	
	protected int amendAddMp(float addmp)
	{
		return (int)randomValueInScale(addmp,0.95f,1.05f);
	}
	
	
	
	public void useOutBattleWithSP(fire.pb.effect.Role erole,Map<Integer, Float> effects)
	{
		Map<Integer,Float> changedAttrs = useOutBattle(erole, effects);
		if(changedAttrs.size() != 0)
		{
			if(erole instanceof RoleImpl)
			{
				SRefreshRoleData snd = new SRefreshRoleData();
				snd.datas.putAll(changedAttrs);
				mkdb.Procedure.psendWhileCommit(((RoleImpl)erole).getRoleId(), snd);
			}
			else if(erole instanceof PetImpl)
			{
				SRefreshPetData petdata = new SRefreshPetData();
				petdata.columnid = PetColumnTypes.PET;
				petdata.petkey = ((PetImpl)erole).getPetkey();
				petdata.datas.putAll(changedAttrs);
				mkdb.Procedure.psendWhileCommit( ((PetImpl)erole).getRoleId(), petdata);
			}
		}
	}
	
	/**生效一次性buff，由BuffAgent调用add类方法，不要直接使用	 */
	public Map<Integer,Float> useOutBattle(fire.pb.effect.Role erole,Map<Integer, Float> effects)
	{
		
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		
		for(Map.Entry<Integer, Float> entry: effects.entrySet())
		{
			switch(entry.getKey())
			{
			case EffectType.HP_ABL:
				erole.addHp(entry.getValue().intValue());
				changedAttrs.put(AttrType.HP, (float)erole.getHp());
				break;
			case EffectType.HP_PCT:
				erole.addHp((int)(erole.getMaxHp() * entry.getValue()));
				changedAttrs.put(AttrType.HP, (float)erole.getHp());
				break;				
			case EffectType.MP_ABL:
				erole.addMp(entry.getValue().intValue());
				changedAttrs.put(AttrType.MP, (float)erole.getMp());
				break;
			case EffectType.MP_PCT:
				erole.addMp((int)(erole.getMaxMp() * entry.getValue()));
				changedAttrs.put(AttrType.MP, (float)erole.getMp());
				break;
			}
		}
		return changedAttrs;
	}
}
