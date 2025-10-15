package fire.pb.item;

import fire.pb.event.Event;

public class EnterBingFengInstEvent implements Event {
	private final long roleId;
	public EnterBingFengInstEvent(final long roleId) {
		this.roleId = roleId;
	}
	@Override
	public long getRoleid() {
		return roleId;
	}

}
