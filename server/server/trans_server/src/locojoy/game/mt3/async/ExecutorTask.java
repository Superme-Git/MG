package locojoy.game.mt3.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import locojoy.game.mt3.utils.Conf;

/**
 * ÈÎÎñÖ´ÐÐÆ÷
 * 
 */
public class ExecutorTask {

	private ExecutorService exec = Executors.newFixedThreadPool(Conf.getThreadNum());
	
	public void excAsync(AsyncTask task) {
		exec.execute(task);
	}
	
}
