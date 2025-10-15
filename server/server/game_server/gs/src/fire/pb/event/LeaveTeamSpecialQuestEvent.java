package fire.pb.event;

public class LeaveTeamSpecialQuestEvent implements Event {
	private final long roleid;
	public LeaveTeamSpecialQuestEvent(final long roleid) {
		this.roleid = roleid;
	}

	@Override
	public long getRoleid() {
		return roleid;
	}
}
