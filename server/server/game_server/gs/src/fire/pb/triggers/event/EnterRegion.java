package fire.pb.triggers.event;

import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public class EnterRegion extends TriggerEvent
{
	private final int regionId;
	public EnterRegion(STriggerCondition scondition)
	{
		super(scondition);
		if(args.isEmpty())
			throw new IllegalArgumentException("EnterRegion args为空，应有regionId");
		regionId = args.get(0);
	}

	
	@Override
	public boolean triggered(TriggerRole triggerRole,List<Integer> args)
	{
		if(args.isEmpty())
			return false;
		int enterregion = args.get(0);
		if(enterregion != regionId)
			return false;
		
		return true;
	}

	@Override
	public int getType()
	{
		return TriggerEvent.TRIGGER_TYPE_ENTER_REGION;
	}

}
