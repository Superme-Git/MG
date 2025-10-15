package fire.pb.shop;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import fire.pb.shop.srv.Shop;
import fire.pb.shop.srv.floating.FileterFloatingShop;
import fire.pb.shop.srv.floating.FloatingOneManager;
import fire.pb.shop.srv.floating.FloatingShopPriceTask;
import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.srv.market.floating.FloatingMarketManager;
import fire.pb.shop.srv.market.jdbc.MarketDAO;
import fire.pb.shop.utils.MarketUtils;
import mkdb.Procedure;

public class Module implements ModuleInterface {
	
	/**
	 * 三级拍卖配置表 key=itemid value=三级表中的道具id对应的数据
	 */
	public static Map<Integer, MarketThreeTable> marketThreeTableMap = null;
	public static Map<Integer, SGoods> sGoodsMap = null;
	public static Map<Integer, SMallShop> sMallShopMap = null;
	public static Map<Integer, SNpcSale> sNpcSaleMap = null;
	public static Map<Integer, MarketFirstTable> marketFirstTableMap = null;
	
	static {
		if (!MarketUtils.isPayService()) {
			marketThreeTableMap = ConfigManager.getInstance().getConf(MarketThreeTable.class);
			sGoodsMap = ConfigManager.getInstance().getConf(SGoods.class);
			sMallShopMap = ConfigManager.getInstance().getConf(SMallShop.class);
			sNpcSaleMap = ConfigManager.getInstance().getConf(SNpcSale.class);
			marketFirstTableMap = ConfigManager.getInstance().getConf(MarketFirstTable.class);
		} else {
			Map<Integer, DMarketThreeTable> dMarketThreeTableMap = ConfigManager.getInstance().getConf(DMarketThreeTable.class);
			marketThreeTableMap = new TreeMap<>(dMarketThreeTableMap);
			
			Map<Integer, DSGoods> dSGoodsMap = ConfigManager.getInstance().getConf(DSGoods.class);
			sGoodsMap = new TreeMap<>(dSGoodsMap);

			Map<Integer, DSMallShop> dSMallShopMap = ConfigManager.getInstance().getConf(DSMallShop.class);
			sMallShopMap = new TreeMap<>(dSMallShopMap);

			Map<Integer, DSNpcSale> dSNpcSaleMap = ConfigManager.getInstance().getConf(DSNpcSale.class);
			sNpcSaleMap = new TreeMap<>(dSNpcSaleMap);

			Map<Integer, DMarketFirstTable> dMarketFirstTableMap = ConfigManager.getInstance().getConf(DMarketFirstTable.class);
			marketFirstTableMap = new TreeMap<>(dMarketFirstTableMap);
		}
		
	}
	
	@Override
	public void exit() {
	}

	@Override
	public void init() throws Exception {
		
		// 创建H2表
		MarketDAO.getInstance().createTable();
		// 加载H2数据
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				return MarketDAO.getInstance().initializationDataFromXdb();
			}
		}.submit();
		
		
		// 初始化商店配置数据
		if (Shop.LOG.isDebugEnabled()) {
			Shop.LOG.debug("shop价格统计模块加载开始");
		}
		mkdb.Procedure.execute(new InitStatisticsOneData(), new InitStatisticsOneDone());
		
		// 初始化摆摊配置数据
		if (MarketManager.LOG.isDebugEnabled()) {
			MarketManager.LOG.debug("摆摊价格统计模块加载开始");
		}
		mkdb.Procedure.execute(new InitStatisticsMarketData(), new InitStatisticsMarketDone());
		
	}
	
	class InitStatisticsOneDone implements Procedure.Done<InitStatisticsOneData> {
		@Override
		public void doDone(InitStatisticsOneData p) {
			Shop.LOG.debug("shop价格统计模块加载完成");
		}
	}
	
	class InitStatisticsMarketDone implements Procedure.Done<InitStatisticsMarketData> {
		@Override
		public void doDone(InitStatisticsMarketData p) {
			MarketManager.LOG.debug("摆摊价格统计模块加载完成");
			// 摆摊配置表初始化完, 才启动定时器, 要不然会有并发修改的异常.
			//(加载配置表正在往FloatingMarketManager.keys数组中增加key, 而这时候定时器已经启动开始对keys进行遍历了,所以会造成并发修改的异常.)
			mkdb.Executor exctor = mkdb.Executor.getInstance();
			exctor.getScheduledTimeoutExecutor().setDefaultTimeout(0);
			exctor.scheduleAtFixedRate(new FloatingShopPriceTask(), 1, 5, TimeUnit.SECONDS);
		}
	}

	@Override
	public ReloadResult reload() throws Exception {
		return null;
	}
	
	
	/**
	 * 新服开服
	 * 初始化统计数据
	 * 将所有商品价格设置为配置中的价格
	 * 只初始一次
	 */
	private class InitStatisticsOneData extends Procedure {
		@Override
		protected boolean process() throws Exception {
			List<Long> oneShopIds = FileterFloatingShop.getInstance().getFloatingOneShopIds();
			for (Long shopId : oneShopIds) {//商店
				FloatingOneManager.getInstance().initStatisticsShopData(shopId);
			}
			return true;
		}
		
	}
	
	/**
	 * 新服开服
	 * 初始化摆摊统计数据
	 * 将所有商品价格设置为配置中的价格
	 * 只初始一次
	 */
	private class InitStatisticsMarketData extends Procedure {
		@Override
		protected boolean process() throws Exception {
			FloatingMarketManager.getInstance().initStatisticsMarketData();
			return true;
		}
		
	}

}
