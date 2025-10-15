package fire.pb.buff.single;

import fire.pb.attr.FightAttrType;

/**连击伤害**/
public class ComboPhyInjure extends PhysicalInjure
{	
	public ComboPhyInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}	

	@Override
	protected void processInjure()
	{
		demoresult.resulttype = 2;
		InjureRate *= opfighter.getEffectRole().getAttrById(FightAttrType.PHYSIC_COMBO_ATTACK_PCT);
		//处理第几次连击
		handleComboCount();
		//修改忽略防御参数
		handleIgnoreDefend();
		// 修改暴击参数
		handleCrit();
		// 计算效果
		handleEffect();
		// 处理防御
		handleDefend();
		// 计算伤害
		handleInjure();
		// 危机保护
		handleDagnerProtect();
		//处理掉蓝
		handleMpChange();
		// 处理吸血
		handleStealHp();
		//处理中毒
		handlePoison();
		//处理死亡禁锢
		handleDieForbid();
		// 处理反震
		handleReturnHurt();
		// 处理物理伤害
		demoresult.hpchange = (int) amendPhyInjure(demoresult.hpchange);
		// 处理原目标
		handleAim();
	}
}
