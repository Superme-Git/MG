package fire.pb.npc;

/**
 * 照顾摇钱树
 */
import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.map.SceneNpcManager;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

public class PLookYaoQian extends Procedure {

	private final long roleid;
	private final long npckey;
	
	public PLookYaoQian(final long roleid, final long npckey) {
		this.roleid = roleid;
		this.npckey = npckey;
	}
	
	@Override
	protected boolean process() throws Exception {
		/*xbean.YaoQianShuInfo yaoqianInfo = xtable.Yaoqianshutables.get(npckey);
		if (yaoqianInfo == null)
			return false;
		
		if (yaoqianInfo.getLookroleids().contains(Long.valueOf(roleid))) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 180025, null);
			return false;
		}
		
		yaoqianInfo.getLookroleids().add(Long.valueOf(roleid));*/
		
		
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
		
		if (yaoqianInfo.getLookroleids().contains(Long.valueOf(roleid))) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 180025, null);
			return false;
		}
		
		yaoqianInfo.getLookroleids().add(Long.valueOf(roleid));
		
		//给照顾者奖励
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
		bag.addSysMoney(20000, "摇钱树", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_yaoqianshu, 0);
		MessageUtil.psendAddorRemoveMoney(roleid, 20000);
		
		SCommon commonMap = ConfigManager.getInstance().getConf(SCommon.class).get(339);
		
		if (yaoqianInfo.getLookroleids().size() == Integer.parseInt(commonMap.getValue())) {
			//给植树者发奖励
			fire.pb.item.Pack ownerbag = new fire.pb.item.Pack(yaoqianRoleid, false);
			ownerbag.addSysMoney(5000000, "摇钱树", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_yaoqianshu, 0);
			MessageUtil.psendAddorRemoveMoney(yaoqianRoleid, 5000000);
			//删除npc
			SceneNpcManager.premoveNpcWhileCommit(npckey);
			
			yaoqianMap.getYaoqianshumaps().remove(npckey);
			
			xtable.Roleyaoqianshuinfos.remove(npckey);
		}
		
		return true;
	}
}
