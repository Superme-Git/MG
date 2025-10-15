package fire.pb.main;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

/**
 * @author dc
 *
 */
public class RejectedExecHandlerImpl implements RejectedExecutionHandler{
	private static Logger logger = Logger.getLogger("SYSTEM");
	
	@Override
	public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
		logger.info("Rejected Execution!" + task.hashCode());
		//TODO:这里是不是来点策略咧？
	}
	
}
