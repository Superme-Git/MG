
package fire.pb;


import fire.log.LogManager;
import mkdb.Procedure;

import com.locojoy.base.Octets;

public class PUnForbidUser extends Procedure {

	private final int dstuserid;

	private final int gmuserid;
	
	private final int gmlocalsid;
	
	private final Octets reason;
	
	private final boolean auForbid;

	public PUnForbidUser(int dstuserid, int gmuserid,int gmlocalsid,Octets reason,boolean auForbid) {

		super();
		this.dstuserid = dstuserid;
		this.gmuserid = gmuserid;
		this.gmlocalsid = gmlocalsid;
		this.reason = reason;
		this.auForbid = auForbid;
	}

	@Override
	protected boolean process() throws Exception {
		if (auForbid){ 
		gnet.GMKickoutUser send = new gnet.GMKickoutUser(gmuserid, gmlocalsid, dstuserid, 1, reason);
		gnet.DeliveryManager.getInstance().send(send);
		}
		xbean.UserPunish userpunish = xtable.Userpunish.get(dstuserid);
		if (userpunish == null) {
			userpunish = xbean.Pod.newUserPunish();
			xtable.Userpunish.insert(dstuserid, userpunish);
		}
		xbean.PunishRecord record = xbean.Pod.newPunishRecord();
		record.setGmuserid(gmuserid);
		record.setForbidtime(0);
		record.setOptime(System.currentTimeMillis());
		record.setReason(reason.getString("UTF-16LE"));
		record.setRoleid(0);
		record.setType(xbean.PunishRecord.TYPE_UNFORBID_LOGIN);
		record.setUserid(dstuserid);
		userpunish.getRecords().add(record);
		//if (gmuserid == GACDManager.GM_ID) GM也可以解答错题的封禁
		   userpunish.setReleasetime(0);

		try {
			LogManager.logger.info("unforbid user.gmuserid:"+gmuserid+"userid:"+dstuserid+reason.getString("UTF-16LE"));
		} catch (Exception e) {
			LogManager.logger.error(e);
		}

		return true;
	}

}
