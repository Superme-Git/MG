package fire.pb.battle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import fire.pb.battle.operate.AiOperate;
import fire.pb.battle.operate.DecisionOperate;
import fire.pb.battle.operate.DoubleSkillOperate;
import fire.pb.battle.operate.Operate;
import fire.pb.battle.operate.ReliveFighterOperate;
import fire.pb.battle.operate.RoundEndOperate;
import fire.pb.buff.BuffConstant;
import fire.pb.skill.SkillConstant;
import fire.pb.talk.MessageMgr;
import fire.pb.util.Parser;

public class BattleScriptProcessor
{
	/**根据战斗者序列获取最初的行动序列*/
	static List<Operate> getOriginActs(xbean.BattleInfo battle, List<xbean.Fighter> sequence)
	{
		List<Operate> acts = new LinkedList<Operate>();
		for(xbean.Fighter xfighter : sequence)
		{
			int fighterId = xfighter.getBattleindex();
			Fighter fighter = BattleField.getFighter(battle, fighterId);
			xbean.Decision decision = battle.getDecisions().get(fighterId);
			DecisionOperate act = new DecisionOperate(fighter);
			act.setDecision(decision);
			acts.add(act);
		}
		acts.add(new ReliveFighterOperate());
		acts.add(new RoundEndOperate());
		return acts;
	}
	
