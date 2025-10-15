package fire.pb.timer;
import java.util.TimerTask;

import fire.pb.state.PEndOfflineProtect;
public class OfflineProtectTask extends TimerTask {
	private final long roleid;
	
	public OfflineProtectTask(long roleid){
		this.roleid = roleid;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
//		final int state = xtable.Roleonoffstate.select(roleid);
//		if (state == fire.pb.State.OFFLINEPROTECT_STATE){
//			final fire.pb.PreOfflineProtectState nextState = new fire.pb.PreOfflineProtectState(roleid);
//			nextState.enter();
//			new PAddOfflineFuture(roleid, null, false).submit();
//		}
		//10分钟保护时间到
		new PEndOfflineProtect(roleid).submit();
	}
}
