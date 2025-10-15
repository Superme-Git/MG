package fire.pb.shop.srv;

import fire.pb.shop.ShopBuyType;
import fire.pb.shop.utils.ShopParameters;
import mkdb.Procedure;

/**
 * 商店购买的工厂
 * 
 * @author liangyanpeng
 *
 */
public class BuyShopFactory extends Procedure {
	
	public Buy buy;
	public ShopParameters params;
	public int shopType;

	public BuyShopFactory(ShopParameters params, int shopType) {
		super();
		this.params = params;
		this.shopType = shopType;
	}

	@Override
	protected boolean process() throws Exception {

		switch (this.shopType) {
		case ShopBuyType.NORMAL_SHOP: {
			this.buy = new BuyNormalShop(params);
			break;
		}
		case ShopBuyType.PET_SHOP: {
			this.buy = new BuyPetShop(params);
			break;
		}
		case ShopBuyType.EXCHANGE_BUY: {
			this.buy = new BuyExchangeShop(params);
			break;
		}
		case ShopBuyType.MALL_SHOP: {
			break;
		}
		case ShopBuyType.CHAMBER_OF_COMMERCE_SHOP_BUY: {
			this.buy = new BuyChamberOfCommerceShop(params);
			break;
		}
		case ShopBuyType.SHENSHOU_SHOP:{
			this.buy = new BuyShenShouShop(params);
			break;
		}
		default:
			return false;
		}
		
		return this.buy.exc();

	}

}
