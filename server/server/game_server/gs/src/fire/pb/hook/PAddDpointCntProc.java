package fire.pb.hook;

import java.util.ArrayList;
import java.util.List;

import fire.pb.talk.MessageMgr;
import mkdb.Procedure;
import xbean.HookData;

public class PAddDpointCntProc extends Procedure {
	private final long roleId;
	private final int cnt;
	
	public PAddDpointCntProc(final long roleId, final int cnt) {
		this.roleId = roleId;
		this.cnt = cnt;
	}

	@Override
	protected boolean process() throws Exception {	
		HookData hookData = xtable.Rolehook.get(roleId);
		short getDpoint = hookData.getCangetdpoint();
		
		if (getDpoint >= 2000) {
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 160174, null);
			return false;
		}
		
		getDpoint += cnt;
		if (getDpoint > 2000) {
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 160175, null);
			return false;
		}
		
		hookData.setCangetdpoint(getDpoint);
		RoleHookManager.getInstance().refreshHookExpdata(roleId, true);
		
		List<String> para = new ArrayList<String>(1);
		para.add(String.valueOf(cnt));
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 160160, para);
		return true;
	}

}
