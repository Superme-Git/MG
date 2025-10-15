package fire.pb.fanpai;

import java.util.List;

import fire.pb.activity.award.RewardMgr;
import fire.pb.map.SActivityAwardItems;
import fire.pb.npc.SReqFortuneWheel;
import fire.pb.util.Misc;
import mkdb.Procedure;


/**
 * @author Administrator
 *
 */
public class PReqCardProc extends Procedure {

	private final long roleId;
	private final int realIndex;
	private final int cardCount;

	private int baseMoney;
	private int baseSMoney;
	private int baseExp;

	public PReqCardProc(long roleId, int realIndex, int cardCount) {
		this.roleId = roleId;
		this.realIndex = realIndex;
		this.cardCount = cardCount;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.RollCardInfo info = xtable.Rolerollcard.get(roleId);
		List<CardItem> items = RollCardManager.getInstance().choseCard(realIndex,
				cardCount);
		if (items == null) {
			RollCardManager.logger.info("Exception:RollCard realIndex="
					+ realIndex + " cardCount=" + cardCount);
			return false;
		}
		int index = RollCardManager.getInstance().getSelectIndex(items);
		if (index >= items.size()) {
			return false;
		}
		if (info == null) {
			info = xbean.Pod.newRollCardInfo();
			xtable.Rolerollcard.insert(roleId, info);
		} else {
			RollCardManager.getInstance().clearLastCard(info);
		}
		
		//添加对物品类型库的处理
		for (CardItem item : items) {
			if (item.getType() == 5) { //物品类型库类型
				int itemid = getItemId(item.getItemId()); //item.getItemId() 里放的是物品类型库里的id
				if (itemid > 0) {
					item.setItemId(itemid);
				}
				//设置为物品
				item.setType(1);
			}
		}

		info.setBaseexp(baseExp);
		info.setBasemoney(baseMoney);
		info.setBasesmoney(baseSMoney);
		info.setIndex(index);
		info.setServiceid(realIndex);
		info.setTakeflag(0);
		info.getWheelitems().addAll(
				RollCardManager.getInstance().toWheelItemList(items));

		// 通知客户端翻牌界面
		SReqFortuneWheel send = new SReqFortuneWheel();
		send.flag = 1;
		send.index = index;
		send.itemids = RollCardManager.getInstance().tiForturneWheelType(items);
		psendWhileCommit(roleId, send);

		return true;
	}
	
	private int getItemId(final int nIndex) {
		SActivityAwardItems items = RewardMgr.getInstance().getAward2Map().get(nIndex);
		if (items == null) {
			return -1;
		}
		
		List<Integer> itemList = items.getItems();
//		int index = Misc.getRandomBetween(0, itemList.size() - 1);
		int index = Misc.getProbability(items.itemsrate);
		
		return itemList.get(index);
	}

}
