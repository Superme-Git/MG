package fire.pb.battle.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import fire.pb.battle.BattleField;
import fire.pb.battle.BattleSequenceHelper;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SubResultItem;
import fire.pb.battle.ai.BattleAIManager;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.DecisionBuff;
import fire.pb.option.BasicOperation;
import fire.pb.skill.Module;
import fire.pb.skill.fight.FightSkillConfig;
/**基本决定产生的动作*/
public class DecisionOperate extends FighterOperate
{
	public DecisionOperate(Fighter fighter)
	{
		super(fighter);
	}
	public DecisionOperate(Fighter fighter, xbean.Decision decision)
	{
		super(fighter);
		this.decision = decision;
	}

	private xbean.Decision decision = null;

	public xbean.Decision getDecision()
	{
		return decision;
	}

	public void setDecision(xbean.Decision decision)
	{
		this.decision = decision;
	}
	@Override
	public BasicOperation getBasicOperation(ListIterator<Operate> lit)
	{
		// 如果没有服务器AI动作，则是怪物正常的出手时机，先检查出手前AI
		FighterOperate curact = this;
		List<AiOperate> extActs = new ArrayList<AiOperate>();
		
		if(getFighter().getFighterId() == 1)
			getClientAIActions().addAll(BattleSequenceHelper.processAIActionBeforeOperation(getBattle(), lit, extActs,getActseq(), getBattle().getFieldfighter()));			

		if (!(curact instanceof DoubleSkillOperate)) //
		{
			DecisionBuff dbuff = getFighter().getDecisonBuff();
			if (dbuff != null&& dbuff.getDecision() != null && decision != null)
			{
				if(!(decision.getOperatetype() == OperationType.ACTION_SUMMON && ((DecisionBuff) dbuff).getDecision().getOperatetype() == OperationType.ACTION_REST))
				{
					decision = ((DecisionBuff) dbuff).getDecision();
					if(decision != null && decision.getOperator() == 0)
						decision.setOperator(getFighter().getFighterId());
				}
			}
			else if(decision != null && getFighter().getFightertype() == xbean.Fighter.FIGHTER_ROLE && (
					(decision.getIsauto() == false||(decision.getIsauto() == true && (decision.getOperatetype() == OperationType.ACTION_CATHCH/*||decision.getOperatetype() == OperationType.ACTION_DEFEND*/)))))
			{				
				if(decision.getIsauto()&&decision.getOperatetype() == OperationType.ACTION_CATHCH)
					if(!BattleField.checkLiveInBattle(getBattle(),decision.getAim()))
						BattleField.dealBabyFight(getBattle(), getFighter(),decision,false);
			}
			else
			{				
				getBeforeAIActions().addAll(BattleSequenceHelper.processAIActionBeforeOperation(getBattle(), lit, extActs,getActseq(), getFighter()));
				if(!extActs.isEmpty())//如果出手前触发AI by changhao
				{
		  			for(AiOperate aiact : extActs)
					{
						if(aiact==null)
							continue;
						xbean.Decision decision = BattleField.aiActionToDecision(getBattle(), getFighter(), aiact.getAiAct());
						if(decision.getOperatetype() == OperationType.ACTION_SKILL)//使用技能 by changhao
						{
							FightSkillConfig battleSkillConfig = fire.pb.skill.Module.getInstance().getFightSkillConfig(decision.getOperateid());
							if (battleSkillConfig == null)
							{
								Module.logger.error("ERROR: Battle Skill is not exist. skillId = " + decision.getOperateid()+" fighterid = " +getFighter().getFighterBean().getUniqueid()+" fightertype = " +getFighter().getFighterBean().getFightertype()+" aiact.id = " +aiact.getAiAct().getIndex());
								throw new IllegalArgumentException("ERROR: Battle Skill is not exist. skillId = " + decision.getOperateid()+"aiact.id = " +aiact.getAiAct().getIndex());
							}
							if(!fire.pb.battle.DecisionProcesser.checkMonsterHpMpConsume(battleSkillConfig, getBattle(), getFighter()))
								continue;
							else
							{
								curact = aiact;
								break;
							}
						}
						else if(aiact.getAiAct()!=null && aiact.getAiAct().getOperationType()==OperationType.ACTION_SUMMON_INSTANT)
						{
							if(this.decision.getOperatetype()!=OperationType.ACTION_SUMMON_INSTANT)
							{
								lit.add(aiact);
								lit.previous();
							}
							break;
						}
						else
						{
							curact = aiact;
							break;
						}
					}
				}
			}			
		}
		
 		if(curact instanceof DecisionOperate)
		{// 如果是普通攻击判断是否已经出过手（合击出手）
			if(((DecisionOperate)curact).getDecision() == null)
				return null;
			if ((!(curact instanceof DoubleSkillOperate))&&(getBattle().getRoundresult().get(getFighter().getFighterId()) & ResultType.RESULT_REST) != 0)
				return null;
			// 验证战斗者是否因为死亡而跳过出手，为了死人爬起来还要出手
			if (getFighter().getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH))
			{
				getBattle().getSkipeddeadfighters().add(getFighter().getFighterId());
				return null;
			}
			return BattleField.decisionToAction(getBattle(), getFighter(), decision);
		}
		else if(curact instanceof AiOperate)
		{
			this.copyEnvParamsTo(curact);
			return curact.getBasicOperation( lit );
		}
		else
			return null;
	}
	
	@Override
	public ResultItem process(ListIterator<Operate> lit)
	{
		ResultItem item = super.process(lit);
		//为失败的自动释放技能而处理
		return processFailedSkill(item);
	}
	
	@Override
	public void onProcessSucc( ResultItem item, ListIterator<Operate> lit)
	{
		super.onProcessSucc(item, lit);
		//检查是否触发了法术连击
		if(getFighter().isDoubleSkill())
		{
			List<Operate> extActs = new ArrayList<Operate>();
			xbean.Decision  decision = getDecision();
			
			int skillid = item.execute.operationid;
			
			if (item.subresultlist != null)
			{
				fire.pb.battle.SubResultItem it = item.subresultlist.get(0);
				if (it != null)
				{
					fire.pb.battle.DemoResult result = it.resultlist.get(0);
					if (result != null)
					{
						decision.setAim(result.targetid);					
					}
				}
			}
			
			decision.setOperateid(skillid);
			extActs.add(new DoubleSkillOperate(getFighter(), decision));
			BattleSequenceHelper.insertExtraActs(getBattle(), lit, getFighter(), extActs);
		}
	}
	
	private ResultItem processFailedSkill(ResultItem item )
	{
/*		if(item == null)
			return item;
		if(item.execute.operationtype != OperationType.ACTION_FAILURE)
			return item;
*/		return item;
	}
	
	@Override
	protected void checkRelive(ListIterator<Operate> lit, ResultItem lastResult)
	{
		Fighter curfighter = getBattle().getFighterobjects().get(lastResult.execute.attackerid);
		if(curfighter == null)
			return;
		for(SubResultItem subresult : lastResult.subresultlist)
		{
			for(DemoResult demo : subresult.resultlist)
			{
				if ((demo.targetresult & ResultType.RESULT_RELIVE) != 0)
				{//有人站起来了！
					if(getBattle().getSkipeddeadfighters().contains(demo.targetid))
					{
						//因为死亡跳过出手
						Fighter relivefighter = getBattle().getFighterobjects().get(demo.targetid);
						if(relivefighter == null)
							continue;
						xbean.Decision decision = getBattle().getDecisions().get(demo.targetid);
						if(decision == null)
							continue;
						getBattle().getReliveacts().add(new DecisionOperate(relivefighter,decision));
						getBattle().getSkipeddeadfighters().remove((Integer)demo.targetid);
					}
				}
			}
		}
	}
	
}
