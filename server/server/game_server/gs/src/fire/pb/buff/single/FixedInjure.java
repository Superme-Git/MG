package fire.pb.buff.single;

import fire.pb.attr.EffectType;
import fire.script.JavaScript;
public class FixedInjure extends Injure
{

	public FixedInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	protected void processInjure()
	{
		//计算伤害
		damage = damageJS.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
		//处理伤害
		demoresult.hpchange = (int) damage;
		// 改变蓝
		handleChangeMp();
		// 百分比改变蓝
		handleChangeMpPct();	
		//处理目标
		handleAim();
	}
	
	protected void handleChangeMp()
	{
		JavaScript changempscript = effects.get(EffectType.MP_ABL);
		if (changempscript != null)
		{
			int changemp = changempscript.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
			changemp = amendAddMp(changemp);
			demoresult.mpchange += aimfighter.attachMpChange(changemp);
		}
	}

	protected void handleChangeMpPct()
	{
		JavaScript changemppctscript = effects.get(EffectType.MP_PCT);
		if (changemppctscript != null)
		{
			int changemp = (int) (changemppctscript.eval(battleInfo.getEngine(),opfighter,aimfighter) * aimfighter.getEffectRole().getMp());
			changemp = amendAddMp(changemp);
			demoresult.mpchange += aimfighter.attachMpChange(changemp);
		}
	}
	protected int amendAddMp(int addmp)
	{
		return (int)randomValueInScale(addmp,0.95f,1.05f);
	}
	
	/**记录攻击和被攻击次数，为耐久度预处理**/
	protected void handleAttackCount()
	{

		if (demoresult.hpchange < 0)
		{
			if(aimfighter.getFighterBean().getActioncount().containsKey(xbean.Fighter.PHY_HURT))
				aimfighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_HURT, aimfighter.getFighterBean().getActioncount().get(xbean.Fighter.PHY_HURT) + 1);
			else
				aimfighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_HURT, 1);
		}
	}
	
	@Override
	public double randomValueInScale(double value, float min, float max)
	{	//固定伤害不修正
		return value;
	}
}
