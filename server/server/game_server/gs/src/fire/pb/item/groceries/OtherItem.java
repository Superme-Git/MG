package fire.pb.item.groceries;

import java.util.ArrayList;
import java.util.List;

import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.title.SItemTitleConfig;
import fire.pb.title.STitleConfig;
import fire.pb.title.Title;
import fire.pb.title.TitleManager;

public class OtherItem extends GroceryItem {

	public OtherItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public OtherItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}

	@Override
	public void setOwnerid(final long roleId) {
		super.setOwnerid(roleId);
	}

	@Override
	protected UseItemHandler getUseItemHandler() {
		if (isTitleItem(getItemId())) {
			return new UseTitleItemHandler();
		}

		return super.getUseItemHandler();
	}

	private boolean isTitleItem(int itemid) {
		java.util.Map<Integer, SItemTitleConfig> titleItem = ConfigManager
				.getInstance().getConf(SItemTitleConfig.class);
		if (titleItem.containsKey(itemid))
			return true;
		return false;
	}

	private class UseTitleItemHandler implements UseItemHandler {

		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			// add title;
			// 使用道具获得称谓
			SItemTitleConfig titleConfig = ConfigManager.getInstance()
					.getConf(SItemTitleConfig.class).get(bi.getItemId());
			if (null != titleConfig) {
				int titleid = titleConfig.getTitleID();
				java.util.Map<Integer, xbean.TitleInfo> allTitles = xtable.Properties
						.selectTitles(roleId);
				if (!allTitles.containsKey(titleid)) {
					Title title = new Title(roleId, false);
					title.addTitle(titleid, "", 0);
					title.onTitle(titleid);
					List<String> param = new ArrayList<String>();
					STitleConfig config = TitleManager
							.getTitleConfigById(titleid);
					if (null != config) {
						param.add(config.getTitlename());
					}
					MessageMgr.psendMsgNotify(roleId, 143001, param);
				}
			}
			return UseResult.SUCC;
		}

	}
}
