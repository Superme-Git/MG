package fire.pb.event;

public class ReleaseApprenticeInfoEvent implements Event {
	private final long roleid;
	public ReleaseApprenticeInfoEvent(final long roleid) {
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

}
