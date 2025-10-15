package mkdb.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadFactory;

/**
 * �̳��� ThreadPoolExecutor�����������ִ�����е����������������������ʱ�䡣
 * �����������ʱ�䳬������ʱ���᳢���ж�ִ���̡߳������ܱ�֤һ�����жϳɹ���
 */
public class TimeoutExecutor extends ThreadPoolExecutor {
    /**
     * updated only while holding synchronized(this), but
     * volatile to allow concurrent readability even during updates.
     */
	private volatile long defaultTimeout; 

	public synchronized void setDefaultTimeout(long defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}

	public long getDefaultTimeout() {
		return defaultTimeout;
	}

	public TimeoutExecutor(
			long defaultTimeout, int corePoolSize,
			ThreadFactory factory) {
		super(corePoolSize, corePoolSize, 0, TimeUnit.NANOSECONDS,
				new LinkedBlockingQueue<Runnable>(), factory);
		this.defaultTimeout = defaultTimeout;
	}

	/**
	 * 
	 * @param defaultTimeout Ĭ�ϳ�ʱ���� <=0 ʱ��Ĭ�ϲ���ʱ��
	 * @param corePoolSize
	 * @param workQueue
	 * @param factory
	 */
	public TimeoutExecutor(
			long defaultTimeout, int corePoolSize,
			BlockingQueue<Runnable> workQueue, ThreadFactory factory) {
		super(corePoolSize, corePoolSize, 0, TimeUnit.NANOSECONDS, workQueue, factory);
		this.defaultTimeout = defaultTimeout;
	}

	//////////////////////////////////////////////////////////
	// ��ʱ�汾���� ExecutorService ��չ�ӿ�
	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public Future<?> submit(Runnable task, long timeout) {
		mkdb.Worker.debugHunger(this);
		return super.submit(mkdb.Angel.decorateCallable(task, timeout));
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public <T> Future<T> submit(Runnable task, T result, long timeout) {
		mkdb.Worker.debugHunger(this);
		return super.submit(mkdb.Angel.decorate(task, result, timeout));
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public <T> Future<T> submit(Callable<T> task, long timeout) {
		mkdb.Worker.debugHunger(this);
		return super.submit(mkdb.Angel.decorate(task, timeout));
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public void execute(Runnable command, long timeout) {
		super.execute(mkdb.Angel.decorateRunnable(command, timeout));
	}

	//////////////////////////////////////////////////////////
	// ������Щ����Ϊ���е������ṩĬ�ϳ�ʱ��

	@Override
	public Future<?> submit(Runnable task) {
		mkdb.Worker.debugHunger(this);
		return super.submit(mkdb.Angel.decorateCallable(task, defaultTimeout));
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		mkdb.Worker.debugHunger(this);
		return super.submit(mkdb.Angel.decorate(task, result, defaultTimeout));
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		mkdb.Worker.debugHunger(this);
		return super.submit(mkdb.Angel.decorate(task, defaultTimeout));
	}

	@Override
	public void execute(Runnable command) {
		super.execute(mkdb.Angel.decorateRunnable(command, defaultTimeout));
	}
}
