package com.locojoy.sdk;

import mkdb.Procedure;

public class PNewUser extends Procedure {
	private static long baseuserid = 4096;
	private final String plat;
	private final String uid;
	private int userid;
	private long userinfoid;
	
	public PNewUser(String plat, String uid) {
		this.plat = plat;
		this.uid = uid;
	}
	@Override
	protected boolean process() throws Exception {
		final xbean.UserInfo userinfo = xbean.Pod.newUserInfo();
		userinfo.setPlat(plat);
		userinfo.setUid(uid);
		userinfoid = xtable.Users.insert(userinfo);
		final long intuserid = userinfoid / baseuserid;
		if(intuserid >= (long)Integer.MAX_VALUE ) {
			mkdb.Trace.error("table users new user userid( " + userid + ") > Integer.MAX_VALUE");
			return false;
		}
		this.userid = (int)intuserid;
		mkdb.Trace.info("PNewUser plat=" + plat + ",uid=" + uid + "-----userinfoid=" + userinfoid + ",userid=" + userid);
		return true;
	}
	
	public int getUserID() {
		return userid;
	}
	
	public long getUserInfoID() {
		return userinfoid;
	}
	
	public static long toXdbUserID(int userid) {
		final mkdb.util.UniqNameConf unc = mkdb.Mkdb.getInstance().getConf().getUniqNameConf();
		return (long)userid * PNewUser.baseuserid + (long)unc.getLocalId();
	}
}
