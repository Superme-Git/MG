package fire.pb.game;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropConf;
import fire.pb.PropRole;
import fire.pb.map.SceneManager;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

public class POpenTraderBox extends Procedure {

	private final long roleid;
	
	public POpenTraderBox(final long roleid) {
		this.roleid = roleid;
	}
	
	@Override
	protected boolean process() throws Exception {
		xbean.TraderWheel traderWheel = xtable.Opentraderwheeltable.get(roleid);
		if (traderWheel == null)
			return false;
		
		WheelAwardItem awardItem = GameManager.getInstance().getAwardItem(traderWheel.getBoxtype(), traderWheel.getItemindex());
		if (awardItem == null)
			return false;
		
		int realAdd = BagUtil.addItem(roleid, awardItem.itemid, awardItem.itemnum, "TraderWheel", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_zhuanpan, awardItem.itemid);
		if (realAdd == awardItem.itemnum && awardItem.msg == 1) {
			MessageUtil.psendAddItemWhileCommit(roleid, awardItem.itemid, realAdd);
		}
		
		int mustAdd = 0;
		if (awardItem.mustitem > 0)
			mustAdd = BagUtil.addItem(roleid, awardItem.mustitem, awardItem.mustnum, "schoolwheel", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_rollschoolwheel, awardItem.mustitem);
		if (mustAdd > 0 && mustAdd == awardItem.mustnum) {
			MessageUtil.psendAddItemWhileCommit(roleid, awardItem.mustitem, mustAdd);
		}
		if (awardItem.itemid == PropConf.FuShi.GOLD_BOX_ID) {
			fire.pb.item.Pack bag2 = new fire.pb.item.Pack(roleid, false);
			bag2.addSysCurrency((long)(PropConf.FuShi.GOLD_BOX_NUM*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_OPENGOLDBOX))
					, MoneyType.MoneyType_EreditPoint, "schoolwheel", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_rollschoolwheel, 0);
		}
		if (awardItem.itemid == PropConf.FuShi.SILVER_BOX_ID) {
			fire.pb.item.Pack bag2 = new fire.pb.item.Pack(roleid, false);
			bag2.addSysCurrency((long)(PropConf.FuShi.SILVER_BOX_NUM*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_OPENSILVERBOX))
					, MoneyType.MoneyType_EreditPoint, "schoolwheel", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_rollschoolwheel, 0);
		}
		
		if (awardItem.notice > 0) {
			List<String> parameters = new ArrayList<String>();
			PropRole pRole = new PropRole(roleid, true);
			parameters.add(pRole.getName());
			parameters.addAll(MessageUtil.getItemMsgParas(awardItem.itemid, awardItem.itemnum));
			STransChatMessageNotify2Client ssmn = MessageMgr.getMsgNotify(
					awardItem.notice, 0, parameters);
			SceneManager.sendAll(ssmn);
		}
		
		xtable.Opentraderwheeltable.remove(roleid);
		return true;
	}

}
