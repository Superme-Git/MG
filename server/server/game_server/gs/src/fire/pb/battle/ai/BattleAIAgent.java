package fire.pb.battle.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.script.ScriptException;

import fire.pb.PropRole;
import fire.pb.battle.BattleAimRelation;
import fire.pb.battle.BattleField;
import fire.pb.battle.BattleFieldFighter;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.FighterSorter;
import fire.pb.battle.ResultItem;
import fire.pb.battle.SubResultItem;
import fire.pb.buff.BuffConstant;
import fire.pb.util.ParseObjIDInBattle;

public class BattleAIAgent
{
	final protected int fighterId;
	final protected Fighter fighter;
	final protected xbean.BattleInfo battle;
	protected List<xbean.BattleAI> ais = Collections.synchronizedList(new ArrayList<>());
	protected Map<Integer,List<Integer>> filter2targetIds = new HashMap<Integer, List<Integer>>();

	private List<AIOperation> serverAIActions = new LinkedList<AIOperation>();
	private ResultItem lastresult;
	
	public BattleAIAgent(Fighter fighter , xbean.BattleInfo battle)
	{
		this.fighter = fighter;
		this.battle = battle;
		this.fighterId = fighter.getFighterId();
		if(fighterId != 0 )
		{
			if(fighter.getFightertype()==xbean.Fighter.FIGHTER_ROLE)
			{
				PropRole prole = new PropRole(fighter.getFighterBean().getUniqueid(), true);				
				this.ais = prole.getProperties().getFighteai();
			}
			else 
				this.ais = battle.getMonsters().get(fighterId).getAis();
		}
		else
		{
			this.ais = battle.getBattleais();
		}
	}

	public void setAis(List<xbean.BattleAI> ais)
	{
		this.ais.clear();
		this.ais.addAll(ais);
	}

	public int getFighterId()
	{
		return fighterId;
	}
	
	public void setLastResultItem(ResultItem lastresult)
	{
		this.lastresult = lastresult;
	}
	
	public ResultItem getLastResultItem()
	{
		return this.lastresult;
	}
	

	public int getTargetId(int filterid)
	{
		List<Integer> targetIds = filter2targetIds.get(filterid);
		for(int targetId:targetIds)
			if(targetId == 1)
				return 1;
		return -1;
	}
		

	public List<AIOperation> getServerAIActions()
	{
		return serverAIActions;
	}
	
	public List<AIOperation> getAIActions(int triggerType)
	{
		filter2targetIds.clear(); 
		List<AIOperation> actions = new LinkedList<AIOperation>();
		if(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST))
			return actions;
		try
		{
			if(fighterId == 0)
				battle.getEngine().setFighterRound(battle.getRound());
			else
				battle.getEngine().setFighterRound(fighter.getFighterBean().getRound());
			
			List<Integer> rmIds = new ArrayList<Integer>();
			int aicount = 0;
			for (ListIterator<xbean.BattleAI> lit = ais.listIterator(); lit.hasNext();)
			{
				if (aicount > 40)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("AI触发处理时，迭代次数超过40次， BattleCfgId = " + battle.getConfigid());}
					break;
				} else
					aicount++;
				xbean.BattleAI xai = lit.next();
				battle.getEngine().setAiRound(battle.getRound() - xai.getEnableround() + 1);
				BattleAI ai = BattleAIManager.getInstance().getBattleAIs().get(xai.getId());
				
				if (ai == null)
					continue;
				if (rmIds.contains(ai.id))
					continue;
				if (ai.triggerType != triggerType)
					continue;
				if (!isTriggered(ai))
					continue;

				if(ai.actions.size()>0)
					actions.addAll(ai.actions);

