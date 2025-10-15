package fire.pb.shop.srv.limit;

import java.util.ArrayList;

import fire.pb.fushi.SSendVipInfo;
import fire.pb.shop.GoodsLimit;
import fire.pb.shop.SGoods;
import fire.pb.shop.SQueryLimit;
import fire.pb.shop.utils.LimitType;
import fire.pb.util.DateValidate;

/**
 * 
 * 限购限售服务
 * 
 * @see 这里所有和XDB操作相关的函数都要在Procedure中调用
 * 
 * @author liangyanpeng
 * 
 */
public class LimitManager {
	
	public static LimitManager getInstance() {
		return LimitBuySrvHolder.instance;
	}
	
	public static class LimitBuySrvHolder {
		static LimitManager instance = new LimitManager();
	}

	/**
	 * @return 限购返回false，不限购返回true
	 */
	public boolean isBuyLimit(final long roleid, final SGoods goods, final int buyNum, final int extNum) {
		if (goods.limitType == LimitType.NO_LIMIT) {
			return true;
		}
		int retBuyNum = retBuyNum(roleid, goods);
		if (retBuyNum == 0) {
			return true;//不限购
		}
					
		return  retBuyNum + buyNum <= (goods.limitNum + extNum);
	}
	
	/**
	 * 已购买数量
	 * @return 默认返回购买数0 
	 */
	public int retBuyNum(final long roleid, final SGoods goods) {
		long curr = System.currentTimeMillis();
		xbean.RoleBuyGoodsLimit buyGoodsLimit = xtable.Rolebuygoodslimits.get(roleid);
		if (buyGoodsLimit == null) {
			return 0;
		}
		xbean.NumberAndTime bean = null;
		switch (goods.limitType) {
		case LimitType.DAY_LIMIT:
			bean = buyGoodsLimit.getDaylimit().get(goods.getId());
			if (bean != null) {
				if (DateValidate.inTheSameDay(curr, bean.getTime())) {
					return bean.getNumber();
				}
			}
			break;
		case LimitType.WEEK_LIMIT:
			bean = buyGoodsLimit.getWeeklimit().get(goods.getId());
			if (bean != null) {
				if (DateValidate.inTheSameWeek(curr, bean.getTime())) {
					return bean.getNumber();
				}
			}
			break;
		case LimitType.MONTH_LIMIT:
			bean = buyGoodsLimit.getMonthlimit().get(goods.getId());
			if (bean != null) {
				if (DateValidate.inTheSameMonth(curr, bean.getTime())) {
					return bean.getNumber();
				}
			}
			break;
		}
		return 0;
	}

	/**
	 * 计买次数
	 * * @return 返回-1为非限买商品,      >= 0的数为限买次数(还有一种为0的情况是策划填错表)
	 */
	public int saveBuyLimit(final long roleid, final SGoods goods, final int buyNum) {
		if (goods.limitType == LimitType.NO_LIMIT) {
			return -1;
		}
		
//		if (!isBuyLimit(roleid, goods, buyNum)) {
//			return;
//		}
		
		xbean.RoleBuyGoodsLimit buyGoodsLimit = xtable.Rolebuygoodslimits.get(roleid);
		if (buyGoodsLimit == null) {
			buyGoodsLimit = xbean.Pod.newRoleBuyGoodsLimit();
			xtable.Rolebuygoodslimits.insert(roleid, buyGoodsLimit);
		}
		long curr = System.currentTimeMillis();
		xbean.NumberAndTime bean = null;
		int newNum = 0;
		switch (goods.limitType) {
		case LimitType.DAY_LIMIT:
			bean = buyGoodsLimit.getDaylimit().get(goods.getId());
			if (bean == null) {
				bean = xbean.Pod.newNumberAndTime();
				buyGoodsLimit.getDaylimit().put(goods.getId(), bean);
			}
			if (DateValidate.inTheSameDay(curr, bean.getTime())) {
				int number = bean.getNumber();
				newNum = number + buyNum;
				bean.setNumber(newNum);
			} else {
				newNum = buyNum;
				bean.setNumber(newNum);
			}
			bean.setTime(curr);
			break;
		case LimitType.WEEK_LIMIT:
			bean = buyGoodsLimit.getWeeklimit().get(goods.getId());
			if (bean == null) {
				bean = xbean.Pod.newNumberAndTime();
				buyGoodsLimit.getWeeklimit().put(goods.getId(), bean);
			}
			if (DateValidate.inTheSameWeek(curr, bean.getTime())) {
				int number = bean.getNumber();
				newNum = number + buyNum;
				bean.setNumber(newNum);
			} else {
				newNum = buyNum;
				bean.setNumber(newNum);
			}
			bean.setTime(curr);
			break;
		case LimitType.MONTH_LIMIT:
			bean = buyGoodsLimit.getMonthlimit().get(goods.getId());
			if (bean == null) {
				bean = xbean.Pod.newNumberAndTime();
				buyGoodsLimit.getMonthlimit().put(goods.getId(), bean);
			}
			if (DateValidate.inTheSameMonth(curr, bean.getTime())) {
				int number = bean.getNumber();
				newNum = number + buyNum;
				bean.setNumber(newNum);
			} else {
				newNum = buyNum;
				bean.setNumber(newNum);
			}
			bean.setTime(curr);
			break;
		}
		
		postNofy(LimitType.QUERY_BUY, roleid, goods.id, newNum);
		
		return newNum;
	}
	
	
	/**
	 * @return 限售返回false，不限售返回true
	 */
	public boolean isSaleLimit(final long roleid, final SGoods goods, final int saleNum) {
		if (goods.limitType == LimitType.NO_LIMIT) {
			return true;
		}
		int retSaleNum = retSaleNum(roleid, goods);
		if (retSaleNum == 0) {
			return true;//不限购
		}
		return  retSaleNum + saleNum <= goods.limitSaleNum;
	}
	
