package fire.pb.triggers.event;

import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public class TriggerNpc extends TriggerEvent
{
	public final int npcbaseID;
	public TriggerNpc(STriggerCondition scondition)
	{
		super(scondition);
		if(args.isEmpty())
			throw new IllegalArgumentException("TriggerNpc args为空，应有npcbaseID");
		npcbaseID = args.get(0);
	}

	
	@Override
	public boolean triggered(TriggerRole triggerRole,List<Integer> args)
	{
		if(args.isEmpty())
			return false;
		int npcid = args.get(0);
		if(npcid != npcbaseID)
			return false;
		
		return true;
	}

	@Override
	public int getType()
	{
		return TriggerEvent.TRIGGER_TYPE_NPC;
	}

}
