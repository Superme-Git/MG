package fire.pb.buff.single;


import fire.pb.attr.FightAttrType;

public class NormalPhyInjure extends PhysicalInjure
{
	public NormalPhyInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}

	// 计算效果
	protected boolean dealEffect()
	{
		float effect = damageJS.eval(battleInfo.getEngine(),opfighter,aimfighter).floatValue();
		effect = (float) ((effect-ignorepct*aimfighter.getEffectRole().getAttrById(FightAttrType.DEFEND))* critpct);
		//修正
		//效果值=效果值*（1+物理伤害效果修正-被物理伤害效果修正）*（1+物理穿透/1000）*（1-物理抵抗/1000）
		//效果值=效果值*random[（1-伤害浮动值）,（1+伤害浮动值）]
		effect = effect*(1+opfighter.getEffectRole().getAttrById(FightAttrType.PHYSIC_DAMGE_RATE) -aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_PHYSIC_DAMGE_RATE))
				*(1+opfighter.getEffectRole().getAttrById(FightAttrType.PHYSIC_DAMGE_PIERCE_RATE)/1000)
				*(1-aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_PHYSIC_DAMGE_PIERCE_RATE)/1000);
		InjureFloatValue += opfighter.getEffectRole().getAttrById(FightAttrType.PHYSIC_FLOAT_VALUE);
		float damgefloat =(float) (1.0f+ randomValueInScale(InjureFloatValue, -1.0f,1.0f));
		damage= (float) (effect*damgefloat)*InjureRate;
		return true;
	}

}
