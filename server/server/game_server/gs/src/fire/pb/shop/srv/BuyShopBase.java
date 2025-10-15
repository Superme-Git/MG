package fire.pb.shop.srv;

import java.util.ArrayList;
import java.util.List;

import fire.log.YYLogger;
import fire.log.beans.OpSilShopBean;
import fire.pb.ErrorCodes;
import fire.pb.SError;
import fire.pb.fushi.SSendVipInfo;
import fire.pb.game.MoneyType;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemMaps;
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
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;

/**
 * 
 * 宠物、普通商店、商会、兑换商店,共用逻辑类.
 * 
 * @author liangyanpeng
 *
 */
public abstract class BuyShopBase extends Shop {

	protected int price;
	protected int moneytype;
	protected long originalMoney;
	protected int shopId;
	protected Pack bag;

	int extNum = 0;	// log用
	
	public BuyShopBase(ShopParameters params) {
		super(params.role, params.goods, params.num);
		this.shopId = params.shopId;
		this.getBag();//初始化背包对象
	}

	/**
	 * @return 默认不绑定
	 */
	public final boolean isBind() {
		return false;
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

	/**
	 * 扣钱
	 */
	protected boolean commonCost() {
		long ret = 0;
		if (moneytype == MoneyType.MoneyType_SilverCoin) {
			ret = bag.subMoney(-price * num, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shanghuigoumaicost, 0);
		} else if (moneytype == MoneyType.MoneyType_GoldCoin) {
			ret = bag.subGold(-price * num, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shanghuigoumaicost, 0);
		} else if (moneytype == MoneyType.MoneyType_HearthStone) {
			ret = bag.subMoney(-price * num, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shanghuigoumaicost, 0);
		} else if (moneytype == MoneyType.MoneyType_RongYu) {
			ret = bag.subCurrency(-price * num, MoneyType.MoneyType_RongYu, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shanghuigoumaicost, 0);
		} else if (moneytype == MoneyType.MoneyType_ShengWang) {
			ret = bag.subCurrency(-price * num, MoneyType.MoneyType_ShengWang, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shanghuigoumaicost, 0);
		} else if (moneytype == MoneyType.MoneyType_FestivalPoint) {
			ret = bag.subCurrency(-price * num, MoneyType.MoneyType_FestivalPoint, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shanghuigoumaicost, 0);
		} else if (moneytype == MoneyType.MoneyType_GoodTeacherVal) {
			ret = bag.subCurrency(-price * num, MoneyType.MoneyType_GoodTeacherVal, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shanghuigoumaicost, 0);
		} else if (moneytype == MoneyType.MoneyType_Item){
			int itemId = goods.getCostItemId();
			int itemNum = goods.getCostItemNum();
			ret = BagUtil.removeItem(role.getRoleId(), itemId, itemNum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shanghuigoumaicost, itemId, "购买物品");
		}
		return ret != 0 ? true : false;
	}

	/**
	 * @return 验证购买条件通过返回true
	 */
	protected boolean commonVerify() {
		// 战斗中不允许npc购买
//		if (xtable.Roleid2battleid.get(role.getRoleId()) != null) {
//			BuyShopFactory.logger.info("战斗中不能购买" + goods.getId());
//			return false;
//		}
		// 商品是否存在
		SNpcSale ns = NpcManager.getInstance().getNpcSale(shopId);
		if (!ns.getGoodsids().contains(goods.getId())) {
			LOG.info("没有该物品的购买" + goods.getId());
			return false;
		}
		
		if (goods.limitType == LimitType.DAY_LIMIT)
			extNum = fire.pb.fushi.Module.getVipTableRight(role.getRoleId(), SSendVipInfo.RIGHT_DAILY_GOODS_COUNT);
		else if (goods.limitType == LimitType.WEEK_LIMIT)
			extNum = fire.pb.fushi.Module.getVipTableRight(role.getRoleId(), SSendVipInfo.RIGHT_WEEKLY_DISCOUNT_COUNT);	

		// 购买数量超过限够数量
		if (goods.limitType != LimitType.NO_LIMIT && num > (goods.limitNum + extNum)) {
			LOG.info("购买数量超过限够数量" + goods.getId());
			return false;
		}

		// 商品是否限购
		if (!LimitManager.getInstance().isBuyLimit(role.getRoleId(), this.goods, this.num, extNum)) {
			LOG.info("购买商品已达到限购次数" + goods.getId());
			return false;
		}

		// 货币、价钱
		List<Integer> currencys = goods.getCurrencys();
		List<Integer> prices = goods.getPrices();
		
		if (currencys == null || prices == null) {
			LOG.error("策划配置出错未填写货币类型:" + goods.getId());
			return false;
		}

		if (currencys.size() == 0 || prices.size() == 0) {
			LOG.error("策划配置出错未填写价格:" + goods.getId());
			return false;
		}

		moneytype = currencys.get(0);
		price = prices.get(0);
		//去商品浮动价格管理获取价格
		int tmpPrice = FloatingOneManager.getInstance().getCurrPrice(shopId, goods.id);
		if (tmpPrice != -1) {
			price = tmpPrice;
		}

		if (price <= 0) {
			int userId = xtable.Properties.selectUserid(role.getRoleId());
			LOG.error("NPC购买价格错误。userId=" + userId + ",role.getRoleId()=" + role.getRoleId() + ",itemId=" + goods.getItemId()
					+ ",num=" + num + ",class=" + getClass());
			return false;
		}
		return true;
	}

	/**
	 * @return 检查货币是否充足
	 */
	protected boolean commonCheckEnoughMoney() {
		if (price * num <= 0)
			return false;
		if (moneytype == MoneyType.MoneyType_SilverCoin) {
			originalMoney = bag.getMoney();
			return originalMoney >= price * num;
		} else if (moneytype == MoneyType.MoneyType_GoldCoin) {
			originalMoney = bag.getGold();
			return originalMoney >= price * num;
		} else if (moneytype == MoneyType.MoneyType_HearthStone) {
			originalMoney = bag.getMoney();
			return originalMoney >= price * num;
		} else if (moneytype == MoneyType.MoneyType_RongYu) {
			originalMoney = bag.getCurrency(MoneyType.MoneyType_RongYu);
			return originalMoney >= price * num;
		} else if (moneytype == MoneyType.MoneyType_ShengWang) {
			originalMoney = bag.getCurrency(MoneyType.MoneyType_ShengWang);
			return originalMoney >= price * num;
		} else if (moneytype == MoneyType.MoneyType_FestivalPoint) {
			originalMoney = bag.getCurrency(MoneyType.MoneyType_FestivalPoint);
			return originalMoney >= price * num;
		} else if (moneytype == MoneyType.MoneyType_GoodTeacherVal) {
			originalMoney = bag.getCurrency(MoneyType.MoneyType_GoodTeacherVal);
			return originalMoney >= price * num;
		} else if (moneytype == MoneyType.MoneyType_Item) {
			int costitemId = goods.getCostItemId();
			int costItemNum = goods.getCostItemNum();
			ItemMaps bagContainer = fire.pb.item.Module.getInstance().getItemMaps(role.getRoleId(), BagTypes.BAG, false);
			originalMoney = bagContainer.getItemNum(costitemId, 0);
			return originalMoney >= costItemNum * num;
		}
		return false;
	}

	/**
	 * 货币不足提示
	 */
	protected void commonNotEnoughMoney() {
		if (isIn()) {
			SError send = new SError();
			send.error = ErrorCodes.NotEnoughMoney;
			mkdb.Procedure.psendWhileRollback(role.getRoleId(), send);
		} else {
			MessageMgr.psendMsgNotifyWhileRollback(role.getRoleId(), 142586, null);
		}
	}

	/**
	 * 购买成功
	 */
	protected void commonSuccess() {
		if (!notifySwitch()) {
			if (moneytype == MoneyType.MoneyType_HearthStone) {
				List<String> parameters = new ArrayList<String>();
				if (itemAttr == null)
					return;
				parameters.add(String.valueOf(num));
				parameters.add(itemAttr.unit);
				parameters.add(itemAttr.name);
				parameters.add(String.valueOf(price * num));
				MessageMgr.psendMsgNotifyWhileCommit(role.getRoleId(), 142587, parameters);
			}
		}
		// 限购记录商品
		if (goods.limitType != LimitType.NO_LIMIT) {
			LimitManager.getInstance().saveBuyLimit(this.role.getRoleId(), this.goods, this.num);
		}
		// 浮动商店记录商品
		if (FileterFloatingShop.getInstance().isFloatingOne(shopId)) {
			FloatingOneManager.getInstance().buyGoodsRecord(shopId, goods.getId(), num);
		}
		
		// 促发购买事件，以便主线任务使用
		fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.BuyItemEvent(this.role.getRoleId(),
				goods.getItemId()));
		
		// 运营日志
		writeYYLogger(OpSilShopBean.Op_SilShopBean_Buy);
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

		int isBuyLimit = LimitManager.getInstance().isBuyLimit(role.getRoleId(), goods, this.num, extNum) ? 0 : 1;
		OpSilShopBean opSilShopBean = new OpSilShopBean(opId, goods.itemId,
				goods.type, getQuality(goods.type, goods.itemId), num, MoneyType.MoneyType_SilverCoin, price * num, curPrice,
				discount, price, isBuyLimit, 1, -1);
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
	
	protected boolean isIn() {
		if (moneytype == MoneyType.MoneyType_SilverCoin || moneytype == MoneyType.MoneyType_GoldCoin
				|| moneytype == MoneyType.MoneyType_RongYu || moneytype == MoneyType.MoneyType_ShengWang
				|| moneytype == MoneyType.MoneyType_FestivalPoint || moneytype == MoneyType.MoneyType_GoodTeacherVal) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return 有匹配的通知类型返回true否则false.
	 */
	protected boolean notifySwitch() {
		boolean isNotify = false;
		List<String> parameters = new ArrayList<String>();
		if (null == itemAttr) return isNotify;
		parameters.add(itemAttr.name);
		parameters.add(String.valueOf(num));
		parameters.add(itemAttr.unit);
		parameters.add(String.valueOf(price * num));
		
		switch (moneytype) {
		case MoneyType.MoneyType_SilverCoin:
			MessageMgr.psendMsgNotifyWhileCommit(role.getRoleId(), 160197, parameters);
			break;
		case MoneyType.MoneyType_GoldCoin:
			MessageMgr.psendMsgNotifyWhileCommit(role.getRoleId(), 160149, parameters);
			break;
		case MoneyType.MoneyType_RongYu:
			MessageMgr.psendMsgNotifyWhileCommit(role.getRoleId(), 160152, parameters);
			break;
		case MoneyType.MoneyType_ShengWang:
			MessageMgr.psendMsgNotifyWhileCommit(role.getRoleId(), 160154, parameters);
			break;
		case MoneyType.MoneyType_FestivalPoint:
			MessageMgr.psendMsgNotifyWhileCommit(role.getRoleId(), 160155, parameters);
			isNotify = true;
			break;
		case MoneyType.MoneyType_GoodTeacherVal:
			MessageMgr.psendMsgNotifyWhileCommit(role.getRoleId(), 160156, parameters);
			isNotify = true;
			break;
		}
		return isNotify;
	}
	
}
