package fire.pb.skill.fight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.PropConf;
import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.FightAttrType;
import fire.pb.battle.BattleAimRelation;
import fire.pb.battle.BattleField;
import fire.pb.battle.BattleType;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.FighterInfo;
import fire.pb.battle.FighterSorter;
import fire.pb.battle.FormationConfig;
import fire.pb.battle.Monster;
import fire.pb.battle.OperationType;
import fire.pb.battle.PNewBattle;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SubResultItem;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.IBuff;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.buff.continual.DecisionBuff;
import fire.pb.buff.single.Injure;
import fire.pb.buff.single.MagicInjure;
import fire.pb.buff.single.SingleBuff;
import fire.pb.buff.single.SkillComboInjure;
import fire.pb.buff.single.SkillPhyInjure;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.option.BasicOperation;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Module;
import fire.pb.skill.Result;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.SubSkillFighterList;
import fire.pb.talk.ChannelType;
import fire.pb.talk.STransChatMessage2Client;
import fire.pb.util.ParseObjIDInBattle;
import fire.script.JavaScript;
import xbean.BattleInfo;

public class FightSkill
{
	final protected xbean.BattleInfo battle;
	final protected int type;// 普通攻击，技能，特技，使用物品走的都是技能流程，所以此处有类型
	protected int targetType;// 技能的目标类型
	final protected int operator; // 动作的使用者
	protected Fighter opfighter;
	protected Fighter aimfighter;// aimfighter可能为null
	protected List<Fighter> mainAimFighters;
	protected int aim;
	final protected int skillId;
	protected int skillLevel = 1;
	protected int subSkillindex = 0;
	final protected FightSkillConfig skillConfig;
	protected SubSkillFighterList[] subSkillFighters;
	protected int mainBuff = 0;// 主buff一定是0号buff
	protected Map<Integer,Boolean> mainBuffHit = new HashMap<Integer, Boolean>();//主buff是否命中,key = fighterid
	protected ResultItem item;
	protected Set<Integer> deadfighterIds = new HashSet<Integer>();

	protected int failReason = 0;
	protected boolean iswithhidebuffeffect = false;
	protected boolean isauto = true;

	public FightSkill(final xbean.BattleInfo battle, final int operator, final int aim, int skillId, int type)
	{
		this.battle = battle;
		this.operator = operator;
		this.opfighter = battle.getFighterobjects().get(operator);
		this.skillId = skillId;
		this.type = type;
		this.subSkillindex = 0;		
		this.isauto =(aim==0); 
		this.skillConfig = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
		if (skillConfig == null)
		{
			Module.logger.error("ERROR: Battle Skill is not exist. skillId = " + skillId);
			throw new IllegalArgumentException("ERROR: Battle Skill is not exist. skillId = " + skillId);
		}
		if(opfighter!=null)
		{
			DecisionBuff dbuff = opfighter.getHateDecisonBuff();
			if (dbuff != null&& skillConfig.getCanEffectByHate())
			{			
				xbean.Decision decision = ((DecisionBuff) dbuff).getDecision();
				if(decision != null)
					this.aim = decision.getAim();
			}
			else
				this.aim = aim;
		}
		else
			this.aim = aim;

		this.aimfighter = battle.getFighterobjects().get(this.aim);
		if (BattleField.logger.isDebugEnabled()) { BattleField.logger.debug("施法者Id: " + operator + "原受击者Id：" +  this.aim + " 技能ID：" + skillId);}
		this.targetType = skillConfig.getTargettype();
		subSkillFighters =  new SubSkillFighterList[skillConfig.getSubSkills().length];
	}

	public int getSkillId()
	{
		return skillId;
	};

	public int getLevel()
	{
		if (skillLevel == 0)
			return 1;
		else
			return skillLevel;
	};

	public int getType()
	{
		return type;
	}

	public int getHPconsume()
	{
		if (opfighter.isDoubleSkill())
			return 0;
		Double result = -skillConfig.getHpConsumeJavaScript().eval(battle.getEngine(),opfighter,null);
		return result.intValue();

	}

	public int getMPconsume()
	{
		if (opfighter.isDoubleSkill())
			return 0;
		Double result = -skillConfig.getMpConsumeJavaScript().eval(battle.getEngine(),opfighter,null);
		return result.intValue();
	}

	public int getSPconsume()
	{
		if (opfighter.isDoubleSkill())
			return 0;
		float sprate = 1.0f;
		if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.BATTLE_EQUIP_LESS_SP_SKILL))
			sprate = 0.8f;
