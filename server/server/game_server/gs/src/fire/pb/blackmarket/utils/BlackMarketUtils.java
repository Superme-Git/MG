package fire.pb.blackmarket.utils;

import java.util.Map;

import org.apache.log4j.Logger;

import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.shop.GoldOrder;

public class BlackMarketUtils {
	
	/**
	 * 0没有公示时间
	 */
	public static final int NO_PUBLICITY = 0;
	
	/**
	 * 订单状态
	 */
	public static final class OrderState {
		/**
		 * 0待上架
		 */
		public static final int STAY_UP = 0;
		
		/**
		 * 1在售
		 */
		public static final int STAY_SALE = 1;
		
		/**
		 * 2锁定
		 */
		public static final int LOCK = 2;
		/**
		 * 3已售
		 */
		public static final int SALE_DONE = 3;
		
		/**
		 * 4待领取
		 */
		public static final int STAY_TAKE = 4;
		
		/**
		 * 5领取完成
		 */
		public static final int TAKE_DONE = 5;
	}


	/**
	 * 订单类型
	 */
	public static final class OrderType {
		/**
		 * 金币 订单
		 */
		public static final int GOLD = 1;
		
		/**
		 * 道具订单
		 */
		public static final int ITEM = 2;
		
		/**
		 * 宠物订单
		 */
		public static final int PET = 3;
	}
	
	/**
	 * 实体对象间的数据拷贝
	 * @param xbeanGoldOrder
	 * @return
	 */
	public static GoldOrder xbeanGoldOrderCopyGoldOrder(xbean.GoldOrder xbeanGoldOrder) {
		GoldOrder goldOrder = new GoldOrder();
		goldOrder.pid = xbeanGoldOrder.getPid();
		goldOrder.number = xbeanGoldOrder.getNumber();
		goldOrder.price = xbeanGoldOrder.getPrice();
		goldOrder.publicity = xbeanGoldOrder.getPublicity();
		goldOrder.state = xbeanGoldOrder.getState();
		goldOrder.locktime = xbeanGoldOrder.getLocktime();
		goldOrder.time = xbeanGoldOrder.getTime();
		return goldOrder;
	}
	
	/**
	 * RMB单位：元
	 * 
	 * @return 返回最低人民币金额, 失败返回-1
	 */
	public static long getMinRMB() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(438);
			if (conf != null) {
				return Long.parseLong(conf.getValue());
			}
		}
		LOG.error("获取最低人民币金额常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * 金币
	 * 
	 * @return 返回最低金币数量, 失败返回-1
	 */
	public static long getMinGold() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(439);
			if (conf != null) {
				return Long.parseLong(conf.getValue());
			}
		}
		LOG.error("获取最低金币数量常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * RMB单位：元
	 * 
	 * @return 返回最高人民币金额, 失败返回-1
	 */
	public static long getMaxRMB() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(440);
			if (conf != null) {
				return Long.parseLong(conf.getValue());
			}
		}
		LOG.error("获取最高人民币金额常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * 金币
	 * 
	 * @return 返回最高金币数量, 失败返回-1
	 */
	public static long getMaxGold() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(441);
			if (conf != null) {
				return Long.parseLong(conf.getValue());
			}
		}
		LOG.error("获取最高金币数量常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * 金币订单最大上架数量
	 * 
	 * @return 返回金币订单显示数量, 失败返回-1
	 */
	public static int getMaxOrderUpNumber() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(442);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		LOG.error("获取金币订单显示数量常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * 金币订单最大显示数量
	 * 
	 * @return 返回金币订单显示数量, 失败返回-1
	 */
	public static int getMaxOrderLstNumber() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(443);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		LOG.error("获取金币订单显示数量常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * 宠物最低价格
	 * 
	 * @return 返回宠物最低价格, 失败返回-1
	 */
	public static int getPetMinPrice() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(452);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		LOG.error("获取宠物最低价格常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * 装备最低价格
	 * 
	 * @return 返回装备最低价格, 失败返回-1
	 */
	public static int getEquipMinPrice() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(453);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		LOG.error("获取装备最低价格常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * 珍品宠物的最低等级
	 * 
	 * @return 返回珍品宠物的最低等级, 失败返回-1
	 */
	public static int getPetMinLevel() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(454);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		LOG.error("获取珍品宠物的最低等级常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * 珍品装备的最低等级
	 * 
	 * @return 返回珍品装备的最低等级, 失败返回-1
	 */
	public static int getEquipMinLevel() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(455);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		LOG.error("获取珍品装备的最低等级常量错误,可能未填写");
		return -1;
	}
	

	private static final Logger LOG = Logger.getLogger("BLACKMARKET");
	
	/**
	 * 通用配置常量表
	 */
	public static Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);
	
}
