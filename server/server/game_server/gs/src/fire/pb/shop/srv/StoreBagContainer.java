package fire.pb.shop.srv;

import fire.pb.ErrorCodes;
import fire.pb.Item;
import fire.pb.SError;
import fire.pb.item.AddItemResult;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Pack;

/**
 * 
 * 添加到背包
 * 
 * @author liangyanpeng
 * 
 */
public class StoreBagContainer implements Container {

	protected Shop shop;
	protected Pack bag;

	public StoreBagContainer(Shop shop, Pack bag) {
		super();
		this.shop = shop;
		this.bag = bag;
	}

	/**
	 * 向背包中增加道具
	 */
	@Override
	public boolean add(boolean isBind) {
		int leftnum = shop.num;
		ItemShuXing itemAttr = fire.pb.item.Module.getInstance().getItemManager().getAttr(shop.goods.getItemId());
		if (itemAttr == null) {
			Shop.LOG.error("item has no itemAttr.itemid:" + shop.goods.getItemId());
			return false;
		}
		int 最大堆叠数量 = itemAttr.maxstack;
		if (最大堆叠数量 <= 0)
			return false;
		while (leftnum > 0) {
			int addnum = leftnum < 最大堆叠数量 ? leftnum : 最大堆叠数量;
			ItemBase item = fire.pb.item.Module.getInstance().getItemManager().genItemBase(shop.goods.getItemId(),
					addnum);
			if (isBind) {
				item.setFlag(Item.BIND);
			}
			if (bag.doAddItem(item, -1, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_goumai, 0) != AddItemResult.SUCC) {
				SError send = new SError();
				send.error = ErrorCodes.AddItemToBagException;
				mkdb.Procedure.psendWhileRollback(shop.role.getRoleId(), send);
				return false;
			}
			leftnum -= addnum;
		}
		return true;
	}

	/**
	 * 从背包中移出道具
	 */
	@Override
	public boolean remove(int itemkey) {
		// 删除物品
		// 出售的itemid
		ItemBase item = bag.TransOut(itemkey, shop.num, "卖店");
		return item != null ? true : false;
	}

}
