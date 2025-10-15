package fire.pb.friends;

import mkdb.Procedure;

public class PSetSign extends Procedure {

	private final long roleId;
	private final String signContent; // 签名内容

	public PSetSign(long roleId, String signContent) {
		this.roleId = roleId;
		this.signContent = signContent;
	}

	@Override
	protected boolean process() {

		if (signContent.isEmpty()) {
			return true;
		}

		// 保存签名
		xbean.FriendGroups groups = xtable.Friends.get(roleId);
		if (groups == null) {
			groups = xbean.Pod.newFriendGroups();
			xtable.Friends.insert(roleId, groups);
		}
		groups.setSign(signContent);

		// 记录日志
		if (Module.logger.isDebugEnabled()) {
			Module.logger.debug("[PSetSign] roleId:" + roleId
					+ " signContent:" + signContent);
		}
		return true;
	}
}
