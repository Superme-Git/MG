
package fire.pb;

import java.util.HashMap;
import java.util.Map;

import xbean.BattleInfo;
import fire.log.LogManager;
import fire.pb.battle.PEnterBattleAfterEnterWorld;
import fire.pb.battle.SSendBattleStart;
import fire.pb.util.InetAddressUtil;
import gnet.link.Dispatch;
import gnet.link.Kick;
import gnet.link.Onlines;





// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CNotifyDeviceInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CNotifyDeviceInfo extends __CNotifyDeviceInfo__ {

	@Override
	protected void process() {
		
		final int userid = ((Dispatch)this.getContext()).userid;
		if(userid == -1){
			kickUser(-1000);
			mkdb.Trace.info("CRoleList====userid = -1  踢掉  =============");
			return;
		}
		
/*		new mkdb.Procedure(){

			@Override
			protected boolean process() throws Exception {

//				Integer userid = xtable.Properties.selectUserid(roleid);
//				xbean.ActiveUserInfo activeUserInfo = xtable.Activeusertable.select(userid);
//				Integer lev = xtable.Properties.selectLevel(roleid);
				xbean.AUUserInfo userinfo = xtable.Auuserinfo.get(userid);
				
//				java.util.Map<String, Object> params = LogUtil.putRoleBasicParams(roleid, new HashMap<String, Object>());
				Map<String, String> machineInfos = new HashMap<String, String>();
				for (UserInfoUnit uiu : info) {
					machineInfos.put(uiu.key, uiu.value);
				}
				
				//这里处理一下日志相关的逻辑
				xbean.UserDeviceInfo userDeviceInfo=xtable.Userdeviceinfotab.get(userid);
				boolean isfirst=false;
				int first=1;
				if(userDeviceInfo==null){
					//是第一次注册
					first=0;
					isfirst=true;
					userDeviceInfo=xbean.Pod.newUserDeviceInfo();
					xtable.Userdeviceinfotab.insert(userid, userDeviceInfo);
				}
				userDeviceInfo.setIp(userinfo!=null?InetAddressUtil.ipInt2String(userinfo.getLoginip()):"auuserinfonull"); // 玩家设备IP
				//设置相关的客户端机器信息
				userDeviceInfo.setTelcooper(machineInfos.get("TelcoOper")==null?"":machineInfos.get("TelcoOper")); // 用户网络运营商
				userDeviceInfo.setChid(machineInfos.get("ChId")==null?"":machineInfos.get("ChId")); // 渠道ID
				userDeviceInfo.setPlattype(machineInfos.get("PlatType")==null?"":machineInfos.get("PlatType")); // 平台ID(IOS,android,wp)
				userDeviceInfo.setDeviid(machineInfos.get("DeviId")==null?"":machineInfos.get("DeviId")); // 玩家设备(mac/idfv)设备唯一标识
				userDeviceInfo.setImei(machineInfos.get("IMEI")==null?"":machineInfos.get("IMEI")); // 入网标识符
				userDeviceInfo.setNetenvir(machineInfos.get("NetEnvir")==null?"":machineInfos.get("NetEnvir")); // 网络环境(如：3G,4G,wifi)
				userDeviceInfo.setOsver(machineInfos.get("OsVer")==null?"":machineInfos.get("OsVer")); // 玩家系统版本(具体版本如ios9,android4.1)
				userDeviceInfo.setBrtype(machineInfos.get("BrType")==null?"":machineInfos.get("BrType")); // 玩家手机类型(如联想，酷派)
				String username=xtable.Auuserinfo.selectUsername(userid);
				if(username!=null){
					userDeviceInfo.setUsername(username);
				}
				
				if(isfirst==true){
					//注册的运营日志
					userDeviceInfo.setFirstdeviid(machineInfos.get("DeviId")==null?"":machineInfos.get("DeviId"));
					fire.log.YYLogger.OpRegLog(userid,0,0);
				}
				//登陆日志
				fire.log.YYLogger.OpLog(userid,0, "login",first,0,0,0);
				
				
				
		        if (userinfo != null){
		        	String appsrc = machineInfos.get("appsrc");
		        	String mac = machineInfos.get("mac");
//		        	String os = machineInfos.get("os");
					userinfo.setClientsource(appsrc!=null?appsrc:"");
					userinfo.setMac(mac!=null?mac.toUpperCase():"");
//					userinfo.setOs(osType);
		        	userinfo.setPlatform(appsrc!=null?appsrc:"");
		        	String idfa = machineInfos.get("idfa");
		        	userinfo.setIdfa(idfa!=null?idfa:"");
		        }else {
					LogManager.logger.error("userinfo is null when CNotifyDeviceInfo.userid:"+userid+" infos:"+machineInfos.toString());
				}
				try {
					
					String devicetype = machineInfos.get("devicetype");
					if (devicetype == null) devicetype = "";
					String os = machineInfos.get("os");
					if (os == null) os = ""; 
					String osversion = machineInfos.get("osversion");
					if (osversion == null) osversion = ""; 
					String width = machineInfos.get("width");
					if (width == null) width = ""; 
					String height = machineInfos.get("height");
					if (height == null) height = ""; 
					String mac = machineInfos.get("mac");
					if (mac == null) mac = ""; 
					mac = mac.toUpperCase();
					String appsrc = machineInfos.get("appsrc");
					if (appsrc == null) appsrc =""; 
					String phonenumber = machineInfos.get("phonenumber");
					if (phonenumber == null) phonenumber = ""; 
					String gprs = machineInfos.get("gprs");
					if (gprs == null) gprs = ""; 
					String accesspoint = machineInfos.get("accesspoint");
					if (accesspoint == null) accesspoint = ""; 
					String cpuname = machineInfos.get("cpuName");
					if (cpuname == null) cpuname = "";
					String cpuMaxFreq = machineInfos.get("cpuMaxFreq");
					if (cpuMaxFreq == null) cpuMaxFreq = ""; 
					String cpuCount = machineInfos.get("cpuCount");
					if (cpuCount == null) cpuCount = "";
					String totalMemSize = machineInfos.get("totalMemSize");
					if (totalMemSize == null) totalMemSize = ""; 
					String udid = machineInfos.get("udid");
					if (udid == null) udid = ""; 
					String idfa = machineInfos.get("idfa");
					if (idfa == null) idfa = ""; 
					
//					params.put(RemoteLogParam.MODEL, devicetype);
//					params.put(RemoteLogParam.SYSTEMTYPE, os);
//					params.put(RemoteLogParam.SYSTEMVERSION, osversion);
//					params.put(RemoteLogParam.RESOLUTION, width+height);
//					params.put(RemoteLogParam.MACADDRESS, mac);
//					params.put(RemoteLogParam.CLIENTSOURCE, appsrc);
//					params.put(RemoteLogParam.ACCOUNTSOURCE, userinfo!= null?userinfo.getUsername().substring(0, 4):"userinfo null");//getNickname --> getUsername
//					params.put(RemoteLogParam.PHONENUM, phonenumber);
//					params.put(RemoteLogParam.GPRSIMFO, gprs);
//					params.put(RemoteLogParam.ACCESSPOINT, accesspoint);
//					params.put(RemoteLogParam.CPUNAME, cpuname);
//					params.put(RemoteLogParam.CPUMAXFARQ, cpuMaxFreq);
//					params.put(RemoteLogParam.CPUCOUNT, cpuCount);
//					params.put(RemoteLogParam.TOTALMEMSIZE, totalMemSize);
//					
//					params.put(RemoteLogParam.PEER, InetAddressUtil.ipInt2String(userinfo!= null?userinfo.getLoginip():0));
//					params.put(RemoteLogParam.ACCOUNT,userinfo!= null?userinfo.getUsername():"userinfo null");
//					params.put(RemoteLogParam.UDID, udid);
//					params.put(RemoteLogParam.IDFA, idfa);
//					params.put(RemoteLogParam.SCORE, xtable.Properties.selectRolezonghemaxscore(roleid));
//					LogManager.getInstance().doLog(RemoteLogID.ROLEINFO, params);
//					params.put(RemoteLogParam.FROM, ConfigManager.getGsZoneId());
//					params.put(RemoteLogParam.PLATFORM, appsrc);
//					params.put(RemoteLogParam.USERID,userid);
//					params.put(RemoteLogParam.MAC,mac);
//					params.put(RemoteLogParam.OS,userinfo!=null?userinfo.getOs():"");
//					params.put(RemoteLogParam.ROLEID,roleid);
//					params.put(RemoteLogParam.LEV,lev);
//					params.put(RemoteLogParam.TOTALCASH,activeUserInfo==null?"":activeUserInfo.getChargetotalrmblong());
//					params.put(RemoteLogParam.HINT,"");
//					params.put(RemoteLogParam.CAMP, String.valueOf(new fire.pb.PropRole(roleid, true).getCamp()));
//					LogManager.getInstance().doLog(RemoteLogID.ROLELOGIN, params);
					
//					if (udid.length()>0){
//						sendDataToAuAny(userid,udid);					
//					}
//					if (mac.length() > 0) {
//						mac = mac.replaceAll(":", "");
//						mac = mac.toLowerCase();
//						sendDataToAuAny(userid, mac);
//					}
//					pexecute(new PForbidUserByMacOrIDFA(roleid, mac, idfa));
//					pexecute(new PFskAward(roleid,devicetype));//发富士康礼包
				} catch (Exception e) {
					LogManager.logger.error("notify device info error", e);
				}
				
				final SNotifyDeviceInfo msg = new SNotifyDeviceInfo();
				msg.ip = userDeviceInfo.getIp();
				mkdb.Procedure.psendWhileCommit(roleid, msg);
				
				return true;
			}
			
		}.submit();*/
		
		new PNotifyDeviceInfo(userid,info,this).submit();
	}
	
	private void kickUser(int error) {
		Kick p1 = new Kick();
		p1.linksid = ((Dispatch)this.getContext()).linksid;
		p1.action = Kick.A_QUICK_CLOSE;
		p1.error = error;
		Onlines.sendProtocl(p1, this.getConnection());
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786514;

	public int getType() {
		return 786514;
	}

	public java.util.ArrayList<fire.pb.UserInfoUnit> info;

	public CNotifyDeviceInfo() {
		info = new java.util.ArrayList<fire.pb.UserInfoUnit>();
	}

	public CNotifyDeviceInfo(java.util.ArrayList<fire.pb.UserInfoUnit> _info_) {
		this.info = _info_;
	}

	public final boolean _validator_() {
		for (fire.pb.UserInfoUnit _v_ : info)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(info.size());
		for (fire.pb.UserInfoUnit _v_ : info) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.UserInfoUnit _v_ = new fire.pb.UserInfoUnit();
			_v_.unmarshal(_os_);
			info.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CNotifyDeviceInfo) {
			CNotifyDeviceInfo _o_ = (CNotifyDeviceInfo)_o1_;
			if (!info.equals(_o_.info)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += info.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(info).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

