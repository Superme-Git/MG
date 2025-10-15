package fire.pb.friends;

import mkdb.Procedure;
import fire.pb.RoleConfigManager;
import fire.pb.common.SCommon;
import fire.pb.game.MoneyType;
import fire.pb.util.MessageUtil;


public class PSetSpaceGift extends Procedure {

	private final long roleId;
	private final int giftNum;

	public PSetSpaceGift(long roleId, int giftNum) {
		this.roleId = roleId;
		this.giftNum = giftNum;
	}

	@Override
	protected boolean process() {
		SSetSpaceGift send = new SSetSpaceGift();
		send.result = 0;
		xbean.RoleSpace rs = xtable.Rolespaces.get(roleId);
		if(rs == null) {
			send.result = -1;
			psendWhileRollback(roleId, send);
			return false;
		}
		final SCommon cc = RoleConfigManager.getRoleCommonConfig(331);
		int maxnum = Integer.parseInt(cc.getValue());
		if((rs.getGift() + giftNum) > maxnum) {
			Module.logger.error("角色[" + roleId + "]放置空间礼物超过最大数量:" + maxnum + ",当前数量:" + rs.getGift() + ",放置数量:" + giftNum);
			send.result = -1;
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 162147, null);
			psendWhileRollback(roleId, send);
			return false;
		}	
		final SCommon cc1 = RoleConfigManager.getRoleCommonConfig(330);
		int cost = Integer.parseInt(cc1.getValue());
		//扣钱放礼物
		int totalcost = cost * giftNum;
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		long ret = bag.subMoney(-totalcost, "放置空间礼物", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_itemuse, 0);
		if(ret == -totalcost){
			MessageUtil.pSendAddOrRemoveCurrency(roleId, ret, MoneyType.MoneyType_SilverCoin);
		} else {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 120025, null);
			Module.logger.error("角色[" + roleId + "]放置空间礼物:subMoney error!ret=" + ret + ",totalcost=" + totalcost);
			send.result = -1;
			psendWhileRollback(roleId, send);
			return false;
		}
		rs.setGift(rs.getGift() + giftNum);
		psendWhileCommit(roleId, send);
		return true;
	}
	
}
