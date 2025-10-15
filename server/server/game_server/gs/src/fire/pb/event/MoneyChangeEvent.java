package fire.pb.event;

public class MoneyChangeEvent implements Event {
	private final long roleid;
	public MoneyChangeEvent(final long roleid) {
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

}
