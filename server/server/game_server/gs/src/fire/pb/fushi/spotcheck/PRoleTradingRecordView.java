package fire.pb.fushi.spotcheck;

import java.util.Map;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.fushi.SSpotCardConfig;
import fire.pb.main.ConfigManager;

public class PRoleTradingRecordView extends Procedure {
	public static final Logger logger = Logger.getLogger("CARD");
	
	private final long roleid;
	
	public Map<Integer, SSpotCardConfig> sSpotCardConfig = ConfigManager.getInstance().getConf(SSpotCardConfig.class);

	public PRoleTradingRecordView(long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		//不是点卡服，不能打开这个功能
		if(fire.pb.fushi.Module.GetPayServiceType() != 1){
			return false;
		}
		SpotCheckManage.refreshRoleTradingRecordView(roleid);
		return true;
	}
	



	
	
	

}
