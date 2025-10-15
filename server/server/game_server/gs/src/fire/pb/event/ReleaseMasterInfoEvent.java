package fire.pb.event;

public class ReleaseMasterInfoEvent implements Event {
	private final long roleid;
	public ReleaseMasterInfoEvent(final long roleid) {
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

}
