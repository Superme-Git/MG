package fire.pb.item;


public class GroceryItem extends ItemBase {
	
	public GroceryItem( ItemMgr im, int itemid ) {
		super( im, itemid );
	}
	
	public GroceryItem( ItemMgr im, xbean.Item item ) {
		super( im, item );
	}



	@Override
	public void onDeleted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInserted() {
		// TODO Auto-generated method stub

	}
	
	public int getLevel()
	{
		if (itemAttr != null)
			return itemAttr.getLevel();
		
		return 1;
	}

}
