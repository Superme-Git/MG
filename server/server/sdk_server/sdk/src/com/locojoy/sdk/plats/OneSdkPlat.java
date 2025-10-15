package com.locojoy.sdk.plats;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Element;

import openau.ErrorCodes;

import com.locojoy.base.Octets;
import com.locojoy.sdk.Config;
import com.locojoy.sdk.PNewUser;
import com.locojoy.sdk.util.HttpConfig;
import com.locojoy.sdk.util.HttpIO;

/***
 * 易接平台相关处理
 * 为了符合乐动卓越平台现状，该平台接的非常畸形，这里仅仅处理新的登录需求参数
 * 登录走乐动单独开辟的新接口，返回帐号形式和原来一样
 * 充值得走回原来乐动回调接口，不能在本类中处理
 * @author Administrator
 *
 */
public class OneSdkPlat extends LocojoyPlat {
	private static Logger logger = Logger.getLogger(OneSdkPlat.class);
	
	private static final String plattagstring = "ljpl";
	private static final String codepage = Config.OCTETS_CHARSET_ANSI;
	
	private String gameid = "88"; //乐动SDK
	private String gamekey = "b18a26ffc632752987bd24a7bf0353f3";
	private String httpurl = "http://billingtest.locojoy.com/api/1sdk/Login.ashx";
	private String httpcallbackapi = "";
	private HttpConfig httpconfig = new HttpConfig();
	
	private Octets makePlatInfo() {
		return com.locojoy.base.Octets.wrap(plattagstring, codepage);
	}

	public OneSdkPlat() {
	}

	@Override
	public String getName() {
		return plattagstring;
	}

	@Override
	public String toString() {
		return "(gameid = " + gameid + " httpurl = " + httpurl + " httpcallbackapi = " + httpcallbackapi + " httpconfig = " + httpconfig + " )";
	}

	@Override
	public void init(Element ele) {
		gameid = ele.getAttribute("gameid");
		gamekey = ele.getAttribute("gamekey");
		httpurl = ele.getAttribute("httpurl");
		httpcallbackapi = ele.getAttribute("httpcallbackapi");
		if(gameid.isEmpty())
			throw new RuntimeException("OneSdkPlat.init appid empty!");
		if(gamekey.isEmpty())
			throw new RuntimeException("OneSdkPlat.init appkey empty!");
		if(httpurl.isEmpty())
			throw new RuntimeException("OneSdkPlat.init httpurl empty!");
		httpconfig.findConfig(ele);
		logger.debug("LocojoyPlat.init " + this);
	}
	
	@Override
	public String getHttpCallBackApi() {
		return httpcallbackapi;
	}
	
	private final String doHttpLogin(String acc, String sid, String srvid, String selfc, String childc, String ips, String did, String rid) throws Exception {
		String[] yjparams = childc.split("\\|");
		String sdk = "";
		String app = "";
		if(yjparams.length < 2){
			logger.error("OneSdkPlat.doHttpLogin " + acc + ",childc=" + childc + ",yjparams=" + yjparams.toString());
			return null;
		} else {
			sdk = yjparams[0];
			app = yjparams[1];
			logger.info("OneSdkPlat.doHttpLogin " + acc + ",sdk=" + sdk + ",app=" + app);
		}
		
		final String gameid = this.gameid;
		final String session = sid;
		final String serverid = srvid;
		final String selfchannel = selfc;
		final String ip = ips;
		final String deviceid = did;
		final String roleid = rid;//no use
		final String loginurl = String.format(
				"%1$s?GameId=%2$s&sess=%3$s&uin=%4$s&ServerId=%5$s&SelfChannel=%6$s&Ip=%7$s&DeviceId=%8$s&RoleId=%9$s&sdk=%10$s&app=%11$s", 
				httpurl, gameid, session, acc, serverid, selfchannel, ip, deviceid, roleid, sdk, app);
		final String result = HttpIO.getRequest(loginurl, httpconfig);
		return result;
	}

	static private class PGetUserID extends mkdb.Procedure {
		final private String account;
		private int userid;
		
