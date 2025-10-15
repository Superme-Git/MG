package fire.pb.event;

public class GetMasterEvent implements Event {
	private final long roleid;
	private final long masterid;
	public GetMasterEvent(final long roleid, final long masterid) {
		this.roleid = roleid;
		this.masterid = masterid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

	public long getMasterid() {
		return masterid;
	}
}
