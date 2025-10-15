package fire.pb.scene;

import fire.pb.scene.movable.Body;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.movable.Role;

/**
 * <pre>
 * 屏幕索引区块,就是将地图分成比Grid更大的区块,
 * 用来建立信息索引 .其中主要用来存储这一区域内角色、NPC等信息
 * 其每个区块周围有八个相邻块，客户端将同时收到九个区块的信息
 * </pre>
 */
public class Screen {

	Screen(int index) {
		this.index = index;
	}

	int getIndex() {
		return index;
	}

	/**
	 * 获取该 屏块中所有的角色
	 */
	public java.util.Map<Long, Role> getAllRoles() {
		return roles;
	}

	/**
	 * 获取该 屏块中所有特殊角色
	 */
	public java.util.Map<Long, Role> getAllSpecialRoles() {
		return specialRoles;
	}

	/**
	 * 获取屏块中的所有NPC对象
	 */
	java.util.Map<Long, NPC> getNpcs() {
		return npcs;
	}

	void removeBody(Body body) {
		if (body instanceof Role) {
			final Role role = (Role) body;
			Role rmvRole = roles.remove(role.getRoleID());
			if (rmvRole == null)
				SceneManager.logger
						.error("remove role from scene failed.roleid:" + role.getRoleID() + "screen:" + getIndex());

			// 摆摊或者是 gm
			if (role.isSpecialRole())
				specialRoles.remove(role.getRoleID());
		}

		if (body instanceof NPC) {
			final NPC npc = (NPC) body;
			NPC rmvNpc = npcs.remove(npc.getNpcKey());
			if (rmvNpc == null)
				SceneManager.logger
						.error("remove npc from scene failed.roleid:" + npc.getNpcKey() + "screen:" + getIndex());
		}
	}

	void addBody(Body body) {
		if (body instanceof Role) {
			final Role role = (Role) body;
			roles.put(role.getRoleID(), role);

			// 摆摊或者是 gm
			if (role.isSpecialRole())
				specialRoles.put(role.getRoleID(), role);
		}

		if (body instanceof NPC) {
			final NPC npc = (NPC) body;
			npcs.put(npc.getNpcKey(), npc);
		}
	}

	/**
	 * 开始摆摊，在屏块内添加
	 */
	void addSpecial(Role role) {
		specialRoles.put(role.getRoleID(), role);
	}

	/**
	 * 停止摆摊，如果不是gm删除
	 */
	void removeSpecial(Role role) {
		if (!role.isSpecialRole())
			specialRoles.remove(role.getRoleID());
	}

	private final int index;
	private final java.util.Map<Long, Role> roles = new java.util.HashMap<Long, Role>();
	private final java.util.Map<Long, NPC> npcs = new java.util.HashMap<Long, NPC>();

	// 该屏块内必能看见的人 eg, gm,摆摊的人
	private final java.util.Map<Long, Role> specialRoles = new java.util.HashMap<Long, Role>();
}
