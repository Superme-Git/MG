package fire.pb.battle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import fire.pb.battle.ai.BattleAIManager;
import fire.pb.battle.operate.AiOperate;
import fire.pb.battle.operate.DecisionOperate;
import fire.pb.battle.operate.FighterOperate;
import fire.pb.battle.operate.Operate;
import fire.pb.skill.Module;
import fire.pb.skill.fight.FightSkillConfig;

public class BattleSequenceHelper
{
	/**回合开始前检测，生成客户端aiaction，并将添加的服务器AI动作者插入序列*/
	public static LinkedList<fire.pb.battle.AIOperation> processAIActionWhileRoundStart(xbean.BattleInfo battle,List<Operate> sequence)
	{
		
		//有回合开始服务器动作的FighterId
		List<AiOperate> extacts = new ArrayList<AiOperate>();
		
		//检查本回合开始是否有服务器AI动作
		LinkedList<fire.pb.battle.AIOperation> clientActions = BattleAIManager.getInstance().getActionWhileRoundStart(battle,extacts);
		
		//从出手序列的第一个位置开始倒序插入，以保证顺序
		for(int i = extacts.size()-1 ; i>=0 ; i--)
		{
			Operate act = extacts.get(i);
			sequence.add(0, act);//插入fighter
		}
		
		//检查本回合开始是否有服务器AI动作
		extacts.clear();
		clientActions.addAll(BattleAIManager.getInstance().getRoleActionWhileRoundStart(battle,extacts));
		List<Integer> roleids = new ArrayList<Integer>();
		for(int i = 0 ; i<extacts.size() ; i++)
		{
			AiOperate act = extacts.get(i);
			if(roleids.contains(act.getFighter().getFighterId()))
				continue;
			if(act.getFighter().isRole())
			{//有角色预置ai取消防御姿态
				xbean.Decision decision = battle.getDecisions().get(act.getFighter().getFighterId());
				if(decision!=null)
				{
					if(decision.getIsauto() == false)
						continue;
					if(act.getAiAct().getOperationType() == OperationType.ACTION_SKILL||act.getAiAct().getOperationType() == OperationType.ACTION_ATTACK)
					{
						FightSkillConfig battleSkillConfig = fire.pb.skill.Module.getInstance().getFightSkillConfig(act.getAiAct().getOperationId());
						if (battleSkillConfig == null)
						{
							Module.logger.error("ERROR: Battle Skill is not exist. skillId = " + act.getAiAct().getOperationId());
							throw new IllegalArgumentException("ERROR: Battle Skill is not exist. skillId = " + act.getAiAct().getOperationId());
						}
						if(fire.pb.battle.DecisionProcesser.checkMonsterHpMpConsume(battleSkillConfig, battle, act.getFighter()))
						{
							if(decision.getIsauto() == true && decision.getOperatetype() == OperationType.ACTION_DEFEND)
							{
								battle.getDecisions().remove(act.getFighter().getFighterId(), decision);
							}
						}
						else
							continue;
					}
				}
			}
/*			List<Operate> extroleActs = new ArrayList<Operate>();
			extroleActs.add(act);
			Iterator<Operate> it = sequence.iterator();
			ListIterator<Operate> lit = (ListIterator<Operate>) it;
*/		
			boolean isinsert = false;
			ListIterator<Operate> lit = sequence.listIterator();
			for (; lit.hasNext();)
			{
				Operate opt = lit.next();
				if(((FighterOperate)opt ).getFighter().getFighterId() == act.getFighter().getFighterId())
				{
					isinsert = true;
					if(lit.hasPrevious())
						lit.previous();					
					lit.add(act);
					//BattleSequenceHelper.insertExtraActs(battle, lit, act.getFighter(), extroleActs);
					roleids.add(act.getFighter().getFighterId());
					break;
				}
			}
			if(isinsert == false)
			{
				sequence.add(0, act);
				//BattleSequenceHelper.insertExtraActs(battle, sequence.listIterator(), act.getFighter(), extroleActs);				
			}
//			sequence.add(0, act);//插入fighter
		}
		return clientActions;
	}
	
	/**行动前检测，生成客户端aiaction，并将添加的服务器AI动作者插入序列*/
	public static List<fire.pb.battle.AIOperation> processAIActionBeforeOperation(final xbean.BattleInfo battle,final ListIterator<Operate> lit, List<AiOperate> extActs, final int actcount,final Fighter curfighter)
	{
		//List<Operate> extActs = new ArrayList<Operate>();//有回合开始服务器动作的FighterId
		List<fire.pb.battle.AIOperation> clientActions = BattleAIManager.getInstance().getActionBeforeOperation(battle,extActs , actcount,curfighter);
		return clientActions;
	}
	
