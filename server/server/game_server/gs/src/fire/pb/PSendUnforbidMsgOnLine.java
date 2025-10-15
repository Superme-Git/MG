

package fire.pb;

import fire.pb.talk.MessageMgr;
import mkdb.Procedure;


public class PSendUnforbidMsgOnLine extends Procedure {

	private final int userid;
	private final long roleid;
	public PSendUnforbidMsgOnLine(int userid, long roleid) {

		super();
		this.userid = userid;
		this.roleid = roleid;
	}
	@Override
	protected boolean process() throws Exception {

		xbean.UserPunish userPunish = xtable.Userpunish.get(userid);
		
		if (userPunish!=null){
			    if (userPunish.getSendmsgtime()==0){
			    	userPunish.setSendmsgtime(System.currentTimeMillis());
			    	int msgid = 0;
			    	if (userPunish.getWaiguatimes() == 1){
			    		msgid = 143947;
			    	}else if (userPunish.getWaiguatimes() == 2) {
						msgid = 143948;
					}else if (userPunish.getWaiguatimes() == 3) {
						msgid = 144346;
					}
			    	if (msgid > 0) {
			    		MessageMgr.psendSystemMessageToRole(roleid, msgid,null);
			    	}
			    }
				return true;
		}
		return true;
	}
	
}

