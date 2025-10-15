
package fire.pb.school.change;

import mkdb.Procedure;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.RoleConfigManager;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemShuXing;
import fire.pb.item.gemItemShuXing;
import fire.pb.shop.srv.floating.FloatingOneManager;
import fire.pb.talk.MessageMgr;

public class PChangeGem extends Procedure {
	private final long roleId;
	private final int gemKey;
	private final int newGemItemId;

	public PChangeGem(long roleId, int gemKey, int newGemItemId) {
		super();
		this.roleId = roleId;
		this.gemKey = gemKey;
		this.newGemItemId = newGemItemId;
	}

	@Override
	protected boolean process() throws Exception {
		// 战斗状态下不能使用该功能
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}

		// 转职检测
		xbean.ChangeSchoolInfo changeSchoolInfo = xtable.Changeschool.get(roleId);
		if (null == changeSchoolInfo || changeSchoolInfo.getRecords().size() == 0) {
			return false;
		}

		// 转宝石次数判断
		int maxChangeGemCount = ChangeSchoolUtils.getMaxChangeGemCount();
		int hasChangeGemCount = changeSchoolInfo.getChangegemcount();
		if (hasChangeGemCount >= maxChangeGemCount) {
			return false;
		}

		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		ItemBase item = bag.getItem(gemKey);
		ItemShuXing newItemAttr = fire.pb.item.Module.getInstance().getItemManager().getAttr(newGemItemId);
		if (newItemAttr == null) {
			return false;
		}

		ItemShuXing oldItemAttr = item.getItemAttr();
		if (oldItemAttr == null) {
			return false;
		}

		// 检查是否可以转换
		if (!isCanTransGem(oldItemAttr, newItemAttr)) {
			return false;
		}

		int ret = bag.removeItemWithKey(gemKey, 1, YYLoggerTuJingEnum.tujing_Value_changeschoolgemcost, 0, "转职转宝石");
		if(0 == ret){
			return false;
		}
		
		ret = bag.doAddItem(newGemItemId, 1, "转职转宝石", YYLoggerTuJingEnum.tujing_Value_changeschoolgem, 0);
		if(0 == ret){
			return false;
		}
		
		// 退钱
		int returnMoney = returnBackMoney(oldItemAttr, newItemAttr);
		if(returnMoney > 0){
			bag.addSysMoney(returnMoney, "转宝石退钱", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_changeschoolgem, 0);
		}
		
		// 消耗次数
		changeSchoolInfo.setChangegemcount(hasChangeGemCount + 1);

		// 通知客户端转换宝石成功
		SChangeGem sendResult = new SChangeGem();
		psendWhileCommit(roleId, sendResult);

		return true;
	}

	/**
	 * 退钱判断并返回 yebin added @ 2016年8月16日 下午4:28:14
	 * 
	 * @param oldGemItemId
	 * @param newGemItemId
	 * @return
	 */
	private int returnBackMoney(ItemShuXing oldItemAttr, ItemShuXing newItemAttr) {
		int ret = 0;

		int srcOldGemGoodId = ChangeSchoolUtils.getSrcGem(((gemItemShuXing)oldItemAttr).getGemType());
		int srcNewGemGoodId = ChangeSchoolUtils.getSrcGem(((gemItemShuXing)newItemAttr).getGemType());
		if (srcOldGemGoodId == 0 || srcNewGemGoodId == 0) {
			return ret;
		}

		int currOldGemPrice = FloatingOneManager.getInstance().getCurrPrice(5, srcOldGemGoodId);
		int currNewGemPrice = FloatingOneManager.getInstance().getCurrPrice(5, srcNewGemGoodId);

		if(currOldGemPrice > currNewGemPrice){
			ret = (currOldGemPrice - currNewGemPrice) * (int)Math.pow(2, oldItemAttr.getLevel() - 1);
		}
		
		return ret;
	}

	/**
	 * 是否可以转换 yebin added @ 2016年8月16日 下午4:28:29
	 * 
	 * @param oldItemAttr
	 * @param newItemAttr
	 * @return
	 */
	private boolean isCanTransGem(ItemShuXing oldItemAttr, ItemShuXing newItemAttr) {
		if (!(oldItemAttr instanceof gemItemShuXing) || !(newItemAttr instanceof gemItemShuXing)) {
			return false;
		}
		
		int oldGemLv = oldItemAttr.getLevel();
		// 等级不同不予转换
		if (newItemAttr.getLevel() != oldGemLv) {
			return false;
		}

		// 转宝石限制宝石最低等级
		int limitGemLv = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(436).getValue());
		if (oldGemLv < limitGemLv) {
			return false;
		}

		// 类型不同不予转换
		if (newItemAttr.getTypeid() != oldItemAttr.getTypeid()) {
			return false;
		}

		return true;
	}
}
