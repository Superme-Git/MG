package fire.pb.triggers.event;

import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public class UpLevelAndTaskCom extends TriggerEvent
{
	public UpLevelAndTaskCom(STriggerCondition scondition)
	{
		super(scondition);
	}

	
	@Override
	public boolean triggered(TriggerRole triggerRole,List<Integer> args)
	{
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
		return TriggerEvent.TRIGGER_TYPE_UPLEVEL_TASK;
	}

}
