package locojoy.game.mt3.async;

/**
 * 异步任务执行
 * 
 * @author liangyanpeng
 *
 */
public abstract class AsyncTask implements java.lang.Runnable {
	
	@Override
	public void run() {
		asyncMethod();
	}

	public abstract void asyncMethod();
	
	
}
