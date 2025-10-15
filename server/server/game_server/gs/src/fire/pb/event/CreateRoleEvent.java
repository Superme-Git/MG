package fire.pb.event;

public class CreateRoleEvent implements Event {
	private final long roleid;
	private final int school;
	public CreateRoleEvent( final long roleid, final int school ) {
		this.roleid = roleid;
		this.school = school;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}
	public int getSchool() {
		return school;
	}
}
