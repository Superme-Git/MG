 package fire.pb.buff.single;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import fire.pb.PropConf;
import fire.pb.attr.EffectType;
import fire.pb.attr.FightAttrType;
import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.skill.Result;
import fire.pb.skill.SkillConstant;
import fire.script.JavaScript;

/**物理类伤害*/
public class PhysicalInjure extends Injure
{
	public PhysicalInjure(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}

	@Override
	protected void processInjure()
	{
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
		// 处理原目标
		handleAim();
	}
	
	protected void handleComboCount()
	{
		Double o = battleInfo.getEngine().getDouble("attacknuma");
		if(o == null)
			battleInfo.getEngine().setSkillComboCurCount(1);
		else
			battleInfo.getEngine().setSkillComboCurCount(o.intValue()+1);
	}
	
	/**修正物理伤害 ---强力碰上防御,减去20%物理伤害*/
	protected double amendPhyInjure(double phydamage)
	{	
		if ((opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_STRONG_POWER_FIRST) ||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_STRONG_POWER_SECOND)) 
				&& (aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEFENCE_FIRST) ||aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEFENCE_SECOND)))
			phydamage = phydamage * 0.75;
		return amendInjure(phydamage);
	}

	/**暴击判断*/
	protected boolean isCrit()
	{
		//物理暴击率=（物理暴击-物理抗暴）/1000+(物理暴击几率修正-被物理暴击几率修正） 
		float phyCritRate = (float) ((opfighter.getEffectRole().getAttrById(FightAttrType.PHY_CRITC_LEVEL) - aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_PHY_CRITC_LEVEL))/1000
				+(opfighter.getEffectRole().getAttrById(FightAttrType.PHYSIC_CRIT_RATE)- aimfighter.getEffectRole().getAttrById(FightAttrType.ANTI_PHYSIC_CRIT_RATE))) + extcritrate;
		phyCritRate = Math.max(phyCritRate, PropConf.Battle.MIX_PHYSIC_CRIT_RATE);
		if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.BATTLE_EQUIP_DEEP_HURT_SKILL))
			if((aimfighter.getEffectRole().getHp()*1.0f)/ (aimfighter.getEffectRole().getUplimithp()*1.0f) <0.3f)
				phyCritRate+=0.25;		
		if (Math.random() < phyCritRate)
			return true;
		else
			return false;
	}
	
	protected void handleCrit()
	{
		if (isCrit())
		{
			//最大暴击程度300%			
			critpct =  (float)(1.0f+ opfighter.getEffectRole().getAttrById(FightAttrType.PHYSIC_CRIT_PCT)/1000);
			demoresult.targetresult |= ResultType.RESULT_CRITIC;
		}
	}
	
	@Override
	protected void handleIgnoreDefend()
	{
		float fignoredefend = opfighter.getEffectRole().getAttrById(FightAttrType.IGNORE_PHYSIC_DEFEND_RATE);
		if (Math.random() < fignoredefend)
		{
			ignorepct = opfighter.getEffectRole().getAttrById(FightAttrType.IGNORE_PHYSIC_DEFEND_PCT);
			demoresult.targetresult |= ResultType.RESULT_IGNORE_PHYDIC_EFEN;			
		}
	}

	// 计算效果
	protected boolean handleEffect()
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
	
	protected boolean handleInjure()
	{
		//效果值=max（物理攻击*最低物理伤害系数,效果值)
		float baseattack = opfighter.getEffectRole().getAttrById(FightAttrType.ATTACK);
		damage = (int) Math.min(damage, -1.0f*baseattack*PropConf.Battle.MIX_PHYSIC_DAMAGE_VALUE) ;
		demoresult.hpchange = (int) damage;		
		return true;
	}

	/**防御 **/
	protected void handleDefend()
	{
		final xbean.Decision d = battleInfo.getDecisions().get(demoresult.targetid);
		if(d != null)
		{
			if (d.getOperatetype() == OperationType.ACTION_DEFEND && forbiddenDefPrt != 1 && forbiddenDefPrt != 3)
			{
				if(aimfighter.canAction(OperationType.ACTION_DEFEND, 0))
				{
					demoresult.targetresult |= ResultType.RESULT_DEFENCE;
					damage = Math.min(-1, damage*(1-PropConf.Battle.DAMAGE_DEFEND_PARAM));
				}
			}
			else if(handleParry())
			{
				demoresult.targetresult |= ResultType.RESULT_PARRY;
				damage =  Math.min(-1, damage*(1-aimfighter.getEffectRole().getAttrById(FightAttrType.PARRY_PCT) )) /*Math.min(-1, demoresult.hpchange / 2)*/;
			}
		}
		
	}
	
	/**格挡**/
	private boolean handleParry()
	{
		float parryrate = aimfighter.getEffectRole().getAttrById(FightAttrType.PARRY_RATE);/*battleInfo.getEngine().getDouble("parrya")*/
		if(Math.random() < parryrate)
			return true;
		else
			return false;
	}
	
	/**处理保护，放在所有伤害都结算完之后来处理*/
	protected void handleProtect(){
		if( forbiddenDefPrt != 2 && forbiddenDefPrt != 3 )
		{
			float protectorDmgPct = PropConf.Battle.DAMAGE_PROTECT_RATE;
			float aimDmgPct = 1-protectorDmgPct;
	
			//获取保护者
			demoresult.protecterid = BattleField.getAProtector(battleInfo, aimfighter.getFighterId(),(int) (damage * protectorDmgPct));
			// 保护流程
			if (demoresult.protecterid != 0){
				Fighter protectFighter = battleInfo.getFighterobjects().get(demoresult.protecterid);
				if(protectFighter == null)
					return;
				
				demoresult.hpchange = Math.min(-1, (int) (damage * aimDmgPct));
				demoresult.protecterhpchange = Math.min(-1,(int) (damage * protectorDmgPct));
				protectFighter.attachHpChange(demoresult.protecterhpchange);
				demoresult.protecterresult |= ResultType.RESULT_HPCHANGE;
	
				if (protectFighter.getEffectRole().getHp() <= 0){
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("战场编号为 "+ protectFighter.getFighterId() + "的战斗者因为保护他人 \t死亡 HP = "+protectFighter.getEffectRole().getHp());}
					demoresult.protecterresult |= handleDeath(opfighter,protectFighter);
					if((demoresult.protecterresult&ResultType.RESULT_GODBLESS) > 0 &&protectFighter.getBuffAgent().existBuff( BuffConstant.CONTINUAL_GODBLESS_ONLYONCE))
					{					
						Result result = protectFighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_GODBLESS_ONLYONCE);
						fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result, protectFighter);
					}
				}
				battleInfo.getRoundresult().put(protectFighter.getFighterId(),(battleInfo.getRoundresult().get(protectFighter.getFighterId()) |demoresult.protecterresult));
			}
		}
	}
	
	/**保护**/
	protected void handleDagnerProtect(){}

	/**处理溅射**/
	protected DemoResult DiffuseAaim(Fighter fighter, float damagerate)
	{
		DemoResult diffusedemo = new DemoResult();
		diffusedemo.targetid = fighter.getFighterId();
		diffusedemo.flagtype = buffConfig.getBuffFlagType();
		//处理目标结果 
		diffusedemo.hpchange = (int) (damage*randomValueInScale(damagerate, 0.95f,1.05f));
		if(diffusedemo.hpchange != 0)
		{
			int aimRemainHp = fighter.getEffectRole().getHp();
			diffusedemo.targetresult |= ResultType.RESULT_HPCHANGE;
			diffusedemo.targetresult |= ResultType.RESULT_HURT;
			diffusedemo.resulttype = 4;
			diffusedemo.hpchange = fighter.attachHpChange(diffusedemo.hpchange);
			
			if(diffusedemo.hpchange < 0)
			{//减血或减蓝后才能取消睡眠变羊buff。
				for(int existbuffId : fighter.getBuffAgent().getAllBuffBeans().keySet())
				{
					ConstantlyBuffConfig buffcfg = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(existbuffId);
					if(buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_IN_BATTLE_HURT)
					{
						Result result = fighter.getBuffAgent().removeCBuff(existbuffId);
						fire.pb.buff.Module.updateDemoResultFromResult(diffusedemo, result, fighter);
					}
				}
			}
			
			if (fighter.getEffectRole().getHp() <= 0)
			{	
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("战场编号为 "+ fighter.getFighterId() + "的战斗者被攻击后 \t死亡 原HP="+aimRemainHp+"现HP = "+fighter.getEffectRole().getHp());}
				int deathResult = handleDeath(opfighter,fighter);
				if(deathResult==ResultType.RESULT_GODBLESS){
					float godblesspct = fighter.getEffectRole().getAttrById(FightAttrType.GOD_BLESS_PCT);
					diffusedemo.godblesshp = (int) (fighter.getEffectRole().getMaxHp()* godblesspct);	
					fighter.getEffectRole().setHp(diffusedemo.godblesshp);
					if(fighter.getBuffAgent().existBuff( BuffConstant.CONTINUAL_GODBLESS_ONLYONCE))
					{					
						Result result = fighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_GODBLESS_ONLYONCE);
						fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result, fighter);
					}
				}
				diffusedemo.targetresult |= deathResult;
			}
			battleInfo.getRoundresult().put(fighter.getFighterId(),(battleInfo.getRoundresult().get(fighter.getFighterId()) |diffusedemo.targetresult));
		}
		
		return diffusedemo;
	}
	
	/**处理溅射**/ 
	public List<DemoResult> handleDiffuse()
	{
		List<DemoResult> demos = new LinkedList<DemoResult>();
		float diffuserate = opfighter.getEffectRole().getAttrById(FightAttrType.DIFFUSE_ATTACK_RATE);
		int diffusecount =  (int) opfighter.getEffectRole().getAttrById(FightAttrType.DIFFUSE_ATTACK_COUNT);
		if(Math.random() < diffuserate)
		{
			float diffusedamage = opfighter.getEffectRole().getAttrById(FightAttrType.DIFFUSE_ATTACK_PCT);
			
			List<Fighter> diffusefigters = new ArrayList<Fighter>();
			
			int LeftFighterId = BattleField.getLeftFighterId(battleInfo, aimfighter.getFighterId());
			if(LeftFighterId>0 && BattleField.checkLiveInBattle(battleInfo, LeftFighterId))
			{
				Fighter diffusefigter = battleInfo.getFighterobjects().get(LeftFighterId);
				diffusefigters.add(diffusefigter);
			}

			int RightFighterId = BattleField.getRightFighterId(battleInfo, aimfighter.getFighterId());
			if(RightFighterId>0 && BattleField.checkLiveInBattle(battleInfo, RightFighterId))
			{
				Fighter diffusefigter = battleInfo.getFighterobjects().get(RightFighterId);
				diffusefigters.add(diffusefigter);
			}

			int FrontFighterId = BattleField.getFrontFighterId(battleInfo, aimfighter.getFighterId());
			if(FrontFighterId>0 && BattleField.checkLiveInBattle(battleInfo, FrontFighterId))
			{
				Fighter diffusefigter = battleInfo.getFighterobjects().get(FrontFighterId);
				diffusefigters.add(diffusefigter);
			}

			int BehindFighterId = BattleField.getBehindFighterId(battleInfo, aimfighter.getFighterId());
			if(BehindFighterId>0 && BattleField.checkLiveInBattle(battleInfo, BehindFighterId))
			{
				Fighter diffusefigter = battleInfo.getFighterobjects().get(BehindFighterId);
				diffusefigters.add(diffusefigter);
			}
			if(diffusefigters.size() > 0)
			{
				BattleField.sortfighter(diffusefigters, battleInfo);
			}
			int i = 0;
			for(Fighter fighter: diffusefigters)
			{	
				if(i >= diffusecount)
				   break;
				DemoResult diffusedemo =  DiffuseAaim(fighter,diffusedamage);
				if (diffusedemo != null)
				{					
					demos.add(diffusedemo);
					i++;
				}
			}
		}
		return demos;
	}
	
	/**处理反震**/
	protected void handleReturnHurt()
	{
		if (isAborbed)
			return;
		// 以下情况不反震：有偷袭，行动者死，目标死
/*		if (opfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_PET)
			return;*/
		if(!BattleField.checkLiveInBattle(battleInfo, opfighter.getFighterId()))
			return;
		if(!BattleField.checkLiveInBattle(battleInfo, aimfighter.getFighterId()))
			return;
		if(!isMultiReturnHurt())
		{
			Double targetnum = battleInfo.getEngine().getDouble("effectnuma");
			if (targetnum == null)
				return;
			if ((targetnum).intValue() > 1)
				return;// 同一个物理伤害类buff多于一个目标时，不反震
		}
		//外功反震率=初值+max(0%,min(100%,外功反震等级b/角色b对应反击反震等级标准值-外功反震抗性a/角色a对应外功反震抗性标准值)
		//攻击方减血效果值=max（目标方剩余血量，效果值*反震伤害系数）
		float returnhurtRate = aimfighter.getEffectRole().getAttrById(FightAttrType.RETURN_HURT_RATE)- opfighter.getEffectRole().getAttrById(FightAttrType.ANTI_RETURN_HURT_RATE);
		if (Math.random() < returnhurtRate && opfighter.getEffectRole().getHp() > 2)
		{ 
			float returnhurtpct = aimfighter.getEffectRole().getAttrById(FightAttrType.RETURN_HURT_PCT) ;
			if(canDeath(aimfighter))
			{
				demoresult.returnhurt =  Math.min(-1, (int)(demoresult.hpchange * returnhurtpct));
			}
			else
			{
				int leftHP =  opfighter.getEffectRole().getHp()-1;
				demoresult.returnhurt = Math.min(-1, 0-Math.min(leftHP, (int) (demoresult.hpchange * returnhurtpct*-1.0)));
			}
			// 结算攻击者
			{
				//上面代码保证了攻击者不死，不需要加神佑逻辑	给攻击者留1点血
				opfighter.attachHpChange(demoresult.returnhurt);
				demoresult.attackerresult |= ResultType.RESULT_HPCHANGE;
				demoresult.targetresult |= ResultType.RESULT_HURT;
				if (opfighter.getEffectRole().getHp() <= 0)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("战场编号为 "+ opfighter.getFighterId() + "的战斗者被反击后 \t死亡 HP = "+opfighter.getEffectRole().getHp()+" 这里出bug了。该给攻击者留1点血的");}
					demoresult.attackerresult |= handleDeath(aimfighter,opfighter);
					if((demoresult.protecterresult&ResultType.RESULT_GODBLESS) > 0 &&opfighter.getBuffAgent().existBuff( BuffConstant.CONTINUAL_GODBLESS_ONLYONCE))
					{					
						Result result = opfighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_GODBLESS_ONLYONCE);
						fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result, opfighter);
					}
				}
				battleInfo.getRoundresult().put(opfighter.getFighterId(),
						(battleInfo.getRoundresult().get(opfighter.getFighterId()) | demoresult.attackerresult));
			}
		}
	}

	/**处理吸血*/
	protected void handleStealHp()
	{
		//先检查buff自带的吸血效果
		if(demoresult.stealhp > 0)
			return;//已经吸过血了
		if (isAborbed || demoresult.hpchange >= 0)
			return;

		float stealhppct = opfighter.getEffectRole().getAttrById(FightAttrType.STEAL_HP_PCT);
		// 对拥有鬼魂 技能的怪物无效
		if (stealhppct != 0 && !(aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_FIRST) ||aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_SECOND)))
		{
			float stealhprate = opfighter.getEffectRole().getAttrById(FightAttrType.STEAL_HP_RATE);
			if(Math.random() < stealhprate)
			{
				demoresult.stealhp = Math.abs((int) (demoresult.hpchange*stealhppct));
				opfighter.attachHpChange(demoresult.stealhp);
			}		
		}
	}

	/**中毒处理*/
	protected void handlePoison()
	{
		if(!BattleField.checkLiveInBattle(battleInfo, aimfighter.getFighterId()))
			return;//死了不加
		if (aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_POISON)/*||(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_TETANUS_SKILL))*/)
		{
			handleInfect();
		}
		else 
		{
			float posionrate = opfighter.getEffectRole().getAttrById(FightAttrType.POISON_RATE);
			if (Math.random() < posionrate)
			{
				if (!(aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_FIRST) ||aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_SECOND)))
				{//对方没有鬼魂技能
					ConstantlyBuff poisonbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_PET_POISON);
					if(poisonbuff!=null)
					{
						poisonbuff.setRound((int)opfighter.getEffectRole().getAttrById(FightAttrType.PIOSON_DAMGE_ROUND));
						poisonbuff.getEffects().put(EffectType.HP_PCT, opfighter.getEffectRole().getAttrById(FightAttrType.PIOSON_DAMGE_HP));
						poisonbuff.getEffects().put(EffectType.MP_PCT, opfighter.getEffectRole().getAttrById(FightAttrType.PIOSON_DAMGE_MP));
						DemoResult poisondemo = poisonbuff.impact(battleInfo, opfighter, aimfighter, null, new HashMap<Integer, JavaScript>());
						demoresult.demobuffs.addAll(poisondemo.demobuffs);
					}
				}
			}
		}
	}
	/**传染处理 */
	protected void handleInfect()
	{
		if(!BattleField.checkLiveInBattle(battleInfo, opfighter.getFighterId()))
			return;//死了不加
		if (Math.random() < PropConf.Battle.POISON_INFECT_RATE - opfighter.getEffectRole().getAttrById(FightAttrType.ANTI_POISON_RATE))
		{
			if (!(opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_FIRST) ||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_SECOND)))
			{//对方没有鬼魂技能
				ConstantlyBuff poisonbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_PET_POISON);
				if(poisonbuff!=null)
				{
					poisonbuff.setRound(3);
					poisonbuff.getEffects().put(EffectType.HP_PCT, aimfighter.getEffectRole().getAttrById(FightAttrType.PIOSON_DAMGE_HP));
					poisonbuff.getEffects().put(EffectType.MP_PCT, aimfighter.getEffectRole().getAttrById(FightAttrType.PIOSON_DAMGE_MP));
					DemoResult poisondemo = poisonbuff.impact(battleInfo, aimfighter, opfighter, null, new HashMap<Integer, JavaScript>());
					demoresult.demobuffs.addAll(poisondemo.demobuffs);
				}
			}
		}
	}

	/**死亡禁锢处理*/
	protected void handleDieForbid()
	{
		float dieForbidrate = opfighter.getEffectRole().getAttrById(FightAttrType.DIE_FORBID_RATE);
		if (Math.random() < dieForbidrate)
		{
			ConstantlyBuff poisonbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_DIE_FORBID);
			if(poisonbuff!=null)
			{
				poisonbuff.setRound((int)opfighter.getEffectRole().getAttrById(FightAttrType.DIE_FORBID_ROUND));
				DemoResult poisondemo = poisonbuff.impact(battleInfo, opfighter, aimfighter, null, new HashMap<Integer, JavaScript>());
				demoresult.demobuffs.addAll(poisondemo.demobuffs);
			}
		}
	}	
	
	/**记录攻击和被攻击次数，为耐久度预处理**/
	protected void handleAttackCount()
	{
		if (demoresult.hpchange < 0)
		{
			if(opfighter.getFighterBean().getActioncount().containsKey(xbean.Fighter.PHY_ATTACK))
				opfighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_ATTACK, opfighter.getFighterBean().getActioncount().get(xbean.Fighter.PHY_ATTACK) + 1);
			else
				opfighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_ATTACK, 1);
			if(aimfighter.getFighterBean().getActioncount().containsKey(xbean.Fighter.PHY_HURT))
				aimfighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_HURT, aimfighter.getFighterBean().getActioncount().get(xbean.Fighter.PHY_HURT) + 1);
			else
				aimfighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_HURT, 1);
		}
		if (demoresult.assisterid != 0)
		{
			Fighter assistfighter = battleInfo.getFighterobjects().get(demoresult.assisterid);
			if(assistfighter.getFighterBean().getActioncount().containsKey(xbean.Fighter.PHY_ATTACK))
				assistfighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_ATTACK, assistfighter.getFighterBean().getActioncount().get(xbean.Fighter.PHY_ATTACK) + 1);
			else
				assistfighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_ATTACK, 1);
		}
		if (demoresult.protecterid != 0)
		{
			Fighter profighter = battleInfo.getFighterobjects().get(demoresult.protecterid);
			if(profighter.getFighterBean().getActioncount().containsKey(xbean.Fighter.PHY_HURT))
				profighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_HURT, profighter.getFighterBean().getActioncount().get(xbean.Fighter.PHY_HURT) + 1);
			else
				profighter.getFighterBean().getActioncount().put(xbean.Fighter.PHY_HURT, 1);
		}
	}
	
	protected boolean isMultiReturnHurt()
	{
		return false;
	}
}
