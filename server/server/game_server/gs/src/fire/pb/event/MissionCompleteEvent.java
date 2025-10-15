package fire.pb.event;


public class MissionCompleteEvent implements Event {
	private final long roleid;
	private final int missionID;
	public MissionCompleteEvent( final long roleid, final int missionID ) {
		this.missionID = missionID;
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() { return roleid; }
	public int getMissionID() { return missionID; }
}
