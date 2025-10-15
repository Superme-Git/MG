
package fire.pb.school.shouxi;

import mkdb.Procedure;

public class PSendShouxiOnlineMsg extends Procedure {

	private final long roleid;
	private final boolean online;

	public PSendShouxiOnlineMsg(long roleid,boolean online) {

		super();
		this.roleid = roleid;
		this.online = online;
	}

	@Override
	protected boolean process() throws Exception {

		ProfessionLeaderManager.sendShouxiOnLineOfflineMsg(roleid, online);
		return true;
	}

}
