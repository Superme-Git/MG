package com.locojoy.sdk;

import java.util.ArrayList;
import java.util.List;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

public class MBeanOperators implements DynamicMBean {

	enum InvokeActionName {
		GetUserInfo,
		GetLocojoyPlatUserInfo,
		JustForTest,
	}
	
	@Override
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		return null;
	}

	@Override
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {
	}

	@Override
	public AttributeList getAttributes(String[] attributes) {
		return null;
	}

	@Override
	public AttributeList setAttributes(AttributeList attributes) {
		return null;
	}

	@Override
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {
		final InvokeActionName	action = InvokeActionName.valueOf( actionName);
		switch(action) {
		case GetUserInfo:
			return doActionGetUserInfo((Integer)params[0]);
		case GetLocojoyPlatUserInfo:
			return doActionGetLocojoyPlatUserInfo(params[0].toString());
		case JustForTest:
			return "JustForTest echo:" + (String)params[0];
		}
		return null;
	}

	static private MBeanParameterInfo[] getOperationGetUserInfoParameterInfoArray() {
		MBeanParameterInfo[] infos = new MBeanParameterInfo[1];
		infos[0] = new MBeanParameterInfo("userid", Integer.class.getName(), "id");
		return infos;
	}
	
	static private MBeanParameterInfo[] getOperationGetLocojoyPlatUserInfoParameterInfoArray() {
		MBeanParameterInfo[] infos = new MBeanParameterInfo[1];
		infos[0] = new MBeanParameterInfo("account", String.class.getName(), "username");
		return infos;
	}

	static private MBeanParameterInfo[] getOperationGetUserIDParameterInfoArray() {
		MBeanParameterInfo[] infos = new MBeanParameterInfo[1];
		infos[0] = new MBeanParameterInfo("input", String.class.getName(), "id");
		return infos;
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		final String beanname = "aunay.userinfo";
		final List<MBeanOperationInfo> operinfos = new ArrayList<MBeanOperationInfo>();

		operinfos.add(new MBeanOperationInfo(
				InvokeActionName.GetUserInfo.name(), 
				"get user info by userid", 
				getOperationGetUserInfoParameterInfoArray(), 
				String.class.getName(), 
				MBeanOperationInfo.INFO));
		
		operinfos.add(new MBeanOperationInfo(
				InvokeActionName.GetLocojoyPlatUserInfo.name(), 
				"get locojoy user info by username(account)",
				getOperationGetLocojoyPlatUserInfoParameterInfoArray(), 
				String.class.getName(), 
				MBeanOperationInfo.INFO));
		
		operinfos.add(new MBeanOperationInfo(
				InvokeActionName.JustForTest.name(), 
				"just a test", 
				getOperationGetUserIDParameterInfoArray(), 
				String.class.getName(), 
				MBeanOperationInfo.INFO));
		
		return new MBeanInfo(this.getClass().getName(), beanname, new MBeanAttributeInfo[0], null, operinfos.toArray(new MBeanOperationInfo[0]), null);
	}
	
	private String doActionGetUserInfo(int userid) {
		final xbean.UserInfo info = xtable.Users.select(PNewUser.toXdbUserID(userid));
		if(null == info)
			return null;
		else
			return "plat=" + info.getPlat() + ",uid=" + info.getUid();
	}
	
	private String doActionGetLocojoyPlatUserInfo(String uin) {
		final xbean.LocojoyPlatUserInfo info = xtable.Locojoyplatusers.select(uin);
		if(null == info)
			return null;
		else
			return "userid=" + info.getUserid() + ",userinfoid=" + info.getUserinfoid();
	}
}
