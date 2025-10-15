package fire.pb.mission.activelist;

public class PRefreshActiveData extends mkdb.Procedure{
	final private long roleId;
	
	public PRefreshActiveData(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception{
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleId, false);
		actrole.checkAndResetActiveData();
		actrole.sendActivityData();
		return true;
	}
	
}
