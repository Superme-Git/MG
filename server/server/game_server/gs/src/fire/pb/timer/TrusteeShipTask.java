package fire.pb.timer;
import java.util.TimerTask;

import fire.pb.state.PEndTrusteeShip;
//import fire.pb.state.PEndOfflineProtect;
public class TrusteeShipTask extends TimerTask {
	private final long roleid;
	
	public TrusteeShipTask(long roleid){
		this.roleid = roleid;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
//		final int state = xtable.Roleonoffstate.select(roleid);
//		if (state == fire.pb.State.TRUSTEESHIP_STATE){
//			final fire.pb.PreTrusteeShipState nextState = new fire.pb.PreTrusteeShipState(roleid);
//			nextState.enter();
//			new PAddOfflineFuture(roleid, null, false).submit();
//		}
		//10分钟保护时间到
		new PEndTrusteeShip(roleid).submit();
	}
}
