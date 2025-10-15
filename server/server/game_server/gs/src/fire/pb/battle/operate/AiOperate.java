package fire.pb.battle.operate;

import java.util.ListIterator;

import fire.pb.battle.BattleField;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultType;
import fire.pb.battle.ai.AIOperation;
import fire.pb.option.BasicOperation;
/**ai动作*/
public class AiOperate extends FighterOperate
{

	public AiOperate(Fighter fighter)
	{
		super(fighter);
	}
	
	public AiOperate(Fighter fighter,AIOperation aiaction)
	{
		super(fighter);
		this.aiaction = new AIOperation(aiaction); 
	}

	private AIOperation aiaction = null;

	public AIOperation getAiAct()
	{
		return aiaction;
	}

	public void setAiAct(AIOperation aiaction)
	{
		this.aiaction = new AIOperation(aiaction); 
	}

	@Override
	public BasicOperation getBasicOperation(ListIterator<Operate> lit)
	{
		if (getFighter().getFightertype() == xbean.Fighter.FIGHTER_ROLE &&(getBattle().getRoundresult().get(getFighter().getFighterId()) & ResultType.RESULT_REST) != 0)
			return null;
		if(aiaction.isClientAction())
			getBeforeAIActions().add(new fire.pb.battle.AIOperation(getActseq(), -1, getFighter().getFighterId(), aiaction.getIndex()));
		BasicOperation basicOperation =  BattleField.aiActionToOperation(getBattle(), getFighter(), aiaction);
		return basicOperation;
	}

	@Override
	public boolean canActWhileBattleEnd()
	{
		if(aiaction.getOperationType() == OperationType.ACTION_SUMMON_INSTANT)//且是瞬时召唤
			return true;
		return false;
	}
	
}
