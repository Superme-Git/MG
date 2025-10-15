package fire.pb.mission.instance;

public class TurnGroup {
	public final int id;
	public final int activityid;
	public final int turnId;
	public final int turnType;
	public final int turnGroup;
	public final int instanceId;
	public final int activitytypeid;
	
	public TurnGroup(final int id, final int activityid, final int activitytypeid, final int turnid, final int turntype, final int turnGroup, final int instanceid) {
		this.id = id;
		this.activityid = activityid;
		this.activitytypeid = activitytypeid;
		this.turnId = turnid;
		this.turnType = turntype;
		this.turnGroup = turnGroup;
		this.instanceId = instanceid;
	}

}
