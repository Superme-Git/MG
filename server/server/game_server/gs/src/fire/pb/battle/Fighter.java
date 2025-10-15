package fire.pb.battle;


import java.util.Map;

import fire.pb.PropRole;
import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.battle.ai.BattleAIAgent;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffMonsterImpl;
import fire.pb.buff.BuffPetImpl;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.Module;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.buff.continual.DecisionBuff;
import fire.pb.effect.Role;
import fire.pb.main.ConfigManager;
import fire.pb.skill.Result;
import fire.pb.skill.SNuqiget;
import fire.pb.skill.SkillAgent;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SkillMonster;
import fire.pb.skill.SkillPet;
import fire.pb.skill.SkillRole;

public class Fighter
{
	protected xbean.BattleInfo battle;
	protected int fighterId; 
	protected xbean.Fighter fighterbean;
	protected BuffAgent buffagent;
	protected SkillAgent skillagent;
	protected fire.pb.effect.Role erole;
	protected BattleAIAgent aiagent;
	protected int saveRoleNum; //本场战斗救活了几个人,成就系统用
	private Result roundBuffResult = new Result(true);//本回合属性和BUFF的改变
	//final private Map<Integer,Result> results = new HashMap<Integer,Result>();//将回合Result分解到每一个动作中
	int tempSpeed=0;//乱敏速度加成,用来存放这场战斗开始，战斗者加成的乱敏速度
	int nFighterStar = 0;
	protected int nOwnerindx = 0;
	protected int bounusnum = 0;
	
	
	private boolean doubleSkill = false;
	
	public int getFightertype()
	{
		return fighterbean.getFightertype();
	}
	
	public boolean isRole()
	{
		return getFightertype() == xbean.Fighter.FIGHTER_ROLE;		
	}

	public boolean isPet()
	{
		return getFightertype() == xbean.Fighter.FIGHTER_PET;		
	}
	
/*	public int getFighterType()
	{
		return fighterbean.getFightertype();
	}	
*/	

	public void setOwner(int nindex)
	{
		nOwnerindx = nindex; 
	}
	
	public int getOwner()
	{
		return nOwnerindx; 
	}
	
	public boolean isPartner()
	{
		return ((getFightertype() == xbean.Fighter.FIGHTER_PARTNER)||(getFightertype() == xbean.Fighter.FIGHTER_SYSTEM_PARTNER));		
	}	
	
	public Fighter(xbean.BattleInfo battle,int fighterId)
	{
		if(battle == null)
			throw new IllegalArgumentException("xbean.BattleInfo is null");
		this.battle = battle;
		xbean.Fighter fighterbean = battle.getFighters().get(fighterId);
		if(fighterbean == null)
			throw new IllegalArgumentException("Fighter does not exist. Id = "+fighterId);
		this.fighterId = fighterId;
		this.fighterbean = fighterbean;
		int type = fighterbean.getFightertype();
		switch (type)
		{
		case xbean.Fighter.FIGHTER_ROLE:
			long roleId = fighterbean.getUniqueid();
			buffagent = new BuffRoleImpl(roleId);
			skillagent = new SkillRole(roleId);
			aiagent = new BattleAIAgent(this,battle);
			break;
		case xbean.Fighter.FIGHTER_PET:
			long masterId = battle.getFighters().get(fighterId - 5).getUniqueid();
			PropRole prole = new PropRole(masterId, true);
			int petkey = prole.getFightpetkey();
			if (petkey < 0)
				throw new IllegalArgumentException("No fight pet. roleId = " + masterId);
			buffagent = new BuffPetImpl(masterId, petkey);
			xbean.Pets petsBean = xtable.Pet.get(masterId);
			skillagent = new SkillPet(petsBean.getPetmap().get(petkey), masterId);
			aiagent = null;
			break;
		//case xbean.Fighter.FIGHTER_MONSTER:
		case xbean.Fighter.FIGHTER_PARTNER:
		case xbean.Fighter.FIGHTER_MONSTER_HIDE:
		case xbean.Fighter.FIGHTER_MONSTER_NPC:
		case xbean.Fighter.FIGHTER_SYSTEM_PARTNER:			
			xbean.Monster monsterbean = battle.getMonsters().get(fighterId);
			buffagent = new BuffMonsterImpl(monsterbean);
			
			long leaderid = 0;
			if (fighterbean.getFightertype() == xbean.Fighter.FIGHTER_PARTNER) //如果是伙伴 by changhao
			{
				//得到队长的id by changhao
				if (battle.getBattletype()==BattleType.BATTLE_PVP)
				{
					if (fighterId > 14) //说明是站在对面的一个团伙 by changhao
					{
						leaderid = battle.getFighters().get(15).getUniqueid();	
					}
					else
						leaderid = battle.getFighters().get(1).getUniqueid();					
				}
				else
				{
					leaderid = battle.getFighters().get(1).getUniqueid();					
				}	
			}

			skillagent = new SkillMonster(monsterbean, leaderid);
			aiagent = new BattleAIAgent(this,battle);
			break;
		default:
			throw new IllegalArgumentException("Fighter Type is wrong");
		}
		erole = buffagent.getERole();
	}
	
