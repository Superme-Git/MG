package fire.pb.mission.instance.line;

import java.util.Map;

import fire.pb.mission.SDropInstance;
import fire.pb.mission.SLineTask;
import mkdb.Procedure;

public class PSendResetLineTask extends Procedure {

	private final long roleid;
	
	public PSendResetLineTask(final long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.get(roleid);
		if (infoCol == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 166096, null);
			return false;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<Integer, xbean.InstanceTimeAwardInfo> awardInfo : infoCol.getInstcount().entrySet()) {
			if (awardInfo.getValue() == null)
				continue;
			
			if (awardInfo.getValue().getStepawardtimes().size() == 0)
				continue;
			
			SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(awardInfo.getKey());
			if (taskCofig == null) {
				LineInstManager.logger.error("副本数据为空 " + awardInfo.getKey());
				return false;
			}
			
			sb.append(taskCofig.taskname).append(",");
		}
		
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.lastIndexOf(","));
			
			SDropInstance sdropInst = new SDropInstance(166071, sb.toString());
			mkdb.Procedure.psendWhileCommit(roleid, sdropInst);
		}
		else {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 166096, null);
		}
		
		return true;
	}
	
	
}
