package fire.pb.friends;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.Statement;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import fire.pb.mysql.C3P0Util;

/**
 * 好友模块
 * 
 * @author XGM
 */
public class Module implements ModuleInterface {

	public static final Logger logger = Logger.getLogger("FRIEND");

	@Override
	public void exit() {
	}

	@Override
	public void init() throws Exception {
		// 好友数据结构升级
		friendDBUpgrade();
		// 清空过期消息
		mkdb.Procedure proc = new PClearTimeOutProtocol();
		if (mkdb.Transaction.current() == null) {
			proc.submit();
		} else {
			mkdb.Procedure.pexecute(proc);
		}
		// 初始化空间属性
		initSpace();
	}

	@Override
	public ReloadResult reload() throws Exception {
		return null;
	}

	/**
	 * 好友数据结构升级
	 */
	private void friendDBUpgrade() {
		Set<Long> roleIds = new HashSet<Long>();
		// 获得需要升级数据的角色id
		xtable.Friends.getTable().browse(new mkdb.TTable.IWalk<Long, xbean.FriendGroups>() {

			@Override
			public boolean onRecord(Long roleId, xbean.FriendGroups value) {
				// TODO
				return true;
			}
		});
		// 升级数据
		mkdb.Procedure proc = new mkdb.Procedure() {

			@Override
			protected boolean process() {
				if (roleIds.isEmpty()) {
					return true;
				}
				lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));
				for (long roleId : roleIds) {
					@SuppressWarnings("unused")
					xbean.FriendGroups groups = xtable.Friends.get(roleId);
				}
				return true;
			}
		};
		if (mkdb.Transaction.current() == null) {
			proc.submit();
		} else {
			mkdb.Procedure.pexecute(proc);
		}
	}

	// ----------------------------------------------------------------------//
	// 空间相关 BEGIN
	// ----------------------------------------------------------------------//
	public static int MAXRATE = 0;

	public static Map<Integer, SpaceDropGift> spacedropmap = new HashMap<Integer, SpaceDropGift>();

	void initSpace() {
		int maxrate = 0;
		final java.util.NavigableMap<Integer, SpaceDropGift> spdg = ConfigManager.getInstance().getConf(
				fire.pb.friends.SpaceDropGift.class);
		for (final SpaceDropGift sdg : spdg.values()) {
			maxrate += sdg.getWeight();
			spacedropmap.put(sdg.getId(), sdg);
		}
		MAXRATE = maxrate;
	}

	public static int getRandomSpaceDropGift(long spaceRoleId) {
		xbean.RoleSpace rs = xtable.Rolespaces.select(spaceRoleId);
		if (rs == null)
			return -1;
		if (rs.getGift() <= 0)
			return -1;
		int totalrate = MAXRATE;
		int randrate = mkdb.Mkdb.random().nextInt(totalrate);
		Module.logger.info("PFriendsInfoInit.getRandomSpaceDropGift randrate:" + randrate + ",totalrate:" + totalrate);
		int pilerate = 0;
		for (SpaceDropGift sdg : spacedropmap.values()) {
			pilerate += sdg.getWeight();
			if (randrate < pilerate) {
				return sdg.getItem();
			}
		}
		return -1;
	}

	/**
	 * 创建说不得大师
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月26日 下午2:56:44
	 * @version 1.0
	 * @param newRoleID
	 * @return
	 */
	public void createXsh(long newRoleID) {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.RoleSpace rs = xtable.Rolespaces.get(newRoleID);
				if (rs != null)
					return false;
				else {
					xbean.RoleSpace newrs = xbean.Pod.newRoleSpace();
					newrs.setGift(99);
					newrs.setPopularity(0);
					newrs.setRecvgift(0);
					newrs.setGetgiftnum(0);
					newrs.setGetgifttime(0);
					xtable.Rolespaces.insert(newRoleID, newrs);
				}
				// 为个人空间好友圈同步信息
				if (!InsertMysqlRelation(newRoleID, "说不得大师", 1, 100)) { // 如果mysql插入失败，直接回滚，角色创建失败
					logger.error("PCreateRole.InsertMysqlRelation failed!");
				}
				return true;
			}
		}.submit();
	}

	/**
	 * 插入角色空间数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月26日 下午3:34:19
	 * @version 1.0
	 * @param roleId
	 * @param rolename
	 * @param shapeid
	 * @param level
	 * @return
	 */
	private boolean InsertMysqlRelation(long roleId, String rolename, int shapeid, int level) {
		boolean updateRet = false;
		Connection conn = C3P0Util.getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			// 创建角色的时候采取没有就插入数据，有就更新数据的策略
			String sqlstr = "INSERT INTO `role`(roleid, name, avatar, level) " + "VALUES ('" + roleId + "', '" + rolename + "', '"
					+ shapeid + "', '" + level + "') " + "ON DUPLICATE KEY UPDATE name='" + rolename + "', avatar=" + shapeid
					+ ", level=" + level;
			int ret = stmt.executeUpdate(sqlstr);
			fire.pb.friends.Module.logger.info("[" + roleId + "]InsertMysqlRealtion:" + sqlstr + ";ret = " + ret);
			updateRet = true;
		} catch (SQLException ex) {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				}
				stmt = null;
			}
			C3P0Util.close(conn, null, null);
			ex.printStackTrace();
			return false;
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
	// ----------------------------------------------------------------------//
	// 空间相关 END
	// ----------------------------------------------------------------------//
}
