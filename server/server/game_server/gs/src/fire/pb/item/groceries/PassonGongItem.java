package fire.pb.item.groceries;

import fire.pb.item.Commontext;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;

public class PassonGongItem extends OtherItem {
	
	public PassonGongItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public PassonGongItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseChuangongItem();
	}
	
	private static class UseChuangongItem implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			
			
			return Commontext.UseResult.FAIL;
		}
	}
}
