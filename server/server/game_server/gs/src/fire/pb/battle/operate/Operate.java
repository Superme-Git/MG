package fire.pb.battle.operate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import fire.pb.battle.BattleSequenceHelper;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SubResultItem;

/**动作基类*/
public abstract class Operate
{
	protected int actseq = -1;
	private xbean.BattleInfo battle;
	private List<fire.pb.battle.AIOperation> clientAIActions;
	private List<fire.pb.battle.AIOperation> beforeAIActions = new LinkedList<fire.pb.battle.AIOperation>();
	private List<ResultItem> script;
	private List<xbean.Fighter> sequence;
	
	public xbean.BattleInfo getBattle()
	{
		return battle;
	}
	public void setBattle(xbean.BattleInfo battle)
	{
		this.battle = battle;
	}
	public void setActseq(int actseq)
	{
		this.actseq = actseq;
	}
	public int getActseq()
	{
		return actseq;
	}
	public void setScript(List<ResultItem> script)
	{
		this.script = script;
	}
	public List<ResultItem> getScript()
	{
		return script;
	}
	public List<fire.pb.battle.AIOperation> getClientAIActions()
	{
		return clientAIActions;
	}
	public void setClientAIActions(List<fire.pb.battle.AIOperation> clientAIActions)
	{
		this.clientAIActions = clientAIActions;
	}
	public List<fire.pb.battle.AIOperation> getBeforeAIActions()
	{
		return beforeAIActions;
	}
	public void setBeforeAIActions(List<fire.pb.battle.AIOperation> beforeAIActions)
	{
		this.beforeAIActions = beforeAIActions;
	}
	/**拷贝环境变量到目标*/
	public void copyEnvParamsTo(Operate target)
	{
		target.setActseq(this.getActseq());
		target.setBattle(this.getBattle());
		target.setClientAIActions(this.getClientAIActions());
		target.setBeforeAIActions(this.getBeforeAIActions());
		target.setScript(this.getScript());
		target.setOriginSequence(this.getOriginSequence());
	}
	
	public List<xbean.Fighter> getOriginSequence()
	{
		return sequence;
	}
	public void setOriginSequence(List<xbean.Fighter> sequence)
	{
		this.sequence = sequence;
	}
	public abstract ResultItem process(ListIterator<Operate> lit);
	
	public boolean canActWhileBattleEnd(){return false;};
	
	/**成功时的处理*/
	public void onProcessSucc(ResultItem item,ListIterator<Operate> lit)
	{
		// 行动成功
		script.add(item);

		// 遍历验证上个脚本里是否有人复活
		checkRelive(lit, item);

		// 行动正常执行，将行动前AI动作加入
		clientAIActions.addAll(beforeAIActions);

		// 处理行动后的AI触发
		clientAIActions.addAll(BattleSequenceHelper.processAIActionAfterOperation(battle, lit, actseq, item));

		// 存储本次行动中各战斗者的属性buff变化
		for (Fighter ofighter : battle.getFighterobjects().values())
		{
			ofighter.storeActionAttrsChange(actseq);
		}
	}
	
	/*复活检查及处理*/
	protected void checkRelive(ListIterator<Operate> lit, ResultItem lastResult)
	{
		List<Operate> extActs = new ArrayList<Operate>();
		Fighter curfighter = battle.getFighterobjects().get(lastResult.execute.attackerid);
		if(curfighter == null)
			return;
		for(SubResultItem subresult : lastResult.subresultlist)
		{
			for(DemoResult demo : subresult.resultlist)
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
		BattleSequenceHelper.insertExtraActs(battle, lit ,curfighter, extActs);
	}
	
}