	protected Fighter(){}//给BattleFieldFighter用的
	
	/**获取fighter属于的xbean.BattleInfo*/
	public xbean.BattleInfo getBattle()
	{
		return battle;
	}
	
	/**取fighterId*/
	public int getFighterId()
	{
		return fighterId;
	}
	
	/**取xbean.Fighter*/
	public xbean.Fighter getFighterBean()
	{
		return fighterbean;
	}
	
	/**获取战斗者的排序速度*/
	public int getSeqSpeed()
	{
		return erole.getSpeed() + tempSpeed;
	}
	
	
	/**获取buffAgent，获取对buff相关操作*/
	public BuffAgent getBuffAgent()
	{
		return buffagent;
	}
	
	/**获取EffectRole，获取对属性的相关操作*/
	public Role getEffectRole()
	{
		return erole;
	}
	
	/**获取SkillAgent，获取对技能的相关操作*/
	public SkillAgent getSkillAgent()
	{
		return skillagent;
	}
	
	
	public int healHp(int v)
	{
		return erole.addHp(v);
	}
	
	/**战斗流程中使用，用于改变血量*/
	public int attachHpChange(int v)
	{
		int hpchange = erole.addHp(v);
		//根据掉血量增加SP
		int addsp = getAddSpFromHpChange(hpchange);
		if (addsp <= 0)
			return v;
		
		if (getSkillAgent().hasEffectSkill(SkillConstant.BATTLE_EQUIP_MORE_SP_SKILL))
			addsp = (int)(addsp * 1.2);
		erole.addSp(addsp, 0);
		getRoundBuffResult().getChangedAttrs().put(AttrType.SP, (float) erole.getSp());
		return v;
	}
	
	protected int getAddSpFromHpChange(int hpchange)
	{
		if(fighterbean.getFightertype() != xbean.Fighter.FIGHTER_ROLE)
			return 0;
		
		if(hpchange >= 0)
			return 0;//加血不加怒气
		if(erole.getMaxHp() <= 0)
			return 0;//加血不加怒气
		hpchange = -hpchange;//变为正值
		int ratio = (hpchange * 100) / erole.getMaxHp();//血量变化比例
		float angryExtra = 1f;//愤怒技能加成

		for(SNuqiget get : ConfigManager.getInstance().getConf(SNuqiget.class).values()){
			if(ratio>=get.hurtmin && ratio<get.hurtmax){
				return (int)(get.nuqiget*angryExtra);
			}
		}
		return 0;
	}
	
	public int attachMpChange(int v)
	{
		return erole.addMp(v);
	}

