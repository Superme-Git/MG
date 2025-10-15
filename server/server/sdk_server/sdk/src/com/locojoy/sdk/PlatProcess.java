package com.locojoy.sdk;

public interface PlatProcess {
	void init(org.w3c.dom.Element ele);
	String getName();
	String getHttpCallBackApi();
	byte[] httpCallBack(String args);
	void userLogin(openau.AuAnyLoginArg arg, openau.AuAnyLoginRes res);
	void queryOrder(mkio.Mkio xio, String platid, String orderserialplat, String orderserialgame);
}
