package fire.pb.school.shouxi;

/***
 * 挑战结束时刷新 by changhao
 */
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

public class RefreshProfessionLeaderTask extends TimerTask {

	@Override
	public void run(){
         try {
			new PRefreshProfessionLeader().submit().get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
	}

}