//			item.execute.spconsume = (int) (item.execute.spconsume*0.8f);
		Double result = -skillConfig.getSpConsumeJavaScript().eval(battle.getEngine(),opfighter,null)*sprate;
		return result.intValue();
	}

	public int getEPconsume()
	{
		if (opfighter.isDoubleSkill())
			return 0;
		Double result = -skillConfig.getEpConsumeJavaScript().eval(battle.getEngine(),opfighter,null);
		return result.intValue();
	}

	// *************************************技能能否释放*************************************//
	public boolean canCastSummon()
	{
		// 检查施法者还存在
		if (!checkOperator())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("施放技能失败：checkOperator，施法者不合法。 施法者Id: " + operator + " ；技能ID：" + skillId);}
			return false;
		}

		// 检查与目标的关系，看是否能成为技能的目标(包括隐身的检查),不合法时换目标
		// 检查主buff，也就是技能的目标
		if (!checkMainBuffTarget())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("施放技能失败：checkMainBuffTarget，主buff无目标。 施法者Id: ").append(operator).append(" ；技能ID：").append(skillId));}
			return false;
		}

		// 检查自身血蓝满足技能需求
		if (!checkHpMpRequire())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("施放技能失败：checkHpMpRequire，血蓝需求不满足。 施法者Id: ").append(operator).append(" ；技能ID：").append(skillId));}
			return false;
		}
		
		// 检查自身血蓝满足技能消耗
		if (!checkHpMpConsume())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("施放技能失败：checkHpMpRequire，血蓝消耗不满足。 施法者Id: ").append(operator).append(" ；技能ID：").append(skillId));}
			return false;
		}

		return true;
	}
	
	public boolean canCast()
	{
		// 检查施法者还存在
		if (!checkOperator())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("施放技能失败：checkOperator，施法者不合法。 施法者Id: " + operator + " ；技能ID：" + skillId);}
			return false;
		}

		// 检查关系，验证客户端发来的目标是否合法
		if (!checkRelation())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("技能更换目标：checkRelation，原目标不合法。 施法者Id: " + operator + ";原受击者Id：" + aim + " ；技能ID：" + skillId);}
			
			//如果有缠绕根须BUF， 检查这个技能是否也带缠绕根须BUF by changhao
			java.util.List<Integer> buffs = new java.util.ArrayList<Integer>();
			buffs.add(BuffConstant.CONTINUAL_CHANRAOGENXU);
			java.util.List<Integer> filterbuffids = BattleField.GetFilterAimsByBuffs(skillId, buffs);
			
			// 当目标不合法时，帮助其更换目标
			aim = BattleField.getRandTargetByType(battle, operator, getTargetType(),false,false, filterbuffids);
			if (aim == 0)
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("技能更换目标失败。 TargetType: " + getTargetType() );}
				return false;
			}
			aimfighter = battle.getFighterobjects().get(aim);
			if (aimfighter == null)
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("技能更换目标失败。 找不到figterid为:" + aim + " 的战斗者");}
				return false;
			}
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("技能更换目标成功。 目标figterid更改为:" + aim + " 的战斗者");}
		}

		// 检查与目标的关系，看是否能成为技能的目标(包括隐身的检查),不合法时换目标
		// 检查主buff，也就是技能的目标
		if (!checkMainBuffTarget())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("施放技能失败：checkMainBuffTarget，主buff无目标。 施法者Id: ").append(operator).append(" ；技能ID：").append(skillId));}
			return false;
		}

		// 检查自身血蓝满足技能需求
		if (!checkHpMpRequire())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("施放技能失败：checkHpMpRequire，血蓝需求不满足。 施法者Id: ").append(operator).append(" ；技能ID：").append(skillId));}
			return false;
		}
		// 检查自身血蓝满足技能消耗
		if (!checkHpMpConsume())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("施放技能失败：checkHpMpRequire，血蓝消耗不满足。 施法者Id: ").append(operator).append(" ；技能ID：").append(skillId));}
			return false;
		}
		
		if(skillConfig.getSubSkills().length == 1 && !canCastSub(skillConfig.getSubSkills()[0]))
		{
			failReason = BasicOperation.FAIL_CANNOT_ACTION;
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("只有1个子技能失败，子技能无法执行").append(operator).append(" ；技能ID：").append(skillId));}
			return false;
		}
		return true;
	}
	
	public boolean canCastSub(SubSkillConfig subskill)
	{
		if (BattleField.checkOutBattle(battle, opfighter.getFighterId())||(opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH)||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST)))
		{//有可能被在上个子技能反击死亡了。不再能进行此项操作了。
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("技能执行者状态不对，或是中限制该操作的buff了!");}
			return false;
		}
		
		// 检查自身属性状态满足技能需求
		if (!checksStateRequire(subskill))
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("施放技能失败:checksStateRequire,属性状态需求不满足.施法者Id: ").append(operator).append(" ;技能ID：").append(skillId).append(" ;子技能ID：").append(subskill.getSubSkillId()));}
			return false;
		}
		
		// 检查施法者自身BUFF的冲突和依赖
		if (!checkClashAndDepend(subskill))
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("施放技能失败:checkClashAndDepend,技能依赖不合法. 施法者Id: ").append(operator).append(" ;技能ID：").append(skillId).append(" ;子技能ID：").append(subskill.getSubSkillId()));}
			return false;
		}
		return true;
	}

	protected boolean checkSkill()
	{/*//只有主角需要做技能校验。
		if(opfighter.getFighterType() != xbean.Fighter.FIGHTER_ROLE )
			return true;*/
		return opfighter.getSkillAgent().hasEffectSkill(skillId);
	}

	protected List<Fighter> getPossibleSortedFighters(SubSkillConfig subskill)
	{
		List<Fighter> tmpFighters = new ArrayList<>();
		List<Fighter> campfighters = getFightersByCampType(subskill.getGoalCampType());
		List<Fighter> racefighters = FilterFightersByRaceType(campfighters, subskill.getGoalRaceTypes());
		List<Fighter> monsteridfighters = FilterFightersByMonsterID(racefighters, subskill.getGoalMonsterids());		
		List<Fighter> fighters = FilterFightersByRelationType(monsteridfighters, subskill.getGoalRelationTypes());				
 		switch (subskill.getTargettype())
		{
		case SkillConstant.TARGET_AIM_CONFIRM_ASLAST://跟上个技能目标相同
			{
				if(subSkillindex == 0 || skillConfig.getSubSkills().length <= subSkillindex)
				{
					fighters.clear();
					break;
				}
				SubSkillFighterList lastsubskillfighter = subSkillFighters[subSkillindex-1];
				if(lastsubskillfighter != null)
				{
					for(Fighter fighter : fighters )
					{
						if(!lastsubskillfighter.getFighterIDs().contains(fighter.getFighterId()))
							continue;
						tmpFighters.add(fighter);
					}
				}
			}
			break;
		case SkillConstant.TARGET_AIM_CONFIRM_ASFIRST://跟首个技能目标相同（首个目标
			{
				if(subSkillindex == 0 || skillConfig.getSubSkills().length <= subSkillindex)
				{
					fighters.clear();
					break;
				}
				SubSkillFighterList firstsubskillfighter = subSkillFighters[0];
				if(firstsubskillfighter != null)
				{
					for(Fighter fighter : fighters )
					{
						if(!firstsubskillfighter.getFighterIDs().contains(fighter.getFighterId()))
							continue;
						tmpFighters.add(fighter);
					}
				}
			}
			break;
		case SkillConstant.TARGET_AIM_CONFIRM_DIFFLAST://跟上个技能目标不同
			{
				if(subSkillindex == 0 || skillConfig.getSubSkills().length <= subSkillindex)
				{
					fighters.clear();
					break;
				}
				SubSkillFighterList lastsubskillfighter = subSkillFighters[subSkillindex-1];
				if(lastsubskillfighter != null)
				{
					for(Fighter fighter : fighters )
					{
						if(lastsubskillfighter.getFighterIDs().contains(fighter.getFighterId()))
							continue;
						tmpFighters.add(fighter);
					}
				}
			}
			break;
		case SkillConstant.TARGET_AIM_CONFIRM_DIFFALL://跟之前所有技能的目标不同（这个时候排序变量也是有用的，如果我此时填speed，则选择过滤掉之前子技能目标后，选择速度最高的目标作为打击目标）
			{
				if(subSkillindex == 0 || skillConfig.getSubSkills().length <= subSkillindex)
				{
					fighters.clear();
					break;
				}
				for(Fighter fighter : fighters )
				{
					tmpFighters.add(fighter);
				}				
				for(SubSkillFighterList subskillfighter : subSkillFighters )
				{
					if(subskillfighter != null && subskillfighter.getFighterIDs().size()>0 && fighters.size() > 0)
					{
						for(Fighter fighter : fighters )
						{
							if(subskillfighter.getFighterIDs().contains(fighter.getFighterId()))
							{
								tmpFighters.remove(fighter);
							}	
						}
					}
				}
			}
			break;
		default:
			for(Fighter fighter : fighters )
			{
				tmpFighters.add(fighter);
			}
			break;
		}
		sortFightersByAttrName(tmpFighters, aimfighter, subskill.getSortParam());
		if(subskill.getBuffUnits() != null&&subskill.getBuffUnits().length > 0)
			sortFightersByBuffId(tmpFighters,aimfighter, subskill.getBuffUnits()[0].buffIndex);
		return tmpFighters;
	}
	
	protected List<Fighter>  FilterFightersByMonsterID(List<Fighter> fighters, int[] monsterids)
	{
		List<Fighter> racefighters = new LinkedList<Fighter>();
		if(monsterids != null && monsterids.length > 0)
		{
			for(int monsterid: monsterids)
			{
				for(Fighter fighter: fighters)
				{
					if(fighter.getFightertype() > xbean.Fighter.FIGHTER_PET && fighter.getFighterBean().getUniqueid() == monsterid)
						racefighters.add(fighter);
				}
			}
		}
		else
		{
			return fighters;
		}
		return racefighters;
	}
	
	protected List<Fighter>  FilterFightersByRaceType(List<Fighter> fighters, int[] racetypes)
	{
		List<Fighter> racefighters = new LinkedList<Fighter>();
		if(racetypes != null && racetypes.length > 0)
		{
			for(int racetype: racetypes)
			{
				for(Fighter fighter: fighters)
				{
					if(fighter.getFightertype() == racetype)
						racefighters.add(fighter);
				}
			}
		}
		else
		{
			return fighters;
		}
		return racefighters;
	}
	
	protected List<Fighter>  FilterFightersByRelationType(List<Fighter> fighters, int[] relationtypes)
	{
		List<Fighter> relationfighters = new LinkedList<Fighter>();
		if(relationtypes != null && relationtypes.length > 0)
		{
			for(int relationtype: relationtypes)
			{
				if(relationtype == 4 )
				{
					if(opfighter.isRole())
					{
						Fighter fighter = battle.getFighterobjects().get(opfighter.getFighterId()+5);
						if(fighter!=null&&fighters.contains(fighter))
							relationfighters.add(fighter);
					}
					else if(opfighter.isPet())
					{
						Fighter fighter = battle.getFighterobjects().get(opfighter.getFighterId()-5);
						if(fighter!=null&&fighters.contains(fighter))
							relationfighters.add(fighter);
					}
				}
				else
				{
					int relation = BattleAimRelation.COUPLE * (int) (Math.pow(2, relationtype-1)); 
					for(Fighter fighter: fighters)
					{
						int aimid = fighter.getFighterId();
						if(Math.abs(aimid-opfighter.getFighterId()) < 14)
						{
							int relation2 = BattleField.getAimRelation(battle, opfighter.getFighterId(), aimid);
							ParseObjIDInBattle objinbattle = new ParseObjIDInBattle(relation);
							if (objinbattle.validTarget(relation2))
							{
								relationfighters.add(fighter);
							}
						}
					}
				}
			}
		}
		else
		{
			return fighters;
		}		
		return relationfighters;
	}	
	
	protected List<Fighter> getFightersByCampType(int camptype)
	{
		List<Fighter> fighters = new LinkedList<Fighter>();
		switch (camptype)
		{
		case SkillConstant.TARGET_AIM_CAMPTYPE_FRIEND:// 原目标不合法时则无效，不能换目标
			java.util.List<Integer> retList = BattleField.getFriends(battle, operator);
			for (int friend : retList)
			{
				Fighter fighter = battle.getFighterobjects().get(friend);
				if (fighter != null)
					fighters.add(fighter);
			}
			break;
		case SkillConstant.TARGET_AIM_CAMPTYPE_ALLFRIENDBUTSELF:
			{
				java.util.List<Integer> retListbutself = BattleField.getFriends(battle, operator);
				for (int friend : retListbutself)
				{
					Fighter fighter = battle.getFighterobjects().get(friend);
					if (fighter != null&&fighter.getFighterId() != operator)
						fighters.add(fighter);
				}
			}
			break;
		case SkillConstant.TARGET_AIM_CAMPTYPE_ENEMY:
		{
			java.util.List<Integer> retListenemy = BattleField.getEnemies(battle, operator);
			for (int enemy : retListenemy)
			{
				Fighter fighter = battle.getFighterobjects().get(enemy);
				if (fighter != null)
					fighters.add(fighter);
			}
		}
		break;
		case SkillConstant.TARGET_AIM_CAMPTYPE_SELF:
			fighters.add(battle.getFighterobjects().get(operator));
			break;
		case SkillConstant.TARGET_AIM_CAMPTYPE_ALL:
			for (Map.Entry<Integer, xbean.Fighter> f : battle.getFighters().entrySet())
			{
				fighters.add(battle.getFighterobjects().get(f.getKey()));
			}
			break;
		}
		return fighters;		
	}

	protected List<Fighter> getSubSkillAimFighters(SubSkillConfig subskill)
	{
		List<Fighter> aimfigters = new ArrayList<Fighter>();
		if(subskill == null)
			return aimfigters;
		List<Fighter> fighters = getPossibleSortedFighters(subskill);

		int num = 0;
		int targetCount = subskill.getTargetCountJs().eval(battle.getEngine(),opfighter,null).intValue();
		boolean bneedalive = false;

		switch (subskill.getTargettype())
		{// 其他情况，上一轮在函数getPossibleSortedFighters中已经过滤了
		case SkillConstant.TARGET_AIM_CONFIRM_NORMAL:// 常规索敌(单体则依赖于客户端给予的目标选择，如果目标死亡或隐身，随机选择目标；
			//多体则依赖于客户端给予的目标，多余的目标，根据排序变量，挑选符合的目标，多体目标死亡或隐身，则随机选择主目标，其他目标根据排序变量，挑选符合的目标。）
			{
				if (checkAimFighter(aimfighter, subskill.getBuffIDs(), true, true))
				{
					aimfigters.add(aimfighter);
					num++;
					if (num >= targetCount)
						return aimfigters;
					if(fighters.contains(aimfighter))
					{
						fighters.remove(aimfighter);
					}
				}
			}
			break;
		case SkillConstant.TARGET_AIM_CONFIRM_NORMAL2:// 常规索敌2(则依赖于客户端给予的目标选择，如果目标死亡或隐身，则释放失败）
			{
				if (!checkAimFighter(aimfighter, subskill.getBuffIDs(), true, true))
					return aimfigters;
			}
			break;
		case SkillConstant.TARGET_AIM_CONFIRM_NORMAL3:// 常规索敌3(单体则依赖于客户端给予的目标选择，如果目标死亡，随机选择目标；
			//多体则依赖于客户端给予的目标，多余的目标，根据排序变量，挑选符合的目标，多体目标死亡，则随机选择主目标，其他目标根据排序变量，挑选符合的目标。）
			{
				boolean ok = selectTarget(subskill, targetCount, fighters, aimfigters, 1);
				if (ok == true)
				{
					return aimfigters;
				}	
			}
			break;
		case SkillConstant.TARGET_AIM_CONFIRM_NORMAL4:// 常规索敌4(则依赖于客户端给予的目标选择，如果目标死亡，则释放失败）
			{
				if (!checkAimFighter(aimfighter, subskill.getBuffIDs(), true, false))
					return aimfigters;
			}
			break;
		case SkillConstant.TARGET_AIM_CONFIRM_NORMAL5:// 死亡才能索敌成功，敌方索隐身目标换目标
			{
				if (checkDeadAimFighter(aimfighter, subskill.getBuffIDs(), true))
				{
					aimfigters.add(aimfighter);
					num++;
					if (num >= targetCount)
						return aimfigters;
					if(fighters.contains(aimfighter))
					{
						fighters.remove(aimfighter);
					}
				}
				//bneedalive = true;
			}
			break;
		case SkillConstant.TARGET_AIM_CONFIRM_NORMAL6:// 死亡才能索敌成功，敌方索隐身目标失败 
			{
				if (!checkDeadAimFighter(aimfighter, subskill.getBuffIDs(), true))
					return aimfigters;
				bneedalive = true;
			}
			break;
		case SkillConstant.TARGET_AIM_CONFIRM_NORMAL7:// 图腾索敌方式，自己周围9宫格 
			{
				List<Integer> poss = fire.pb.battle.Module.getInstance().getEffectPoss(
						FormationConfig.GetPosbyFighteid(operator<15? battle.getHostform():battle.getGuestform(),operator));
				poss = FormationConfig.GetPosbyFighteids(operator<15? battle.getHostform():battle.getGuestform(),poss,operator<15);
				if(poss == null)
					return aimfigters;
				for (int pos: poss)
				{
					Fighter fighter = battle.getFighterobjects().get(pos);
					if (fighter != null)
					{
						aimfigters.add(fighter);
						num++;
					}
				}
				return aimfigters;			
			}
			
		case SkillConstant.TARGET_AIM_CONFIRM_NORMAL8: // 上上个目标锁敌人 by changhao 
			{
				boolean ok = selectTarget(subskill, targetCount, fighters, aimfigters, 2);
				if (ok == true)
				{
					return aimfigters;
				}
			}
			break;
		}
		for (Fighter fighter : fighters)
		{
			if(bneedalive == true)
			{
				if (checkDeadAimFighter(fighter, subskill.getBuffIDs(),true))
				{
					aimfigters.add(fighter);
					num++;
					if (num >= targetCount)
						break;
				}				
			}
			else
			{
				if (checkAimFighter(fighter, subskill.getBuffIDs(),true, true))
				{
					aimfigters.add(fighter);
					num++;
					if (num >= targetCount)
						break;
				}
			}
		}
		return aimfigters;
	}
	
	/***
	 * 选择主目标， by changhao
	 * @return
	 */
	private boolean selectTarget(SubSkillConfig subSkill, int targetCount, List<Fighter> fighters, List<Fighter> aimfigters, int sub)
	{
		int num = 0;
		if (checkAimFighter(aimfighter, subSkill.getBuffIDs(), true, false))
		{
			aimfigters.add(aimfighter);
			num++;
			
			if (num >= targetCount)
			{
				return true;
			}
			
			if(fighters.contains(aimfighter))
			{
				fighters.remove(aimfighter);
			}
		}
		else
		{
			if(subSkillindex < skillConfig.getSubSkills().length)
			{
				if (subSkillindex - sub >= 0)
				{
					SubSkillFighterList lastsubskillfighter = subSkillFighters[subSkillindex - sub];
					if(lastsubskillfighter != null)
					{
						for(Fighter fighter : fighters )
						{
							if(!lastsubskillfighter.getFighterIDs().contains(fighter.getFighterId()))
							{
								continue;
							}
							
							if (checkAimFighter(fighter, subSkill.getBuffIDs(), true, false))
							{
								aimfigters.add(fighter);
								num++;
								
								if (num >= targetCount)
								{
									return true;
								}
								
								if(fighters.contains(fighter))
								{
									fighters.remove(fighter);
								}
							}
						}
					}					
				}
			}
		}		

		return false;
	}

	private void sortFightersByBuffId(List<Fighter> fighters, Fighter aimfighter, int buffId)
	{
		if(!fire.pb.buff.Module.isConstantlyBuff(buffId))
			return;
		if(fighters.isEmpty())
			return;
		List<Fighter> removefighters = new LinkedList<Fighter>();
		for(Iterator<Fighter> it = fighters.iterator();it.hasNext();)
		{
			Fighter fighter = it.next();
			if(aimfighter!= null && fighter.getFighterId() == aimfighter.getFighterId())
				continue;
			if(fighter.getBuffAgent().existBuff(buffId))
			{
				it.remove();
				removefighters.add(fighter);
			}
		}
		
		for(Fighter fighter : removefighters)
		{
			fighters.add(fighter);
		}
	}

	protected boolean checkAimFighter(Fighter fighter, List<Integer> buffIds, boolean examdead, boolean examhide)
	{
		if(buffIds.size() < 1)
			return false;
		if (!battle.getFighters().containsKey(fighter.getFighterId()))
			return false;
		if (BattleField.checkOutBattle(battle, fighter.getFighterId()))
			return false;
		if(examdead || !deadfighterIds.contains(fighter.getFighterId()))
		{
			if (fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH))				
				if (!buffIds.contains(BuffConstant.INSTANT_RELIVE) && !buffIds.contains(BuffConstant.INSTANT_ITEM_RELIVE)&&!buffIds.contains(BuffConstant.INSTANT_SUMMON_BACK) )
					return false;
			if (fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST))
				return false;
		}
		if (!fighter.canBeAction(getType()))
			return false;
		/*if ( aimfighter != null && fighter != aimfighter && subSkillindex == 0)
		{
			if (fighter.getBuffAgent().checkBuffClash(buffIds.get(0)) > 0)
				return false;
		}*/
		if(examhide&& skillConfig.getIgnoreHide() != 1)
		{
			if (!checkHidden(fighter))
				return false;
		}
		return true;
	}

	protected boolean checkDeadAimFighter(Fighter fighter, List<Integer> buffIds, boolean examhide)
	{
		if(buffIds.size() < 1)
			return false;
		if (!battle.getFighters().containsKey(fighter.getFighterId()))
			return false;
		if (BattleField.checkOutBattle(battle, fighter.getFighterId()))
			return false;
		if (!(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST)))
			return false;
		if (!fighter.canBeAction(getType()))
			return false;
		if (fighter.getBuffAgent().checkBuffClash(buffIds.get(0)) > 0)
			return false;
		if(examhide)
		{
			if (!checkHidden(fighter))
				return false;
		}
		return true;
	}
	
	//隐身校验
	protected boolean checkHidden(Fighter fighter)
	{
		if (fighter == null)
			return false;
		if (BattleField.isFriend(battle, operator, fighter.getFighterId()))
			return true;//如果动作者和目标是友方，则隐身无效
		// 如果是辅助或治疗类技能则不用检查隐身
		if (!fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_HIDDEN_FIRST)
				&& !fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_HIDDEN_SECOND)
				&& !fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ROLE_HIDDEN))
			return true;// 目标没有隐身
		if (opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN_FIRST)||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN_SECOND)||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN))
			return true;// 动作者有反隐
		return false;
	}

	private boolean checkOperator()
	{
		if (!battle.getFighters().containsKey(getOperator()))
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("战场中 找不到figterid为:" + getOperator() + " 的战斗者");}
			return false;
		}
		if (BattleField.checkOutBattle(battle, operator))
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("战场中 figterid为:" + getOperator() + " 的战斗者已经出战斗!");}
			return false;
		}
		if (opfighter == null)
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("技能执行者对象空了!");}
			return false;
		}
		if (!opfighter.canAction(getType(), getSkillId()))
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("技能执行者状态不对，或是中限制该操作的buff了!");}
			failReason = BasicOperation.FAIL_CANNOT_ACTION;
			return false;
		}
		if(battle.getBattletype()% 10 == 1 && skillConfig.getIsOnlyPve())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("此技能只能在pve战场执行!");}
			failReason = BasicOperation.FAIL_CANNOT_ACTION;
			return false;
		}
		return true;
	}

	protected int getTargetType()
	{
		return targetType;
	}

	protected boolean checkRelation()
	{
		if (aim < 0 || aim > BattleField.A_SIDE_FIGHTER_NUM * 2)
			return false;
		int relation = BattleField.getAimRelation(battle, operator, aim);
		ParseObjIDInBattle objinbattle = new ParseObjIDInBattle(getTargetType());
		if (!objinbattle.validTarget(relation))
			return false;

		return true;
	}

	// 检查血蓝要求
	protected boolean checkHpMpRequire(){return true;}

	// 消耗检查
	protected boolean checkHpMpConsume()
	{
		if (-getHPconsume() >= opfighter.getEffectRole().getHp())
		{
			failReason = BasicOperation.FAIL_HP_NOT_ENOUGH;
			return false;
		}
		if (-getMPconsume() > opfighter.getEffectRole().getMp())
		{
			if (opfighter.getFightertype() == xbean.Fighter.FIGHTER_PET)
				failReason = BasicOperation.FAIL_PET_MP_NOT_ENOUGH;
			else
				failReason = BasicOperation.FAIL_MP_NOT_ENOUGH;
			return false;
		}
		if (-getSPconsume() > opfighter.getEffectRole().getSp())
		{
			failReason = BasicOperation.FAIL_SP_NOT_ENOUGH;
			return false;
		}
		if (-getEPconsume() > opfighter.getFighterBean().getInitattrs().get(AttrType.EFFECT_POINT))
		{
			failReason = BasicOperation.FAIL_EP_NOT_ENOUGH;
			return false;
		}
		
		return true;
	}

	protected boolean checksBuffStateRequire(BuffUnit buffarg)
	{
		return buffarg.confilictStateJavascript.evalToBoolean(battle.getEngine(),opfighter,null);
	}
	
	protected boolean checkBuffClashAndDepend(BuffUnit buffarg, Fighter fighter)
	{
		if (buffarg.requiredBuffIds != null)
		{
			for (int reqBuffId : buffarg.requiredBuffIds)
			{
				if(reqBuffId >1000)
				{
					if (!fighter.getBuffAgent().existBuff(reqBuffId))
					{
						failReason = BasicOperation.FAIL_ZITAI;
						return false;
					}
				}
				else
				{
					if (!fighter.getBuffAgent().existBuffByType(reqBuffId))
					{
						failReason = BasicOperation.FAIL_ZITAI;
						return false;
					}
					
				}
			}
		}
		if (buffarg.confilictBuffIds != null)
		{
			for (int conBuffId : buffarg.confilictBuffIds)
			{
				if(conBuffId >1000)
				{
					if (fighter.getBuffAgent().existBuff(conBuffId))
						return false;
				}
				else
				{
					if (fighter.getBuffAgent().existBuffByType(conBuffId))
						return false;
				}
			}
		}
		return true;
	}

	protected boolean checksBuffodds(BuffUnit buffarg)
	{
		double v = buffarg.oddsJavascript.eval(battle.getEngine(),opfighter,null);
		return v > Math.random() ? true : false;
	}
	
	protected boolean checksStateRequire(SubSkillConfig subskill)
	{
		if(!subskill.getConfilictStateJs().evalToBoolean(battle.getEngine(),opfighter,null))
		{
			if(subskill.getConfilictStatesnotifyId() > 0)
			{
				item.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
				item.execute.msgid = subskill.getConfilictStatesnotifyId();
			}
			return false;
		}
		return true;
	}

	protected boolean checkClashAndDepend(SubSkillConfig subskill)
	{
		if (subskill.getRequiredBuffIndexs() != null)
		{
			for (int reqBuffId : subskill.getRequiredBuffIndexs())
			{
				if(reqBuffId > 1000)
				{
					if (!opfighter.getBuffAgent().existBuff(reqBuffId))
					{
						failReason = BasicOperation.FAIL_ZITAI;
						return false;
					}
				}
				else
				{
					if (!opfighter.getBuffAgent().existBuffByType(reqBuffId))
					{
						failReason = BasicOperation.FAIL_ZITAI;
						return false;
					}
				}
			}
		}
		if (subskill.getConfilictBuffIndexs() != null)
		{
			for (int conBuffId : subskill.getConfilictBuffIndexs())
			{
				if(conBuffId > 1000)
				{
					if (opfighter.getBuffAgent().existBuff(conBuffId))
					{
						item.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
						item.execute.msgid = subskill.getConfilictStatesnotifyId();
						return false;
					}
				}
				else
				{
					if (opfighter.getBuffAgent().existBuffByType(conBuffId))
					{
						item.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
						item.execute.msgid = subskill.getConfilictStatesnotifyId();
						return false;
					}
				}
			}
		}
		return true;
	}


	protected boolean checkMainBuffTarget()
	{
		if(skillConfig.getSubSkills().length == 0)
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("没有主技能，技能ID为：" + skillId);}
			return false;
		}
		
		if(skillConfig.getSubSkills()[0].getGoalCampType() == 6 && BattleField.getFriendEmptyPos(battle,operator)!=0)
		{
			return true;			
		}	
		else if(skillConfig.getSubSkills()[0].getGoalCampType() == 7 && BattleField.getEnemyEmptyPos(battle,operator)!=0)
		{
			return true;			
		}	
		else
		{
			this.mainAimFighters = getSubSkillAimFighters(skillConfig.getSubSkills()[0]);
			if (mainAimFighters.size() != 0)
			{
				if(!mainAimFighters.contains(aimfighter))
				{
/*					if(opfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_ROLE && !isauto)
					{
						failReason = getFailAimMsg();
						return false;
					}	*/
					aimfighter = mainAimFighters.get(0);
					aim = aimfighter.getFighterId();
				}
				battle.getEngine().setSkillTargetNum(mainAimFighters.size());
				return true;
			} else
			{
				failReason = getFailAimMsg();
				return false;
			}
		}
	}
	
	protected int getFailAimMsg()
	{
		return BasicOperation.FAIL_SKILL_AIM;
	}

	protected List<Fighter> sortFightersByAttrName(final List<Fighter> fighters, Fighter aimfighter, String sortAttr)
	{

		if (fighters.size() == 0)
			return fighters;
		FighterSorter.sortFightersByAttrName(fighters, sortAttr);			
		if ((sortAttr!=null||subSkillindex == 0)&&aimfighter != null && fighters.remove(aimfighter))
			fighters.add(0, aimfighter);
		return fighters;
	}

	//命中检查
	protected Double getHitRate(JavaScript hitScript,Fighter fighter)
	{
		Double result = hitScript.eval(battle.getEngine(),opfighter,fighter);
		if (result == null)
			return 1.0d;
		return result;
	}
	
	//命中检查
	protected boolean isHit(Double hitRate, Fighter curAim, int buffId)
	{
		double rand = Math.random();
		boolean ishit = (hitRate >= rand);
		sendHitRateForDebug(hitRate, curAim, buffId, ishit);
		return ishit;
	}

	protected void sendHitRateForDebug(double result, Fighter curAim, int buffId, boolean ishit)
	{
		Set<Long> roleIds = new HashSet<Long>();
		roleIds.addAll(battle.getRoleids().keySet());
		for (Iterator<Long> it = roleIds.iterator(); it.hasNext();)
		{
			long roleId = it.next();
			xbean.DebugFlag debugflag = xtable.Debugflag.get(roleId);
			if (debugflag == null || !debugflag.getDebugs().contains(xbean.DebugFlag.BATTLE))
				it.remove();
		}
		if(roleIds.size() > 0)
		{
			Double rate = result * 10000;
			rate = (double) rate.intValue();
			if (BattleField.logger.isDebugEnabled()) {
				String info = "动作者ID     " + getOperator() + "    释放技能：        " + getSkillId() + "    目标：       " + curAim.getFighterId()
				+ "    BUFF ID：     " + buffId + "    BUFF命中率为:    " + (rate / 10000) + "    命中？" + ishit;
				BattleField.logger.debug(info);
				final STransChatMessage2Client snd = new STransChatMessage2Client();
				snd.messagetype = ChannelType.CHANNEL_SYSTEM;
				snd.message = info;
				mkdb.Procedure.psend(roleIds, snd);
			}
		}
	}

	//在JSEngine参数注入
	protected void setJSEngineArgs()
	{
		battle.getEngine().setOpFighter(opfighter, skillConfig.getUsedAttrTypea());
		battle.getEngine().setSkillLevel(getLevel());
		battle.getEngine().setMainDamage(0);
		battle.getEngine().setSurvival(
				BattleField.getLiveFriends(battle, opfighter.getFighterId()).size(),
				BattleField.getLiveFriendNoPets(battle, opfighter.getFighterId()).size(),
				BattleField.getLiveEnemys(battle, opfighter.getFighterId()).size(),
				BattleField.getLiveEnemyNoPets(battle, opfighter.getFighterId()).size());
		
	}
	
	protected void setResultItemExecuteAndConsume()
	{
		item.execute.attackerid = getOperator();
		if (!opfighter.isDoubleSkill())
		{
			item.execute.hpconsume = getHPconsume();
			opfighter.attachHpChange(item.execute.hpconsume);
	
			// 先判断专注
			boolean cancelmp = false;
			if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.BATTLE_EQUIP_COOL_HEART_SKILL)&& Math.random() < 0.15f)
				cancelmp = true;
			if(cancelmp == false)
			{
				item.execute.mpconsume = getMPconsume();
				opfighter.getEffectRole().addMp(item.execute.mpconsume);
			}
	
			int nEp = getEPconsume();
			if(nEp != 0)
			{
				opfighter.getFighterBean().getInitattrs().put(AttrType.EFFECT_POINT, opfighter.getFighterBean().getInitattrs().get(AttrType.EFFECT_POINT)+nEp);				
			}
			
			item.execute.spconsume = getSPconsume();
			int spconsume = opfighter.getEffectRole().addSp(item.execute.spconsume, 0);			
			if (spconsume != 0)// sp变化放入属性变化中
				opfighter.getRoundBuffResult().getChangedAttrs().put(AttrType.SP, (float) opfighter.getEffectRole().getSp());
		}
		item.execute.operationtype = getType();
		if(item.execute.operationtype == OperationType.ACTION_SPECIAL_SKILL||item.execute.operationtype == OperationType.ACTION_UNIQUE_SKILL)
			item.execute.operationtype = OperationType.ACTION_SKILL;//为了客户端识别，特技也变为技能
		item.execute.operationid = getSkillId();
	}
	
	protected SubResultItem GetSubResultItem(ResultItem item, int subskillid)
	{
		SubResultItem subItem = null;
		if(item.subresultlist.size() > 0)
		{
			for(SubResultItem subItem2 : item.subresultlist)
			{
				if(subItem2.subskillid == subskillid)
					return subItem2;
			}
		}
		return subItem;		
	}
	// 特殊被动技能处理	
	public void processSpecialPassiveSkillBeforeSkill()
	{
		if(opfighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE||(SkillConstant.BATTLE_SKILL_BASIC_ATTACK != skillId && SkillConstant.BATTLE_SKILL_FAST_ATTACK != skillId &&
				SkillConstant.BATTLE_SKILL_BASIC_CTRL_ATTACK != skillId && SkillConstant.BATTLE_SKILL_BASIC_CHAOS_ATTACK != skillId))
		{
			if (opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_HIDDEN_FIRST)
					||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_HIDDEN_SECOND)
					||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ROLE_HIDDEN))
				iswithhidebuffeffect = true;
		}
