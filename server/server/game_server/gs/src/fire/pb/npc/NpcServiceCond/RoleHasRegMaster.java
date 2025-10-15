package fire.pb.npc.NpcServiceCond;

public class RoleHasRegMaster implements Condition {

	@Override
	public boolean CheckCond(long roleid, int ret, int args2) {
		
//		if ( ret == 1)
//			return fire.pb.master.Module.getInstance().hasRegMaster( roleid );
//		else if ( ret == 0 )
//			return !fire.pb.master.Module.getInstance().hasRegMaster( roleid );
//		else
//			throw new IllegalArgumentException("判断是否登记做师傅条件参数"+ret+"错误,0 or 1");
		
		return false;
	}

}
