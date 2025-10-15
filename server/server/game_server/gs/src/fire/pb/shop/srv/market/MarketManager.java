package fire.pb.shop.srv.market;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import fire.log.YYLogger;
import fire.log.beans.OpAucConBean;
import fire.log.beans.OpAucRackBean;
import fire.log.beans.OpTradExBean;
import fire.pb.fushi.DayPayManager;
import fire.pb.game.MoneyType;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.item.Pack;
import fire.pb.main.Gs;
import fire.pb.pet.Pet;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.PetError;
import fire.pb.pet.PetManager;
import fire.pb.pet.SPetError;
import fire.pb.shop.GoodsType;
import fire.pb.shop.LogBean;
import fire.pb.shop.MarketGoods;
import fire.pb.shop.MarketSearchAttr;
import fire.pb.shop.MarketThreeTable;
import fire.pb.shop.SGetMarketUpPrice;
import fire.pb.shop.SMarketBrowse;
import fire.pb.shop.SMarketBuy;
import fire.pb.shop.SMarketContainerBrowse;
import fire.pb.shop.SMarketItemChatShow;
import fire.pb.shop.SMarketSearchResult;
import fire.pb.shop.SMarketTradeLog;
import fire.pb.shop.SMarketUpSucc;
import fire.pb.shop.STakeBackTempMarketContainerItem;
import fire.pb.shop.STempMarketContainer;
import fire.pb.shop.TempMarketContainerGoods;
import fire.pb.shop.srv.market.floating.FloatingMarketManager;
import fire.pb.shop.srv.market.jdbc.MarketDAO;
import fire.pb.shop.srv.market.jdbc.Page;
import fire.pb.shop.srv.market.jdbc.Table;
import fire.pb.shop.srv.market.jdbc.dao.EquipDAO;
import fire.pb.shop.srv.market.jdbc.dao.NormalDAO;
import fire.pb.shop.srv.market.jdbc.dao.PetDAO;
import fire.pb.shop.srv.market.pojo.DaoBean;
import fire.pb.shop.srv.market.pojo.EquipDaoBean;
import fire.pb.shop.srv.market.pojo.NormalDaoBean;
import fire.pb.shop.srv.market.pojo.PetDaoBean;
import fire.pb.shop.utils.MarketUtils;
import fire.pb.shop.utils.MarketUtils.AttentionType;
import fire.pb.talk.MessageMgr;
import fire.pb.tel.utils.GoodsSafeLocksUtils;
import fire.pb.tel.utils.TelBindUtils;
import xbean.PetInfo;
import xbean.Pod;

/**
 * 
 * 拍卖 包括功能：
 *  1、购买功能, 
 *  2、售卖功能,{1、提供指导价 2、负责将道具从背包容器转移到拍卖容器中} 
 *  3、售卖列表浏览
 *  4、交易记录
 *  5、关注
 *  6、搜索装备、搜索宠物
 *  7、发送道具链接（通过聊天发送的链接浏览拍卖中的道具）
 * 
 * @author liangyanpeng
 *
 */
public class MarketManager implements IMarket {

	public static final Logger LOG = Logger.getLogger("MARKET");

	/**
	 * 聊天展示并浏览道具缓存,只保存100条缓存
	 */
	protected Map<Long, ChatBrowseMarketItem> chatBrowseCache = Collections
			.synchronizedMap(new LinkedHashMap<Long, ChatBrowseMarketItem>() {

				private static final long serialVersionUID = 2618899083464775452L;
				private static final int MAX_ENTRIES = 100;

				protected boolean removeEldestEntry(Map.Entry<Long, ChatBrowseMarketItem> eldest) {
					return size() > MAX_ENTRIES;
				}

			});

	private MarketManager() {
	}

	private static class MarketMarketHolder {
		static MarketManager instance = new MarketManager();
	}

	public static MarketManager getInstance() {
		return MarketMarketHolder.instance;
	}

