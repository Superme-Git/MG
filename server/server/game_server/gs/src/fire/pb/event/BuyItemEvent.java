package fire.pb.event;

public class BuyItemEvent implements Event {
	private final long roleid;
	private final int relatedid;	//itemid or petid
	public BuyItemEvent(final long roleid, final int relatedid) {
		this.roleid = roleid;
		this.relatedid = relatedid;
	}
	public int getRelatedid() {return relatedid;}
	@Override
	public long getRoleid() {return roleid;}
}
