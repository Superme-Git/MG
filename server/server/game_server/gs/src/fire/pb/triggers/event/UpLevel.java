package fire.pb.triggers.event;

import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public class UpLevel extends TriggerEvent
{
	//private int regionId = 0;
	public UpLevel(STriggerCondition scondition)
	{
		super(scondition);
		/*
		if(args.isEmpty())
			throw new IllegalArgumentException("UpLevel args为空，应有regionId");
		regionId = args.get(0);
		*/
	}

	
	@Override
	public boolean triggered(TriggerRole triggerRole,List<Integer> args)
	{
		//检验玩家现在所在区域是否符合
		/*
		if(!triggerRole.inRegion(regionId))
			return false;
			*/
		if(this.needquest > 0) {
			xbean.CommitedMissions ct = xtable.Commitedmission.select(triggerRole.getRoleid());
			if(null == ct)
				return false;
			else {
				if(ct.getCommitted().contains(Integer.valueOf(this.needquest)))
					return true;
				else 
					return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public int getType()
	{
		return TriggerEvent.TRIGGER_TYPE_UPLEVEL;
	}

}
