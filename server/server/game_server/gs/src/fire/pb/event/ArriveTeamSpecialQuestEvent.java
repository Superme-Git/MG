package fire.pb.event;

public class ArriveTeamSpecialQuestEvent implements Event {

	private final long teamLeaderRoleId;
	private final long applierRoleId;
	
	public ArriveTeamSpecialQuestEvent(final long leaderId, final long applierId) {
		this.teamLeaderRoleId = leaderId;
		this.applierRoleId = applierId;
	}
	@Override
	public long getRoleid() {
		return teamLeaderRoleId;
	}
	
	public long getApplierRoleId() {
		return applierRoleId;
	}

}
