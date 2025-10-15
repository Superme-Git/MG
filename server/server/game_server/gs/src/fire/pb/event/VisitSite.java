package fire.pb.event;

public class VisitSite implements Event {
	private final long roleid;
	private final int siteid;
	public VisitSite(final long roleid, final int siteid) {
		this.roleid = roleid;
		this.siteid = siteid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

	public int getSiteid() {
		return siteid;
	}
}
