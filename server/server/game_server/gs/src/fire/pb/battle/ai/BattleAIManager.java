package fire.pb.battle.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SAIAction;
import fire.pb.battle.SBattleAI;
import fire.pb.battle.SCondition;
import fire.pb.battle.SRoleFighteAI;
import fire.pb.battle.STargetFilter;
import fire.pb.battle.SubResultItem;
import fire.pb.battle.operate.AiOperate;
import fire.pb.main.ConfigManager;
import fire.pb.skill.Module;
import fire.pb.skill.fight.FightSkillConfig;
import fire.script.JavaScript;


public class BattleAIManager
{
	private final static BattleAIManager instance = new BattleAIManager();
	private BattleAIManager(){};
	public static BattleAIManager getInstance()
	{
		return instance;
	}

	
	/**all AI*/
	Map<Integer,BattleAI> battleAIs = new TreeMap<Integer, BattleAI>();
	
	public Map<Integer, BattleAI> getBattleAIs()
	{
		return battleAIs;
	}	
	
	/**Require*/
	Map<Integer,Require> requires = new TreeMap<Integer, Require>();	
	public Map<Integer,Require> getRequires()
	{
		return requires;
	}
	
	/**所有目标过滤*/
	Map<Integer,TargetFilter> targetFilters = new TreeMap<Integer, TargetFilter>();	
	public Map<Integer,TargetFilter> getTargetFilters()
	{
		return  targetFilters;
	}
	
	/**所有动作*/
	Map<Integer,AIOperation> actions = new TreeMap<Integer, AIOperation>();	
	public Map<Integer,AIOperation> getAIActions()
	{
		return  actions;
	}
	
	Map<Integer, List<Integer>> roleschoolfighterais = new TreeMap<Integer, List<Integer>>();
	Map<Integer, Integer> aiid2fighteraiid = new TreeMap<Integer,Integer>();

	public List<Integer> getRoleFighteAIIDs( int schoolid)
	{
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> aiids =   roleschoolfighterais.get(schoolid);
		if(aiids == null)
			return null;
		for(int id: aiids)
		{
			SRoleFighteAI roleai = getRoleFighteAI(id);
			if (roleai != null)
				list.add(roleai.id);
		}
		return list;
	}
	
	public List<Integer> getRoleDefaultFighteAIIDs( int schoolid)
	{
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> aiids =   roleschoolfighterais.get(schoolid);
		if(aiids == null)
			return null;
		for(int id: aiids)
		{
			SRoleFighteAI roleai = getRoleFighteAI(id);
			if (roleai != null && roleai.defaulthave > 0)
				list.add(roleai.aiid);
		}
		return list;
	}
	
	public List<SRoleFighteAI> getRoleFighteAIs( int schoolid)
	{
		List<SRoleFighteAI> list = new ArrayList<SRoleFighteAI>();
		List<Integer> aiids =   roleschoolfighterais.get(schoolid);
		if(aiids == null)
			return null;
		for(int id: aiids)
		{
			SRoleFighteAI roleai = getRoleFighteAI(id);
			if (roleai != null)
				list.add(roleai);
		}
		return list;
	}
	
	public int getRoleFighteAIIDbyAIID(int aiid)
	{
		if(aiid2fighteraiid.get(aiid)!=null)
			return aiid2fighteraiid.get(aiid);
		return 0;
	}
	
	
	public SRoleFighteAI getRoleFighteAI(int id)
	{
		return ConfigManager.getInstance().getConf(fire.pb.battle.SRoleFighteAI.class).get(id);
	}

