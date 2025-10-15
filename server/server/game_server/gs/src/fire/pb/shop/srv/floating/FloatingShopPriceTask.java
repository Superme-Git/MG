package fire.pb.shop.srv.floating;

import java.util.List;

import fire.pb.shop.srv.Shop;
import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.srv.market.floating.FloatingMarketManager;
import fire.pb.shop.utils.MarketUtils;
import mkdb.Procedure;

/**
 * 
 * 商店价格浮动定时统计
 * 
 * @author liangyanpeng
 * 
 */
public class FloatingShopPriceTask implements Runnable {
	
	private long intervalTime = 5 * 60 * 1000;//间隔时间
	private long nextRefreshTime = 0;//下一次重新加载时间
	private long nextMarketStatisticsTime = 0;//下一次执行拍卖浮动价格统计时间
	
	public FloatingShopPriceTask() {
		super();
		if (Shop.LOG.isDebugEnabled()) {
			Shop.LOG.debug("价格浮动定时器启动");
		}
		long currTime = System.currentTimeMillis();
		nextMarketStatisticsTime = currTime + MarketUtils.getFloatingPriceTime();
	}

	@Override
	public void run() {
		long currTime = System.currentTimeMillis();
		try {
			// reload 
			if (currTime >= nextRefreshTime) {
				if (Shop.LOG.isDebugEnabled()) {
					Shop.LOG.debug("定时执行统计浮动价格商店");
				}
				nextRefreshTime = currTime + intervalTime;
				FileterFloatingShop.getInstance().refreshNpcSale();
				//过滤一遍看看是不是有以前统计的商店,但现在不统计了的数据,删除掉
				List<Long> noFloatingShopIds = FileterFloatingShop.getInstance().getNoFloatingShopIds();
				new Procedure(){
					@Override
					protected boolean process() throws Exception {
						FloatingOneManager.getInstance().removeStatisticsShopData(noFloatingShopIds);
						return true;
					}
				};
			}
		} catch (Exception e) {
			Shop.LOG.error("重新加载统计价格浮动商店", e);
		}
		
		// 调用统计成交量
		try {
			List<Long> floatingOneShopIds = FileterFloatingShop.getInstance().getFloatingOneShopIds();
			FloatingOneManager.getInstance().new StatisticsOne(floatingOneShopIds, currTime).submit();
		} catch (Exception e) {
			Shop.LOG.error("调用商会价格浮动统计功能出错", e);
		}
		
		
		// 调用拍卖价格浮动统计
		try {
			if (currTime >= nextMarketStatisticsTime) {
				if (Shop.LOG.isDebugEnabled()) {
					Shop.LOG.debug("定时执行拍卖价格浮动统计");
				}
				nextMarketStatisticsTime = currTime + MarketUtils.getFloatingPriceTime();
				FloatingMarketManager.getInstance().new StatisticsMarket().submit();
			}
		} catch (Exception e) {
			MarketManager.LOG.error("调用拍卖价格浮动统计功能出错", e);
		}
	}

}
