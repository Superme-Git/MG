package fire.pb.event;

public class RefineEquipEvent implements Event {
	private final long roleid;
	private final int itemid;
	private final int starnum;
	public RefineEquipEvent(final long roleid, final int itemid, final int starnum) {
		this.roleid = roleid;
		this.itemid = itemid;
		this.starnum = starnum;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

	public int getItemid() {
		return itemid;
	}
	
	public int getStarnum() {
		return starnum;
	}
}
