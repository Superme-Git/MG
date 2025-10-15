package fire.pb.option;

import java.util.List;

import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SubResultItem;
import fire.pb.battle.ai.AIOperation;

/**回收怪物，只用于AI*/
public class SummonBack extends BasicOperation {

	final private AIOperation aiaction;
	
	public SummonBack(final xbean.BattleInfo battle, final int operator, AIOperation aiaction){
		super(battle, operator, operator);
		this.aiaction = aiaction;
	}
	
	
	public ResultItem getResultItem(){
		return resultItem;
		
	}
	
	public boolean checkOperator()
	{
		if(BattleField.checkOutBattle(getBattle(), getOperator()))
			return false;//如果怪物已经不在战斗中了，不能召回
		return true;
	}

	@Override
	public int getResultType() {return ResultType.RESULT_SUMMONBACK;}

	@Override
	public int getType() {return OperationType.ACTION_WITHDRAW;}

	@Override
	public void process(){
		
		resultItem = null;
		if(opfighter == null)
			return;
		List<Integer> targets = opfighter.getAiagent().getTargets(aiaction.getGoal());
		if(targets.isEmpty())
			return;
		resultItem = new ResultItem();
		resultItem.execute.attackerid = getOperator();
		resultItem.execute.operationtype = getType();
		
		for(int target : targets)
		{
			Fighter fighter = getBattle().getFighterobjects().get(target);
			if(fighter == null)
				continue;
			if(BattleField.checkOutBattle(getBattle(), target))
				continue;//已经不在战斗中了
			SubResultItem subitem = new SubResultItem();			
			DemoResult demo = new DemoResult();
			demo.targetid = target;
			demo.targetresult = getResultType();
			subitem.resultlist.add(demo);
			resultItem.subresultlist.add(subitem);
		}
		if(resultItem.subresultlist.isEmpty())
			resultItem = null;
	}
		
	public String toString(){
		return getOperator() + "\t召回怪物\t";
	}
	
	
	@Override
	protected boolean checkAim()
	{
		return true;
	}

	@Override
	public boolean canChangeAim()
	{
		return false;
	}

	@Override
	public int getOperateID()
	{
		return 0;
	}
}
