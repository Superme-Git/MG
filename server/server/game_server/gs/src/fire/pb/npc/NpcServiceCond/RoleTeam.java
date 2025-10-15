package fire.pb.npc.NpcServiceCond;
import fire.pb.buff.BuffConstant;


public class RoleTeam implements Condition{
	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		// TODO Auto-generated method stub
		fire.pb.buff.BuffAgent agent = new fire.pb.buff.BuffRoleImpl(roleid, true);
		if (agent.existState(BuffConstant.StateType.STATE_TEAM)){
			if (args1 == 1)
				return true;
			if (args1 == 0)
				return false;
		}
		else
			if (args1 == 1)
				return false;
		return true;
	}
}
