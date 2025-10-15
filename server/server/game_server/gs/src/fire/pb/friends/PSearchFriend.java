package fire.pb.friends;

import mkdb.Procedure;

public class PSearchFriend extends Procedure {

	private long roleId;
	private String friendParam;

	public PSearchFriend(long roleid, String friendParam) {
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
		// 通知客户端
		SSearchFriend send = new SSearchFriend();
		send.friendinfobean = FriendHelper.toInfoBean(roleId, friendId);
		mkdb.Procedure.psendWhileCommit(roleId, send);
		return true;
	}
}
