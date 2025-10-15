package fire.pb.event;

public class AddGemToEquip implements Event {
	private final long roleid;
	private final int itemid;
	public AddGemToEquip(final long roleid, final int itemid) {
		this.roleid = roleid;
		this.itemid = itemid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}
	
	public int getItemid() {
		return itemid;
	}

}
