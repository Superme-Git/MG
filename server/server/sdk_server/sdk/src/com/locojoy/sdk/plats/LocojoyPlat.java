package com.locojoy.sdk.plats;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Element;

import openau.ErrorCodes;
import openau.QueryOrderResponse;
import mkio.Mkio;

import com.locojoy.base.Octets;
import com.locojoy.sdk.Config;
import com.locojoy.sdk.PNewUser;
import com.locojoy.sdk.PlatManager;
import com.locojoy.sdk.PlatProcess;
import com.locojoy.sdk.XioManager;
import com.locojoy.sdk.util.HttpConfig;
import com.locojoy.sdk.util.HttpIO;

public class LocojoyPlat implements PlatProcess {
	private static Logger logger = Logger.getLogger(LocojoyPlat.class);
	
	private static final String plattagstring = "ljpl";
	private static final String codepage = Config.OCTETS_CHARSET_ANSI;
	
	private String gameid = "88"; //乐动SDK
	private String gamekey = "b18a26ffc632752987bd24a7bf0353f3";
	private String httpurl = "http://billingtest.locojoy.com/api/open/VerifySession.ashx";
	private String httpcallbackapi = "/locojoycallback";
	private HttpConfig httpconfig = new HttpConfig();
	
	private Octets makePlatInfo() {
		return com.locojoy.base.Octets.wrap(plattagstring, codepage);
	}

	public LocojoyPlat() {
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
			throw new RuntimeException("LocojoyPlat.init appid empty!");
		if(gamekey.isEmpty())
			throw new RuntimeException("LocojoyPlat.init appkey empty!");
		if(httpurl.isEmpty())
			throw new RuntimeException("LocojoyPlat.init httpurl empty!");
		if(httpcallbackapi.isEmpty())
			throw new RuntimeException("LocojoyPlat.init httpcallbackapi empty!");
		httpconfig.findConfig(ele);
		logger.debug("LocojoyPlat.init " + this);
	}
	
	@Override
	public String getHttpCallBackApi() {
		return httpcallbackapi;
	}
	
