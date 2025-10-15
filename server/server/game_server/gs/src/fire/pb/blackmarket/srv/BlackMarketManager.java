package fire.pb.blackmarket.srv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.blackmarket.utils.BlackMarketUtils;
import fire.pb.game.MoneyType;
import fire.pb.http.cbg.CBGHttpUtils;
import fire.pb.http.cbg.CBGOrderDownHttpCallBackHandler;
import fire.pb.http.cbg.CBGOrderUpHttpCallBackHandler;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.Module;
import fire.pb.item.Pack;
import fire.pb.main.Gs;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.PetError;
import fire.pb.pet.SPetError;
import fire.pb.shop.GoldOrder;
import fire.pb.shop.SGoldOrderBrowseBlackMarket;
import fire.pb.shop.SRefreshGoldOrderState;
import fire.pb.shop.utils.MarketUtils;
import fire.pb.talk.MessageMgr;
import fire.pb.tel.utils.CheckCodeUtils;

/**
 * 黑市 玩家通过WEB交易平台能进行游戏中的道具交易
 * 
 * @author liangyanpeng
 *
 */
public class BlackMarketManager {

	public static final Logger LOG = Logger.getLogger("BLACKMARKET");

	private static class BlackMarketManagerHolder {
		static BlackMarketManager instance = new BlackMarketManager();
	}

	public static BlackMarketManager getInstance() {
		return BlackMarketManagerHolder.instance;
	}
	
	///////////////////////////////////////////////////////////////////////
	////////////////////金币订单

	/**
	 * 上架金币订单
	 * 
	 * @param gold
	 *            金币数量
	 * @param rmb
	 *            rmb 单位分
	 * @return
	 */
	public boolean goldOrderUp(long roleId, long gold, long rmb) {
		if (!CheckCodeUtils.isCanUpByCJG(roleId)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(",藏金阁上架的时候没有进行短信验证");
			LOG.error(sbd.toString());
			return false;
		}
		
		if (gold < BlackMarketUtils.getMinGold() || rmb < BlackMarketUtils.getMinRMB() 
				|| gold > BlackMarketUtils.getMaxGold() || rmb > BlackMarketUtils.getMaxRMB()) {
			LOG.error("role=" + roleId + ", 金币订单上架错误：gold=" + gold + ", rmb=" + rmb + ", 输入的金币数量或RMB数量不在指定范围内！");
			return false;
		}

		Pack pack = new Pack(roleId, false);
		long goldNubmer = pack.getCurrency(MoneyType.MoneyType_GoldCoin);
		if (goldNubmer - gold < 0) {
			LOG.error("role=" + roleId + ", 金币不足！");
			return false;
		}
		
		if (pack.getCurrency(MoneyType.MoneyType_EreditPoint) + gold*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_CBG) < 0) {
			LOG.error("role=" + roleId + ", 信用值不足！");
			MessageMgr.sendMsgNotify(roleId, 162194, null);// 信用值不足 无法上架	 
			return false;
		}
		
