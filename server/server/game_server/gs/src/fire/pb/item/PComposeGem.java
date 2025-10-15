package fire.pb.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fire.log.LogManager;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.main.ConfigManager;
import fire.pb.shop.SGoods;
import fire.pb.shop.srv.BuyShopFactory;
import fire.pb.talk.MessageMgr;
import fire.pb.util.Misc;
import mkdb.Procedure;

public class PComposeGem extends Procedure {
	private static Map<Integer, SEquipHeCheng> combineConfs = null;
	static {
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) {
			Map<Integer, DSEquipHeCheng> dSEquipHeCheng = ConfigManager.getInstance().getConf(DSEquipHeCheng.class);
			combineConfs = new TreeMap<>(dSEquipHeCheng);
		} else {
			combineConfs = ConfigManager.getInstance().getConf(SEquipHeCheng.class);
		}
	}

	private final long roleId;
	private final byte bUseRongHeJi; // 是否使用融合剂 0:不使用 1:使用
	private final int targetGemItemId; // 合成宝石的ID
	private final ArrayList<ComposeGemInfoBean> bagGems; // 使用的背包宝石
	private final ArrayList<ComposeGemInfoBean> shopGems; // 使用的商店宝石

	public PComposeGem(long roleId, byte bUseRongHeJi, int targetGemItemId, ArrayList<ComposeGemInfoBean> bagGems, ArrayList<ComposeGemInfoBean> shopGems) {
		this.roleId = roleId;
		this.bUseRongHeJi = bUseRongHeJi;
		this.targetGemItemId = targetGemItemId;
		this.bagGems = bagGems;
		this.shopGems = shopGems;
	}

	@Override
	protected boolean process() throws Exception {
		ItemShuXing targetGemItemAttr = Module.getInstance().getItemManager().getAttr(targetGemItemId);
		if (null == targetGemItemAttr || !(targetGemItemAttr instanceof gemItemShuXing)) {
			MessageMgr.psendMsgNotify(roleId, 150167, null);
			return false;
		}

		SEquipHeCheng sEquipCombin = combineConfs.get(targetGemItemId);
		if (sEquipCombin == null) {
			MessageMgr.psendMsgNotify(roleId, 150167, null);
			return false;
		}

		int exp = 0;
		int needExp = calcGemExp((gemItemShuXing) targetGemItemAttr, 1);
		Pack bag = new fire.pb.item.Pack(roleId, false);
		
		// 背包宝石使用
		for (ComposeGemInfoBean infoBean : bagGems) {
			int itemId = infoBean.itemidorgoodid;
			int num = infoBean.num;
			int remainNum = bag.getBagItemNum(itemId);
			if (remainNum < num) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}

			ItemShuXing itemAttr = Module.getInstance().getItemManager().getAttr(itemId);
			if (itemAttr == null || !(itemAttr instanceof gemItemShuXing)) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}

			if(!isCanUseForAddExp((gemItemShuXing)targetGemItemAttr, (gemItemShuXing)itemAttr)){
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}
			
			// 扣除
			int ret = bag.removeItemById(itemId, num, YYLoggerTuJingEnum.tujing_Value_composegemcost, 0, "宝石合成");
			if (ret == num) {
				exp += calcGemExp((gemItemShuXing) itemAttr, ret);
			} else {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}
		}

		// 商店宝石使用
		for (ComposeGemInfoBean infoBean : shopGems) {
			int goodId = infoBean.itemidorgoodid;
			int num = infoBean.num;
			SGoods goods = fire.pb.shop.Module.sGoodsMap.get(goodId);
			if (null == goods) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}

			ItemShuXing itemAttr = Module.getInstance().getItemManager().getAttr(goods.getItemId());
			if (null == itemAttr || !(itemAttr instanceof gemItemShuXing)) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}

			// 判断是否可用来加经验
			if(!isCanUseForAddExp((gemItemShuXing)targetGemItemAttr, (gemItemShuXing)itemAttr)){
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}
			
			// 购买逻辑
			fire.pb.shop.utils.ShopParameters params = new fire.pb.shop.utils.ShopParameters();
			params.role = new fire.pb.PropRole(roleId, true);
			params.goods = goods;
			params.num = num;
			params.shopId = 5; // 商会
			
			BuyShopFactory buyProc = new fire.pb.shop.srv.BuyShopFactory(params, 1); // 普通购买
			if (!buyProc.call()) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}
			
			// 扣除
			int ret = bag.removeItemById(goods.getItemId(), num, YYLoggerTuJingEnum.tujing_Value_composegemcost, 0, "宝石合成");
			if (ret == num) {
				exp += calcGemExp((gemItemShuXing) itemAttr, ret);
			} else {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}
		}

		// 经验判断
		if (exp < needExp) {
			MessageMgr.psendMsgNotify(roleId, 150167, null);
			return false;
		}

		// 合成成功率
		int rate = sEquipCombin.hechengrate;
		
		// 融合剂逻辑处理
		if (bUseRongHeJi == 1) {
			if (sEquipCombin.getHammerid() < 0) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}
			if (sEquipCombin.getHammernum() < 0) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}

			int decHammerNum = sEquipCombin.getHammernum();
			int rmvHammmer = bag.removeItemById(sEquipCombin.getHammerid(), decHammerNum, YYLoggerTuJingEnum.tujing_Value_hecheng, sEquipCombin.getHammerid(), "道具合成");
			if (rmvHammmer != decHammerNum) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}

			// 添加合成机率
			rate = sEquipCombin.getHammerrate();
		}

		// 检查背包状态
		if (bag.isFull()) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 144875, 0, null);
			return false;
		}

		// 若不成功, 处理返还
		if (!Misc.checkRatePercent(rate)) {
			bag.doAddItem(sEquipCombin.hechengfailreturn, sEquipCombin.failreturnnum, "道具合成失败返还", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng, sEquipCombin.nextid);

			SHeChengItem combineItemResult = new SHeChengItem(0, 0);
			psendWhileCommit(roleId, combineItemResult);

			List<String> param = new ArrayList<String>();
			ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(targetGemItemId);
			if (iAttr == null) {
				return true;
			}

			param.add(iAttr.getName());
			param.add(String.valueOf(sEquipCombin.failreturnnum));
			MessageMgr.psendMsgNotify(roleId, 160114, param);

			return true;
		}

		// 成功,添加物品
		bag.doAddItem(targetGemItemId, 1, "宝石合成", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_composegem, targetGemItemId);

		// 发送事件通知
		fire.pb.event.Poster.getPoster().dispatchEvent(new CombineItemEvent(roleId));

		// 发送协议
		SHeChengItem sCombineItem = new SHeChengItem(1, targetGemItemId);
		psendWhileCommit(roleId, sCombineItem);

		// 发送提示消息
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 150168, 0, null);

		return true;
	}

	/**
	 * 判断宝石是否可以被用来增加经验
	 * yebin added @ 2016年8月17日 上午2:22:48
	 * @param targetGemItemAttr
	 * @param gemItemAttr
	 * @return
	 */
	private final boolean isCanUseForAddExp(gemItemShuXing targetGemItemAttr, gemItemShuXing gemItemAttr) {
		if(targetGemItemAttr.getGemType() == 0 || gemItemAttr.getGemType() == 0){
			LogManager.logger.error("策划配置表错误,错误的宝石类型ItemId=" + targetGemItemAttr.getId());
			return false;
		}
		return targetGemItemAttr.getGemType() == gemItemAttr.getGemType();
	}
	
	/**
	 * 计算宝石经验
	 * yebin added @ 2016年8月17日 上午2:22:36
	 * @param gemItemAttr
	 * @param num
	 * @return
	 */
	private final int calcGemExp(gemItemShuXing gemItemAttr, int num) {
		if (gemItemAttr.getLevel() <= 0) {
			LogManager.logger.error("宝石合成错误,合成宝石等级不正确ItemId=" + gemItemAttr.getId());
			return 0;
		}
		return (int) Math.pow(2, gemItemAttr.getLevel() - 1) * num;
	}
}
