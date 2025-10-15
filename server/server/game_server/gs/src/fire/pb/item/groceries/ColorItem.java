package fire.pb.item.groceries;

import fire.pb.item.GroceryItem;
import fire.pb.item.ItemMgr;

public class ColorItem extends GroceryItem {

	public ColorItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	public ColorItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}

}
