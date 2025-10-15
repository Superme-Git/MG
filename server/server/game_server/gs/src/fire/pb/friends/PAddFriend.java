package fire.pb.friends;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mkdb.Procedure;

import com.locojoy.base.Runnable;

import fire.log.beans.SNSOpBean;
import fire.pb.PropRole;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.mysql.C3P0Util;
import fire.pb.util.StringConstant;

public class PAddFriend extends Procedure {

	private long roleId;
	private long friendId;

	public PAddFriend(long roleId, long friendId) {
		this.roleId = roleId;
		this.friendId = friendId;
	}
	
	private boolean InsertMysqlRelation() {
		boolean updateRet = false;
		Connection conn = C3P0Util.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sqlstr = "INSERT INTO `role_relation`(roleid, friendid, relation) "
					+ "VALUES ('" + roleId + "', '" + friendId + "', '" + 1 + "')";
			int ret = stmt.executeUpdate(sqlstr);
			Module.logger.info("[" + roleId + "]InsertMysqlRealtion:" + sqlstr + ";ret = " + ret);
			if(ret > 0)
				updateRet = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				}
				stmt = null;
			}
			C3P0Util.close(conn, null, null);
			return updateRet;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				}
				stmt = null;
			}
		}
		C3P0Util.close(conn, null, null);
		return updateRet;
	}

	@Override
	public boolean process() {

		if (roleId == friendId) {
			return false;
		}

		PropRole role = new PropRole(roleId, true);

		if (xtable.Properties.selectRolename(friendId) == null) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 170040, null);
			return false;
		}
		PropRole friendRole = new PropRole(friendId, true);

		List<Long> list = new ArrayList<Long>();
		list.add(friendId);
		list.add(roleId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, list));

		xbean.FriendGroups groups = xtable.Friends.get(roleId);
		if (groups == null) {
			groups = xbean.Pod.newFriendGroups();
			xtable.Friends.insert(roleId, groups);
		}

		// 好友数量限制
		if (groups.getFriendmap().size() >= FriendHelper.FRIEND_NUM_LIMIT) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 140213, null);
			return false;
		}

		if (groups.getFriendmap().get(friendId) != null) {
			return false;
		} else {
			if(ConfigManager.getUseMysql()){
				//为个人空间好友圈同步好友信息
				Gs.getExecService().execute(new Runnable() {
					@Override
					public void run() {
						if(!InsertMysqlRelation()){
							Module.logger.error("PAddFriend.InsertMysqlRelation failed!");
						}
					}
				});	
				
//				if(!InsertMysqlRelation()){ //如果mysql插入失败，直接回滚，这个好友咱不加了！
//					Module.logger.error("PAddFriend.InsertMysqlRelation failed!");
//					return false;
//				}
			}
			xbean.FriendBean friendBean = xbean.Pod.newFriendBean();
			friendBean.setFriendlydegrees(FriendHelper.FRIENDLYDEGREES_INIT);
			groups.getFriendmap().put(friendId, friendBean);

			List<String> p1 = new ArrayList<String>();
			p1.add(role.getName());

			List<String> p2 = new ArrayList<String>();
			p2.add(friendRole.getName());

			fire.pb.talk.MessageMgr.sendMsgNotify(friendId, 140201, p1);
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 140202, p2);

			SAddFriend sendAddFriend = new SAddFriend();
			sendAddFriend.friendinfobean = FriendHelper.toInfoBean(roleId, friendId);
			mkdb.Procedure.psendWhileCommit(roleId, sendAddFriend);

			String msg = StringConstant.ID2String("你好，我加你为好友了哦~以后常联系~", StringConstant.你好我加你为好友了哦以后常联系);
			String content = "<T t=\"" + msg + "\" c=\"ff693f00\"></T>";
			// 被加好友的人收到提示消息
			if (OffLineMessageManager.getOffLineMsgManager().isOnline(friendId)) {
				if (FriendHelper.isBothwayFriend(roleId, friendId)) {
					SFriendMessageToRole friendMsg = new SFriendMessageToRole();
					friendMsg.roleid = roleId;
					friendMsg.content = content;
					friendMsg.rolelevel = (short) role.getLevel();
					mkdb.Procedure.psendWhileCommit(friendId, friendMsg);
				} else {
					SStrangerMessageToRole strangerMsg = new SStrangerMessageToRole();
					StrangerMessageBean mb = new StrangerMessageBean();
					mb.friendinfobean = FriendHelper.toInfoBean(friendId, roleId);
					mb.content = content;
					strangerMsg.strangermessage = mb;
					mkdb.Procedure.psendWhileCommit(friendId, strangerMsg);
				}
			} else {

			}

			int friendNum = groups.getFriendmap().size();
			
			fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.FRIEND_COURSE, 1, 0, friendNum);
			fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.FRIEND_COURSE, 10, 0, friendNum);
			fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.FRIEND_COURSE, 50, 0, friendNum);
			// 记录日志
			if (Module.logger.isInfoEnabled()) {
				Module.logger.info("[PAddFriend] roleId:" + roleId
						+ " friendRoleId:" + friendId
						+ " friendRoleName:" + sendAddFriend.friendinfobean.name
						+ " friendNum:" + friendNum);
			}
			
			// 运营日志
			writeYYLogger(friendNum);
		}
		return true;
	}

	/**
	 * 运营日志
	 * @param friendNum
	 */
	private void writeYYLogger(int friendNum) {
		fire.log.YYLogger.SNSLog(roleId, new SNSOpBean(friendNum));
	}
}
