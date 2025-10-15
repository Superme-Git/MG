package fire.pb.triggers.event;

import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public class UseItem extends TriggerEvent
{
	private final int itemId;
	private final int regionId;
	public UseItem(STriggerCondition scondition)
	{
		super(scondition);
		if(args.isEmpty())
			throw new IllegalArgumentException("UseItem args为空，应有regionId");
		itemId = args.get(0);
		if(args.size() >= 2)
			regionId = args.get(1);
		else
			regionId = 0;
	}

	
	@Override
	public boolean triggered(TriggerRole triggerRole,List<Integer> args)
	{
		if(args.isEmpty())
			return false;
		int itemid = args.get(0);//物品id
		if(this.itemId != itemid)
			return false;
		if(regionId != 0)
		{
			//检验玩家现在所在区域是否符合
			if(!triggerRole.inRegion(regionId))
				return false;
		}
		
		return true;
	}

	@Override
	public int getType()
	{
		return TriggerEvent.TRIGGER_TYPE_USE_ITEM;
	}

}
