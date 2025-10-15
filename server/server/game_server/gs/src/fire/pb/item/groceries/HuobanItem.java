package fire.pb.item.groceries;

import java.util.ArrayList;
import java.util.List;

import fire.pb.huoban.PActiveHuoBan;
import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.SItemToHuoban;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SHeroBaseInfo;
import fire.pb.talk.MessageMgr;

public class HuobanItem extends GroceryItem {

	public HuobanItem(ItemMgr im, int itemid) {
		super(im, itemid);
		// TODO Auto-generated constructor stub
	}
	
	public HuobanItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}

	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UseHuobanItemHandler();
	}
	
	private static class UseHuobanItemHandler implements UseItemHandler {

		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			if (usednum != 1)
				return Commontext.UseResult.FAIL;
			
			SItemToHuoban huobanItem = fire.pb.main.ConfigManager.getInstance().getConf(SItemToHuoban.class).get(bi.getItemId());
			if (huobanItem == null)
				return Commontext.UseResult.FAIL;
			
			PActiveHuoBan huobanPro = new PActiveHuoBan(roleId, huobanItem.getHuobanId(), 2, huobanItem.getDaysType());
			if (huobanPro.call()) {
				SHeroBaseInfo huobanInfo = ConfigManager.getInstance().getConf(SHeroBaseInfo.class).get(huobanItem.huobanId);
				List<String> para = new ArrayList<String>();
				para.add(huobanInfo.getName());
				Integer days = 0;
				switch (huobanItem.getDaysType()) {
				case 0:
					days = 7;
					break;
				case 1:
					days = 30;
					break;
				default:
					break;
				}
				
				if (huobanItem.getDaysType() < 2) {
					para.add(String.valueOf(days));
				} else {
					para.add("无限");
				}
				MessageMgr.sendMsgNotify(roleId, 160143, para);
				
				return Commontext.UseResult.SUCC;
			}
			
			return Commontext.UseResult.FAIL;
		}
	}
}
