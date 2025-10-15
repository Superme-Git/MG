package com.locojoy.sdk.plats;

import java.util.HashMap;
import java.util.Map;

import openau.AuAnyLoginArg;
import openau.AuAnyLoginRes;
import openau.ErrorCodes;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mkio.Mkio;

import com.locojoy.sdk.Config;
import com.locojoy.sdk.PlatManager;
import com.locojoy.sdk.PlatProcess;

public class Hybrid implements PlatProcess {

	private static final String codepage = Config.OCTETS_CHARSET_ANSI;
	private final Map<String,PlatProcess> platnamemap = new HashMap<String,PlatProcess>();
	private PlatProcess	platprocessdefault = null;
	
	private PlatProcess getPlatProcessByName(String name) {
		return platnamemap.get(name);
	}
	private void insertPlatProcess(PlatProcess plat) {
		platnamemap.put(plat.getName(), plat);
	}

	@Override
	public String getName() {
		return "hybrid";
	}

	@Override
	public String toString() {
		return "(platprocessdefault = " + platprocessdefault.getName() + ")";
	}

	@Override
	public void init(Element ele) {	
		final NodeList subs = ele.getChildNodes(); 
		for(int i = 0; i < subs.getLength(); i ++ ) {
			final Node node = subs.item( i);
			if(Node.ELEMENT_NODE != node.getNodeType())
				continue;
			final Element subele = (Element)node;
			if(0 != subele.getNodeName().compareToIgnoreCase("use"))
				continue;
			final int usetype = Integer.parseInt(subele.getAttribute("type"));
			final PlatProcess plat = PlatManager.getPlatProcessByType(usetype);
			if(null == plat)
				throw new RuntimeException("Hybrid.init unknow found PlatProcess : type = " + usetype);
			insertPlatProcess(plat);
		}
		final int defaultplattype = Integer.parseInt(ele.getAttribute("default"));
		platprocessdefault = PlatManager.getPlatProcessByType( defaultplattype);
		if(null == platprocessdefault)
			throw new RuntimeException("Hybrid.init unknow default PlatProcess : " + defaultplattype);
		if(mkdb.Trace.isDebugEnabled())
			mkdb.Trace.debug("Hybrid.init " + this);
	}

	@Override
	public String getHttpCallBackApi() {
		return null;
	}

	@Override
	public byte[] httpCallBack(String args) {
		return null;
	}

	@Override
	public void userLogin(AuAnyLoginArg arg, AuAnyLoginRes res) {
		if( mkdb.Trace.isDebugEnabled())
			mkdb.Trace.debug( "Hybrid.userLogin " + arg);
		
		try {
			final String username = new String( arg.account.getBytes(), codepage); 
			final int index = username.lastIndexOf( '$');
			if( -1 == index) {
				platprocessdefault.userLogin(arg, res);
				return;
			}
			final String platname = username.substring( index + 1);
			if( mkdb.Trace.isDebugEnabled())
				mkdb.Trace.debug( "Hybrid.userLogin username = " + username + " platname = " + platname);
			PlatProcess process = getPlatProcessByName( platname);
			if( null == process)
				process = platprocessdefault;
			else
				arg.account = com.locojoy.base.Octets.wrap( username.substring( 0, index), codepage);
			process.userLogin(arg, res);			
		} catch( Exception e) {
			mkdb.Trace.error( "Locojoy.userLogin", e);
			res.retcode =ErrorCodes.error_plat_trans;
		}
	}

	@Override
	public void queryOrder(Mkio xio, String platid, String orderserialplat, String orderserialgame) {
		if( mkdb.Trace.isDebugEnabled())
			mkdb.Trace.debug( "Hybrid.queryOrder platid = " + platid + " orderserialplat = " + orderserialplat + " orderserialgame = " + orderserialgame);
		PlatProcess process = getPlatProcessByName( platid);
		if( null == process)
			process = platprocessdefault;
		process.queryOrder(xio, platid, orderserialplat, orderserialgame);			
	}
}
