package fire.pb.item.food;

import fire.pb.item.FoodItem;
import fire.pb.item.ItemMgr;

public class EasyRecoveryItem extends FoodItem {
	public static final int 食盒物品id = 32032;
	public static final int 药罐物品id = 32033;
	public static final int 宠物罐头物品id = 32034;
	
	public EasyRecoveryItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public EasyRecoveryItem(ItemMgr im, xbean.Item xitem) {
		super(im, xitem);
	}
}
