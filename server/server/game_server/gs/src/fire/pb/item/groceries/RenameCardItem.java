package fire.pb.item.groceries;

import fire.pb.SRequestUsedNameData;
import fire.pb.item.Commontext;
import fire.pb.item.Commontext.UseResult;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;

public class RenameCardItem extends GroceryItem {

	public RenameCardItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	public RenameCardItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}

	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseRenameCardItemHandler();
	}
	
	private static class UseRenameCardItemHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			if(usednum != 1){
				return Commontext.UseResult.FAIL;
			}
			
			SRequestUsedNameData send = new SRequestUsedNameData();
			send.itemkey = bi.getKey();
			int num = 0;
			final xbean.Properties prop = xtable.Properties.select(roleId);
			for (String name : prop.getUsedname()) {
				if(num == 8){
					break;
				}
				send.usednames.add(name);
			}
			mkdb.Procedure.psendWhileCommit(roleId, send);
			return Commontext.UseResult.SUCC_NODEL;
		}
	}
}
