package com.locojoy.iplimit;
import com.world2.utils.*;
public class IpSpeedCtrl {
	//SpeedLimit<String> querySpeedLimit;
	//SpeedLimit<String> RegisterSpeedLimit;
	SpeedLimit<String> UploadSpeedLimit;
	//SpeedLimit<String> UpdateSpeedLimit;
	private IpSpeedCtrl () {
		UploadSpeedLimit = new SpeedLimit<String>();
		UploadSpeedLimit.config(Integer.parseInt(UploadConfig.getInstance().getUploadWindow()),Integer.parseInt(UploadConfig.getInstance().getUploadWindowCountLimit()));
		LogUtil.logger.info(UploadConfig.getInstance().getUploadWindow()+","+UploadConfig.getInstance().getUploadWindowCountLimit());
	}
	private static IpSpeedCtrl instance = null; 
	public static synchronized IpSpeedCtrl getInstance() {
		if (instance == null) {
			instance = new IpSpeedCtrl();
		}
		return instance;
	}
	
	public SpeedLimit<String> getUploadSpeedLimit() {
		return UploadSpeedLimit;
	}
}
