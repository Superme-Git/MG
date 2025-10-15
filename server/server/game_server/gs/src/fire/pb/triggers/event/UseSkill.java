package fire.pb.triggers.event;

import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public class UseSkill extends TriggerEvent
{
	private final int skillId;
	private final int regionId;
	
	public UseSkill(STriggerCondition scondition)
	{
		super(scondition);
		if(args.isEmpty())
			throw new IllegalArgumentException("skillId args为空，应有skillId");
		skillId = args.get(0);
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
		int skillid = args.get(0);//技能id
		if(this.skillId != skillid)
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
		return TriggerEvent.TRIGGER_TYPE_USE_SKILL;
	}

}
