package fire.pb.event;

public class UnequipItemEvent implements Event {
	private final long roleid;
	private final int itemid; // 装备物品的id
	public UnequipItemEvent( final long roleid, final int itemid ) {
		this.roleid = roleid;
		this.itemid = itemid;
	}
	@Override
	public long getRoleid() { return roleid; }
	public int getItemid() { return itemid; }
}
