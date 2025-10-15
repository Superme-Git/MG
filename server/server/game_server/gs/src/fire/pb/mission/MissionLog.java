package fire.pb.mission;

public interface MissionLog {
	public long getRoleId();
	public int getMissionId();
	public void write();
	public MissionLogType getType();
}
