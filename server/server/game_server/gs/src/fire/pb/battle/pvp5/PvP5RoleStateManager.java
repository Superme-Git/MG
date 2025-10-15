package fire.pb.battle.pvp5;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 5v5角色状态管理
 * @author XGM
 */
public class PvP5RoleStateManager {

	// 角色PVP状态:
	public static final int PVPSTATE_DEFAULT = 0; 			// 默认
	public static final int PVPSTATE_MATCH = 1;				// 匹配中
	public static final int PVPSTATE_BATTLE_PREPARE = 2;	// 匹配结束准备战斗中

	public Map<Long, Integer> rolesPvPState = new ConcurrentHashMap<Long, Integer>();

	static class SingletonHolder {
		static PvP5RoleStateManager singleton = new PvP5RoleStateManager();
	}

	public static PvP5RoleStateManager getInstance() {
		return SingletonHolder.singleton;
	}

	public void init() {
		rolesPvPState.clear();
	}

	/**
	 * 检测角色的PVP状态能不能进行队伍操作
	 * @param roleId
	 * @return
	 */
	public boolean checkPvPStateCanOperationTeam(long roleId) {
		// 在竞技场中,检测状态
		final fire.pb.map.Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole != null && PvP5Helper.isPvPMap(mapRole.getMapId())) {
			Integer state = rolesPvPState.get(roleId);
			if (state != null && state != PVPSTATE_DEFAULT) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检测PvP状态
	 * @param roleId
	 * @param state
	 * @return
	 */
	public boolean checkPvPState(long roleId, int state) {
		Integer s = rolesPvPState.get(roleId);
		if (s != null) {
			if (s == state) {
				return true;
			}
		} else {
			if (state == PVPSTATE_DEFAULT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置角色PVP状态
	 * @param roleId
	 * @param state
	 */
	public void setRolePvPState(long roleId, int state) {
		rolesPvPState.put(roleId, state);
	}

}
