package fire.pb.event;

public class PetStarEvent implements Event {
	private final long roleid;
	public PetStarEvent(final long roleid) {
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

}
