package fire.pb.option;

import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;

/**防御*/
public class Defend extends BasicOperation {

	public Defend(final xbean.BattleInfo battle, final int operator){
		super(battle, operator, operator);
	}
	@Override
	public int getOperateID() {return 0;}

	public ResultItem getResultItem(){return null;}
	
	@Override
	public java.util.LinkedList<fire.pb.battle.DemoResult> getDemoResult(){
		return null;
	}
	
	@Override
	public boolean checkEffective()
	{//防御在回合开始便已执行
		return false;
	}
	
	@Override
	public void process(){}
	
	@Override
	public int getResultType() {return ResultType.RESULT_DEFENCE;}

	@Override
	public int getType() {return OperationType.ACTION_DEFEND;}
	
	public String toString(){return getOperator() + "\t进行防御";}
	
	@Override
	public boolean canChangeAim(){return false;}
}
