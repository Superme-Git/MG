package fire.pb.shop.srv;

import java.util.List;

import fire.log.YYLogger;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.fushi.SSendVipInfo;
import fire.pb.game.MoneyType;
import fire.pb.item.Commontext.UseResult;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Pack;
import fire.pb.item.UseItemToRole;
import fire.pb.pet.PetAttr;
import fire.pb.shop.SGoods;
import fire.pb.shop.SMallShop;
import fire.pb.shop.SNotifyBuySuccess;
import fire.pb.shop.ShopBuyType;
import fire.pb.shop.srv.limit.LimitManager;
import fire.pb.shop.utils.LimitType;
import mkdb.Procedure;

/**
 * 
 * 商城购买
 * 
 * @author liangyanpeng
 *
 */
public class BuyMallShop implements Buy {

	private final long roleid;
	private final int userid;
	private final int goodsid;
	private final int num;
	private int totalYb;
	private final boolean use;// 是否使用道具

	private SGoods goods;
	private SMallShop mallShop;

	private Pack bag;

	protected int price;
	protected int moneytype;

	private int extNum = 0; //log用
	/**
	 * @param roleid
	 *            角色id
	 * @param userid
	 *            用户id
	 * @param goodsid
	 *            商品id
	 * @param num
	 *            道具数量
	 * @param use
	 *            是否使用道具
	 */
	public BuyMallShop(long roleid, int userid, int goodsid, int num, boolean use) {
		super();
		this.roleid = roleid;
		this.userid = userid;
		this.goodsid = goodsid;
		this.num = num;
		this.use = use;
		// 获得背包
		bag = new Pack(roleid, false);
	}

	@Override
	public boolean cost() {
		long ret = 0;
		if (moneytype == MoneyType.MoneyType_SilverCoin) {
			ret = bag.subMoney(-price * num, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_goumai, 0);
			return ret != 0 ? true : false;
		} else if (moneytype == MoneyType.MoneyType_GoldCoin) {
			ret = bag.subGold(-price * num, "购买物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_goumai, 0);
			return ret != 0 ? true : false;
		} else if (moneytype == MoneyType.MoneyType_HearthStone) {
			return FushiManager.subFushiFromUser(userid, roleid, totalYb, goods.itemId, num, FushiConst.REASON_BUY_ITEM,
					YYLoggerTuJingEnum.tujing_Value_goumai,true);
		}

		return false;
	}

	@Override
	public void success() {

		// 促发购买事件，以便主线任务使用
		fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.BuyItemEvent(roleid, goods.getItemId()));

		// 记录消费日志
		StringBuffer sb = new StringBuffer();
		sb.append("Buy RMB Item by Fushi: ").append("Userid:").append(userid).append(" Roleid:").append(roleid)
				.append(" Roleid:").append(goods.id).append(" Itemid:").append(goods.itemId).append(" num:").append(num)
				.append(" fushi:").append(totalYb);
		FushiManager.logger.info(sb.toString());

		// 限购记录商品
		if (goods.limitType != LimitType.NO_LIMIT) {
			LimitManager.getInstance().saveBuyLimit(roleid, this.goods, this.num);
		}

		// 是否使用道具
		if (use) {
			int usekey = 0;
			for (fire.pb.item.ItemBase bi : bag) {
				if (bi.getItemId() == goods.itemId) {
					if (bi.onUse(1) == UseResult.FAIL) {
						if (bi.onUse(1, new UseItemToRole(roleid)) != UseResult.FAIL) {
							usekey = bi.getKey();
						}
						break;
					} else {
						usekey = bi.getKey();
						break;
					}
				}
			}
			// 扣除背包中道具
			if (usekey != 0)
				bag.removeItemWithKey(usekey, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_itemuse, 0, "购买并使用");
		}
		// 购买成功通知
		SNotifyBuySuccess notifySuccess = new SNotifyBuySuccess();
		notifySuccess.notifytype = ShopBuyType.MALL_SHOP;
		if (goods.getType() == 1) {
			ItemShuXing itemAttr = fire.pb.item.Module.getInstance().getItemManager().getAttr(goods.getItemId());
			notifySuccess.name = itemAttr.getName();
			notifySuccess.units = itemAttr.unit;
			notifySuccess.itemorpet = 1;
		} else {
			PetAttr petAttr = fire.pb.pet.Module.getInstance().getPetManager().getAttr(goods.getItemId());
			notifySuccess.name = petAttr.getName();
			notifySuccess.itemorpet = 2;
		}
		notifySuccess.number = this.num;
		notifySuccess.money = this.totalYb;
		notifySuccess.currency = this.moneytype;
		Procedure.psendWhileCommit(this.roleid, notifySuccess);
		
		// 运营日志
		writeYYLogger();
	}

