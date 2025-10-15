package fire.pb.triggers.event;

import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public class BattleDead extends TriggerEvent
{
	private final int areaId;
	public BattleDead(STriggerCondition scondition)
	{
		super(scondition);
		if(args.isEmpty())
			throw new IllegalArgumentException("BattleDead args为空，应有areaId");
		areaId = args.get(0);
	}

	@Override
	public boolean triggered(TriggerRole triggerRole,List<Integer> args)
	{
		//检验玩家现在所在区域是否符合
		if(!triggerRole.inRegion(areaId))
			return false;
		return true;
	}

	@Override
	public int getType()
	{
		return TriggerEvent.TRIGGER_TYPE_DEATH;
	}

}
