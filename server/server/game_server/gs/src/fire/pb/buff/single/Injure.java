package fire.pb.buff.single;

import java.util.Map;

import fire.pb.PropConf;
import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.FightAttrType;
import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.Monster;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.Module;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.skill.Result;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;

public abstract class Injure extends SingleBuff
{
	protected float damage;//永远是负数，不能大于-1
	protected JavaScript damageJS;
	protected boolean isAborbed = false;//伤害吸收
	float InjureRate = 1.0f;
	float InjureFloatValue = PropConf.Battle.DAMAGE_FLOAT_VALUE;
	float AbsorbRate = 1.0f;
	float ignorepct = 0.0f;
	
	public Injure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter, FightSkill battleskill,Map<Integer, JavaScript> effects)
	{
		float formeffcet =  (aimfighter.getFighterId() > BattleField.A_SIDE_FIGHTER_NUM? battleInfo.getHostformeffcet():0-battleInfo.getHostformeffcet());
		if(opfighter.getFighterId() > 0 && formeffcet >0)
		{
			InjureRate -= formeffcet;
		}
		//初始化
		init(battleInfo, opfighter, aimfighter, effects);
		//主要伤害处理流程，不同伤害覆盖写自己的处理
		processInjure();
		//处理掉怒气
		handleSpChange();
		//记录攻击和被攻击次数，为耐久度预处理
		handleAttackCount();
		//结束
		end();
		return demoresult;
	}
	
	protected void processInjure()
	{
		// 修改暴击参数
		handleCrit();
		//计算伤害
		handleInjure();
		//处理伤害
		damage = (int)amendInjure(damage);
		//处理目标
		handleAim();
	}
	
	protected void handleCrit()
	{
		if (isCrit())
		{	//最大暴击程度300%
			critpct = 1.0f;
			demoresult.targetresult |= ResultType.RESULT_CRITIC;
		}
	}

	protected double amendInjure(double damage)
	{
		//检查驱魔对应鬼魂
		if (aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_FIRST) ||aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_SECOND) )
		{
			if (opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ANTI_GHOST_SECOND))
			{
				damage = damage * 2;
			} else if (opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ANTI_GHOST_FIRST)
					||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ANTI_GHOST))
			{
				damage = damage * 1.5;
			}
		}

		if(damage > -1)
			damage = -1;
		return damage;
	}
	/**设置全局变量，初始化 */
	protected void init(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter, Map<Integer, JavaScript> effects)
	{
		super.init(battleInfo, opfighter, aimfighter, effects);
		
		//获取伤害脚本
		if (effects.containsKey(EffectType.HP_ABL))
			damageJS = effects.get(EffectType.HP_ABL);
		else if (effects.containsKey(EffectType.HP_PCT))// 如果是百分比，则只存放了一个浮点数
		{
			JavaScript pct = effects.get(EffectType.HP_PCT);
			damageJS = new JavaScript(String.valueOf(pct.eval(battleInfo.getEngine(),opfighter,aimfighter) * aimfighter.getEffectRole().getHp()));
		}
		else
			damageJS = new JavaScript("0");
	}
	
	protected void handleIgnoreDefend()
	{
	}

	/**是否暴击,法术和物理的暴击自己分别计算*/
	protected boolean isCrit()
	{
		return false;
	}
	
	protected boolean handleInjure()
	{
		damage = (int)(damageJS.eval(battleInfo.getEngine(),opfighter,aimfighter).intValue()*critpct);
		if(damage > -1)
			damage = -1;
		demoresult.hpchange = (int) damage;
		return true;
	}
		
	//计算原目标结果
	protected void handleAim()
	{
		int aimRemainHp = aimfighter.getEffectRole().getHp();
		if(demoresult.hpchange < 0 
				&& ((aimfighter.isPartner() && aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FERITY_INSTINCT_IMMUNE))
						||aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FERITY_INSTINCT_IMMUNE2)))
		{
			demoresult.hpchange = -1;
		}
		//处理伤害吸收
		if(isAborbed)
		{
			handleAbsorb();
			aimfighter.attachHpChange(demoresult.hpchange);
			demoresult.targetresult |= ResultType.RESULT_ABORBE;
			handleBuffAfterInjure();
			return;
		}
		else if(aimfighter.isPartner()&&aimfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_PANDORA_BOX_SKILL))
		{
			if(Math.random() < PropConf.Battle.PANDORA_BOX_RATE)
				demoresult.hpchange = demoresult.hpchange*2;
			else
			{
				demoresult.targetresult |= ResultType.RESULT_HURT;
				demoresult.hpchange = Math.abs(demoresult.hpchange);
			}
		}
		if(demoresult.hpchange < 0 && 0-demoresult.hpchange >= aimRemainHp)
		{
			if(aimfighter.isPartner() && aimfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_REVENGE_SKILL)
					&& !aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_REVENGE))
			{//奥术禁锢
				demoresult.hpchange = 1-aimRemainHp;
				Result result = aimfighter.getBuffAgent().addCBuff(BuffConstant.CONTINUAL_REVENGE, -1, 0);
				Module.updateDemoResultFromResult(demoresult, result, aimfighter);// 死亡
			}
		}
		demoresult.hpchange = aimfighter.attachHpChange(demoresult.hpchange);
			
		handleBuffAfterInjure();
		if (aimfighter.getEffectRole().getHp() <= 0)
		{	
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("战场编号为 "+ aimfighter.getFighterId() + "的战斗者被攻击后 \t死亡 原HP="+aimRemainHp+"现HP = "+aimfighter.getEffectRole().getHp());}
			int deathResult = handleDeath(opfighter,aimfighter);
			if(deathResult==ResultType.RESULT_GODBLESS){
				float godblesspct = aimfighter.getEffectRole().getAttrById(FightAttrType.GOD_BLESS_PCT);
				demoresult.godblesshp = (int) (aimfighter.getEffectRole().getMaxHp()* godblesspct);	
				aimfighter.getEffectRole().setHp(demoresult.godblesshp);
				
				if(aimfighter.getBuffAgent().existBuff( BuffConstant.CONTINUAL_GODBLESS_ONLYONCE))
				{					
					Result result = aimfighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_GODBLESS_ONLYONCE);
					fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result, aimfighter);
				}
			}
			demoresult.targetresult |= deathResult;
			if(deathResult==ResultType.RESULT_DEATH && aimfighter.isPartner() 
					&& aimfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_ELEMENT_AWKEN_SKILL)
					&& !aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ELEMENT_AWKEN_FLAG))
			{
				Result result = aimfighter.getBuffAgent().addCBuff(BuffConstant.CONTINUAL_ELEMENT_AWKEN_FLAG, -1, 0);
				Module.updateDemoResultFromResult(demoresult, result, aimfighter);// 死亡
				result = aimfighter.getBuffAgent().addCBuff(BuffConstant.CONTINUAL_ELEMENT_AWKEN_RELIVE, 1, 0);
				Module.updateDemoResultFromResult(demoresult, result, aimfighter);// 死亡
			}
		}
		battleInfo.getRoundresult().put(aimfighter.getFighterId(),(battleInfo.getRoundresult().get(aimfighter.getFighterId()) |demoresult.targetresult));
	}
	
	/**处理吸收 */
	protected void handleAbsorb()
	{
		demoresult.hpchange = (int) (Math.abs(demoresult.hpchange)*AbsorbRate);
	}
	
	/**处理单位死亡*/
	protected int handleDeath(Fighter killer, Fighter deadfighter)
	{
		if (deadfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH) || deadfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST))
			return 0;
		
		float godbless = aimfighter.getEffectRole().getAttrById(FightAttrType.GOD_BLESS_RATE);
		if (Math.random() < godbless)
			return  ResultType.RESULT_GODBLESS;
		
		for(int existbuffId : deadfighter.getBuffAgent().getAllBuffBeans().keySet())
		{
			ConstantlyBuffConfig buffcfg = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(existbuffId);
			if(buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_IN_BATTLE_DEATH
					||buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_USE_SKILL
					||buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_USE_SKILL_ATTACK)
			{
				Result deadresult = deadfighter.getBuffAgent().removeCBuff(existbuffId);
				fire.pb.buff.Module.updateDemoResultFromResult(demoresult, deadresult, deadfighter);
			}
		}
		
		switch(deadfighter.getFightertype())
		{
		case xbean.Fighter.FIGHTER_ROLE:
		{		
			Result deathresult = deadfighter.getBuffAgent().addCBuff(BuffConstant.CONTINUAL_DEATH);
			if(!deathresult.isSuccess())
			{//防止出现死亡buff加不上的情况
				deadfighter.getEffectRole().setHp(1);
				return 0;
			}
			//死亡怒气处理，根据配置
			fire.pb.effect.RoleImpl roleImpl = ((fire.pb.effect.RoleImpl) deadfighter.getEffectRole());
			int originSP = roleImpl.getSp();
			roleImpl.setSp((int)(originSP * PropConf.Battle.PROP_SP_DISCOUNT/100.0));
			deathresult.getChangedAttrs().put(AttrType.SP, (float)roleImpl.getSp());
			//伤变化
			fire.pb.buff.Module.updateDemoResultFromResult(demoresult, deathresult, deadfighter);
			
			//重要：攻击者死亡在buff外面BattleSkill内处理，保护者死亡客户端特殊处理
			//记录一下死亡的角色的回合数
			battleInfo.getBattledeads().put(deadfighter.getFighterBean().getUniqueid(), battleInfo.getRound());
			return ResultType.RESULT_DEATH;
		}
		case xbean.Fighter.FIGHTER_PET:
		case xbean.Fighter.FIGHTER_PARTNER:
		case xbean.Fighter.FIGHTER_MONSTER_HIDE:
		case xbean.Fighter.FIGHTER_MONSTER_NPC:
		case xbean.Fighter.FIGHTER_SYSTEM_PARTNER:			
		{			
			if (deadfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_FIRST) ||deadfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_SECOND) )
			{
				
				if (killer != null && (killer.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ANTI_GHOST)
										||killer.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ANTI_GHOST_FIRST)
										||killer.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ANTI_GHOST_SECOND)))
					return ResultType.RESULT_KICKOUT;// 驱魔，击飞
				else
				{
					// 触发鬼魂状态
					Result result = deadfighter.getBuffAgent().addCBuff(BuffConstant.CONTINUAL_GHOST, 5, 0);
					if(!result.isSuccess())
					{//防止出现鬼魂buff加不上的情况
						deadfighter.getEffectRole().setHp(1);
						return 0;
					}
					battleInfo.getBattledeads().put(deadfighter.getFighterBean().getUniqueid(), battleInfo.getRound());
					Module.updateDemoResultFromResult(demoresult, result, deadfighter);// 死亡
					// 重要：攻击者鬼魂在buff外面处理，保护者鬼魂客户端特殊处理
					return ResultType.RESULT_GHOST;
				}
			}
			else
			{
				if(canDeath(deadfighter))
				{
					Result deathresult = deadfighter.getBuffAgent().addCBuff(BuffConstant.CONTINUAL_DEATH);
					if(!deathresult.isSuccess())
					{//防止出现死亡buff加不上的情况
						deadfighter.getEffectRole().setHp(1);
						return 0;
					}
					battleInfo.getBattledeads().put(deadfighter.getFighterBean().getUniqueid(), battleInfo.getRound());
					fire.pb.buff.Module.updateDemoResultFromResult(demoresult, deathresult, deadfighter);
					//重要：攻击者死亡在buff外面BattleSkill内处理，保护者死亡客户端特殊处理
					return ResultType.RESULT_DEATH;
				}
				battleInfo.getBattledeads().put(deadfighter.getFighterBean().getUniqueid(), battleInfo.getRound());				
/*				这段代码在子类型没有加入类型5时候就存在。那个时候这段代码是无效的。现在先屏蔽。edit by lxh
  				if(deadfighter.getFighterBean().getSubtype() == 5)
					return ResultType.RESULT_DEATH;
*/				return ResultType.RESULT_KICKOUT;
			}// 击飞
		}
		default:
			return 0;
		}
	}
	/**处理掉蓝，有的伤害buff带有掉蓝效果 */
	protected void handleMpChange()
	{
		float fireMP = opfighter.getEffectRole().getAttrById(FightAttrType.FIRE_MP_RATE);
		if(Math.random() < fireMP)
		{
			float fireMPpet = opfighter.getEffectRole().getAttrById(FightAttrType.FIRE_MP_PCT);
			demoresult.mpchange = (int) (demoresult.hpchange*fireMPpet);
			if(demoresult.mpchange != 0)
			{
				demoresult.targetresult |= ResultType.RESULT_FIRE_MANA;
				aimfighter.attachMpChange(demoresult.mpchange);
			}
		}		
	}
	
	/**检查战斗者能否倒地死亡*/
	protected boolean canDeath(Fighter deadfighter)
	{
		if(deadfighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			return true;
		if(deadfighter.getFightertype() == xbean.Fighter.FIGHTER_PET)
			return false;
		if(deadfighter.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER)
			return Monster.canDeath((int)deadfighter.getFighterBean().getUniqueid());
		return false;
	}
	
	protected void handleBuffAfterInjure()
	{
		if(demoresult.hpchange < 0||demoresult.mpchange < 0)
		{//减血或减蓝后才能取消睡眠变羊buff。
/*			Result result = aimfighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_SLEEP);
			fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result, aimfighter);
			Result shapechangeresult = aimfighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_SHAPECHANGE);
			fire.pb.buff.Module.updateDemoResultFromResult(demoresult, shapechangeresult, aimfighter);
*/
			for(int existbuffId : aimfighter.getBuffAgent().getAllBuffBeans().keySet())
			{
				ConstantlyBuffConfig buffcfg = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(existbuffId);
				if(buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_IN_BATTLE_HURT)
				{
					Result result = aimfighter.getBuffAgent().removeCBuff(existbuffId);
					fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result, aimfighter);
				}
			}
		}
	}
	
	/**怒气处理*/
	protected void handleSpChange()
	{
		if(effects.containsKey(EffectType.SP_ABL))
		{
			demoresult.spchange = effects.get(EffectType.SP_ABL).eval(battleInfo.getEngine(),opfighter,aimfighter).intValue();
			if(demoresult.spchange > -1)
				demoresult.spchange = -1;
		}
		if(effects.containsKey(EffectType.SP_PCT))
		{
			demoresult.spchange +=  (int)(effects.get(EffectType.SP_PCT).eval(battleInfo.getEngine(),opfighter,aimfighter) * aimfighter.getEffectRole().getMaxSp());
			if(demoresult.spchange > -1)
				demoresult.spchange = -1;
		}
		if(demoresult.spchange != 0)
		{
			aimfighter.getEffectRole().addSp(demoresult.spchange, 0);
			aimfighter.getRoundBuffResult().getChangedAttrs().put(AttrType.SP, (float)aimfighter.getEffectRole().getSp());
		}
	}
	
	//记录攻击和被攻击次数，为耐久度预处理
	abstract protected void handleAttackCount();
	
}
