package fire.pb.mission;

public class PMissionAccept extends PMissionProc {
	
	protected final int missionid;
	protected final long roleid;
	
	public PMissionAccept(final long roleid, final int missionid) {
		this.missionid = missionid;
		this.roleid = roleid;
	}
	
	protected boolean acceptExecute() {
		return false;
	}

	
	@Override
	protected final boolean missionExecute() {
		if (!acceptExecute()) {
			return false;
		}
		getLogs().addLog(new MissionAcceptLog(roleid, missionid));
		return true;
	}

}
