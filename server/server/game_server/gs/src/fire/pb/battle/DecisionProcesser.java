package fire.pb.battle;

import java.util.Map;

import xbean.HookData;
import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.battle.ai.BattleAIManager;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.DecisionBuff;
import fire.pb.npc.MonsterConfig;
import fire.pb.option.PAddProtector;
import fire.pb.school.SchoolConst;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.util.Misc;
import fire.script.JavaScript;

public class DecisionProcesser
{
	public static void addRoleDecision(xbean.BattleInfo battle, Integer index, Operation ro, boolean isauto)
	{
		if (battle.getDecisions().get(index) != null)
			return;
		Fighter fighter = battle.getFighterobjects().get(index);
		if (fighter == null)
			return;
		xbean.Decision decision = null;
		DecisionBuff dbuff = fighter.getDecisonBuff();
		if (dbuff != null)
		{			
			decision = ((DecisionBuff) dbuff).getDecision();
			if(dbuff.getId() == BuffConstant.CONTINUAL_REST&&(ro.operationtype == OperationType.ACTION_ESCAPE||
					ro.operationtype == OperationType.ACTION_SUMMON||ro.operationtype == OperationType.ACTION_WITHDRAW))
			{
				decision.setAim(ro.aim);
				decision.setOperateid(ro.operationid);
				decision.setOperatetype(ro.operationtype);
				decision.setOperator(index);
				decision.setIsauto(isauto);				
			}
		}
		else
		{
			decision = xbean.Pod.newDecision();
			decision.setAim(ro.aim);
			decision.setOperateid(ro.operationid);
			decision.setOperatetype(ro.operationtype);
			decision.setOperator(index);
			decision.setIsauto(isauto);
			if(isauto)
				BattleField.dealBabyFight(battle,battle.getFighterobjects().get(index),decision,false);
		}
		addDecisionWithSP(battle, decision);
	}

	public static void addPetDecision(xbean.BattleInfo battle, Integer index, Operation po, boolean isauto)
	{

		if (battle.getDecisions().get(index) != null)
			return;
		if (BattleField.checkOutBattle(battle, index))
			return;
		Fighter fighter = battle.getFighterobjects().get(index);
		if (fighter == null)
			return;
		// 忠诚度判断

		xbean.Decision decision = null;
		decision = xbean.Pod.newDecision();
		decision.setAim(po.aim);
		if(po.operationtype == OperationType.ACTION_SKILL && !fighter.getSkillAgent().hasEffectSkill(po.operationid))
		{
			decision.setOperateid(0);
			decision.setOperatetype(OperationType.ACTION_ATTACK);
		}
		else
		{
			decision.setOperateid(po.operationid);
			decision.setOperatetype(po.operationtype);
		}
		decision.setOperator(index);
		decision.setIsauto(isauto);
		if(isauto)
			BattleField.dealBabyFight(battle,battle.getFighterobjects().get(index),decision,false);
		addDecisionWithSP(battle, decision);
	}

	/**为场景设置操作*/
	public static void setFieldfighterAction(final xbean.BattleInfo battle)
	{
		xbean.Decision decision = BattleAIManager.getInstance().getDecision(battle, 0);
		addDecisionWithSP(battle, decision);
	}
	/**为怪物设置操作，可能会有怪物的AI 因为只设置怪物的动作，所以不用锁其他的锁*/
	public static void setMonsterAction(final xbean.BattleInfo battle)
	{
		for (Fighter monsterfighter : battle.getFighterobjects().values())
		{
			xbean.Decision decision = null;

			if (monsterfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_ROLE ||
					monsterfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_PET)
				continue;			
			if (BattleField.checkOutBattle(battle, monsterfighter.getFighterId()))
				continue;
			if (battle.getDecisions().containsKey(monsterfighter.getFighterId()))
				continue;
			
			// 1 查找DecisionBuff
			DecisionBuff dbuff = monsterfighter.getDecisonBuff();
			if (dbuff != null)
			{
				decision = ((DecisionBuff) dbuff).getDecision();
				if(decision != null && decision.getOperator() == 0)
					decision.setOperator(monsterfighter.getFighterId());					
			}
			if(decision == null)
				decision = BattleAIManager.getInstance().getDecision(battle, monsterfighter.getFighterId());
			
			// 3 查找怪是否有逃跑几率
			if(decision == null)
			{
				xbean.Monster xmonster = battle.getMonsters().get(monsterfighter.getFighterId());
				MonsterConfig cfg = Monster.getMonsterConfig(xmonster.getMonsterid());
				if(cfg != null && Misc.checkRate(1000, cfg.getRunRate()))
				{
					decision = xbean.Pod.newDecision();
					decision.setOperator(monsterfighter.getFighterId());
					decision.setOperatetype(OperationType.ACTION_ESCAPE);
				}
			}
			
			// 4 找怪物中是否有随机动作（技能或普通攻击）
			if (decision == null)
				decision = getMonsterRandomDecision(battle, monsterfighter);
			
			addDecisionWithSP(battle, decision);
		}
	}

