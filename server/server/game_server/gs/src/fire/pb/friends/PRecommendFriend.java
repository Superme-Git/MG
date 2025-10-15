package fire.pb.friends;

import fire.pb.util.Misc;
import gnet.link.Role;
import mkdb.Procedure;

import java.util.LinkedList;
import java.util.List;

/**
 * 推荐好友
 * @author XGM
 */
public class PRecommendFriend extends Procedure {

	private long roleId;

	public PRecommendFriend(final long roleId) {
		this.roleId = roleId;
	}

	@Override
	public boolean process() {

		List<Long> roleIds = getRecommendIDList();

		SRecommendFriend send = new SRecommendFriend();
		for (Long otherId : roleIds) {
			fire.pb.friends.InfoBean info = FriendHelper.toInfoBean(otherId);
			send.friendinfobeanlist.add(info);
		}
		psendWhileCommit(roleId, send);
		return true;
	}

	public List<Long> getRecommendIDList() {
		List<Role> roles = gnet.link.Onlines.getInstance().getSafeRoles();

		// 随机获取推荐的roleId
		List<Long> roleIds = new LinkedList<Long>();
		if (roles.size() > 0) {
			// 得到所有角色ID
			List<Long> allRoleIds = new LinkedList<Long>();
			for (Role role : roles) {
				allRoleIds.add(role.getRoleid());
			}
			// 删除自己
			allRoleIds.remove((Long) roleId);
			// 删除自己的好友
			xbean.FriendGroups myFriendGroups = xtable.Friends.select(roleId);
			if (myFriendGroups != null) {
				for (Long fid : myFriendGroups.getFriendmap().keySet()) {
					allRoleIds.remove((Long) fid);
				}
			}
			// 删除黑名单人员
			xbean.RolePingBiRolesInfo myPingBi = xtable.Rolepingbirolesinfos.select(roleId);
			if (myPingBi != null) {
				for (Long pbid : myPingBi.getPingbiroles().keySet()) {
					allRoleIds.remove((Long) pbid);
				}
			}
			// 随机推荐好友
			int limit = 6;
			for (int i = 0; i < limit && allRoleIds.size() > 0; i++) {
				int r = Misc.getRandomBetween(0, allRoleIds.size() - 1);
				roleIds.add(allRoleIds.get(r));
				allRoleIds.remove(r);
			}
		}

		return roleIds;
	}
}
