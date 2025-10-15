package fire.pb.item;

import fire.pb.event.Event;

public class CombineItemEvent implements Event {
	private final long roleId;
	public CombineItemEvent(final long roleId) {
		this.roleId = roleId;
	}
	@Override
	public long getRoleid() {
		return roleId;
	}

}
