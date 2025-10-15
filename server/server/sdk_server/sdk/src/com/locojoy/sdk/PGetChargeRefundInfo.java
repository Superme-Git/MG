package com.locojoy.sdk;

import org.apache.log4j.Logger;

import openau.DataBetweenAuAnyAndGS;

import com.locojoy.base.Marshal.OctetsStream;

import mkdb.Procedure;

public class PGetChargeRefundInfo extends Procedure {
	private static Logger logger = Logger.getLogger(Config.class);
	
	private final mkio.Protocol proto;
	
	public PGetChargeRefundInfo(openau.DataBetweenAuAnyAndGS proto) {
		this.proto = proto;
	}
	
	@Override
	protected boolean process() throws Exception {
		openau.DataBetweenAuAnyAndGS p = (openau.DataBetweenAuAnyAndGS)proto;
		String result = "";
		try{
			OctetsStream temp = new OctetsStream(p.info);
			String username = temp.unmarshal_String();
			String server = temp.unmarshal_String();
			
			String ousername = null;
			if(Math.abs(username.lastIndexOf("@") - username.lastIndexOf("@@")) <= 1) {
				//这是老版帐号,如:214402654@@locojoy
				ousername = username;
			} else {
				//这是新版帐号,加了苹果安卓渠道区分之后,如:214402654@@locojoy@1
				int lstatidx = username.lastIndexOf("@");
				ousername = username.substring(0, lstatidx);
			}
			
			//String urlstr = new String(p.info.getBytes(), "ISO-8859-1");
			//根据username查mkdb获得充值返还相关数据，然后返回给gs
			Integer chargeRe = Config.getInstance().getChargeRefund(ousername);	//username -> ousername 获得返还的符石数据
			Integer charge = Config.getInstance().getCharge(ousername);	//username -> ousername 获得曾经的充值人民币金额
			logger.info("PGetChargeRefundInfo.userid:" + p.userid + ",username:" + username + ",ousername:" + ousername + ",config charge:" + charge + ",chargeRe:" + chargeRe);
			if(chargeRe != null && chargeRe > 0) {
				//有返还资格
				Integer ch = xtable.Userschargerefund.get(ousername);	//username -> ousername
				logger.info("PGetChargeRefundInfo.userid:" + p.userid + ",username:" + username + ",ousername:" + ousername + ",db charge:" + ch);
				if(null == ch) {
					//有资格，也没领取过返还的，把数据组装给gs
					StringBuilder builder = new StringBuilder();
					builder.append(1).append("#");//可以领取
					builder.append(chargeRe).append("#");//返还的符石数据
					builder.append(charge).append("#");//曾经的充值金额
					result = builder.toString();
					logger.info("PGetChargeRefundInfo.user " + p.userid + "in server " + server + "can charge refund.");
				} else {
					//已经领取过返还了
					StringBuilder builder = new StringBuilder();
					builder.append(2).append("#");//不可以领取
					builder.append(chargeRe).append("#");//返还的符石数据
					builder.append(charge).append("#");//曾经的充值金额
					result = builder.toString();
					logger.info("PGetChargeRefundInfo.user " + p.userid + "in server " + server + "already charge refund.");
				}
			} else {
				//没有返还资格
				StringBuilder builder = new StringBuilder();
				builder.append(0).append("#");//不可以领取
				result = builder.toString();
				logger.info("PGetChargeRefundInfo.user " + p.userid + "in server " + server + "cannot charge refund.");
			}
		} catch(Exception e) {
			mkdb.Trace.error("PGetChargeRefundInfo.process:", e);
		} finally {
			DataBetweenAuAnyAndGS msg = new DataBetweenAuAnyAndGS();
			msg.flag = DataBetweenAuAnyAndGS.AuAny_TO_GS;
			msg.userid = p.userid;
			msg.qtype = 9002;
			msg.info = com.locojoy.base.Octets.wrap(result, "ISO-8859-1");
			msg.send(proto.getConnection());
		}
		return true;
	}
}
