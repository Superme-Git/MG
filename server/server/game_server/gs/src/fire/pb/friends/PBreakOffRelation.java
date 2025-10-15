package fire.pb.friends;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.locojoy.base.Runnable;

import fire.log.beans.SNSOpBean;
import fire.pb.PropRole;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.mysql.C3P0Util;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

public class PBreakOffRelation extends Procedure {

	private long roleId;
	private long friendId;

	public PBreakOffRelation(long roleId, long friendId) {
		this.roleId = roleId;
		this.friendId = friendId;
	}

	private boolean DeleteMysqlRelation() {
		boolean updateRet = false;
		Connection conn = C3P0Util.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sqlstr = "DELETE FROM `role_relation` WHERE roleid=" + roleId + " and friendid=" + friendId;
			int ret = stmt.executeUpdate(sqlstr);
			Module.logger.info("[" + roleId + "]DeleteMysqlRealtion:" + sqlstr + ";ret = " + ret);
			if(ret >= 0)
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

		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(roleId);
		roleIds.add(friendId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));

		// 我的好友列表中删除目标
		xbean.FriendGroups myGroups = xtable.Friends.get(roleId);
		if (myGroups == null) {
			return false;
		}
		xbean.FriendBean myFriendBean = myGroups.getFriendmap().remove(friendId);
		if (myFriendBean == null) {
			return false;
		}

		// 目标的好友列表中重置我的好友度
		xbean.FriendGroups targetGroups = xtable.Friends.get(friendId);
		if (targetGroups != null) {
			xbean.FriendBean targetFriendBean = targetGroups.getFriendmap().get(roleId);
			if (targetFriendBean != null) {
				int value = FriendHelper.FRIENDLYDEGREES_INIT;
				targetFriendBean.setFriendlydegrees(value);
				// 刷新好友度
				SUpdateFriendLevel send = new SUpdateFriendLevel();
				send.friendid = roleId;
				send.currentfriendlevel = value;
				mkdb.Procedure.psendWhileCommit(friendId, send);
			}
		}

		if(ConfigManager.getUseMysql()){
			//为个人空间好友圈同步好友信息
			Gs.getExecService().execute(new Runnable() {
				@Override
				public void run() {
					if(!DeleteMysqlRelation()){
						Module.logger.error("PBreakOffRelation.DeleteMysqlRelation failed!");
					}
				}
			});	
//			if(!DeleteMysqlRelation()){ //如果mysql删除失败，直接回滚，这个好友咱不删了！
//				Module.logger.error("PBreakOffRelation.DeleteMysqlRelation failed!");
//				return false;
//			}
		}

		SBreakOffRelation breakOffRelation = new SBreakOffRelation(friendId);

		mkdb.Procedure.psendWhileCommit(roleId, breakOffRelation);

		PropRole friendProp = new PropRole(friendId, true);
		List<String> param = new ArrayList<String>();
		param.add(friendProp.getName());
		MessageMgr.psendMsgNotify(roleId, 140379, param);

		int friendNum = myGroups.getFriendmap().size();
		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PBreakOffRelation] roleId:" + roleId
					+ " friendRoleId:" + friendId
					+ " friendRoleName:" + friendProp.getName()
					+ " friendNum:" + friendNum);
		}
		
		// 运营日志
		writeYYLogger(friendNum);
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
