package fire.pb.mission;

public class MissionAcceptLog implements MissionLog {
	private final long roleid;
	private final int missionid;
	public MissionAcceptLog( final long roleid, final int missionid ) {
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
		return MissionLogType.ACCEPT;
	}
	@Override
	public void write() {
		Module.logger.debug( "人物id=" + roleid + "接受了任务" + missionid );
//		fire.log.LogUtil.doAcceptTaskLog( roleid, taskid, taskid );
		Module.getInstance().trackAccpetMission( roleid, missionid );
	}

}
