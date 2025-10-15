package fire.pb.shop.srv.market.floating;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.main.ConfigManager;
import fire.pb.shop.GoodsType;
import fire.pb.shop.MarketThreeTable;
import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.utils.MarketUtils;
import mkdb.Procedure;
import xbean.MarketFloatingGoods;
import xtable.Marketfloatingtab;

/**
 * 
 * 拍卖价格浮动管理
 * 
 * @author liangyanpeng
 *
 */
public class FloatingMarketManager {
	
	private static class FloatingMarketManagerHolder {
		static FloatingMarketManager instance = new FloatingMarketManager();
	}
	
	public static FloatingMarketManager getInstance() {
		return FloatingMarketManagerHolder.instance;
	}
	
	/**
	 * 拍卖中的商品统计key
	 */
	private List<String> keys = new ArrayList<>();
	
	/**
	 * 获得物品建议价格
	 * @param itemId 道具id
	 * @param level 等级或品质
	 * @return 正常返回大于0的价格,错误和珍品都返回-1.
	 */
	public int getPrice(int itemId, int level) {
		
		MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(itemId);
		
		if (itemConf == null) {
			MarketManager.LOG.error("道具id = " + itemId + " 未找到对应的配置对象,非可售卖物品或表填错!");
			return -1;
		}
		
		// 珍品不建议价格
		if (itemConf.israrity == MarketUtils.RARITY) {
			return -1;
		}
		
		String key = Integer.toString(itemId);
		int itemType = itemConf.getItemtype();
		
		// 道具类型GoodsType.OTHER_ITEM要做特殊处理
		if (itemType == GoodsType.OTHER_ITEM) {
			level = MarketUtils.retRangeByLevel(level);
			key += Integer.toString(level);
		}
		
		// 从xdb中获取价格
		MarketFloatingGoods value = Marketfloatingtab.select(key);
		if (value == null) {
			MarketManager.LOG.error("道具id = " + itemId + " 未配置价格此物品, 可能是珍品或表填错!");
			return -1;
		}
		// 价格
		int price = value.getPrice();
		
		return price;
	}
	
	/**
	 * 增加挂单量
	 * @param itemId
	 * @param level 品质或者等级
	 */
	public void hangOrderNumInc(int itemId, int level) {
		this.IncAndDec(true, itemId, level);
	}
	
	/**
	 * 减少挂单量
	 * @param itemId
	 * @param level 品质或者等级
	 */
	public void hangOrderNumDec(int itemId, int level) {
		this.IncAndDec(false, itemId, level);
	}
	
	/**
	 * 挂单量操作
	 * @param isInc 增加true,减少false
	 */
	private void IncAndDec(boolean isInc, int itemId, int level) {
		MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(itemId);
		if (itemConf.getIsrarity() == MarketUtils.RARITY)// 珍品不处理
			return;
		String key = Integer.toString(itemId);
		if (itemConf.getItemtype() == GoodsType.OTHER_ITEM) {
			key += Integer.toString(level);
		}
		MarketFloatingGoods value = Marketfloatingtab.get(key);
		if (value == null) {
			MarketManager.LOG.error("key=" + key + ",有错误不应该为空,有可能是后添加的新商品没有初始化！");
			return;
		}
		if (isInc)
			value.setHangordernum(value.getHangordernum() + 1);// 增加挂单量
		else
			value.setHangordernum(value.getHangordernum() - 1);// 增加挂单量
	}
	
	/**
	 * 增加售卖总金额和售卖数量
	 */
	public void totalMoneyAndSaleNumInc(int itemId, int level, int money, int number) {
		MarketThreeTable itemConf = fire.pb.shop.Module.marketThreeTableMap.get(itemId);
		if (itemConf.getIsrarity() == MarketUtils.RARITY)// 珍品不处理
			return;
		String key = Integer.toString(itemId);
		if (itemConf.getItemtype() == GoodsType.OTHER_ITEM) {
			key += Integer.toString(level);
		}
		MarketFloatingGoods value = Marketfloatingtab.get(key);
		if (value == null) {
			MarketManager.LOG.error("有错误不应该为空,有可能是后添加的新商品没有初始化！");
			return;
		}
		value.setTotalmoney(value.getTotalmoney() + money);// 增加售卖总金额
		value.setSalenum(value.getSalenum() + number);// 增加售卖数量
	}
	
	
	/**
	 * 初始化拍卖商品统计数据
	 * @see 注意：要在Procedure中使用
	 */
	public void initStatisticsMarketData() {
		if (fire.pb.shop.Module.marketThreeTableMap == null || fire.pb.shop.Module.marketThreeTableMap.isEmpty()) {
			MarketManager.LOG.error("拍卖三级表数据为空!");
			return;
		}
		for (MarketThreeTable m : fire.pb.shop.Module.marketThreeTableMap.values()) {
			int isRarity = m.getIsrarity();
			int itemId = m.getId();
			int basehangordernum = m.get挂单量();//基准挂单量
			int basesalenum = m.get成交量();//基准成交量
			float floatingmin = (float) m.getFloatingmin();//价格下限
			float floatingmax = (float) m.getFloatingmax();//价格上限
			float floatingprice = (float) m.getFloatingprice();//浮动价格系数
			//珍品不统计
			if (isRarity == MarketUtils.RARITY) {
				continue;
			}
			String key = Integer.toString(itemId);
			// 特殊物品
			if (m.getItemtype() == GoodsType.OTHER_ITEM) {
				List<Integer> prices = m.getPrices();
				List<Integer> ranges = m.getRanges();
				for (int i = 0; i < prices.size(); i++) {
					int price = prices.get(i);// 价格
					String range = Integer.toString(MarketUtils.retRangeByLevel(ranges.get(i))); // 区间
					String tmpKey = key + range;//道具id+区间=唯一的key
					this.keys.add(tmpKey);// 添加key
					initXdb(tmpKey, price, basehangordernum, basesalenum, floatingmin, floatingmax, floatingprice);// 初始化商品
				}
				continue;
			}
			// 正常道具
			List<Integer> prices = m.getPrices();
			if (prices == null || prices.size() == 0) {
				MarketManager.LOG.error("严重错误, 拍卖三级表数据价格为空道具id=" + itemId);
				continue;
			}
			this.keys.add(key);
			Integer price = prices.get(0);
			this.initXdb(key, price, basehangordernum, basesalenum, floatingmin, floatingmax, floatingprice);// 初始化商品
		}
	}
	