	/**回合结束检测，生成客户端aiaction，并将添加的服务器AI动作者插入序列*/
	public static List<fire.pb.battle.AIOperation> processAIActionRoundEnd(final xbean.BattleInfo battle,final ListIterator<Operate> lit, final int actcount)
	{
		List<AiOperate> extActs = new ArrayList<AiOperate>();//有回合开始服务器动作的FighterId
		List<fire.pb.battle.AIOperation> clientActions = new LinkedList<fire.pb.battle.AIOperation>();//客户端AI动作
		clientActions.addAll(BattleAIManager.getInstance().getActionRoundEnd(battle,extActs,actcount, battle.getFieldfighter()));
		for(Fighter fighter : battle.getFighterobjects().values())
		{
			List<AiOperate> extActs2 = new ArrayList<AiOperate>();//有回合开始服务器动作的FighterId
			if(fighter.getAiagent()==null)
				continue;
			if(fighter.getFightertype() != xbean.Fighter.FIGHTER_ROLE)
			{	
				clientActions.addAll(BattleAIManager.getInstance().getActionRoundEnd(battle,extActs2,actcount, fighter));
				if(extActs2.size() > 1)
				{
					ListIterator<AiOperate> litopt = extActs2.listIterator();
					AiOperate root = null;
					for (; litopt.hasNext();)
					{
						AiOperate opt = litopt.next();
						if(opt.getAiAct().getOperationType() == OperationType.ACTION_SUMMON_INSTANT)
						{
							if(root == null)
								root = opt;
							else
							{
								root.getAiAct().getSummonMaps().putAll(opt.getAiAct().getSummonMaps());
								litopt.remove();
							}
						}
					}
					
				}
				if(extActs2.size() > 0)
					insertExtraActs(battle,lit, fighter, extActs2);
			}
		}
		return clientActions;
	}	
	
	/**当行动者的行动是防御或者保护时（客户端不播放动作），将生成的AIAction放入前一个行动者之后*/
	public static List<fire.pb.battle.AIOperation> processAIActionsForDefendAndProtect(final List<ResultItem> script,final List<fire.pb.battle.AIOperation> beforeAIActions)
	{
		if(script.size() != 0)
		{
			//把保护和防御行动前面的客户端AIAction放到前一个行动的最后执行
			for(fire.pb.battle.AIOperation aiaction : beforeAIActions)
			{
				aiaction.actionseq = aiaction.actionseq - 1;
				aiaction.actionmoment = -1;
			}
		}
		//如果没有前一个行动（防御者是第一个行动，则把这些AIActions插入到后一个行动者的前面，当也没有后一个行动者时，整场战斗没有行动，不播放）
		return beforeAIActions;
	}

	/**行动后检测，生成客户端aiaction，并将添加的服务器AI动作者插入序列*/
	public static List<fire.pb.battle.AIOperation> processAIActionAfterOperation(final xbean.BattleInfo battle,final ListIterator<Operate> lit,final int actcount,final ResultItem lastResult)
	{
		Fighter curfighter = null;
		if(lastResult.execute.attackerid == 0)
			curfighter = battle.getFieldfighter();
		else
			curfighter = battle.getFighterobjects().get(lastResult.execute.attackerid);
		if(actcount == -1 || curfighter == null)
			return new LinkedList<fire.pb.battle.AIOperation>();
		List<AiOperate> extActs = new ArrayList<AiOperate>();//有回合开始服务器动作的FighterId
		List<fire.pb.battle.AIOperation> clientActions = BattleAIManager.getInstance().getActionAfterOperation(battle,extActs , actcount,lastResult);
		
		//将生成的新服务器动作插入序列中
		insertExtraActs(battle,lit, curfighter, extActs);

		return clientActions;
	}
	
	/**处理前面因为死亡跳过出手，复活后要马上出手的战斗者*/
	public static void processRelive(xbean.BattleInfo battle, ListIterator<Operate> lit, ResultItem lastResult)
	{
		List<Operate> extActs = new ArrayList<Operate>();
		Fighter curfighter = battle.getFighterobjects().get(lastResult.execute.attackerid);
		if(curfighter == null)
			return;
		for(SubResultItem subresult : lastResult.subresultlist)
		{	for(DemoResult demo : subresult.resultlist)
			{
				if ((demo.targetresult & ResultType.RESULT_RELIVE) != 0)
				{//有人站起来了！
					if(battle.getSkipeddeadfighters().contains(demo.targetid))
					{
						//因为死亡跳过出手
						Fighter relivefighter = battle.getFighterobjects().get(demo.targetid);
						if(relivefighter == null)
							continue;
						xbean.Decision decision = battle.getDecisions().get(demo.targetid);
						if(decision == null)
							continue;
						extActs.add(new DecisionOperate(relivefighter,decision));
						battle.getSkipeddeadfighters().remove((Integer)demo.targetid);
					}
				}
			}
		}
		insertExtraActs(battle, lit ,curfighter, extActs);
	}
	
	/**将刚生成的额外的战斗者动作插入当前战斗者的动作之后*/
	public static void insertExtraActs(xbean.BattleInfo battle,ListIterator<Operate> lit, Fighter curfighter,  List<? extends Operate> extActs)
	{
		if (curfighter == null)
			return;
		if (extActs.isEmpty())
			return;
		// 注意！此处对迭代器操作复杂，可能引起bug
		// 要把动作插入到当前动作者的所有连续动作之后

		int movecount = 0;// 迭代器移动次数
		// 迭代器移动到当前fighter连续动作的最后一个
		if (lit.hasPrevious())// 如果不是一个序列的开始
		{
			while (lit.hasNext())// 迭代器while，序列有限，能保证不会死循环
			{
				Operate act = lit.next();
				if (!(act instanceof FighterOperate) || ((FighterOperate) act).getFighter().getFighterId() != curfighter.getFighterId())
				{
					lit.previous();
					break;
				}
				movecount++;
			}
		}
		// 正序插入
		for (Operate act : extActs)// 可能将一次动作替换为多个AIAction
		{
			lit.add(act);
			movecount++;
			// }
		}
		// 倒退回去
		for (int i = 0; i < movecount; i++)
		{
			lit.previous();
		}
	}
}