		public PGetUserID(String account) {
			this.account = account;
		}
		@Override
		protected boolean process() throws Exception {
			xbean.LocojoyPlatUserInfo ljpuserinfo = xtable.Locojoyplatusers.get(account);
			if(null != ljpuserinfo) {
				userid = ljpuserinfo.getUserid();
				return true;
			}
			final PNewUser p = new PNewUser(plattagstring, account);
			if(!p.call())
				return false;
			ljpuserinfo = xbean.Pod.newLocojoyPlatUserInfo();
			ljpuserinfo.setUserid(p.getUserID());
			ljpuserinfo.setUserinfoid(p.getUserInfoID());
			userid = p.getUserID();
			if(logger.isDebugEnabled())
				logger.debug("OneSdkPlat.PGetUserID plat=" + plattagstring + ",account=" + account + ",userid=" + userid);
			return xtable.Locojoyplatusers.add(account, ljpuserinfo);
		}
	}
	
	private static void onUserLoginSucceed(String account, openau.AuAnyLoginRes res) {
		final PGetUserID p = new PGetUserID(account);
		if(p.call()) {
			logger.debug("OneSdkPlat.onUserLoginSucceed plat=" + plattagstring + ",account=" + account + ",userid=" + p.userid);
			res.retcode = ErrorCodes.error_succeed;
			res.userid = p.userid;
		} else {
			logger.error("OneSdkPlat.onUserLoginSucceed PGetUserID error!");
			res.userid = ErrorCodes.error_xdb_store;
		}
	}
	
	@Override
	public void userLogin(openau.AuAnyLoginArg arg, openau.AuAnyLoginRes res) {
		try {
			res.platinfo = makePlatInfo();
			String account = new String(arg.account.getBytes(), codepage); 			
			final String session = new String(arg.response.getBytes(), codepage);
			final String loginipstr = new String(arg.loginipoctets.getBytes(), codepage);
			int loginip = arg.loginip;
			int plattype = arg.plattype;
			int gameid = arg.gameid;

			final String serverid = new String(arg.serverid.getBytes(), codepage);
			final String selfchannel = new String(arg.selfchannel.getBytes(), codepage);
			final String childchannel = new String(arg.childchannel.getBytes(), codepage);
			final String deviceid = new String(arg.deviceid.getBytes(), codepage);
			logger.debug("OneSdkPlat.userLogin account=" + account + ",\n" +
					/*",session=" + session + "\n" +*/
					"loginipstr=" + loginipstr + 
					",loginip=" + loginip + 
					",plattype=" + plattype + 
					",gameid=" + gameid + 
					/*",gamekey=" + gamekey + "\n" +*/
					",serverid=" + serverid +
					",selfchannel=" + selfchannel +
					",childchannel=" + childchannel +
					",deviceid=" + deviceid
					);
			
			//正常SDK逻辑流程
			String result = "";
			logger.debug("OneSdkPlat.userLogin start doHttpLogin serverid=" + serverid + ",selfchannel=" + selfchannel);
			result = doHttpLogin(account, session, serverid, selfchannel, childchannel, loginipstr, deviceid, "0");
			logger.debug("OneSdkPlat.userLogin finish doHttpLogin serverid=" + serverid + ",selfchannel=" + selfchannel);
			logger.debug("OneSdkPlat.userLogin doHttpLogin=" + result);
			final JSONObject jsonret = new JSONObject(result);
			final int errcode = Integer.valueOf(jsonret.getString("Code"));
			switch(errcode) {
			case 1:
				String channel = jsonret.getString("Channel");
				String platformid = jsonret.getString("PlatformId");
				//这里拼接造一个唯一帐号
				//account = channel + "##" + platformid;
				account = platformid + "@@" + channel;
				logger.debug("OneSdkPlat.userLogin make account=" + account);
				res.realaccount = new Octets(account.getBytes());
				onUserLoginSucceed(account, res);
				break;
			case 2:
				res.retcode = ErrorCodes.error_invalid;	
				logger.error("OneSdkPlat.userLogin errcode=" + errcode + ",Sign Failed!");
				break;
			default:
				res.retcode = ErrorCodes.error_plat_args;
				logger.error("OneSdkPlat.userLogin errcode=" + errcode + "!");
				break;
			}
		} catch(Exception e) {
			logger.error("OneSdkPlat.userLogin ", e);
			res.retcode = ErrorCodes.error_plat_trans;
		}
	}

}
