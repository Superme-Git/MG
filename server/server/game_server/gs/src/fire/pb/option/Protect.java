package fire.pb.option;

import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;

/**保护*/
public class Protect extends BasicOperation {

	public Protect(final xbean.BattleInfo battle, final int operator, final int aim){
		super(battle, operator, aim);
	}
	@Override
	public int getOperateID() {return 0;}

	public ResultItem getResultItem(){return null;}
	
	@Override
	public  java.util.LinkedList<fire.pb.battle.DemoResult> getDemoResult(){
		return null;
	}

	@Override
	public boolean checkEffective()
	{	//保护在回合开始便已执行
		return false;
	}
	
	@Override
	public int getResultType() {return 0;}

	@Override
	public int getType() {return OperationType.ACTION_PROTECT;}

	@Override
	public void process(){
	}
	
	public String toString(){
		return getOperator() + "\t保护\t" + getAim();
	}
	
	@Override
	public boolean canChangeAim(){return false;}
	
}
