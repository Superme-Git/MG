package fire.pb.friends;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.PropRole;
import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.util.DateValidate;

/**
 * 好友帮助类
 * @author XGM
 */
public class FriendHelper {

	// 好友人数限制
	public static final int FRIEND_NUM_LIMIT = 100;			// 好友人数限制

	// 好友度限制
	public static final int FRIENDLYDEGREES_INIT = 1;		// 好友度初值

	// 好友关系
	public static final short MARRY = 1;	// 婚姻
	public static final short SWORN = 2;	// 结拜
	public static final short MASTER = 4;	// 师徒

	/**
	 * 是否为双向好友
	 */
	public static boolean isBothwayFriend(xbean.FriendGroups friendGroups1,
			long roleId1, xbean.FriendGroups friendGroups2, long roleId2) {
		if (friendGroups1 == null || friendGroups2 == null) {
			return false;
		}
		if (friendGroups1.getFriendmap().get(roleId2) == null
				|| friendGroups2.getFriendmap().get(roleId1) == null) {
			return false;
		}
		return true;
	}

	/**
	 * 是否为双向好友
	 */
	public static boolean isBothwayFriend(long roleId1, long roleId2) {
		xbean.FriendGroups friendGroups1 = xtable.Friends.select(roleId1);
		xbean.FriendGroups friendGroups2 = xtable.Friends.select(roleId2);
		return isBothwayFriend(friendGroups1, roleId1, friendGroups2, roleId2);
	}

	/**
	 * 查看好友度
	 */
	public static int getFriendlyDegrees(xbean.FriendGroups myFriendGroups,
			long friendRoleId) {
		if (myFriendGroups != null) {
			xbean.FriendBean friendBean = myFriendGroups.getFriendmap().get(friendRoleId);
			if (friendBean != null) {
				return friendBean.getFriendlydegrees();
			}
		}
		return -1;
	}

	/**
	 * 查看好友度
	 */
	public static int getFriendlyDegrees(long myRoleId, long friendRoleId) {
		Map<Long, xbean.FriendBean> friendMap = xtable.Friends.selectFriendmap(myRoleId);
		if (friendMap != null) {
			xbean.FriendBean friendBean = friendMap.get(friendRoleId);
			if (friendBean != null) {
				return friendBean.getFriendlydegrees();
			}
		}
		return -1;
	}

	/**
	 * 加好友度
	 */
	public static int addFriendlyDegrees(xbean.FriendGroups myFriendGroups,
			long myRoleId, long friendRoleId, int addValue) {
		if (myFriendGroups == null) {
			return 0;
		}
		xbean.FriendBean friendBean = myFriendGroups.getFriendmap().get(friendRoleId);
		if (friendBean != null) {
			int nowValue = friendBean.getFriendlydegrees();
			if (nowValue + addValue > getFriendlyDegreesMax()) {
				addValue = Math.max(getFriendlyDegreesMax() - nowValue, 0);
			}
			nowValue += addValue;
			friendBean.setFriendlydegrees(nowValue);
			// 刷新好友度
			SUpdateFriendLevel send = new SUpdateFriendLevel();
			send.friendid = friendRoleId;
			send.currentfriendlevel = nowValue;
			mkdb.Procedure.psendWhileCommit(myRoleId, send);
			// 好友度增加事件
			if (addValue > 0) {
				onAddFriendlyDegrees(myRoleId, nowValue);
			}
			return addValue;
		}
		return 0;
	}

