package fire.pb.school.shouxi;

import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

public class RefreshShouxiTask extends TimerTask {
    private static Logger logger=Logger.getLogger(RefreshShouxiTask.class);
	@Override
	public void run(){
         try {
			new PRefreshAllShouxis().submit().get();
		} catch (InterruptedException e) {
			logger.debug("RefreshShouxiTask failed:vote end time"+ProfessionLeaderManager.getInstance().getVoteEndTime(), e);
		} catch (ExecutionException e) {
			logger.debug("RefreshShouxiTask failed:vote end time"+ProfessionLeaderManager.getInstance().getVoteEndTime(), e);
		}

		ProfessionLeaderManager.getInstance().adjustVoteChallengeTime();
	}

}
