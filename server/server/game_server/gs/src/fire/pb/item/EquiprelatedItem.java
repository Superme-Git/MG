package fire.pb.item;

public class EquiprelatedItem extends ItemBase {
	
	public EquiprelatedItem(ItemMgr im, int itemid) {
		super(im, itemid );
		// TODO Auto-generated constructor stub
	}
	
	public EquiprelatedItem(ItemMgr im, xbean.Item item) {
		super(im, item);
		// TODO Auto-generated constructor stub
	}



	@Override
	public void onDeleted() {
	}

	@Override
	public void onInserted() {
	}
}
