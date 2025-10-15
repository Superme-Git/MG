package fire.pb.fushi.monthcard;

import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

public class RefreshMonthCardTask extends TimerTask {
	
	@Override
	public void run(){
         try {
			new PRefreshAllMonthCard().submit().get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
	}

}