	private final String doHttpLogin(String sid, String srvid, String selfc, String childc, String ips, String did, String rid) throws Exception {
		final String gamekey = this.gamekey;
		final String gameid = this.gameid;
		final String session = java.net.URLEncoder.encode(sid, "utf-8");
		//final String session = sid;
		final String serverid = srvid;
		final String selfchannel = selfc;
		final String childchannel = childc;
		final String ip = ips;
		final String deviceid = did;
		final String sign = HttpIO.digestVarString(String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s%8$s", 
				gamekey, gameid, session, serverid, selfchannel, childchannel, ip, deviceid));
		logger.debug("LocojoyPlat.doHttpLogin sign:" + sign);
		final String roleid = rid;//no use
		final String loginurl = String.format(
				"%1$s?GameId=%2$s&Session=%3$s&ServerId=%4$s&SelfChannel=%5$s&ChildChannel=%6$s&Ip=%7$s&DeviceId=%8$s&Sign=%9$s&RoleId=%10$s", 
				httpurl, gameid, session, serverid, selfchannel, childchannel, ip, deviceid, sign, roleid);
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
				logger.debug("LocojoyPlat.PGetUserID plat=" + plattagstring + ",account=" + account + ",userid=" + userid);
			return xtable.Locojoyplatusers.add(account, ljpuserinfo);
		}
	}
	
	//注意：乐动sdk比较特别，是一个包含多个渠道的总sdk，类似一个sdk的适配器，所以这里传入的帐号在之前已经特殊处理
	private static void onUserLoginSucceed(String account, openau.AuAnyLoginRes res) {
		final PGetUserID p = new PGetUserID(account);
		if(p.call()) {
			logger.debug("LocojoyPlat.onUserLoginSucceed plat=" + plattagstring + ",account=" + account + ",userid=" + p.userid);
			res.retcode = ErrorCodes.error_succeed;
			res.userid = p.userid;
		} else {
			logger.error("LocojoyPlat.onUserLoginSucceed PGetUserID error!");
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
			logger.debug("LocojoyPlat.userLogin account=" + account + ",\n" +
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
			
			if(null != account && !account.isEmpty() && account.length() > 0){
				if(account.startsWith("GM#")){
					String tm = account.replaceAll("GM#", "");
					Integer usid = Integer.valueOf(tm);
					res.realaccount = new Octets(tm.getBytes());
					res.retcode = ErrorCodes.error_succeed;
					res.userid = usid;
					logger.debug("LocojoyPlat.userLogin account=" + tm + "!gm use!");
					return;
				}
				if(Config.getInstance().isAllowRobot()) {
					if(account.startsWith(Config.getInstance().getAllowRobotName())){
						//这说明是机器人或者测试用的登录，用的配置里的帐号密码，直接过了
						res.realaccount = new Octets(account.getBytes());
						onUserLoginSucceed(account, res);
						logger.debug("LocojoyPlat.userLogin account=" + account + "!JUST TEST!DON'T AUTH!" + 
								"Allow account:" + Config.getInstance().getAllowRobotName() + ".");
						return;
					} else {
						logger.debug("LocojoyPlat.userLogin account=" + account + 
								"!Isn't the allow robot name(" + Config.getInstance().getAllowRobotName() + ")!Will do sdk http login!");
					}
				} else {
					logger.debug("LocojoyPlat.userLogin account=" + account + "!Config don't allow robot!Will do sdk http login!");
				}
			}
			
			//正常SDK逻辑流程
			String result = "";
			logger.debug("LocojoyPlat.userLogin start doHttpLogin serverid=" + serverid + ",selfchannel=" + selfchannel);
			result = doHttpLogin(session, serverid, selfchannel, childchannel, loginipstr, deviceid, "0");
			logger.debug("LocojoyPlat.userLogin finish doHttpLogin serverid=" + serverid + ",selfchannel=" + selfchannel);
			logger.debug("LocojoyPlat.userLogin doHttpLogin=" + result);
			final JSONObject jsonret = new JSONObject(result);
			final int errcode = Integer.valueOf(jsonret.getString("Code"));
			switch(errcode) {
			case 1:
				String channel = jsonret.getString("Channel");
				String platformid = jsonret.getString("PlatformId");
				String selfchannel0 = selfchannel.substring(0,1);
				//这里拼接造一个唯一帐号
				//account = channel + "##" + platformid;
				account = platformid + "@@" + channel + "@" + selfchannel0;
				logger.debug("LocojoyPlat.userLogin make account=" + account);
				res.realaccount = new Octets(account.getBytes());
				onUserLoginSucceed(account, res);
				break;
			case 2:
				res.retcode = ErrorCodes.error_invalid;	
				logger.error("LocojoyPlat.userLogin errcode=" + errcode + ",Sign Failed!");
				break;
			default:
				res.retcode = ErrorCodes.error_plat_args;
				logger.error("LocojoyPlat.userLogin errcode=" + errcode + "!");
				break;
			}
		} catch(Exception e) {
			logger.error("LocojoyPlat.userLogin ", e);
			res.retcode = ErrorCodes.error_plat_trans;
		}
	}
	
	private static int getUserID(String account) {
		final Integer userid = xtable.Locojoyplatusers.selectUserid(account);
		if(null == userid)
			return -1;
		else
			return userid.intValue();
	}
	
	private static class PInsertOrder extends mkdb.Procedure {
		private final String orderserialplat;
		private final String orderserialgame;
		private final String username;
		private final Octets vars;
		private final Map<String,String> varsmap;
		public PInsertOrder(String orderserialplat, String orderserialgame,  String username, Octets vars, Map<String,String> varsmap) {
			this.orderserialplat = orderserialplat;
			this.orderserialgame = orderserialgame;
			this.username = username;
			this.vars = vars;
			this.varsmap = varsmap;
		}
		
		@Override
		protected boolean process() throws Exception {
			final xbean.LocojoyPlatOrderInfo info = xbean.Pod.newLocojoyPlatOrderInfo();
			info.setCreatetime(System.currentTimeMillis());
			info.setOrderserialplat(orderserialplat);
			info.setOrderserialgame(orderserialgame);
			info.setUsername(username);
			info.setVarsCopy(vars.getBytes());
			if(xtable.Locojoyplatorderinfos.get(orderserialplat) != null)
				xtable.Locojoyplatorderinfos.remove(orderserialplat);
			xtable.Locojoyplatorderinfos.insert(orderserialplat, info);
			
			String oldorderplat = xtable.Locojoyplatordergametoplat.get(orderserialgame);
			if(oldorderplat != null)
			{
				if(oldorderplat.equals(orderserialplat))
				{
					logInfo();
					return true;//重复不再插入
				}
				xtable.Locojoyplatordergametoplat.remove(orderserialgame);
				//打日志，万一出现这种情况:对游戏服务器请求了一次充值，但在sdk那边出现了两笔订单！
				logger.error("LocojoyPlat.PInsertOrder:same orderserialgame= " + orderserialgame + ",diff orderserialplat=" + ",old:"+oldorderplat+",new:"+orderserialplat);
			}
			xtable.Locojoyplatordergametoplat.insert(orderserialgame, orderserialplat);
			logInfo();
			return true;
		}
		
		private void logInfo()
		{
			StringBuilder sb = new StringBuilder("LocojoyPlat.PInsertOrder:username=")
			.append(username).append(",orderserialplat=").append(orderserialplat)
			.append(",orderserialgame=").append(orderserialgame)
			.append(",vars=").append(varsmap);
			logger.info(sb.toString());
		}
	}

	private static void onQueryOrderResponse(Mkio xio, String orderserialplat, String orderserialgame, int userid, int errorcode, Octets vars) {
		new QueryOrderResponse(errorcode, 
				/*QueryOrderResponse.restype_query,*/0, 
				plattagstring, 
				orderserialplat, 
				orderserialgame, 
				userid, 
				vars).send(xio);
		logger.debug("LocojoyPlat.onQueryOrderResponse send QueryOrderResponse to user:" + userid + " at serverip:" + xio.getPeerInfo());
	}

	@Override
	public void queryOrder(Mkio xio, String platid, String orderserialplat, String orderserialgame) {
		String serverid = XioManager.getInstance().getXioInfo(xio).getServerid();
		if(logger.isDebugEnabled())
			logger.debug("LocojoyPlat.queryOrder orderserialplat=" + orderserialplat + ",orderserialgame=" + orderserialgame);
		
		if(orderserialplat.isEmpty()) {
			//gs过来的查询订单，一般如乐动sdk，不会知道平台订单，所以空，查游戏内订单
			logger.debug("LocojoyPlat.queryOrder server:" + serverid + "orderserialplat empty,select orderserialgame.");
			if(!orderserialgame.isEmpty()) {
				orderserialplat = xtable.Locojoyplatordergametoplat.select(orderserialgame);
				logger.debug("LocojoyPlat.queryOrder orderserialplat " + orderserialplat + " by select orderserialgame.");
				//通过游戏内订单查询平台回调推过来的平台订单
				if(null == orderserialplat)
					orderserialplat = "";
			}
		}
		logger.debug("LocojoyPlat.queryOrder orderserialplat:" + orderserialplat + ",orderserialgame:" + orderserialgame);
		xbean.LocojoyPlatOrderInfo orderinfo = null;
		if(!orderserialplat.isEmpty()) {
			logger.debug("LocojoyPlat.queryOrder orderserialplat:empty.");
			orderinfo = xtable.Locojoyplatorderinfos.select(orderserialplat);
		}
		if(null != orderinfo) {
			logger.debug("LocojoyPlat.queryOrder serverid:" + serverid + ",orderserialplat:" + orderserialplat + ",orderserialgame:" + orderserialgame);
			LocojoyPlat.onQueryOrderResponse(xio, orderinfo.getOrderserialplat(), orderserialgame, getUserID(orderinfo.getUsername()), ErrorCodes.error_succeed, Octets.wrap(orderinfo.getVarsCopy()));
			return;
		}
		logger.debug("LocojoyPlat.queryOrder orderinfo null!orderserialplat:" + orderserialplat);
	}
	
	private byte[] doVersion5CallBack(String version, String paydata, String sign, Map<String,String> vars) throws Exception {
		final JSONObject pd = new JSONObject(paydata);
		final String OrderId = pd.getString("OrderId");
		final String Channel = pd.getString("Channel");
		//final String AppId = pd.getString("AppId");
		//final String ConsumeId = pd.getString("ConsumeId");
		final String PlatformId = pd.getString("PlatformId");
		//final String ProductId = pd.getString("ProductId");
		//final String ProductCount = pd.getString("ProductCount");
		//final String Money = pd.getString("Money");
		//final String Currency = pd.getString("Currency");
		//final String Points = pd.getString("Points");
		//final String BonusPoints = pd.getString("BonusPoints");
		final String CpInfo = pd.getString("CpInfo");
		//final String CpExtra = pd.getString("CpExtra");
		final String ServerId = pd.getString("ServerId");
		//final String RoleId = pd.getString("RoleId");
		//final String OrderTime = pd.getString("OrderTime");
		
		logger.debug("LocojoyPlat.doVersion5CallBack CpInfo=" + CpInfo);
		//解出cpinfo里的游戏内部订单号
		final String decodeCpInfo = HttpIO.getFromBase64(CpInfo); 
		if(null != decodeCpInfo){
			logger.debug("LocojoyPlat.doVersion5CallBack decodeCpInfo=" + decodeCpInfo);
		} else {
			logger.error("LocojoyPlat.doVersion5CallBack decodeCpInfo=" + decodeCpInfo);
			return ("failing").getBytes(codepage);
		}
		
		String selfchannel = "";
		String[] cpinfos = decodeCpInfo.split("\\|");
		String cpinfoGamesn = "";
		if(Channel.equals("1SDK")){
			if(cpinfos.length <= 7){
				logger.error("LocojoyPlat.doVersion5CallBack cpinfos=" + cpinfos.toString() + ",length=" + cpinfos.length);
				return ("failing").getBytes(codepage);
			} else {
				//String cpinfoServerId = cpinfos[0];
				String cpinfoSelfChannel = cpinfos[1];
				selfchannel = cpinfoSelfChannel;
				String cpinfoRoleId = cpinfos[2];
				//String cpinfoIp = cpinfos[3];
				//String cpinfoDeviceId = cpinfos[4];
				//String cpinfoGameId = cpinfos[5];
				//String cpinfoProductId = cpinfos[6];
				cpinfoGamesn = cpinfos[7];
				logger.debug("LocojoyPlat.doVersion5CallBack roleId=" + cpinfoRoleId + ",gamesn=" + cpinfoGamesn + ",selfchannel=" + selfchannel);
			}
		} else {
			if(cpinfos.length <= 5){
				logger.error("LocojoyPlat.doVersion5CallBack cpinfos=" + cpinfos.toString() + ",length=" + cpinfos.length);
				return ("failing").getBytes(codepage);
			} else {
				//String cpinfoServerId = cpinfos[0];
				String cpinfoSelfChannel = cpinfos[1];
				selfchannel = cpinfoSelfChannel;
				String cpinfoRoleId = cpinfos[2];
				//String cpinfoIp = cpinfos[3];
				//String cpinfoDeviceId = cpinfos[4];
				cpinfoGamesn = cpinfos[5];
				logger.debug("LocojoyPlat.doVersion5CallBack roleId=" + cpinfoRoleId + ",gamesn=" + cpinfoGamesn + ",selfchannel=" + selfchannel);
			}
		}
		
		final String checksign = HttpIO.digestVarString(
				gamekey +
				version + 
				paydata);
		if(0 != checksign.compareToIgnoreCase(sign)) {
			logger.error("LocojoyPlat.doVersion5CallBack check sign failed!checksign=" + checksign);
			return ("failing").getBytes(codepage);
		}

		String selfchannel0 = selfchannel.substring(0,1);
		//因为是混服，增加一个安卓和ios的渠道区分，同服但是帐号不互通。
		String account = PlatformId + "@@" + Channel + "@" + selfchannel0;
		logger.debug("LocojoyPlat.doVersion5CallBack account=" + account);
		final int userid = getUserID(account);
		logger.debug("LocojoyPlat.doVersion5CallBack userid=" + userid + ",account=" + account);
		if(userid <= 0) {
			logger.error("LocojoyPlat.doVersion5CallBack UserID=" + userid + ",not find!");
			return ("failing").getBytes(codepage);
		}
		logger.debug("LocojoyPlat.doVersion5CallBack serverid=" + ServerId + ",OrderId=" + OrderId + ",userid = " + userid + ",account =" + account);	
		final Octets octetsvars = PlatManager.marshalVars(vars);
		if(new PInsertOrder(OrderId, cpinfoGamesn, account, octetsvars, vars).call()) {
			final QueryOrderResponse protocol = new QueryOrderResponse(
					ErrorCodes.error_succeed, 
					/*QueryOrderResponse.restype_notify,*/1, 
					plattagstring, 
					OrderId, 
					cpinfoGamesn, 
					userid, 
					octetsvars);
			XioManager.getInstance().sendProtocol(protocol, ServerId);
			logger.debug("LocojoyPlat.doVersion5CallBack send QueryOrderResponse to server:" + ServerId + ",user:" + userid);
			return ("success").getBytes(codepage);
		} else {
			return ("failing").getBytes(codepage);
		}
	}
	
	@Override
	public byte[] httpCallBack(String args) {
		if(logger.isDebugEnabled())
			logger.debug("LocojoyPlat.httpCallBack args=" + args);
		
		try {
			final Map<String,String> vars = HttpIO.argsToStringMap(args);
			final String Version = vars.get("Version");
			if(Version.equals("5")) {
				final String Sign = vars.get("Sign");
				String paytdata = java.net.URLDecoder.decode(vars.get("PayData"), "utf-8");
				return doVersion5CallBack(Version, paytdata, Sign, vars);
			} else {
				//这里Version应该等于3？
				final String OrderId = vars.get("OrderId");
				final String Channel = vars.get("Channel");
				//final String AppId = vars.get("AppId");
				//final String ConsumeId = vars.get("ConsumeId");
				final String PlatformId = vars.get("PlatformId");
				//final String ProductId = vars.get("ProductId");
				//final String Money = vars.get("Money");
				//final String Points = vars.get("Points");
				//final String BonusPoints = vars.get("BonusPoints");
				final String CpInfo = vars.get("CpInfo");
				//final String CpExtra = vars.get("CpExtra");
				final String ServerId = vars.get("ServerId");
				//final String RoleId = vars.get("RoleId");
				//final String OrderTime = vars.get("OrderTime");
				//final String Sign = vars.get("Sign");
				/*if(0 != AppId.compareToIgnoreCase(this.appid)) {
					logger.error("LocojoyPlat.httpCallBack check AppId error!AppId=" + AppId);
					return ("failing").getBytes(codepage);
				}*/
				
				logger.debug("LocojoyPlat.httpCallBack CpInfo=" + CpInfo);
				//解出cpinfo里的游戏内部订单号
				final String decodeCpInfo = HttpIO.getFromBase64(CpInfo); 
				if(null != decodeCpInfo){
					logger.debug("LocojoyPlat.httpCallBack decodeCpInfo=" + decodeCpInfo);
				} else {
					logger.error("LocojoyPlat.httpCallBack decodeCpInfo=" + decodeCpInfo);
					return ("failing").getBytes(codepage);
				}
				
				String selfchannel = "";
				String[] cpinfos = decodeCpInfo.split("\\|");
				String cpinfoGamesn = "";
				if(Channel.equals("1SDK")){
					if(cpinfos.length <= 7){
						logger.error("LocojoyPlat.httpCallBack 1SDK cpinfos=" + cpinfos.toString() + ",length=" + cpinfos.length);
						return ("failing").getBytes(codepage);
					} else {
						//String cpinfoServerId = cpinfos[0];
						String cpinfoSelfChannel = cpinfos[1];
						selfchannel = cpinfoSelfChannel;
						String cpinfoRoleId = cpinfos[2];
						//String cpinfoIp = cpinfos[3];
						//String cpinfoDeviceId = cpinfos[4];
						//String cpinfoGameId = cpinfos[5];
						//String cpinfoProductId = cpinfos[6];
						cpinfoGamesn = cpinfos[7];
						//cpinfoGamesn = cpinfos[cpinfos.length - 1];
						logger.debug("LocojoyPlat.httpCallBack 1SDK roleId=" + cpinfoRoleId + ",gamesn=" + cpinfoGamesn);
					}
				} else {
					if(cpinfos.length <= 5){
						logger.error("LocojoyPlat.httpCallBack normal cpinfos=" + cpinfos.toString() + ",length=" + cpinfos.length);
						return ("failing").getBytes(codepage);
					} else {
						//String cpinfoServerId = cpinfos[0];
						String cpinfoSelfChannel = cpinfos[1];
						selfchannel = cpinfoSelfChannel;
						String cpinfoRoleId = cpinfos[2];
						//String cpinfoIp = cpinfos[3];
						//String cpinfoDeviceId = cpinfos[4];
						cpinfoGamesn = cpinfos[5];
						logger.debug("LocojoyPlat.httpCallBack normal roleId=" + cpinfoRoleId + ",gamesn=" + cpinfoGamesn);
					}
				}
				
				final String checksign = HttpIO.digestVarString(
						gamekey +
						vars.get("Version") + 
						vars.get("OrderId") + 
						vars.get("Channel") + 
						vars.get("AppId") + 
						vars.get("ConsumeId") + 
						vars.get("PlatformId") + 
						vars.get("ProductId") + 
						vars.get("Money") + 
						vars.get("Points") +
						vars.get("BonusPoints") + 
						vars.get("CpInfo"));
				if(0 != checksign.compareToIgnoreCase(vars.get("Sign"))) {
					logger.error("LocojoyPlat.httpCallBack check sign failed!checksign=" + checksign);
					return ("failing").getBytes(codepage);
				}

				String selfchannel0 = selfchannel.substring(0,1);
				//因为是混服，增加一个安卓和ios的渠道区分，同服但是帐号不互通。
				String account = PlatformId + "@@" + Channel + "@" + selfchannel0;
				logger.debug("LocojoyPlat.httpCallBack account=" + account);
				final int userid = getUserID(account);
				logger.debug("LocojoyPlat.httpCallBack userid=" + userid + ",account=" + account);
				if(userid <= 0) {
					logger.error("LocojoyPlat.httpCallBack UserID=" + userid + ",not find!");
					return ("failing").getBytes(codepage);
				}
				logger.debug("LocojoyPlat.httpCallBack serverid=" + ServerId + ",OrderId=" + OrderId + ",userid = " + userid + ",account =" + account);	
				final Octets octetsvars = PlatManager.marshalVars(vars);
				if(new PInsertOrder(OrderId, cpinfoGamesn, account, octetsvars, vars).call()) {
					final QueryOrderResponse protocol = new QueryOrderResponse(
							ErrorCodes.error_succeed, 
							/*QueryOrderResponse.restype_notify,*/1, 
							plattagstring, 
							OrderId, 
							cpinfoGamesn, 
							userid, 
							octetsvars);
					XioManager.getInstance().sendProtocol(protocol, ServerId);
					logger.debug("LocojoyPlat.httpCallBack send QueryOrderResponse to server:" + ServerId + ",user:" + userid);
					return ("success").getBytes(codepage);
				} else {
					logger.error("LocojoyPlat.httpCallBack PInsertOrder call fail!user:" + userid);
					return ("failing").getBytes(codepage);
				}
			}	
		} catch(Exception e) {
			logger.error("LocojoyPlat.httpCallBack ", e);
			try {
				return ("failing").getBytes(codepage);
			} catch (UnsupportedEncodingException e1) {
				return null;
			}
		}
		
	}
		
}