	/**
	 * 运营日志
	 */
	private void writeYYLogger() {
		float discount = -1;
		List<Integer> prices = goods.getPrices();
		List<Integer> oldPrices = goods.getOldprices();
		if(prices != null && oldPrices != null && prices.size() != 0 && oldPrices.size() != 0){
			discount = (float)prices.get(0) / oldPrices.get(0);
		}
		
		int isBuyLimit = LimitManager.getInstance().isBuyLimit(roleid, goods, this.num, extNum) ? 0 : 1;
		int costMoney = 0;
		switch (moneytype) {
		case MoneyType.MoneyType_SilverCoin:
		case MoneyType.MoneyType_GoldCoin:
			costMoney = price * num;
			break;
		case MoneyType.MoneyType_HearthStone:
			costMoney = this.totalYb;
			break;
		default:
			break;
		}
		fire.log.beans.OpTokShopBean opTokShopBean = new fire.log.beans.OpTokShopBean(
				fire.log.beans.OpTokShopBean.OpTokShopBean_Oper_Buy, goods.getType(), goods.getId(), this.num, 
				this.moneytype, costMoney, discount, isBuyLimit);
		YYLogger.tokShopLog(roleid, opTokShopBean);
	}

	@Override
	public void fail() {

	}

	@Override
	public boolean verify() {
		mallShop = fire.pb.shop.Module.sMallShopMap.get(goodsid);
		if (null == mallShop) {
			return false;
		}

		// 商品是否限购
		
		if (mallShop.viplvrequire>0) {
			int nviplv = 0;
			Integer viplv = xtable.Vipinfo.selectViplevel(roleid);	
			if(viplv != null)
				nviplv = viplv.intValue();
			if(mallShop.viplvrequire>nviplv)
			{
				if (Shop.LOG.isInfoEnabled()) {
					Shop.LOG.info("角色"+roleid+"未达到商品所需要的vip等级" + mallShop.viplvrequire);
				}
				return false;
			}
		}
		
		goods = fire.pb.shop.Module.sGoodsMap.get(mallShop.id);
		if (null == goods) {
			return false;
		}

		PropRole role = new PropRole(roleid, true);
		int roleLeve = role.getLevel();
		
		extNum = 0;
		if (goods.limitType == LimitType.DAY_LIMIT)
			extNum = fire.pb.fushi.Module.getVipTableRight(roleid, SSendVipInfo.RIGHT_DAILY_GOODS_COUNT);
		else if (goods.limitType == LimitType.WEEK_LIMIT)
			extNum = fire.pb.fushi.Module.getVipTableRight(roleid, SSendVipInfo.RIGHT_WEEKLY_DISCOUNT_COUNT);	

		// 等级不符合要求
		if (roleLeve < goods.lvMin || roleLeve > goods.lvMax) {
			return false;
		}

		// 购买数量超过限够数量
		if (goods.limitType != LimitType.NO_LIMIT && num > (goods.limitNum + extNum)) {
			if (Shop.LOG.isInfoEnabled()) {
				Shop.LOG.info("购买数量超过限够数量" + goods.getId());
			}
			return false;
		}

		// 商品是否限购
		if (!LimitManager.getInstance().isBuyLimit(role.getRoleId(), this.goods, this.num, extNum)) {
			if (Shop.LOG.isInfoEnabled()) {
				Shop.LOG.info("购买商品已达到限购次数" + goods.getId());
			}
			return false;
		}

		// 货币、价钱
		List<Integer> currencys = goods.getCurrencys();
		List<Integer> prices = goods.getPrices();

		if (currencys == null || prices == null) {
			Shop.LOG.error("未找到配置中的货币类型和价格,商品id=" + goodsid);
			return false;
		}

		if (currencys.size() == 0 || prices.size() == 0) {
			Shop.LOG.error("未找到配置中的货币类型和价格,商品id=" + goodsid);
			return false;
		}
		// 获得价格
		moneytype = currencys.get(0);
		price = prices.get(0);

		if (this.moneytype == MoneyType.MoneyType_HearthStone) {
			totalYb = num * price;
			if (num <= 0 || totalYb <= 0)
				return false;
			
			if (!fire.pb.fushi.Module.getIsYYBUser(userid)) {
				// 获取符石
				xbean.YbNums ybNums = xtable.Fushinum.select(userid);
				if (ybNums == null)
					return false;
				xbean.YbNum ybNum = ybNums.getRoleyb().get(roleid);
				if (ybNum == null)
					return false;

				if (ybNum.getNum() < 0 || ybNum.getSysnum() < 0)
					return false;
			}
		}

		return true;

	}

	@Override
	public boolean add() {
		// 给道具,相关id: 0系统送的符石,1是现金充值
		int itemNum = bag.doAddItem(goods.itemId, num, 2, 0, "buy RMBItem by Fushi",
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shanghuigoumaiget, 1);

		if (itemNum != num) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 142338, null);
			return false;
		}
		return true;
	}

	@Override
	public boolean exc() {
		if (!verify()) {
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
