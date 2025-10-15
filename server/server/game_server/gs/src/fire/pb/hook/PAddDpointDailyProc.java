package fire.pb.hook;

public class PAddDpointDailyProc extends mkdb.Procedure{
	
	private final long roleId;
	

	public PAddDpointDailyProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {	
		
		RoleHookManager.getInstance().addDpointDaily(roleId);
		RoleHookManager.getInstance().refreshHookExpdata(roleId,true);
		
		return true;
	}
}
