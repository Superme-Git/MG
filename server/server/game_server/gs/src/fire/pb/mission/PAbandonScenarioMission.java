package fire.pb.mission;

public class PAbandonScenarioMission extends PMissionAbandon {
	
	public PAbandonScenarioMission(final long roleid, final int missionid) {
		super(roleid, missionid);
	}
	
	@Override
	public boolean abandonExecute() {
		final int lineid = UtilHelper.getMissionLineid(missionid);
		RoleMission mission = RoleMission.getScenarioTaskInstance(roleid, lineid);
		if (mission == null) {
			return false;
		}
		return mission.abandon();
	}
	
}
