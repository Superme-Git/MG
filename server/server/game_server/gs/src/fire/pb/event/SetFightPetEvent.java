package fire.pb.event;
/**
 */
public class SetFightPetEvent implements Event {
	private final long roleid;
	private final int petkey; // -1为没有参战宠物
	private final int petid; // -1为没有参战宠物
	private final int color;
	public SetFightPetEvent( final long roleid, final int petkey, final int petid, final int color ) {
		this.roleid = roleid;
		this.petkey = petkey;
		this.petid = petid;
		this.color = color;
	}
	@Override
	public long getRoleid() { return roleid; }
	public int getPetkey() { return petkey; }
	public int getPetid() { return petid; }
	public int getColor() { return color; }
}