	private void initXdb(String key, int price, int basehangordernum, int basesalenum, float floatingmin, float floatingmax, float floatingprice) {
		MarketFloatingGoods value = Marketfloatingtab.get(key);
		if (value != null) {
//			MarketManager.LOG.debug("拍卖中商品id=" + key + "已经初始化过!价格=" + value.getPrice());
			value.setBasehangordernum(basehangordernum);//基准挂单量
			value.setBasesalenum(basesalenum);//基准成交量
			value.setFloatingmin(price * floatingmin);//价格下限
			value.setFloatingmax(price * floatingmax);//价格上限
			value.setFloatingprice(floatingprice);//浮动价格系数
			return;
		}
//		if (MarketManager.LOG.isDebugEnabled()) {
//			MarketManager.LOG.debug("新增拍卖中商品id=" + key + "初始化!价格=" + price);
//		}
		value = xbean.Pod.newMarketFloatingGoods();
		value.setPriorperiodprice(price);//初始化上期价格
		value.setPrice(price);//初始化为配表价格
		value.setBasehangordernum(basehangordernum);//基准挂单量
		value.setBasesalenum(basesalenum);//基准成交量
		value.setFloatingmin(price * floatingmin);//价格下限
		value.setFloatingmax(price * floatingmax);//价格上限
		value.setFloatingprice(floatingprice);//浮动价格系数
		Marketfloatingtab.insert(key, value);
	}
	
	
	/**
	 * 
	 * 拍卖价格浮动统计
	 * 
	 * @author liangyanpeng
	 *
	 */
	public class StatisticsMarket extends Procedure {

		@Override
		protected boolean process() throws Exception {
			for (String key : keys) {
				MarketFloatingGoods value = Marketfloatingtab.get(key);
				if (value == null) {
					MarketManager.LOG.error("严重错误, 出现不匹配数据！有key, 无初始化的拍卖商品数据key=" + key);
					continue;
				}
				this.statisticsMarketData(value);
			}
			return true;
		}
		
		private void statisticsMarketData(MarketFloatingGoods value) {
			int totalMoney = value.getTotalmoney();// 本期售卖总金额
			int saleNum = value.getSalenum();//当期成交量
			int baseSaleNum = value.getBasesalenum();//基准成交量
			int baseHangOrderNum = value.getBasehangordernum();//基准挂单量
			int hangOrderNum = value.getHangordernum();//挂单量
			float floatingmin = value.getFloatingmin();//价格下限
			float floatingmax = value.getFloatingmax();//价格上限
			float floatingprice = value.getFloatingprice();//浮动价格系数
			
			int newPrice = 0;
			/*
			 *  当期成交量 >= 基准成交量
			 *  刷新价格=当期平均价格
			 */
			if (saleNum > 0 && saleNum >= baseSaleNum) {
				newPrice = totalMoney / saleNum;
			}
			else {
				/*
				 *  当期成交量    < 基准成交量
				 *  挂单量 < 基准挂单量     ,  刷新价格=上期价格 * (1 + 系统浮动系数)
				 *  挂单量 >= 基准挂单量     ,  刷新价格=上期价格 * (1 - 系统浮动系数)
				 */
				if (hangOrderNum < baseHangOrderNum) {
					newPrice = (int) (value.getPriorperiodprice() * (1 + floatingprice));
				}
				else {
					newPrice = (int) (value.getPriorperiodprice() * (1 - floatingprice));
				}
			}
			if (newPrice <= 0) {
				newPrice = value.getPrice();//保持当前价格
			} else {
				if (newPrice < floatingmin) {// 价格下限保护
					newPrice = (int) floatingmin;
				}
				if (newPrice > floatingmax) {// 价格上限保护
					newPrice = (int) floatingmax;
				}
			}
			value.setPriorperiodprice(value.getPrice());//重置上期价格
			value.setPrice(newPrice);//重置新的价格
			value.setSalenum(0);//重置当期成交量
			value.setTotalmoney(0);//重置总售卖金额
		}
		
	}

}
