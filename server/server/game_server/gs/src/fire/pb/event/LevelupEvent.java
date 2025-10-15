package fire.pb.event;

public class LevelupEvent implements Event {
	private final long roleid;
	private final int level;
	public LevelupEvent( final long roleid, final int level ) {
		this.roleid = roleid;
		this.level = level;
	}
	@Override
	public long getRoleid() { return roleid; }
	public int getLevel() { return level; }
}
