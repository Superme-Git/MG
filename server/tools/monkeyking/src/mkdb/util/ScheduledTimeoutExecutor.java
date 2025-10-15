package mkdb.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ScheduledTimeoutExecutor extends ScheduledThreadPoolExecutor {
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

	public ScheduledTimeoutExecutor(
			long defaultTimeout, int corePoolSize, ThreadFactory factory) {
		super(corePoolSize);
		super.setThreadFactory(factory);
		this.defaultTimeout = defaultTimeout;
	}

	/*
	 * ע�⣬�����ʵ�ֲ������ö�Ӧ��super��������Ϊ�� ScheduledThreadPoolExecutor ���ڲ�ʵ��
	 * ���ǵ��� schedule������Ҳֱ�Ӿ͵����������jdk��ʵ���оֲ��Ż������ַ�ʽ����������ᶪʧ�Ż���
	 * ����ٶ� jdk ������ô���ˡ�
	 */

	//////////////////////////////////////////////////////////
	// ��ʱ�汾�� executor ��չ�ӿڡ�
	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public Future<?> submit(Runnable task, long timeout) {
		mkdb.Worker.debugHunger(this);
		return super.schedule(mkdb.Angel.decorateCallable(task, timeout), 0, TimeUnit.NANOSECONDS);
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public <T> Future<T> submit(Runnable task, T result, long timeout) {
		mkdb.Worker.debugHunger(this);
		return super.schedule(mkdb.Angel.decorate(task, result, timeout), 0, TimeUnit.NANOSECONDS);
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public <T> Future<T> submit(Callable<T> task, long timeout) {
		mkdb.Worker.debugHunger(this);
		return super.schedule(mkdb.Angel.decorate(task, timeout), 0, TimeUnit.NANOSECONDS);
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public void execute(Runnable command, long timeout) {
		super.schedule(mkdb.Angel.decorateCallable(command, timeout), 0, TimeUnit.NANOSECONDS);
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, long timeout) {
		return super.schedule(mkdb.Angel.decorate(callable, timeout), delay, unit);
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit, long timeout) {
		return super.schedule(mkdb.Angel.decorateCallable(command, timeout), delay, unit);
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, long timeout) {
		return super.scheduleAtFixedRate(mkdb.Angel.decorateRunnable(command, timeout), initialDelay, period, unit);
	}

	/**
	 * �� timeout <= 0��������ʱ���
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit, long timeout) {
		return super.scheduleWithFixedDelay(mkdb.Angel.decorateRunnable(command, timeout), initialDelay, delay, unit);
	}

	//////////////////////////////////////////////////////////
	// ������Щ����Ϊ���е������ṩĬ�ϳ�ʱ��

	@Override
	public Future<?> submit(Runnable task) {
		mkdb.Worker.debugHunger(this);
		return super.schedule(mkdb.Angel.decorateCallable(task, defaultTimeout), 0, TimeUnit.NANOSECONDS);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		mkdb.Worker.debugHunger(this);
		return super.schedule(mkdb.Angel.decorate(task, result, defaultTimeout), 0, TimeUnit.NANOSECONDS);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		mkdb.Worker.debugHunger(this);
		return super.schedule(mkdb.Angel.decorate(task, defaultTimeout), 0, TimeUnit.NANOSECONDS);
	}

	@Override
	public void execute(Runnable command) {
		super.schedule(mkdb.Angel.decorateCallable(command, defaultTimeout), 0, TimeUnit.NANOSECONDS);
	}

	@Override
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		return super.schedule(mkdb.Angel.decorate(callable, defaultTimeout), delay, unit);
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return super.schedule(mkdb.Angel.decorateCallable(command, defaultTimeout), delay, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return super.scheduleAtFixedRate(mkdb.Angel.decorateRunnable(command, defaultTimeout), initialDelay, period, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		return super.scheduleWithFixedDelay(mkdb.Angel.decorateRunnable(command, defaultTimeout), initialDelay, delay, unit);
	}
}
