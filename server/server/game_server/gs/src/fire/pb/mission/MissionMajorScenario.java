package fire.pb.mission;

import fire.pb.mission.MissionStatus;



public class MissionMajorScenario extends RoleMission {
	public MissionMajorScenario(long roleid, boolean readonly) {
		super(roleid, readonly);
		if (readonly) {
			data = xtable.Majorscenariomission.select( roleid );
			if (data == null) {
				data = xbean.Pod.newMissionData();
				data.setStatus(MissionStatus.UNACCEPT);
			}
		} else {
			data = xtable.Majorscenariomission.get( roleid );
			if (data == null) {
				data = xbean.Pod.newMission();
				data.setStatus(MissionStatus.UNACCEPT);
				xtable.Majorscenariomission.insert(roleid, data);
			}
		}
		conf = MissionManager.getInstance().getMissionConfig(getId());
	}
}
