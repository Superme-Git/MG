package fire.pb.master.proc;

import fire.pb.PropRole;
import fire.pb.master.MasterManager;

/**
 * 领取拜师酒
 * @author nobody
 */
public class PTakeMasterWineProc extends mkdb.Procedure {
	
	private long roleId;
	
	public PTakeMasterWineProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		PropRole prole = new PropRole(roleId, true);
		if(prole.getLevel() >= MasterManager.APPRENT_MAX_LEVEL){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 146395 , null);
			return false;
		}
		
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		int number = bag.getItemNum(MasterManager.JIE_BAI_JIU_ITEEM_ID, 0);
		if(number >0){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 146396 , null);
			return false;
		}
		//发礼包
		if(bag.doAddItem(MasterManager.JIE_BAI_JIU_ITEEM_ID, 1, 0, 0, "结拜酒给予", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_baishi, MasterManager.JIE_BAI_JIU_ITEEM_ID) != 1){
			return false;
		}
		return true;
	}
}
