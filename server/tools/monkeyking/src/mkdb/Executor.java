package mkdb;

import java.util.concurrent.*;

import mkdb.util.ScheduledTimeoutExecutor;
import mkdb.util.TimeoutExecutor;

import java.util.List;

/**
 * 
 * �̳߳ء������������ӡ�
 *
 * <p> ScheduledThreadPoolExecutor scheduled������������Ҫ�ӳٻ��߶�ʱִ�е�����
 * <p> ThreadPoolExecutor procedure��������������ִ�д洢��������XDB�ڲ�����������ʹ��������ӡ�
 * <p> ThreadPoolExecutor Ĭ�ϵģ���������Ӧ�ò������ִ������һ����˵����ִ��Э�飬ֱ�Ӽ̳С�
 * 
 * <p> Ϊ�˼��ݣ�Executor ʵ���˽ӿ� ScheduledExecutorService��������Ҫ schedule ֧�ֵ�����
 *     �ڲ������Щ������ɵ� scheduled ��ִ�С�
 *
 * @see java.util.concurrent.ScheduledThreadPoolExecutor
 * @see java.util.concurrent.ThreadPoolExecutor
 */
public final class Executor extends TimeoutExecutor
		implements ScheduledExecutorService, ExecutorMBean {

	private final ScheduledTimeoutExecutor scheduled;
	private final TimeoutExecutor          procedure;

	private mkdb.util.MBeans.Manager mbeans = new mkdb.util.MBeans.Manager();

	private static Object lock = new Object();
	private static Executor instance;

	public static void start(int defaultTimeout,
			int executorCorePoolSize, int procedureCorePoolSize, int scheduleCorePoolSize,
			int timeoutPeriod) {

		synchronized (lock) {
			if (null == instance) {
				instance = new Executor(defaultTimeout,
					executorCorePoolSize, procedureCorePoolSize, scheduleCorePoolSize,
					timeoutPeriod);
			}
		}
	}

	/**
	 * <p>���� mkdb Ӧ�ã�����Ҫ�������ã�Mkdb.stop ��������������
	 * <p>���� mkio only ��Ӧ�ã���Ҫ�����������������
	 */
	public static void stop() {
		synchronized (lock) {
			if (null != instance) {
				instance.shutdown_protected = false; // allow shutdown now
				instance.shutdown();
				instance = null;
			}
		}
	}

	public static Executor getInstance() {
		return instance;
	}

	@SuppressWarnings("deprecation")
	private Executor(int defaultTimeout,
			int executorCorePoolSize, int procedureCorePoolSize, int scheduleCorePoolSize,
			int timeoutPeriod) {

		super(defaultTimeout, executorCorePoolSize, Worker.newFactory(""));

		this.procedure = new TimeoutExecutor(
				defaultTimeout, procedureCorePoolSize, Worker.newFactory("procedure"));

		this.scheduled = new ScheduledTimeoutExecutor(
				defaultTimeout, scheduleCorePoolSize, Worker.newFactory("scheduled"));

		// ע�⣬timeout ������ͨ��ʱ����Ҳ�ŵ� scheduled �����С�
		this.scheduled.scheduleWithFixedDelay(
				mkdb.util.TimeoutManager.getInstance(),
				timeoutPeriod, timeoutPeriod, TimeUnit.MILLISECONDS);

		mbeans.register(this, "mkdb:type=Mkdb,name=Executor");
	}

	/**
	 * ����֧���ӳ٣�֧�ֳ�ʱ��ִ������
	 * Executor ��Ȼ����� schedule �����Ĳ����������Ƿ�ŵ���������С�
	 * ͨ�� Executor �޷����� ScheduledTimeoutExecutor ���з�������Ҫʱʹ���������ֱ���õ����á�
	 * @return
	 */
	public ScheduledTimeoutExecutor getScheduledTimeoutExecutor() {
		return scheduled;
	}

	/**
	 * ���ش洢����ר��ִ������mkdb ����ʹ���������ִ�д洢���̡�
	 * �磬��ʹ�� mkdb.Procedure��execute,submit��������ʱ��ʹ�����ִ������
	 * ������Щֱ�ӵ��� mkdb.Procedure.call ��������Ĵ洢���̵��ã����ڵ������߳���ִ�С�
	 * @return
	 */
	public TimeoutExecutor getProcedureTimeoutExecutor() {
		return procedure;
	}

	/**
	 * �����̳߳ص�������<B>����maxһ��ʼ�ͱ����óɳ�ʼ��С������ֻ����С�ĵط��ġ�</B>
	 * @param executorCorePoolSize
	 * @param procedureCorePoolSize
	 * @param scheduleCorePoolSize
	 */
	public void setCorePoolSize(
			int executorCorePoolSize, int procedureCorePoolSize, int scheduleCorePoolSize) {
		super.setCorePoolSize(executorCorePoolSize);
		this.procedure.setCorePoolSize(procedureCorePoolSize);
		this.scheduled.setCorePoolSize(scheduleCorePoolSize);
	}

	/**
	 * αװ schedule �Ľ����ʵ���ϴ����񱻷ŵ� ThreadPoolExecutor ��ִ�С�
	 */
	private static class ScheduledFutureWrapper<V> implements ScheduledFuture<V> {
		private final Future<V> future;
		ScheduledFutureWrapper(Future<V> future) {
			this.future = future;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return 0;
		}
		@Override
		public int compareTo(Delayed o) {
			long d = o.getDelay(TimeUnit.NANOSECONDS);
			return (d == 0)? 0 : ((d > 0)? -1 : 1);
		}
		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return future.cancel(mayInterruptIfRunning);
		}
		@Override
		public V get() throws InterruptedException, ExecutionException {
			return future.get();
		}
		@Override
		public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			return future.get(timeout, unit);
		}
		@Override
		public boolean isCancelled() {
			return future.isCancelled();
		}
		@Override
		public boolean isDone() {
			return future.isDone();
		}
		
	}

	@Override
	public boolean remove(Runnable task) {
		if (task instanceof ScheduledFuture<?>)
			return super.remove(task);
		return this.scheduled.remove(task);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		if (delay <= 0)
			return new ScheduledFutureWrapper(super.submit(command));
		return this.scheduled.schedule(command, delay, unit);
	}

	@Override
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		if (delay <= 0)
			return new ScheduledFutureWrapper<V>(super.submit(callable));
		return this.scheduled.schedule(callable, delay, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return this.scheduled.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		return this.scheduled.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}

	////////////////////////////////////////////////////////////////////
	// shutdown process
	// package private.
	private boolean shutdown_protected = true; // ֻ��  Executor.stop() ��������������� Executor����ֹ�������

	@SuppressWarnings("deprecation")
	@Override
	public void shutdown() {
		if (shutdown_protected)
			throw new IllegalStateException("shutdown protected");

		// ʹ�ÿ����˳����ж�����ִ�е�����ȡ�������е�����
		super.shutdownNow();
		this.scheduled.shutdownNow();
		this.procedure.shutdownNow();

		mkdb.util.TimeoutManager.getInstance().clear();
		await(this, "");
		await(this.scheduled, "scheduled");
		await(this.procedure, "procedure");

		mbeans.unregisterAll();
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Runnable> shutdownNow() {
		if (shutdown_protected)
			throw new IllegalStateException("shutdown protected");

		List<Runnable> r = super.shutdownNow();
		r.addAll(this.scheduled.shutdownNow());
		r.addAll(this.procedure.shutdownNow());

		mkdb.util.TimeoutManager.getInstance().clear();
		await(this, "");
		await(this.scheduled, "scheduled");
		await(this.procedure, "procedure");

		mbeans.unregisterAll();

		return r;
	}

	private void await(ThreadPoolExecutor tpe, final String name) {
		for (;;) {
			try {
				if (tpe.awaitTermination(5, TimeUnit.SECONDS))
					return;
				Trace.warn("Executor." + name + ".shutdown timeout. queue.size=" + tpe.getQueue().size());
			} catch (InterruptedException e) {
				Trace.warn("Executor." + name + ".shutdown inprocess. skip InterruptedException");
			}
		}
	}

	// MBean Executor
	@Override
	public int getExecutorActiveCount() {
		return super.getActiveCount();
	}

	@Override
	public long getExecutorCompletedTaskCount() {
		return super.getCompletedTaskCount();
	}

	@Override
	public int getExecutorPoolSize() {
		return super.getPoolSize();
	}

	@Override
	public String getExecutorState() {
		if (super.isShutdown())
			return super.isTerminated() ? "TERMINATED" : "TERMINATING";
		return "RUNNING";
	}

	@Override
	public long getExecutorTaskCount() {
		return super.getTaskCount();
	}

	// MBean Procedure
	@Override
	public int getProcedureActiveCount() {
		return this.procedure.getActiveCount();
	}

	@Override
	public long getProcedureCompletedTaskCount() {
		return this.procedure.getCompletedTaskCount();
	}

	@Override
	public int getProcedurePoolSize() {
		return this.procedure.getPoolSize();
	}

	@Override
	public String getProcedureState() {
		if (this.procedure.isShutdown())
			return this.procedure.isTerminated() ? "TERMINATED" : "TERMINATING";
		return "RUNNING";
	}

	@Override
	public long getProcedureTaskCount() {
		return this.procedure.getTaskCount();
	}

	// MBean Scheduled
	@Override
	public int getScheduledActiveCount() {
		return this.scheduled.getActiveCount();
	}

	@Override
	public long getScheduledCompletedTaskCount() {
		return this.scheduled.getCompletedTaskCount();
	}

	@Override
	public int getScheduledPoolSize() {
		return this.scheduled.getPoolSize();
	}

	@Override
	public String getScheduledState() {
		if (this.scheduled.isShutdown())
			return this.scheduled.isTerminated() ? "TERMINATED" : "TERMINATING";
		return "RUNNING";
	}

	@Override
	public long getScheduledTaskCount() {
		return this.scheduled.getTaskCount();
	}

	// MBean purge
	@Override
	public void purgeExecutor(String iamsure) {
		if (iamsure.equals("iamsure"))
			super.purge();
	}

	@Override
	public void purgeScheduled(String iamsure) {
		if (iamsure.equals("iamsure"))
			this.scheduled.purge();
	}

	@Override
	public void purgeProcedure(String iamsure) {
		if (iamsure.equals("iamsure"))
			this.procedure.purge();
	}

	// MBean corePoolSize
	@Override
	public void setExecutorCorePoolSize(int corePoolSize) {
		super.setCorePoolSize(corePoolSize);
	}

	@Override
	public void setScheduledCorePoolSize(int corePoolSize) {
		this.scheduled.setCorePoolSize(corePoolSize);
	}

	@Override
	public int getProcedureCorePoolSize() {
		return this.procedure.getCorePoolSize();
	}

	@Override
	public void setProcedureCorePoolSize(int corePoolSize) {
		this.procedure.setCorePoolSize(corePoolSize);
	}

	@Override
	public int getExecutorCorePoolSize() {
		return super.getCorePoolSize();
	}

	@Override
	public int getScheduledCorePoolSize() {
		return this.scheduled.getCorePoolSize();
	}

	@Override
	public void testAlive(long timeout)
		throws InterruptedException, ExecutionException, TimeoutException {
		if (timeout < 500)
			timeout = 500;

		Runnable alive = new Runnable() { public void run() { /* do nothing */ } };

		// ���� Executor ��Ҫ�޸����
		super.submit(alive).get(timeout, TimeUnit.MILLISECONDS);
		this.scheduled.submit(alive).get(timeout, TimeUnit.MILLISECONDS);
		this.procedure.submit(alive).get(timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public long getExecutorDefaultTimeout() {
		return super.getDefaultTimeout();
	}

	@Override
	public long getScheduledDefaultTimeout() {
		return this.scheduled.getDefaultTimeout();
	}

	@Override
	public void setExecutorDefaultTimeout(long timeout) {
		super.setDefaultTimeout(timeout);
	}

	@Override
	public void setScheduledDefaultTimeout(long timeout) {
		this.scheduled.setDefaultTimeout(timeout);
	}

	@Override
	public long getProcedureDefaultTimeout() {
		return this.procedure.getDefaultTimeout();
	}

	@Override
	public void setProcedureDefaultTimeout(long timeout) {
		this.procedure.setDefaultTimeout(timeout);
	}
}
