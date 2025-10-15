package fire.pb.shop.srv.floating;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.npc.NpcManager;
import fire.pb.shop.SGoods;
import fire.pb.shop.SNpcSale;
import fire.pb.shop.srv.Shop;
import fire.pb.shop.utils.FloatingType;
import mkdb.Procedure;

/**
 * 
 * 商品价格浮动类型one管理
 * 
 * @see 这里所有和XDB操作相关的函数都要在Procedure中调用
 * 
 * @author liangyanpeng
 *
 */
public class FloatingOneManager {
	
	/**
	 * 通用配置常量表
	 */
	public Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);
	/**
	 * 商品表
	 */
	public Map<Integer, SGoods> goodsMap = fire.pb.shop.Module.sGoodsMap;

	private FloatingOneManager() {}
	
	public static class FlotingManagerHolder {
		static FloatingOneManager instance = new FloatingOneManager();
	}
	
	public static FloatingOneManager getInstance() {
		return FlotingManagerHolder.instance;
	}
	
	/**
	 * @param shopId
	 * @return 返回商店的商品和价格
	 * @see 注意：要在Procedure中使用
	 */
	public ArrayList<fire.pb.shop.Goods> getGoodsAndPrice(final long shopId) {
		xbean.NpcSaleBusiness npcSaleBusiness = xtable.Npcsalebusinesstab.get(shopId);
		if (npcSaleBusiness == null)
			return null;
		
		Map<Integer, xbean.GoodsBusiness> goodsBusinessMap = npcSaleBusiness.getGoodsmap();
		if (goodsBusinessMap.isEmpty())
			return null;
		
		//遍历商店,取出商品id和价格
		ArrayList<fire.pb.shop.Goods> goodsList= new ArrayList<>();
		for (Map.Entry<Integer, xbean.GoodsBusiness> entry : goodsBusinessMap.entrySet()) {
			int goodsid = entry.getKey();
			xbean.GoodsBusiness goodsBusiness = entry.getValue();
			int priorPeriodPrice = goodsBusiness.getPriorperiodprice();// 上一次价格
			int price = goodsBusiness.getPrice();// 当前价格
			if (price <= 0) {//价格保护
				Shop.LOG.error("从浮动商店中获取商品价格异常：shopId=" + shopId + ", goodsid=" + goodsid + "价格异常,价格为=" + price);
				SGoods goods = goodsMap.get(goodsid);
				price = goods.getPrices().get(0);//商品价格
			}
			fire.pb.shop.Goods goods = new fire.pb.shop.Goods();
			goods.goodsid = goodsid;
			goods.price = price;
			goods.priorperiodprice = priorPeriodPrice;
			goodsList.add(goods);
		}
		return goodsList;
	}
	
	/**
	 * 买商品记录
	 * @param shopId
	 * @param goodsid
	 * @param num 购买数量
	 * @see 注意：要在Procedure中使用
	 */
	public void buyGoodsRecord(final long shopId, final int goodsid, final long num) {
		if (num == 0)
			return;
		xbean.NpcSaleBusiness npcSaleBusiness = xtable.Npcsalebusinesstab.get(shopId);
		if (npcSaleBusiness == null)
			return;
		xbean.GoodsBusiness goodsBusiness = npcSaleBusiness.getGoodsmap().get(goodsid);
		if (goodsBusiness == null)
			return;
		long buyNum = goodsBusiness.getBuynum();
		goodsBusiness.setBuynum(buyNum + num);//增加购买数量
		short isActivate = npcSaleBusiness.getIsactive();
		if (isActivate == FloatingType.NO_ACTIVATE)//激活统计
			npcSaleBusiness.setIsactive(FloatingType.ACTIVATE);
	}
	
	
	/**
	 * 卖商品记录
	 * @param shopId
	 * @param goodsid
	 * @param num 售卖数量
	 * @see 注意：要在Procedure中使用
	 */
	public void saleGoodsRecord(final long shopId, final int goodsid, final long num) {
		if (num == 0)
			return;
		xbean.NpcSaleBusiness npcSaleBusiness = xtable.Npcsalebusinesstab.get(shopId);
		if (npcSaleBusiness == null)
			return;
		xbean.GoodsBusiness goodsBusiness = npcSaleBusiness.getGoodsmap().get(goodsid);
		if (goodsBusiness == null)
			return;
		long saleNum = goodsBusiness.getSalenum();
		goodsBusiness.setSalenum(saleNum + num);//增加售卖数量
//		short isActivate = npcSaleBusiness.getIsactive();
//		if (isActivate == FloatingType.NO_ACTIVATE)//激活统计
//			npcSaleBusiness.setIsactive(FloatingType.ACTIVATE);
	}
	
	/**
	 * 当前价格
	 * @param shopId
	 * @param goodsid
	 * @return 获取失败返回-1,注意对返回值的处理
	 * @see 注意：要在Procedure中使用
	 */
	public int getCurrPrice(final long shopId, final int goodsid) {
		xbean.NpcSaleBusiness npcSaleBusiness = xtable.Npcsalebusinesstab.get(shopId);
		if (npcSaleBusiness == null)
			return -1;
		xbean.GoodsBusiness goodsBusiness = npcSaleBusiness.getGoodsmap().get(goodsid);
		int currPrice = goodsBusiness.getPrice();
		if (currPrice > 0)
			return currPrice;
		Shop.LOG.error("从浮动商店中获取商品价格异常：shopId=" + shopId + ", goodsid=" + goodsid + "价格异常,价格为=" + currPrice);
		return -1;
	}
	
	/**
	 * @return 返回X周期值,获取失败返回-1
	 */
	private int getX() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(140);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		Shop.LOG.error("获取统计周期常量x错误");
		return -1;
	}
	
	/**
	 * @return 返回Y影响价格的值,获取失败返回-1
	 */
	private int getY() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(141);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		Shop.LOG.error("获取统计系数常量y错误");
		return -1;
	}
	
	/**
	 * 删除商店商品统计数据
	 * @param shopIds
	 * @see 注意：要在Procedure中使用
	 */
	public void removeStatisticsShopData(java.util.List<Long> shopIds) {
		for (Long shopId : shopIds) {
			xbean.NpcSaleBusiness npcSaleBusiness = xtable.Npcsalebusinesstab.get(shopId);
			if (npcSaleBusiness == null)
				continue;
			xtable.Npcsalebusinesstab.remove(shopId);
		}
	}
	
	/**
	 * 初始化商店商品统计数据
	 * @param shopId 配置表中的主键
	 * @see 注意：要在Procedure中使用
	 */
	public void initStatisticsShopData(Long shopId) {
		xbean.NpcSaleBusiness npcSaleBusiness = xtable.Npcsalebusinesstab.get(shopId);
		SNpcSale npcSale = NpcManager.getInstance().getNpcSale(shopId.intValue());
		ArrayList<Integer> goodsIds = npcSale.getGoodsids();
		
		if (npcSaleBusiness != null) {
			Map<Integer, xbean.GoodsBusiness> initGoodsMap = npcSaleBusiness.getGoodsmap();
			for (Integer goodsId : goodsIds) {
				if (!initGoodsMap.containsKey(goodsId)) {
					initMkdbGoodsData(goodsId, npcSaleBusiness);
					Shop.LOG.info("已初始化过的npc商店商品, 商店id=" + shopId + ", 新增并初始化商品=" + goodsId);
				}
			}
			return;
		}
		npcSaleBusiness = xbean.Pod.newNpcSaleBusiness();
		xtable.Npcsalebusinesstab.insert(shopId, npcSaleBusiness);
		
		for (Integer goodsId : goodsIds) {//商品
			initMkdbGoodsData(goodsId, npcSaleBusiness);
		}
	}
	
	protected void initMkdbGoodsData(Integer goodsId, xbean.NpcSaleBusiness npcSaleBusiness) {
		SGoods goods = goodsMap.get(goodsId);
		int price = goods.getPrices().get(0);//商品价格
		Map<Integer, xbean.GoodsBusiness> initGoodsMap = npcSaleBusiness.getGoodsmap();
		xbean.GoodsBusiness goodsBusiness = xbean.Pod.newGoodsBusiness();
		goodsBusiness.setPriorperiodprice(price);//初始化时上一次价格和当前价格一样
		goodsBusiness.setPrice(price);
		initGoodsMap.put(goodsId, goodsBusiness);
	}

	/**
	 * 浮动规则算法one类型算法
	 * 
	 * @author liangyanpeng
	 *
	 */
	public class StatisticsOne extends Procedure{

		private List<Long> shopIds;
		private long currTime;
		public StatisticsOne(List<Long> shopIds, long currTime) {
			super();
			this.shopIds = shopIds;
			this.currTime = currTime;
		}

		@Override
		protected boolean process() throws Exception {
			for (Long shopId : shopIds) {
				xbean.NpcSaleBusiness npcSaleBusiness = xtable.Npcsalebusinesstab.get(shopId);
				if (npcSaleBusiness == null) {//新增商店
					initStatisticsShopData(shopId);
					continue;
				}
				// 开始统计
				this.statisticsShopData(npcSaleBusiness, shopId);
			}
			return true;
		}
		
		/**
		 * 商店商品统计
		 * @param npcSaleBusiness
		 * @param shopId
		 * @return
		 */
		private boolean statisticsShopData(xbean.NpcSaleBusiness npcSaleBusiness, Long shopId) {
			SNpcSale npcSale = NpcManager.getInstance().getNpcSale(shopId.intValue());
			long intervalTime =/* 50000;*/npcSale.floatingtime * 1000;//配置的统计时间
			long time = npcSaleBusiness.getTime();//上一次统计时间
			int isActivate = npcSale.floating;//是否统计,默认为0不启动,1是启动
			if (currTime - time < intervalTime || isActivate == FloatingType.NO_ACTIVATE) {//未到统计时间或未开启统计
				return false;
			}
			npcSaleBusiness.setTime(currTime);//设置新的统计时间
			//遍历商店中的商品,计算新的价格
			Map<Integer, xbean.GoodsBusiness> goodsBusinessMap = npcSaleBusiness.getGoodsmap();
			for (Map.Entry<Integer, xbean.GoodsBusiness> entry : goodsBusinessMap.entrySet()) {
				int goodsid = entry.getKey();
				xbean.GoodsBusiness goodsBusiness = entry.getValue();
				int count = goodsBusiness.getCount();//成交量小于等于0的统计次数
				long buyNum = goodsBusiness.getBuynum();
				long saleNum = goodsBusiness.getSalenum();
				long result = buyNum - saleNum;//本期成交量
				long lastNum = goodsBusiness.getLastnum();//上期成交量
				int currPrice = goodsBusiness.getPrice();//当前价格
				int newPrice = 0;//新价格
				boolean isChange = false; //价格是否有变化
				
				SGoods goods = FloatingOneManager.getInstance().goodsMap.get(goodsid);
				if (goods == null) {
					StringBuilder sbd = new StringBuilder();
					sbd.append("价格浮动统计, 此道具已被策划删除所以不参与价格浮动统计.商品id=").append(goodsid);
					Shop.LOG.error(sbd.toString());
					continue;
				}
				int price = goods.getPrices().get(0);//原价
				int floatingRisePrice = (int) goods.getFloatingRisePrice();//价格上涨浮动
				int floatingDepreciatePrice = (int) goods.getFloatingDepreciatePrice();//价格下降浮动
				double floatingPriceMax = goods.getFloatingmax();//价格上限系数
				double floatingPriceMin = goods.getFloatingmin();//价格下限系数
				//成交量大于零
				if (result > 0) {
					if (result > lastNum) {
						//刷新价格=min(默认价格*价格上限系数,当前价格+浮动价格)
						double a = java.lang.Math.ceil(price * floatingPriceMax);//向上取整
						int b = currPrice + floatingRisePrice;
						newPrice = (int) java.lang.Math.min(a, b);
						if (newPrice <= 0) {
							Shop.LOG.error("价格浮动one计算时,商品价格向下溢出,出现价格小于或等于0的新价格,shopId=" + shopId + "商品id=" + goodsid);
							continue;
						}
						isChange = true;
					}
					if (result < lastNum) {
						//刷新价格=max(默认价格*价格下限系数,当前价格-浮动价格)
						double a = java.lang.Math.ceil(price * floatingPriceMin);//向上取整
						int b = currPrice - floatingDepreciatePrice;
						newPrice = (int) java.lang.Math.max(a, b);
						if (newPrice <= 0) {
							Shop.LOG.error("价格浮动one计算时,商品价格向下溢出,出现价格小于或等于0的新价格,shopId=" + shopId + "商品id=" + goodsid);
							continue;
						}
						isChange = true;
					}
					//不连续所以重置
					goodsBusiness.setCount(0);//重置成交量小于等于0的统计次数
				}
				//成交量小于等于零
				if (result <= 0) {
					int x = getX();//x
					if (x == -1) {
						return false;
					}
					goodsBusiness.setCount(count+1);
					count = goodsBusiness.getCount();
					if ((count) >= x) {
						//连续x次所以要降价并重置
						//刷新价格=当前价格-浮动价格*y
						goodsBusiness.setCount(0);//重置成交量小于等于0的统计次数
						int y = getY();//y
						if (y == -1) {
							return false;
						}
						double a = java.lang.Math.ceil(price * floatingPriceMin);//向上取整
						int b = currPrice - floatingDepreciatePrice * y;//公式计算产生新的价格
						newPrice = (int) java.lang.Math.max(a, b);
						if (newPrice <= 0) {
							Shop.LOG.error("价格浮动one计算时,商品价格向下溢出,出现价格小于或等于0的新价格,shopId=" + shopId + "商品id=" + goodsid);
							continue;
						}
						isChange = true;
					}
				}
				
				if (isChange) {
					goodsBusiness.setPriorperiodprice(currPrice);//设置上一次价格
					goodsBusiness.setPrice(newPrice);//设置新的价格
				} else {
					goodsBusiness.setPriorperiodprice(currPrice);//设置上一次价格
					goodsBusiness.setPrice(currPrice);//设置新的价格
				}
				
				// 重置本期购买的数量、本期售卖的数量、上期成交量
				goodsBusiness.setBuynum(0L);
				goodsBusiness.setSalenum(0L);
				goodsBusiness.setLastnum(result);
			}
			return true;
		}
		
	}

	
}
