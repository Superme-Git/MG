package fire.pb.event;

public class LevelBreakEvent implements Event {
	private final long roleid;
	private final int level;
	
	public LevelBreakEvent(final long roleid, final int level) {
		this.roleid = roleid;
		this.level = level;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}
	
	public int getRolelevel() {
		return level;
	}

}