	/**
	 * 已售数量
	 * @return 默认返回0
	 */
	public int retSaleNum(final long roleid, final SGoods goods) {
		long curr = System.currentTimeMillis();
		xbean.RoleSaleGoodsLimit saleGoodsLimit = xtable.Rolesalegoodslimits.get(roleid);
		if (saleGoodsLimit == null) {
			return 0;
		}
		xbean.NumberAndTime bean = null;
		switch (goods.limitType) {
		case LimitType.DAY_LIMIT:
			bean = saleGoodsLimit.getDaylimit().get(goods.getId());
			if (bean != null) {
				if (DateValidate.inTheSameDay(curr, bean.getTime())) {
					return bean.getNumber();
				}
			}
			break;
		case LimitType.WEEK_LIMIT:
			bean = saleGoodsLimit.getWeeklimit().get(goods.getId());
			if (bean != null) {
				if (DateValidate.inTheSameWeek(curr, bean.getTime())) {
					return bean.getNumber();
				}
			}
			break;
		case LimitType.MONTH_LIMIT:
			bean = saleGoodsLimit.getMonthlimit().get(goods.getId());
			if (bean != null) {
				if (DateValidate.inTheSameMonth(curr, bean.getTime())) {
					return bean.getNumber();
				}
			}
			break;
		}
		return 0;
	}

	/**
	 * 计售次数
	 * @return 返回-1为非限售商品,      >= 0的数为限售次数(还有一种为0的情况是策划填错表)
	 */
	public int saveSaleLimit(final long roleid, final SGoods goods, final int saleNum) {
		if (goods.limitType == LimitType.NO_LIMIT) {
			return -1;
		}
//		if (!isSaleLimit(roleid, goods, saleNum)) {
//			return;
//		}
		xbean.RoleSaleGoodsLimit saleGoodsLimit = xtable.Rolesalegoodslimits.get(roleid);
		if (saleGoodsLimit == null) {
			saleGoodsLimit = xbean.Pod.newRoleSaleGoodsLimit();
			xtable.Rolesalegoodslimits.insert(roleid, saleGoodsLimit);
		}
		long curr = System.currentTimeMillis();
		xbean.NumberAndTime bean = null;
		int newNum = 0;
		switch (goods.limitType) {
		case LimitType.DAY_LIMIT:
			bean = saleGoodsLimit.getDaylimit().get(goods.getId());
			if (bean == null) {
				bean = xbean.Pod.newNumberAndTime();
				saleGoodsLimit.getDaylimit().put(goods.getId(), bean);
			}
			if (DateValidate.inTheSameDay(curr, bean.getTime())) {
				int number = bean.getNumber();
				newNum = number + saleNum;
				bean.setNumber(newNum);
			} else {
				newNum = saleNum;
				bean.setNumber(newNum);
			}
			bean.setTime(curr);
			break;
		case LimitType.WEEK_LIMIT:
			bean = saleGoodsLimit.getWeeklimit().get(goods.getId());
			if (bean == null) {
				bean = xbean.Pod.newNumberAndTime();
				saleGoodsLimit.getWeeklimit().put(goods.getId(), bean);
			}
			if (DateValidate.inTheSameWeek(curr, bean.getTime())) {
				int number = bean.getNumber();
				newNum = number + saleNum;
				bean.setNumber(newNum);
			} else {
				newNum = saleNum;
				bean.setNumber(newNum);
			}
			bean.setTime(curr);
			break;
		case LimitType.MONTH_LIMIT:
			bean = saleGoodsLimit.getMonthlimit().get(goods.getId());
			if (bean == null) {
				bean = xbean.Pod.newNumberAndTime();
				saleGoodsLimit.getMonthlimit().put(goods.getId(), bean);
			}
			if (DateValidate.inTheSameMonth(curr, bean.getTime())) {
				int number = bean.getNumber();
				newNum = number + saleNum;
				bean.setNumber(newNum);
			} else {
				newNum = saleNum;
				bean.setNumber(newNum);
			}
			bean.setTime(curr);
			break;
		}
		
		postNofy(LimitType.QUERY_SALE, roleid, goods.id, newNum);
		
		return newNum;
	}
	
	/**
	 * 购买或售卖成功后通知前端
	 */
	public void postNofy(int type, long roldId, int goodsId, int number) {
		GoodsLimit goodsLimit = new GoodsLimit(goodsId, number);
		ArrayList<GoodsLimit> goodsLimits = new ArrayList<>(1);
		goodsLimits.add(goodsLimit);
		SQueryLimit squeryLimit = new SQueryLimit(type, goodsLimits);
		mkdb.Procedure.psendWhileCommit(roldId, squeryLimit);
	}

}
