package fire.pb.hook;

import java.util.Map;

import org.apache.log4j.Logger;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.common.SCommon;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.item.DPGiveItem;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import xbean.HookData;

public class PBuyDpointProc extends mkdb.Procedure {

	final private long roleId;
	final private int userId;
	

	final private static Logger logger = Logger.getLogger("SYSTEM");

	public PBuyDpointProc(long roleId, int userId) {
		this.roleId = roleId;
		this.userId = userId;
	}

	@Override
	protected boolean process() throws Exception {
		
		/**
		 * 0, 符石; 1, 金币
		 */
		int moneytype = fire.pb.fushi.Module.GetPayServiceType();

		HookData hookData = xtable.Rolehook.select(roleId);
		short canGetDpoint = hookData.getCangetdpoint();
		int itemId = 0;
		int needFuShi = 0;

		if (canGetDpoint > 0) {
			// 直接返回
			logger.info("PBuyDpointProc roleid:" + roleId + " canGetDpoint:" + canGetDpoint);
			return false;
		}

		Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);
		if (commonMap == null) {
			logger.error("PBuyDpointProc roleid:" + roleId + " SCommon error!");
			return false;
		}

		SCommon sCommonItemId = commonMap.get(265);
		if (sCommonItemId == null) {
			logger.error("PBuyDpointProc roleid:" + roleId + " SCommon ItemId error!");
			return false;
		}
		itemId = Integer.parseInt(sCommonItemId.getValue());

		if (moneytype == 0) { //扣符石
			SCommon sCommonNeedYB = commonMap.get(266);
			if (sCommonNeedYB == null) {
				logger.error("PBuyDpointProc roleid:" + roleId + " SCommon fushi error!");
				return false;
			}
			needFuShi = Integer.parseInt(sCommonNeedYB.getValue());

			/*// 获取符石
			xbean.YbNums ybNums = xtable.Fushinum.get(userId);
			if (ybNums == null) {
				logger.error("PBuyDpointProc roleid:" + roleId + " ybNums error!");
				return false;
			}
			xbean.YbNum ybNum = null;
			ybNum = ybNums.getRoleyb().get(roleId);
			if (ybNum == null) {
				logger.error("PBuyDpointProc roleid:" + roleId + " ybNum error!");
				return false;
			}

			if (ybNum.getNum() < 0 || ybNum.getSysnum() < 0) {
				logger.info("PBuyDpointProc roleid:" + roleId + " ybNum not enough!");
				MessageMgr.sendMsgNotify(roleId, 162032, null);
				return false;
			}

			if (ybNum.getNum() < needFuShi) {
				logger.info("PBuyDpointProc roleid:" + roleId + " ybNum not enough!");
				MessageMgr.sendMsgNotify(roleId, 162032, null);
				return false;
			}*/
			if (!FushiManager.subFushiFromUser(userId, roleId, needFuShi, itemId, 1, FushiConst.REASON_BUY_ITEM,
					YYLoggerTuJingEnum.tujing_Value_goumai, true))
				return false;
			
			if (!new DPGiveItem(roleId, itemId, 1).call()) {
				logger.info("PBuyDpointProc roleid:" + roleId + " AbPGiveItem error! itemId:" + itemId);
				return false;
			}
		}
		else if (moneytype == 1) { //扣金币
			fire.pb.fushi.SCommonDayPay daypay = ConfigManager.getInstance().getConf(fire.pb.fushi.SCommonDayPay.class).get(4);
			Long subgold = Long.valueOf(daypay.getServerdata());
			Pack rolepack = new Pack(roleId, false);
			if (-subgold != rolepack.subGold(-subgold, "购买双倍点数", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_goumai, 0)) {
				MessageMgr.sendMsgNotify(roleId, 160118, null);
				return false;
			}
			
			if (!new DPGiveItem(roleId, itemId, 1).call()) {
				logger.info("PBuyDpointProc roleid:" + roleId + " AbPGiveItem error! itemId:" + itemId);
				return false;
			}
		}

		RoleHookManager.getInstance().refreshHookExpdata(roleId, true);

		return true;
	}
}
