package fire.pb.fushi.spotcheck;

import java.util.Map;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.fushi.SSpotCardConfig;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;

public class PTradingSpotCardView extends Procedure {
	public static final Logger logger = Logger.getLogger("CARD");
	
	private final long roleid;
	
	public Map<Integer, SSpotCardConfig> sSpotCardConfig = ConfigManager.getInstance().getConf(SSpotCardConfig.class);

	public PTradingSpotCardView(long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		//不是点卡服，不能打开这个功能
		if(fire.pb.fushi.Module.GetPayServiceType() != 1){
			return false;
		}
		//兑换所需要玩家进行一次充值后才开启，需要服务器添加限制
		if(!SpotCheckManage.canOpenFun(roleid)){
			MessageMgr.sendMsgNotify(roleid, 300011, null);
			return false;
		}
		SpotCheckManage.refreshTradingSpotCardView(roleid);
		return true;
	}
	



	
	
	

}
