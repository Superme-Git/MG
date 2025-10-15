package fire.pb.item.groceries;

import mkdb.Procedure;
import fire.pb.item.Commontext;
import fire.pb.item.Commontext.UseResult;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;


public class RideItem extends GroceryItem{
	
	public RideItem(ItemMgr im, int itemid) {
		super(im, itemid);

	}
	
	public RideItem(ItemMgr im, xbean.Item item) {
		super(im, item);

	}
	
	public RideItem(ItemMgr im, int itemid, mkdb.Bean extinfo) {
		super(im, itemid);

	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseRideItemHandler();
	}
	
	@Override
	public void onTimeout() {
		super.onTimeout();
		
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop != null)
		{
			int ride = prop.getRide();
			if (ride == this.getItemId())
			{
				Procedure.pexecuteWhileCommit(new fire.pb.item.PEquipRideProc(roleid, 0, this.keyinpack));
			}
		}
	}

	@Override
	public void onDeleted()
	{
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop != null)
		{
			int ride = prop.getRide();
			if (ride == this.getItemId())
			{
				Procedure.pexecuteWhileCommit(new fire.pb.item.PEquipRideProc(roleid, 0, this.keyinpack));
			}
		}		
	}
	
	private static class UseRideItemHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			
			Procedure.pexecuteWhileCommit(new fire.pb.item.PEquipRideProc(roleId, bi.getItemId(), bi.getKey()));		
			return Commontext.UseResult.SUCC_NODEL;
		}
	}
}