	/**
	 * 好友度增加事件
	 */
	private static void onAddFriendlyDegrees(long roleId, int nowValue) {
		// 好友度的成就
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.HAO_YOU_DU, 500, 0, nowValue);
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.HAO_YOU_DU, 1000, 0, nowValue);
	}

	//----------------------------------------------------------------------//
	// 好友度每日限制相关 BEGIN
	//----------------------------------------------------------------------//

	/**
	 * 修正好友度每日限制
	 */
	public static void fixFriendlyDegreesLimit(xbean.FriendGroups myFriendGroups) {
		// 隔天重置
		long now = System.currentTimeMillis();
		xbean.FriendlyDegreesLimit limit = myFriendGroups.getFriendlydegreeslimit();
		if (DateValidate.inTheSameDay(now, limit.getResettime()) == false) {
			limit.getTodayfriendlydegreesmap().clear();
			limit.setResettime(now);
		}
	}

	/**
	 * 获得今日增加的好友度
	 */
	public static int getTodayFriendlyDegrees(xbean.FriendGroups myFriendGroups, long friendRoleId) {
		fixFriendlyDegreesLimit(myFriendGroups);
		Integer ret = myFriendGroups.getFriendlydegreeslimit().getTodayfriendlydegreesmap().get(friendRoleId);
		return ret != null ? ret : 0;
	}

	/**
	 * 增加今日累计好友度
	 */
	public static void addTodayFriendlyDegrees(xbean.FriendGroups myFriendGroups,
			long friendRoleId, int addValue) {
		fixFriendlyDegreesLimit(myFriendGroups);
		int value = getTodayFriendlyDegrees(myFriendGroups, friendRoleId) + addValue;
		myFriendGroups.getFriendlydegreeslimit().getTodayfriendlydegreesmap().put(friendRoleId, value);
	}

	//----------------------------------------------------------------------//
	// 好友度每日限制相关 END
	//----------------------------------------------------------------------//

	//----------------------------------------------------------------------//
	// 其他 BEGIN
	//----------------------------------------------------------------------//

	/**
	 * 获得好友关系
	 */
	public static int getRelation(long srcRoleId, long friendId) {
		int relation = 0;
		long marryRoleId = 0;
		long masterId = 0;
		xbean.MenstorAndApprent master = xtable.Menstor.select(srcRoleId);
		if (master != null) {
			masterId = master.getMenstorid();
		}
		if (marryRoleId == friendId) {
			relation = MARRY;
		}
		if (masterId == friendId) {
			relation = relation + MASTER;
		}
		return relation;
	}

	/**
	 * 获得好友关系
	 */
	public static int getRelation(long roleid, long marryRoleId, long swordId, long masterId) {
		int relation = 0;
		if (marryRoleId == roleid) {
			relation = MARRY;
		}
		if (masterId == roleid) {
			relation = relation + MASTER;
		}
		return relation;
	}

	public static InfoBean toInfoBean(long srcRoleId, long friendId) {
		InfoBean friendInfoBean = new InfoBean();
		boolean onlineFlag = fire.pb.StateCommon.isOnlineBuffer(friendId);
		int flag = onlineFlag == true ? 1 : 0;
		PropRole friendAttr = new PropRole(friendId, true);
		friendInfoBean.roleid = friendId;
		friendInfoBean.name = friendAttr.getName();
		friendInfoBean.rolelevel = (short) friendAttr.getLevel();
		friendInfoBean.school = (byte) friendAttr.getSchool();
		friendInfoBean.shape = friendAttr.getShape();
		friendInfoBean.online = (byte) flag;
		friendInfoBean.relation = (short) getRelation(srcRoleId, friendId);
		friendInfoBean.factionid = friendAttr.getClanKey();
		if (friendInfoBean.factionid > 0) {
//			String clanname = xtable.Clans.selectClanname(friendInfoBean.factionid);
//			if (clanname != null) {
//				friendInfoBean.factionname = clanname;
//			}
		}
		return friendInfoBean;
	}

	public static InfoBean toInfoBean(long friendId) {
		InfoBean friendInfoBean = new InfoBean();
		boolean onlineFlag = fire.pb.StateCommon.isOnlineBuffer(friendId);
		int flag = onlineFlag == true ? 1 : 0;
		PropRole friendAttr = new PropRole(friendId, true);
		friendInfoBean.roleid = friendId;
		friendInfoBean.name = friendAttr.getName();
		friendInfoBean.rolelevel = (short) friendAttr.getLevel();
		friendInfoBean.school = (byte) friendAttr.getSchool();
		friendInfoBean.shape = friendAttr.getShape();
		friendInfoBean.online = (byte) flag;
		friendInfoBean.factionid = friendAttr.getClanKey();
		if (friendInfoBean.factionid > 0) {
//			String clanname = xtable.Clans.selectClanname(friendInfoBean.factionid);
//			if (clanname != null) {
//				friendInfoBean.factionname = clanname;
//			}
		}
		return friendInfoBean;
	}

	/**
	 * role on line call
	 */
	public static InfoBean toInfoBean(long roleId, long marryRoleId, long swordId, long masterId) {
		InfoBean friendInfoBean = new InfoBean();
		boolean onlineFlag = fire.pb.StateCommon.isOnlineBuffer(roleId);
		int flag = onlineFlag == true ? 1 : 0;
		PropRole friendAttr = new PropRole(roleId, true);
		friendInfoBean.roleid = roleId;
		friendInfoBean.name = friendAttr.getName();
		friendInfoBean.rolelevel = (short) friendAttr.getLevel();
		friendInfoBean.school = (byte) friendAttr.getSchool();
		friendInfoBean.shape = friendAttr.getShape();
		friendInfoBean.online = (byte) flag;
		friendInfoBean.relation = (short) getRelation(roleId, marryRoleId, swordId, masterId);
		friendInfoBean.factionid = friendAttr.getClanKey();
		if (friendInfoBean.factionid > 0) {
//			String clanname = xtable.Clans.selectClanname(friendInfoBean.factionid);
//			if (clanname != null) {
//				friendInfoBean.factionname = clanname;
//			}
		}
		return friendInfoBean;
	}

	/**
	 * 通知好友上下线
	 */
	public static void notifyFriendStatus(final boolean online, final long srcRoleId) {

		mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
			@Override
			public boolean process() {
				xbean.FriendGroups groups = xtable.Friends.select(srcRoleId);
				if (groups == null) {
					return false;
				}
				SFriendsOnline send = new SFriendsOnline();
				send.roleid = srcRoleId;
				send.online = online ? (byte) 1 : 0;
				List<Long> sendList = new ArrayList<Long>();

				for (long roleId : groups.getFriendmap().keySet()) {
					xbean.FriendGroups temp = xtable.Friends.select(roleId);
					if (temp == null) {
						continue;
					}
					if (temp.getFriendmap().get(srcRoleId) != null) {
						sendList.add(roleId);
					}
				}

				if (sendList.size() > 0) {
					mkdb.Procedure.psendWhileCommit(sendList, send);
				}
				return true;
			}
		});
	}

	//----------------------------------------------------------------------//
	// 其他 END
	//----------------------------------------------------------------------//

	/**
	 * 获得通用配置
	 */
	public static Integer getCommonConf(int id) {
		java.util.Map<Integer, SCommon> confs = ConfigManager.getInstance().getConf(SCommon.class);
		if (confs != null) {
			SCommon conf = confs.get(id);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return null;
	}

	/**
	 * 好友间每日获得好友度上限
	 */
	public static int getFriendlyDegreesDayMax() {
		Integer value = getCommonConf(343);
		if (value == null) {
			value = 100;
		}
		return value;
	}

	/**
	 * 好友间的好友度上限,这个是总限制
	 */
	public static int getFriendlyDegreesMax() {
		Integer value = getCommonConf(342);
		if (value == null) {
			value = 9999;
		}
		return value;
	}

	/**
	 * 战斗加好友度的上限
	 */
	public static int getFriendlyDegreesMaxOfBattle() {
		Integer value = getCommonConf(340);
		if (value == null) {
			value = 9999;
		}
		return value;
	}

	/**
	 * 送花加好友度的上限
	 */
	public static int getFriendlyDegreesMaxOfGiveGift() {
		Integer value = getCommonConf(341);
		if (value == null) {
			value = 9999;
		}
		return value;
	}
}
