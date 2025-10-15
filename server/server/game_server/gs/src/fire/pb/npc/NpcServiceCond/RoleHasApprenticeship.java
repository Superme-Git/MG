package fire.pb.npc.NpcServiceCond;

public class RoleHasApprenticeship implements Condition {

	@Override
	public boolean CheckCond(long roleid, int ret, int args2) {	
//		if ( ret == 1)
//			return fire.pb.master.Module.getInstance().hasApprenticeship( roleid );
//		else if ( ret == 0 )
//			return !fire.pb.master.Module.getInstance().hasApprenticeship( roleid );
//		else
//			throw new IllegalArgumentException("判断是否有师徒关系条件参数"+ret+"错误,0 or 1");
		return false;
	}

}
