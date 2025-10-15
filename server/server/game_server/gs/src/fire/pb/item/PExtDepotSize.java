package fire.pb.item;

import java.util.Map;

import fire.pb.PropRole;
import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.product.SErrorCode;
import mkdb.Procedure;

public class PExtDepotSize extends mkdb.Procedure {

	private final long roleId;

	public PExtDepotSize(long roleId) {
		super();
		this.roleId = roleId;
	}

	@Override
	protected boolean process() {
		// 获得用户id
		PropRole pRole = new PropRole(roleId, true);
		if (pRole.getLevel() < Commontext.USE_DEPOT_LEVEL) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 145948, null);
			return false;
		}

		final ItemMaps bag = Module.getInstance().getItemMaps(roleId, BagTypes.DEPOT, false);
		int capacity = bag.getCapacity();

		Map<Integer, SCommon> Commons = ConfigManager.getInstance().getConf(SCommon.class);
		if (Commons == null) {
			return false;
		}

		SCommon common = Commons.get(Commontext.MAX_DEPOT_SIZE);

		int maxCapacity = Module.getInstance().getMaxBagCapacity(roleId, BagTypes.DEPOT);

		// 对个数进行判断
		if (capacity < Commontext.MIN_DEPOT_SIZE || capacity >= maxCapacity) {
			return false;
		}

		common = Commons.get(Commontext.ADD_DEPOT_SIZE);

		int canAddCapacity = Integer.parseInt(common.getValue());

		int addcount = canAddCapacity;
		if (capacity >= maxCapacity - canAddCapacity && capacity < maxCapacity)
			addcount = maxCapacity - capacity;

		//减去vip送的仓库格子
		int viplevel = xtable.Properties.selectViplevel(roleId);
		if (viplevel > 0) {
			fire.pb.fushi.SVipInfoConfig vipconfig = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.fushi.SVipInfoConfig.class).get(viplevel);
			if (vipconfig == null)
				return false;
			
			capacity -= vipconfig.getDepotsize();
		}
		
		ItemMgrImp im = (ItemMgrImp) Module.getInstance().getItemManager();
		int yinbiCount = im.getBuyDepotMoney(capacity);
		if (yinbiCount <= 0) {
			return false;
		}

		// 检查钱是否够
		Pack rolebag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(
				roleId, fire.pb.item.BagTypes.BAG, false);

		final SErrorCode sErrorCode = new SErrorCode();
		if (yinbiCount > rolebag.getMoney()) {
			sErrorCode.errorcode = fire.pb.product.Commontext.MONEY_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}
		// 扣钱
		if (rolebag.subMoney(-yinbiCount,
				fire.pb.product.Commontext.REASON_STRING,
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_buydepot, 0) != -yinbiCount) {
			sErrorCode.errorcode = fire.pb.product.Commontext.MONEY_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}

		bag.addCapacity(addcount); // 增加格子
		SRefreshPackSize send = new SRefreshPackSize();
		send.packid = BagTypes.DEPOT;
		send.cap = bag.getCapacity();
		mkdb.Procedure.psendWhileCommit(roleId, send);
		return true;
	}

}
