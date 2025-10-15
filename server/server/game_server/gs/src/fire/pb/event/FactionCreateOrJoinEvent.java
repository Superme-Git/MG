package fire.pb.event;

public class FactionCreateOrJoinEvent implements Event{

	private long roleId;
	
	public FactionCreateOrJoinEvent(long roleid){
		this.roleId = roleid;
	}
	
	@Override
	public long getRoleid() {
		return roleId;
	}

}
