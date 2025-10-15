package fire.pb.item.groceries;

import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;


public class FormBookHalf extends GroceryItem{

	public FormBookHalf(ItemMgr im, int itemid) {
		super(im, itemid);

	}
	
	public FormBookHalf(ItemMgr im, xbean.Item item) {
		super(im, item);

	}
	
	public FormBookHalf(ItemMgr im, int itemid, mkdb.Bean extinfo) {
		super(im, itemid);

	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseFormBookHalfHandler();
	}
	
	@Override
	public void onDeleted() {

	}
	
	private static class UseFormBookHalfHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			//2 固定为光环合成
			fire.pb.item.PProduceItem pItem = new fire.pb.item.PProduceItem(roleId, 2, bi.getNumber(), bi.getKey(), 0);
			if (pItem.call()) {
				return Commontext.UseResult.SUCC;
			}
			return Commontext.UseResult.FAIL;
		}
	}
}
