package fire.pb.mission;

public class MissionAbandonLog implements MissionLog {
	private final long roleid;
	private final int missionid;
	public MissionAbandonLog( final long roleid, final int missionid ) {
		this.roleid = roleid;
		this.missionid = missionid;
	}
	@Override
	public long getRoleId() {
		return roleid;
	}

	@Override
	public int getMissionId() {
		return missionid;
	}

	@Override
	public MissionLogType getType() {
		return MissionLogType.ABANDON;
	}
	@Override
	public void write() {
		Module.logger.debug("人物id=" + roleid + "放弃了任务" + missionid);
		Module.getInstance().untrackMission( roleid, missionid );
	}

}
