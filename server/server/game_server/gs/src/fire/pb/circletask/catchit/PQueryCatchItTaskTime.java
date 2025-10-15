package fire.pb.circletask.catchit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.hook.RoleHookManager;
import mkdb.Procedure;



public class PQueryCatchItTaskTime extends Procedure {
	private final long roleId;
	private final int npcId;
	private final int circletype;
	
	public PQueryCatchItTaskTime(final long roleId, final int npcId, final int circletype) {
		this.roleId = roleId;
		this.npcId = npcId;
		this.circletype = circletype;
	}

	public boolean process() {
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleId, circletype);;
		if(ct == null) {
			return false;
		}
		
		int doublePoint = 1;
		if (ct.doublepoint > 0)
			doublePoint = ct.doublepoint;
		
		List<String> param = new ArrayList<String>();
		
		short dpoint = RoleHookManager.getInstance().getAllDpoint(roleId);
		
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleId);
		if (quest_map == null) {
			param.add(String.valueOf(0));
			param.add(String.valueOf(dpoint));
			param.add(String.valueOf(ct.awardtime));
			param.add(String.valueOf(dpoint/doublePoint));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleId, 150173, npcId, param);
			return true;
		}
			
		xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(circletype);
		if (sqinfo == null) {
			param.add(String.valueOf(0));
			param.add(String.valueOf(dpoint));
			param.add(String.valueOf(ct.awardtime));
			param.add(String.valueOf(dpoint/doublePoint));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleId, 150173, npcId, param);
			return true;
		}
		
		final long now = Calendar.getInstance().getTimeInMillis();
		if (!fire.pb.util.DateValidate.inTheSameDay(sqinfo.getTakequesttime(), now)) {
			sqinfo.setSumnum(0);
			sqinfo.setCatchitsingletime(0);
			sqinfo.setCatchitdoubletime(0);
		}
		
		int danbei = ct.awardtime - sqinfo.getCatchitsingletime();
		if (danbei < 0) {
			danbei = 0;
		}
		
		param.add(String.valueOf(sqinfo.getCatchitsingletime()));
		param.add(String.valueOf(dpoint));
		param.add(String.valueOf(danbei));
		param.add(String.valueOf(dpoint/doublePoint));
		fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleId, 150173, npcId, param);
		
		return true;
	}
}