	/**
	 * <b>事务内调用</b> 上架 将背包中的道具转移到拍卖中
	 * 
	 * @param roleId 角色id
	 * @param key 道具背包中的key
	 * @param number 转移数量
	 * @return 成功返回上架道具对象,失败返回null
	 */
	private ItemBase moveBagItemToMarket(long roleId, int key, int number) {
		ItemMaps bagContainer = Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		ItemMaps marketContainer = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, false);
		ItemBase upItem = bagContainer.TransOut(key, number, "从背包移动道具到拍卖中");
		if (upItem == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("role=").append(roleId).append(", 上架道具在背包中不存在！");
			LOG.error(sb.toString());
			return null;
		}
		// 检查是否在冻结期
		long freezeTime = upItem.getDataItem().getMarkettime();
		if (freezeTime > 0) {
			long currTime = System.currentTimeMillis();
			if (currTime < freezeTime) {
				StringBuilder sb = new StringBuilder();
				sb.append("role=").append(roleId).append(", 道具id=").append(upItem.getItemId()).append(", 道具冻结期内不能上架！");
				LOG.error(sb.toString());				
				return null;
			}
		}
		AddItemResult ret = marketContainer.doAddItem(upItem, -1, "拍卖上架", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_paimaiget, 0);
		if (LOG.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("role=").append(roleId).append(", 将商品上架").append(ret);
			LOG.info(sb.toString());
		}
		boolean succ = ret.name().equalsIgnoreCase("succ");
		if (!succ) {
			return null;
		}
		return upItem;
	}

	/**
	 * <b>事务内调用</b> 下架 将拍卖中的道具转移到背包中
	 * 
	 * @param roleId 角色id
	 * @param key 道具背包中的key
	 * @param number 转移数量
	 * @return 成功返回下架道具对象,失败返回null
	 */
	private ItemBase moveMarketItemToBag(long roleId, int key, int number) {
		ItemMaps bagContainer = Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		ItemMaps marketContainer = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, false);
		ItemBase downItem = marketContainer.TransOut(key, number, "从拍卖移动道具到背包中");
		if (downItem == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("role=").append(roleId).append(", 下架道具在拍卖容器中不存在 ！");
			LOG.error(sb.toString());
			return null;
		}
		AddItemResult ret = bagContainer.doAddItem(downItem, -1, "拍卖下架", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_paimai, 0);
		if (LOG.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("role=").append(roleId).append(", 将商品下架").append(ret);
			LOG.info(sb.toString());
		}
		boolean succ = ret.name().equalsIgnoreCase("succ");
		if (!succ) {
			return null;
		}
		return downItem;
	}

	/**
	 * <b>事务内调用</b> 其它角色购买道具
	 * 
	 * @param buyRoleId 买家角色id
	 * @param saleRoleId 卖家角色id
	 * @param key 道具背包中的key
	 * @param number 购买数量
	 * @return element1 操作状态true成功false失败, element2 操作对象
	 */
	private Object[] mkdbBuy(long buyRoleId, long saleRoleId, int key, int number) {
		ItemMaps bagContainer = Module.getInstance().getItemMaps(buyRoleId, BagTypes.BAG, false);// 买家背包容器
		ItemMaps marketContainer = Module.getInstance().getItemMaps(saleRoleId, BagTypes.MARKET, false);// 卖家拍卖道具容器
		// 卖家拍卖表
		ItemBase item = marketContainer.TransOut(key, number, "拍卖中售卖");
		if (item == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("role=").append(buyRoleId).append(", 购买role=").append(saleRoleId).append(", 道具失败！");
			LOG.error(sb.toString());
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
		AddItemResult ret = bagContainer.doAddItem(item, -1, "拍卖中购买", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_goumai, 0);
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
	private Pet movePetToMarket(long roleId, int key) {
		PetColumn srccol = new PetColumn(roleId, PetColumnTypes.PET, false);
		PetColumn dstcol = new PetColumn(roleId, PetColumnTypes.MARKETPET, false);
		
		// 检查宠物是否在冻结期
		fire.pb.pet.Pet pet = srccol.getPet(key);
		if (pet != null) {
			if (pet.isMarketFreeze()) {
				StringBuilder sb = new StringBuilder();
				sb.append("role=").append(roleId).append(", 上架宠物id=").append(pet.getBaseId()).append(", 宠物冻结期内不能上架！");
				LOG.error(sb.toString());
				return null;
			}
		}
		
		int newKey = PetColumn.doMovePet(srccol, key, dstcol);
		if (newKey < 0) {
			if (newKey == PetError.FightPetCantMoveErr) {
				StringBuilder sb = new StringBuilder();
				sb.append("role=").append(roleId).append(", 参战宠物不能上架!");
				LOG.error(sb.toString());
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("role=").append(roleId).append(", 宠物不能上架原因").append(newKey);
				LOG.error(sb.toString());
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
	private Pet moveMarketToPet(long roleId, int key) {
		PetColumn srccol = new PetColumn(roleId, PetColumnTypes.MARKETPET, false);
		PetColumn dstcol = new PetColumn(roleId, PetColumnTypes.PET, false);
		int newKey = PetColumn.doMovePet(srccol, key, dstcol);
		if (newKey < 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("role=").append(roleId).append(", 宠物不能下架原因").append(newKey);
			LOG.error(sb.toString());
			
			final SPetError errosend = new SPetError(newKey);
			mkdb.Procedure.psend(roleId, errosend);
			return null;
		}
		return dstcol.getPet(newKey);
	}

	/**
	 * <b>事务内调用</b> 其它角色购买宠物
	 * 
	 * @param buyRoleId 买家角色id
	 * @param saleRoleId 卖家角色id
	 * @param key 道具背包中的key
	 * @return 成功true
	 */
	private boolean mkdbBuyPet(long buyRoleId, long saleRoleId, int key) {
		PetColumn srccol = new PetColumn(saleRoleId, PetColumnTypes.MARKETPET, false);// 卖家拍卖宠物容器
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
			StringBuilder sb = new StringBuilder();
			sb.append("role=").append(buyRoleId).append(", 购买role=").append(saleRoleId).append(", 宠物失败！");
			LOG.error(sb.toString());
			
			MessageMgr.sendMsgNotify(buyRoleId, 168001, null);
			return false;
		}
		return ret != 0 ? false : true;
	}

	/**
	 * <b>事务内调用</b> 买家扣钱,卖家加钱
	 * 
	 * @param costRoleId 扣钱角色id
	 * @param addRoleId 加钱角色id
	 * @param money 金额
	 * @return 成功true
	 */
	protected boolean costGold(long costRoleId, long addRoleId, int money, boolean israrity) {
		if (money <= 0)
			return false;
		// 买家
		Pack costRoleBag = new Pack(costRoleId, false);
		long gold = costRoleBag.getGold();
		long ereditPoint = costRoleBag.getCurrency(MoneyType.MoneyType_EreditPoint);
		if (gold - money < 0) {
			MessageMgr.sendMsgNotify(costRoleId, 160212, null);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(costRoleId).append(", 购买道具金币不足！");
			LOG.error(sbd.toString());
			return false;
		}
		long costcp =  money*(long)(israrity?fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_BUY_RARE):fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_BUY));
		if (ereditPoint + costcp < 0) {

			List<String> params = new ArrayList<String>();
			params.add("" +ereditPoint);
			params.add("" +(0-(ereditPoint + costcp)));
			fire.pb.talk.MessageMgr.sendMsgNotify(costRoleId, 180021, params);	

//			MessageMgr.sendMsgNotify(costRoleId, 180021, null);
			MessageMgr.sendSystemMessageToRole(costRoleId, 180026, null);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(costRoleId).append(", 购买道具信用值不足！");
			LOG.error(sbd.toString());
			return false;
		}
		costRoleBag.subGold(-money, "拍卖购买道具", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_paimai, 0);
		// 卖家
		// 需要扣税,目前是9%
		money = this.costTax(money);
		Pack addRoleBag = new Pack(addRoleId, false);
		long ret = addRoleBag.addSysGold(money, "拍卖购卖道具", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_paimaiget, 0);
		boolean success = ret == 0 ? false : true;
		if (!success) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(addRoleId).append(", 售卖道具增加金币失败！");
			LOG.error(sbd.toString());
		}
		return success;
	}

	/**
	 * <b>事务内调用</b> 卖家银币扣钱
	 * 
	 * @param costRoleId 扣钱角色id
	 * @param money 金额
	 * @return 成功true, 失败false
	 */
	protected boolean costMoney(long costRoleId, int money) {
		if (money <= 0)
			return false;
		Pack costRoleBag = new Pack(costRoleId, false);
		long gold = costRoleBag.getMoney();
		if (gold - money < 0)
			return false;
		long ret = costRoleBag.subMoney(-money, "拍卖购上架道具摊位费", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_paimaiget, 0);
		return ret == 0 ? false : true;
	}

	/**
	 * <b>事务内调用</b> 个人拍卖交易记录
	 * 
	 * @param type 1：购买 2：售卖
	 * @param log 日志内容
	 */
	public void tradeLog(int type, long roleId, LogBean logBean) {
		xbean.MarketTradeLog tradeLog = xtable.Markettradelogtab.get(roleId);
		if (tradeLog == null) {
			tradeLog = xbean.Pod.newMarketTradeLog();
			xtable.Markettradelogtab.insert(roleId, tradeLog);
		}
		List<xbean.LogBean> logs = null;
		xbean.LogBean tmp = Pod.newLogBean();
		if (type == MarketUtils.BUY_LOG) {
			logs = tradeLog.getBuylog();
			tmp.setItemid(logBean.itemid);
			tmp.setLevel(logBean.level);
			tmp.setMoney(logBean.price);
			tmp.setNumber(logBean.num);
			if (logs.size() > MarketUtils.LOG_SIZE) {
				logs.remove(0);
				logs.add(tmp);
				return;
			}
			logs.add(tmp);
		} else if (type == MarketUtils.SALE_LOG) {
			logs = tradeLog.getSalelog();
			tmp.setItemid(logBean.itemid);
			tmp.setLevel(logBean.level);
			tmp.setMoney(logBean.price);
			tmp.setNumber(logBean.num);
			if (logs.size() > MarketUtils.LOG_SIZE) {
				logs.remove(0);
				logs.add(tmp);
				return;
			}
			logs.add(tmp);
		}
	}

	/**
	 * <b>事务内调用</b> 查询交易记录
	 */
	public void getTradeLogByRoleId(long roleId) {
		xbean.MarketTradeLog tradeLog = xtable.Markettradelogtab.select(roleId);
		SMarketTradeLog send = new SMarketTradeLog();
		if (tradeLog != null) {
			for (xbean.LogBean logBean : tradeLog.getBuylog()) {
				LogBean tmp = new LogBean();
				tmp.itemid = logBean.getItemid();
				tmp.level = logBean.getLevel();
				tmp.price = logBean.getMoney();
				tmp.num = logBean.getNumber();
				send.buylog.add(tmp);
			}
			for (xbean.LogBean logBean : tradeLog.getSalelog()) {
				LogBean tmp = new LogBean();
				tmp.itemid = logBean.getItemid();
				tmp.level = logBean.getLevel();
				tmp.price = logBean.getMoney();
				tmp.num = logBean.getNumber();
				send.salelog.add(tmp);
			}
		}
		mkdb.Procedure.psendWhileCommit(roleId, send);
	}
	
	/**
	 * <b>事务内调用</b> 清除交易记录
	 */
	public void cleanTradeLogByRoleId(long roleId) {
		xbean.MarketTradeLog tradeLog = xtable.Markettradelogtab.select(roleId);
		if (tradeLog != null) {
			xtable.Markettradelogtab.remove(roleId);
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 清除交易记录");
				LOG.info(sbd.toString());
			}
		}
		this.getTradeLogByRoleId(roleId);
	}
	
	
	@Override
	public boolean up(int upType, long roleId, int key, int number, int money) {
		if (Gs.isMergeServer()) {
			// 查询表中此角色是否有取回临时拍卖道具的历史
			Integer val = xtable.Takebackmarketcontainertab.select(roleId);
			if (val == null) {
				this.sendTempMarketContainer(roleId);
				if (LOG.isInfoEnabled()) {
					StringBuilder sbd = new StringBuilder();
					sbd.append("role=").append(roleId).append(", 上架道具返回临时背包！");
					LOG.info(sbd.toString());
				}
				MessageMgr.sendSystemMessageToRole(roleId, 190091, null);
				return false;
			}
		}
		if (number <= 0 || money <= 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 金额或数量不能为空！");
			LOG.error(sbd.toString());
			return false;
		}

		if (money > MarketUtils.MAX_PRICE) {
			money = MarketUtils.MAX_PRICE;
		}

		long uniqid = 0;
		int itemid = 0;
		int israrity = 0;
		int itemtype = 0;
		int logictype = 0;
		int firstno = 0;
		int twono = 0;
		int threeno = 0;
		int level = 0;// 等级或品质
		long showtime = 0;
		long expiretime = 0;
		long nextKey = xtable.Equiptab.nextKey();

		// 玩家等级小于最小购买出售等级
		int marketLv = MarketUtils.getMarketLv();
		if (marketLv < 0) {
			return false;
		}
		//创建角色超出七天不绑定手机限制拍卖行和兑换所的使用
		if(TelBindUtils.isLimitByBindTel(roleId)){
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 上架道具错误！创建角色超出七天不绑定手机限制拍卖行");
			LOG.error(sbd.toString());
			return false;
		}
		int buyRoleLevel = xtable.Properties.selectLevel(roleId);
		if (buyRoleLevel < marketLv) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 上架道具错误！等级小于").append(marketLv);
			LOG.error(sbd.toString());
			return false;
		}
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) // 点卡服务器 by changhao
		{
			boolean ok = DayPayManager.getInstance().CheckTrade(roleId, buyRoleLevel);
			if (ok == false) {
				MessageMgr.psendMsgNotify(roleId, 300005, null);	
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 点卡服,你不能拍卖！");
				LOG.error(sbd.toString());
				return false;
			}
		}

		if (!this.checkMarketSize(roleId)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 拍卖容器中已没有空位置最大上限为[").append(MarketUtils.MARKET_CONTAINER_LIMIT).append("]个");
			LOG.error(sbd.toString());
			return false;
		}

		int stallPrice = this.computeStallPrice(money);
		if (stallPrice <= 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 摊位费计算后不合法！");
			LOG.error(sbd.toString());
			return false;
		}

		// 扣除摊位费
		if (!this.costMoney(roleId, stallPrice)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 扣除摊位费失败！");
			LOG.error(sbd.toString());
			return false;
		}
		// 数据库操作成功或失败标志
		boolean succ = false;
		MarketThreeTable itemConf = null;
		
		if (MarketUtils.CONTAINER_BAG == upType) {
			ItemBase item = moveBagItemToMarket(roleId, key, number);
			if (item == null) {
				return false;
			}
			//道具安全锁判断
			if (GoodsSafeLocksUtils.checkLockStatus(roleId,item)) {
				LOG.error("开启了道具安全锁，需要输入密码" + roleId);
				return false;
			}
			// 获取配置
			itemid = item.getItemId();
			if (!checkMarketUp(itemid)) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 上架拍卖道具id=").append(itemid).append(", 不是可上架的道具！");
				LOG.error(sbd.toString());
				return false;
			}
			itemConf = fire.pb.shop.Module.marketThreeTableMap.get(itemid);
			uniqid = item.getUniqId();
			itemtype = itemConf.itemtype;
			israrity = itemConf.israrity;
			logictype = itemConf.logictype;
			firstno = itemConf.firstno;
			twono = itemConf.twono;
			threeno = itemConf.threeno;
			
			if (israrity == MarketUtils.RARITY && !MarketUtils.isPayService()) {
				showtime = MarketUtils.getShowTime();
				expiretime = MarketUtils.getRarityExpireTime();
			} else {
				expiretime = MarketUtils.getNotRarityExpireTime();
			}
			
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 上架拍卖道具id=").append(itemid).append(", 上架的道具公示时间=").append(showtime).append(", 过期时间=").append(expiretime);
				LOG.info(sbd.toString());
			}
			
			// 数据库存储
			if (item instanceof EquipItem) {
				EquipItem equipItem = (EquipItem) item;
				// 检查非珍品道具价格50%的价格调整
				if (israrity == MarketUtils.NOT_RARITY) {
					if (!MarketUtils.checkUpPriceFloating(itemid, equipItem.getItemAttr().getLevel(), money)) {
						StringBuilder sbd = new StringBuilder();
						sbd.append("role=").append(roleId).append(", 上架拍卖装备道具id=").append(itemid).append(", 上架价格不在合理价格！");
						LOG.error(sbd.toString());
						return false;
					}
				}
/*				
				if (equipItem.getTreasure() == 0) {
					LOG.error("非珍品装备不能上架!");
					return false;
				}
				*/
				EquipDaoBean edb = new EquipDaoBean();
				edb.setId(nextKey);
				edb.setFirstno(firstno);
				edb.setTwono(twono);
				// 配表中逻辑类型为0是没有3级菜单的意思
				if (logictype != MarketUtils.NO_THREE_MENU) {
					edb.setThreeno(threeno);
				} else {
					edb.setThreeno(MarketUtils.NO_THREE_MENU);
				}
				edb.setUniquid(equipItem.getUniqId());
				edb.setKey(equipItem.getKey());
				edb.setRoleid(roleId);
				edb.setItemid(itemid);
				edb.setName(equipItem.getName());
				edb.setShowtime(showtime);
				edb.setExpiretime(expiretime);
				edb.setExtid(equipItem.getDataItem().getExtid());
				edb.setLevel(equipItem.getItemAttr().getLevel());
				edb.setEffect(equipItem.getEquipAttr().getEffect());// 特效
				edb.setSkill(equipItem.getEquipAttr().getSkill());// 特技
				edb.setColor(equipItem.getItemAttr().getNquality());// 品质
				edb.setAttr(equipItem.getEquipAttr().getAttr());// 基础属性
				edb.setAddattr(equipItem.getEquipAttr().getAddattr()); // 附加属性
				edb.setTotalattr(MarketUtils.getTotalAttr(equipItem.getEquipAttr().getAddattr()));// 属性总和
				edb.setEquipscore(equipItem.getEquipAttr().getEquipscore());// 评分
				edb.setNumber(1);
				edb.setPrice(money);
				edb.setAttentionnumber(0);
				level = equipItem.getItemAttr().getLevel();
				// store
				succ = MarketStore.addEquip(nextKey, edb);
			} else {
				level = MarketUtils.getItemLevel(item);
				// 检查非珍品道具价格50%的价格调整
				if (israrity == MarketUtils.NOT_RARITY) {
					if (!MarketUtils.checkUpPriceFloating(itemid, level, money)) {
						StringBuilder sbd = new StringBuilder();
						sbd.append("role=").append(roleId).append(", 上架拍卖普通道具id=").append(itemid).append(", 上架价格不在合理价格！");
						LOG.error(sbd.toString());
						return false;
					}
				}
				NormalDaoBean ndb = new NormalDaoBean();
				ndb.setId(nextKey);
				ndb.setFirstno(firstno);
				ndb.setTwono(twono);
				// 配表中逻辑类型为0是没有3级菜单的意思
				if (logictype != MarketUtils.NO_THREE_MENU) {
					ndb.setThreeno(threeno);
				} else {
					ndb.setThreeno(MarketUtils.NO_THREE_MENU);
				}
				ndb.setNumber(number);
				ndb.setUniquid(item.getUniqId());
				ndb.setKey(item.getKey());
				ndb.setRoleid(roleId);
				ndb.setItemid(itemid);
				ndb.setName(item.getName());
				ndb.setShowtime(showtime);
				ndb.setExpiretime(expiretime);
				ndb.setExtid(item.getDataItem().getExtid());
				ndb.setLevel(level);
				ndb.setPrice(money);
				ndb.setAttentionnumber(0);
				// store
				succ = MarketStore.addNormal(nextKey, ndb);
			}
		}

		if (MarketUtils.CONTAINER_PET == upType) {
			Pet pet = movePetToMarket(roleId, key);
			if (pet == null) {
				return false;
			}
			//道具安全锁判断
			if (GoodsSafeLocksUtils.checkLockStatus(roleId,pet.getPetInfo())) {
				LOG.error("开启了道具安全锁，需要输入密码" + roleId);
				return false;
			}
			if (pet.getPetInfo().getTreasure() == 0 && !MarketUtils.isPayService()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 上架拍卖宠物id=").append(itemid).append(", 非珍品宠物不能上架！");
				LOG.error(sbd.toString());
				return false;
			}
			// 获取配置
			itemid = pet.getBaseId();
			if (!checkMarketUp(itemid)) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 上架拍卖宠物id=").append(itemid).append(", 不是可上架的宠物！");
				LOG.error(sbd.toString());
				return false;
			}
			itemConf = fire.pb.shop.Module.marketThreeTableMap.get(itemid);
			uniqid = pet.getUniqueId();
			israrity = itemConf.israrity;
			itemtype = itemConf.itemtype;
			logictype = itemConf.logictype;
			firstno = itemConf.firstno;
			twono = itemConf.twono;
			
			if (israrity == MarketUtils.RARITY && !MarketUtils.isPayService()) {
				showtime = MarketUtils.getShowTime();
				expiretime = MarketUtils.getRarityExpireTime();
			} else {
				expiretime = MarketUtils.getNotRarityExpireTime();
			}
			
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 上架拍卖道具id=").append(itemid).append(", 上架的道具公示时间=").append(showtime).append(", 过期时间=").append(expiretime);
				LOG.info(sbd.toString());
			}
			// 数据库存储
			fire.pb.Pet petInfo = pet.getProtocolPet();
			itemid = petInfo.id;
			PetDaoBean pdb = new PetDaoBean();
			pdb.setId(nextKey);
			pdb.setFirstno(firstno);
			pdb.setTwono(twono);
			// 配表中逻辑类型为0是没有3级菜单的意思
			if (logictype != MarketUtils.NO_THREE_MENU) {
				pdb.setThreeno(threeno);
			} else {
				pdb.setThreeno(MarketUtils.NO_THREE_MENU);
			}
			pdb.setRoleid(roleId);
			pdb.setShowtime(showtime);
			pdb.setExpiretime(expiretime);
			pdb.setUniquid(pet.getUniqueId());
			pdb.setItemid(itemid);
			pdb.setKey(petInfo.key);
			pdb.setName(petInfo.name);
			pdb.setLevel(petInfo.level);
			pdb.setAttack(petInfo.attack);
			pdb.setDefend(petInfo.defend);
			pdb.setSpeed(petInfo.speed);
			pdb.setMagicattack(petInfo.magicattack);
			pdb.setMagicdef(petInfo.magicdef);
			pdb.setMaxhp(petInfo.maxhp);
			pdb.setAttackapt(petInfo.attackapt);
			pdb.setDefendapt(petInfo.defendapt);
			pdb.setPhyforceapt(petInfo.phyforceapt);
			pdb.setMagicapt(petInfo.magicapt);
			pdb.setSpeedapt(petInfo.speedapt);
			pdb.setDodgeapt(petInfo.dodgeapt);
			pdb.setGrowrate(petInfo.growrate);
			pdb.setSkills(MarketUtils.toInnerPetSkill(petInfo.skills));
			pdb.setSkillNumber(petInfo.skills.size());
			pdb.setPetscore(petInfo.petscore);
			pdb.setNumber(1);
			pdb.setPrice(money);
			pdb.setAttentionnumber(0);
			level = petInfo.level;
			// save
			succ = MarketStore.addPet(nextKey, pdb);
		}

		if (!succ) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 上架道具id=").append(itemid).append(", 数据库增加道具失败！");
			LOG.error(sbd.toString());
			return false;
		}

		// 特殊类型处理(区间范围)
		if (itemtype == GoodsType.OTHER_ITEM) {
			level = MarketUtils.retRangeByLevel(level);
		}
		// 增加挂单数
		FloatingMarketManager.getInstance().hangOrderNumInc(itemid, level);

		// 上架成功的时候发送通知给前端
		SMarketUpSucc succmsg = new SMarketUpSucc();
		succmsg.israrity = 0;
		if (israrity == MarketUtils.RARITY) {
			// 是珍品
			succmsg.israrity = 1;
		}
		mkdb.Procedure.psendWhileCommit(roleId, succmsg);
		
		// 运营日志
		writeYYLogger(roleId, uniqid, itemConf, 1);
		//清除数据
     	GoodsSafeLocksUtils.doClearDataWhileCommit(roleId);
		if (LOG.isInfoEnabled()) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 上架拍卖道具id=").append(itemid).append(", 数据库唯一id=").append(nextKey).append(", 上架的道具公示时间=").append(showtime).append(", 过期时间=").append(expiretime);
			LOG.info(sbd.toString());
		}
		
		return succ;
	}

	/**
	 * 运营日志 - 上下架
	 * @param roleId
	 * @param number
	 * @param money
	 * @param level
	 * @param itemConf
	 */
	private void writeYYLogger(long roleId, long uniqid, MarketThreeTable itemConf, int opId) {
		if(itemConf == null) return;

		OpAucRackBean opAucRackBean = new OpAucRackBean(opId
				, itemConf.getTwono(), 2, getMarketSize(roleId), 
				itemConf.getId(), itemConf.getItemtype(), getQuality(itemConf.itemtype, itemConf.getId()), itemConf.getIsrarity(), uniqid, itemConf.getIsrarity());
		YYLogger.aucRackLog(roleId, opAucRackBean);
	}

	@Override
	public boolean down(int downType, long roleId, int key) {
		int itemId = 0;
		long uniqueId = 0;
		int level = 0;
		MarketGoods marketGoods = null;
		long id = 0L;//数据库中道具唯一id
		boolean succ = false;
		if (GoodsType.NORMAL_ITEM == downType) {
			ItemBase item = moveMarketItemToBag(roleId, key, -1);
			if (item == null) {
				return false;
			}
			// 数据库删除操作
			if (item instanceof EquipItem) {
				EquipItem equipItem = (EquipItem) item;
				uniqueId = equipItem.getUniqId();
				itemId = equipItem.getItemId();
				level = equipItem.getItemAttr().getLevel();
				marketGoods = EquipDAO.getInstance().queryEquip(roleId, uniqueId, itemId, key);
				id = marketGoods.id;
				// remove
				succ = MarketStore.removeEquip(id);
			} else {
				uniqueId = item.getUniqId();
				itemId = item.getItemId();
				level = item.getItemAttr().getLevel();
				marketGoods = NormalDAO.getInstance().queryNormal(roleId, uniqueId, itemId, key);
				id = marketGoods.id;
				// remove
				succ = MarketStore.removeNormal(id);
			}
		}
		if (GoodsType.PET == downType) {
			Pet pet = moveMarketToPet(roleId, key);
			if (pet == null) {
				return false;
			}
			// 宠物表删除数据
			itemId = pet.getBaseId();
			uniqueId = pet.getUniqueId();
			level = pet.getLevel();
			marketGoods = PetDAO.getInstance().queryPet(roleId, uniqueId, itemId, key);
			id = marketGoods.id;
			// remove
			succ = MarketStore.removePet(id);
		}

		if (!succ) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 下架道具id=").append(itemId).append(", 数据库id=").append(id).append(", 数据库删除道具失败！");
			LOG.error(sbd.toString());
			return false;
		}

		// 减少挂单量
		FloatingMarketManager.getInstance().hangOrderNumDec(itemId, level);
		
		// 运营日志
		MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(itemId);
		writeYYLogger(roleId, uniqueId, itemConf, 0);
		
		if (marketGoods != null) {
			int itemtype = itemConf.itemtype;
			// 通知关注的玩家删除此关注商品的引用
			String attentionKey = MarketUtils.genAttentionKey(itemtype, marketGoods.id);
			boolean delAttention = MarketUtils.notifyRoleRemoveAttention(attentionKey, 0);
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", attentionId=").append(attentionKey).append(", 下架删除操作状态").append(delAttention);
				LOG.info(sbd.toString());
			}
		}
		
		// 更改缓存状态
		if (chatBrowseCache.containsKey(id)) {
			ChatBrowseMarketItem chatBrowseMarketItem = chatBrowseCache.get(id);
			chatBrowseMarketItem.setState(1);
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 下架道具id=").append(itemId).append(", 数据库id=").append(id).append(", 更改聊天展示拍卖道具缓存！");
				LOG.info(sbd.toString());
			}
		}
		
		if (LOG.isInfoEnabled()) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 下架道具id=").append(itemId).append(", 数据库id=").append(id).append(", 操作状态").append(succ);
			LOG.info(sbd.toString());
		}
		
		return succ;
	}
	
	public boolean buy(long buyRoleId, long id, long saleRoleId, int itemid, int number) {
		Boolean succ = false;
		// 创建角色超出七天不绑定手机限制拍卖行和兑换所的使用
		if (fire.pb.tel.utils.TelBindUtils.isLimitByBindTel(buyRoleId)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(buyRoleId).append(", 购买道具错误！创建角色超出七天不绑定手机限制拍卖行");
			LOG.error(sbd.toString());
			return false;
		}
		if (!MarketUtils.isPayService()) {
			succ = presellBuy(buyRoleId, id, saleRoleId, itemid, number);
		} else {
			succ = normalBuy(buyRoleId, id, saleRoleId, itemid, number);
		}

		return succ;
	}
	
	/**
	 * 预购购买
	 */
	private boolean presellBuy(long buyRoleId, long id, long saleRoleId, int itemid, int number) {
		if (number <= 0 || number > Integer.MAX_VALUE) {
			return false;
		}
		MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(itemid);
		if (null == itemConf || itemConf.getCanstall() == 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(buyRoleId).append(", 购买不存在的道具id=").append(itemid).append(", 或这个道具已禁止售卖！");
			LOG.error(sbd.toString());
			return false;
		}
		// 是否符合拍卖购买等级
		if (!checkBuyLv(buyRoleId)) {
			return false;
		}
		int itemtype = itemConf.getItemtype();
		int israrity = itemConf.israrity;
		int money = 0;
		long showTime = 0;
		///////////////////////////
		// 不是珍品走正常购买逻辑
		if (israrity != MarketUtils.RARITY) {
			return normalBuy(buyRoleId, id, saleRoleId, itemid, number);
		}
		
		/////////////////////////////////
		// 珍品装备需要购买预购
		
		// 购买普通装备
		if (GoodsType.NORMAL_ITEM == itemtype || GoodsType.OTHER_ITEM == itemtype) {
			xbean.NormalItem ni = MkdbStore.selectNormal(id);
			if (ni == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 预购不存在的道具id=").append(itemid).append(", 数据库id=").append(id).append(", 商品已售出！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 160187, null);
				return false;
			}
			money = ni.getPrice();
			showTime = ni.getShowtime();
		}
		// 购买装备
		if (GoodsType.EQUIP_ITEM == itemtype) {
			xbean.EquipItem ei = MkdbStore.selectEquip(id);
			if (ei == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 预购不存在的道具id=").append(itemid).append(", 数据库id=").append(id).append(", 商品已售出！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 160187, null);
				return false;
			}
			money = ei.getPrice();
			showTime = ei.getShowtime();
		}
		// 购买宠物
		if (GoodsType.PET == itemtype) {
			xbean.PetItem pi = MkdbStore.selectPet(id);
			if (pi == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 预购不存在的道具id=").append(itemid).append(", 数据库id=").append(id).append(", 商品已售出！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 160187, null);
				return false;
			}
			money = pi.getPrice();
			showTime = pi.getShowtime();
			// 过滤掉不符合的玩家申请
			PetAttr pattr = fire.pb.pet.Module.getInstance().getPetManager().getAttr(pi.getItemid());
			if(pattr == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 预购宠物id=").append(itemid).append(", 数据库id=").append(id).append(", PetAttr null！");
				LOG.error(sbd.toString());
				return false;
			}
			int takelevel = pattr.getTakelevel();
			if (xtable.Properties.selectLevel(buyRoleId) < takelevel) {
				MessageMgr.sendMsgNotify(buyRoleId, 141610, null);
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 预购宠物id=").append(itemid).append(", 数据库id=").append(id).append(", 宠物的携带等级过高！");
				LOG.error(sbd.toString());
				return false;
			}
		}
		
		if (money <= 0 || showTime <= 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(buyRoleId).append(", 预购道具id=").append(itemid).append(", 数据库id=").append(id).append(", 价格或者公示时间异常不能等于小于0！");
			LOG.error(sbd.toString());
			return false;
		}
		
		long now = System.currentTimeMillis();
		long time = now - showTime;
		xbean.PresellItem presellItem = xtable.Preselltab.get(id);
		if (presellItem != null) {
			// 如果过期时间大于2分钟,要手动删除,走正常购买
			// 防止关闭服务器时候,有玩家正在预购中
			if (now - presellItem.getExpiretime() > 120000) {
				boolean f = xtable.Preselltab.remove(id);
				if (f) {
					presellItem = xtable.Preselltab.get(id);
				}
				f = presellItem != null ? false : true;
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 预购过期时间大于2分钟的道具id=").append(itemid).append(", 数据库id=").append(id).append(", 删除").append(f).append(", 走正常购买！");
				LOG.error(sbd.toString());
			}
		}
		///////////////////////////////////////////////////////////
		//公示时间超过一分钟并且预购列表中没有此道具,走正常购买
		if (time >= 60000 && presellItem == null) {
			return normalBuy(buyRoleId, id, saleRoleId, itemid, number);
		}
		
		if ((time > 60000 && presellItem != null)) {
			MessageMgr.sendMsgNotify(buyRoleId, 190061, null); // 预购未结束，暂时不能购买
			return false;
		}
		
		///////////////////////////////////////
		// 创建个新的预购队列
		if (!MarketUtils.checkMoneyAndCredit(buyRoleId, money)) {
			return false;
		}
		
		if (presellItem == null) {
			long exprieTime = System.currentTimeMillis() + MarketUtils.getPresellTime();
			presellItem = Pod.newPresellItem();
			presellItem.getRoleid().add(buyRoleId);
			presellItem.setExpiretime(exprieTime);
			xtable.Preselltab.insert(id, presellItem);
			MessageMgr.sendMsgNotify(buyRoleId, 190053, null);
			//创建定时任务
			return scheduleBuy(id, saleRoleId, itemid, money);
		}
		if (now > presellItem.getExpiretime()) {
			MessageMgr.sendMsgNotify(buyRoleId, 190056, null);
			return false;
		}
		// 验证人数
		List<Long> roles = presellItem.getRoleid();
		if (LOG.isInfoEnabled()) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("参与预购").append("道具id=").append(itemid).append(", 数据库id=").append(id).append(", 人数：").append(roles.size());
			LOG.info(sbd.toString());
		}
		if (roles.size() > MarketUtils.getPresellQueueMaxNum()) {
			MessageMgr.sendMsgNotify(buyRoleId, 190057, null);
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("参与预购").append("道具id=").append(itemid).append(", 数据库id=").append(id).append(", 预购人数已满！");
				LOG.info(sbd.toString());
			}
			return false;
		}
		MessageMgr.sendMsgNotify(buyRoleId, 190053, null);
		// 添加道具购买角色
		return presellItem.getRoleid().add(buyRoleId);
			
	}

	/**
	 * 普通购买
	 */
	@Override
	public boolean normalBuy(long buyRoleId, long id, long saleRoleId, int itemid, int number) {
		if (number <= 0 || number > Integer.MAX_VALUE) {
			return false;
		}
		MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(itemid);
		if (null == itemConf || itemConf.getCanstall() == 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(buyRoleId).append(", 购买不存在的道具id=").append(itemid).append(", 或这个道具已禁止售卖！");
			LOG.error(sbd.toString());
			return false;
		}
		Boolean succ = false;
		// 是否符合拍卖购买等级
		succ = checkBuyLv(buyRoleId);
		if (!succ) {
			return succ;
		}
		int level = 0, money = 0;
		int itemtype = itemConf.itemtype;
		int israrity = itemConf.israrity;
		List<Integer> prices = itemConf.getPrices();
		String itemname = "";
		int newSaleNumber = 0;
		long currTime = System.currentTimeMillis();
		long showTime = 0;
		long expireTime = 0;

		// 购买普通道具
		long uniqId = 0;
		if (GoodsType.NORMAL_ITEM == itemtype || GoodsType.OTHER_ITEM == itemtype) {
			NormalDaoBean ndb = NormalDAO.getInstance().queryNormal(id);
			if (ndb == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买不存在的道具id=").append(itemid).append(", 数据库id=").append(id).append(", 商品已售出！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 160187, null);
				return false;
			}
			// 验证公示时间是否到期可购买
			showTime = ndb.getShowtime();
			if (showTime >= currTime) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 玩家用外挂封包购买公示道具！");
				LOG.error(sbd.toString());
				return false;
			}
			// 验证是否未超过期时间
			expireTime = ndb.getExpiretime();
			if (expireTime <= currTime) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 道具已过期！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 168005, null);// 商品信息已刷新，请重新打开界面购买
				return false;
			}
			uniqId = ndb.getId();
			itemname = ndb.getName();
			int key = ndb.getKey();
			
			// 增加金币和减少金币
			money = ndb.getPrice() * number;
			if (!this.costGold(buyRoleId, saleRoleId, money, israrity == MarketUtils.RARITY)) {
				return false;
			}
			
			Object[] results = mkdbBuy(buyRoleId, saleRoleId, key, number);
			succ = (Boolean) results[0];
			if (!succ) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 购买失败,道具被买走或下架！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 168005, null);
				return false;
			}
			// 等级或品质
			level = ndb.getLevel();
			// 特殊类型得特殊处理,为了价格浮动统计的计算
			if (GoodsType.OTHER_ITEM == itemtype) {
				ItemBase item = (ItemBase) results[1];
				level = MarketUtils.getItemLevel(item);
			}
			
			// 处理购买数量
			int saleNumber = ndb.getNumber();
			if (saleNumber == number) {
				// remove
				succ = MarketStore.removeNormal(id);
			} else if (saleNumber > number) {
				newSaleNumber = saleNumber - number;
				// update
				succ = MarketStore.updateNumber(Table.ITEM_NORMAL, id, newSaleNumber);
			} else {
				succ = false;
			}
		}

		// 购买装备
		if (GoodsType.EQUIP_ITEM == itemtype) {
			EquipDaoBean edb = EquipDAO.getInstance().queryEquip(id);
			if (edb == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买不存在的道具id=").append(itemid).append(", 数据库id=").append(id).append(", 商品已售出！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 160187, null);
				return false;
			}
			// 验证公示时间是否到期可购买
			showTime = edb.getShowtime();
			if (showTime >= currTime) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 玩家用外挂封包购买公示道具！");
				LOG.error(sbd.toString());
				return false;
			}
			// 验证是否未超过期时间
			expireTime = edb.getExpiretime();
			if (expireTime <= currTime) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 道具已过期！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 168005, null);
				return false;
			}
			itemname = edb.getName();
			int key = edb.getKey();
			level = edb.getLevel();
			money = edb.getPrice() * number;
			uniqId = edb.getId();
			
			// 增加金币和减少金币
			if (!this.costGold(buyRoleId, saleRoleId, money,israrity == MarketUtils.RARITY)) {
				return false;
			}

			Object[] results = mkdbBuy(buyRoleId, saleRoleId, key, 1);
			succ = (Boolean) results[0];
			if (!succ) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 购买失败,道具被买走或下架！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 168005, null);
				return false;
			}
			// remove
			succ = MarketStore.removeEquip(id);
		}

		// 购买宠物
		if (GoodsType.PET == itemtype) {
			PetDaoBean pdb = PetDAO.getInstance().queryPet(id);
			if (pdb == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买不存在的道具id=").append(itemid).append(", 数据库id=").append(id).append(", 商品已售出！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 160187, null);
				return false;
			}
			// 验证公示时间是否到期可购买
			showTime = pdb.getShowtime();
			if (showTime >= currTime) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 玩家用外挂封包购买公示道具！");
				LOG.error(sbd.toString());
				return false;
			}
			// 验证是否未超过期时间
			expireTime = pdb.getExpiretime();
			if (expireTime <= currTime) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 道具已过期！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 168005, null);
				return false;
			}
			itemname = pdb.getName();
			int key = pdb.getKey();
			level = pdb.getLevel();
			money = pdb.getPrice() * number;
			uniqId = pdb.getUniquid();
			
			// 增加金币和减少金币
			if (!this.costGold(buyRoleId, saleRoleId, money,israrity == MarketUtils.RARITY)) {
				return false;
			}

			succ = mkdbBuyPet(buyRoleId, saleRoleId, key);
			if (!succ) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 购买失败,道具被买走或下架！");
				LOG.error(sbd.toString());
				MessageMgr.sendMsgNotify(buyRoleId, 168005, null);
				return false;
			}
			// remove
			succ = MarketStore.removePet(id);
		}

		if (!succ) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 数据库删除道具失败！");
			LOG.error(sbd.toString());
			return false;
		}
		Pack costRoleBag = new Pack(buyRoleId, false);
		Pack addRoleBag = new Pack(saleRoleId, false);
		if(israrity == MarketUtils.RARITY)
		{
			costRoleBag.addSysCurrency((long)(money*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_BUY_RARE))
					, MoneyType.MoneyType_EreditPoint, "摆摊交易",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_paimai, 0);
			addRoleBag.addSysCurrency(this.costTax((int)(money*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_SALL_RARE)))
					, MoneyType.MoneyType_EreditPoint, "摆摊交易",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_paimaiget, 0);
		}
		else
		{
			costRoleBag.addSysCurrency((long)(money*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_BUY))
					, MoneyType.MoneyType_EreditPoint, "摆摊交易",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_paimai, 0);
			addRoleBag.addSysCurrency(this.costTax((int)(money*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_SALL)))
					, MoneyType.MoneyType_EreditPoint, "摆摊交易",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_paimaiget, 0);
		}
			
		// 特殊类型处理(区间范围)
		if (itemtype == GoodsType.OTHER_ITEM) {
			level = MarketUtils.retRangeByLevel(level);
		}
		// 增加售卖总金额和售卖数量
		FloatingMarketManager.getInstance().totalMoneyAndSaleNumInc(itemid, level, money, number);
		// 是珍品或点卡服才记录日志
		if (israrity == MarketUtils.RARITY || MarketUtils.isPayService()) {
			this.tradeLog(MarketUtils.BUY_LOG, buyRoleId, new LogBean(itemid, number, money, level));
			this.tradeLog(MarketUtils.SALE_LOG, saleRoleId, new LogBean(itemid, number, money, level));
		}

		money = this.costTax(money);
		
		// 好友系统消息提示：
		// 恭喜！您出售的$parameter1$已经被ID$parameter2$购买。\n您获得$parameter3$金币\n今日税率：9%
		MessageMgr.sendSystemMessageToRole(saleRoleId, 160216,
				Arrays.asList(itemname, MarketUtils.getShieldRoleId(buyRoleId), Integer.toString(money)));
		
		// 购买成功提示
