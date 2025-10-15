package fire.pb.event;

public class UpdateInbornLevel implements Event {
	private final long roleid;
	private final int apId;
	public UpdateInbornLevel( final long roleid, final int apId ) {
		this.roleid = roleid;
		this.apId = apId;
	}
	public int getApId() {
		return apId;
	}
	
	@Override
	public long getRoleid() {
		// TODO Auto-generated method stub
		return roleid;
	}

}
