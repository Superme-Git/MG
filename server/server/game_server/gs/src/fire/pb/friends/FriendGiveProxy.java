package fire.pb.friends;

import java.util.Map;

import fire.pb.util.DateValidate;

/**
 * 好友赠送代理
 * @author XGM
 */
public class FriendGiveProxy {

	public static final int DAY_GIVE_MAX = 5; // 每日赠送上限

	/**
	 * 获取代理
	 * @param roleId
	 * @param readonly
	 * @return
	 */
	public static FriendGiveProxy getFriendGiveProxy(long roleId, boolean readonly) {

		xbean.FriendGiveNum xFriendGive = null;
		if (readonly) {
			xFriendGive = xtable.Friendgivenum.select(roleId);
		} else {
			xFriendGive = xtable.Friendgivenum.get(roleId);
		}

		if (xFriendGive == null) {
			if (readonly) {
				xFriendGive = xbean.Pod.newFriendGiveNumData();
			} else {
				xFriendGive = xbean.Pod.newFriendGiveNum();
				xtable.Friendgivenum.insert(roleId, xFriendGive);
			}
		}

		return new FriendGiveProxy(roleId, readonly, xFriendGive);
	}

	private final long roleId;
	private final boolean readonly;
	private xbean.FriendGiveNum xFriendGive;

	/**
	 * 构造
	 * @param roleId
	 * @param readonly
	 * @param xFriendGive
	 */
	public FriendGiveProxy(long roleId, boolean readonly, xbean.FriendGiveNum xFriendGive) {
		this.roleId = roleId;
		this.readonly = readonly;
		this.xFriendGive = xFriendGive;
	}

	public long getRoleId() {
		return roleId;
	}

	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * 修正
	 */
	public void fix() {
		// 隔天重置
		long now = System.currentTimeMillis();
		if (DateValidate.inTheSameDay(now, xFriendGive.getResettime()) == false) {
			xFriendGive.getGivenummap().clear();
			xFriendGive.setResettime(now);
		}
	}

	/**
	 * 获取赠送数量
	 * @param targetRoleId
	 * @return
	 */
	public int getGiveNum(long targetRoleId) {
		fix();
		Integer ret = xFriendGive.getGivenummap().get(targetRoleId);
		return ret != null ? ret : 0;
	}

	/**
	 * 增加赠送数量
	 * @param targetRoleId
	 * @param addNum
	 */
	public void addGiveNum(long targetRoleId, int addNum) {
		fix();
		int num = getGiveNum(targetRoleId) + addNum;
		xFriendGive.getGivenummap().put(targetRoleId, num);
	}

	/**
	 * 获取赠送数量列表
	 * @return
	 */
	public Map<Long, Integer> getGiveNumMap() {
		fix();
		return xFriendGive.getGivenummap();
	}

	/**
	 * 获得每日赠送上限
	 * @return
	 */
	public static int getDayGiveMax() {
		return DAY_GIVE_MAX;
	}
}
