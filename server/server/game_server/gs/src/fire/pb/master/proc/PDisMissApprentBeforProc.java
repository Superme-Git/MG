package fire.pb.master.proc;

import fire.pb.master.PrenticeData;
import fire.pb.master.SDismissApprentces;
import fire.pb.util.DateValidate;

/**
 * 师傅要罢免徒弟   通知师傅徒弟信息
 * @author nobody
 */
public class PDisMissApprentBeforProc extends mkdb.Procedure{

	private long mentorId;
	
	public PDisMissApprentBeforProc(long rid){
		this.mentorId = rid;
	}

	@Override
	protected boolean process() throws Exception {
		
		xbean.MenstorInfo menstor = xtable.Apprents.select(mentorId);
		if(menstor == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(mentorId, 146397 , null);
			return false;
		}
		
		xbean.MenstorAndApprent master = xtable.Menstor.select(mentorId);
		if(master == null || master.getFlag() != 2){
			fire.pb.talk.MessageMgr.sendMsgNotify(mentorId, 146397 , null);
			return false;
		}
		
		long currTime = System.currentTimeMillis();
		SDismissApprentces send = new SDismissApprentces();
		for(xbean.CurrApprent curr : menstor.getCurrapprentices()){
			long rid = curr.getApprentinfo().getRoleid();
			PrenticeData pd = new PrenticeData();
			pd.roleid = rid;
			pd.rolename = curr.getApprentinfo().getName();
			
			xbean.Properties prop =  xtable.Properties.select(rid);
			if(prop != null){
				long lastOffLineTime = prop.getOfflinetime();
				long lastOnlineTime = prop.getOnlinetime();
				if(lastOffLineTime > lastOnlineTime){
					int offLineDays = DateValidate.getDaysBetween1(currTime,lastOffLineTime);
					if(offLineDays >=7){
						pd.flag = 1;
					}
				}
			}
			send.prenticelist.add(pd);
		}
		mkdb.Procedure.psendWhileCommit(mentorId, send);
		return true;
	}
}
