
package fire.pb;

import gnet.link.Onlines;
import mkdb.Procedure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import fire.log.LogManager;
import fire.log.Logger;


public class PForbidUser extends Procedure {
	
	private static Map<Integer, Integer>extraProbMap = new ConcurrentHashMap<Integer, Integer>();
	
	private final int dstuserid;

	private final int gmuserid;

	@SuppressWarnings("unused")
	private final int gmlocalsid;

	private int forbid_time;// min

	private String reason;
	
	private int kickoutReason;

	private final boolean auForbid; // true 为发给au封禁,否则gs封禁
	
	static Logger logger = Logger.getLogger("SYSTEM");

	public PForbidUser(int dstuserid, int gmuserid, int gmlocalsid, int forbid_time, String reason, boolean auForbid,int kickoutReason) {

		super();
		this.dstuserid = dstuserid;
		this.gmlocalsid = gmlocalsid;
		this.gmuserid = gmuserid;
		this.forbid_time = forbid_time;
		this.reason = reason;
		this.kickoutReason = kickoutReason;
		this.auForbid = auForbid;
	}

	@Override
	protected boolean process() throws Exception {

		xbean.User user = xtable.User.select(dstuserid);
		if (user == null){
			logger.error("forbid failed.dstuserid didn't exist.dstuserid:"+dstuserid);
			return false;
		}
		final long roleid = user.getPrevloginroleid();
		xbean.UserPunish userpunish = xtable.Userpunish.get(dstuserid);
		if (userpunish == null) {
			userpunish = xbean.Pod.newUserPunish();
			xtable.Userpunish.insert(dstuserid, userpunish);
		}
		if (forbid_time == -5){//-5表示增加这个账号的反外挂几率
			Integer value = extraProbMap.get(dstuserid);
			if (value == null)
				extraProbMap.put(dstuserid, 1);
			else
				extraProbMap.put(dstuserid, value+1);
			return true;
		}
		kickoutReason = KickErrConst.ERR_FORBID_USER;
		if (forbid_time == -1){
			kickoutReason = KickErrConst.ERR_GACD_KICKOUT;
		}
		if (forbid_time == -2){//因为使用外挂被封禁
			userpunish.setWaiguatimes(userpunish.getWaiguatimes()+1);
			userpunish.setSendmsgtime(0);
			kickoutReason = KickErrConst.ERR_GACD_WAIGUA;
			if (userpunish.getWaiguatimes() == 1){
				forbid_time = 10;
				
			}else if (userpunish.getWaiguatimes()==2){
				forbid_time = 1440;//day
				
			}
			else if (userpunish.getWaiguatimes() == 3) {
				forbid_time = 10080;//week
				
			}else {
				forbid_time = 5256000;//10 years
				
			}
		}
		if(forbid_time == 0){// 永久封停
			forbid_time = 5256000;//10 years
		}
		if (forbid_time < 0)//小于0的情况使用我们自己的提示消息 
		    Onlines.getInstance().kick(roleid, kickoutReason);
		else {//否则使用发过来的提示消息
			if (reason==null)
				reason = "";
			Onlines.getInstance().send(roleid, new SGACDKickoutMsg1(reason));
			mkdb.Executor.getInstance().schedule(new Runnable(){

				@Override
				public void run() {
					Onlines.getInstance().kick(roleid, -1000);
				}
				
			}, 5, TimeUnit.SECONDS);
			
		}
		if (forbid_time < 0)
			forbid_time = 0;
		if (auForbid) {

		}
		xbean.PunishRecord record = xbean.Pod.newPunishRecord();
		record.setGmuserid(gmuserid);
		record.setForbidtime(forbid_time * 60 * 1000L);
		record.setOptime(System.currentTimeMillis());
		record.setReason(reason);
		record.setRoleid(0);
		record.setType(xbean.PunishRecord.TYPE_FORBID_LOGIN);
		record.setUserid(dstuserid);
		userpunish.getRecords().add(record);
		 
		if(!auForbid)//只有封单服时才保存时间   
			userpunish.setReleasetime(record.getOptime() + record.getForbidtime());

		try {
			logger.info("forbid user.gmuserid:"+gmuserid+"userid:"+dstuserid+"time:"+forbid_time+reason);
		} catch (Exception e) {
			LogManager.logger.error(e);
		}

		return true;
	}

	public static int getExtraProb(int userid){
		if (extraProbMap.containsKey(userid)) 
			return extraProbMap.get(userid);
		return 0;
	}
}
