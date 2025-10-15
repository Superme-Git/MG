package fire.pb.hook;


public class PReqGetDpointProc extends mkdb.Procedure{
	
	private final long roleId;
	

	public PReqGetDpointProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {	
		
		int res = RoleHookManager.getInstance().getDpoint(roleId);
		if (res == HookGetDpointResEnum.GETDPOINTSUCC) {

		} else if (res == HookGetDpointResEnum.GETDPOINTLIMIT) {

		} else if (res == HookGetDpointResEnum.GETDPOINTFAIL) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 160060, null);
		}
			
		RoleHookManager.getInstance().refreshHookExpdata(roleId,true);
		return true;
	}
}
