package fire.pb.blackmarket.srv;

import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;

/**
 * 黑市背包容器
 * 
 * @author liangyanpeng
 *
 */
public class BlackMarket extends ItemMaps {

	public BlackMarket(long roleId, boolean readonly) {
		super(roleId, readonly);
	}

	@Override
	protected int getPackid() {
		return BagTypes.BLACKMARKET;
	}

	/**
	 * 上架
	 */
	@Override
	public AddItemResult doAddItem(final ItemBase item, final int p, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {

		if (BlackMarketManager.LOG.isInfoEnabled() && item != null) {
			BlackMarketManager.LOG.debug("黑市上架物品:srcItem={itemname=" + item.getName() + ", itemid=" + item.getItemId()
					+ ", key=" + item.getKey() + ", position=" + item.getPosition());
		}

		return super.doAddItem(item, p, reason, countertype, xiangguanid);
	}

	/**
	 * 下架
	 */
	@Override
	public ItemBase TransOut(final int key, final int number, final String reason) {
		ItemBase item = super.TransOut(key, number, reason);
		if (BlackMarketManager.LOG.isInfoEnabled() && item != null) {
			BlackMarketManager.LOG.debug("黑市下架物品:srcItem={itemname=" + item.getName() + ", itemid=" + item.getItemId()
					+ ", key=" + item.getKey() + ", position=" + item.getPosition());
		}
		return item;
	}

}
