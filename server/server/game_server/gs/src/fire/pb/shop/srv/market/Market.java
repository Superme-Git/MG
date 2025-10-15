package fire.pb.shop.srv.market;

import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;

/**
 * 摆摊道具容器(包括普通物品和装备)
 * 
 * @author liangyanpeng
 *
 */
public class Market extends ItemMaps {

	public Market(long roleid, boolean readonly) {
		super(roleid, readonly);
	}

	@Override
	protected int getPackid() {
		return BagTypes.MARKET;
	}

	/**
	 * 上架
	 */
	@Override
	public AddItemResult doAddItem(final ItemBase item, final int p, final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype,
			final int xiangguanid) {
		if (MarketManager.LOG.isDebugEnabled() && item != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("摆摊上架物品:srcItem={itemname=").append(item.getName()).append(", itemid=").append(item.getItemId())
					.append(", key=").append(item.getKey()).append(", position=").append(item.getPosition());
			MarketManager.LOG.debug(sb.toString());
		}
		return super.doAddItem(item, p, reason, countertype, xiangguanid);
	}

	
	/**
	 * 下架
	 */
	@Override
	public ItemBase TransOut(final int key, final int number, final String reason) {
		ItemBase item = super.TransOut(key, number, reason);
		if (MarketManager.LOG.isDebugEnabled() && item != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("摆摊下架物品:srcItem={itemname=").append(item.getName()).append(", itemid=").append(item.getItemId())
					.append(", key=").append(item.getKey()).append(", position=").append(item.getPosition());
			MarketManager.LOG.debug(sb.toString());
		}
		return item;
	}

	/**
	 * 获得某个道具的数量
	 * @param itemid
	 * @return 道具数量
	 */
	public int countItem(int itemid) {
		int count = 0;
		for (xbean.Item item : pack.getItems().values()) {
			if (item.getId() == itemid)
				count++;
		}
		return count;
	}

	/**
	 * 使用摆摊功能检查
	 * 
	 * @param roleId
	 * @return
	 */
	public static boolean useMarketCheck(long roleId) {
		// 战斗状态
//		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.StateType.STATE_BATTLE_FIGHTER))
//			return false;
		return true;
	}

}