	/**获取本回合剧本*/
	static java.util.List<ResultItem> getScript(final xbean.BattleInfo battle, final List<fire.pb.battle.AIOperation> clientAIActions)
	{
		//设置连击初始值
		for(Fighter fighter : battle.getFighterobjects().values())
			fighter.setDoubleSkill(false);
		
		final java.util.List<ResultItem> script = new java.util.LinkedList<ResultItem>();
		
		//战斗类型放入JS引擎
		battle.getEngine().setBattleType(battle.getBattletype());
		
		final List<xbean.Fighter> sequence = new java.util.LinkedList<xbean.Fighter>();
		for (xbean.Fighter fighter : battle.getFighters().values()) {
			int index=fighter.getBattleindex();
			if(!BattleField.checkOutBattle(battle, index))
				sequence.add(fighter);
		}
		// 对战斗单位进行排序
		BattleField.sort(sequence,battle);	
		Integer actcount = 0;//行动次数，一回合最多出手不超过80次，防止死循环
		List<Operate> actsequence = getOriginActs(battle, sequence);
		//处理回合开始的战斗AI
		clientAIActions.addAll(BattleSequenceHelper.processAIActionWhileRoundStart(battle, actsequence));
		
		if(battle.getRound() == 1)//第一回合，也就是战斗开始
		{
			Iterator<Operate> it = actsequence.iterator();
			ListIterator<Operate> lit = (ListIterator<Operate>) it;
			for(Fighter f : battle.getFighterobjects().values())
			{
				int nskill = 0;
				if(f.getSkillAgent().hasSkill(SkillConstant.BATTLE_ROLE_BEHUNTER))
					nskill = SkillConstant.BATTLE_ROLE_BEHUNTER;
				else if(f.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_BEHUNTER_1))
					nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_1;
				else if(f.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_BEHUNTER_2))
					nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_2;
				else if(f.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_BEHUNTER_3))
					nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_3;
				else if(f.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_BEHUNTER_4))
					nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_4;

				if(nskill != 0)
				{
					List<Operate> extActs = new ArrayList<Operate>();
					xbean.Decision decision = xbean.Pod.newDecision();
					
					decision.setAim(f.getFighterId());
					decision.setOperateid(nskill);
					decision.setOperatetype(OperationType.ACTION_SKILL);
					decision.setOperator(f.getFighterId());		
					
					extActs.add(new DoubleSkillOperate(f, decision));
					BattleSequenceHelper.insertExtraActs(battle, lit, f, extActs);
				}
				if(f.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PASSIVE_HIDDEN_FIRST ))
				{
					List<Operate> extActs = new ArrayList<Operate>();
					xbean.Decision decision = xbean.Pod.newDecision();
					decision.setAim(f.getFighterId());
					decision.setOperateid(SkillConstant.BATTLE_PET_HIDDEN_FIRST);
					decision.setOperatetype(OperationType.ACTION_SKILL);
					decision.setOperator(f.getFighterId());		
					
					extActs.add(new DoubleSkillOperate(f, decision));
					BattleSequenceHelper.insertExtraActs(battle, lit, f, extActs);
				}
				else if(f.getBuffAgent().existBuff(BuffConstant.CONTINUAL_PASSIVE_HIDDEN_SECOND ))
				{
					List<Operate> extActs = new ArrayList<Operate>();
					xbean.Decision decision = xbean.Pod.newDecision();

					decision.setAim(f.getFighterId());
					decision.setOperateid(SkillConstant.BATTLE_PET_HIDDEN_SECOND);
					decision.setOperatetype(OperationType.ACTION_SKILL);
					decision.setOperator(f.getFighterId());		
					
					extActs.add(new DoubleSkillOperate(f, decision));
					BattleSequenceHelper.insertExtraActs(battle, lit, f, extActs);
				}
			}
		}
		//！！进入战斗序列，开始生成战斗脚本
		boolean isfirst = true;
		for(Iterator<Operate> it = actsequence.iterator();it.hasNext();)
		{
			ListIterator<Operate> lit = (ListIterator<Operate>) it;
			Operate act = lit.next();
			try
			{
				//验证在战斗结束的情况下，还能否执行
				if(battle.getBattleresult() != BattleField.BATTLE_NOT_END && !act.canActWhileBattleEnd())
					continue;
				//设置环境变量
				act.setActseq(actcount);
				act.setBattle(battle);
				act.setClientAIActions(clientAIActions);
				act.setScript(script);
				act.setOriginSequence(sequence);
				
				ResultItem item = act.process(lit);
				
				//行动后的后续处理
				if (null != item)
				{
					act.onProcessSucc(item, lit);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			} 
			// 如果战斗可以结束
			if(battle.getBattleresult() == BattleField.BATTLE_NOT_END || act.canActWhileBattleEnd())
				battle.setBattleresult(BattleField.checkBattleEnd(battle));
			
			actcount = script.size();
			if(actcount>=80)//FIXME，约束一回合的总出手次数，防止出错。调试期间，暂设置80。
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("BUG! 一回合内行动次数超过80次");}
				break;
			}
			if(isfirst == true&&!it.hasNext())
			{
				List<Integer> fighterids = new ArrayList<Integer>();

				for(Fighter fighter : battle.getFighterobjects().values())
				{
					if(fighter.getFightertype() > xbean.Fighter.FIGHTER_PET&&
					((battle.getRoundresult().get(fighter.getFighterId())&(ResultType.RESULT_KICKOUT|ResultType.RESULT_RUNAWAY|ResultType.RESULT_SEIZE|ResultType.RESULT_SUMMONBACK)) > 0))
					{
						fighterids.add(fighter.getFighterId());
					}
				}
				//处理失败的怪物（包括主客方）
				BattleField.processFaileMonsters(battle,true);

				for(int fighterid:fighterids)
				{
					battle.getFighters().remove(fighterid);
					battle.getFighterobjects().remove(fighterid);
				}
				isfirst = false;
				clientAIActions.addAll(BattleSequenceHelper.processAIActionRoundEnd(battle, lit,actcount-1));
			}
		}
		//检测回合数，以防两方都没有出手而导致超过200回合不结束
		if (battle.getBattleresult() == BattleField.BATTLE_NOT_END && battle.getRound() >= xbean.BattleInfo.MAX_ROUNDNUM)
		{
			if(battle.getBattletype() % 10 == 0)
			{
				battle.setBattleresult(BattleField.BATTLE_LOSE);//PVE战斗算输
				for(long roleId : battle.getRoleids().keySet())
					MessageMgr.psendMsgNotifyWhileCommit(roleId, 141781, null);
			}
			else
			{
				battle.setBattleresult(BattleField.BATTLE_DRAW);//PVP战斗算平
				for(long roleId : battle.getRoleids().keySet())
					MessageMgr.psendMsgNotifyWhileCommit(roleId, 141781, null);
			}
		}
		
		if(battle.getBattleresult() != BattleField.BATTLE_NOT_END && script.size() > 0)
		{
			ResultItem lastitem =  script.get(script.size()-1);			
			DemoResult battleendresult = new DemoResult();
			if(lastitem.subresultlist.size() > 0)
			{
				battleendresult.resulttype = 5;
				SubResultItem lastsubitem = lastitem.subresultlist.get(lastitem.subresultlist.size()-1);
				if(lastsubitem!=null)
					lastsubitem.resultlist.add(battleendresult);
			}
		}
		return script;
	}

}
