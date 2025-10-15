package fire.pb.activity.winner;

import fire.pb.util.TaskDlgUtil;
import mkdb.Procedure;

public class PClearWinnerTask extends Procedure {

	private final long roleid;

	public PClearWinnerTask(long roleId) {

		this.roleid = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		long currentTime = System.currentTimeMillis();
		if (!WinnerManager.getInstance().isInWinnerActiveTime(currentTime)) {
			if (TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID)||TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID_ACCEPT)){
				WinnerManager.getInstance().abandonWinnerTask(roleid);
			}
		}else {
			WinnerRoleRecord record =WinnerManager.winnerrolerecords.get(roleid);
			if (record!=null){
				if (!WinnerManager.getInstance().isInWinnerActiveTime(record.getTime())) 
					if (TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID)||TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID_ACCEPT)){
						WinnerManager.getInstance().abandonWinnerTask(roleid);
					}
			}
		}
		return true;
	}

}
