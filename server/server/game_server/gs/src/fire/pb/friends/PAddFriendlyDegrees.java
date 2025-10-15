package fire.pb.friends;

import java.util.List;

/**
 * 战斗结束加好友度,有每日限制
 * @author XGM
 */
public class PAddFriendlyDegrees extends mkdb.Procedure {

	private final List<Long> roleIds;
	
	public PAddFriendlyDegrees(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	@Override
	protected boolean process() throws Exception {

		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));

		for (long selfRoleId : roleIds) {
			xbean.FriendGroups selfFriendGroups = xtable.Friends.get(selfRoleId);
			for (long otherRoleId : roleIds) {
				if (selfRoleId == otherRoleId) {
					continue;
				}
				xbean.FriendGroups otherFriendGroups = xtable.Friends.get(otherRoleId);
				if (FriendHelper.isBothwayFriend(selfFriendGroups, selfRoleId, otherFriendGroups, otherRoleId)) {
					addFriendlyDegrees(selfFriendGroups, selfRoleId, otherRoleId, 1);
				}
			}
		}

		return true;
	}

	/**
	 * 增加好友度
	 */
	public static void addFriendlyDegrees(xbean.FriendGroups myFriendGroups,
			long myRoleId, long friendRoleId, int addValue) {
		if (myFriendGroups.getFriendmap().get(friendRoleId) != null) {
			int nowValue = FriendHelper.getFriendlyDegrees(myFriendGroups, friendRoleId);
			if (nowValue >= FriendHelper.getFriendlyDegreesMaxOfBattle()) {
				// 超出战斗上限时,忽略
				return;
			}
			int value = FriendHelper.getTodayFriendlyDegrees(myFriendGroups, friendRoleId);
			if (value < FriendHelper.getFriendlyDegreesDayMax()) {
				FriendHelper.addTodayFriendlyDegrees(myFriendGroups, friendRoleId, addValue);
				FriendHelper.addFriendlyDegrees(myFriendGroups, myRoleId, friendRoleId, addValue);
			}
		}
	}
}
