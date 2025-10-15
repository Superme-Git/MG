package fire.pb.npc;

/**
 * 删除摇钱树
 */

import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

public class PRemoveYaoQian extends Procedure {

	private final long npckey;
	
	public PRemoveYaoQian(final long npckey) {
		this.npckey = npckey;
	}

	@Override
	protected boolean process() throws Exception {
		/*xbean.YaoQianShuInfo yaoqianInfo = xtable.Yaoqianshutables.get(npckey);
		if (yaoqianInfo == null)
			return false;
		
		//给植树者发奖励
		fire.pb.item.Pack ownerbag = new fire.pb.item.Pack(yaoqianInfo.getRoleid(), false);
		int lookids = yaoqianInfo.getLookroleids().size();*/
		
		Long yaoqianRoleid = xtable.Roleyaoqianshuinfos.get(npckey);
		if (yaoqianRoleid == null)
			return false;
		
		xbean.RoleYaoQianShuInfoMaps yaoqianMap = xtable.Roleyaoqianshutables.get(yaoqianRoleid);
		if (yaoqianMap == null) {
			return false;
		}
		
		xbean.RoleYaoQianShuInfo yaoqianInfo = yaoqianMap.getYaoqianshumaps().get(npckey);
		if (yaoqianInfo == null)
			return false;
		int lookids = yaoqianInfo.getLookroleids().size();
		fire.pb.item.Pack ownerbag = new fire.pb.item.Pack(yaoqianRoleid, false);
		
		SCommon commonMap = ConfigManager.getInstance().getConf(SCommon.class).get(338);
		if (lookids >= Integer.valueOf(commonMap.getValue())) {
			ownerbag.addSysMoney(5000000, "摇钱树", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_yaoqianshu, 0);
			MessageUtil.psendAddorRemoveMoney(yaoqianRoleid, 5000000);
		}
		else {
			long awardmoney = 1000000 + lookids * 10000;
			ownerbag.addSysMoney(awardmoney, "摇钱树", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_yaoqianshu, 0);
			MessageUtil.psendAddorRemoveMoney(yaoqianRoleid, awardmoney);
		}
		
		yaoqianMap.getYaoqianshumaps().remove(npckey);
		//删除npc
		xtable.Roleyaoqianshuinfos.remove(npckey);
		return true;
	}
	
}
