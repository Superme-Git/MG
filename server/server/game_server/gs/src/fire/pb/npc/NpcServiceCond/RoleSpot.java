package fire.pb.npc.NpcServiceCond;

import fire.pb.triggers.TriggerRole;


public class RoleSpot implements Condition{

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		TriggerRole trole = new TriggerRole(roleid, true);
		boolean triggered = trole.isTriggered(args1);
		if(args2 == 0)
			return !triggered;
		else
			return triggered;
	}
}
