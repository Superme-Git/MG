package fire.pb.item.groceries;

import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;


public class TailorDye extends GroceryItem{
	
	public TailorDye(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	public TailorDye(ItemMgr im, xbean.Item item) {
		super(im, item);
	}
	
	public TailorDye(ItemMgr im, int itemid, mkdb.Bean extinfo) {
		super(im, itemid);
		if (extinfo != null && !(extinfo instanceof xbean.TicketItemExInfo)) {
			throw new IllegalArgumentException("");
		}
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseTailorDyeHandler();
	}
	
	@Override
	public void onDeleted() {

	}
	
	private static class UseTailorDyeHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			return Commontext.UseResult.FAIL;
		}
	}
}
