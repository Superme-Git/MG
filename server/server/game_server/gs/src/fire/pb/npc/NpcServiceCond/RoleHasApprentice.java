package fire.pb.npc.NpcServiceCond;

public class RoleHasApprentice implements Condition {

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
//		if ( args1 == 1 )
//			return new MyMaster(roleid, true).getApprenticeNum() > 0;
//		return new MyMaster(roleid, true).getApprenticeNum() <= 0;
		return false;
	}

}
