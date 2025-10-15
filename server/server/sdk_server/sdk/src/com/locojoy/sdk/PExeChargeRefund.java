package com.locojoy.sdk;

import org.apache.log4j.Logger;

import openau.DataBetweenAuAnyAndGS;

import com.locojoy.base.Marshal.OctetsStream;

import mkdb.Procedure;

public class PExeChargeRefund extends Procedure {
	private static Logger logger = Logger.getLogger(Config.class);
	
	private final mkio.Protocol proto;
	
	private final int type; //0,准备执行充值返还; 1,已经执行充值返还
	
	public PExeChargeRefund(openau.DataBetweenAuAnyAndGS proto, int type) {
		this.proto = proto;
		this.type = type;
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
			Integer chargeRe = Config.getInstance().getChargeRefund(ousername);	//username -> ousername 获得返还的符石数据
			Integer charge = Config.getInstance().getCharge(ousername);	//username -> ousername 获得曾经的充值人民币金额
			logger.info("PExeChargeRefund.userid:" + p.userid + ",username:" + username + ",ousername:" + ousername + ",config charge:" + charge + ",chargeRe:" + chargeRe);
			if(chargeRe != null && chargeRe > 0) {
				//有返还资格
				Integer ch = xtable.Userschargerefund.get(ousername);	//username -> ousername
				logger.info("PExeChargeRefund.userid:" + p.userid + ",username:" + username + ",ousername:" + ousername + ",db charge:" + ch);
				if(null == ch) {
					//有资格，也没领取过返还的
					if(type == 1) {
						//gs已经领取成功，存库标志
						xtable.Userschargerefund.add(ousername, chargeRe);	//username -> ousername
						logger.info("PExeChargeRefund.userid:" + p.userid + ",username:" + username + ",ousername:" + ousername + ",save to db!");
					} else if(type == 0) {
						StringBuilder builder = new StringBuilder();
						builder.append(1).append("#");//可以领取
						builder.append(chargeRe).append("#");//返还的符石数据
						builder.append(charge).append("#");//曾经的充值金额
						builder.append(Config.getInstance().getCharge(username)).append("#");
						result = builder.toString();
						logger.info("PExeChargeRefund.user " + p.userid + "in server " + server + "get charge refund.");
					}
				} else {
					//已经领取过返还了
					StringBuilder builder = new StringBuilder();
					builder.append(2).append("#");//不可以领取
					builder.append(chargeRe).append("#");//返还的符石数据
					builder.append(charge).append("#");//曾经的充值金额
					builder.append(Config.getInstance().getCharge(username)).append("#");
					result = builder.toString();
					logger.info("PExeChargeRefund.user " + p.userid + "in server " + server + " already charge refund.");
				}
			} else {
				//没有返还资格
				StringBuilder builder = new StringBuilder();
				builder.append(0).append("#");//不可以领取
				result = builder.toString();
				logger.info("PExeChargeRefund.user " + p.userid + "in server " + server + " cannot charge refund.");
			}
		} catch(Exception e) {
			mkdb.Trace.error("PExeChargeRefund.process:", e);
		} finally {
			if(type == 0) {
				DataBetweenAuAnyAndGS msg = new DataBetweenAuAnyAndGS();
				msg.flag = DataBetweenAuAnyAndGS.AuAny_TO_GS;
				msg.userid = p.userid;
				msg.qtype = 9004;
				//result应该包含是否有充值返还资格，成功失败，是否领取过返还，曾经充值多少，应该返还多少
				msg.info = com.locojoy.base.Octets.wrap(result, "ISO-8859-1");
				msg.send(proto.getConnection());
				logger.info("PExeChargeRefund.userid:" + p.userid + ",send gs charge refund info!");
			} else if(type == 1) {
				//这里已经保存领取记录，不再往gs返回了
				logger.info("PExeChargeRefund.userid:" + p.userid + ",get charge refund over!");
			}
		}
		return true;
	}
}
