package com.locojoy.sdk;

import openau.DataBetweenAuAnyAndGS;

import com.locojoy.sdk.util.HttpConfig;
import com.locojoy.sdk.util.HttpIO;

import mkdb.Procedure;

public class PGsHttpRequest extends Procedure {
	private final mkio.Protocol proto;
	private static HttpConfig httpconfig4gs = new HttpConfig();
	
	public PGsHttpRequest(openau.DataBetweenAuAnyAndGS proto) {
		this.proto = proto;
	}
	
	@Override
	protected boolean process() throws Exception {
		openau.DataBetweenAuAnyAndGS p = (openau.DataBetweenAuAnyAndGS)proto;
		String result = "";
		try{
			String urlstr = new String(p.info.getBytes(), "ISO-8859-1");
			mkdb.Trace.debug("PGsHttpRequest.process " + urlstr);
			result = HttpIO.getRequest(urlstr, httpconfig4gs);
		} catch(Exception e) {
			mkdb.Trace.error("PGsHttpRequest.process:", e);
		} finally {
			DataBetweenAuAnyAndGS msg = new DataBetweenAuAnyAndGS();
			msg.flag = DataBetweenAuAnyAndGS.AuAny_TO_GS;
			msg.userid = p.userid;
			msg.qtype = 10002;
			msg.info = com.locojoy.base.Octets.wrap(result, "ISO-8859-1");
			msg.send(proto.getConnection());
		}
		return true;
	}
}
