package fire.pb.item.groceries;

import java.util.ArrayList;
import java.util.List;

import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.SItemToPet;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.main.ConfigManager;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;

public class PetItem extends GroceryItem {

	public PetItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	public PetItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}

	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UsePetItemHandler();
	}
	
	private static class UsePetItemHandler implements UseItemHandler {
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			if (usednum != 1)
				return Commontext.UseResult.FAIL;
			
			SItemToPet petInfo = fire.pb.main.ConfigManager.getInstance().getConf(SItemToPet.class).get(bi.getItemId());
			if (petInfo == null)
				return Commontext.UseResult.FAIL;
			
			PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.PET,	false);
			if (petColumn.size() >= petColumn.getCapacity()) {
				MessageMgr.sendMsgNotify(roleId, 160198, null);
				return Commontext.UseResult.FAIL;// 宠物栏已满
			}
			
			int ret = petColumn.addPetByID(petInfo.getPetId(), true, PetColumn.ADD_REASON_ITEM);
			if (ret < 0) {
				return Commontext.UseResult.FAIL;
			}
			
			PetAttr confs = ConfigManager.getInstance().getConf(PetAttr.class).get(petInfo.petId);
			List<String> para = new ArrayList<String>();
			para.add(confs.getName());
			MessageMgr.sendMsgNotify(roleId, 160142, para);
			return Commontext.UseResult.SUCC;
		}
	}

}
