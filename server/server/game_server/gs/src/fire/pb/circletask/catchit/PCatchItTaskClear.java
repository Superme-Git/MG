package fire.pb.circletask.catchit;

import java.util.Calendar;

import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.SpecialQuestID;
import fire.pb.circletask.SpecialQuestState;
import mkdb.Procedure;

public class PCatchItTaskClear extends Procedure {
	private final long roleId;
	
	public PCatchItTaskClear(final long roleid) {
		this.roleId = roleid;
	}

	public boolean process() {
		pexecuteWhileCommit(new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleId);
				if (quest_map == null)
					return false;
				xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(SpecialQuestID.catchIt);
				if (sqinfo != null) {
					final long now = Calendar.getInstance().getTimeInMillis();
					if (sqinfo.getQueststate() != 0 && sqinfo.getQueststate() != SpecialQuestState.FAIL) {
						sqinfo.setSumnum(1);						
					}
					else {
						sqinfo.setSumnum(0);						
					}
					
//					sqinfo.setTakequesttime(now);
					sqinfo.setFirsttasktime(now);
					sqinfo.setSametimes(0);
					CircleTaskManager.getInstance().refresh2role(sqinfo, roleId, false);
				}
				return true;
			}
		});
		return true;
	}
}