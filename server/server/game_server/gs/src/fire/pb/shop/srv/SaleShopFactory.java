package fire.pb.shop.srv;

import org.apache.log4j.Logger;

import fire.pb.shop.ShopBuyType;
import fire.pb.shop.utils.ShopParameters;
import mkdb.Procedure;

/**
 * 商店售卖的工厂
 * 
 * @author liangyanpeng
 *
 */
public class SaleShopFactory extends Procedure {

	public static final Logger logger = Logger.getLogger(BuyShopFactory.class);

	public Sale sale;
	public ShopParameters params;
	public int shopType;

	public SaleShopFactory(ShopParameters params, int shopType) {
		super();
		this.params = params;
		this.shopType = shopType;
	}

	@Override
	protected boolean process() throws Exception {

		switch (this.shopType) {
		case ShopBuyType.CHAMBER_OF_COMMERCE_SHOP_SALE: {
			this.sale = new SaleChamberOfCommerceShop(params);
			break;
		}
		default:
			return false;
		}

		return this.sale.exc();
	}

}
