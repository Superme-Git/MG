package fire.pb.buff.single;

import fire.pb.attr.FightAttrType;


/**无属性法术伤害*/
public class NormalMagicInjure extends MagicInjure
{
	public NormalMagicInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}

	/**计算效果*/
	protected boolean dealEffect()
	{
		// 法术伤害=(法攻*技能放大系数-法防+技能等级*技能伤害系数)*(1+法术暴伤/1000)
		float effect = damageJS.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
		effect = (float) ((effect-ignorepct*opfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_DEF))* critpct);
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
	protected void checkAbsorb(){}

}
