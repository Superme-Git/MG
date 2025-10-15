package fire.pb.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import fire.log.RemoteLogParam;
import fire.pb.main.ConfigManager;
/**
 * 5分钟快照
 *
 */
public class PerFiveMinutesTask extends TimerTask{
	
	public PerFiveMinutesTask(){
	}

	@Override
	public void run() {
		//5分钟快照日志
		Map<String,Object> param = new HashMap<String, Object>();
		param.put(RemoteLogParam.SOURCESERVER, ConfigManager.getGsZoneId());
		param.put(RemoteLogParam.SOURCEPROC, "GS");
		param.put(RemoteLogParam.CURRENTMACHINE, 0);
		int onlinerolenum = 0;
		
		ConfigManager.setOnlineRoleNum(onlinerolenum);
		param.put(RemoteLogParam.CURRENTNUM, ConfigManager.getOnlineRoleNum());
		StringBuilder sb = new StringBuilder();
		param.put(RemoteLogParam.LEVELNUMLIST,sb.toString());
		param.put(RemoteLogParam.MAXNUMONSRV, ConfigManager.getDayMaxRoleNum());
		param.put(RemoteLogParam.FROM, ConfigManager.getGsZoneId());
		param.put(RemoteLogParam.HINT, "");
		
	}

	static List<Integer> xtccList = new ArrayList<Integer>();
	static{
		xtccList.add(fire.log.enums.YYLoggerTuJingEnum.QUICKSHOT.toVal());
	}
	

	
}
