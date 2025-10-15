package fire.pb.shop.srv;

import java.util.Arrays;

import fire.pb.item.ItemShuXing;
import fire.pb.pet.PetColumn;
import fire.pb.shop.utils.ShopParameters;
import fire.pb.talk.MessageMgr;

/**
 * 宠物商店购买放入宠物栏中
 * 
 * @author liangyanpeng
 *
 */
public class BuyShenShouShop extends BuyShopBase implements Buy {

	protected PetColumn petcol;

	public BuyShenShouShop(ShopParameters params) {
		super(params);
	}

	@Override
	public boolean cost() {
		return super.commonCost();
	}

	@Override
	public void success() {
		super.commonSuccess();
		//购买成功消息
		MessageMgr.sendMsgNotify(role.getRoleId(), 162097, Arrays.asList(this.name));
		MessageMgr.sendSystemMsg(162142, Arrays.asList(role.getName(), this.name));
	}

	@Override
	public void fail() {

	}

	@Override
	public boolean verify() {
		return super.commonVerify();
	}

	@Override
	public boolean add() {
		return new StoreShenShouContainer(this).add(false);
	}

	@Override
	public boolean exc() {
		if (!verify()) {
			return false;
		}
		if (!super.commonCheckEnoughMoney()) {
			int itemId = goods.costItemId;
			ItemShuXing itemShuXing = fire.pb.item.Module.getInstance().getItemManager().getAttr(itemId);
			if (itemShuXing == null) {
				Shop.LOG.error("角色id " + role.getRoleId() + "兑换神兽" + "\t道具错误");
				return false;
			}
			MessageMgr.sendMsgNotify(role.getRoleId(), 162110, Arrays.asList(itemShuXing.getName()));
			return false;
		}
		if (!cost()) {
			fail();
			return false;
		}
		if (!add()) {
			fail();
			return false;
		}
		success();
		return true;
	}

}
