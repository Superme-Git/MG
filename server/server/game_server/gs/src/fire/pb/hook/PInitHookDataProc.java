package fire.pb.hook;


public class PInitHookDataProc extends mkdb.Procedure{
	
	private final long roleId;
	

	public PInitHookDataProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		
		RoleHookManager.getInstance().initHookdata(roleId);
		return true;
	}
}
