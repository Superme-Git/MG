package fire.pb.activity.impexam;

import fire.log.Logger;

import fire.pb.npc.ImpExamType;
import fire.pb.npc.SQueryImpExamState;


/**
 * 查询是否可以参加智力试练决赛
 * @author cn
 */
public class PQueryImpExamStateProc extends mkdb.Procedure{
	
	private final long roleId;
	private static Logger logger = Logger.getLogger("SYSTEM");
	
	public PQueryImpExamStateProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		
		//检测一下当前是否在活动范围内
		boolean isIe = ImpExamManager.getInstance().isImpExamTime(ImpExamType.IMPEXAM_STATE);
		if(!isIe){
			sendQueryIEState(roleId,0);
			return false;
		}
		
		xbean.ImpExamStateRecord iesRecord = null;
		
		iesRecord = xtable.Impexamstatetbl.get(1);
		if(iesRecord == null){
			sendQueryIEState(roleId,0);
			return false;
		}
		
		if(!iesRecord.getIdslist().contains(roleId)){
			sendQueryIEState(roleId,0);
			return false;
		}
		
		xbean.ImpExamRecord record = xtable.Role2impexam.get(roleId);
		if(record == null){
			sendQueryIEState(roleId,0);
			return false;
		}
		
		int flag = record.getFlag();
		if(flag > 1){
			sendQueryIEState(roleId,0);
			return false;
		}

		sendQueryIEState(roleId,1);
		
		logger.info("PQueryImpExamStateProc roleid:" + roleId);
		
		return true;
	}
	
	private void sendQueryIEState(long roleId, int isAttend){
		SQueryImpExamState msg = new SQueryImpExamState();
		msg.isattend = (byte)isAttend;
		gnet.link.Onlines.getInstance().send(roleId, msg);
	}

}
