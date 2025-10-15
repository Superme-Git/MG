package fire.pb.item.groceries;

import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;


public class RoleParticleBook extends GroceryItem{
	
	public RoleParticleBook(ItemMgr im, int itemid) {
		super(im, itemid);

	}
	
	public RoleParticleBook(ItemMgr im, xbean.Item item) {
		super(im, item);

	}
	
	public RoleParticleBook(ItemMgr im, int itemid, mkdb.Bean extinfo) {
		super(im, itemid);

	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseFormBookHandler();
	}
	
	@Override
	public void onDeleted() {

	}
	
	private static class UseFormBookHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			return Commontext.UseResult.FAIL;
		}
	}
}
