package fire.pb.shop;

import java.util.List;
import mkdb.Procedure;

public class PQueryLimit extends Procedure {
	
	private final long roleid;
	private final int querytype;
	private final List<Integer> goodsids;
	
	public PQueryLimit(final long roleid, final int querytype, final List<Integer> goodsids) {
		this.roleid = roleid;
		this.querytype = querytype;
		this.goodsids = goodsids;
	}

	@Override
	protected boolean process() throws Exception {
		java.util.Map<java.lang.Integer, fire.pb.shop.SGoods> goodsMap = fire.pb.shop.Module.sGoodsMap;
		java.util.List<fire.pb.shop.SGoods> tmpGoodsList = new java.util.ArrayList<>();
//		if (goodsids != null) {
			for (Integer goodsId : goodsids) {
				if (!goodsMap.containsKey(goodsId))//验证商品是否存在
					continue;
				fire.pb.shop.SGoods goods = goodsMap.get(goodsId);
				if (goodsMap.get(goodsId).limitType == fire.pb.shop.utils.LimitType.NO_LIMIT)//验证是否为限购或限卖商品
					continue;
				tmpGoodsList.add(goods);
			}
//		}
//		else {
//			for (fire.pb.shop.SGoods goods : goodsMap.values()) {
//				if (goods.limitType == fire.pb.shop.utils.LimitType.DAY_LIMIT ||
//						goods.limitType == fire.pb.shop.utils.LimitType.WEEK_LIMIT) {
//					tmpGoodsList.add(goods);
//				}
//			}
//		}
		
		if (tmpGoodsList.size() == 0)
			return false;
		
		java.util.ArrayList<fire.pb.shop.GoodsLimit> retGoodsLimits = new java.util.ArrayList<>();
		for (fire.pb.shop.SGoods goods : tmpGoodsList) {
			int number = 0;
			fire.pb.shop.GoodsLimit goodsLimit = new fire.pb.shop.GoodsLimit();
			if (querytype == fire.pb.shop.utils.LimitType.QUERY_BUY) {
				number = fire.pb.shop.srv.limit.LimitManager.getInstance().retBuyNum(roleid, goods);
			} else if (querytype == fire.pb.shop.utils.LimitType.QUERY_SALE) {
				number = fire.pb.shop.srv.limit.LimitManager.getInstance().retSaleNum(roleid, goods);
			}
			
//			if (querytype == fire.pb.shop.utils.LimitType.QUERY_BUY) {
//				int extNum = 0;
//				if (goods.limitType == LimitType.DAY_LIMIT)
//					extNum = fire.pb.fushi.Module.getVipTableRight(roleid, SSendVipInfo.RIGHT_DAILY_GOODS_COUNT);
//				else if (goods.limitType == LimitType.WEEK_LIMIT)
//					extNum = fire.pb.fushi.Module.getVipTableRight(roleid, SSendVipInfo.RIGHT_WEEKLY_DISCOUNT_COUNT);
//				extNum += goods.limitNum;
//				
//				goodsLimit.goodsid = goods.getId();
//				goodsLimit.number = extNum;
//				retGoodsLimits.add(goodsLimit);
//			}
//			else {
				goodsLimit.goodsid = goods.getId();
				goodsLimit.number = number;
				retGoodsLimits.add(goodsLimit);
//			}
		}
		
		fire.pb.shop.SQueryLimit p = new fire.pb.shop.SQueryLimit();
		p.querytype = querytype;
		p.goodslimits = retGoodsLimits;
		
		mkdb.Procedure.psendWhileCommit(roleid, p);
		return true;
	}
}
