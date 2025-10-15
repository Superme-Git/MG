package fire.pb.item;

import java.util.Map;

import mkdb.Procedure;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.talk.MessageMgr;

/**
 * 道具找回
 * @author XGM
 */
public class PItemRecover extends Procedure {

	public final long roleId;
	public final long uniqId; // 唯一id

	public PItemRecover(long roleId, long uniqId) {
		this.roleId = roleId;
		this.uniqId = uniqId;
	}

	@Override
	protected boolean process() {

		// 获得可回收uniqId列表
		xbean.Itemrecoverlist itemRecoverList = xtable.Itemrecover.get(roleId);
		if (itemRecoverList == null) {
			return false;
		}

		// 是否存在可回收的uniqId
		if (itemRecoverList.getUniqids().contains(uniqId) == false) {
			return false;
		}

		// 获得丢弃的道具
		xbean.DiscardItem ditem = xtable.Itemrecyclebin.get(uniqId);
		if (ditem == null) {
			return false;
		}

		// 获得道具配置
		ItemShuXing itemAttrConf = Module.getInstance().getItemManager().getAttr(ditem.getItem().getId());
		if (itemAttrConf == null) {
			return false;
		}

		// 金币消耗
		int cost = itemAttrConf.getRecovercost() * -1;
		Pack bag = new Pack(roleId, false);
		if (cost != bag.subGold(cost, "道具找回", YYLoggerTuJingEnum.tujing_Value_itemrecovercost, 0)) {
			return false;
		}

		xbean.Item xitem = ditem.getItem().copy();
		ItemBase bi = Module.getInstance().getItemManager().toItemBase(xitem);
		if (bi == null) {
			return false;
		}

		bi.getDataItem().getNumbermap().clear();
		for (Map.Entry<Integer, Integer> e : xitem.getNumbermap().entrySet()) {
			bi.getDataItem().getNumbermap().put(e.getKey(), e.getValue());
		}
		bi.getDataItem().setNumber(xitem.getNumber());

		// 进背包
		AddItemResult result = bag.doAddItem(bi, -1, "找回道具", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_itemrecover, xitem.getId());
		if (result != AddItemResult.SUCC) {
			// 背包空间不足
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 144965, null);
			return false;
		}

		// recycle bin delete
		xtable.Itemrecyclebin.delete(uniqId);

		// remove uniqId
		itemRecoverList.getUniqids().remove(Long.valueOf(uniqId));

		// 通知客户端
		SItemRecover send = new SItemRecover();
		send.itemid = bi.getItemId();
		send.uniqid = uniqId;
		mkdb.Procedure.psendWhileCommit(roleId, send);

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PItemRecover] roleId:" + roleId
					+ " itemId:" + bi.getItemId()
					+ " uniqId:" + uniqId);
		}
		return true;
	}
}
