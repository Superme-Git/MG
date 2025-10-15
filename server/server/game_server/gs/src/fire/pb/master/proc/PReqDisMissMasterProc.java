package fire.pb.master.proc;

import fire.pb.master.SNotifyDismissMaster;
import fire.pb.util.DateValidate;

/**
 * 罢免师傅
 * @author nobody
 */
public class PReqDisMissMasterProc extends mkdb.Procedure{
	
	private final long apprenceId;
	
	public PReqDisMissMasterProc(long rid){
		this.apprenceId = rid;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.MenstorAndApprent apprent = xtable.Menstor.select(apprenceId);
		if(apprent == null || apprent.getFlag() != 1 || apprent.getMenstorid() <= 0){
			fire.pb.talk.MessageMgr.sendMsgNotify(apprenceId, 146403 , null);
			return false;
		}
		
		long mentorId = apprent.getMenstorid();
		xbean.Properties prop =  xtable.Properties.select(mentorId);
		if(prop != null){
			long currTime = System.currentTimeMillis();
			long lastOffLineTime = prop.getOfflinetime();
			long lastOnlineTime = prop.getOnlinetime();
			if(lastOffLineTime > lastOnlineTime){
				int offLineDays = DateValidate.getDaysBetween1(currTime,lastOffLineTime);
				if(offLineDays <7){
					SNotifyDismissMaster send = new SNotifyDismissMaster();
					send.mastername = prop.getRolename();
					mkdb.Procedure.psendWhileCommit(apprenceId, send);
				}else{
					mkdb.Procedure.pexecuteWhileCommit(new PDisMissMasterProc(apprenceId));
				}
			}else{
				SNotifyDismissMaster send = new SNotifyDismissMaster();
				send.mastername = prop.getRolename();
				mkdb.Procedure.psendWhileCommit(apprenceId, send);
			}
		}
		return true;
	}
}
