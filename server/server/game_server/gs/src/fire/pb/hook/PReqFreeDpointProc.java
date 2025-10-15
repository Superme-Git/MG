package fire.pb.hook;


public class PReqFreeDpointProc extends mkdb.Procedure{
	
	private final long roleId;
	
	public PReqFreeDpointProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {	
		int res = RoleHookManager.getInstance().freeDpoint(roleId);
		if(res == HookFreeDpointResEnum.FREEDPOINTSUCC){
			
		}else if(res == HookGetDpointResEnum.GETDPOINTFAIL){
			
		}
		
		RoleHookManager.getInstance().refreshHookExpdata(roleId,true);
		return true;
	}
}
