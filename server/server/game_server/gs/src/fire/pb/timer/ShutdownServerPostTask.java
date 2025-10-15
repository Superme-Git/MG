package fire.pb.timer;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import fire.pb.main.Stopper;

/**
 * 关服时的每分钟的循环公告
 * @author nobody
 *
 */
public class ShutdownServerPostTask extends TimerTask{
	
	public ShutdownServerPostTask(){
	}

	@Override
	public void run() {
		
		long stoptime = Stopper.stopTime;
		if(stoptime < 0)
			return;
		long seconds = (stoptime - System.currentTimeMillis())/1000; 
		/* 发全服提示 */
		List<String> pars = new LinkedList<String>();
		pars.add(String.valueOf(Math.round((double)seconds/60)));
		fire.pb.talk.MessageMgr.broadcastMsgNotify(141390, 0, pars); 
		fire.pb.talk.MessageMgr.broadcastMsgNotify(141391, 0, pars); 
	}
	
}
