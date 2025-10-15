package fire.pb.buff.single;

/**技能物理伤害，继承自物理伤害基类*/
public class SkillPhyInjure extends PhysicalInjure
{
	public SkillPhyInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	@Override
	protected void processInjure()
	{
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
		// 处理反震
		handleReturnHurt();
		// 处理物理伤害
		demoresult.hpchange = (int) amendPhyInjure(demoresult.hpchange);
		// 处理原目标
		handleAim();
	}	
}
