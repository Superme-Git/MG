package fire.pb.shop.srv;

import fire.pb.shop.utils.ShopParameters;

public class BuyExchangeShop extends BuyShopBase implements Buy {

	public BuyExchangeShop(ShopParameters params) {
		super(params);
	}

	@Override
	public boolean add() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("兑换物品进背包");
		}
		return new StoreBagContainer(this, bag).add(false);
	}

	@Override
	public void success() {
		super.commonSuccess();
	}
	
	@Override
	public boolean cost() {
		return super.commonCost();
	}

	@Override
	public boolean verify() {
		return super.commonVerify();
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


}
