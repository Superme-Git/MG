package fire.pb.item;

public class TaskItem extends ItemBase {
	
	public TaskItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}
	
	public TaskItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	@Override
	protected void onDeleted() {
	}

	@Override
	protected void onInserted() {
	}

}