//		if (MarketUtils.isPayService())
			MessageMgr.psendMsgNotify(buyRoleId, 160258, null);
//		else
//			MessageMgr.psendMsgNotify(buyRoleId, 190054, null);

		// 购买成功发送
		SMarketBuy procotol = new SMarketBuy();
		procotol.id = id;
		procotol.surplusnum = newSaleNumber;
		mkdb.Procedure.psendWhileCommit(buyRoleId, procotol);
		
		// 运营日志
		writeYYLogger(buyRoleId, saleRoleId, itemid, number, israrity, prices, money, itemtype, uniqId);
		
		// 通知关注的玩家删除此关注商品的引用
		String attentionKey = MarketUtils.genAttentionKey(itemtype, id);
		boolean delAttention = MarketUtils.notifyRoleRemoveAttention(attentionKey, AttentionType.ATTENTION_BUY);
		if (LOG.isInfoEnabled()) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(buyRoleId).append(", attentionId=").append(attentionKey).append(", 购买删除操作状态").append(delAttention);
			LOG.info(sbd.toString());
		}
		
		// 更改缓存状态
		if (chatBrowseCache.containsKey(id)) {
			ChatBrowseMarketItem chatBrowseMarketItem = chatBrowseCache.get(id);
			chatBrowseMarketItem.setState(1);
			
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(buyRoleId).append(", 购买道具id=").append(itemid).append(", 数据库id=").append(id).append(", 更改聊天展示拍卖道具缓存！");
				LOG.info(sbd.toString());
			}
		}		
		
		if (LOG.isInfoEnabled()) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(buyRoleId).append(", 购买saleId=").append(saleRoleId).append(", 道具id=").append(itemid).append(", 数据库id=").append(id).append(", 数量=").append(number).append(", 购买道具状态").append(succ);
			LOG.info(sbd.toString());
		}
		
		return succ;
	}

	/**
	 * 运营日志 - 交易
	 * @param buyRoleId 买家等级验证
	 * @return true可以购买, false不可以
	 */
	private boolean checkBuyLv(long buyRoleId) {
		// 玩家等级小于最小购买出售等级
		int marketLv = MarketUtils.getMarketLv();
		if (marketLv < 0) {
			return false;
		}
		int buyRoleLevel = xtable.Properties.selectLevel(buyRoleId);
		if (buyRoleLevel < marketLv) {
			LOG.error("购买道具错误,购买人id=" + buyRoleId + " 等级小于" + marketLv);
			return false;
		}
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			boolean ok = DayPayManager.getInstance().CheckTrade(buyRoleId, buyRoleLevel);
			if (ok == false)
			{		
				MessageMgr.psendMsgNotify(buyRoleId, 300005, null);	
				LOG.info("点卡服,你不能买,买人id=" + buyRoleId + "");
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 运营日志
	 * @param buyRoleId
	 * @param saleRoleId
	 * @param itemid
	 * @param number
	 * @param itemConf
	 * @param money
	 * @param itemtype
	 * @param logQuailty
	 * @param uniqId
	 */
	private void writeYYLogger(long buyRoleId, long saleRoleId, int itemid, int number, int israrity, List<Integer> prices, int money, int itemtype, long uniqId) {
		
		float fPricePer = 0;
		if(prices != null && prices.size() != 0 && prices.get(0) > 0){
			fPricePer = (float)money / prices.get(0);
		}

		float perPrice = money;
		if(number > 0){
			perPrice = (float)money / number;
		}
		
		OpTradExBean opTradExBean = new OpTradExBean(itemid, itemtype, getQuality(itemtype, itemid),
				israrity, uniqId, perPrice, number, MoneyType.MoneyType_GoldCoin, 
				money, MoneyType.MoneyType_SilverCoin, money, fPricePer);
		
		YYLogger.tradExLog(buyRoleId, saleRoleId, opTradExBean);
	}

	private int getQuality(int itemType, int goodId){
		int quailty = -1;
		if(itemType == GoodsType.PET){
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
	
	@Override
	public void marketBrowse(long roleId, int firstno, int twono, ArrayList<Integer> threeno, int itemtype,
			int limitmin, int limitmax, int browsetype, int currpage, int priceSort, int issearch) {
		SMarketBrowse browse = new SMarketBrowse();
		browse.browsetype = browsetype;
		browse.firstno = firstno;
		browse.twono = twono;
		browse.threeno = threeno;
		browse.limitmin = limitmin;
		browse.limitmax = limitmax;
		browse.itemtype = itemtype;
		browse.currpage = currpage;
		int totalRow = 0;
		int totalPage = 1;
		long currtime = System.currentTimeMillis();
		// 浏览普通道具
		if (GoodsType.NORMAL_ITEM == itemtype) {
			if (MarketUtils.isPayService()) {
				totalRow = NormalDAO.getInstance().queryNormalTotalRow(browsetype, firstno, twono, threeno, currtime);
				if (totalRow > 0) {
					Page page = new Page(totalRow, currpage, MarketUtils.PAGE_SIZE);
					// 价格升序降序由客户端指定
					List<MarketGoods> normals = NormalDAO.getInstance().queryNormal(browsetype, firstno, twono, threeno,
							currtime, page, priceSort);
					if (normals != null && normals.size() > 0) {
						browse.goodslist.addAll(normals);
					}
					totalPage = page.getTotalPage();
				}
			} else {
				Page page = new Page(8, 1, MarketUtils.PAGE_SIZE);
				// 价格升序
				List<MarketGoods> normals = NormalDAO.getInstance().queryNormal(browsetype, firstno, twono, threeno,
						currtime, page, 1);
				if (normals != null && normals.size() > 0) {
					browse.goodslist.addAll(normals);
				}
				totalPage = page.getTotalPage();
			}
		}
		// 浏览范围普通道具
		if (GoodsType.OTHER_ITEM == itemtype) {
			// 范围值小于等于0的情况是默认打开
			if (limitmin <= 0 && limitmax <= 0) {
				List<MarketGoods> normals = NormalDAO.getInstance().queryNormalOrderByPriceAndLeve(browsetype, firstno,
						twono, threeno, currtime);
				if (normals != null && normals.size() > 0) {
					MarketGoods marketGoods = normals.get(0);
					int level = marketGoods.level;
					int[] levelRange = MarketUtils.getLevelRange(level);
					limitmin = levelRange[0];
					limitmax = levelRange[1];
					browse.limitmin = limitmin;
					browse.limitmax = limitmax;
				}
			}
			if (MarketUtils.isPayService()) {
				if (limitmin > 0 && limitmax > 0) {
					totalRow = NormalDAO.getInstance().queryNormalTotalRow(browsetype, firstno, twono, threeno, limitmin,
							limitmax, currtime);
					if (totalRow > 0) {
						Page page = new Page(totalRow, currpage, MarketUtils.PAGE_SIZE);
						List<MarketGoods> normalRanges = NormalDAO.getInstance().queryNormal(browsetype, firstno, twono,
								threeno, limitmin, limitmax, currtime, page, priceSort);
						if (normalRanges != null && normalRanges.size() > 0) {
							browse.goodslist.addAll(normalRanges);
						}
						totalPage = page.getTotalPage();
					}
				}
			} else {
				/*
					select c.* from item_normal c inner join (
					select min(b.id) as id, a.level, a.price from (select level, min(price) as price from item_normal where firstno = 1 and twono = 2 and threeno = 3 and level >= 1 and level <= 10 group by level) a
inner join item_normal b on  firstno = 1 and twono = 2 and threeno = 3 and  a.level = b.level and a.price = b.price  group by a.level, a.price) d on c.id = d.id order by a.price, a.level desc
					
				 */
				if (limitmin > 0 && limitmax > 0) {
						List<MarketGoods> normalRanges = NormalDAO.getInstance().queryNormal(browsetype, firstno, twono,
								threeno, limitmin, limitmax, currtime);
						if (normalRanges != null && normalRanges.size() > 0) {
							browse.goodslist.addAll(normalRanges);
						}
				}
			}
		}
		// 浏览装备
		if (GoodsType.EQUIP_ITEM == itemtype) {
			totalRow = EquipDAO.getInstance().queryEquipTotalRow(browsetype, firstno, twono, threeno, currtime);
			if (totalRow > 0) {
				Page page = new Page(totalRow, currpage, MarketUtils.PAGE_SIZE);
				// 价格升序
				List<MarketGoods> equips = EquipDAO.getInstance().queryEquip(browsetype, firstno, twono, threeno,
						currtime, page, priceSort);
				if (equips != null && equips.size() > 0) {
					browse.goodslist.addAll(equips);
				}
				totalPage = page.getTotalPage();
			}
		}
		// 浏览宠物价格升序
		if (GoodsType.PET == itemtype) {
			totalRow = PetDAO.getInstance().queryPetTotalRow(browsetype, firstno, twono, threeno, currtime);
			if (totalRow > 0) {
				Page page = new Page(totalRow, currpage, MarketUtils.PAGE_SIZE);
				// 价格升序
				List<MarketGoods> pets = PetDAO.getInstance().queryPet(browsetype, firstno, twono, threeno, currtime,
						page, priceSort);
				if (pets != null && pets.size() > 0) {
					browse.goodslist.addAll(pets);
				}
				totalPage = page.getTotalPage();
			}
		}

		browse.totalpage = totalPage;
		browse.pricesort = priceSort;

		mkdb.Procedure.psendWhileCommit(roleId, browse);
		
		// 运营日志
		switch (issearch) {
		case 0:
			writeYYLogger(roleId, firstno, twono);
			break;
		case 1:
			if(threeno.size() > 0){
				int goodid = threeno.get(0);
				MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(goodid);
				if(itemConf !=null)
					writeYYLogger(roleId, itemConf.getId());
			}
			break;
		default:
			LOG.error("MarketManager.marketBrowse.YYLogger错误issearch提示");
			break;
		}
	}

	/**
	 * 运营日志 - 筛选
	 * @param roleId
	 * @param firstno
	 * @param twono
	 */
	private void writeYYLogger(long roleId, int firstno, int twono) {
		YYLogger.aucFilLog(roleId, firstno, twono);
	}

	/**
	 * <b>事务内调用</b> 发送玩家拍卖容器中的道具信息
	 * 
	 * @param roleId
	 */
	public void sendMarketContainerByRoleId(long roleId) {
		int itemId = 0;
		long uniqueId = 0;
		int itemKey = 0;
		int itemType = 0;
		int itemNum = 0;
		long now = System.currentTimeMillis();

		ItemMaps itemcol = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, false);
		PetColumn petcol = new PetColumn(roleId, PetColumnTypes.MARKETPET, false);

		ArrayList<MarketGoods> goodslist = new ArrayList<>();
		MarketGoods marketGoods = null;
		for (ItemBase item : itemcol) {
			if (item instanceof EquipItem) {
				EquipItem equipItem = (EquipItem) item;
				itemId = equipItem.getItemId();
				uniqueId = equipItem.getUniqId();
				itemKey = equipItem.getKey();
				itemNum = equipItem.getNumber();
				itemType = GoodsType.EQUIP_ITEM;// 这里细化类型是因为h2中是不同的表,当日志输出时能定位是哪个具体逻辑
				marketGoods = EquipDAO.getInstance().queryEquip(roleId, uniqueId, itemId, itemKey);
			} else {
				itemId = item.getItemId();
				uniqueId = item.getUniqId();
				itemKey = item.getKey();
				itemNum = item.getNumber();
				itemType = GoodsType.NORMAL_ITEM;
				marketGoods = NormalDAO.getInstance().queryNormal(roleId, uniqueId, itemId, itemKey);
			}
			if (marketGoods == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId)
				.append(", 道具id=").append(itemId)
				.append(", 道具类型：").append(itemType)
				.append(", 道具KEY：").append(itemKey)
				.append(", 道具唯一id：").append(uniqueId)
				.append(", 拍卖容器中的道具信息数据不匹配！");
				LOG.error(sbd.toString());
				continue;
			}
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId)
				.append(", 拍卖中道具容器[数据库id=").append(marketGoods.id)
				.append(", 道具id=").append(itemId)
				.append(", 道具数量：").append(itemNum)
				.append(", 公示时间：").append(marketGoods.showtime)
				.append(", 到期时间：").append(marketGoods.expiretime)
				.append(", 当前时间：").append(now);
				LOG.info(sbd.toString());
			}
			marketGoods.itemtype = GoodsType.NORMAL_ITEM;
			marketGoods.num = itemNum;
			goodslist.add(marketGoods);
		}
		List<Pet> pets = petcol.getPets();
		for (Pet pet : pets) {
			itemId = pet.getBaseId();
			uniqueId = pet.getUniqueId();
			itemKey = pet.getPetkey();
			itemNum = 1;
			itemType = GoodsType.PET;
			marketGoods = PetDAO.getInstance().queryPet(roleId, uniqueId, itemId, itemKey);
			if (marketGoods == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId)
				.append(", 道具id=").append(itemId)
				.append(", 道具类型：").append(itemType)
				.append(", 道具KEY：").append(itemKey)
				.append(", 道具唯一id：").append(uniqueId)
				.append(", 拍卖容器中的道具信息数据不匹配！");
				LOG.error(sbd.toString());
				continue;
			}
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId)
				.append(", 拍卖中道具容器[数据库id=").append(marketGoods.id)
				.append(", 道具id=").append(itemId)
				.append(", 道具数量：").append(itemNum)
				.append(", 公示时间：").append(marketGoods.showtime)
				.append(", 到期时间：").append(marketGoods.expiretime)
				.append(", 当前时间：").append(now);
				LOG.info(sbd.toString());
			}
			marketGoods.itemtype = itemType;
			marketGoods.num = itemNum;
			goodslist.add(marketGoods);
		}

		SMarketContainerBrowse browse = new SMarketContainerBrowse();
		browse.actiontype = 3;
		browse.goodslist = goodslist;
		mkdb.Procedure.psendWhileCommit(roleId, browse);
	}

	/**
	 * 上架前获得价格
	 * 
	 * @param roleId
	 * @param containerType
	 * @param key
	 */
	public void getPrice(long roleId, int containerType, int key) {
		int itemId = 0;
		int level = 0;
		if (containerType == MarketUtils.CONTAINER_BAG) {
			ItemMaps bagContainer = Module.getInstance().getItemMaps(roleId, BagTypes.BAG, true);
			ItemBase item = bagContainer.getItem(key);
			if (item == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 获取上架价格, 背包中未找到该道具！");
				LOG.error(sbd.toString());
				return;
			}
			itemId = item.getItemId();
			level = MarketUtils.getItemLevel(item);
		} else if (containerType == MarketUtils.CONTAINER_PET) {
			PetColumn petContainer = new PetColumn(roleId, PetColumnTypes.PET, true);
			Pet pet = petContainer.getPet(key);
			if (pet == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 获取上架价格, 宠物栏中未找到该宠物！");
				LOG.error(sbd.toString());
				return;
			}
			itemId = pet.getBaseId();
			level = pet.getLevel();
		} else if (containerType == MarketUtils.CONTAINER_MARKET_PET) {
			PetColumn marketPetContainer = new PetColumn(roleId, PetColumnTypes.MARKETPET, true);
			Pet pet = marketPetContainer.getPet(key);
			if (pet == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 获取上架价格, 拍卖宠物栏中未找到该宠物！");
				LOG.error(sbd.toString());
				return;
			}
			itemId = pet.getBaseId();
			level = pet.getLevel();
		} else if (containerType == MarketUtils.CONTAINER_MARKET_BAG) {
			ItemMaps marketBagContainer = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, true);
			ItemBase item = marketBagContainer.getItem(key);
			if (item == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 获取上架价格, 拍卖背包中未找到该道具！");
				LOG.error(sbd.toString());
				return;
			}
			itemId = item.getItemId();
			level = MarketUtils.getItemLevel(item);
		}
		// 未获得道具id
		if (itemId == 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 获取上架价格, 未获得道具id！");
			LOG.error(sbd.toString());
			return;
		}
		int price = FloatingMarketManager.getInstance().getPrice(itemId, level);
		if (price < 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 获取上架道具id=").append(itemId).append("价格=").append(price);
			LOG.error(sbd.toString());
		}

		// 返回协议
		SGetMarketUpPrice snd = new SGetMarketUpPrice();
		snd.containertype = containerType;
		snd.price = price;
		snd.stallprice = computeStallPrice(price);

		gnet.link.Onlines.getInstance().send(roleId, snd);
	}

	@Override
	public boolean remarketUp(int itemType, long roleId, long id, int newPrice) {
		
		if (newPrice <= 0 || newPrice > Integer.MAX_VALUE) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 重新上架道具, 数据库id=").append(id).append(", 新的价格=").append(newPrice).append(", 重新上架接收到价格出现非法数值！");
			LOG.error(sbd.toString());
			return false;
		}
		
		int oldPrice = 0;
		int level = 0;
		int itemId = 0;
		// 验证数据库中是否存在
		switch (itemType) {
		case GoodsType.NORMAL_ITEM:
			NormalDaoBean normalBean = NormalDAO.getInstance().queryNormal(id);
			if (null == normalBean) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 重新上架道具, 数据库id=").append(id).append(", 新的价格=").append(newPrice).append(", 数据库中不存在此道具！");
				LOG.error(sbd.toString());
				return false;
			}
			ItemMaps itemColumn = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, true);
			ItemBase basicItem = itemColumn.getItem(normalBean.getKey());
			if (null == basicItem) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 重新上架道具, 数据库id=").append(id).append(", 新的价格=").append(newPrice).append(", 拍卖容器中不存在此道具！");
				LOG.error(sbd.toString());
				return false;
			}
			oldPrice = normalBean.getPrice();
			level = normalBean.getLevel();
			itemId = normalBean.getItemid();
			break;
		case GoodsType.EQUIP_ITEM:
			EquipDaoBean equipBean = EquipDAO.getInstance().queryEquip(id);
			if (null == equipBean) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 重新上架道具, 数据库id=").append(id).append(", 新的价格=").append(newPrice).append(", 数据库中不存在此道具！");
				LOG.error(sbd.toString());
				return false;
			}
			itemColumn = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, true);
			basicItem = itemColumn.getItem(equipBean.getKey());
			if (null == basicItem) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 重新上架道具, 数据库id=").append(id).append(", 新的价格=").append(newPrice).append(", 拍卖容器中不存在此道具！");
				LOG.error(sbd.toString());
				return false;
			}
			oldPrice = equipBean.getPrice();
			level = equipBean.getLevel();
			itemId = equipBean.getItemid();
			break;
		case GoodsType.PET:
			PetDaoBean petBean = PetDAO.getInstance().queryPet(id);
			if (null == petBean) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 重新上架道具, 数据库id=").append(id).append(", 新的价格=").append(newPrice).append(", 数据库中不存在此道具！");
				LOG.error(sbd.toString());
				return false;
			}
			PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.MARKETPET, true);
			PetInfo petInfo = petColumn.getPetInfo(petBean.getKey());
			if (null == petInfo) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 重新上架道具, 数据库id=").append(id).append(", 新的价格=").append(newPrice).append(", 拍卖容器中不存在此道具！");
				LOG.error(sbd.toString());
				return false;
			}
			oldPrice = petBean.getPrice();
			level = petBean.getLevel();
			itemId = petBean.getItemid();
			break;
		default:
			return false;
		}
		
		// 是否有变化
		boolean isMoneyChange = ( oldPrice == newPrice ? true : false );
		
		// 计算摊位费
		int stallPrice = this.computeStallPrice( ( isMoneyChange == true ? oldPrice : newPrice ) );
		if (stallPrice <= 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 重新上架道具, 数据库id=").append(id).append(", 摊位费=").append(stallPrice).append(", 摊位费计算后不合法！");
			LOG.error(sbd.toString());
			return false;
		}

		// 扣除摊位费
		if (!this.costMoney(roleId, stallPrice)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 重新上架道具, 数据库id=").append(id).append(", 摊位费=").append(stallPrice).append(", 扣除摊位费失败！");
			LOG.error(sbd.toString());
			return false;
		}

		// 更新数据
		MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(itemId);
		long newExpireTime = 0;
		if (itemConf.israrity == MarketUtils.RARITY && !MarketUtils.isPayService()) {
			newExpireTime = MarketUtils.getRarityExpireTime();
		} else {
			newExpireTime = MarketUtils.getNotRarityExpireTime();
		}

		// 更新标志
		boolean succ = false;
		Table table = null;
		switch (itemType) {
		case GoodsType.NORMAL_ITEM:
			table = Table.ITEM_NORMAL;
			break;
		case GoodsType.EQUIP_ITEM:
			table = Table.ITEM_EQUIP;
			break;
		case GoodsType.PET:
			table = Table.ITEM_PET;
			break;
		}
		
		if (table == null)
			return false;
		
		succ = MarketStore.updateExpireTime(table, id, newExpireTime);
		// 普通服,价格改变,是珍品,更新过期时间成功,才修改公示时间
		if (!isMoneyChange && succ && itemConf.israrity == MarketUtils.RARITY && !MarketUtils.isPayService()) {
			succ = MarketStore.updateShowTime(table, id, MarketUtils.getShowTime());
		}
		// 更新价格
		if (!isMoneyChange && succ) {
			succ = MarketStore.updatePrice(table, id, newPrice);
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 重新上架道具id=").append(itemId).append(", 数据库id=").append(id).append(", 更新价格操作状态：").append(succ);
				LOG.info(sbd.toString());
			}
		}
		// 关注数量清零
		if (succ) {
			succ = MarketStore.updateAttentionNumber(table, id, 0);
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 重新上架道具id=").append(itemId).append(", 数据库id=").append(id).append(", 更新关注数量清零操作状态：").append(succ);
				LOG.info(sbd.toString());
			}
		}

		if (!succ) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 重新上架道具id=").append(itemId).append(", 数据库id=").append(id).append(", 关注数量清零, 数据库更新错误！");
			LOG.error(sbd.toString());
			return false;
		}

		// 减少挂单量
		FloatingMarketManager.getInstance().hangOrderNumDec(itemId, level);
		// 增加挂单数
		FloatingMarketManager.getInstance().hangOrderNumInc(itemId, level);
		
		// 更改缓存状态
		if (chatBrowseCache.containsKey(id)) {
			ChatBrowseMarketItem chatBrowseMarketItem = chatBrowseCache.get(id);
			chatBrowseMarketItem.setQuery(1);
			
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 重新上架id=").append(itemId).append(", 数据库id=").append(id).append(", 更改聊天展示拍卖道具缓存！");
				LOG.info(sbd.toString());
			}
		}

		return succ;
	}
	
	@Override
	public boolean attention(int itemType, int okOrCancel, long roleId, long id, int buyOrPublicity) {
		DaoBean db = null;
		Table table = null;
		switch (itemType) {
		case GoodsType.NORMAL_ITEM:
			db = NormalDAO.getInstance().queryNormal(id);
			table = Table.ITEM_NORMAL;
			break;
		case GoodsType.EQUIP_ITEM:
			db = EquipDAO.getInstance().queryEquip(id);
			table = Table.ITEM_EQUIP;
			break;
		case GoodsType.PET:
			db = PetDAO.getInstance().queryPet(id);
			table = Table.ITEM_PET;
			break;
		}
		if (null == db) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 关注").append(buyOrPublicity).append("物品, 数据库id=").append(id).append(", 数据库中不存在此道具！");
			LOG.error(sbd.toString());
			return false;
		}
		String attentionKey = MarketUtils.genAttentionKey(itemType, id);
		// 只有关注才检查
		if (AttentionType.ATTENTION_OK == okOrCancel) {
			if (!MarketUtils.checkRoleAttentionNumber(roleId, buyOrPublicity)) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 关注").append(buyOrPublicity).append(", 物品attentionKey=").append(attentionKey).append("失败, 角色关注数量已到上限！");
				LOG.error(sbd.toString());
				return false;
			}
		}
		// 检查道具被关注的上限
		int newNumber = db.getAttentionnumber();
		newNumber = MarketUtils.checkAttionNumber(okOrCancel, newNumber);
		if (newNumber < 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 关注").append(buyOrPublicity).append(", 物品attentionKey=").append(attentionKey).append("失败, 道具被关注数量已到上限！");
			LOG.error(sbd.toString());
			return false;
		}
		// 道具被哪些角色关注
		if (!MarketUtils.goodsAttentionRoleXdb(okOrCancel, attentionKey, roleId)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 关注").append(buyOrPublicity).append(", 添加或取消").append(okOrCancel).append(", 物品attentionKey=").append(attentionKey).append("道具被哪些角色关注失败！");
			LOG.error(sbd.toString());
			return false;
		}
		// 角色关注的添加和取消
		long showTime = db.getShowtime(), expireTime = db.getExpiretime();
		if (!MarketUtils.roleAttentionGoodsXdb(okOrCancel, attentionKey, showTime, expireTime, roleId, buyOrPublicity)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 关注").append(buyOrPublicity).append(", 添加或取消").append(okOrCancel).append(", 物品attentionKey=").append(attentionKey).append("角色关注的添加和取消失败！");
			LOG.error(sbd.toString());
			return false;
		}
		// 更新关注数量
		if (!MarketStore.updateAttentionNumber(table, id, newNumber)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 关注").append(buyOrPublicity).append(", 添加或取消").append(okOrCancel).append(", 物品attentionKey=").append(attentionKey).append("更新关注数量失败！");
			LOG.error(sbd.toString());
			return false;
		}
		
		// 运营日志
		writeYYLogger(itemType, okOrCancel, roleId, db);
		return true;
	}

	/**
	 * 运营日志 - 关注
	 * @param itemType
	 * @param okOrCancel
	 * @param roleId
	 * @param db
	 */
	private void writeYYLogger(int itemType, int okOrCancel, long roleId, DaoBean db) {
		MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(db.getItemid());
		if(itemConf == null ) return;
		
		int attentionNumber =  MarketUtils.getRoleAttentionNumber(roleId);
		OpAucConBean opAucConBean = new OpAucConBean(db.getFirstno(), db.getTwono(), db.getItemid(), 
				itemType, getQuality(itemType, db.getItemid()), itemConf.getIsrarity(), db.getId(), okOrCancel, attentionNumber);
		
		YYLogger.aucConLog(roleId, opAucConBean);
	}
	
	/**
	 * 关注道具浏览
	 * @param roleId
	 * @param attenType 关注状态 1购买，2公示
	 * @return 返回玩家关注的道具信息(道具过期不推送并解除关注)
	 */
	public boolean attentionBrowse(long roleId, int attenType) {
		xbean.AttentionGoods attentionGoods = xtable.Marketattentiongoodstab.get(roleId);
		if (attentionGoods == null) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 未关注任何商品！");
			LOG.error(sbd.toString());
			return false;
		}
		
		ArrayList<MarketGoods> goodslist = new ArrayList<>(8);
		// 筛选关注
		long currTime = System.currentTimeMillis();
		List<xbean.AttentionGoodsBean> attentionGoodsBeans = null;
		
		if (attenType == AttentionType.ATTENTION_BUY) {// 购买关注
			attentionGoodsBeans = attentionGoods.getBuyattentions();
		} else if (attenType == AttentionType.ATTENTION_PUBLICITY) {// 公示关注
			attentionGoodsBeans = attentionGoods.getPublicityattentions();
		}
		
		if (attentionGoodsBeans != null) {
			Iterator<xbean.AttentionGoodsBean> it = attentionGoodsBeans.iterator();
			while (it.hasNext()) {
				xbean.AttentionGoodsBean attentionGoodsBean = it.next();
				String attentionKey = attentionGoodsBean.getId();
				MarketGoods goods = this.attentionMarketGoods(attentionKey);
				
				long showTime = attentionGoodsBean.getShowtime();
				long expireTime = attentionGoodsBean.getExpiretime();
				
				if (attenType == AttentionType.ATTENTION_BUY) {
					if (currTime >= expireTime) {
						it.remove();
						if (goods == null) {
							xtable.Marketattentionroletab.remove(attentionKey);// 道具关联的角色表
						}
						continue;
					}
				} else if (attenType == AttentionType.ATTENTION_PUBLICITY) {
					if (currTime >= showTime && currTime < expireTime) {// 将关注对象从公示列表中移到购买列表中
						List<xbean.AttentionGoodsBean> buyAttentions = attentionGoods.getBuyattentions();
						buyAttentions.add(attentionGoodsBean.copy());
						it.remove();
						continue;
					} else if (currTime >= expireTime) {
						it.remove();
						if (goods == null) {
							xtable.Marketattentionroletab.remove(attentionKey);// 道具关联的角色表
						}
						continue;
					}
				}
				
				// 如果道具已不存在, 删除关注
				if (goods == null) {
					it.remove();
					xtable.Marketattentionroletab.remove(attentionKey);// 道具关联的角色表
					continue;
				}
				goodslist.add(goods);
			}
		}
		
		// 发送
		SMarketContainerBrowse browse = new SMarketContainerBrowse();
		browse.actiontype = attenType;
		browse.goodslist = goodslist;
		mkdb.Procedure.psendWhileCommit(roleId, browse);
		
		return true;
	}
	
	/**
	 * @param attentionKey
	 * @return 创建查询出来的关注对象转换发给前端的对象
	 */
	protected MarketGoods attentionMarketGoods(String attentionKey) {
		MarketGoods marketGoods = null;
		String[] keys = attentionKey.split("\\-");
		int itemType = Integer.parseInt(keys[0]);
		long key = Long.parseLong(keys[1]);
		
		switch(itemType) {
			case GoodsType.NORMAL_ITEM:
				xbean.NormalItem ni = MkdbStore.selectNormal(key);
				if (null == ni)
					return null;
				marketGoods = new MarketGoods();
				marketGoods.id = key;
				marketGoods.saleroleid = ni.getRoleid();
				marketGoods.itemid = ni.getItemid();
				marketGoods.num = ni.getNumber();
				marketGoods.key = ni.getKey();
				marketGoods.price = ni.getPrice();
				marketGoods.showtime = ni.getShowtime();
				marketGoods.expiretime = ni.getExpiretime();
				if (ni.getItemid() > 0) {
					MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(ni.getItemid());
					int type = itemConf.getItemtype();
					if (type == GoodsType.PET) {
						marketGoods.itemtype = GoodsType.PET;
					} else {
						marketGoods.itemtype = GoodsType.NORMAL_ITEM;
					}
				}
				marketGoods.level = ni.getLevel();
				marketGoods.attentionnumber = ni.getAttentionnumber();
				break;
			case GoodsType.EQUIP_ITEM:
				xbean.EquipItem ei = MkdbStore.selectEquip(key);
				if (null == ei)
					return null;
				marketGoods = new MarketGoods();
				marketGoods.id = key;
				marketGoods.saleroleid = ei.getRoleid();
				marketGoods.itemid = ei.getItemid();
				marketGoods.num = ei.getNumber();
				marketGoods.key = ei.getKey();
				marketGoods.price = ei.getPrice();
				marketGoods.showtime = ei.getShowtime();
				marketGoods.expiretime = ei.getExpiretime();
				if (ei.getItemid() > 0) {
					MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(ei.getItemid());
					int type = itemConf.getItemtype();
					if (type == GoodsType.PET) {
						marketGoods.itemtype = GoodsType.PET;
					} else {
						marketGoods.itemtype = GoodsType.NORMAL_ITEM;
					}
				}
				marketGoods.level = ei.getLevel();
				marketGoods.attentionnumber = ei.getAttentionnumber();
				break;
			case GoodsType.PET:
				xbean.PetItem pi = MkdbStore.selectPet(key);
				if (null == pi)
					return null;
				marketGoods = new MarketGoods();
				marketGoods.id = key;
				marketGoods.saleroleid = pi.getRoleid();
				marketGoods.itemid = pi.getItemid();
				marketGoods.num = pi.getNumber();
				marketGoods.key = pi.getKey();
				marketGoods.price = pi.getPrice();
				marketGoods.showtime = pi.getShowtime();
				marketGoods.expiretime = pi.getExpiretime();
				if (pi.getItemid() > 0) {
					MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(pi.getItemid());
					int type = itemConf.getItemtype();
					if (type == GoodsType.PET) {
						marketGoods.itemtype = GoodsType.PET;
					} else {
						marketGoods.itemtype = GoodsType.NORMAL_ITEM;
					}
				}
				marketGoods.level = pi.getLevel();
				marketGoods.attentionnumber = pi.getAttentionnumber();
				break;
		}
		
		return marketGoods;
	}
	
	/**
	 * 拍卖上架时收取1%的摊位费
	 * 最低1000银币
	 * 最高100000银币
	 * 
	 * @param price 上架金额
	 * @return 返回摊位费
	 */
	protected int computeStallPrice(int price) {
		int tmp = (int) (price * 100 * 0.01);// 金币 * 100(金币和银币是1:100) * 1%=摊位费
		int stallPrice = tmp < 1000 ? 1000 : (tmp > 100000 ? 100000 : tmp);
		return stallPrice;
	}

	/**
	 * 扣除税费
	 * 
	 * @money 要扣税的金额
	 */
	protected int costTax(int money) {
		Double tax = MarketUtils.getTax();
		if (money <= 0 || tax == -1) {
			LOG.error("错误的金额无法扣除税率!");
			return 0;
		}
		return money -= ((int) (money * tax));
	}

	/**
	 * 检查上架道具是否还有空位置
	 * 
	 * @return true可上架
	 */
	protected boolean checkMarketSize(long roleId) {
		ItemMaps itemcol = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, true);
		PetColumn petcol = new PetColumn(roleId, PetColumnTypes.MARKETPET, true);
		return itemcol.size() + petcol.size() >= MarketUtils.MARKET_CONTAINER_LIMIT ? false : true;
	}

	/**
	 * 检查上架道具是否可上架
	 * 
	 * @param itemId 道具id
	 * @return true可上架
	 */
	protected boolean checkMarketUp(int itemId) {
		return fire.pb.shop.Module.marketThreeTableMap.containsKey(itemId) && fire.pb.shop.Module.marketThreeTableMap.get(itemId).getCanstall() == 1;
	}

	@Override
	public boolean searchEquip(long roleId, int euqipType, int level, int priceMin, int priceMax, int effect, int skill,
			List<Integer> colors, List<fire.pb.shop.MarketSearchAttr> basicAttrs,
			List<Integer> additionalAttrs, int totalAttr, int score, int sellState) {
		
		Object[] results = MarketStore.searchEquip(euqipType, level, priceMin, priceMax, effect, skill, colors,
				basicAttrs, additionalAttrs, totalAttr, score, sellState);
		
		int firstNo = 0;
		int twoNo = 0;
		int threeNo = 0;
		int currpage = 1;
		int totalpage = 1;
		ArrayList<MarketGoods> marketGoods = null;
		
		if (results != null) {
			firstNo = (int) results[0];
			twoNo = (int) results[1];
			threeNo = (int) results[2];
			marketGoods = (ArrayList<MarketGoods>) results[3];
			if (marketGoods != null && marketGoods.size() > 0) {
				Page page = new Page(marketGoods.size(), currpage, MarketUtils.PAGE_SIZE);
				totalpage = page.getTotalPage();
				
				SMarketSearchResult searchResult = new SMarketSearchResult();
				searchResult.browsetype = sellState;
				searchResult.firstno = firstNo;
				searchResult.twono = twoNo;
				searchResult.currpage = currpage;
				searchResult.totalpage = totalpage;
				searchResult.goodslist = marketGoods;
				
				mkdb.Procedure.psendWhileCommit(roleId, searchResult);
			}
		}
		
		// 运营日志
		writeYYLogger(roleId, euqipType);
		
		return true;
	}

	/**
	 * 运营日志 - 搜索
	 * @param roleId
	 * @param marketGoods
	 */
	private void writeYYLogger(long roleId, int itemId) {
		YYLogger.aucSeacLog(roleId, itemId);
	}
	
	@Override
	public boolean searchPet(long roleId, int petType, int levelMin, int levelMax, int priceMin, int priceMax,
			List<MarketSearchAttr> talents, List<Integer> skills, int skillNumber, List<MarketSearchAttr> attrs,
			int score, int sellState) {

		Object[] results = MarketStore.searchPet(petType, levelMin, levelMax, priceMin, priceMax,
				talents, skills, skillNumber, attrs, score, sellState);
		
		int firstNo = 0;
		int twoNo = 0;
		int threeNo = 0;
		int currpage = 1;
		int totalpage = 1;
		ArrayList<MarketGoods> marketGoods = null;
		
		if (results != null) {
			firstNo = (int) results[0];
			twoNo = (int) results[1];
			threeNo = (int) results[2];
			marketGoods = (ArrayList<MarketGoods>) results[3];
			if (marketGoods != null && marketGoods.size() > 0) {
				Page page = new Page(marketGoods.size(), currpage, MarketUtils.PAGE_SIZE);
				totalpage = page.getTotalPage();
				
				SMarketSearchResult searchResult = new SMarketSearchResult();
				searchResult.browsetype = sellState;
				searchResult.firstno = firstNo;
				searchResult.twono = twoNo;
				searchResult.currpage = currpage;
				searchResult.totalpage = totalpage;
				searchResult.goodslist = marketGoods;
				
				mkdb.Procedure.psendWhileCommit(roleId, searchResult);
			}
		}
		
		writeYYLogger(roleId, petType);
		return true;
	}
	
	@Override
	public void chatBrowseMarketItem(long roleId, long id, int itemType) {
		
		Table table = null;
		switch (itemType) {
		case GoodsType.NORMAL_ITEM:
			table = Table.ITEM_NORMAL;
			break;
		case GoodsType.EQUIP_ITEM:
			table = Table.ITEM_EQUIP;
			break;
		case GoodsType.PET:
			table = Table.ITEM_PET;
			break;
		case GoodsType.OTHER_ITEM:
			table = Table.ITEM_NORMAL;
			break;
		}
		// 如果缓存中有直接返回
		if (chatBrowseCache.containsKey(id)) {
			ChatBrowseMarketItem chatBrowseMarketItem = chatBrowseCache.get(id);
			if (chatBrowseMarketItem.getState() == 1) {// 无效
				MessageMgr.sendMsgNotify(roleId, 160187, null);
				return;
			}
			long showTime = chatBrowseMarketItem.getShowTime();
			long expireTime = chatBrowseMarketItem.getExpireTime();
			int showType = chatBrowseMarketItem.getShowType();
			long now = System.currentTimeMillis();
			if (showTime > 0 && now > showTime && showType == 2) {
				chatBrowseMarketItem.setQuery(1);
			}
			
			if (expireTime > 0 && now > expireTime) {
				chatBrowseMarketItem.setQuery(1);
			}
			
			// 数据无变化
			if (chatBrowseMarketItem.getQuery() == 0) {
				mkdb.Procedure.psendWhileCommit(roleId, chatBrowseMarketItem.getChatBrowse());
				StringBuilder sbd = new StringBuilder();
				sbd.append("查询显示道具").append(id).append(" , 缓存命中").append(chatBrowseMarketItem.getCacheCount()).append("!");
				LOG.error(sbd);
				return;
			}
		}
		
		Object[] results = MarketDAO.getInstance().queryTotalRowAndRowNum(table, id, itemType);
		if (results == null) {
			MessageMgr.sendMsgNotify(roleId, 180016, null);
			return;
		}
		
		int browseType = (int) results[0];
		int firstNo = (int) results[1];
		int twoNo = (int) results[2];
		int threeNo = (int) results[3];
		int totalRow = (int) results[4];
		int indexRow = (int) results[5];
		int level = (int) results[6];
		int[] range = MarketUtils.getLevelRange(level);
		int limitmin = range[0];
		int limitmax = range[1];
		long now = System.currentTimeMillis();
		ArrayList<Integer> threeNos = new ArrayList<Integer>() {
			private static final long serialVersionUID = 1L;
			{
				add(threeNo);
			}
		};
		
		double currentPage = (double)indexRow / (double)MarketUtils.PAGE_SIZE;

		Page page = new Page(totalRow, ((int) Math.ceil(currentPage)), MarketUtils.PAGE_SIZE);
		
		List<MarketGoods> browses = null;
		
		switch (itemType) {
		case GoodsType.NORMAL_ITEM:
			browses = NormalDAO.getInstance().queryNormal(browseType, firstNo, twoNo, threeNos, now, page, 1);
			break;
		case GoodsType.EQUIP_ITEM:
			browses = EquipDAO.getInstance().queryEquip(browseType, firstNo, twoNo, threeNos, now, page, 1);
			break;
		case GoodsType.PET:
			browses = PetDAO.getInstance().queryPet(browseType, firstNo, twoNo, threeNos, now, page, 1);
			break;
		case GoodsType.OTHER_ITEM:
			browses = NormalDAO.getInstance().queryNormal(browseType, firstNo, twoNo, threeNos, limitmin, limitmax, now, page, 1);
			break;
		}
		
		SMarketItemChatShow chatBrowse = new SMarketItemChatShow();
		chatBrowse.browsetype = browseType;
		chatBrowse.firstno = firstNo;
		chatBrowse.twono = twoNo;
		chatBrowse.threeno = threeNos;
		chatBrowse.id = id;
		chatBrowse.currpage = page.getCurrPage();
		chatBrowse.totalpage = page.getTotalPage();
		chatBrowse.goodslist.addAll(browses);
		
		long showTime = 0L;
		long expireTime = 0L;
		int showType = 0;
		// 如果缓存中没有添加
		if (!chatBrowseCache.containsKey(id)) {
			if (browses != null && browses.size() > 0) {
				for (MarketGoods marketGoods : browses) {
					if (marketGoods.id != id) {
						continue;
					}
					showTime = marketGoods.showtime;
					expireTime = marketGoods.expiretime;
					showType = browseType;
					break;
				}
			}
			chatBrowseCache.put(id, new ChatBrowseMarketItem(chatBrowse, showTime, expireTime, showType));
		} else {// 如果已经存在更新缓存
			ChatBrowseMarketItem chatBrowseMarketItem = chatBrowseCache.get(id);
			showTime = chatBrowseMarketItem.getShowTime();
			expireTime = chatBrowseMarketItem.getExpireTime();
			showType = chatBrowseMarketItem.getShowType();
			chatBrowseCache.put(id, new ChatBrowseMarketItem(chatBrowse, showTime, expireTime, showType));
		}
		
		mkdb.Procedure.psendWhileCommit(roleId, chatBrowse);
	}

	/**
	 * 获取上架道具数量
	 * @return true可上架
	 */
	protected int getMarketSize(long roleId) {
		ItemMaps itemcol = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, true);
		PetColumn petcol = new PetColumn(roleId, PetColumnTypes.MARKETPET, true);
		return itemcol.size() + petcol.size();
	}
	
	/////////////////////////////////////////
	// 定时执行购买, 随机抽取
	private boolean scheduleBuy(long id, long saleroleid, int itemid, int money) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("创建新的定时器");
		}
		ScheduledFuture<?> future = mkdb.Executor.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				new mkdb.Procedure() {
					@Override
					protected boolean process() throws Exception {
						if (LOG.isInfoEnabled()) {
							StringBuilder sbd = new StringBuilder();
							sbd.append("售卖道具role=").append(saleroleid).append(", 售卖道具 id=").append(itemid).append(", 数据库id=").append(id).append(", 定时器执行！");
							LOG.info(sbd.toString());
						}
						xbean.PresellItem presellItem = xtable.Preselltab.get(id);
						List<Long> roles = presellItem.getRoleid();
						List<Long> rolesTemp = new ArrayList<>();
						rolesTemp.addAll(roles);
						/////////////////////////////////////////////////////
						//锁住参与购买的角色
//						lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roles));
						Random r = new Random();
						int idx = r.nextInt(rolesTemp.size());
						long buyroleid = rolesTemp.get(idx);
						///////////////////////////////////
						//如果金币和信用值不符合，并且还有玩家没有选择到
						while(!MarketUtils.checkMoneyAndCredit(buyroleid, money) && rolesTemp.size() > 0) {
							idx = r.nextInt(rolesTemp.size());
							buyroleid = rolesTemp.remove(idx);
						}
						// 删除自己(群发消息时不提醒自己)
						roles.remove(idx);
						
						fire.pb.shop.srv.market.IMarket market = fire.pb.shop.srv.market.MarketManager.getInstance();
						fire.pb.shop.srv.market.proxy.MarketTransactionProxy handler = new fire.pb.shop.srv.market.proxy.MarketTransactionProxy(market);
						fire.pb.shop.srv.market.IMarket proxy = (fire.pb.shop.srv.market.IMarket) handler.getProxy();
						boolean success = proxy.normalBuy(buyroleid, id, saleroleid, itemid, 1);
						
						if (LOG.isInfoEnabled()) {
							StringBuilder sbd = new StringBuilder();
							sbd.append("售卖道具role=").append(saleroleid).append(", 售卖道具 id=").append(itemid).append(", 数据库id=").append(id).append(", 定时器执行完成状态：").append(success);
							LOG.info(sbd.toString());
						}
						
//						if (success) {
							// 刷新前台数据
//							fire.pb.shop.srv.market.MarketManager.getInstance().sendMarketContainerByRoleId(buyroleid);
//						}
						
						// 好友系统消息提示：
						for (Long roleId : roles) {
							MessageMgr.sendSystemMessageToRole(roleId, 190055, null);
						}
						
						// 删除预售道具
						xtable.Preselltab.remove(id);
						return success;
					}
					
				}.submit();
			}
		}, MarketUtils.getPresellTime() + 1000, TimeUnit.MILLISECONDS);
		
		return true;
	}
	
	/**
	 * <b>事务内调用</b> 发送玩家拍卖合服后容器中道具信息
	 * 
	 * @param roleId
	 */
	public void sendTempMarketContainer(long roleId) {
		int itemId = 0;
		int itemKey = 0;
		int itemType = 0;
		int itemNum = 0;
		int itemLevel = 0;

		ItemMaps itemcol = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, true);
		PetColumn petcol = new PetColumn(roleId, PetColumnTypes.MARKETPET, true);

		ArrayList<TempMarketContainerGoods> goodslist = new ArrayList<>(8);
		TempMarketContainerGoods marketGoods = null;
		for (ItemBase item : itemcol) {
			marketGoods = new TempMarketContainerGoods();
			if (item instanceof EquipItem) {
				EquipItem equipItem = (EquipItem) item;
				itemId = equipItem.getItemId();
				itemKey = equipItem.getKey();
				itemNum = equipItem.getNumber();
				itemType = GoodsType.EQUIP_ITEM;
				itemLevel = equipItem.getItemAttr().getLevel();
			} else {
				itemId = item.getItemId();
				itemKey = item.getKey();
				itemNum = item.getNumber();
				itemType = GoodsType.NORMAL_ITEM;
				itemLevel = MarketUtils.getItemLevel(item);
			}
			marketGoods.itemid = itemId;
			marketGoods.num = itemNum;
			marketGoods.key = itemKey;
			marketGoods.itemtype = itemType;
			marketGoods.level = itemLevel;
			goodslist.add(marketGoods);
			
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId)
				.append(", 拍卖临时容器").append(marketGoods.toString());
				LOG.info(sbd.toString());
			}
		}
		List<Pet> pets = petcol.getPets();
		for (Pet pet : pets) {
			marketGoods = new TempMarketContainerGoods();
			itemId = pet.getBaseId();
			itemKey = pet.getPetkey();
			itemNum = 1;
			itemType = GoodsType.PET;
			itemLevel = pet.getLevel();
			
			marketGoods.itemid = itemId;
			marketGoods.num = itemNum;
			marketGoods.key = itemKey;
			marketGoods.itemtype = itemType;
			marketGoods.level = itemLevel;
			goodslist.add(marketGoods);
			
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId)
				.append(", 拍卖临时容器").append(marketGoods.toString());
				LOG.info(sbd.toString());
			}
		}
		
		// 没有道具
		if (goodslist.size() == 0) {
			// 记录取过的角色
			boolean succ = xtable.Takebackmarketcontainertab.add(roleId, 1);
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId).append(", 没有道具直接标志为取过物品, 操作状态").append(succ);
				LOG.info(sbd.toString());
			}
		}

		STempMarketContainer browse = new STempMarketContainer();
		browse.goodslist = goodslist;
		mkdb.Procedure.psendWhileCommit(roleId, browse);
	}

	
	/**
	 * <b>事务内调用</b> 玩家拍卖合服后容器中取回道具
	 * 
	 * @param roleId/
	 */
	public boolean TempTakeBackMarketContainer(long roleId) {
		
		int itemId = 0;
		int key = 0;
		int number = 0;
		int level = 0;
		
		ItemMaps itemcol = Module.getInstance().getItemMaps(roleId, BagTypes.MARKET, true);
		
		for (ItemBase item : itemcol) {
			itemId = item.getItemId();
			key = item.getKey();
			number = item.getNumber();
			level = item.getItemAttr().getLevel();
			
			item = this.moveMarketItemToBag(roleId, key, number);
			if (item == null) {
				MessageMgr.psendMsgNotify(roleId, 150147, null);
				return false;
			}
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId)
				.append(", 取出拍卖临时容器[").append("itemId=")
				.append(itemId).append(", key=").append(key)
				.append(", number=").append(number).append(", level=").append(level).append("]");
				LOG.info(sbd.toString());
			}
		}
		
		PetColumn petcol = new PetColumn(roleId, PetColumnTypes.MARKETPET, true);
		
		List<Pet> pets = petcol.getPets();
		for (Pet pet : pets) {
			itemId = pet.getBaseId();
			key = pet.getPetkey();
			level = pet.getLevel();
			
			pet = this.moveMarketToPet(roleId, key);
			if (pet == null) {
				MessageMgr.psendMsgNotify(roleId, 150147, null);
				return false;
			}
			
			if (LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleId)
				.append(", 取出拍卖临时容器[").append("itemId=")
				.append(itemId).append(", key=").append(key)
				.append(", number=").append(1).append(", level=").append(level).append("]");
				LOG.info(sbd.toString());
			}
		}
		
		// 记录取过的角色
		boolean succ = xtable.Takebackmarketcontainertab.add(roleId, 1);
		if (!succ) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 取出拍卖临时容器道具, 操作状态").append(succ);
			LOG.error(sbd.toString());
			mkdb.Procedure.psend(roleId, new STakeBackTempMarketContainerItem(0));
		}
		
		mkdb.Procedure.psend(roleId, new STakeBackTempMarketContainerItem(1));
		
		return succ;
	}
	
}
