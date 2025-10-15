package com.world2.auth;

public class GroupInfo {
	/*	group name */
	String name;
	
	/* all usernames in group, is no user, please set '-' for allow_users[0]*/
	String[] allow_users;
	
	/* group autheticated type, value should be: "OU_and_CN" or "OU_only" */
	String verifytype;
	
	public String[] getAllow_users() {
		return allow_users;
	}
	public void setAllow_users(String[] allow_users) {
		this.allow_users = allow_users;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVerifytype() {
		return verifytype;
	}
	public void setVerifytype(String verifytype) {
		this.verifytype = verifytype;
	}
}
