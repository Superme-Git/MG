package fire.pb.npc.NpcServiceCond;


public class RoleSchool implements Condition{

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		final fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
		return prole.getSchool() >= args1 && prole.getSchool() <= args2;
	}
}
