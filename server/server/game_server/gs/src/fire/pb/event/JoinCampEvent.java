package fire.pb.event;

public class JoinCampEvent implements Event{
	
	private final long roleid;
	
	public JoinCampEvent(long roleid){
		this.roleid = roleid;
	}

	@Override
	public long getRoleid() {
		return roleid;
	}

}
