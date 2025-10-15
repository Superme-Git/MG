package locojoy.game.mt3.timer;

import java.util.TimerTask;

import locojoy.game.mt3.utils.FileUtils;

/**
 * Ãû³ÆÉú³É
 * 
 * @author liangyanpeng
 *
 */
public class NameTimer extends TimerTask {

	@Override
	public void run() {
		int queueSize = FileUtils.nameQueue.size();
		if (queueSize < 1000) {}
	}

}
