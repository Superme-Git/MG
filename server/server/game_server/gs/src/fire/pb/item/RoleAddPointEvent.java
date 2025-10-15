package fire.pb.item;

import fire.pb.event.Event;

public class RoleAddPointEvent implements Event {
	private final long roleId;
	public RoleAddPointEvent(final long roleId) {
		this.roleId = roleId;
	}
	@Override
	public long getRoleid() {
		return roleId;
	}

}
