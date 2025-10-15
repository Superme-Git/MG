package fire.pb.item.groceries;

import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.SItemToDbPoint;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;

public class DoublePointItem extends GroceryItem {

	public DoublePointItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	public DoublePointItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseDoublePointItemHandler();
	}
	
	public static class UseDoublePointItemHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			SItemToDbPoint itemInfo = fire.pb.main.ConfigManager.getInstance().getConf(SItemToDbPoint.class).get(bi.getItemId());
			if (itemInfo == null)
				return Commontext.UseResult.FAIL;
			if (new fire.pb.hook.PAddDpointCntProc(roleId, itemInfo.dbpoint).call()) {
				return Commontext.UseResult.SUCC;
			}
			return Commontext.UseResult.FAIL;
		}
	}
}
