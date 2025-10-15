package fire.pb.event;

public class ReleaseInfoEvent implements Event {
	private final long roleid;
	public ReleaseInfoEvent( final long roleid ) {
		this.roleid = roleid;
	} 
	@Override
	public long getRoleid() {
		return roleid;
	}

}
