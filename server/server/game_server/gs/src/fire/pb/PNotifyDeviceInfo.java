package fire.pb;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import fire.log.LogManager;
import fire.pb.util.InetAddressUtil;
import gnet.link.Onlines;


public class PNotifyDeviceInfo extends mkdb.Procedure{
	public static final Logger logger = Logger.getLogger("SYSTEM");
	private int userid;
	private CNotifyDeviceInfo cNotDevInfo;
	private java.util.ArrayList<fire.pb.UserInfoUnit> info;
	
	public PNotifyDeviceInfo(final int userid, java.util.ArrayList<fire.pb.UserInfoUnit> info, CNotifyDeviceInfo cNotDevInfo){
		this.userid = userid;
		this.info=info;
		this.cNotDevInfo=cNotDevInfo;
	}
	public boolean process() throws Exception {
		
		xbean.AUUserInfo userinfo = xtable.Auuserinfo.get(userid);
		
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
		fire.log.YYLogger.OpLog(userid, 0, "login", first, 0);
		
		
		
        if (userinfo != null){
        	String appsrc = machineInfos.get("appsrc");
        	String mac = machineInfos.get("mac");
//	        	String os = machineInfos.get("os");
			userinfo.setClientsource(appsrc!=null?appsrc:"");
			userinfo.setMac(mac!=null?mac.toUpperCase():"");
//				userinfo.setOs(osType);
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
			
		} catch (Exception e) {
			LogManager.logger.error("notify device info error", e);
		}
		
		final SNotifyDeviceInfo msg = new SNotifyDeviceInfo();
		msg.ip = userDeviceInfo.getIp();
		Onlines.getInstance().sendResponse(cNotDevInfo, msg);
		
		return true;
		
	}
	

}
