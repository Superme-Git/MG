package fire.pb.mission.instance.line;

import java.util.Map;

import mkdb.Procedure;

public class PResetLineTask extends Procedure {

	private final long roleid;
	
	public PResetLineTask(final long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.get(roleid);
		if (infoCol == null) {
			return false;
		}
		
		boolean reset = false;
		for (Map.Entry<Integer, xbean.InstanceTimeAwardInfo> awardInfo : infoCol.getInstcount().entrySet()) {
			if (awardInfo.getValue() == null)
				continue;
			
			if (awardInfo.getValue().getStepawardtimes().size() == 0)
				continue;
			
			awardInfo.getValue().getStepawardtimes().clear();
			reset = true;
		}
		
		for (xbean.InstanceTaskInfo instInfo : infoCol.getInstinfo().values()) {
			//先找出最大的次数
			Integer maxVal = 0;
			for (Integer times : instInfo.getStepawardtimes().values()) {
				if (times > maxVal)
					maxVal = times;
			}
			
			for (Map.Entry<Integer, Integer> stepAward : instInfo.getStepawardtimes().entrySet()) {
				stepAward.setValue(maxVal);
			}
		}
		
		if (reset) {
			LineInstManager.logger.info(new StringBuilder().append("重置精英副本, roleid:").append(roleid));
			infoCol.setReset(1);
			fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 166098, null);
		}
		
		return true;
	}
}
