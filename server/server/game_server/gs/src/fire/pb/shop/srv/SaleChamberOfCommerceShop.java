package fire.pb.shop.srv;

import java.util.ArrayList;
import java.util.List;

import fire.pb.effect.Role;
import fire.pb.item.ItemBase;
import fire.pb.shop.srv.limit.LimitManager;
import fire.pb.shop.utils.ShopParameters;
import fire.pb.talk.MessageMgr;
import fire.pb.tel.utils.GoodsSafeLocksUtils;

/**
 * 出售商品给商会
 * 
 * @author liangyanpeng
 *
 */
public class SaleChamberOfCommerceShop extends SaleShopBase implements Sale {

	protected ItemBase item;
	
	int itemkey;
	
	public SaleChamberOfCommerceShop(ShopParameters params) {
		super(params);
		this.itemkey = params.itemkey;
	}

	@Override
	public boolean removeGoods() {
		return new StoreBagContainer(this, bag).remove(itemkey) ;
	}

	@Override
	public boolean add() { 
		return commonAddMoney(getSellMoney() * num);
	}

	@Override
	public boolean verify() {
		if (!super.commonVerify())
			return false;
		item = bag.getItem(itemkey);
		if (item == null)
			return false;
//		if (item.isLocked() != -1) {
//			fire.pb.talk.Message.psendMsgNotify(role.getRoleId(), BasicItem.ITEM_LOCK_ERROR, null);
//			return false;
//		}
//		if (!(item instanceof EquipItem) || item.getAttr().可否卖店 != 1) {
//			return false;
//		}
		
		// 道具锁判断
		if (GoodsSafeLocksUtils.checkLockStatus(role.getRoleId(),item)) {
			return false;
		}
		return true;
	}

	@Override
	public void success() {
		super.commonSuccess();
		
		//通知客户端
		List<String> parameters = new ArrayList<>(1);
		parameters.add("" + num);
		parameters.add(this.name);
		parameters.add("" + getSellMoney() * num);
		int surplus = goods.limitSaleNum - LimitManager.getInstance().retSaleNum(role.getRoleId(), goods);
		parameters.add("" + surplus);
		MessageMgr.psendMsgNotify(role.getRoleId(), 150504, parameters);
	}

	@Override
	public void fail() {

	}

	@Override
	public boolean exc() {
		if (!verify()) {
			return false;
		}
		if (!removeGoods()) {
			return false;
		}
		if (!add()) {
			fail();
			return false;
		}
		success();
		//清除数据
		GoodsSafeLocksUtils.doClearDataWhileCommit(role.getRoleId());
		return true;
	}
	
	/**
	 * @return 售卖价格
	 */
	public long getSellMoney() {
		return price * 5 / 6;
	}

}