				// 先启用 AI
				int addcount = 0;
				for(int enableaiId : ai.enableAIs)
				{
					boolean alreadyin = false;
					for (xbean.BattleAI aai : ais)
						if (aai.getId() == enableaiId)
						{
							alreadyin = true;
							break;
						}
					if (alreadyin)
						continue;
					xbean.BattleAI xnewai = xbean.Pod.newBattleAI();
					xnewai.setId(enableaiId);
					xnewai.setEnableround(battle.getRound());
					lit.add(xnewai);
					addcount++;
				}
				for(int i = 0; i < addcount; i++)
					lit.previous();
				rmIds.addAll(ai.disableAIs);
				xai.setCount(xai.getCount() + 1);
				if (ai.count > 0 && xai.getCount() >= ai.count)
					rmIds.add(ai.id);
			}
			removeAis(rmIds, ais);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		for (Iterator<AIOperation> it = actions.iterator(); it.hasNext();)
		{
			AIOperation action = it.next();
			if(action.operationType == 2 && action.operationId > 0 
					&& fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE 
					&&!fighter.getSkillAgent().hasSkill(action.operationId))
				it.remove();
		}
		changeShape(actions);
		changeBG(actions);
		return actions;
	}
	

	private void removeAis(List<Integer> rmIds, List<xbean.BattleAI> ais)
	{
		for (Iterator<xbean.BattleAI> it = ais.iterator(); it.hasNext();)
		{
			xbean.BattleAI rmai = it.next();
			if (rmIds.contains(rmai.getId()))
				it.remove();
		}
	}
	
	private void changeBG(List<AIOperation> actions)
	{
		for(AIOperation action : actions)
		{
			if(action.changeBG <= 0)
				continue;
			battle.setBackground(action.changeBG);
		}
	}

	private void changeShape(List<AIOperation> actions)
	{
		if(fighterId == 0)
			return;
		xbean.Monster xmonster = battle.getMonsters().get(fighterId);
		if(xmonster == null)
			return;
		for(AIOperation action : actions)
		{
			if(action.changeShape > 0)
				xmonster.setShape(action.changeShape);
		}
	}
	private boolean isTriggered(BattleAI ai)
	{
		if(ai == null)
			return false;
		if(ai.requires == null||ai.requires.size() <1)
		{
			if(battle.getRound() < 1&&ai.triggerType != BattleAI.TRIGGER_DECISION) 
				return false;
		}
		
		for(Require require : ai.requires)
		{
			try
			{
				if(require.odds!= -1 && Math.random() > require.odds)
					return false;//有几率要求，并且没有触发
				
				if(require.bounusnum > 0)
				{
					int bounusnumcount = 0;
					for(int fighterid:battle.getRoleids().values())
					{	
						Fighter rolefighter = battle.getFighterobjects().get(fighterid);				
						if(rolefighter != null && rolefighter.getBounusNum() > 0)
						{
							bounusnumcount++;
							break;
						}							
					}
					if(bounusnumcount == 0)
						return false;//有奖励点数要求，所有玩家都没活动点数
				}
				
				//验证满足战斗回合数条件
				if(require.strBattleRound!=null )
				{
					if(!fire.script.Calculate.getResult(battle.getRound(), require.strBattleRound))
						return false;
				}
				else if(battle.getRound() < 1)
					return false;
				
				if((require.strFighterRound!=null) && fighter!=null && 
						!fire.script.Calculate.getResult(fighter.getFighterBean().getRound(), require.strFighterRound))
					return false;
				if(require.beActedSkills.size() > 0)
				{
					if(!require.beActedSkills.contains(getLastResultItem().execute.operationid))
						return false;
					boolean hasfighter = false;
					for(SubResultItem subItem :getLastResultItem().subresultlist)
					{
						for(DemoResult demo :subItem.resultlist)
						{
							if(demo.targetid == getFighterId())
								hasfighter = true;
						}
					}
					if(!hasfighter)
						return false;
				}
				
				if(require.strGoalRequire != null)
				{
					Map<Integer, List<Integer>> targets = new HashMap<Integer, List<Integer>>();
					for (int filterId : require.goalFilterIds)
					{
						List<Integer> targetFids = getTargets(filterId);
						if (targetFids == null)
							targetFids = new LinkedList<Integer>();
						targets.put(filterId, targetFids);
					}
					for (Map.Entry<Integer, List<Integer>> entry : targets.entrySet())
					{
						battle.getEngine().put("_" + entry.getKey() + "_", entry.getValue().size());
					}
					Boolean b = require.strGoalRequire.evalToBoolean(battle.getEngine(),fighter,null);
					if(b != null && !b)
						return false;
/*						if (!(Boolean) (battle.getEngine().eval("with(Math){(" + require.strGoalRequire + ")? true : false}")))
						return false;
*/
				}
				
				if(require.requireJS!= null)
				{
					if(fighter != null&&fighter.getFighterId() > 0)
						battle.getEngine().put("RoleLv", fighter.getEffectRole().getLevel());
					else
						battle.getEngine().put("RoleLv", battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_AVERAGE_LEVEL));
					Boolean b = require.requireJS.evalToBoolean(battle.getEngine(),fighter,null);
					if(b != null && !b)
						return false;
				}
			}
			catch (Exception e)
			{
				BattleField.logger.error("错误的JS： " + require.strGoalRequire+"ai"+ai.id, e);
				return false;
			}
		}
		
		return true;
	}
	
	public List<Integer> getTargets(int filterId)
	{
		if(filter2targetIds.containsKey(filterId))
			return filter2targetIds.get(filterId);
		TargetFilter filter = BattleAIManager.getInstance().getTargetFilters().get(filterId);
		if(filter == null)
			return null;
		List<Fighter> targets = new ArrayList<Fighter>();
		for(Fighter targetfighter : battle.getFighterobjects().values())
		{
			if(filter.fighterIds.size() != 0)
			{
				if(!filter.fighterIds.contains((Integer)targetfighter.getFighterId()))
					continue;
			}
			int nFightertype = targetfighter.getFighterBean().getFightertype();
			if(filter.monsterIds.size() != 0)
			{
				if(nFightertype < xbean.Fighter.FIGHTER_PARTNER)
					continue;
				xbean.Monster monsterbean = battle.getMonsters().get(targetfighter.getFighterId()); 
				if(!filter.monsterIds.contains((Integer)monsterbean.getMonsterid()))
					continue;
			}

			if(filter.specialType > 0 && nFightertype > xbean.Fighter.FIGHTER_PET)
			{
				xbean.Monster monsterbean = battle.getMonsters().get(targetfighter.getFighterId()); 
				if(filter.specialType != monsterbean.getSpecialtype())
					continue;//fail
			}
			
			if(targetfighter.getEffectRole().getLevel() < filter.minLevel)
				continue;
			if(targetfighter.getEffectRole().getLevel() > filter.maxLevel)
				continue;
			
			if(filter.hpPercentMin != -1)
			{
				if(targetfighter.getEffectRole().getHpPercent() < filter.hpPercentMin)
					continue;//fail
			}
			if(filter.hpPercentMax != -1)
			{
				if(targetfighter.getEffectRole().getHpPercent() > filter.hpPercentMax)
					continue;//fail
			}
			if(filter.mpPercentMin != -1)
			{
				if(targetfighter.getEffectRole().getMpPercent() < filter.mpPercentMin)
					continue;//fail
			}
			if(filter.mpPercentMax != -1)
			{
				if(targetfighter.getEffectRole().getMpPercent() > filter.mpPercentMax)
					continue;//fail
			}
			if(filter.relation != -1 && fighter!=null && !(fighter instanceof BattleFieldFighter))
			{
				ParseObjIDInBattle legalRelations = new ParseObjIDInBattle(filter.relation);
				int relation =  BattleField.getAimRelation(battle, fighter.getFighterId() , targetfighter.getFighterId());
				if(!legalRelations.validTarget(relation))
					continue;//关系不符
			}
			
			if(!(targetfighter instanceof BattleFieldFighter))
			{
				if(fighter!=null && !(fighter instanceof BattleFieldFighter))
				{
					if(filter.unitRelation.size() != 0)
					{
						boolean bvalid = false;
						for(int unitrelation : filter.unitRelation)
						{
							if(unitrelation == 0)
							{
								bvalid = true;
								break;
							}
								
							int relation = BattleAimRelation.COUPLE * (int) (Math.pow(2, unitrelation-1)); 
							int aimid = targetfighter.getFighterId();
							if(Math.abs(aimid-fighter.getFighterId()) < 14)
							{
								int relation2 = BattleField.getAimRelation(battle, fighter.getFighterId(), aimid);
								ParseObjIDInBattle objinbattle = new ParseObjIDInBattle(relation);
								if (objinbattle.validTarget(relation2))
								{
									bvalid = true;
									break;
								}
							}			
						}
						if(bvalid == false)
							continue;
					}
				}				
				boolean bunitcampCheck = false;
				
				boolean ishost = fighter.getFighterBean().getIshost();
				if(fighter.getFighterId() == 0)
					ishost = true;
				switch(filter.unitCamp)
				{
				case BattleAI.AI_CAMP_FRIEND: 
				case BattleAI.AI_CAMP_FRIEND_NOME:
					{
						if (ishost == targetfighter.getFighterBean().getIshost())
						{
							if(filter.unitCamp == BattleAI.AI_CAMP_FRIEND_NOME)
							{
								if(fighter.getFighterBean() != targetfighter.getFighterBean())
									bunitcampCheck = true;
							}	
							else
								bunitcampCheck = true;
						}
					}
					break;
				case BattleAI.AI_CAMP_ENEMY: 
					{
						if (ishost != targetfighter.getFighterBean().getIshost())
							bunitcampCheck = true;					
					}
					break;
				case BattleAI.AI_CAMP_SLEF: 
					{
						if (fighter.getFighterId() == targetfighter.getFighterId())
							bunitcampCheck = true;	
					}
					break;
				default:
					bunitcampCheck = true;	
					break;
				}
				if(bunitcampCheck == false)
					continue;
					
				if(filter.unitType.size() != 0)
				{
					boolean bvalid = false;
					for(int unittype : filter.unitType)
					{
						if (targetfighter.getFighterBean().getFightertype() == unittype)
						{
							if(filter.unitMonsterType > 0 && (unittype > xbean.Fighter.FIGHTER_PARTNER && unittype <= xbean.Fighter.FIGHTER_SYSTEM_PARTNER))
							{
								if(filter.unitMonsterType != targetfighter.getFighterBean().getSubtype())
									continue;
							}
							bvalid = true;
						}
					}
					if(bvalid == false)
						continue;
				}				
			}
			if(!filter.includeDead && !BattleField.checkLiveInBattle(battle, targetfighter.getFighterId()))
				continue;
			if(!filter.includeHide && fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_HIDDEN_FIRST)
					&& fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_HIDDEN_SECOND)
					&& fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ROLE_HIDDEN))
				continue;
			
			if(filter.resultType > 0)
			{
				int result = battle.getRoundresult().get(targetfighter.getFighterId());
				if((result & filter.resultType) == 0)
					continue;
			}
			
			if(filter.sex != -1 || filter.jobs.size()> 0)
			{
				if(targetfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_ROLE)
				{
					PropRole prole = new PropRole(targetfighter.getFighterBean().getUniqueid(), false);
					if(filter.sex != -1 && filter.sex != prole.getSex())
						continue;
					if(filter.jobs.size()> 0)
					{  
						boolean bCheck = false;
						for(int nschool:filter.jobs)
						{
							if(nschool== prole.getSchool())
							{
								bCheck = true;
								break;
							}
							
						}
						if(bCheck == false)
							continue;
					}
				}
				else
					continue;
			}
			
			if(filter.buffs.size() != 0 && filter.buffjs != null)
			{
				for(int buffId : filter.buffs)
				{
					if(buffId < 1000)
						battle.getEngine().put("_"+buffId+"_", targetfighter.getBuffAgent().existBuffByType(buffId));
					else
						battle.getEngine().put("_"+buffId+"_", targetfighter.getBuffAgent().existBuff(buffId));
				}
				if(!filter.buffjs.evalToBoolean(battle.getEngine(),null,null))
					continue;
				
			}
			targets.add(targetfighter);
		}
		//排序
		if(filter.sortAttrId != -1)
			FighterSorter.sortFightersByAttr(targets, filter.sortAttrId, filter.sortMaxMin, 0);
		
		List<Integer> targetIds = new LinkedList<Integer>();
		for(Fighter f : targets)
			targetIds.add(f.getFighterId());
		filter2targetIds.put(filter.id, targetIds);
		return targetIds;
	}

	
}
