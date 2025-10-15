package fire.pb.buff.single;

import fire.pb.PropConf;
import fire.pb.attr.FightAttrType;
/**反击伤害状态**/
public class AttackBackPhyInjure extends PhysicalInjure
{
	public AttackBackPhyInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}	
	
	@Override
	protected void processInjure()
	{
		demoresult.resulttype = 1;
		InjureRate *= opfighter.getEffectRole().getAttrById(FightAttrType.ATTACK_BACK_PCT);
		super.processInjure();
	}
	
	@Override
	protected boolean handleInjure()
	{
		//效果值=max（物理攻击*最低物理伤害系数,效果值)
		float baseattack = opfighter.getEffectRole().getAttrById(FightAttrType.ATTACK);
		damage = (int) Math.min(damage, -1.0f*baseattack*PropConf.Battle.MIX_PHYSIC_DAMAGE_VALUE) ;
		demoresult.hpchange = (int) damage;		
		return true;
	}	
	
	// 计算效果
	@Override
	protected boolean handleEffect()
	{
		float temp3 = opfighter.getEffectRole().getAttack();
		float temp4 = aimfighter.getEffectRole().getDefend();
		float effect = -(temp3*1-(1-ignorepct)*temp4);
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
