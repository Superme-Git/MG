package fire.pb.triggers.event;

import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public class BattleWin extends TriggerEvent
{
	public BattleWin(STriggerCondition scondition)
	{
		super(scondition);
	}

	@Override
	public boolean triggered(TriggerRole triggerRole,List<Integer> args)
	{
		if(args.isEmpty())
			return false;
		return this.args.containsAll(args);
	}

	@Override
	public int getType()
	{
		return TriggerEvent.TRIGGER_TYPE_BATTLE_WIN;
	}
}
