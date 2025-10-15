package fire.pb.option;

import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;

/**改变环境*/
public class ChangeEnvironment extends BasicOperation {

	public ChangeEnvironment(final xbean.BattleInfo battle, final int operator, final int envId){
		super(battle, operator, operator);
		this.envId = envId;
	}
	int envId = 0;
	@Override
	public int getOperateID() {return envId;}

	public ResultItem getResultItem()
	{
		if(isFailed)
			return null;
		ResultItem item = new ResultItem();
		item.execute.attackerid = getOperator();
		item.execute.operationtype = getType();
		item.execute.operationid = getOperateID();
		return item;
	}
	
	
	@Override
	public boolean checkEffective()
	{
		return true;
	}
	
	@Override
	public  java.util.LinkedList<fire.pb.battle.DemoResult> getDemoResult(){
		return null;
	}	
	@Override
	public int getResultType() {return 0;}

	@Override
	public int getType() {return OperationType.ACTION_ENVIRONMENTCHANGE;}

	@Override
	public void process(){
		//TODO 验证ID的正确性
		
		if(getBattle().getEnvironment() == getOperateID())
		{
			isFailed = true;
			return;
		}
		return;
	}
	
	public String toString(){
		return getOperator() + "\t改变战场环境为\t" + getOperateID();
	}
	
	@Override
	public boolean canChangeAim(){return false;}
}
