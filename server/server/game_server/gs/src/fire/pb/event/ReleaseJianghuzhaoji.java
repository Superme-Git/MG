package fire.pb.event;

public class ReleaseJianghuzhaoji implements Event {
	private final long roleid;
	public ReleaseJianghuzhaoji( final long roleid ) {
		this.roleid = roleid;
	} 
	@Override
	public long getRoleid() {
		return roleid;
	}
}
