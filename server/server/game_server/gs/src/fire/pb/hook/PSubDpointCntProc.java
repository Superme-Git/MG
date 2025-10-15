package fire.pb.hook;

import mkdb.Procedure;

public class PSubDpointCntProc extends Procedure {
	
	private final long roleId;
	private final int cnt;
	
	public PSubDpointCntProc(final long roleId, final int cnt) {
		this.roleId = roleId;
		this.cnt = cnt;
	}

	@Override
	protected boolean process() throws Exception {	
		
		RoleHookManager.getInstance().consumeDpoint(roleId, cnt);
		RoleHookManager.getInstance().refreshHookExpdata(roleId, true);
		return true;
		
	}
}
