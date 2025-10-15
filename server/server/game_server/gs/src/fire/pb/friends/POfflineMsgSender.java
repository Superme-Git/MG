package fire.pb.friends;

import mkdb.Procedure;

public class POfflineMsgSender extends Procedure {

	private long roleId;

	private OffLineMessageManager offlineMgr;

	public POfflineMsgSender(long roleId) {
		this.roleId = roleId;
		offlineMgr = OffLineMessageManager.getOffLineMsgManager();
	}

	@Override
	public boolean process() {
		return offlineMgr.executeOfflineMsgById(roleId);
	}
}
