package fire.pb.mission.instance.line;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.main.ConfigManager;
import fire.pb.mission.SLineTask;
import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;
import mkdb.Procedure;

public class PQueryLineTime extends Procedure {

	private final long roleid;
	
	public PQueryLineTime(final long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(231);
		if (common == null)
			return false;
		
		int commonCnt = Integer.parseInt(common.getValue());
		
		xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.select(roleid);
		if (infoCol == null) {
			List<String> para = new ArrayList<String>(1);
			para.add(String.valueOf(commonCnt));
			MessageMgr.psendMsgNotify(roleid, 166129, para);
			return false;
		}
		
		long currentTime = System.currentTimeMillis();
		if (!DateValidate.inTheSameDay(infoCol.getAccepttime(), currentTime)) {
			List<String> para = new ArrayList<String>(1);
			para.add(String.valueOf(commonCnt));
			MessageMgr.psendMsgNotify(roleid, 166129, para);
			return false;
		}
		
		List<LineInstData> lineData = new ArrayList<LineInstData>(commonCnt);
		
		for (Map.Entry<Integer, xbean.InstanceTimeAwardInfo> awardInfo : infoCol.getInstcount().entrySet()) {
			List<Integer> steplist = LineInstManager.getUndoStep(roleid, awardInfo.getKey());
			if (steplist != null && steplist.size() != 0) {
				SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(awardInfo.getKey());
				if (taskCofig == null) {
					return false;
				}
				
				LineInstData ld = new LineInstData(taskCofig.taskname, steplist);
				lineData.add(ld);
			}
		}
		
		if (lineData.size() == 1) {
			List<String> para = new ArrayList<String>(3);
			para.add(String.valueOf(commonCnt - infoCol.getCounts()));
			for (LineInstData ld : lineData) {
				para.add(String.valueOf(ld.getInstname()));
				para.add(getStepString(ld.getStep()));
			}
			
			MessageMgr.psendMsgNotify(roleid, 166132, para);
		}
		else if (lineData.size() == 0) {
			List<String> para = new ArrayList<String>(1);
			para.add(String.valueOf(commonCnt - infoCol.getCounts()));
			MessageMgr.psendMsgNotify(roleid, 166129, para);
		}
		else {
			List<String> para = new ArrayList<String>(5);
			para.add(String.valueOf(commonCnt - infoCol.getCounts()));
			for (LineInstData ld : lineData) {
				para.add(String.valueOf(ld.getInstname()));
				para.add(getStepString(ld.getStep()));
			}
			
			MessageMgr.psendMsgNotify(roleid, 166133, para);
		}
		
		return true;
	}
	
	private String getStepString(List<Integer> steplist) {
		StringBuilder sb = new StringBuilder();
		for (Integer step : steplist) {
			sb.append("关卡").append(step).append(",");
		}
		
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		
		return sb.toString();
	}
	
}
