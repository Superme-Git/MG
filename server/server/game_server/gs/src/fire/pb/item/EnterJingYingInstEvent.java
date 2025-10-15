package fire.pb.item;

import fire.pb.event.Event;

public class EnterJingYingInstEvent implements Event {
	private final long roleId;
	public EnterJingYingInstEvent(final long roleId) {
		this.roleId = roleId;
	}
	@Override
	public long getRoleid() {
		return roleId;
	}

}
