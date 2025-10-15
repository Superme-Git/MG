package fire.pb.buff.single;

import java.util.Map;

import fire.pb.attr.AttrType;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.RootBuff;
import fire.pb.skill.Result;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;

public class SingleBuff extends RootBuff
{
	protected SingleBuffConfig buffConfig;
	protected BuffAgent agent;
	protected String[] values;
	protected int buffId;
	protected int hitrate;
	protected int targetType;
	protected int targetCount;
	protected String sortArg;
	
	protected float extcritrate = 0.0f;
	protected float critpct = 1.0f;
	protected int forbiddenDefPrt = 0;
	
	protected xbean.BattleInfo battleInfo;
	protected Fighter opfighter;
	protected Fighter aimfighter; 
	protected Map<Integer, JavaScript> effects;
	protected DemoResult demoresult;
	
	
	public SingleBuff(SingleBuffConfig buffConfig)
	{
		this.buffConfig = buffConfig;
		this.buffId = buffConfig.getBuffTypeId();
	}

	
	public void setForbiddenDefPrt(int forbiddenDefPrt)
	{
		//TODO 检查buff冲突
		this.forbiddenDefPrt = forbiddenDefPrt;
	}
	
	public void setExtCritRate(float extcritrate)
	{
		//TODO 检查buff冲突
		this.extcritrate = extcritrate;
	}
	
	@Override
	public Result impact(BuffAgent agent)
	{
		//TODO 检查buff冲突
		this.agent = agent;
		return new Result(false);
	}
	
	@Override
	public SingleBuff copy()
	{
		return fire.pb.buff.Module.getInstance().createInstantBuff(buffId);
	}
	

	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter,FightSkill battleskill, Map<Integer, JavaScript> effects)
	{
		init(battleInfo, opfighter, aimfighter, effects);
		
		end();
		return demoresult;
	}
	
	/**设置全局变量，初始化*/
	protected void init(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter, Map<Integer, JavaScript> effects)
	{
		this.battleInfo = battleInfo;
		this.opfighter = opfighter;
		this.aimfighter = aimfighter;
		this.effects = effects;
		demoresult = new DemoResult();
		demoresult.targetid = aimfighter.getFighterId();
		demoresult.flagtype = buffConfig.getBuffFlagType();
	}
	

	
	/**
	 * 将本回合目标、攻击者、保护者本次的结果放到RoundResult中
	 */
	protected void end()
	{
		//处理目标结果
		if(demoresult.hpchange != 0)
			demoresult.targetresult |= ResultType.RESULT_HPCHANGE;
		if(demoresult.mpchange != 0)
			demoresult.targetresult |= ResultType.RESULT_MPCHANGE;
		if(demoresult.spchange != 0)
			demoresult.targetresult |= ResultType.RESULT_SPCHANGE;
		if(demoresult.epchange != 0)
			demoresult.targetresult |= ResultType.RESULT_EPCHANGE;
		if(demoresult.uplimithpchange != 0)
		{
			demoresult.targetresult |= ResultType.RESULT_ULHPCHANGE;
			aimfighter.getRoundBuffResult().getChangedAttrs().put(AttrType.UP_LIMITED_HP, (float)aimfighter.getEffectRole().getUplimithp());
		}
		
		if(demoresult.hpchange <0 && (demoresult.targetresult&ResultType.RESULT_DEFENCE) == 0 && (demoresult.targetresult&ResultType.RESULT_DODGE) == 0 )
		{
			if(!(demoresult.resulttype == 0 && demoresult.targetid == opfighter.getFighterId()))
				demoresult.targetresult |= ResultType.RESULT_HURT;		
		}
		int aimResult = battleInfo.getRoundresult().get(aimfighter.getFighterId());
		battleInfo.getRoundresult().put(aimfighter.getFighterId(), aimResult | demoresult.targetresult);
		if(demoresult.attackerresult != 0)
		{
			if(demoresult.attackerulhpchange != 0)
				demoresult.attackerresult |= ResultType.RESULT_ULHPCHANGE;
			int attackerResult = battleInfo.getRoundresult().get(opfighter.getFighterId());
			battleInfo.getRoundresult().put(opfighter.getFighterId(), attackerResult | demoresult.attackerresult);
		}
		if (demoresult.protecterid != 0)
		{
			if(demoresult.protecterhpchange > -1)
				demoresult.protecterhpchange = -1;
			if(demoresult.protecterulhpchange != 0)
				demoresult.protecterresult |= ResultType.RESULT_ULHPCHANGE;
			int proResult = battleInfo.getRoundresult().get(demoresult.protecterid);
			battleInfo.getRoundresult().put(demoresult.protecterid, proResult | demoresult.protecterresult);
		}
	}
	
	@Override
	public int getId()
	{
		return buffId;
	}
	
	/**
	 * 随机浮动伤害等数值
	 * @param value
	 * @param min 浮动下限
	 * @param max 浮动上限
	 * @return
	 */
	public double randomValueInScale(double value, float min, float max)
	{
		double finalfloat = min + Math.random() * (max - min);
		return value * finalfloat;
	}
	
}
