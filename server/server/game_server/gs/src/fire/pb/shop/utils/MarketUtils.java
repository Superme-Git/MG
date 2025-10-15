package fire.pb.shop.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fire.pb.common.SCommon;
import fire.pb.game.MoneyType;
import fire.pb.item.FoodItem;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.shop.MarketThreeTable;
import fire.pb.shop.srv.market.MarketManager;
import fire.pb.shop.srv.market.floating.FloatingMarketManager;
import fire.pb.talk.MessageMgr;

/**
 * 拍卖常量和工具方法类
 * 
 * @author liangyanpeng
 */
public class MarketUtils {

	private static final Logger LOG = Logger.getLogger("MARKET");

	/**
	 * 通用配置常量表
	 */
	public static Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);

	/**
	 * 拍卖买物品日志
	 */
	public static final int BUY_LOG = 1;

	/**
	 * 拍卖卖物品日志
	 */
	public static final int SALE_LOG = 2;

	/**
	 * 最多保存多少交易日志
	 */
	public static final int LOG_SIZE = 90;

	/**
	 * 玩家拍卖上架商品限制格子数
	 */
	public static final int MARKET_CONTAINER_LIMIT = 8;

	/**
	 * 8小时毫秒
	 */
	public static final long EIGHT_HOUR = 8 * 60 * 60 * 1000;

	/**
	 * 1天毫秒
	 */
	public static final long ONE_DAY = 24 * 60 * 60 * 1000;

	/**
	 * 背包容器
	 */
	public static final int CONTAINER_BAG = 1;

	/**
	 * 宠物栏容器
	 */
	public static final int CONTAINER_PET = 2;

	/**
	 * 拍卖宠物栏容器
	 */
	public static final int CONTAINER_MARKET_PET = 3;

	/**
	 * 拍卖背包容器
	 */
	public static final int CONTAINER_MARKET_BAG = 4;

	/**
	 * 我要购买
	 */
	public static final int BROWSE_MY_BUY = 1;

	/**
	 * 公示物品
	 */
	public static final int BROWSE_SHOW = 2;

	/**
	 * 非珍品
	 */
	public static final int NOT_RARITY = 0;

	/**
	 * 珍品
	 */
	public static final int RARITY = 1;

	/**
	 * 最大价格
	 */
	public static final int MAX_PRICE = 999999999;

	/**
	 * 没有三级菜单
	 */
	public static final int NO_THREE_MENU = 0;

	/**
	 * 一个商品被关注的最大次数限制
	 */
	public static final int MAX_ATTENTION_NUMBER = 999;

	/**
	 * 角色关注数量上限,购买或公示
	 */
	public static final int ROLE_MAX_ATTENTION_NUMBER = 8;

	/**
	 * 分页数量
	 */
	public static final int PAGE_SIZE = 8;
	
	/**
	 * 最大搜索结果
	 */
	public static final int MAX_SEARCH_RESULT = 320;

	/**
	 * 拍卖道具是否在世界频道显示（玩家点击的时候）
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月14日 上午1:35:06
	 * @version 1.0
	 */
	public static boolean isMarketItemChatShow = true;

	/**
	 * 验证是否拍卖中的物品时间过期
	 * 
	 * @return 超时返回true
	 */
	public static boolean validatorExpireTime(final long exprieTime) {
		long currTime = System.currentTimeMillis();
		return currTime >= exprieTime;
	}

	/**
	 * 毫秒
	 * 
	 * @return 返回物品上架到期公示时间, 失败返回-1
	 */
	public static long getShowTime() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(153);
			if (conf != null) {
				long currTime = System.currentTimeMillis();
				// TODO 为了测试
				return currTime + /* 120 * 1000 */Integer.parseInt(conf.getValue()) * (60 * 60 * 1000);// 小时
				// return currTime + 5000;
			}
		}
		LOG.error("获取公示时间常量错误,可能未填写");
		return -1;
	}

	/**
	 * 毫秒
	 * 
	 * @return 返回珍品物品上架到期时间, 失败返回-1
	 */
	public static long getRarityExpireTime() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(154);
			if (conf != null) {
				long showTime = getShowTime();
				return showTime + Integer.parseInt(conf.getValue()) * (24 * 60 * 60 * 1000);// 天
				// return showTime + 10000;
			}
		}
		LOG.error("获取珍品过期时间常量错误,可能未填写");
		return -1;
	}

	/**
	 * 毫秒
	 * 
	 * @return 返回非珍品物品上架到期时间, 失败返回-1
	 */
	public static long getNotRarityExpireTime() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(155);
			if (conf != null) {
				long currTime = System.currentTimeMillis();
				return currTime + Integer.parseInt(conf.getValue()) * (60 * 60 * 1000);// 小时
				// return currTime + 10000;
			}
		}
		LOG.error("获取非珍品过期时间常量错误,可能未填写");
		return -1;
	}

	/**
	 * 拍卖浮动价格统计周期(单位:毫秒)
	 * 
	 * @return 返回拍卖价格浮动时间周期, 失败返回-1
	 */
	public static long getFloatingPriceTime() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(156);
			if (conf != null) {
				return Integer.parseInt(conf.getValue()) * 1000;// 秒
			}
		}
		LOG.error("获取拍卖价格浮动时间周期常量错误,可能未填写");
		return -1;
	}

	/**
	 * 获取拍卖购买和出售等级
	 * 
	 * @return 返回拍卖购买和出售等级, 失败返回-1
	 */
	public static int getMarketLv() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(160);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());// 等级
			}
		}
		LOG.error("获取拍卖购买和出售等级常量错误,可能未填写");
		return -1;
	}

	/**
	 * 购买预售时间(单位:毫秒)
	 * 
	 * @return 预售时间, 失败返回-1
	 */
	public static int getPresellTime() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(327);
			if (conf != null) {
				return Integer.parseInt(conf.getValue()) * 1000;// 毫秒
			}
		}
		LOG.error("获取拍卖购买预售时间常量错误,可能未填写");
		return -1;
	}

	/**
	 * 购买预售列表队列上限数量
	 * 
	 * @return 预售时间, 失败返回-1
	 */
	public static int getPresellQueueMaxNum() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(328);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());// 队列长度
			}
		}
		LOG.error("获取拍卖购预售列表队列上限数量常量错误,可能未填写");
		return -1;
	}
	
	/**
	 * @return  拍卖行银币税率百分比, 失败返回-1
	 */
	public static Double getTax() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(367);
			if (conf != null) {
				return Double.parseDouble(conf.getValue())  / 100;
			}
		}
		LOG.error("获取拍卖行银币税率百分比常量错误,可能未填写");
		return -1D;
	}


	public static int retRangeByLevel(int level) {
		if (level >= 0 && level < 10) {
			level = 1;
		} else if (level >= 10 && level < 20) {
			level = 10;
		} else if (level >= 20 && level < 30) {
			level = 20;
		} else if (level >= 30 && level < 40) {
			level = 30;
		} else if (level >= 40 && level < 50) {
			level = 40;
		} else if (level >= 50 && level < 60) {
			level = 50;
		} else if (level >= 60 && level < 70) {
			level = 60;
		} else if (level >= 70 && level < 80) {
			level = 70;
		} else if (level >= 80 && level < 90) {
			level = 80;
		} else if (level >= 90 && level < 100) {
			level = 90;
		} else if (level >= 100 && level < 110) {
			level = 100;
		} else if (level >= 110 && level < 120) {
			level = 110;
		} else if (level >= 120 && level < 130) {
			level = 120;
		} else if (level >= 130 && level < 140) {
			level = 130;
		} else if (level >= 140 && level < 150) {
			level = 140;
		} else if (level >= 150 && level < 160) {
			level = 150;
		} else if (level >= 160 && level < 170) {
			level = 160;
		} else if (level >= 170 && level < 180) {
			level = 170;
		} else if (level >= 180 && level < 190) {
			level = 180;
		} else if (level >= 190 && level < 200) {
			level = 190;
		} else if (level >= 200 && level < 210) {
			level = 200;
		}
		return level;
	}

	public static int[] getLevelRange(int level) {
		int[] levelRange = new int[2];
		if (level >= 1 && level <= 10) {
			levelRange[0] = 1;
			levelRange[1] = 10;
		} else if (level > 10 && level <= 20) {
			levelRange[0] = 11;
			levelRange[1] = 20;
		} else if (level > 20 && level <= 30) {
			levelRange[0] = 21;
			levelRange[1] = 30;
		} else if (level > 30 && level <= 40) {
			levelRange[0] = 31;
			levelRange[1] = 40;
		} else if (level > 40 && level <= 50) {
			levelRange[0] = 41;
			levelRange[1] = 50;
		} else if (level > 50 && level <= 60) {
			levelRange[0] = 51;
			levelRange[1] = 60;
		} else if (level > 60 && level <= 70) {
			levelRange[0] = 61;
			levelRange[1] = 70;
		} else if (level > 70 && level <= 80) {
			levelRange[0] = 71;
			levelRange[1] = 80;
		} else if (level > 80 && level <= 90) {
			levelRange[0] = 81;
			levelRange[1] = 90;
		} else if (level > 90 && level <= 100) {
			levelRange[0] = 91;
			levelRange[1] = 100;
		} else if (level > 100 && level <= 110) {
			levelRange[0] = 101;
			levelRange[1] = 110;
		} else if (level > 110 && level <= 120) {
			levelRange[0] = 111;
			levelRange[1] = 120;
		} else if (level > 120 && level <= 130) {
			levelRange[0] = 121;
			levelRange[1] = 130;
		} else if (level > 130 && level <= 140) {
			levelRange[0] = 131;
			levelRange[1] = 140;
		} else if (level > 140 && level <= 150) {
			levelRange[0] = 141;
			levelRange[1] = 150;
		} else if (level > 150 && level <= 160) {
			levelRange[0] = 151;
			levelRange[1] = 160;
		} else if (level > 160 && level <= 170) {
			levelRange[0] = 161;
			levelRange[1] = 170;
		} else if (level > 170 && level <= 180) {
			levelRange[0] = 171;
			levelRange[1] = 180;
		} else if (level > 180 && level <= 190) {
			levelRange[0] = 181;
			levelRange[1] = 190;
		} else if (level > 190 && level <= 200) {
			levelRange[0] = 191;
			levelRange[1] = 200;
		} else if (level > 200 && level <= 210) {
			levelRange[0] = 201;
			levelRange[1] = 210;
		}
		return levelRange;
	}

	/**
	 * 等级获得下标
	 * 
	 * @param level 等级
	 * @return 失败返回-1,正常返回数组下标
	 */
	public static int getIdx(int level, int[] arrays) {
		int tmp = -1;
		for (int i = 0, j = 1; j < arrays.length; i++, j++) {
			if ((level > arrays[i] && level < arrays[j]) || level == arrays[i]) {
				tmp = i;
				break;
			}
		}
		return tmp;
	}

	public static List<Integer> toInnerPetSkill(LinkedList<fire.pb.Petskill> list) {
		List<Integer> retList = new ArrayList<>();
		for (fire.pb.Petskill petSkill : list) {
			retList.add(petSkill.skillid);
		}
		return retList;
	}

	/**
	 * 角色id加星号屏蔽保留后两位显示
	 */
	public static String getShieldRoleId(long roldId) {
		String roleIdStr = Long.toString(roldId);
		char[] tmp = roleIdStr.toCharArray();
		for (int i = 0; i < tmp.length - 3; i++) {
			tmp[i] = '*';
		}
		return String.valueOf(tmp);
	}

	/**
	 * @param itemType 道具类型
	 * @param id 数据库唯一id, 不相同的类型会有重复的id, 因为是不同的表
	 * @return 生成关注商品字串格式<H1>3-1</H1>
	 */
	public static String genAttentionKey(int itemType, long id) {
		StringBuilder element = new StringBuilder();
		element.append(itemType).append("-").append(id);
		return element.toString();
	}

	/**
	 * 检查数量
	 * return -1无效 , > 0 有效
	 */
	public static int checkAttionNumber(int okOrCancel, int newNumber) {
		switch (okOrCancel) {
		case AttentionType.ATTENTION_OK:// 关注
			newNumber += 1;
			if (newNumber > MarketUtils.MAX_ATTENTION_NUMBER) {
				LOG.error("关注次数已最大化！");
				return -1;
			}
			break;
		case AttentionType.ATTENTION_CANCEL: // 取消关注
			newNumber -= 1;
			if (newNumber < 0) {
				LOG.error("关注次数已最小化！");
				return -1;
			}
			break;
		}
		return newNumber;
	}

	/**
	 * 检查角色是否可以继续关注道具
	 * 
	 * @param roleId
	 * @param buyOrPublicity 公示或购买
	 * @return true可关注, false不可关注
	 */
	public static boolean checkRoleAttentionNumber(long roleId, int buyOrPublicity) {
		// mxdb关注表
		xbean.AttentionGoods attentionGoods = xtable.Marketattentiongoodstab.get(roleId);
		if (attentionGoods == null) {
			return true;
		}
		List<xbean.AttentionGoodsBean> attentionLst = null;
		switch (buyOrPublicity) {
		case AttentionType.ATTENTION_BUY:
			attentionLst = attentionGoods.getBuyattentions();
			break;
		case AttentionType.ATTENTION_PUBLICITY:
			attentionLst = attentionGoods.getPublicityattentions();
			break;
		}
		if (attentionLst != null && attentionLst.size() < ROLE_MAX_ATTENTION_NUMBER) {
			return true;
		}
		return false;
	}

	/**
	 * 记录某个角色关注或取消关注商品数据库id <H1>roleId(one) <--> id(many) one-to-many</H1>+
	 * 
	 * @param okOrCancel 1关注 2取消关注
	 * @param attentionKey 关注key
	 * @param showTime 公示时间,取消关注的时候可以为0
	 * @param expireTime 过期时间,取消关注的时候可以为0
	 * @param roleId
	 * @param buyOrPublicity 关注状态 1购买，2公示
	 */
	public static boolean roleAttentionGoodsXdb(int okOrCancel, String attentionKey, long showTime, long expireTime,
			long roleId, int buyOrPublicity) {
		boolean succ = false;
		List<xbean.AttentionGoodsBean> attentionLst = null;
		// mkdb关注表
		xbean.AttentionGoods attentionGoods = xtable.Marketattentiongoodstab.get(roleId);
		if (attentionGoods == null) {
			attentionGoods = xbean.Pod.newAttentionGoods();
			xtable.Marketattentiongoodstab.insert(roleId, attentionGoods);
		}
		switch (okOrCancel) {
		case AttentionType.ATTENTION_OK:// 关注
			// 遍历是否关注过此商品
			if (AttentionType.ATTENTION_BUY == buyOrPublicity) { // 购买关注
				attentionLst = attentionGoods.getBuyattentions();
			} else if (AttentionType.ATTENTION_PUBLICITY == buyOrPublicity) {// 公示关注
				attentionLst = attentionGoods.getPublicityattentions();
			}
			if (attentionLst != null) {
				for (xbean.AttentionGoodsBean attention : attentionLst) {
					if (attentionKey.equals(attention.getId())) {
						StringBuilder sbd = new StringBuilder();
						sbd.append("role=").append(roleId).append(", 添加关注attentionKey=").append(attentionKey).append(", 已经关注过！");
						LOG.info(sbd.toString());
						return false;
					}
				}
				// 添加关注商品
				xbean.AttentionGoodsBean attentionOk = xbean.Pod.newAttentionGoodsBean();
				attentionOk.setId(attentionKey);
				attentionOk.setShowtime(showTime);
				attentionOk.setExpiretime(expireTime);
				succ = attentionLst.add(attentionOk);
				if (MarketManager.LOG.isInfoEnabled()) {
					StringBuilder sbd = new StringBuilder();
					sbd.append("role=").append(roleId).append(", 添加关注attentionKey=").append(attentionKey).append(", 操作状态：").append(succ);
					LOG.info(sbd.toString());
				}
			}
			break;
		case AttentionType.ATTENTION_CANCEL: // 取消关注
			// 遍历是否关注过此商品
			if (AttentionType.ATTENTION_BUY == buyOrPublicity) { // 购买关注
				attentionLst = attentionGoods.getBuyattentions();
			} else if (AttentionType.ATTENTION_PUBLICITY == buyOrPublicity) {// 公示关注
				attentionLst = attentionGoods.getPublicityattentions();
			}
			int idx = 0;
			xbean.AttentionGoodsBean tmp = null;
			if (attentionLst != null) {
				for (int i = 0; i < attentionLst.size(); i++) {
					xbean.AttentionGoodsBean attentionCancel = attentionLst.get(i);
					if (attentionKey.equals(attentionCancel.getId())) {
						tmp = attentionCancel;
						idx = i;
						break;
					}
				}
				if (tmp != null) {
					// 取消关注商品
					succ = attentionLst.remove(idx) != null ? true : false;
					if (MarketManager.LOG.isInfoEnabled()) {
						StringBuilder sbd = new StringBuilder();
						sbd.append("role=").append(roleId).append(", 取消关注attentionKey=").append(attentionKey).append(", 操作状态：").append(succ);
						LOG.info(sbd.toString());
					}
				}
			}
			break;
		}
		return succ;
	}

	/**
	 * 记录商品被多个角色关注,当卖出或下架,通过这个关系表来通知关注的角色 <H1>key(one) <--> roleId(many) one-to-many</H1>
	 * 
	 * @param attentionType 动作类型：1、关注 2、取消关注
	 * @param key 关注的商品id
	 * @param roleId 关注商品角色id
	 */
	public static boolean goodsAttentionRoleXdb(int attentionType, String attentionKey, long roleId) {
		xbean.AttentionRole attentionRole = xtable.Marketattentionroletab.get(attentionKey);
		Set<Long> roleIds = null;
		boolean succ = false;
		switch (attentionType) {
		case AttentionType.ATTENTION_OK: // 关注
			if (attentionRole == null) {
				attentionRole = xbean.Pod.newAttentionRole();
				xtable.Marketattentionroletab.insert(attentionKey, attentionRole);
			}
			roleIds = attentionRole.getRoleids();
			if (!roleIds.contains(roleIds)) {// 某个角色未关注过再添加
				succ = roleIds.add(roleId);
				if (MarketManager.LOG.isInfoEnabled()) {
					StringBuilder sbd = new StringBuilder();
					sbd.append("道具attentionKey=").append(attentionKey).append(", 添加关注role=").append(roleId).append(", 操作状态：").append(succ);
					LOG.info(sbd.toString());
				}
			}
			break;
		case AttentionType.ATTENTION_CANCEL: // 取消关注
			if (attentionRole == null) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("道具attentionKey=").append(attentionKey).append(", 取消关注role=").append(roleId).append(", 此商品从未被关注过,不需取消！");
				LOG.error(sbd.toString());
				return false;
			}
			roleIds = attentionRole.getRoleids();
			succ = roleIds.remove(roleId);
			if (MarketManager.LOG.isInfoEnabled()) {
				StringBuilder sbd = new StringBuilder();
				sbd.append("道具attentionKey=").append(attentionKey).append(", 取消关注role=").append(roleId).append(", 操作状态：").append(succ);
				LOG.info(sbd.toString());
			}
			break;
		}
		return succ;
	}

	/**
	 * 物品不存在（下架、卖出、过期），通知关注物品的角色删除关注
	 * 
	 * @param attenType 关注状态 1购买，2公示 ，其它数值状态为购买和公示都遍历寻找删除
	 */
	public static boolean notifyRoleRemoveAttention(String attentionKey, int attenType) {
		//
		xbean.AttentionRole attentionRole = xtable.Marketattentionroletab.get(attentionKey);
		if (attentionRole == null) {
			return true;
		}
		Set<Long> roleIds = attentionRole.getRoleids();
		// 删除每个角色关注的商品
		for (Long roleId : roleIds) {
			if (attenType == AttentionType.ATTENTION_BUY) {
				roleAttentionGoodsXdb(AttentionType.ATTENTION_CANCEL, attentionKey, 0, 0, roleId, AttentionType.ATTENTION_BUY);
			} else if (attenType == AttentionType.ATTENTION_PUBLICITY) {
				roleAttentionGoodsXdb(AttentionType.ATTENTION_CANCEL, attentionKey, 0, 0, roleId, AttentionType.ATTENTION_PUBLICITY);
			} else {
				roleAttentionGoodsXdb(AttentionType.ATTENTION_CANCEL, attentionKey, 0, 0, roleId, AttentionType.ATTENTION_BUY);
				roleAttentionGoodsXdb(AttentionType.ATTENTION_CANCEL, attentionKey, 0, 0, roleId, AttentionType.ATTENTION_PUBLICITY);
			}
		}
		// 删除被关注的商品
		return xtable.Marketattentionroletab.remove(attentionKey);
	}

	/**
	 * @param item 所有继承BasicItem对象的道具类
	 * @return 返回道具的等级或品质
	 */
	public static int getItemLevel(ItemBase item) {
		// 普通道具有品质和等级区分
		if (item instanceof FoodItem) {
			FoodItem fItem = (FoodItem) item;
			return fItem.getQuality();
		} else if (item instanceof GroceryItem) {
			GroceryItem gItem = (GroceryItem) item;
			return gItem.getLevel();
		} else {
			return item.getItemAttr().getLevel();
		}
	}

	/**
	 * @param addAttr 附加属性
	 * @return 属性总和
	 */
	public static int getTotalAttr(Map<Integer, Integer> addAttr) {
		int totalValue = 0;
		if (addAttr == null || addAttr.size() == 0) {
			return totalValue;
		}
		for (int v : addAttr.values()) {
			totalValue += v;
		}
		return totalValue;
	}

	/**
	 * 关注的各种类型枚举
	 * 
	 * @author liangyanpeng
	 */
	public static class AttentionType {

		/**
		 * 关注
		 */
		public static final int ATTENTION_OK = 1;

		/**
		 * 取消关注
		 */
		public static final int ATTENTION_CANCEL = 2;

		/**
		 * 购买关注
		 */
		public static final int ATTENTION_BUY = 1;

		/**
		 * 公示关注
		 */
		public static final int ATTENTION_PUBLICITY = 2;
	}

	/**
	 * 获取关注道具数量
	 * 
	 * @param roleId
	 * @param buyOrPublicity 公示或购买
	 * @return true可关注, false不可关注
	 */
	public static int getRoleAttentionNumber(long roleId) {
		// mxdb关注表
		xbean.AttentionGoods attentionGoods = xtable.Marketattentiongoodstab.get(roleId);
		if (attentionGoods == null) {
			return 0;
		}
		int num = 0;
		if (attentionGoods.getBuyattentions() != null)
			num += attentionGoods.getBuyattentions().size();
		if (attentionGoods.getPublicityattentions() != null)
			num += attentionGoods.getPublicityattentions().size();
		return num;
	}

	/**
	 * 是否是点卡服务器
	 * 
	 * @return true 是点卡服务器, false是普通服务器
	 */
	public static boolean isPayService() {
		return fire.pb.fushi.Module.GetPayServiceType() == 1 ? true : false;
	}

	/**
	 * 检查是否有钱和信用值符合购买条件
	 * 
	 * @param roleId
	 * @param money
	 * @return
	 */
	public static boolean checkMoneyAndCredit(long roleId, int money) {
		if (money <= 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 价格不能小于或者等于0, money=").append(money);
			LOG.error(sbd.toString());
			return false;
		}
		// 买家
		Pack costRoleBag = new Pack(roleId, true);
		long gold = costRoleBag.getGold();
		long cereditPoint = costRoleBag.getCurrency(MoneyType.MoneyType_EreditPoint);
		if (gold - money < 0) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 购买道具金币不足！");
			LOG.error(sbd.toString());
			MessageMgr.sendMsgNotify(roleId, 160212, null);
			return false;
		}
		long costereditPoint = (long)(money*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_BUY_RARE));
		if (cereditPoint + costereditPoint < 0) {
			List<String> params = new ArrayList<String>();
			params.add("" +cereditPoint);
			params.add("" +(0-(money+costereditPoint)));
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 180021, params);	
			
			MessageMgr.sendSystemMessageToRole(roleId, 180026, null);
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleId).append(", 购买道具信用值不足！");
			LOG.error(sbd.toString());
			return false;
		}
		return true;
	}

	/**
	 * @return 检测上架非珍品道具价格是否在上下50%范围,返回true在范围内
	 */
	public static boolean checkUpPriceFloating(int itemId, int level, int money) {
		if (!isPayService()) {
			int price = FloatingMarketManager.getInstance().getPrice(itemId, level);
			if (price < 0 || money < (int) (price * 0.5) || money > (int) (price * 1.5)) {
				return false;
			}
		}
		return true;
	}

	public int getMarketThreePrice(MarketThreeTable itemConf, int nlv) {
		if (itemConf.ranges != null && itemConf.ranges.size() > 1 && nlv > 0) {
			if (itemConf.prices != null && itemConf.prices.size() >= (int) (nlv / 10))
				return itemConf.prices.get((int) (nlv / 10));
		} else if (itemConf.prices != null && itemConf.prices.size() > 0) {
			return itemConf.prices.get(0);
		}
		return 0;
	}
}
