package fire.pb.event;

public class EnterPetLuckEvent implements Event{
	
	private final long roleid;
	
	public EnterPetLuckEvent(long roleId){
		this.roleid = roleId;
	}

	@Override
	public long getRoleid() {
		return roleid;
	}
}
