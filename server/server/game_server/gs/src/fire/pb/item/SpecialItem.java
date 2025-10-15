package fire.pb.item;

import fire.pb.item.Commontext.UseResult;

public class SpecialItem extends GroceryItem {

	public static final int CLEAR_ITEM = 36020;
	public static final int STUPOR_ITEM = 36019;
	public static final int STUPOR_BUFF_ID = 500008;
	SpecialItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	SpecialItem(ItemMgr im, xbean.Item data) {
		super(im, data);
	}

	@Override
	public void onDeleted() {
	}

	@Override
	public void onInserted() {
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseSpecialItemHandler();
	}
	
	private static class UseSpecialItemHandler implements UseItemHandler {

		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			return Commontext.UseResult.SUCC;
		}
		
	}

}
