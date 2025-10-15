package fire.pb.fushi.payday;

import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

public class RefreshDayPayTask extends TimerTask {
	
	public java.util.Set<Long> setroleids;
	public int id;
	
	RefreshDayPayTask(java.util.Set<Long> setroleids, int id)
	{
		this.setroleids = setroleids;
		this.id = id;
	}
	
	@Override
	public void run(){
         try {
			new PRefreshAllDayPayByRoleIds(setroleids, id).submit().get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
	}

}
