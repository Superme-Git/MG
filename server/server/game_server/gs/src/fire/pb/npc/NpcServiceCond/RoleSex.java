package fire.pb.npc.NpcServiceCond;


public class RoleSex implements Condition{

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		final fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
		return prole.getSex() >= args1 && prole.getSex() <= args2;
	}
}