/*		if(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_FULL_POWER_SKILL))//血脉愤张技能	
		{
			if(opfighter.getEffectRole().getHp() == opfighter.getEffectRole().getMaxHp())
				battle.getEngine().put("attacka", opfighter.getEffectRole().getAttack()battle.getEngine().getDouble("attacka").intValue() + skillLevel*2);//十字军之心
		}
		if(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_CRUSADES_HEART_SKILL))
		{
			int addvalue = 105-((opfighter.getEffectRole().getHp()*100)/opfighter.getEffectRole().getMaxHp());
			battle.getEngine().put("sealhita", opfighter.getEffectRole().getAttrById(FightAttrType.SEAL) + addvalue);		
		}
*/		
		return;
	}	
	
	// 特殊被动技能处理
	public void processSpecialPassiveSkillBeforeBuff(Fighter fighter)
	{
/*		if(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_FIND_WEAK_SKILL))//洞悉弱点
		{
			int defb = aimfighter.getEffectRole().getDefend(); 
			int magicdefb = aimfighter.getEffectRole().getMagicDef();
			if(defb>magicdefb)
			{
				battle.getEngine().put("defendb", magicdefb);		
			}
			else
			{
				battle.getEngine().put("magicdefb", defb);		
			}
		}
		if(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_B52_SKILL))//狂轰乱炸
		{
			int speeda = battle.getEngine().getDouble("speeda").intValue();
			int speedb = battle.getEngine().getDouble("speedb").intValue();
			if(speeda > speedb)
			{
				battle.getEngine().put("attacka", battle.getEngine().getDouble("attacka").intValue() + speeda - speedb);				
			}
		}*/
		return;
	}	
	
	// 特殊被动技能处理
	public List<DemoResult> processSpecialPassiveSkillAfterBuff(Fighter fighter, int resulttype)
	{
		if(iswithhidebuffeffect == true)
			iswithhidebuffeffect = false;

		List<DemoResult> demos = new ArrayList<DemoResult>();
/*	
		if(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_SCARE_HOWL_SKILL)&& resulttype < 2)//破胆怒吼
		{
			ConstantlyBuff scarehowlbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_SCARE_HOWL);
			scarehowlbuff.setRound(2);
			Map<Integer,JavaScript> neweffects = new HashMap<Integer, JavaScript>();
			neweffects.put(EffectType.DAMAGE_ABL,  new JavaScript("-1.5*skilllevela"));
			DemoResult scarehowldemo = scarehowlbuff.impact(battle, opfighter, fighter, null, neweffects);
			demos.add(scarehowldemo);
		}
		if(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_PEEL_ARMOUR_SKILL)&& resulttype < 2)//卸甲
		{
			ConstantlyBuff scarehowlbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_PEEL_ARMOUR);
			scarehowlbuff.setRound(1);
			Map<Integer,JavaScript> neweffects = new HashMap<Integer, JavaScript>();
			neweffects.put(EffectType.DEFEND_ABL,  new JavaScript("-1.5*skilllevela")); 
			DemoResult scarehowldemo = scarehowlbuff.impact(battle, opfighter, fighter, null, neweffects);
			demos.add(scarehowldemo);
		}		*/
		if(fighter.isPartner()&&fighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_MYSTERY_FORBID_SKILL) 
				&& !(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH)) 
				&& Math.random() < PropConf.Battle.MYSTERY_FORBID_RATE)
		{//奥术禁锢
/*			ConstantlyBuff frozenmanabuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_FROZEN_MANA);
			frozenmanabuff.setRound(2);
			DemoResult frozenmanademo = frozenmanabuff.impact(battle, fighter,opfighter,  null, null);*/
			ConstantlyBuff frozenbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_FROZEN);
			if(frozenbuff != null)
			{	
				frozenbuff.setRound(2);
				DemoResult frozendemo = frozenbuff.impact(battle, fighter, opfighter, null, null);
				//调整结果集为反击
				frozendemo.targetid = fighter.getFighterId();
				frozendemo.resulttype =1;
				//frozenmanademo.demobuffs.addAll(frozendemo.demobuffs);
				demos.add(frozendemo);
			}
		}
		return demos;
	}
	// 特殊被动技能处理
	public List<DemoResult> processSpecialPassiveSkillAfterSkill()
	{
		if(iswithhidebuffeffect == true)
			iswithhidebuffeffect = false;

		if (opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS)||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
			return null;

		List<DemoResult> demos = new ArrayList<DemoResult>();
			
		if(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_HADES_FORBID_SKILL))//魔魂禁锢
		{
			boolean bhaveother= false;
			java.util.List<Integer> retList = BattleField.getFriends(battle, operator);
			for (int friend : retList)
			//for (int friend : BattleField.getFriends(battle, operator))
			{
				Fighter friendfighter = battle.getFighterobjects().get(friend);
				if (friendfighter != null && friendfighter != opfighter && friendfighter.isPartner()&&friendfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_HADES_FORBID_SKILL))
				{
					bhaveother= true;
					break;
				}
			}
			if(bhaveother == true)
			{
				ConstantlyBuff frozenmanabuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_DIE_FORBID);
				if(frozenmanabuff != null)
				{
					frozenmanabuff.setRound(2);
					int fighterid = BattleField.getRandTargetByType(battle, opfighter.getFighterId(),480,false,true, null);
					if(fighterid > 0)
					{
						Fighter aim = battle.getFighterobjects().get(fighterid);
						DemoResult frozenmanademo = frozenmanabuff.impact(battle, opfighter, aim,  null, null);
						demos.add(frozenmanademo);
					}
				}
			}
		}
		if(demos.size()>0)
			return demos;
