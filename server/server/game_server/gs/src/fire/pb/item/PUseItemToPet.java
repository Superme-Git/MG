package fire.pb.item;


public class PUseItemToPet extends DPUseItem {
	private final int petkey;
	public PUseItemToPet(final long roleId, final int keyinpack, final int usednum, final int petkey) {
		this(roleId, keyinpack, usednum, petkey, true);
	}
	
	public PUseItemToPet(final long roleId, final int keyinpack, final int usednum, final int petkey, final boolean singleplayer) {
		super(roleId, keyinpack, usednum, singleplayer);
		this.petkey = petkey;
	}

	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseItemToPet(petkey);
	}
}
