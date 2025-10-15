package fire.pb.event;
/**
 * 装备物品事件
 *
 */
public class EquipItemEvent implements Event {
	private final long roleid;
	private final int itemid; // 装备物品的id
	public EquipItemEvent( final long roleid, final int itemid ) {
		this.itemid = itemid;
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() { return roleid; }
	public int getItemid() { return itemid; }
}