/*		if(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_ICE_DEFEND_SKILL))//寒冰护体
		{	
			DemoResult demo = new DemoResult();
			List<Integer> dispelBuffId = new ArrayList<Integer>();
			Result result = new Result(false);
			Fighter fighter = null;
			java.util.List<Integer> retList = BattleField.getFriends(battle, operator);
			for (int friend : retList)
			{
				fighter = battle.getFighterobjects().get(friend);
				if (fighter != null)
				{
					for(int buffId : opfighter.getBuffAgent().getAllBuffBeans().keySet())
					{
						int bufftype = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId).getBuffType();
						if(bufftype/10 ==BuffConstant.TYPE_FATHER_CONTINUAL_DOT)
						{
							dispelBuffId.add(buffId);
							break;
						}						
					}
				}
				if(dispelBuffId.size() > 0)
					break;
				fighter = null;
			}
			if(fighter!=null)
			{
				for(int buffId : dispelBuffId)
				{
					result.updateResult(fighter.getBuffAgent().removeCBuff(buffId));
				}
			}
			if(result.isSuccess())
			{	
				//转换
				fire.pb.buff.Module.updateDemoResultFromResult(demo, result,fighter);
				demos.add(demo);
			}
		}*/
		return null;
	}

	public DemoResult processSummonBuff(BuffUnit buffarg, boolean operatorpos)
	{
		if (buffarg == null)
			return null;
		if (buffarg.effectJavascriptMap == null)
			return null;
		JavaScript monsteridscript = buffarg.effectJavascriptMap.get(EffectType.AIM);//
		if(monsteridscript == null)
			return null;
		int monsterid = monsteridscript.eval(battle.getEngine(),opfighter,null).intValue();
		if(monsterid < 1)
			return null;

		List<FighterInfo> newfighters = new LinkedList<FighterInfo>();
		boolean ishost = BattleField.isHost(operator);
		//验证该fighterId是否已经有战斗者占用
		if(operatorpos)
			aim = operator;
		else
		{
			if(aim == 0||aim == operator)
				aim = BattleField.getAvailableFighterId(getBattle(), operator, ishost);
			else
				aim = BattleField.getAvailableFighterId(getBattle(), aim, ishost);
		}
		if(aim!=0)
		{
			int form = (ishost)?getBattle().getHostform():getBattle().getGuestform();
			int formLevel = ishost ? getBattle().getHostformlevel() : getBattle().getGuestformlevel();
			//有空闲位置，可以添加
			if(PNewBattle.enterBattleMonster(getBattle(), new Monster(monsterid, skillLevel),aim,ishost,form,formLevel))
			{
				newfighters.add(BattleField.getFighterInfo(getBattle().getFighters().get(aim), getBattle()));
			}
		}
		else
			return null;
		
		item.newfighter.addAll(newfighters);		
		final DemoResult r = new DemoResult();
		r.targetid = aim;		
		r.targetresult = ResultType.RESULT_SUMMONPET;
		return r;
	}
	
	//召唤处理
	public ResultItem processSummon()
	{
		item = new ResultItem();
		//需求校验
		if (!canCastSummon())
		{
			if(failReason > 0)
			{
				item.execute.attackerid = getOperator();
				item.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
				item.execute.msgid = failReason;
				SubResultItem subitem = new SubResultItem();
				subitem.subskillid = 0;
				subitem.subskillstarttime = 0;
				item.subresultlist.add(subitem);
				return item;
			}
			else
				return null;
		}		
		
		if (opfighter.isDoubleSkill())
		{
			return null;
		}
		// 扣消耗
		setResultItemExecuteAndConsume();
		
		SubSkillConfig subSkill = skillConfig.getSubSkills()[0];
		if(subSkill==null)
			return null;
		SubResultItem subitem = new SubResultItem();
		subitem.subskillid = subSkill.getSubSkillId();
		if(subSkillindex == 0)
			subitem.subskillstarttime = 0;
		else
			subitem.subskillstarttime = subSkill.getSkillBeginTime();
		
		DemoResult result = null;
		boolean operatorpos = false;
		for (int index = 0; index < subSkill.getBuffUnits().length; index++)
		{
			BuffUnit buffarg = subSkill.getBuffUnits()[index];
			if(buffarg == null)
				continue;
			if(buffarg.buffIndex == BuffConstant.INSTANT_SUMMON_FIGHTER)
			{
				result = processSummonBuff(buffarg, operatorpos);
				if(result == null&& operatorpos == false)
				{
					item.execute.msgid = BasicOperation.FAIL_SOMMON_NO_POS;
					item.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
				}
				else if(result != null )
				{
					if(SkillConstant.BATTLE_ROLE_SUMMON_TOTEM_HEAL==skillId || SkillConstant.BATTLE_ROLE_SUMMON_TOTEM_CRAZE==skillId)
					{	
						Fighter newfighter = battle.getFighterobjects().get(result.targetid);
						long monsterid = newfighter.getFighterBean().getUniqueid();
						for (int friend : BattleField.getFriends(battle, operator))
						{
							Fighter oldfighter = battle.getFighterobjects().get(friend);
							if(oldfighter.getFighterBean().getUniqueid() == monsterid && oldfighter.getOwner() == operator)
							{
								IBuff buff = fire.pb.buff.Module.getInstance().createBuff(BuffConstant.INSTANT_SUMMON_BACK);
								DemoResult summombackresult = buff.impact(battle, opfighter, oldfighter, this, null);
								BattleField.removeDeathFighterHaloBuffs(battle, summombackresult, friend);
								subitem.resultlist.add(summombackresult);
								int aimid = oldfighter.getFighterId();
								battle.getFighters().remove(aimid); 
								battle.getFighterobjects().remove(aimid); 
							}
						}
						if(SkillConstant.BATTLE_ROLE_SUMMON_TOTEM_HEAL==skillId)
						{	
							newfighter.getEffectRole().attachEffect(EffectType.MEDICAL_ABL,opfighter.getEffectRole().getAttrById(FightAttrType.MEDICAL)-newfighter.getEffectRole().getAttrById(FightAttrType.MEDICAL));
							newfighter.getEffectRole().attachEffect(FightAttrType.HEAL_EFFECT_RATE + 1,opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_EFFECT_RATE)-newfighter.getEffectRole().getAttrById(FightAttrType.HEAL_EFFECT_RATE));
							newfighter.getEffectRole().attachEffect(FightAttrType.HEAL_DEEP_RATE+1,opfighter.getEffectRole().getAttrById(FightAttrType.HEAL_DEEP_RATE)-newfighter.getEffectRole().getAttrById(FightAttrType.HEAL_DEEP_RATE));
							newfighter.getEffectRole().updateAllFinalAttrs();
						}
						getBattle().getFighterobjects().get(aim).setOwner(operator);
					}
					subitem.resultlist.add(result);
					item.execute.operationtype = OperationType.ACTION_SUMMON;
				}
			}
			else if(buffarg.buffIndex == BuffConstant.INSTANT_SUMMON_BACK)
			{
				operatorpos = true;
				IBuff buff = fire.pb.buff.Module.getInstance().createBuff(buffarg.buffIndex);
				result = buff.impact(battle, opfighter, aimfighter, this, null);
				subitem.resultlist.add(result);
				int aimid = aimfighter.getFighterId();
				battle.getFighters().remove(aimid); 
				battle.getFighterobjects().remove(aimid); 
			}			
		}
		item.subresultlist.add(subitem);
		return item;
	}	

	//为衰减做预先统计目标数量
	public int getaimcount()
	{
		List<Integer> nFighterIDs = new ArrayList<Integer>();

		for (; subSkillindex < skillConfig.getSubSkills().length; subSkillindex++)
		{
			SubSkillConfig subSkill = skillConfig.getSubSkills()[subSkillindex];
	
			// 索敌
			List<Fighter> aimfighters = null;
			if (subSkillindex == 0)
				aimfighters = this.mainAimFighters;
			else
				aimfighters = getSubSkillAimFighters(subSkill);

			if (aimfighters.size() == 0)
				continue;
			
			subSkillFighters[subSkillindex] = new SubSkillFighterList();
			
			if (!canCastSub(subSkill))
			{
				continue;
			}
			
			for (Fighter fighter : aimfighters)
			{
				subSkillFighters[subSkillindex].addFighterID(fighter.getFighterId());
				if(!nFighterIDs.contains(fighter.getFighterId()))
					nFighterIDs.add(fighter.getFighterId());					
			}			
		}
		for(SubSkillFighterList fighterlist:subSkillFighters)
		{
			if(fighterlist!=null)
				fighterlist.getFighterIDs().clear();
		}
		subSkillindex = 0;		
		if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("getaimcount = ："+nFighterIDs.toString());}		
		return nFighterIDs.size();		
	}
	//技能计算处理
	public ResultItem process()
	{
		try
		{
			setJSEngineArgs();
			if(skillConfig.getType() == SkillConstant.BATTLE_SKILL_TYPE_SUMMON)
			{
				setRestState();
				return processSummon();
			}
			item = new ResultItem();

			// 需求校验
			if (!canCast())
			{
				if(failReason > 0)
				{
					item.execute.attackerid = getOperator();
					if(failReason == BasicOperation.FAIL_HP_NOT_ENOUGH ||failReason == BasicOperation.FAIL_MP_NOT_ENOUGH||failReason == BasicOperation.FAIL_PET_MP_NOT_ENOUGH
							||failReason == BasicOperation.FAIL_SP_NOT_ENOUGH||failReason == BasicOperation.FAIL_EP_NOT_ENOUGH ||failReason==0)
						item.execute.operationtype = OperationType.ACTION_FAILURE;
					else
						item.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
					item.execute.msgid = failReason;
					SubResultItem subitem = new SubResultItem();
					subitem.subskillid = 0;
					subitem.subskillstarttime = 0;
					item.subresultlist.add(subitem);
					setRestState();
					return item;
				}
				else
				{
					setRestState();
					return null;
				}
			}
			// 特殊被动技能处理
			processSpecialPassiveSkillBeforeSkill();
			
			// 扣消耗
			setResultItemExecuteAndConsume();

			if(skillConfig.isPreCountAim())
			{
				battle.getEngine().setSkillPreAimCount(getaimcount());
			}
			int useskillremovebuffcount = 0;
			DemoResult useskillremovebuffdemoresult = new DemoResult();
			for(int existbuffId : opfighter.getBuffAgent().getAllBuffBeans().keySet())
			{
				if(type == OperationType.ACTION_USEITEM) break;
				ConstantlyBuffConfig buffcfg = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(existbuffId);
				if(buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_USE_SKILL_ATTACK||
						(buffcfg.getClearType() == BuffConstant.CLEAR_TYPE_USE_SKILL &&( SkillConstant.BATTLE_SKILL_BASIC_ATTACK != skillId && SkillConstant.BATTLE_SKILL_FAST_ATTACK != skillId &&
						SkillConstant.BATTLE_SKILL_BASIC_CTRL_ATTACK != skillId && SkillConstant.BATTLE_SKILL_BASIC_CHAOS_ATTACK != skillId)))
				{
					Result result = opfighter.getBuffAgent().removeCBuff(existbuffId);
					if(result.isSuccess())
					{
						fire.pb.buff.Module.updateDemoResultFromResult(useskillremovebuffdemoresult, result, opfighter);
						useskillremovebuffdemoresult.resulttype = 6;
						useskillremovebuffcount ++;
					}
				}
			}

			// 何种情况下执行本技能,关联技能
			for (; subSkillindex < skillConfig.getSubSkills().length; subSkillindex++)
			{
				SubSkillConfig subSkill = skillConfig.getSubSkills()[subSkillindex];
				
				if(subSkill==null)
					continue;
				
				if(subSkillindex > 0)
				{
					boolean errorcondition = false;
					if(subSkill.getRelevanceSubSkillId() != 0)
					{//关联技能为空则不需要下面操作
						SubResultItem lastsubitem = GetSubResultItem(item, subSkill.getRelevanceSubSkillId());
						if(lastsubitem == null)
							errorcondition = true;
						else
						{
							int[] conditions = subSkill.getActionConditions();
							for(int condition: conditions)
							{
								switch (condition)
								{
								case SkillConstant.USBSKILL_ACT_CONDITION_HIT_ALIVE://关联技能索敌成功，命中，受击方未全部死亡
									{
										if(lastsubitem.resultlist.size()>0)
										{
											for(DemoResult demo: lastsubitem.resultlist)
											{
												if(BattleField.checkLiveInBattle(battle, demo.targetid))
												{
													errorcondition = true;
													break; // 受击者id
												}
											}										
										}	
									}
									break;
								case SkillConstant.USBSKILL_ACT_CONDITION_HIT_ALLDIE:// 关联技能索敌成功，命中，受击方全部死亡	
									{
										if(lastsubitem.resultlist.size()>0)
										{
											for(DemoResult demo: lastsubitem.resultlist) 
											{
												if(!BattleField.checkLiveInBattle(battle, demo.targetid))
												{
													errorcondition = true;
													break; // 受击者id
												}
											}										
										}
									}
									break;
								case SkillConstant.USBSKILL_ACT_CONDITION_MISS:// 关联技能索敌成功，未命中
									{
										if(lastsubitem.resultlist.size()>0)
										{
											for(DemoResult demo: lastsubitem.resultlist)
											{
												if(demo.targetresult == ResultType.RESULT_DODGE)
												{
													errorcondition = true;
													break; // 受击者id
												}
											}										
										}		
									}
									break;
								case SkillConstant.USBSKILL_ACT_CONDITION_NOTFIND:// 关联技能索敌失败
									{
										if(lastsubitem.resultlist.size()<1)
											errorcondition = true;
									}
									break;
								}
								if(errorcondition == true)
									break;
							}
						}
					}
					if(errorcondition == true)
					{
						continue;
					}
				}
				
				// 索敌
				List<Fighter> aimfighters = null;
				if (subSkillindex == 0)
					aimfighters = this.mainAimFighters;
				else
					aimfighters = getSubSkillAimFighters(subSkill);

				subSkillFighters[subSkillindex] = new SubSkillFighterList();

				for (Iterator<Fighter> it = aimfighters.iterator(); it.hasNext();)
				{
					Fighter fighter = it.next();
					float fTargetRate = subSkill.getTargetRate().eval(battle.getEngine(),opfighter, fighter).floatValue();
					if(Math.random() >= fTargetRate)
					{
						it.remove();
						continue;
					}
					subSkillFighters[subSkillindex].addFighterID(fighter.getFighterId());
				}

				if (aimfighters.size() == 0)
					continue;
				
				
				if (!canCastSub(subSkill))
				{
					subSkillFighters[subSkillindex].clearlist();
					continue;
				}

			
				SubResultItem subitem = new SubResultItem();
				subitem.subskillid = subSkill.getSubSkillId();
				if(subSkillindex == 0)
					subitem.subskillstarttime = 0;
				else
					subitem.subskillstarttime = subSkill.getSkillBeginTime();
				// 按buff生成结果集
				for (int index = 0; index < subSkill.getBuffUnits().length; index++)
				{
					BuffUnit buffarg = subSkill.getBuffUnits()[index];
					if (buffarg == null)
						continue;
					
					if(index == 0)
						mainBuff = buffarg.buffIndex;
					
					// 触发率校验
					if (!checksBuffodds(buffarg))
					{
						if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("技能施放buff失败：checksBuffodds，buff触发几率不够。 施法者Id: ").append(operator).append(" ；技能ID：").append(skillId).append(" ；BuffID：").append(buffarg.buffIndex));}
						continue;
					}		
					
					// 重置连击数
					battle.getEngine().setSkillComboCurCount(0);
	
					// create Buff
					IBuff buff = fire.pb.buff.Module.getInstance().createBuff(buffarg.buffIndex);
					for (Fighter fighter : aimfighters)
					{
						// 冲突和依赖校验
						if (!checkBuffClashAndDepend(buffarg, fighter))
						{
							if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("技能施放buff失败：checkBuffClashAndDepend，技能依赖不合法。 施法者Id: ").append(operator).append(" ；受击者ID：").append(fighter.getFighterId()).append(" ；技能ID：").append(skillId).append(" ；BuffID：").append(buffarg.buffIndex));}
							continue;
						}
						
						if(buff instanceof SkillComboInjure)
						{//如果是技能连击，动作者中途死了，则不执行后面的连击
							if(!opfighter.canAction(getType(), getSkillId()))
								break;
						}
	
						battle.getEngine().setAimFighter(fighter, skillConfig.getUsedAttrTypeb());
						
						processSpecialPassiveSkillBeforeBuff(fighter);
						
						// 属性状态检查
						if (!checksBuffStateRequire(buffarg))
						{
							if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("技能施放buff失败：checksBuffStateRequire，属性状态需求不满足。 施法者Id: ").append(operator).append(" ；技能ID：").append(skillId).append(" ；BuffID：").append(buffarg.buffIndex));}
							continue;
						}		
						
						// 复制buff
						IBuff buffcopy = buff.copy();
						Map<Integer, JavaScript> effectscopy = new HashMap<Integer, JavaScript>();
						if (buffarg.effectJavascriptMap != null)
							effectscopy.putAll(buffarg.effectJavascriptMap);
						Double hitRate = getHitRate(buffarg.rateJavascript, fighter);
						
						if(hitRate.intValue() < 0&&!buffarg.rateJavascript.isFun())
						{
							if(subitem.resultlist.size() >0)
							{
								DemoResult demoresult =  subitem.resultlist.getLast();
								if(demoresult == null||(demoresult.targetresult&ResultType.RESULT_DODGE) != 0)
									hitRate = (hitRate.intValue() == -1)?0.0d:1.0d;
								else
									hitRate = (hitRate.intValue() == -1)?1.0d:0.0d;
							}
							else
								hitRate = (hitRate.intValue() == -1)?0.0d:1.0d;
						}
						else if(fighter.getFighterBean().getFightertype() > xbean.Fighter.FIGHTER_PARTNER)
						{
							if(BuffConstant.CONTINUAL_HATEDECISION == buffarg.buffIndex)
							{
								if(fighter.getFighterBean().getSubtype() == Monster.MONSTER_BOSS)
									hitRate += 0.5;
							}
						}
						if (!isHit(hitRate, fighter, buffarg.buffIndex))
						{// 不命中
							DemoResult demoresult = new DemoResult();
							demoresult.targetid = fighter.getFighterId();
							demoresult.targetresult = ResultType.RESULT_DODGE;
//							subitem.resultlist.add(demoresult);
							List<DemoResult> extdemo =  new ArrayList<DemoResult>();
							extdemo.add(demoresult);
							boolean ismain = (index == 0);
							if(index+1 < subSkill.getBuffUnits().length 
									&&  subSkill.getBuffUnits()[index+1] != null 
									&& subSkill.getBuffUnits()[index+1].rateJavascript!=null
									&& getHitRate(subSkill.getBuffUnits()[index+1].rateJavascript, fighter)==-2.0)
								ismain= true;
							addDemoResults(subitem, extdemo, subSkill.getTargettype(), ismain);
							if (buffarg.buffIndex == mainBuff)
							{
						 		if(skillConfig.canAtackBack()&&(buffcopy instanceof fire.pb.buff.single.PhysicalInjure )&&aimfighter.canAction(OperationType.ACTION_ATTACK, 0)&&
						 				(!(opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH) || opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST)||
						 						aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH) || aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST))))
								{	
									float attackbackrate= aimfighter.getEffectRole().getAttrById(FightAttrType.ATTACK_BACK_RATE)-opfighter.getEffectRole().getAttrById(FightAttrType.ANTI_ATTACK_BACK_RATE);
									float frate = (float) Math.random();
									if(frate <attackbackrate)
									{
										SingleBuff instantBuff = fire.pb.buff.Module.getInstance().createInstantBuff(BuffConstant.INSTANT_ATACK_BACK_PHY_DAMAGE);
										DemoResult demo = instantBuff.impact(getBattle(),aimfighter,opfighter, null, effectscopy);
										demo.targetid = aimfighter.getFighterId();
										subitem.resultlist.add(demo);
									}
								}
							}
						}
						else
						{
							if (index == mainBuff)
								mainBuffHit.put(fighter.getFighterId(), true);
							if (buffcopy instanceof ConstantlyBuff)
							{
								boolean isuselastround = false;
								int round = buffarg.roundJavascript.eval(battle.getEngine(),opfighter,fighter).intValue();
								if(round == 0)
								{
									round = ((ConstantlyBuff) buffcopy).getBuffConfig().getInitCount();
								}
								if(round < -1)
								{
									DemoResult demoresult =  subitem.resultlist.getLast();
									if(demoresult != null&&demoresult.demobuffs != null&&demoresult.demobuffs.size() > 0)
									{
										round=demoresult.demobuffs.get(0).round;
										isuselastround = true;
									}
								}
								// 检查是不是增益类法术
								if ((isuselastround == false)&&((ConstantlyBuff) buffcopy).getBuffConfig().getBuffType() / 100 == 2)
								{
									// 检查宠物的永恒
									if (fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ETERNITY_SECOND ))
									{
										round = round * 4;
									} else if (fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ETERNITY_FIRST))
									{
										round = round * 2;
									}
								}
								((ConstantlyBuff) buffcopy).getBuffBean().setRound(round);
								//要重新加隐身buff，就不再清除隐身buff了
								if(buffcopy.getId() == BuffConstant.CONTINUAL_ROLE_HIDDEN)
									useskillremovebuffcount = 0;
							}
							
							if (buffcopy instanceof MagicInjure)
							{
								int minmagicdamage = subSkill.getMinmagicdamageJs().eval(battle.getEngine(),opfighter,fighter).intValue();
								((MagicInjure) buffcopy).setMinmagicdamage(minmagicdamage);
							}
							if(iswithhidebuffeffect == true && buffcopy instanceof Injure)
								((Injure) buffcopy).setExtCritRate(PropConf.Battle.ROLE_HIDE_EXT_CRIT_RATE);
							List<DemoResult> demos = processBuff(buffcopy, battle, opfighter, fighter, effectscopy);
							if (demos != null && demos.size() > 0)
							{
								List<DemoResult> extdemo =  new ArrayList<DemoResult>();
								for(DemoResult demo: demos)
								{
									if(getOperator() > 0 && BattleField.checkOutBattle(battle,getOperator()))
										BattleField.removeDeathFighterHaloBuffs(battle,demo,getOperator());//extdemo.addAll(BattleField.removeDeathFighterHaloBuffs(battle,getOperator()));
	
									if(demo.targetid > 0 && BattleField.checkOutBattle(battle,demo.targetid))
										BattleField.removeDeathFighterHaloBuffs(battle,demo,demo.targetid);//extdemo.addAll(BattleField.removeDeathFighterHaloBuffs(battle,demo.targetid));
									
									if(demo.protecterid > 0 && BattleField.checkOutBattle(battle,demo.protecterid))//这个基本不可能
										BattleField.removeDeathFighterHaloBuffs(battle,demo,demo.protecterid);//extdemo.addAll(BattleField.removeDeathFighterHaloBuffs(battle,demo.protecterid));
									
									if((demo.targetresult&(ResultType.RESULT_HURT|ResultType.RESULT_DEFENCE|ResultType.RESULT_FIRE_MANA|ResultType.RESULT_DEATH|ResultType.RESULT_GHOST|ResultType.RESULT_KICKOUT)) >0)
									{
										List<DemoResult> tempdemo = processSpecialPassiveSkillAfterBuff(fighter, demo.resulttype);
										if(tempdemo != null && tempdemo.size() > 0)
											extdemo.addAll(tempdemo);
										if((ResultType.RESULT_DEATH & demo.targetresult)>0)											
										{
											if(opfighter.isPartner()&&opfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_DIE_FORBID_SKILL) )
											{
												ConstantlyBuff frozenbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_DIE_FORBID);
												if(frozenbuff != null)
												{
													frozenbuff.setRound(2);
													DemoResult frozendemo = frozenbuff.impact(battle, opfighter, fighter, null, null);
													extdemo.add(frozendemo);
												}
											}
										}
										if(((ResultType.RESULT_DEATH|ResultType.RESULT_KICKOUT )& demo.targetresult)>0)											
										{
											if(battle.getBattletype() == BattleType.BATTLE_CLANBOSS && fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CLANBOSS))
											{
												InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(battle.getHostroleid(), false);
												if (instzone != null) {
													if (instzone instanceof FactionInstZone) {
														xbean.ClanInstances clanInst = ((FactionInstZone) instzone).getClanInstData();
														if (((FactionInstZone) instzone).checkBattleid(battle.getConfigid())) {
															xbean.ClanBossInfo bossinfo = clanInst.getBossinfo().get(battle.getConfigid());
															Fighter boss = battle.getFighterobjects().get(bossinfo.getFighteindx());
															DemoResult demoresult = new DemoResult();
															demoresult.targetid = bossinfo.getFighteindx();
															demoresult.targetresult = ResultType.RESULT_HPCHANGE;													
															boss.getEffectRole().setHp(boss.getEffectRole().getHp() - fighter.getFighterBean().getInihp());													
															demoresult.hpchange = -fighter.getFighterBean().getInihp();													
															extdemo.add(demoresult);
														}
													}
												}
											}
										}	
									}
									if(demo.hpchange != 0)
										battle.getEngine().setMainDamage(demo.hpchange);// 初始化主伤害
									else if(demo.mpchange != 0)
										battle.getEngine().setMainDamage(demo.mpchange);// 初始化主伤害
								}								
								if(extdemo.size() > 0)
									demos.addAll(extdemo);
								addDemoResults(subitem, demos, subSkill.getTargettype(), index == 0);
							}
						}
					}
				}
				
				if(subitem.resultlist.size() > 0)
				{
					item.subresultlist.add(subitem);
				}
			}
			setRestState();
			if (item.subresultlist.size() == 0)
			{
				// 失败处理
				processSkillFailed();
				return item;
			}
			else
			{
				if(useskillremovebuffcount > 0 && item.subresultlist.size() > 0)
				{
					SubResultItem lastsubitem = item.subresultlist.get(item.subresultlist.size()-1);
					if(lastsubitem!=null)
						lastsubitem.resultlist.add(useskillremovebuffdemoresult);
					useskillremovebuffcount = 0;
				}
				
				//连击触发
				List<DemoResult> demoAfterSkill = processSpecialPassiveSkillAfterSkill();
				if(demoAfterSkill != null)
				{ 
					SubResultItem lastsubitem = item.subresultlist.get(item.subresultlist.size()-1);					
					addDemoResults(lastsubitem, demoAfterSkill, 0, false);
				}
				
				checkDoubleSkill();
			}	
			return item;
		} catch (Exception e)
		{
			e.printStackTrace();
			Module.logger.error("技能释放异常：" + toString() +". " , e);
			return null;
		}
	}
	
	protected DemoResult processMiss(IBuff buff, Fighter opfighter, Fighter aimfighter, Map<Integer, fire.script.JavaScript> effects)
	{
		return null;
	}
	
	
	protected List<DemoResult> processBuff(IBuff buff, BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter, Map<Integer, fire.script.JavaScript> effects)
	{
		List<DemoResult> demos = new ArrayList<DemoResult>();
		if(buff instanceof SingleBuff && skillConfig.getForbiddenDefPrt() > 0)
		{
			((SingleBuff)buff).setForbiddenDefPrt(skillConfig.getForbiddenDefPrt());
		}
		DemoResult demo = buff.impact(battle, opfighter, aimfighter, this, effects);

		if (demo != null)
		{
/*			// 处理buff覆盖
			List<Integer> overbuffIds = aimfighter.getBuffAgent().getOverridedBuffIds(buff.getId());
			Result result = new Result(true);
			// 移除buff
			for (int overbuffId : overbuffIds)
			{
				result.updateResult(aimfighter.getBuffAgent().removeCBuff(overbuffId));
			}
			// 将覆盖的buff更新到战斗demo
			fire.pb.buff.Module.updateDemoResultFromResult(demo, result, aimfighter);
*/			// 加入demo
			demos.add(demo);
			//处理溅射
			if(buff instanceof SkillPhyInjure)
			{
				List<DemoResult> diffusedemos = ((fire.pb.buff.single.PhysicalInjure)buff).handleDiffuse();
				if(diffusedemos.size() > 0)
					demos.addAll(diffusedemos);		
			}
			// 处理buff执行后战斗者死亡
			recordDeathFighters(demo);

	 		if(skillConfig.canAtackBack()&&(buff instanceof fire.pb.buff.single.PhysicalInjure )&&aimfighter.canAction(OperationType.ACTION_ATTACK, 0)&&
	 				(!(opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH) || opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST)||BattleField.checkOutBattle(battle, aimfighter.getFighterId())||
	 						aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH) || aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST))))
