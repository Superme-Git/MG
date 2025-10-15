package fire.pb.shop.srv;

import java.util.List;

import fire.log.YYLogger;
import fire.log.beans.OpSilShopBean;
import fire.pb.game.MoneyType;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.item.Pack;
import fire.pb.npc.NpcManager;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetManager;
import fire.pb.shop.SNpcSale;
import fire.pb.shop.srv.floating.FileterFloatingShop;
import fire.pb.shop.srv.floating.FloatingOneManager;
import fire.pb.shop.srv.limit.LimitManager;
import fire.pb.shop.utils.LimitType;
import fire.pb.shop.utils.ShopParameters;

public class SaleShopBase extends Shop {
	private final int shopId;
	protected Pack bag;
	protected int price;
	protected int moneytype;
	
	public SaleShopBase(ShopParameters params) {
		super(params.role, params.goods, params.num);
		this.shopId = params.shopId;
		this.getBag();
		originalGold = bag.getGold();
		originalSilver = bag.getMoney();
	}

	/**
	 * @return 背包对象
	 */
	protected final Pack getBag() {
		if (bag == null) {
			bag = new Pack(role.getRoleId(), false);
		}
		return bag;
	}
	
	protected void commonSuccess() {
		// 限购商品记录售买
		if (goods.limitType != LimitType.NO_LIMIT) {
			LimitManager.getInstance().saveSaleLimit(this.role.getRoleId(), this.goods, this.num);
		}
		// 浮动商店记录商品
		if (FileterFloatingShop.getInstance().isFloatingOne(shopId)) {
			FloatingOneManager.getInstance().saleGoodsRecord(shopId, goods.getId(), num);
		}
		
		writeYYLogger(OpSilShopBean.Op_SilShopBean_Sell);
	}
	
	/**
	 * 运营日志
	 */
	private void writeYYLogger(int opId) {
		int curPrice = -1;
		float discount = 1;
		List<Integer> prices = goods.getPrices();
		List<Integer> oldPrices = goods.getOldprices();
		if(prices != null && prices.size() != 0){
			curPrice = prices.get(0);
			if(oldPrices != null && oldPrices.size() != 0){
				discount = (float)curPrice / goods.getOldprices().get(0);
			}
		}
		
		OpSilShopBean opSilShopBean = new OpSilShopBean(opId, goods.itemId,
				goods.type, getQuality(goods.type, goods.itemId), num, MoneyType.MoneyType_SilverCoin, price * num, curPrice,
				discount, price, 0, 1, -1);
		YYLogger.silShopLog(role.getRoleId(), opSilShopBean);
	}
	
	private int getQuality(int itemType, int goodId){
		int quailty = -1;
		if(itemType == 2){
			PetAttr attr = PetManager.getInstance().getAttr(goodId);
			if (attr != null) {
				quailty = attr.getQuality();
			}
		}else {
			ItemShuXing attr = Module.getInstance().getItemManager().getAttr(goodId);
			if (attr != null) {
				quailty = attr.getNquality();
			}
			
		}
		return quailty;
	}
	
	protected boolean commonAddMoney(long money) {
		long ret = 0;
		switch (moneytype) {
		case MoneyType.MoneyType_SilverCoin:
			ret = bag.addSysMoney(money, "出售物品增加银币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_ShangChengCS, 0);
			break;
		case MoneyType.MoneyType_GoldCoin:
			ret = bag.addSysGold(money, "出售物品增加金币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_ShangChengCS, 0);
			break;
		default:
			return false;
		}
		return ret != 0 ? true : false;
	}
	
	protected boolean commonVerify() {
		// 战斗中不允许npc售卖
//		if (xtable.Roleid2battleid.get(role.getRoleId()) != null) {
//			BuyShopFactory.logger.info("战斗中不能购买" + goods.getId());
//			return false;
//		}
		// 商品是否存在
		SNpcSale ns = NpcManager.getInstance().getNpcSale(shopId);
		if (!ns.getGoodsids().contains(goods.getId())) {
			if (LOG.isDebugEnabled()) {
				LOG.info("没有该物品的出售" + goods.getId());
			}
			return false;
		}
		
		// 商品是否限售
		if (!LimitManager.getInstance().isSaleLimit(role.getRoleId(), this.goods, this.num)) {
			if (LOG.isDebugEnabled()) {
				LOG.info("购买商品已达到限购次数" + goods.getId());
			}
			return false;
		}
		
		// 货币、价钱
		List<Integer> currencys = goods.getCurrencys();
		List<Integer> prices = goods.getPrices();
		
		if (goods.getCurrencys() == null || goods.getPrices() == null) {
			if (LOG.isDebugEnabled()) {
				LOG.error("策划配置出错未填写货币类型:" + goods.getId());
			}
			return false;
		}

		if (goods.getCurrencys().size() == 0 || goods.getPrices().size() == 0) {
			if (LOG.isDebugEnabled()) {
				LOG.error("策划配置出错未填写价格:" + goods.getId());
			}
			return false;
		}

		moneytype = currencys.get(0);
		price = prices.get(0);
		//去商品浮动价格管理获取价格
		int floatingPrice = FloatingOneManager.getInstance().getCurrPrice(shopId, goods.id);
		if (floatingPrice != -1) {
			price = floatingPrice;
		}
		return true;
	}


}
