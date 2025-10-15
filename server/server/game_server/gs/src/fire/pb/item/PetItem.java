package fire.pb.item;

public class PetItem extends ItemBase {
	public PetItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public PetItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}

	@Override
	public void onDeleted() {
	}

	@Override
	public void onInserted() {
	}
}