	/**获取怪物随机的决定*/
	public static xbean.Decision getMonsterRandomDecision(xbean.BattleInfo battle, Fighter monsterfighter)
	{
		xbean.Decision decision = xbean.Pod.newDecision();
		final int enemyIndex = 0;//BattleField.getRandEnemy(battle, monsterfighter.getFighterId());

		xbean.Monster xmonster = battle.getMonsters().get(monsterfighter.getFighterId());

		int[] rates = new int[xmonster.getSkills().size() + 1];

		int i = 0;
		int ratesum = 0;
		int remainrate = 0;
		for (xbean.MonsterSkill mskill : xmonster.getSkills())
		{
			FightSkillConfig conf = fire.pb.skill.Module.getInstance().getFightSkillConfig(mskill.getId());
			if (conf == null || !conf.isActiveSkill() || !monsterfighter.canAction(OperationType.ACTION_SKILL, mskill.getId()) || !checkMonsterHpMpConsume(conf, battle, monsterfighter))
			{
				rates[i]=0;
				remainrate+= mskill.getCastrate();
			}
			else
			{
				rates[i] = mskill.getCastrate();
			}
			ratesum += mskill.getCastrate();
			i++;
		}
		if(rates.length > 1 && remainrate > 0)
		{
			float fext = 0.0f;
			if(remainrate != ratesum)
				fext = remainrate/(ratesum - remainrate);
			for(int rate :rates)
			{
				if(rate>0)
					rate = (int)(rate*(1+fext))+1;
			}
		}
		rates[xmonster.getSkills().size()] = (ratesum > 1000) ? 0 : (1000 - ratesum);
		int chose = Misc.getProbability(rates);

		decision.setOperator(monsterfighter.getFighterId());
		if (chose==-1 || chose == xmonster.getSkills().size())
		{
			if(monsterfighter.getFightertype() == xbean.Fighter.FIGHTER_PARTNER && monsterfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FROZEN_MANA)
					&&(xmonster.getSchool() != SchoolConst.WARRIOR && xmonster.getSchool() != SchoolConst.HUNTER && xmonster.getSchool() != SchoolConst.ROGUE))
			{
				BattleField.logger.debug("怪物中了沉默buff，改为防御");
				decision.setOperatetype(OperationType.ACTION_DEFEND);
				decision.setOperateid(0);
				decision.setAim(monsterfighter.getFighterId());
			}
			else if(monsterfighter.getFightertype() == xbean.Fighter.FIGHTER_PARTNER && monsterfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FROZEN))
			{
				BattleField.logger.debug("怪物中了定身buff，改为防御");
				decision.setOperatetype(OperationType.ACTION_DEFEND);
				decision.setOperateid(0);
				decision.setAim(monsterfighter.getFighterId());
			}
			else
			{
				decision.setOperatetype(OperationType.ACTION_ATTACK);
				decision.setOperateid(0);
				decision.setAim(enemyIndex);
			}
		} 
		else
		{
			xbean.MonsterSkill mskill = xmonster.getSkills().get(chose);
			FightSkillConfig conf = fire.pb.skill.Module.getInstance().getFightSkillConfig(mskill.getId());
			if (conf == null || !conf.isActiveSkill() || !monsterfighter.canAction(OperationType.ACTION_SKILL, mskill.getId()) || !checkMonsterHpMpConsume(conf, battle, monsterfighter))
			{// 没有该技能，或者该技能为被动技能，或者不能使用技能，或者放该技能的蓝不够
				if(monsterfighter.getFightertype() == xbean.Fighter.FIGHTER_PARTNER && monsterfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FROZEN_MANA)
						&&(xmonster.getSchool() != SchoolConst.WARRIOR && xmonster.getSchool() != SchoolConst.HUNTER && xmonster.getSchool() != SchoolConst.ROGUE))
				{
					BattleField.logger.debug("怪物中了沉默buff，改为防御");
					decision.setOperatetype(OperationType.ACTION_DEFEND);
					decision.setOperateid(0);
					decision.setAim(monsterfighter.getFighterId());
				}
				else if(monsterfighter.getFightertype() == xbean.Fighter.FIGHTER_PARTNER && monsterfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FROZEN))
				{
					BattleField.logger.debug("怪物中了定身buff，改为防御");
					decision.setOperatetype(OperationType.ACTION_DEFEND);
					decision.setOperateid(0);
					decision.setAim(monsterfighter.getFighterId());
				}
				else
				{
					decision.setOperatetype(OperationType.ACTION_ATTACK);
					decision.setOperateid(0);
					decision.setAim(enemyIndex);
					BattleField.logger.error("ai随机选择技能。如果跑到这里填表或程序已经有bug了，请和程序联系!");
				}
			} else
			{
				decision.setOperatetype(OperationType.ACTION_SKILL);
				decision.setOperateid(mskill.getId());
																
				java.util.List<Integer> buffs = new java.util.ArrayList<Integer>();
				buffs.add(BuffConstant.CONTINUAL_CHANRAOGENXU);
				java.util.List<Integer> filterbuffids = BattleField.GetFilterAimsByBuffs(mskill.getId(), buffs);

				decision.setAim(BattleField.getRandTargetByType(battle, monsterfighter.getFighterId(), conf.getType(),false,false, filterbuffids));
			}
		}
		return decision;
	}

	/**处理防御和保护，两者都需要先把状态设置好*/
	private static boolean processProtectAndDefend(xbean.BattleInfo battle, xbean.Decision decision)
	{
		if (decision.getOperatetype() == OperationType.ACTION_PROTECT)
		{
			if (decision.getOperator() == decision.getAim())
				return false;
			new PAddProtector(battle.getBattleid(), decision.getOperator(), decision.getAim()).call();
			return true;

		} else if (decision.getOperatetype() == OperationType.ACTION_DEFEND)
		{
			int result = battle.getRoundresult().get(decision.getOperator());
			battle.getRoundresult().put(decision.getOperator(), result | ResultType.RESULT_DEFENCE);
			return true;
		}
		return false;
	}

	/**先发技能的速度加成 不做合法性检验，只加上速度，合法性留到技能释放时再去检验
	 * 到释放时如果检验不合法，不会释放技能，这样只是加上了速度，并不影响战斗 此buff和速度属性改变不通知客户端
	 */
	private static void addInitiveSkillSpeed(xbean.BattleInfo battle, xbean.Decision decision)
	{
		if (decision.getOperatetype() != OperationType.ACTION_SKILL)
			return;
		FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(decision.getOperateid());
		if (sconf == null || sconf.getSpeedBonus() == 0)
			return;
		ConstantlyBuff cbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_INIT_SPEED_BONUS);
		cbuff.getEffects().put(EffectType.SPEED_ABL, (float) sconf.getSpeedBonus());
		Fighter fighter = battle.getFighterobjects().get(decision.getOperator());
		if(fighter != null)
			fighter.getBuffAgent().addCBuff(cbuff);// 先发速度buff不需要通知客户端
	}

	/**检查怪物放技能的mp消耗，以避免血蓝不够不出手*/
	public static boolean checkMonsterHpMpConsume(FightSkillConfig skillConfig, xbean.BattleInfo battle, Fighter opfighter)
	{
		try
		{
			if(battle.getRound() < 3 && skillConfig.getHpConsumeJavaScript().GetDoubleValue()==null)
				return true;
			if (opfighter == null)
				return false;
			battle.getEngine().setSkillLevel(opfighter.getSkillAgent().getLevel(skillConfig.getSkillIndex()));
			battle.getEngine().setOpFighter(opfighter,skillConfig.getUsedAttrTypea());
			battle.getEngine().setSkillTargetNum(1);
			// 检查mp
			Double mpconsume = skillConfig.getMpConsumeJavaScript().eval(battle.getEngine(),opfighter,null);
			if (mpconsume > opfighter.getEffectRole().getMp())
				return false;

			if(battle.getBattletype()% 10 == 1 && skillConfig.getIsOnlyPve())
				return false;
			
			if(skillConfig.getHpConsumeJavaScript() != null)
			{
				Double hpconsume = skillConfig.getHpConsumeJavaScript().eval(battle.getEngine(),opfighter,null);
				if (hpconsume >= opfighter.getEffectRole().getHp())
					return false;
			}
			
			if(skillConfig.getSpConsumeJavaScript() != null)
			{
				int spconsume = skillConfig.getSpConsumeJavaScript().eval(battle.getEngine(),opfighter,null).intValue();
				if (spconsume > opfighter.getEffectRole().getSp())
					return false;
			}
			
			if(skillConfig.getEpConsumeJavaScript() != null && opfighter.getFighterBean() != null && opfighter.getFighterBean().getInitattrs() != null)
			{
				Double epconsume = skillConfig.getEpConsumeJavaScript().eval(battle.getEngine(),opfighter,null);
				if (epconsume > opfighter.getFighterBean().getInitattrs().get(AttrType.EFFECT_POINT))
					return false;
			}
			
			if(skillConfig.getSubSkills()!= null&&skillConfig.getSubSkills().length > 0)
			{
				SubSkillConfig subSkill = skillConfig.getSubSkills()[0];
				if(!subSkill.getConfilictStateJs().evalToBoolean(battle.getEngine(),opfighter,null))
					return false;
			}
			
			return true;
		} catch (Exception e)
		{
			BattleField.logger.error(e);
		}
		return false;
	}

	/**将Decision添加到battle，处理先发速度、防御、保护，并广播通知*/
	private static void addDecisionWithSP(xbean.BattleInfo battle, xbean.Decision decision)
	{
		if(decision == null)
			return;
		battle.getDecisions().put(decision.getOperator(), decision);
		processProtectAndDefend(battle, decision);
		addInitiveSkillSpeed(battle, decision);// 先发技能速度加成
		xbean.Fighter xfighter = battle.getFighters().get(decision.getOperator());
		if(xfighter == null || xfighter.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER)
			return;
		SSendBattlerOperateState snd = new SSendBattlerOperateState(decision.getOperator(), 2);
		mkdb.Procedure.psendWhileCommit(battle.getRoleids().keySet(), snd);
	}
	
	public static xbean.Decision getDefaultDecision(final xbean.BattleInfo battle, final xbean.Fighter fighter){
		final xbean.Decision d = xbean.Pod.newDecision();
		d.setOperator(fighter.getBattleindex());
		d.setAim(0);
		if(fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
		{
			HookData hookData =  xtable.Rolehook.get(fighter.getUniqueid());
			if(hookData == null)
			{
				d.setOperatetype(OperationType.ACTION_ATTACK);
			}
			else
			{
				d.setOperatetype(hookData.getCharoptype());
				d.setOperateid(hookData.getCharopid());
			}
		}
		else if(fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
		{
			HookData hookData =  xtable.Rolehook.get(fighter.getUniqueid());
			if(hookData == null)
			{
				d.setOperatetype(OperationType.ACTION_ATTACK);
			}
			else
			{
				d.setOperatetype(hookData.getPetoptype());
				d.setOperateid(hookData.getPetopid());
			}
		}
		else
			d.setOperatetype(OperationType.ACTION_ATTACK);
		return d;
	}
	
	public static Operation getDefaultOperation(){
		Operation operation = new Operation();
		operation.operationtype = OperationType.ACTION_ATTACK;
		operation.aim = 0;
		operation.operationid = 0;
		return operation;
	}
	
	public static Operation getRoleDefaultOperation(long roleId){
		Operation operation = new Operation();
		HookData hookData =  xtable.Rolehook.get(roleId);
		if(hookData == null)
		{
			operation.operationtype = OperationType.ACTION_ATTACK;
			operation.aim = 0;
			operation.operationid = 0;
		}
		else
		{
			operation.operationtype = hookData.getCharoptype();
			operation.aim = 0;
			operation.operationid = hookData.getCharopid();
		}
		return operation;
	}	

	public static Operation getPetDefaultOperation(long roleId){
		Operation operation = new Operation();
		HookData hookData =  xtable.Rolehook.get(roleId);
		if(hookData == null)
		{
			operation.operationtype = OperationType.ACTION_ATTACK;
			operation.aim = 0;
			operation.operationid = 0;
		}
		else
		{
			operation.operationtype = hookData.getPetoptype();
			operation.aim = 0;
			operation.operationid = hookData.getPetopid();
		}
		return operation;
	}	
}