//	 		if((buff instanceof fire.pb.buff.instant.PhysicalInjure )&&aimfighter.canAction(OperationType.ACTION_ATTACK, 0)&&(!(opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH) || opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST))))
			{	
				float attackbackrate= aimfighter.getEffectRole().getAttrById(FightAttrType.ATTACK_BACK_RATE)-opfighter.getEffectRole().getAttrById(FightAttrType.ANTI_ATTACK_BACK_RATE);
				float frate = (float) Math.random();
//				BattleField.logger.debug(" frate（随机值） "+ frate + "  attackbackrate（反击几率）: "+ attackbackrate);		
				if(frate <attackbackrate)
				{
					SingleBuff instantBuff = fire.pb.buff.Module.getInstance().createInstantBuff(BuffConstant.INSTANT_ATACK_BACK_PHY_DAMAGE);
					DemoResult atackbackdemo = instantBuff.impact(getBattle(),aimfighter,opfighter, null, effects);
					atackbackdemo.targetid = aimfighter.getFighterId();
					demos.add(atackbackdemo);
				}
			}
		}
		else
		{
			if(buff.getId() == BuffConstant.INSTANT_RELIVE && aimfighter != null && aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DIE_FORBID))
			{
				item.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
				item.execute.msgid = BasicOperation.FAIL_CANNOT_RELIVE_WITH_DIE_FORBID;
			}
		}					
		return demos;
	}

	protected void addDemoResults(SubResultItem subitem,List<DemoResult> demos, int targetType, boolean isMainbuff)
	{

		// 此处目的是将跟主buff相关的辅buff加入主buff的demo，减少demo数量，特别是有的demo只有一个持续性buff的id
		for (Iterator<DemoResult> it = demos.iterator(); it.hasNext();)
		{
			DemoResult demo = it.next();
			// 验证除buff变化外，没有血蓝等结果的变化
			if ((demo.targetresult == 0 && demo.attackerresult == 0 && demo.assisterid == 0 && demo.protecterid == 0)||(demo.targetresult&ResultType.RESULT_DODGE) > 0)
			{
				// 有buff变化
				if (demo.demobuffs.size() != 0)
				{
					for (DemoResult existDemo : subitem.resultlist)
					{
						// 找到前面已经存在的同一目标的demo
						if (existDemo.targetid == demo.targetid)
						{
							existDemo.demobuffs.addAll(demo.demobuffs);
							if(it.hasNext()){
								it.remove();// 将buff加入之前的buff，移除该条demo
							}
							else
							{
								it.remove();// 将buff加入之前的buff，移除该条demo
								break;
							}
						}
					}
				} else
				{
					if (!isMainbuff)
						it.remove();// 这个demo什么都没有？移除
				}
			}
			else if(demo.resulttype == 0&&(demo.hpchange > 0||demo.mpchange > 0||demo.spchange > 0||demo.epchange > 0)
					&&((demo.targetresult&(ResultType.RESULT_ABORBE|ResultType.RESULT_HURT)) == 0) )
			{
/*				// 有buff变化
				if (demo.demobuffs.size() != 0)
				{*/
					for (DemoResult existDemo : subitem.resultlist)
					{
						// 找到前面已经存在的同一目标的demo
						if (existDemo.targetid == demo.targetid)
						{
							existDemo.hpchange += demo.hpchange;
							existDemo.mpchange += demo.mpchange;
							existDemo.spchange += demo.spchange;							
							existDemo.epchange += demo.epchange;	
							existDemo.targetresult =existDemo.targetresult|demo.targetresult;
//							if(it.hasNext()){
								it.remove();// 将buff加入之前的buff，移除该条demo
								break;
//							}
						}
					}
/*				}
				else
				{
					if (!isMainbuff)
						it.remove();// 这个demo什么都没有？移除
				}	*/			
			}			
		}
		if (!isMainbuff)
		{
			// 如果作用于自身的buff不是主要buff，则将用于自身的buff从单独的demo移到execute中
			for (Iterator<DemoResult> it = demos.iterator(); it.hasNext();)
			{
				DemoResult demo = it.next();
				if (demo.targetid == item.execute.attackerid && demo.targetresult == 0)
				{
					item.execute.demobuffs.addAll(demo.demobuffs);
					it.remove();
				}
			}

		}
		// 将剩余的脚本加入
		for(int i = 0; i < demos.size();i++)
		{
			if((demos.get(i).targetresult&ResultType.RESULT_HURT) > 0 && (demos.get(i).targetresult&ResultType.RESULT_DODGE) > 0)
				demos.get(i).targetresult-=ResultType.RESULT_DODGE;
		}
		if(demos.size() > 0)
			subitem.resultlist.addAll(demos);
	}


	public int getOperator()
	{
		return operator;
	}

	public int getAim()
	{
		return aim;
	}

	public xbean.BattleInfo getBattle()
	{
		return battle;
	}

	public Fighter getOpfighter()
	{
		return opfighter;
	}

	public FightSkillConfig getSkillConfig()
	{
		return skillConfig;
	}

	protected boolean canChangeAim()
	{
		return true;
	}

	/**
	 * 设置休息状态 战斗者使用技能后进入休息状态，不再参与合击出手等
	 */
	protected void setRestState()
	{/**/
		if(opfighter.getFighterBean().getSubtype() == Monster.MONSTER_CLANBOSS||((opfighter.getFighterBean().getRound() < 1||(battle.getRound() == 1?opfighter.getFighterBean().getRound() == 1:opfighter.getFighterBean().getRound() < 1))&&
				(skillId == SkillConstant.BATTLE_ROLE_BEHUNTER||
				skillId == SkillConstant.BATTLE_HUOBAN_BEHUNTER_1||
				skillId == SkillConstant.BATTLE_HUOBAN_BEHUNTER_2||
				skillId == SkillConstant.BATTLE_HUOBAN_BEHUNTER_3||
				skillId == SkillConstant.BATTLE_HUOBAN_BEHUNTER_4||
				skillId == SkillConstant.BATTLE_PET_HIDDEN_FIRST||
				skillId == SkillConstant.BATTLE_PET_HIDDEN_SECOND)
				))
			return;
		int result = battle.getRoundresult().get(getOperator());
		battle.getRoundresult().put(getOperator(), result | ResultType.RESULT_REST);
	}

	public final static int RESULT_DEATH_GHOST = ResultType.RESULT_DEATH | ResultType.RESULT_GHOST;
	/**
	 * 记录在本技能中死亡的战斗者
	 * 
	 * @return 可能为null
	 */
	protected void recordDeathFighters(DemoResult demo)
	{
		//  死亡
		if ((demo.attackerresult & RESULT_DEATH_GHOST) != 0)
		{
			if(getOperator() != 0)
				deadfighterIds.add(getOperator());
		}
		else if((demo.targetresult & RESULT_DEATH_GHOST) != 0)
		{
			if(demo.targetresult != 0)
				deadfighterIds.add(demo.targetid);
		}
		else if((demo.protecterresult & RESULT_DEATH_GHOST) != 0)
		{
			deadfighterIds.add(demo.protecterid);
		}
	}

	/**
	 * 释放法术成功后，检查法术连击
	 */
	protected void checkDoubleSkill()
	{
		if (!skillConfig.canSkillDouble())
			return;
		if (opfighter.isDoubleSkill())
		{
			opfighter.setDoubleSkill(false);// 已经法术连击后就不能连击了
			return;
		}
		if (!BattleField.checkLiveInBattle(battle, aimfighter.getFighterId()))
			return;
		
		if(SkillConstant.BATTLE_SKILL_BASIC_ATTACK == skillId || SkillConstant.BATTLE_SKILL_FAST_ATTACK == skillId ||
				SkillConstant.BATTLE_SKILL_BASIC_CTRL_ATTACK == skillId ||SkillConstant.BATTLE_SKILL_BASIC_CHAOS_ATTACK == skillId ||  
				opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS)||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
			return;
		
		float comborate = opfighter.getEffectRole().getAttrById(FightAttrType.MAGIC_COMBO_ATTACK_RATE);
		if (Math.random() < comborate)
		{
			opfighter.setDoubleSkill(true);
		}			
	}

	/**
	 * 当item.resultlist.size() == 0 时，技能释放失败，需要还原消耗血蓝
	 */
	protected void processSkillFailed()
	{
		// 法术释放失败
		// 返还蓝血消耗
		opfighter.getEffectRole().addHp(-item.execute.hpconsume);
		opfighter.getEffectRole().addMp(-item.execute.mpconsume);
		opfighter.getEffectRole().addSp(-item.execute.spconsume, 0);
		if (item.execute.spconsume != 0)// sp变化放入属性变化中
			opfighter.getRoundBuffResult().getChangedAttrs().put(AttrType.SP, (float) opfighter.getEffectRole().getSp());
		item.execute.hpconsume = 0;
		item.execute.mpconsume = 0;
		// 不能法术连击
		opfighter.setDoubleSkill(false);
		{
			item.execute.attackerid = getOperator();
			if(item.execute.msgid==0 && (failReason == BasicOperation.FAIL_HP_NOT_ENOUGH ||failReason == BasicOperation.FAIL_MP_NOT_ENOUGH
					||failReason == BasicOperation.FAIL_SP_NOT_ENOUGH||failReason == BasicOperation.FAIL_EP_NOT_ENOUGH ||failReason==0))	
				item.execute.operationtype = OperationType.ACTION_FAILURE;
			else if(item.execute.msgid == BasicOperation.FAIL_HP_NOT_ENOUGH_WARRIOR && (item.execute.operationid == SkillConstant.BATTLE_HUMAN_WARRIOR_3_1 
			||item.execute.operationid == SkillConstant.BATTLE_HUOBAN_WARRIOR_3_1||item.execute.operationid == SkillConstant.BATTLE_HUOBAN_WARRIOR_3_2
			||item.execute.operationid == SkillConstant.BATTLE_HUOBAN_WARRIOR_3_3||item.execute.operationid == SkillConstant.BATTLE_HUOBAN_WARRIOR_3_4))
			{
				item.execute.operationtype = OperationType.ACTION_FAILURE;		
			}
			else
			{
				item.execute.operationtype = OperationType.ACTION_FAILURE_NO_WONDER;
			}
			if(failReason!=0)
				item.execute.msgid = failReason;
			SubResultItem subitem = new SubResultItem();
			subitem.subskillid = 0;
			subitem.subskillstarttime = 0;
			item.subresultlist.add(subitem);
		}
	}

	protected boolean checkClashAndDepend() {
		// TODO 自动生成的方法存根
		return false;
	}
}
