package com.locojoy.sdk;


public class PGetMacUserID extends mkdb.Procedure {
	final private String username;//就是mac
	public int userid;
	private String plattagstr;
	
	public PGetMacUserID(String username, String plattagstr) {
		this.username = username;
		this.plattagstr = plattagstr;
	}
	@Override
	protected boolean process() throws Exception {
		/*
		xbean.MacUserInfo uinfo = xtable.Macusers.get( username);
		if( null != uinfo) {
			userid = uinfo.getUserid();
			return true;
		}
		final PNewUser p = new PNewUser( plattagstr, username);
		if( !p.call())
			return false;
		userid = p.getUserID();
		uinfo = xbean.Pod.newMacUserInfo();
		uinfo.setUserid( userid);
		uinfo.setUserinfoid( p.getUserInfoID());
		uinfo.setUsername( username);
		return xtable.Macusers.add( username, uinfo);
		*/
		return true;
	}
}