package fire.pb.friends;

import mkdb.Procedure;

public class PSendSignList extends Procedure {

	private final long roleId;

	public PSendSignList(long roleId) {
		this.roleId = roleId;
	}

	@Override
	protected boolean process() {

		xbean.FriendGroups groups = xtable.Friends.select(roleId);
		if (groups == null) {
			return true;
		}

		// 查询所有好友的签名
		SSignList send = new SSignList();
		for (Long rid : groups.getFriendmap().keySet()) {
			String sign = xtable.Friends.selectSign(rid);
			if (sign != null && sign.length() > 0) {
				send.signcontentmap.put(rid, sign);
			}
		}
		psendWhileCommit(roleId, send);
		return true;
	}
}
