package fire.pb.item.groceries;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.attr.AttrType;
import fire.pb.item.Commontext;
import fire.pb.item.EquipItem;
import fire.pb.item.EquipItemType;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.Module;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.util.Misc;


public class DistributionPack extends GroceryItem{
	
	protected xbean.EnhancementAttr enhancement; // 道具附魔属性 by changhao
	
	public DistributionPack(ItemMgr im, int itemid) {
		super(im, itemid);
	
	}
	
	public DistributionPack(ItemMgr im, xbean.Item item) {
		super(im, item);
		
	}
	
	public DistributionPack(ItemMgr im, int itemid, mkdb.Bean extinfo) {
		super(im, itemid);
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseAttrUpItemHandler();
	}
	
	@Override
	public void onDeleted() {
	}
	
	public String getItemCode(){
		return "";
	}
	
	@Override
	public Octets getTips() {
		if ( os==null) {
			os = new OctetsStream();
		}
		return os;
	}
	
	private static class UseAttrUpItemHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum)
		{			
			return Commontext.UseResult.FAIL;			
		}
	}
}