	/**获取当前存储的result*/
	public Result getRoundBuffResult()
	{
		return roundBuffResult;
	}
	/**更新当前存储的result*/
	public Result updateRoundBuffResult(Result newbuffResult)
	{
		roundBuffResult.updateResult(newbuffResult);
		return roundBuffResult;
	}
	/**将当前存储的result根据动作顺序存储起来*/
	public void storeActionAttrsChange(int actcount)
	{
		if (roundBuffResult.getChangedAttrs().size() == 0)
			return;// 没有变化不保存
		if(fighterbean.getFightertype() != xbean.Fighter.FIGHTER_ROLE && fighterbean.getFightertype() != xbean.Fighter.FIGHTER_PET)
			return;//只有人和宠物才保存
		ActionChangedAttrs actionAttrs = battle.getFighterchangedattrs().get(fighterId);
		if(actionAttrs == null)
		{
			actionAttrs = new ActionChangedAttrs();
			battle.getFighterchangedattrs().put(fighterId,actionAttrs);
		}
		actionAttrs.getChangedAttrs().put(actcount,fire.pb.effect.Module.getClientAttrs(roundBuffResult.getChangedAttrs()));
		roundBuffResult.getChangedAttrs().clear();
	}
	
	/**检查存不存在不能行动的buff，例如睡眠封印等*/
	public boolean canAction(int operationType,int operationId)
	{
		if(!validLimitOperationBuffs(operationType))
			return false;
		switch(operationType)
		{
		case OperationType.ACTION_ATTACK:
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
				return false;			
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP2)||buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS)||buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_FROZEN))
				return false;
			break;
		case OperationType.ACTION_SKILL:
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
				return false;			
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP2)||buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_FROZEN_MANA)&& (operationId != SkillConstant.BATTLE_SKILL_BASIC_CHAOS_ATTACK||operationId != SkillConstant.BATTLE_SKILL_BASIC_CTRL_ATTACK))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_FROZEN))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			if((buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS)||buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS_ADV)) && operationId != SkillConstant.BATTLE_SKILL_BASIC_CHAOS_ATTACK)
				return false;
			break;
			
		case OperationType.ACTION_UNIQUE_SKILL:
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS))
				return false;
			break;
		case OperationType.ACTION_SPECIAL_SKILL:
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
				return false;			
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP2)||buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS))
				return false;
			break;
		case OperationType.ACTION_USEITEM:
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
				return false;			
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP2)||buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS)||buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
				return false;
			break;
		case OperationType.ACTION_PROTECT://因为保护生效时不是在执行本人的Decision，所以要验证一下状态
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
				return false;			
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP2)||buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_FROZEN))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS)||buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
				return false;
			break;			
		case OperationType.ACTION_SUMMON://普通召唤要加限制
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
				return false;			
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP2)||buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			break;
		case OperationType.ACTION_SUMMON_INSTANT://因为立即召唤是AI怪用的，所以不加限制
			break;
		case OperationType.ACTION_DEFEND://为防御生效时不是在执行本人的Decision，所以要验证一下状态
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS)||buffagent.existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
				return false;
			break;
		case OperationType.ACTION_ESCAPE_INSTANT://因为立即逃跑是AI怪用的，所以不加限制
			break;
		case OperationType.ACTION_ESCAPE://逃跑
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			break;
		case OperationType.ACTION_CATHCH:
			if((battle.getBattletype() == BattleType.BATTLE_LINE && getBounusNum() <=0))
				return false;
		default:
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
				return false;			
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP2)||buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_SLEEP))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
		}
		return true;
	}
	
	private boolean validLimitOperationBuffs(int operationType)
	{
		for(int buffId : fire.pb.buff.Module.getInstance().getLimitBuffIdsByOperType(operationType))
		{
			if(buffagent.existBuff(buffId))
				return false;
		}
		return true;
	}
	
	/**检查存不存在不能被行动的buff，例如死亡等*/
	public boolean canBeAction(int operationType)
	{		
		switch(operationType)
		{
		case OperationType.ACTION_ATTACK:
		case OperationType.ACTION_PROTECT:
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			else if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			break;
		case OperationType.ACTION_SKILL:
			break;
		case OperationType.ACTION_UNIQUE_SKILL:
		case OperationType.ACTION_SPECIAL_SKILL:
			break;
		case OperationType.ACTION_USEITEM:
			break;
		case OperationType.ACTION_CATHCH:
			if(buffagent.existBuff(BuffConstant.CONTINUAL_DEATH))
				return false;
			else if(buffagent.existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
			break;
			
		}
		return true;
	}

	public int getTempSpeed() {
		return tempSpeed;
	}

	public void setTempSpeed(int tempSpeed) {
		this.tempSpeed = tempSpeed;
	}

	/**获取AIAgent*/
	public BattleAIAgent getAiagent()
	{
		return aiagent;
	}
	

	/**设置下一个是否是法术连击*/
	public void setDoubleSkill(boolean doubleSkill)
	{
		this.doubleSkill = doubleSkill;
	}
	/**是否正处在法术连击中*/
	public boolean isDoubleSkill()
	{
		return doubleSkill;
	}
	
	/**检查是否有DecisionBuff*/
	public DecisionBuff getDecisonBuff()
	{
		//因为这个buff只能在战斗中存在，所以只检查那些临时buff即可
		for(Map.Entry<Integer, xbean.Buff> entry : getBuffAgent().getAgent().getBuffs().entrySet())
		{
			int buffId = entry.getKey();
			ConstantlyBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId);
			if(cfg.getClassName().equals(BuffConstant.DECISON_BUFF_CLASS_NAME))
				return (DecisionBuff)fire.pb.buff.Module.getInstance().createConstantlyBuff(entry.getValue());
		}
		return getHateDecisonBuff();
	}

	/**检查是否有DecisionBuff*/
	public DecisionBuff getHateDecisonBuff()
	{
		//因为这个buff只能在战斗中存在，所以只检查那些临时buff即可
		for(Map.Entry<Integer, xbean.Buff> entry : getBuffAgent().getAgent().getBuffs().entrySet())
		{
			int buffId = entry.getKey();
			ConstantlyBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId);
			if(cfg.getClassName().equals(BuffConstant.HATEDECISON_BUFF_CLASS_NAME))
			{
				if(BattleField.checkOutBattle(battle, entry.getValue().getEffects().get(EffectType.AIM).intValue())
						||!battle.getFighterobjects().get(entry.getValue().getEffects().get(EffectType.AIM).intValue()).canBeAction(OperationType.ACTION_ATTACK))
						return null;
				return (DecisionBuff)fire.pb.buff.Module.getInstance().createConstantlyBuff(entry.getValue());
			}
		}
		return null;
	}
	
	public int getSaveRoleNum() {
	
		return saveRoleNum;
	}

	
	public void setSaveRoleNum(int saveRoleNum) {
	
		this.saveRoleNum = saveRoleNum;
	}
	
	public int getBounusNum() {
		
		return bounusnum;
	}

	
	public void setBounusNum(int bounusnum) {
	
		this.bounusnum = bounusnum;
	}
	
	public void sendAlreadyUsedItems()
	{
		if(getFightertype() != xbean.Fighter.FIGHTER_ROLE)
			return;
		if(getFighterBean().getUseditems().isEmpty())
			return;
		SSendAlreadyUseItem sSendAlreadyUseItem = new SSendAlreadyUseItem();
		sSendAlreadyUseItem.itemlist.putAll(getFighterBean().getUseditems());
		mkdb.Procedure.psendWhileCommit(getFighterBean().getUniqueid(), sSendAlreadyUseItem);
	}
	
	public boolean cankickout(Fighter killer)
	{
		if(getFightertype() > xbean.Fighter.FIGHTER_ROLE)
		{	
			if (getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_FIRST) ||getBuffAgent().existBuff(BuffConstant.CONTINUAL_PET_GHOST_SECOND) )
			{
				
				if (killer != null && (killer.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ANTI_GHOST)
										||killer.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ANTI_GHOST_FIRST)
										||killer.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ANTI_GHOST_SECOND)))
					return true;//驱魔
			}
			else
				return true;//击飞
		}
		return false;//死在当地
	}
}

