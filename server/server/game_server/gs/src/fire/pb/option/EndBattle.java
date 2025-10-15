package fire.pb.option;

import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;

/**战斗结束*/
public class EndBattle extends BasicOperation {

	public EndBattle(final xbean.BattleInfo battle, final int operator, final int result){
		super(battle, operator, operator);
		this.result = result;
	}
	int result = 0;
	@Override
	public int getOperateID() {return result;}

	public ResultItem getResultItem(){return null;}
	
	
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
	public int getType() {return OperationType.ACTION_BATTLE_END;}

	@Override
	public void process(){
		getBattle().setBattleresult(result);
		
		return;
	}
	
	public String toString(){
		return getOperator() + "\t结束战斗\t";
	}
	
	@Override
	public boolean canChangeAim(){return false;}
}
