package fire.pb.event;

public class SetProtectPasswordEvent implements Event {
	private final long roleid;
	public SetProtectPasswordEvent(final long roleid) {
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

}
