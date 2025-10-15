package fire.pb.buff.single;

import fire.pb.PropConf;
import fire.pb.attr.FightAttrType;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultType;
import fire.pb.skill.SkillConstant;

public class MagicInjure extends Injure
{
	int minmagicdamage = 1;
	public static final float INIT_MAGIC_CTIR_RATE = fire.pb.effect.Module.getInstance().getInitValueByAttrId(FightAttrType.MAGIC_CRIT_RATE); 

	public MagicInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}

	public void setMinmagicdamage(int minmagicdamage)
	{
		this.minmagicdamage = minmagicdamage;		
	}
	
	@Override
	protected void processInjure()
	{
		if(opfighter.isDoubleSkill())
			InjureRate = opfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_COMBO_ATTACK_PCT);
		//设置攻击的个数
		handleComboCount();
		//检查吸收
		checkAbsorb();
		//修改忽略防御参数
		handleIgnoreDefend();
		// 修改暴击参数
		handleCrit();
		// 计算效果
		handleEffect();
		// 计算伤害
		handleInjure();		
		// 处理物理伤害
		demoresult.hpchange = (int) amendMagicInjure(demoresult.hpchange);
		//处理目标，掉血，吸收等
		handleAim();
	}

	protected double amendMagicInjure(double magicInjure)
	{
		return amendInjure(magicInjure);
	}

	@Override
	protected void handleIgnoreDefend()
	{
		float fignoredefend = opfighter.getEffectRole().getAttrById(FightAttrType.IGNORE_MAGIC_DEFEND_RATE);
		if (Math.random() < fignoredefend)
		{
			ignorepct = opfighter.getEffectRole().getAttrById(FightAttrType.IGNORE_MAGIC_DEFEND_PCT);
			demoresult.targetresult |= ResultType.RESULT_IGNORE_PHYDIC_EFEN;			
		}
	}
	
	/**是否暴击*/
	@Override
	protected boolean isCrit()
	{
		//法术暴击率=（法术暴击-法术抗暴）/1000+(法术暴击几率修正-被法术暴击几率修正）  
		//人物抗暴处理 ANTI_PHYSIC_CRIT_RATE
		float myV = opfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_CRITC_LEVEL);
		float targetV = aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_MAGIC_CRITC_LEVEL);
		float myR = opfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_CRIT_RATE);
		float targetR = aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_MAGIC_CRIT_RATE);
		float magicCritRate = (myV - targetV)/1000 +(myR - targetR)+extcritrate;
		magicCritRate = Math.max(magicCritRate, PropConf.Battle.MIX_MAGIC_CRIT_RATE);
		if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.BATTLE_EQUIP_DEEP_HURT_SKILL))
			if((aimfighter.getEffectRole().getHp()*1.0f)/ (aimfighter.getEffectRole().getUplimithp()*1.0f) <0.3f)
				magicCritRate+=0.25;

		if (Math.random() < magicCritRate)
			return true;
		else
			return false;
	}

	@Override
	protected void handleCrit()
	{
		if (isCrit())
		{
			//最大暴击程度300%
			critpct = (1.0f+ opfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_CRIT_PCT)/1000);
			demoresult.targetresult |= ResultType.RESULT_CRITIC;
		}
	}
	/** 计算效果**/
	protected boolean handleEffect()
	{
		// 法术伤害=(法攻*技能放大系数-法防+技能等级*技能伤害系数)*(1+法术暴伤/1000)
		float effect = damageJS.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
		effect = (float) ((effect-ignorepct*aimfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_DEF))* critpct);
		// 修正
		// 法术伤害=法术伤害*（1+法术伤害效果修正-被法术伤害效果修正）（1+法术穿透/1000）*（1-法术抵抗/1000）
		//	法术伤害=法术伤害*random[（1-伤害浮动值）,（1+伤害浮动值）]
		effect = effect*(1+opfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_DAMGE_RATE) -aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_MAGIC_DAMGE_RATE))
				*(1+opfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_DAMGE_PIERCE_RATE)/1000)
				*(1-aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_MAGIC_DAMGE_PIERCE_RATE)/1000);
		InjureFloatValue += opfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_FLOAT_VALUE);
		damage= (float) (effect*(1.0f+ randomValueInScale(InjureFloatValue, -1.0f,1.0f)))*InjureRate;/**/
		return true;
	}
	
	@Override
	protected boolean handleInjure()
	{
		//	法术伤害最小伤害=技能等级*技能伤害系数
		damage = (int) Math.min(damage, -1.0f*minmagicdamage) ;
		demoresult.hpchange = (int) damage;		
		return true;
	}
	
	/**检查吸收*/
	protected void checkAbsorb(){};

	/**记录伤害次数**/
	protected void handleAttackCount()
	{

		if (demoresult.hpchange < 0)
		{
			if(opfighter.getFighterBean().getActioncount().containsKey(xbean.Fighter.MAGIC_ATTACK))
				opfighter.getFighterBean().getActioncount().put(xbean.Fighter.MAGIC_ATTACK, opfighter.getFighterBean().getActioncount().get(xbean.Fighter.MAGIC_ATTACK) + 1);
			else
				opfighter.getFighterBean().getActioncount().put(xbean.Fighter.MAGIC_ATTACK, 1);
			if(aimfighter.getFighterBean().getActioncount().containsKey(xbean.Fighter.MAGIC_HURT))
				aimfighter.getFighterBean().getActioncount().put(xbean.Fighter.MAGIC_HURT, aimfighter.getFighterBean().getActioncount().get(xbean.Fighter.MAGIC_HURT) + 1);
			else
				aimfighter.getFighterBean().getActioncount().put(xbean.Fighter.MAGIC_HURT, 1);
		}
		if (demoresult.protecterid != 0)
		{
			Fighter profighter = battleInfo.getFighterobjects().get(demoresult.protecterid);
			if(profighter.getFighterBean().getActioncount().containsKey(xbean.Fighter.MAGIC_HURT))
				profighter.getFighterBean().getActioncount().put(xbean.Fighter.MAGIC_HURT, profighter.getFighterBean().getActioncount().get(xbean.Fighter.MAGIC_HURT) + 1);
			else
				profighter.getFighterBean().getActioncount().put(xbean.Fighter.MAGIC_HURT, 1);
		}
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
}
