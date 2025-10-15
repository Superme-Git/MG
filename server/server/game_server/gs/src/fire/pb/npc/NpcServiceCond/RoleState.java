package fire.pb.npc.NpcServiceCond;


public class RoleState implements Condition{

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		// TODO Auto-generated method stub
		fire.pb.buff.BuffAgent agent = new fire.pb.buff.BuffRoleImpl(roleid, true);
		return agent.existState(args1);
	}
}
