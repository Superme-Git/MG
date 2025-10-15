package fire.pb.event;

public class PetColumnChange implements Event {
	private final long roleid;
	private final int petid;
	public PetColumnChange( final long roleid, final int petid ) {
		this.roleid = roleid;
		this.petid = petid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}
	public int getPetid() { return petid; }
}
