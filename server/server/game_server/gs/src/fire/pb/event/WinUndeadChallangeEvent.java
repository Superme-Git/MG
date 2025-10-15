package fire.pb.event;

public class WinUndeadChallangeEvent implements Event {
	private final long roleid;
	private final int challangeid;
	public WinUndeadChallangeEvent(final long roleid, final int challangeid) {
		this.roleid = roleid;
		this.challangeid = challangeid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}
	
	public int getChallangeid() {
		return challangeid;
	}

}
