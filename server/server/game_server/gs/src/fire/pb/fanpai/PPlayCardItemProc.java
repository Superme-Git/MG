package fire.pb.fanpai;

import fire.pb.PAddExpProc;
import fire.pb.util.BagUtil;
import mkdb.Procedure;

public class PPlayCardItemProc extends Procedure {

	private final long roleId;

	public PPlayCardItemProc(long roleId) {
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		boolean succ = false;
		xbean.RollCardInfo info = xtable.Rolerollcard.get(roleId);
		if (info == null) {
			return false;
		}

		if (info.getTakeflag() == 1) {
			return false;
		}

		int index = info.getIndex();
		if (index >= info.getWheelitems().size()) {
			RollCardManager.logger.info("Exception:RollCard  奖励下标大于牌的数量 index="
					+ index + "     awardId = " + info.getServiceid()
					+ "     roleId=" + roleId);
			return false;
		}

		xbean.WheelItem item = info.getWheelitems().get(index);
		if (item == null) {
			RollCardManager.logger.info("Exception:RollCard  item=null roleId="
					+ roleId);
			return false;
		}

		int type = item.getItemtype();

		// 给物品
		if (type == 1 && giveItem(info, item)) {
			succ = true;
		}

		// 给经验
		if (type == 2 && giveExp(info, item)) {
			succ = true;
		}

		// 给银
		if (type == 3 && giveMoney(info, item)) {
			succ = true;
		}

		if (succ) {
			info.setTakeflag(1);
		}

		return succ;
	}

	private boolean giveItem(xbean.RollCardInfo info, xbean.WheelItem item) {
		int rt = 0;
		if (item.getBind() == 0) {
			rt = BagUtil.addItem(roleId, item.getItemid(), item.getNum(),
					"翻牌奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fanpaijiangli,
					info.getServiceid());
		} else {
			rt = BagUtil.addBindItem(roleId, item.getItemid(), item.getNum(),
					"翻牌奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fanpaijiangli,
					info.getServiceid(), true);
		}

		if (rt != item.getNum()) {
			return false;
		}

		return true;
	}

	private boolean giveExp(xbean.RollCardInfo info, xbean.WheelItem item) {

		int baseExp = info.getBaseexp();
		int currentExp = item.getNum();
		int times = item.getTimes();

		int exp = 0;
		if (currentExp > 0) {
			exp = currentExp;
		} else if (baseExp > 0) {
			exp = baseExp;
			if (times > 0) {
				exp = exp * times;
			}
		}
		return new PAddExpProc(roleId, exp, true, PAddExpProc.OTHER, "翻牌经验奖励").call();
	}

	private boolean giveMoney(xbean.RollCardInfo info, xbean.WheelItem item) {
		boolean succ = false;
		long baseMoney = info.getBasemoney();
		long currentMoney = item.getNum();
		int times = item.getTimes();

		long money = 0;
		if (currentMoney > 0) {
			money = currentMoney;
		} else if (baseMoney > 0) {
			money = baseMoney;
			if (times > 0) {
				money = money * times;
			}
		}

		if (money > 0) {
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
			long add = bag.addSysMoney(money, "翻牌银两",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_zhuanpan,
					info.getServiceid());
			if (add == money) {
				succ = true;
			}
		}

		return succ;
	}
}
