package fire.pb.option;

import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.SubResultItem;

/**休息*/
public class Rest extends BasicOperation {

	public Rest(final xbean.BattleInfo battle, final int operator){
		super(battle, operator, operator);
	}
	@Override
	public int getOperateID() {return 0;}

	public ResultItem getResultItem()
	{
		if(resultItem != null)
		{
			resultItem.execute.operationtype = OperationType.ACTION_REST;
			resultItem.execute.attackerid = getOperator();
			resultItem.execute.operationid = getOperateID();
			SubResultItem subitem = new SubResultItem();
			subitem.subskillid = 0;
			resultItem.subresultlist.add(subitem);
			return resultItem;
		}
		return null;
	}
	
	
	@Override
	public boolean checkEffective()
	{
		return super.checkEffective();
	}
	
	@Override
	public  java.util.LinkedList<fire.pb.battle.DemoResult> getDemoResult(){
		return null;
	}	
	@Override
	public int getResultType() {return 0;}

	@Override
	public int getType() {return OperationType.ACTION_REST;}

	@Override
	public void process(){
		return;
	}
	
	public String toString(){
		return getOperator() + "\t休息或待机\t";
	}
	
	@Override
	public boolean canChangeAim(){return false;}
}
