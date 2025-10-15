package fire.pb.item;

import fire.pb.event.Event;

public class EquipMakeEvent implements Event {
	private final long roleId;
	public EquipMakeEvent(final long roleId) {
		this.roleId = roleId;
	}
	@Override
	public long getRoleid() {
		return roleId;
	}

}
