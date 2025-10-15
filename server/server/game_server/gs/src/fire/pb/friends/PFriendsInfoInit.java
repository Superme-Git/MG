package fire.pb.friends;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mkdb.Procedure;

import com.locojoy.base.Octets;

import fire.pb.talk.DisplayInfo;

/**
 * 好友列表,离线消息
 */
public class PFriendsInfoInit extends Procedure {

	private final long selfRoleId;

	public PFriendsInfoInit(long roleId) {
		this.selfRoleId = roleId;
	}

	@Override
	public boolean process() {

		SFriendsInfoInit friends = new SFriendsInfoInit();
		friends.friendnumlimit = FriendHelper.FRIEND_NUM_LIMIT;

		long marryRoleId = 0;
		long swordId = 0;
		long masterId = 0;

		xbean.MenstorAndApprent master = xtable.Menstor.select(selfRoleId);
		if (master != null) {
			masterId = master.getMenstorid();
		}
		xbean.FriendGroups groups = xtable.Friends.select(selfRoleId);
		if (groups != null) {
			Map<Long, xbean.FriendBean> friendsMap = groups.getFriendmap();
			createFriendsList(friendsMap, friends, marryRoleId, swordId, masterId);
			friends.refusestrangermsg = (byte) groups.getRefusestrangermsg();
		}

		// 发送角色好友信息以及设置
		gnet.link.Onlines.getInstance().send(selfRoleId, friends);

		// 收取离线消息(好友列表需要在好友系统消息前送达)
		mkdb.Procedure.pexecuteWhileCommit(new POfflineMsgSender(selfRoleId));

		if (groups == null) {
			return true;
		}

		// 查看离线消息
		java.util.List<xbean.OfflineMsg> offLineMsgList = groups.getOfflinemsglist();
		if (offLineMsgList.size() > 0) {

			SOffLineMsgMessageToRole offLineMsg = new SOffLineMsgMessageToRole();

			for (xbean.OfflineMsg msg : offLineMsgList) {
				offLineMsgBean offms = new offLineMsgBean();
				long friendId = msg.getRoleid();
				offms.strangermessage.friendinfobean = FriendHelper.toInfoBean(friendId, marryRoleId, swordId, masterId);
				offms.strangermessage.content = msg.getContent();
				for (byte[] bb : msg.getDetails()) {
					offms.strangermessage.details.add(new Octets(bb));
				}
				offms.time = msg.getSendtime();
				for (xbean.ShowInfoBean temp : msg.getShowinfos()) {
					DisplayInfo sinfo = new DisplayInfo();
					sinfo.counterid = temp.getCounterid();
					sinfo.roleid = temp.getRoleid();
					sinfo.displaytype = temp.getShowtype();
					sinfo.shopid = temp.getShopid();
					sinfo.uniqid = temp.getThisid();
					offms.strangermessage.displayinfo.add(sinfo);
				}

				if (offms.strangermessage.details.size() != msg.getShowinfos().size()) {
					continue;
				}
				offLineMsg.offlinemsglist.add(offms);
			}

			// 发送角色收到的离线消息
			gnet.link.Onlines.getInstance().send(selfRoleId, offLineMsg);

			clearOffLineMsg();
		}

		// 上线了通知好友
		FriendHelper.notifyFriendStatus(true, selfRoleId);

		// 发送签名列表
		mkdb.Procedure.pexecuteWhileCommit(new PSendSignList(selfRoleId));

		// 清除好友列表中因合服时被删除的角色
		clearInvalidRole(selfRoleId);

		return true;
	}

	private void createFriendsList(Map<Long, xbean.FriendBean> friendsMap,
			SFriendsInfoInit friends, long marryRoleId, long swordId,
			long masterId) {

		if (friendsMap == null) {
			return;
		}
		for (long roleId : friendsMap.keySet()) {
			if (roleId == selfRoleId) {
				continue;
			}
			xbean.FriendBean friendBean = friendsMap.get(roleId);
			fire.pb.friends.FriendInfo info = new fire.pb.friends.FriendInfo();
			if (xtable.Properties.selectRolename(roleId) != null) { // 有可能合服时,角色被删除了
				InfoBean friendinfobean = FriendHelper.toInfoBean(roleId, marryRoleId, swordId, masterId);
				info.friendinfobean = friendinfobean;
				info.friendlevel = friendBean.getFriendlydegrees();
				friends.friends.add(info);
			}
		}
	}

	public void clearOffLineMsg() {
		mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
			@Override
			public boolean process() {
				xbean.FriendGroups groups = xtable.Friends.get(selfRoleId);
				if (groups != null) {
					groups.getOfflinemsglist().clear();
				}
				return true;
			}
		});
	}

	/**
	 * 获得无效的角色列表
	 */
	public static List<Long> getInvalidRoleList(long selfRoleId) {
		List<Long> delRoleIds = new LinkedList<Long>();
		xbean.FriendGroups groups = xtable.Friends.select(selfRoleId);
		if (groups != null) {
			// 遍历好友列表
			for (long roleId : groups.getFriendmap().keySet()) {
				if (xtable.Properties.selectRolename(roleId) == null) {
					// 删除无效角色
					delRoleIds.add(roleId);
				}
			}
		}
		return delRoleIds;
	}

	/**
	 * 清除好友列表中因合服时被删除的角色
	 */
	public static void clearInvalidRole(long selfRoleId) {
		mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
			@Override
			protected boolean process() {
				// 获得无效的角色列表
				List<Long> delRoleIds = getInvalidRoleList(selfRoleId);
				if (delRoleIds.size() > 0) {
					xbean.FriendGroups groups = xtable.Friends.get(selfRoleId);
					if (groups != null) {
						for (long roleId : delRoleIds) {
							// 删除无效角色
							groups.getFriendmap().remove(roleId);
							if (Module.logger.isInfoEnabled()) {
								Module.logger.info("[PFriendsInfoInit.clearInvalidRole] roleId:" + selfRoleId
										+ " friendRoleId:" + roleId
										+ " friendNum:" + groups.getFriendmap().size());
							}
						}
					}
				}
				return true;
			}
		});
	}
}
