package fire.pb.mission.notify;

import java.util.TimerTask;

public class NotifyTask extends TimerTask{
	
	private final int notifyId;
	private final long roleId;
	
	public NotifyTask(long roleId, int notifyId){
		this.notifyId = notifyId;
		this.roleId = roleId;
	}

	@Override
	public void run() {
		TuiSongNotifyManager.getInstance().notify(notifyId, roleId);
	}
}
