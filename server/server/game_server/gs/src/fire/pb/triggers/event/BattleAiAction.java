package fire.pb.triggers.event;

import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public class BattleAiAction extends TriggerEvent
{

	public BattleAiAction(STriggerCondition scondition)
	{
		super(scondition);
	}

	@Override
	public boolean triggered(TriggerRole triggerRole,List<Integer> args)
	{
		return true;
	}

	@Override
	public int getType()
	{
		return TriggerEvent.TRIGGER_TYPE_BATTLE_AI;
	}

}
