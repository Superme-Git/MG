package com.locojoy.sdk;

import java.util.HashMap;
import java.util.Map;


public class PlatManager {
	static private final Map<Integer,PlatProcess> processmap = new HashMap<Integer,PlatProcess>();
	static private final Map<String,PlatProcess> callbackmap = new HashMap<String,PlatProcess>();
	
	static public PlatProcess getPlatProcessByType(int pt) {
		return processmap.get(Integer.valueOf(pt));
	}

	static private PlatProcess getPlatProcessByXio(mkio.Mkio xio) {
		final XioManager.XioInfo info = XioManager.getInstance().getXioInfo(xio);
		if(null == info)
			throw new RuntimeException("getPlatProcessByXio xio=" + xio + ",lost XioInfo!");
		return processmap.get(info.getPlattype());
	}
	
	static public void insertPlatProcess(int pt, PlatProcess process) {
		final String url = process.getHttpCallBackApi();
		if(null != url && !url.isEmpty())
			callbackmap.put(url, process);
		processmap.put(pt, process);
	}

	static public boolean isPlatTypeExisting(int pt) {
		return processmap.containsKey(pt);
	}
	static public int checkPlatTypeExisting(int pt) {
		if(isPlatTypeExisting(pt))
			return pt;
		else
			return openau.AuAnyPlatTypes.unknow;
	}
	
	static public void onAuAnyLogin(mkio.Mkio xio, openau.AuAnyLoginArg arg, openau.AuAnyLoginRes res) {
		boolean isbyxio = arg.plattype > 10000 ? true : false ; //暂时认为大于10000的平台类型通过xio决定具体平台
		if(isbyxio) {
			final PlatProcess process = getPlatProcessByXio(xio);
			process.userLogin(arg, res);
		} else {
			final PlatProcess process = getPlatProcessByType(arg.plattype);
			process.userLogin(arg, res);
		}
	}
	
	static public void onAuanyQueryOrder(mkio.Mkio xio, String platid, String orderserialplat, String orderserialgame) {
		final PlatProcess process = getPlatProcessByXio(xio);
		process.queryOrder(xio, platid, orderserialplat, orderserialgame);
	}
	
	static public byte[] onHttpServerCallBack(String url, String args) {
		final PlatProcess process = callbackmap.get( url);
		if(null != process)
			return process.httpCallBack( args);
		else
			return null;
	}
	
	public static com.locojoy.base.Octets marshalVars(Map<String,String> vars)
	{
		final com.locojoy.base.Marshal.OctetsStream os = new com.locojoy.base.Marshal.OctetsStream();
		os.compact_uint32(vars.size());
		for (java.util.Map.Entry<java.lang.String, java.lang.String> _e_ : vars.entrySet()) {
			os.marshal(_e_.getKey(), "UTF-16LE");
			os.marshal(_e_.getValue(), "UTF-16LE");
		}
		return os;
	}
}
