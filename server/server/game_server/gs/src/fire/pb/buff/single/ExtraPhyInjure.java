package fire.pb.buff.single;

import fire.pb.attr.FightAttrType;

/**追击伤害状态*/
public class ExtraPhyInjure extends PhysicalInjure
{	public ExtraPhyInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	protected void processInjure()
	{
		demoresult.resulttype = 3;
		InjureRate *= opfighter.getEffectRole().getAttrById(FightAttrType.EXTRA_ATTACK_PCT);
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
		// 处理保护
		handleProtect();
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
		handleAim();
	}
	
}
