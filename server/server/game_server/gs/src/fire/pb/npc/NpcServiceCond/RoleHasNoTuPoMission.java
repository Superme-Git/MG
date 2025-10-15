package fire.pb.npc.NpcServiceCond;

import fire.pb.mission.MissionColumn;
import fire.pb.mission.MissionManager;
import fire.pb.mission.RoleMission;

public class RoleHasNoTuPoMission  implements Condition{
	
	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		MissionColumn tsc = new MissionColumn(roleid, true);
		boolean isfind = false;
		for (RoleMission task : tsc) {
			if (task.getConf() != null) {
				if(MissionManager.getInstance().getTuPoTaskList().contains(task.getId())) {
					isfind = true;
					break;
				} else if(task.getId() / 10000 == 51) {
					isfind = true;
					break;
				}
			}
		}
		return !isfind;
	}
}
