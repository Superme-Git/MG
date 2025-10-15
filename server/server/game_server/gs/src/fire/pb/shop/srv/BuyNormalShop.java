package fire.pb.shop.srv;

import fire.pb.shop.utils.ShopParameters;

/**
 * 普通商店购买放入背包中
 * 
 * 
 * @author liangyanpeng
 *
 */
public class BuyNormalShop extends BuyShopBase implements Buy {
	

	public BuyNormalShop(ShopParameters params) {
		super(params);
	}

	@Override
	public boolean add() {
		return new StoreBagContainer(this, bag).add(false);
	}

	@Override
	public void success() {
		super.commonSuccess();
	}

	@Override
	public void fail() {

	}

	@Override
	public boolean exc() {
		if (!verify()) {
			return false;
		}
		if (!super.commonCheckEnoughMoney()) {
			super.commonNotEnoughMoney();
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

	@Override
	public boolean cost() {
		return super.commonCost();
	}

	@Override
	public boolean verify() {
		return super.commonVerify();
	}
}
