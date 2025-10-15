package fire.pb.clan;

import fire.pb.StateCommon;
import mkdb.Procedure;

public class PSearchRole extends Procedure {

	private long roleId;
	private String friendParam;

	public PSearchRole(long roleid, String friendParam) {
		this.roleId = roleid;
		this.friendParam = friendParam;
	}

	@Override
	public boolean process() {
		Long friendId = 0L;
		try {
			friendId = Long.valueOf(friendParam);
		} catch (Exception e) {
			friendId = xtable.Rolename2key.select(friendParam);
		}
		if (friendId == null || friendId <= 0) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 146130, null);
			return false;
		}
		// 不能自己搜自己
		if (roleId == friendId) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145347, null);
			return false;
		}
		xbean.Properties prop = xtable.Properties.select(friendId);
		// 没有找到
		if (prop == null || prop.getDeletetime() != 0) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 146130, null);
			return false;
		}
		//判断对方是否在线
		if (!StateCommon.isOnline(friendId)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 145001, null);
			return false;
		}
		
		// 通知客户端
		SRequestSearchRole send = new SRequestSearchRole();
		InvitationRoleInfo invitationRoleInfo=PInvitationViewProc.createInvitationRoleInfo(friendId,prop);
		send.invitationroleinfolist=invitationRoleInfo;
		mkdb.Procedure.psendWhileCommit(roleId, send);
		return true;
	}
}
