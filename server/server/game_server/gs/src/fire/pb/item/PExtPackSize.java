package fire.pb.item;

import fire.pb.product.SErrorCode;
import mkdb.Procedure;

public class PExtPackSize extends Procedure {

	private final long roleId;

	public PExtPackSize(final long roleId) {
		this.roleId = roleId;
	}

	@Override
	protected boolean process() {

		final ItemMaps bag = Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		int capacity = bag.getCapacity();

		int maxCapacity = Module.getInstance().getMaxBagCapacity(roleId, BagTypes.BAG);

		// 对个数进行判断
		if (capacity < Commontext.MIN_BAG_SIZE || capacity >= maxCapacity) {
			return false;
		}

		int addcount = Commontext.ADD_CAPACITY_SIZE;
		
		//减去vip送的背包格式数量
		int viplevel = xtable.Properties.selectViplevel(roleId);
		if (viplevel > 0) {
			fire.pb.fushi.SVipInfoConfig vipconfig = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.fushi.SVipInfoConfig.class).get(viplevel);
			if (vipconfig == null)
				return false;
			
			capacity -= vipconfig.getBagsize();
		}
		
		ItemMgrImp im = (ItemMgrImp) Module.getInstance().getItemManager();
		int yinbiCount = im.getBuyPackYinBi(capacity);
		if (yinbiCount <= 0) {
			return false;
		}

		// 检查钱是否够
		Pack rolebag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(roleId, fire.pb.item.BagTypes.BAG, false);

		final SErrorCode sErrorCode = new SErrorCode();
		if (yinbiCount > rolebag.getMoney()) {
			sErrorCode.errorcode = fire.pb.product.Commontext.MONEY_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}
		// 扣钱
		if (rolebag.subMoney(-yinbiCount,
				fire.pb.product.Commontext.REASON_STRING,
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_buypack, 0) != -yinbiCount) {
			sErrorCode.errorcode = fire.pb.product.Commontext.MONEY_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}

		bag.addCapacity(addcount); // 增加格子
		SRefreshPackSize send = new SRefreshPackSize();
		send.packid = BagTypes.BAG;
		send.cap = bag.getCapacity();
		mkdb.Procedure.psendWhileCommit(roleId, send);
		return true;
	}

}
