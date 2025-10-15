package fire.pb.mission;

public class PMissionCommit extends PMissionProc {
	protected final long roleid;
	protected final int missionid;
	public PMissionCommit(final long roleid, final int missionid) {
		this.roleid = roleid;
		this.missionid = missionid;
	}
	
	protected MissionReward commitExecute() {
		return null;
	}
	
	@Override
	protected boolean missionExecute() {
		MissionReward reward = commitExecute();
		if (reward == null) {
			return false;
		}
		getLogs().addLog(new MissionCommitLog(roleid, missionid, reward));
		return true;
	}

}
