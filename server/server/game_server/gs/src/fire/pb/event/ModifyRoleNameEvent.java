package fire.pb.event;

/**
 * 角色改名事件
 * @author XGM
 */
public class ModifyRoleNameEvent implements Event {

	private final long roleId;
	private final String oldName;
	private final String newName;

	public ModifyRoleNameEvent(long roleId, String oldName, String newName) {
		this.roleId = roleId;
		this.oldName = oldName;
		this.newName = newName;
	}

	@Override
	public long getRoleid() {
		return roleId;
	}

	public String getOldName() {
		return oldName;
	}

	public String getNewName() {
		return newName;
	}
}
