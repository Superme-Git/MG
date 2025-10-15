package fire.pb.event;

public class EnterWorldEvent implements Event {
	private final long roleid;
	public EnterWorldEvent( final long roleid ) {
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() { return roleid; }
}