		// 扣除金币
		long costGold = pack.subGold(-gold, "", YYLoggerTuJingEnum.tujing_Value_balckmarket, 1);
		if (costGold != -gold) {
			LOG.error("role=" + roleId + ", 金币订单上架错误, 扣金币失败！");
			return false;
		}
		//扣信用值
		pack.addSysCurrency((long)(gold*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_CBG))
				, MoneyType.MoneyType_EreditPoint, "金币订单上架	", YYLoggerTuJingEnum.tujing_Value_balckmarket, 0);


		xbean.RoleBlackMarket roleBlackMarket = xtable.Blackmarkettab.get(roleId);
		if (null == roleBlackMarket) {
			roleBlackMarket = xbean.Pod.newRoleBlackMarket();
			xtable.Blackmarkettab.insert(roleId, roleBlackMarket);
		}
		
		////////////////////////////
		// 检查上架待售金币订单数量
		if (roleBlackMarket.getGoldordersale().size() > 0) {
			int maxOrderUpNumber = 0;
			for (xbean.GoldOrder order : roleBlackMarket.getGoldordersale().values()) {
				if (order.getState() != BlackMarketUtils.OrderState.STAY_SALE)
					continue;
				++maxOrderUpNumber;
			}
			if (maxOrderUpNumber == BlackMarketUtils.getMaxOrderUpNumber()) {
				MessageMgr.sendMsgNotify(roleId, 162188, null);// 地精市场最多上架5单	
				LOG.error("role=" + roleId + ", orderUpNumber=" + maxOrderUpNumber + ", 上架金币订单数量超出, 新金币订单上架失败！");
				return false;
			}
		}

		// 生成订单
		long now = System.currentTimeMillis();
		long pid = xtable.Orderidtab.nextKey();
		xbean.GoldOrder order = xbean.Pod.newGoldOrder();
		order.setPid(pid);
		order.setNumber(gold);
		order.setPrice(rmb);
		order.setPublicity(BlackMarketUtils.NO_PUBLICITY);
		order.setTime(now);
		order.setState(BlackMarketUtils.OrderState.STAY_UP);
		
		if (LOG.isInfoEnabled()) {
			LOG.info("上架金币订单：role=" + roleId + ", pid=" + pid + ", gold=" + gold + ", price=" + rmb);
		} 

		// 向角色身上添加售卖订单
		roleBlackMarket.getGoldordersale().put(pid, order);

		// 向平台发送订单
		HttpPost orderPost = CBGHttpUtils.genItemUpHttpPostRequest(roleId, rmb, gold, Long.toString(pid),
				BlackMarketUtils.NO_PUBLICITY, BlackMarketUtils.OrderType.GOLD, null);

		if (orderPost == null) {
			LOG.error(new StringBuilder("role=").append(roleId).append(", 生成平台订单发送对象失败！"));
			return false;
		}

		Future<HttpResponse> done = Gs.getHttpClient().execute(orderPost,
				new CBGOrderUpHttpCallBackHandler(roleId, pid, BlackMarketUtils.OrderType.GOLD));
		// if (done.isDone()) {}
		return true;
	}

	/**
	 * 金币订单下架
	 * 
	 * @param pid
	 *            订单号
	 * @return
	 */
	public boolean goldOrderDown(long roleId, long pid) {
		Pack pack = new Pack(roleId, false);
		long goldNubmer = pack.getCurrency(MoneyType.MoneyType_GoldCoin);
		if (goldNubmer >= Long.MAX_VALUE) {
			LOG.error("role=" + roleId + ", gold=" + goldNubmer + ", 再添加金币就溢出了！");
			MessageMgr.sendMsgNotify(roleId, 162187, null);// 金币已满, 不能下架金币
			return false;
		}

		// 向平台发送订单
		HttpPost orderPost = CBGHttpUtils.genItemDownHttpPostRequest(roleId, Long.toString(pid));

		if (orderPost == null) {
			LOG.error("role="+ roleId + ", 生成平台订单发送对象失败！");
			return false;
		}

		Future<HttpResponse> done = Gs.getHttpClient().execute(orderPost,
				new CBGOrderDownHttpCallBackHandler(roleId, pid, BlackMarketUtils.OrderType.GOLD));

		return true;
	}

	/**
	 * 交易金币
	 * 
	 * @param buyerRoleId
	 *            买家
	 * @param sellerRoleId
	 *            卖家
	 * @param pid
	 *            订单id
	 */
	public void goldOrderTrade(final long buyerRoleId, final long sellerRoleId, final long pid) {

		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				/////////////////////////////
				// 处理卖家
				xbean.RoleBlackMarket sellerRoleBlackMarket = xtable.Blackmarkettab.get(sellerRoleId);
				if (null == sellerRoleBlackMarket) {
					LOG.error("role=" + sellerRoleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", 黑市订单表中不存在此角色信息, 有错误！");
					return false;
				}
				xbean.GoldOrder goldOrder = sellerRoleBlackMarket.getGoldordersale().get(pid);
				if (null == goldOrder) {
					LOG.error("role=" + sellerRoleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", 黑市订单表中不存在此角色信息, 有错误！");
					return false;
				}
				int orderState = goldOrder.getState();
				if (orderState != BlackMarketUtils.OrderState.STAY_SALE) {
					LOG.error("role=" + sellerRoleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", order state=" + orderState + ", 订单状态非法.");
					return false;
				}

				goldOrder.setState(BlackMarketUtils.OrderState.SALE_DONE);
				
				// 刷新卖家订单状态
				SRefreshGoldOrderState refreshSellerOrderState = new SRefreshGoldOrderState(goldOrder.getPid(), goldOrder.getState());
				mkdb.Procedure.psendWhileCommit(sellerRoleId, refreshSellerOrderState);
				
				// 给卖家发送系统消息
				MessageMgr.sendSystemMessageToRole(sellerRoleId, 162181,
						Arrays.asList(Long.toString(goldOrder.getNumber())));
				

				//////////////////////////////
				// 处理买家
				xbean.RoleBlackMarket buyerRoleBlackMarket = xtable.Blackmarkettab.get(buyerRoleId);
				if (null == buyerRoleBlackMarket) {
					buyerRoleBlackMarket = xbean.Pod.newRoleBlackMarket();
					xtable.Blackmarkettab.insert(buyerRoleId, buyerRoleBlackMarket);
				}

				// 生成订单
				long now = System.currentTimeMillis();
				xbean.GoldOrder order = xbean.Pod.newGoldOrder();
				order.setPid(pid);
				order.setNumber(goldOrder.getNumber());
				order.setPrice(goldOrder.getPrice());
				order.setPublicity(BlackMarketUtils.NO_PUBLICITY);
				order.setTime(now);
				order.setState(BlackMarketUtils.OrderState.STAY_TAKE);

				// 向买家角色身上添加购买订单
				buyerRoleBlackMarket.getGoldorderbuy().put(pid, order);
				
				// 刷新买家订单状态
				SRefreshGoldOrderState refreshBuyerState = new SRefreshGoldOrderState(goldOrder.getPid(), order.getState());
				mkdb.Procedure.psendWhileCommit(buyerRoleId, refreshBuyerState);
				
				// 给买家发送系统消息
				MessageMgr.sendSystemMessageToRole(buyerRoleId, 162182,
						Arrays.asList(Long.toString(order.getNumber())));
				
				if (LOG.isInfoEnabled()) {
					LOG.error("buyerRoleId=" + buyerRoleId + " 购买 sellerRoleId=" + sellerRoleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD 
							+ ", 买家order state=" + order.getState() + ", 卖家order state=" + goldOrder.getState() + ", 交易完成.");
				}

				return true;
			}

		}.submit();

	}

	/**
	 * 是否在售
	 * 
	 * @param roleId
	 * @param pid
	 * @return
	 */
	public boolean goldOrderIsSale(final long roleId, final long pid) {
		xbean.RoleBlackMarket roleBlackMarket = xtable.Blackmarkettab.select(roleId);
		if (null == roleBlackMarket) {
			LOG.error("role=" + roleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", 黑市订单表中不存在此角色信息, 有错误！");
			return false;
		}
		xbean.GoldOrder goldOrder = roleBlackMarket.getGoldordersale().get(pid);
		if (null == goldOrder) {
			LOG.error("role=" + roleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", 角色售卖表中不存在此订单！");
			return false;
		}
		int orderState = goldOrder.getState();
		if (orderState != BlackMarketUtils.OrderState.STAY_SALE) {
			LOG.error("role=" + roleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", order state=" + orderState + ", 订单状态非法.");
			return false;
		}
		
		if (LOG.isInfoEnabled()) {
			LOG.error("role=" + roleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", order state=" + orderState + ", 可以出售.");
		}
		
		return true;
	}

	/**
	 * 取出购买的订单金币
	 * 
	 * @param roleId
	 * @param pid
	 * @return
	 */
	public boolean goldOrderTakeOut(final long roleId, final long pid) {

		Pack pack = new Pack(roleId, false);
		long goldNubmer = pack.getCurrency(MoneyType.MoneyType_GoldCoin);
		if (goldNubmer >= Long.MAX_VALUE) {
			LOG.error("role=" + roleId + ", gold=" + goldNubmer + ", 再添加金币就溢出了！");
			MessageMgr.sendMsgNotify(roleId, 162186, null);// 金币已满, 不能取回金币
			return false;
		}

		xbean.RoleBlackMarket roleBlackMarket = xtable.Blackmarkettab.get(roleId);
		if (null == roleBlackMarket) {
			LOG.error("role=" + roleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", 黑市订单表中不存在此角色信息, 有错误！");
			return false;
		}
		xbean.GoldOrder goldOrder = roleBlackMarket.getGoldorderbuy().get(pid);
		if (null == goldOrder) {
			LOG.error("role=" + roleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", 角色售卖表中不存在此订单！");
			return false;
		}
		int orderState = goldOrder.getState();
		if (orderState != BlackMarketUtils.OrderState.STAY_TAKE) {
			LOG.error("role=" + roleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", order state=" + orderState + ", 订单状态非法.");
			return false;
		}
		// 更改订单为取完状态
		goldOrder.setState(BlackMarketUtils.OrderState.TAKE_DONE);
		// 添加金币
		long goldNumber = goldOrder.getNumber();
		long backGold = pack.addSysGold(goldNumber, "", YYLoggerTuJingEnum.tujing_Value_balckmarket, 1);
		if (BlackMarketManager.LOG.isInfoEnabled()) {
			LOG.info("role=" + roleId + ", pid=" + pid + ", gold=" + backGold + ", 取回金币订单中的金币！");
		}
		pack.addSysCurrency((long)(goldNumber*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_CBG))
				, MoneyType.MoneyType_EreditPoint, "藏宝阁取回金币订单中的金币", YYLoggerTuJingEnum.tujing_Value_balckmarket, 0);

		// 刷新订单状态
		SRefreshGoldOrderState refreshState = new SRefreshGoldOrderState(goldOrder.getPid(), goldOrder.getState());
		mkdb.Procedure.psendWhileCommit(roleId, refreshState);

		return true;
	}

	/**
	 * 金币订单浏览
	 * @param roleId
	 * @return
	 */
	public boolean goldOrderBrowse(final long roleId) {
		xbean.RoleBlackMarket roleBlackMarket = xtable.Blackmarkettab.select(roleId);
		if (null == roleBlackMarket) {
			LOG.error("role=" + roleId + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", 黑市订单表中不存在此角色信息！");
			return false;
		}
		
		Map<Long, xbean.GoldOrder> goldOrderBuyMap = roleBlackMarket.getGoldorderbuy();
		Map<Long, xbean.GoldOrder> goldOrderSaleMap = roleBlackMarket.getGoldordersale();
		
		ArrayList<GoldOrder> buyList = new ArrayList<>(10);
		ArrayList<GoldOrder> saleList = new ArrayList<>(10);
		
		if (goldOrderBuyMap.size() > 0) {
			int maxOrderLstNumber = 0;// 最大显示订单列表
			for (xbean.GoldOrder xbeanGoldOrder : goldOrderBuyMap.values()) {
				GoldOrder order = BlackMarketUtils.xbeanGoldOrderCopyGoldOrder(xbeanGoldOrder);
				buyList.add(order);
				if (LOG.isInfoEnabled()) {
					LOG.info(new StringBuilder("金币购买订单：").append(order.toString()));
				}
				if (++maxOrderLstNumber > BlackMarketUtils.getMaxOrderLstNumber()) {
					break;
				}
			}
			// 倒序
			Collections.sort(buyList, new Comparator<GoldOrder>() {
				@Override
				public int compare(GoldOrder o1, GoldOrder o2) {
					return Long.signum(o2.time - o1.time);
				}
			});
		}
		
		if (goldOrderSaleMap.size() > 0) {
			int maxOrderLstNumber = 0;// 最大显示订单列表
			for (xbean.GoldOrder xbeanGoldOrder : goldOrderSaleMap.values()) {
				GoldOrder order = BlackMarketUtils.xbeanGoldOrderCopyGoldOrder(xbeanGoldOrder);
				saleList.add(order);
				if (LOG.isInfoEnabled()) {
					LOG.info(new StringBuilder("金币售卖订单：").append(order.toString()));
				}
				if (++maxOrderLstNumber > BlackMarketUtils.getMaxOrderLstNumber()) {
					break;
				}
			}
			// 倒序
			Collections.sort(saleList, new Comparator<GoldOrder>() {
				@Override
				public int compare(GoldOrder o1, GoldOrder o2) {
					return Long.signum(o2.time - o1.time);
				}
			});
		}
		
		SGoldOrderBrowseBlackMarket goldOrderBrowse = new SGoldOrderBrowseBlackMarket(saleList, buyList);
		mkdb.Procedure.psendWhileCommit(roleId, goldOrderBrowse);
		
		return true;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////
	////////////////////////////////道具订单
	
	/**
	 * <b>事务内调用</b> 道具上架
	 * 
	 * @param roleId 角色id
	 * @param key 道具背包中的key
	 * @param number 转移数量
	 * @return 成功返回上架道具对象,失败返回null
	 */
	private ItemBase itemOrderUp(long roleId, int key, int number) {
		ItemMaps srcbag = Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		ItemMaps destbag = Module.getInstance().getItemMaps(roleId, BagTypes.BLACKMARKET, false);
		ItemBase upItem = srcbag.TransOut(key, number, "从背包移动道具到黑市中");
		if (upItem == null) {
			LOG.error("role=" + roleId + ", 上架道具在背包容器中不存在！");
			return null;
		}
		// 检查是否在冻结期
		long freezeTime = upItem.getDataItem().getMarkettime();
		if (freezeTime > 0) {
			long currTime = System.currentTimeMillis();
			if (currTime < freezeTime) {
				LOG.error("role=" + roleId + ", 道具id=" + upItem.getItemId() + ", 道具冻结期内不能上架！");				
				return null;
			}
		}
		AddItemResult ret = destbag.doAddItem(upItem, -1, "黑市上架", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_balckmarket, 0);
		if (LOG.isInfoEnabled()) {
			LOG.info("role=" + roleId + ", 将商品上架" + ret);
		}
		boolean succ = ret.name().equalsIgnoreCase("succ");
		if (!succ) {
			return null;
		}
		return upItem;
	}

	/**
	 * <b>事务内调用</b> 道具下架
	 * 
	 * @param roleId 角色id
	 * @param key 道具背包中的key
	 * @param number 转移数量
	 * @return 成功返回下架道具对象,失败返回null
	 */
	private ItemBase itemOrderDown(long roleId, int key, int number) {
		ItemMaps srcbag = Module.getInstance().getItemMaps(roleId, BagTypes.BLACKMARKET, false);
		ItemMaps destbag = Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		ItemBase downItem = srcbag.TransOut(key, number, "从黑市移动道具到背包中");
		if (downItem == null) {
			LOG.error("role=" + roleId + ", 下架道具在黑市容器中不存在 ！");
			return null;
		}
		AddItemResult ret = destbag.doAddItem(downItem, -1, "黑市下架", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_balckmarket, 0);
		if (LOG.isInfoEnabled()) {
			LOG.info("role=" + roleId + ", 将商品下架" + ret);
		}
		boolean succ = ret.name().equalsIgnoreCase("succ");
		if (!succ) {
			return null;
		}
		return downItem;
	}

	/**
	 * <b>事务内调用</b> 交易道具
	 * 
	 * @param buyRoleId 买家角色id
	 * @param saleRoleId 卖家角色id
	 * @param key 道具背包中的key
	 * @param number 购买数量
	 * @return element1 操作状态true成功false失败, element2 操作对象
	 */
	private Object[] itemOrderTrade(long buyRoleId, long saleRoleId, int key, int number) {
		ItemMaps srcbag = Module.getInstance().getItemMaps(saleRoleId, BagTypes.BLACKMARKET, false);// 卖家黑市道具容器
		ItemMaps destbag = Module.getInstance().getItemMaps(buyRoleId, BagTypes.BAG, false);// 买家背包容器
		// 卖家拍卖表
		ItemBase item = srcbag.TransOut(key, number, "黑市中售卖");
		if (item == null) {
			LOG.error("role=" + buyRoleId + ", 购买role=" + saleRoleId + ", 道具失败！");
			return new Object[] { false, null };
		}
		if (!MarketUtils.isPayService()) {
			// 设置拍卖冻结时间
			long freezeTimeConf = item.getItemAttr().getSalefreezetime();
			if (freezeTimeConf > 0) {
				freezeTimeConf = freezeTimeConf * MarketUtils.ONE_DAY;
				long currTime = System.currentTimeMillis();
				item.getDataItem().setMarkettime(currTime + freezeTimeConf);
			}
		}
		// 道具放入买家背包
		AddItemResult ret = destbag.doAddItem(item, -1, "黑市中购买", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_balckmarket, 0);
		boolean succ = ret.name().equalsIgnoreCase("succ");
		return new Object[] { succ, item };
	}

	/**
	 * <b>事务内调用</b> 宠物上架
	 * 
	 * @param roleId 角色id
	 * @param key 宠物key
	 * @return 成功返回上架宠物对象,失败返回null
	 */
	private Pet petOrderUp(long roleId, int key) {
		PetColumn srccol = new PetColumn(roleId, PetColumnTypes.PET, false);
		PetColumn dstcol = new PetColumn(roleId, PetColumnTypes.BLACKMARKETPET, false);
		
		// 检查宠物是否在冻结期
		fire.pb.pet.Pet pet = srccol.getPet(key);
		if (pet != null) {
			if (pet.isMarketFreeze()) {
				LOG.error("role=" + roleId + ", 上架宠物id=" + pet.getBaseId() + ", 宠物冻结期内不能上架！");
				return null;
			}
		}
		
		int newKey = PetColumn.doMovePet(srccol, key, dstcol);
		if (newKey < 0) {
			if (newKey == PetError.FightPetCantMoveErr) {
				LOG.error("role=" + roleId + ", 参战宠物不能上架!");
			} else {
				LOG.error("role=" + roleId + ", 宠物不能上架原因" + newKey);
			}
			
			final SPetError errosend = new SPetError(newKey);
			mkdb.Procedure.psend(roleId, errosend);
			return null;
		}
		
		return dstcol.getPet(newKey);
	}

	/**
	 * <b>事务内调用</b> 宠物下架
	 * 
	 * @param roleId 角色id
	 * @param key 宠物key
	 * @return 成功返回下架宠物对象,失败返回null
	 */
	private Pet petOrderDown(long roleId, int key) {
		PetColumn srccol = new PetColumn(roleId, PetColumnTypes.BLACKMARKETPET, false);
		PetColumn dstcol = new PetColumn(roleId, PetColumnTypes.PET, false);
		int newKey = PetColumn.doMovePet(srccol, key, dstcol);
		if (newKey < 0) {
			LOG.error("role=" + roleId + ", 宠物不能下架原因" + newKey);
			
			final SPetError errosend = new SPetError(newKey);
			mkdb.Procedure.psend(roleId, errosend);
			return null;
		}
		return dstcol.getPet(newKey);
	}

	/**
	 * <b>事务内调用</b> 交易宠物
	 * 
	 * @param buyRoleId 买家角色id
	 * @param saleRoleId 卖家角色id
	 * @param key 道具背包中的key
	 * @return 成功true
	 */
	private boolean petOrderTrade(long buyRoleId, long saleRoleId, int key) {
		PetColumn srccol = new PetColumn(saleRoleId, PetColumnTypes.BLACKMARKETPET, false);// 卖家宠物容器
		PetColumn dstcol = new PetColumn(buyRoleId, PetColumnTypes.PET, false);// 买家宠物栏容器
		
		if (!MarketUtils.isPayService()) {
			// 设置宠物冻结时间
			fire.pb.pet.Pet pet = srccol.getPet(key);
			if (pet != null) {
				long freezeTime = pet.getPetAttr().getMarketfreezetime();
				if (freezeTime > 0) {
					freezeTime = freezeTime * MarketUtils.ONE_DAY;
					long currTime = System.currentTimeMillis();
					pet.setMarketFreezeExpire(currTime + freezeTime);
				}
			}
		}
		
		int ret = PetColumn.moveBetweenRolePetColumn(srccol, dstcol, key);
		if (ret == -1) {
			LOG.error("role=" + buyRoleId + ", 购买role=" + saleRoleId + ", 宠物失败！");
			MessageMgr.sendMsgNotify(buyRoleId, 168001, null);
			return false;
		}
		return ret != 0 ? false : true;
	}
	
	//	MessageMgr.sendMsgNotify(roleId, 162192, null);   // 地精市场宠物上架最低101元
	//  MessageMgr.sendMsgNotify(roleId, 162193, null);   // 地精市场装备上架最低101元

}