	public void initBattleAI()
	{
		//parse targetfilter
		Map<Integer,SRoleFighteAI> srolefighterais = ConfigManager.getInstance().getConf(fire.pb.battle.SRoleFighteAI.class);
		synchronized (roleschoolfighterais)
		{
			roleschoolfighterais.clear();
			for (SRoleFighteAI srolefighterai : srolefighterais.values())
			{
				List<Integer> list =  roleschoolfighterais.get(srolefighterai.schoolid);
				if(roleschoolfighterais.get(srolefighterai.schoolid) == null)
				{
					 list = new ArrayList<Integer>();					 
					 roleschoolfighterais.put(srolefighterai.schoolid, list);
				}
				list.add(srolefighterai.id);
				 aiid2fighteraiid.put(srolefighterai.aiid, srolefighterai.id);
			}
		}
		
		Map<Integer,STargetFilter> stargetFilters = ConfigManager.getInstance().getConf(fire.pb.battle.STargetFilter.class);
		synchronized (targetFilters)
		{
			targetFilters.clear();
			for (STargetFilter sfilter : stargetFilters.values())
			{
				TargetFilter filter = new TargetFilter(sfilter.id);
				filter.hpPercentMin = ((sfilter.hppercentmin == null) ? -1 : Float.valueOf(sfilter.hppercentmin));
				filter.hpPercentMax = ((sfilter.hppercentmax == null) ? -1 : Float.valueOf(sfilter.hppercentmax));
				filter.mpPercentMin = ((sfilter.mppercentmin == null) ? -1 : Float.valueOf(sfilter.mppercentmin));
				filter.mpPercentMax = ((sfilter.mppercentmax == null) ? -1 : Float.valueOf(sfilter.mppercentmax));
				filter.relation = ((sfilter.relation == null) ? -1 : Float.valueOf(sfilter.relation).intValue());
				filter.resultType = ((sfilter.roundresult == null) ? -1 : Float.valueOf(sfilter.roundresult).intValue());
				filter.includeDead = ((sfilter.includedead == null)? false : Float.valueOf(sfilter.includedead).intValue()==1);// ==null时为false，==1时为true
				filter.includeHide = ((sfilter.includehide == null)? false : Float.valueOf(sfilter.includehide).intValue()==1);// ==null时为false，==1时为true
				filter.sex = ((sfilter.gender == null) ? -1 : Float.valueOf(sfilter.gender).intValue());
				filter.minLevel = sfilter.levelmin;
				filter.maxLevel = sfilter.levelmax;
				
				if (sfilter.unitrelation != null)
				{
					String[] strs = sfilter.unitrelation.split(";");
					for (String str : strs)
						filter.unitRelation.add(Float.valueOf(str).intValue());
				}
				
				filter.unitCamp = sfilter.unitcamp;
				
				if (sfilter.unittype != null)
				{
					String[] strs = sfilter.unittype.split(";");
					for (String str : strs)
						filter.unitType.add(Float.valueOf(str).intValue());
				}				

				filter.unitMonsterType = sfilter.unitmonstertype;
				filter.specialType = sfilter.specialtype;

				if(sfilter.attributesort != null)
				{
					String[] strs = sfilter.attributesort.split("@");
					filter.sortAttrId = fire.pb.effect.Module.getInstance().getAttrIdByName(strs[0]);
					filter.sortMaxMin = strs[1].equals("1");
				}
				
				if (sfilter.school != null)
				{
					try
					{
						String[] strs = sfilter.school.split("_");
						for (int i = 1; i < strs.length; i += 2)
						{
							int job = Integer.valueOf(strs[i]);
							if (job > 0)
								filter.jobs.add(job);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}

				if (sfilter.buffs != null)
				{
					try
					{
						String[] strs = sfilter.buffs.split("_");
						for (int i = 1; i < strs.length; i += 2)
						{
							int buffId = Integer.valueOf(strs[i]);
							if (buffId > 0)
								filter.buffs.add(buffId);
						}
						filter.buffjs = new JavaScript(sfilter.buffs);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				
				if (sfilter.monsterids != null)
				{
					String[] strs = sfilter.monsterids.split(";");
					for (String str : strs)
						filter.monsterIds.add(Float.valueOf(str).intValue());
				}
				if (sfilter.figherids != null)
				{
					String[] strs = sfilter.figherids.split(";");
					for (String str : strs)
						filter.fighterIds.add(Float.valueOf(str).intValue());
				}
				targetFilters.put(filter.id, filter);
			}
		}
		
		//parse AIOperation
		Map<Integer,SAIAction> saiActions = ConfigManager.getInstance().getConf(fire.pb.battle.SAIAction.class);
		synchronized (actions)
		{
			actions.clear();
			for (SAIAction saction : saiActions.values())
			{
				try
				{
					AIOperation operation = new AIOperation(saction.id);
					operation.changeShape = (saction.changeshape == null)? -1 :Float.valueOf(saction.changeshape).intValue();
					operation.operationType = (saction.optiontype == null) ? -1 : Float.valueOf(saction.optiontype).intValue();
					operation.operationId = (saction.optionid == null) ? -1 : Float.valueOf(saction.optionid).intValue();
					operation.escOdds = (saction.escapeodds == null) ? -1 : Float.valueOf(saction.escapeodds).intValue();
					operation.goal = (saction.target == null) ? -1 : Float.valueOf(saction.target).intValue();
					operation.countFactor = (saction.skillfactor == null || saction.skillfactor.equals("")) ? 0 : Float.valueOf(saction.skillfactor).floatValue();
					operation.countConst = (saction.skillconstant == null || saction.skillconstant.equals("")) ? 0 : Float.valueOf(saction.skillconstant).floatValue();
					operation.changeBG = saction.changeground;
					operation.clientAction = saction.clientAction;
					operation.changeAim = (saction.changeaim > 0 )? true :false;
					if (saction.skillsoltid != null)
					{
						String[] strs = saction.skillsoltid.split(";");
						for (String str : strs)
						{
							String[] ints = str.split("@");
							operation.skillSoltId.add(Integer.valueOf(ints[0]));
							operation.skillSoltOdds.add(Float.valueOf(ints[1]));							
						}
					}
					
					if (saction.summons != null)
					{
						String[] strs = saction.summons.split(",");
						for (String str : strs)
						{
							String[] ints = str.split("\\$");
							if(ints[0].length() > 2)
							{
								String strsummons = ints[0].substring(1,ints[0].length()-1);								
								operation.summonMaps.put(Integer.valueOf(ints[1]), fire.pb.util.Parser.parseIdAndOdds(strsummons));
							}
						}
					}
					actions.put(operation.index, operation);
				} catch (Exception e)
				{
					BattleField.logger.error("战斗AI动作初始化失败，ActionId = " + saction.id,e);
				}
				
			}
		}
		
		//parse require
		Map<Integer,SCondition> sconditions = ConfigManager.getInstance().getConf(fire.pb.battle.SCondition.class);
		synchronized (requires)
		{
			requires.clear();
			for (SCondition scondition : sconditions.values())
			{
				try
				{
					Require require = new Require(scondition.id);
					require.strBattleRound = scondition.battleround;
					require.strFighterRound = scondition.fighterround;
					require.bounusnum = scondition.bounusnum;
					if(scondition.skillids != null)
					{
						String[] strs = scondition.skillids.split(";");
						for(String str : strs)
							require.beActedSkills.add(Integer.valueOf(str));
					}
					require.odds = (scondition.odds == null) ? 1 : Float.valueOf(scondition.odds);
					if(scondition.conditionscript != null && !scondition.conditionscript.equals(""))
						require.requireJS = new JavaScript(scondition.conditionscript);
					if (scondition.targetfilters != null && !scondition.targetfilters.equals(""))
					{
						
						try
						{
							String[] strs = scondition.targetfilters.split("_");
							for (int i = 1; i < strs.length; i += 2)
							{
								int targetfilterId = Integer.valueOf(strs[i]);
								if (targetfilterId > 0)
									require.goalFilterIds.add(targetfilterId);
							}
							require.strGoalRequire =  new JavaScript(scondition.targetfilters);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					requires.put(require.index, require);
				} catch (Exception e)
				{
					BattleField.logger.error("战斗AI条件初始化失败，ConditionId = " + scondition.id,e);
				}
			}
		}

		//parse battle ai
		Map<Integer,SBattleAI> sbattleAIs = ConfigManager.getInstance().getConf(fire.pb.battle.SBattleAI.class);
		synchronized (battleAIs)
		{
			battleAIs.clear();
			for (SBattleAI sai : sbattleAIs.values())
			{
				try
				{

					BattleAI ai = new BattleAI(sai.id);
					ai.triggerType = sai.trigger;
					if (sai.conditions != null)
					{
						String[] strs = sai.conditions.split(";");
						for (String str : strs)
						{
							Require requires = getRequires().get(Float.valueOf(str).intValue());
							if (requires == null)
								throw new NullPointerException("Null Require id = " + str);
							ai.requires.add(requires);
						}
					}
					// AIOperation action = getAIActions().get(sai.actionId);
					if (sai.actionId != null)
					{
						String[] strs = sai.actionId.split(";");
						for (String str : strs)
						{
							int acId = Float.valueOf(str).intValue();
							AIOperation action = getAIActions().get(acId);
							if (action == null)
							{
								if (BattleField.logger.isDebugEnabled()) 
								{
									BattleField.logger.debug("AI总表里的动作在动作表中不存在,AIID=" + sai.id + "; ActionId=" + acId);
								}
							}
							else
								ai.actions.add(action);
						}
					} 
					else if(sai.enableAI == null || sai.enableAI.isEmpty())
					{
						if (BattleField.logger.isDebugEnabled()) 
						{
							BattleField.logger.debug("AI总表里的动作ID为空,AI ID = " + sai.id);
						}
					}

					ai.count = (sai.count == null) ? -1 : Float.valueOf(sai.count).intValue();

					if (sai.enableAI != null)
					{
						String[] strs = sai.enableAI.split(";");
						for (String str : strs)
						{
							String[] enableAI = str.split("@");
							if(enableAI[1].equals("1"))
								ai.enableAIs.add(Integer.valueOf(enableAI[0]));
							else
								ai.disableAIs.add(Integer.valueOf(enableAI[0]));
						}
					}
					battleAIs.put(ai.id, ai);
				} catch (Exception e)
				{
					BattleField.logger.error("战斗AI初始化失败，BattleAIId = " + sai.id,e);
				}
			}
		}
		MonsterAIManager.getInstance().init();
	}
	
	/**获取战斗开始时会触发的AI，该动作进入战斗时检测*/
	public HashMap<Integer,Integer> getActionWhileBattleStart(xbean.BattleInfo battle)
	{	
		HashMap<Integer,Integer> actionIds = new HashMap<Integer, Integer>();
		//战场AI产生的动作
		if(battle.getBattleais().size()!=0)
		{
			BattleAIAgent aiagent = battle.getFieldfighter().getAiagent();
			List<AIOperation> actions =  aiagent.getAIActions(BattleAI.TRIGGER_BATTLE_START);
			for(AIOperation action : actions)
				if(action.isClientAction())
				{
					if(action.changeAim == true&&action.goal>0 && aiagent.getTargetId(action.goal) > 0)
					{
						actionIds.put(aiagent.getTargetId(action.goal), action.index);
					}
					else
						actionIds.put(0, action.index);
				}
		}
		
		for(Fighter fighter : battle.getFighterobjects().values())
		{
			if(fighter.getAiagent()==null||fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE) continue;
			List<AIOperation> actions =  fighter.getAiagent().getAIActions(BattleAI.TRIGGER_BATTLE_START);
			for(AIOperation action : actions)
				if(action.isClientAction())
					actionIds.put(fighter.getFighterId(), action.index);
		}
		return actionIds;
	}
	

	/**获取读秒前会触发的AI，该动回合开始读秒前时检测*/
	public HashMap<Integer,Integer> getActionBeforeRoundStart(xbean.BattleInfo battle)
	{	
		HashMap<Integer,Integer> actionIds = new HashMap<Integer, Integer>();
	
		//战场AI产生的动作
		if(battle.getBattleais().size()!=0)
		{
			BattleAIAgent aiagent = battle.getFieldfighter().getAiagent();
			List<AIOperation> actions =  aiagent.getAIActions(BattleAI.TRIGGER_BEFORE_ROUND_START);
			for(AIOperation action : actions)
				if(action.isClientAction())
				{
					if(action.changeAim == true&&action.goal>0 && aiagent.getTargetId(action.goal) > 0)
					{
						actionIds.put(aiagent.getTargetId(action.goal), action.index);
					}
					else
						actionIds.put(0, action.index);
				}
		}
		
		for(Fighter fighter : battle.getFighterobjects().values())
		{
			if(fighter.getAiagent()==null||fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE) continue;
			List<AIOperation> actions =  fighter.getAiagent().getAIActions(BattleAI.TRIGGER_BEFORE_ROUND_START);
			for(AIOperation action : actions)
				if(action.isClientAction())
					actionIds.put(fighter.getFighterId(), action.index);
		}
		return actionIds;
	}

	
	/**获取回合开始时会触发的AI，回合开始时检测*/
	public LinkedList<fire.pb.battle.AIOperation> getActionWhileRoundStart(xbean.BattleInfo battle,List<AiOperate> extActs)
	{	
		LinkedList<fire.pb.battle.AIOperation> clientActions = new LinkedList<fire.pb.battle.AIOperation>();//客户端AI动作
		//战场AI产生的动作
		if(battle.getBattleais().size()!=0)
		{
			clientActions.addAll(getSingleFighterActions(battle.getFieldfighter(), extActs, BattleAI.TRIGGER_ROUND_START, 0, -1));
		}
		//怪物AI产生的动作
		for(Fighter fighter : battle.getFighterobjects().values())
		{	
			if(fighter.getFightertype()== xbean.Fighter.FIGHTER_ROLE || fighter.getAiagent()==null) continue;
			clientActions.addAll(getSingleFighterActions(fighter, extActs, BattleAI.TRIGGER_ROUND_START, 0, -1));
		}
		return clientActions;
	}
	
	/**获取回合开始时会触发的AI，回合开始时检测*/
	public LinkedList<fire.pb.battle.AIOperation> getRoleActionWhileRoundStart(xbean.BattleInfo battle,List<AiOperate> extActs)
	{	
		LinkedList<fire.pb.battle.AIOperation> clientActions = new LinkedList<fire.pb.battle.AIOperation>();//客户端AI动作

		List<AiOperate> roleextActs = new ArrayList<AiOperate>();//客户端AI动作
		//Role预置AI产生的动作
		for(Fighter fighter : battle.getFighterobjects().values())
		{	
			if(fighter.getFightertype()!= xbean.Fighter.FIGHTER_ROLE||fighter.getAiagent()==null) continue;
			xbean.Decision decision = battle.getDecisions().get(fighter.getFighterId());
			if(decision != null && (decision.getIsauto() == false || decision.getOperatetype() == OperationType.ACTION_CATHCH))
				continue;
			clientActions.addAll(getSingleFighterActions(fighter, roleextActs, BattleAI.TRIGGER_ROUND_START, 0, -1));
		}
		if(roleextActs.size() > 0 )
		{//排序
//			BattleField.sortAIOperation(roleextActs);
			extActs.addAll(roleextActs);
		}
		return clientActions;
	}
	
	/**获取行动前时会触发的AI，玩家行动前检测*/
	public LinkedList<fire.pb.battle.AIOperation> getActionBeforeOperation(xbean.BattleInfo battle,List<AiOperate> extActs, final int actcount,final Fighter fighter)
	{
		LinkedList<fire.pb.battle.AIOperation> clientActions = new LinkedList<fire.pb.battle.AIOperation>();//客户端AI动作
		
		if(fighter == null || fighter.getAiagent()==null)
			return clientActions;
		clientActions.addAll(getSingleFighterActions(fighter, extActs, BattleAI.TRIGGER_BEFORE_OPERATION, actcount, -1));
		return clientActions;
	}
	
	/**获取回合结束时会触发的AI，回合结束检测*/
	/**
	 * 回合结束检测
	 * 可能会有的AIAction:
	 * @return
	 */
	public LinkedList<fire.pb.battle.AIOperation> getActionRoundEnd(xbean.BattleInfo battle, List<AiOperate> extActs, final int actcount,final Fighter fighter)
	{
		LinkedList<fire.pb.battle.AIOperation> clientActions = new LinkedList<fire.pb.battle.AIOperation>();//客户端AI动作
		
		if(fighter == null || fighter.getAiagent()==null||fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			return clientActions;
		clientActions.addAll(getSingleFighterActions(fighter, extActs, BattleAI.TRIGGER_ROUND_END, actcount, -1));
		return clientActions;
	}
	
	/**获取行动后会触发的AI，行动后检测*/
	public List<fire.pb.battle.AIOperation> getActionAfterOperation(xbean.BattleInfo battle,List<AiOperate> extActs, int actcount , ResultItem lastResult)
	{
		List<fire.pb.battle.AIOperation> clientActions = new LinkedList<fire.pb.battle.AIOperation>();//客户端AI动作
		
		//死亡触发
		for(SubResultItem subItem : lastResult.subresultlist)
		{
			for(DemoResult demo : subItem.resultlist)
			{
				if(checkDeath(demo.targetresult))
				{//目标死
					clientActions.addAll(getActionWhileDead(battle, demo.targetid, extActs, actcount));
				}
				if(checkDeath(demo.protecterresult))
				{//保护者死
					clientActions.addAll(getActionWhileDead(battle, demo.protecterid, extActs, actcount));
				}
				if(checkDeath(demo.attackerresult))
				{//攻击者死
					clientActions.addAll(getActionWhileDead(battle, lastResult.execute.attackerid, extActs, actcount));
				}
			}
		}
		Fighter opFighter = battle.getFighterobjects().get(lastResult.execute.attackerid);
		if(opFighter != null && opFighter.getAiagent() != null&&opFighter.getFightertype() != xbean.Fighter.FIGHTER_ROLE)
		{
			opFighter.getAiagent().setLastResultItem(lastResult);
			// 逃跑触发
			if (lastResult.execute.operationtype == OperationType.ACTION_ESCAPE && lastResult.subresultlist.size() > 0)
			{
				SubResultItem subitem = lastResult.subresultlist.get(0);
				if(subitem.resultlist.size() > 0)
				{
					DemoResult demo = subitem.resultlist.get(0);
					if ((demo.targetresult & ResultType.RESULT_RUNAWAY) != 0)
					{// 逃跑成功
						clientActions.addAll(getSingleFighterActions(opFighter, null, BattleAI.TRIGGER_ESCAPE_SUCC, actcount, -1));
					} else
					{// 逃跑失败
						clientActions.addAll(getSingleFighterActions(opFighter, extActs, BattleAI.TRIGGER_ESCAPE_FAIL, actcount, 0));
					}
				}
			}
			// 行动后触发的自己的AI
			clientActions.addAll(getSingleFighterActions(opFighter, extActs, BattleAI.TRIGGER_AFTER_SELF_OPERATION, actcount, 0));
		}
		
		//行动后触发的战场AI
		if(battle.getBattleais().size()!=0)
		{
			battle.getFieldfighter().getAiagent().setLastResultItem(lastResult);
			clientActions.addAll(getSingleFighterActions(battle.getFieldfighter(), extActs, BattleAI.TRIGGER_AFTER_OTHERS_OPERATION, actcount, 0));
		}
		
		
		//行动后触发的其他战斗者AI
		for(Fighter fighter : battle.getFighterobjects().values())
		{
			if(fighter.getAiagent()==null||fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
				continue;
			if(opFighter != null && fighter.getFighterId() == opFighter.getFighterId())
				continue;//自己的别人行动后AI不触发
			fighter.getAiagent().setLastResultItem(lastResult);
			clientActions.addAll(getSingleFighterActions(fighter, extActs, BattleAI.TRIGGER_AFTER_OTHERS_OPERATION, actcount, 0));
		}
		return clientActions;
	}
	
	private boolean checkDeath(int result)
	{
		if ((result & ResultType.RESULT_DEATH) != 0)
			return true;
		
		else if ((result & ResultType.RESULT_KICKOUT) != 0)
			return true;
		
		else if ((result & ResultType.RESULT_GHOST) != 0)
			return true;
		
		return false;
	}
	
	/**获取死亡后会触发的AI，死亡后检测*/
	private LinkedList<fire.pb.battle.AIOperation> getActionWhileDead(xbean.BattleInfo battle ,final int deadFighterId,List<AiOperate> extActs, int actcount)
	{
		LinkedList<fire.pb.battle.AIOperation> clientActions = new LinkedList<fire.pb.battle.AIOperation>();//客户端AI动作
		final Fighter deadFighter = battle.getFighterobjects().get(deadFighterId);
		if(deadFighter == null)
			return clientActions;
		//死亡触发自己的AI
		if(deadFighter.getAiagent()!= null)
		{
			clientActions.addAll(getSingleFighterActions(deadFighter, extActs, BattleAI.TRIGGER_DEATH_SELF, actcount, deadFighterId));
		}
		List<Integer> freinds = BattleField.getFriends(battle, deadFighterId);
		freinds.remove((Integer)deadFighterId);
		//死亡触发友方的AI
		for(int friendId : freinds)
		{
			Fighter friendFighter = battle.getFighterobjects().get(friendId);
			if(friendFighter != null && friendFighter.getAiagent() != null&&friendFighter.getFightertype() != xbean.Fighter.FIGHTER_ROLE)
			{
				clientActions.addAll(getSingleFighterActions(friendFighter, extActs, BattleAI.TRIGGER_DEATH_FRIEND, actcount, deadFighterId));
			}
		}
		
		//死亡触发敌方的AI
		for(int enermyId : BattleField.getEnemies(battle, deadFighterId))
		{
			Fighter enermyFighter = battle.getFighterobjects().get(enermyId);
			if(enermyFighter != null && enermyFighter.getAiagent() != null&&enermyFighter.getFightertype() != xbean.Fighter.FIGHTER_ROLE)
			{
				clientActions.addAll(getSingleFighterActions(enermyFighter, extActs, BattleAI.TRIGGER_DEATH_ENERMY, actcount, deadFighterId));
			}
		}
		//死亡触发战场AI
		clientActions.addAll(getSingleFighterActions(battle.getFieldfighter(), extActs, BattleAI.TRIGGER_DEATH_SELF, actcount, deadFighterId));
		
		return clientActions;
	}
	
	private static List<fire.pb.battle.AIOperation> getSingleFighterActions(Fighter actionFighter,List<AiOperate> extActs,int trigger,int actcount,int actionmoment)
	{
		LinkedList<fire.pb.battle.AIOperation> clientActions = new LinkedList<fire.pb.battle.AIOperation>();//客户端AI动作
		for(AIOperation action : actionFighter.getAiagent().getAIActions(trigger))
		{
			if(extActs != null && action.operationType != -1)
			{
				//有服务器动作
				if(action.changeAim == true&&action.goal>0 && actionFighter.getFighterId() == 0 && actionFighter.getAiagent().getTargetId(action.goal) > 0)
				{
					Fighter aimfighter = actionFighter.getBattle().getFighterobjects().get(actionFighter.getAiagent().getTargetId(action.goal));
					if(aimfighter != null)
						extActs.add(new AiOperate(aimfighter,action));
				}
				else
					extActs.add(new AiOperate(actionFighter,action));
			}
			else
			{
				//纯客户端动作
				fire.pb.battle.AIOperation clientAction = new fire.pb.battle.AIOperation();
				clientAction.actionseq = actcount;//第几轮行动
				clientAction.actionmoment = actionmoment;;//死亡时
				if(action.changeAim == true&&action.goal>0 && actionFighter.getAiagent().getTargetId(action.goal) > 0)
					clientAction.actionfighterid = actionFighter.getAiagent().getTargetId(action.goal);
				else
					clientAction.actionfighterid = actionFighter.getFighterId();					
				clientAction.actionid = action.index;
				clientActions.add(clientAction);
			}
		}
		return clientActions;
	}
		
	/**获取战斗结束后会触发的AI，战斗结束检测*/
	public HashMap<Integer,Integer> getActionWhileBattleEnd(xbean.BattleInfo battle)
	{
		HashMap<Integer,Integer> actionIds = new HashMap<Integer, Integer>();
		try{
			// 战场AI产生的动作
			if (battle.getBattleais().size() != 0)
			{
				BattleAIAgent aiagent = battle.getFieldfighter().getAiagent();
				List<AIOperation> actions = aiagent.getAIActions(BattleAI.TRIGGER_BATTLE_END);
				for (AIOperation action : actions)
					if (action.isClientAction())
						actionIds.put(0, action.index);
			}

			for (Fighter fighter : battle.getFighterobjects().values())
			{
				if (fighter.getAiagent() == null||fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
					continue;
				List<AIOperation> actions = fighter.getAiagent().getAIActions(BattleAI.TRIGGER_BATTLE_END);
				for (AIOperation action : actions)
					if (action.isClientAction())
						actionIds.put(fighter.getFighterId(), action.index);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return actionIds;
	}
	
	/**获得AI指定的常规Decision*/
	public xbean.Decision getDecision(final xbean.BattleInfo battle,final int fighterId)
	{
		Fighter fighter =  null;
		if(fighterId == 0)
			fighter = battle.getFieldfighter();
		else
			fighter = battle.getFighterobjects().get(fighterId);
		if(fighter.getAiagent()== null||fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			return null;
		List<AIOperation> aiactions = fighter.getAiagent().getAIActions(BattleAI.TRIGGER_DECISION);
		if(aiactions.size() == 0)
			return null;
		AIOperation action = null;
		for(AIOperation aiaction : aiactions)
		{
			if(aiaction.operationType == OperationType.ACTION_SKILL)
			{
				FightSkillConfig battleSkillConfig = fire.pb.skill.Module.getInstance().getFightSkillConfig(aiaction.operationId);
				if (battleSkillConfig == null)
				{
					Module.logger.error("ERROR: Battle Skill is not exist. skillId = " + aiaction.operationId);
					continue;
				}
				if(!fire.pb.battle.DecisionProcesser.checkMonsterHpMpConsume(battleSkillConfig, battle, fighter))
					continue;
				else
				{
					action = aiaction;
					break;
				}
			}
			else
			{
				action = aiaction;
				break;
			}
		}
		return BattleField.aiActionToDecision(battle, fighter, action);
	}
}
