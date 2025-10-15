package fire.pb.item.food;

import fire.pb.item.FoodItem;
import fire.pb.item.ItemMgr;

public class GeneralFood extends FoodItem {
	public GeneralFood(ItemMgr im, int number, mkdb.Bean extinfo) {
		super(im, number, extinfo);
	}

	public GeneralFood(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public GeneralFood(ItemMgr im, xbean.Item item) {
		super(im, item);
	}
}
