package fire.pb.friends;

import mkdb.Procedure;

/**
 * 好友赠送信息列表
 * @author XGM
 */
public class PGiveInfoList extends Procedure {

	private final long roleId;

	public PGiveInfoList(final long roleId) {
		this.roleId = roleId;
	}

	@Override
	protected boolean process() {

		FriendGiveProxy friendGiveProxy = FriendGiveProxy.getFriendGiveProxy(roleId, false);
		if (friendGiveProxy != null) {
			SGiveInfoList send = new SGiveInfoList();
			for (java.util.Map.Entry<Long, Integer> e : friendGiveProxy.getGiveNumMap().entrySet()) {
				send.givenummap.put(e.getKey(), e.getValue().byteValue());
			}
			psendWhileCommit(roleId, send);
		}
		return true;
	}

}
