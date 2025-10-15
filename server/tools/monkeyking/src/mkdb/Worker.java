package mkdb;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public final class Worker extends java.lang.Thread {

	private ThreadFactory threadFactory;

	private Worker(ThreadFactory threadFactory, String executorName, Runnable r) {
		super(r);
		this.threadFactory = threadFactory;
		this.setDaemon(true);
		this.setName("mkdb.Worker." + executorName + "." + this.getId());
	}

//	/**
//	 * ���ش������̵߳Ĺ�����
//	 * @return
//	 */
//	public ThreadFactory getThreadFactory() {
//		return threadFactory;
//	}

	@Override
	public void run() {
		//Trace.debug("start " + this);

		workers.put(getId(), this);

		try {
			super.run();

		} catch (Throwable th) {
			mkdb.Trace.error("worker catch Exception", th);
		} finally {
			workers.remove(getId());
		}
	}

	private static ConcurrentMap<Long, Worker> workers = mkdb.util.Misc.newConcurrentMap();

	static Worker get(Long threadid) {
		return workers.get(threadid);
	}

	private AtomicBoolean angel = new AtomicBoolean(false); 

	void angelInterrupt() {
		angel.set(true);
		super.interrupt();
	}

	static boolean angelInterrupted() {
		Thread cur = Thread.currentThread();
		if (cur instanceof Worker) {
			return ((Worker)cur).angel.getAndSet(false);
		}
		return false;
	}

	private static class WorkerFactory implements ThreadFactory {
		private final String executorName;
		WorkerFactory(String executorName) {
			this.executorName = executorName;
		}
		@Override
		public Thread newThread(Runnable r) {
			return new Worker(this, executorName, r);
		}
	};

	/**
	 * ���ݸ��������ִ����̹߳�������������������̶߳����Դ�����Ϊǰ׺������
	 * һ������ ThreadPoolExecutor��
	 * @param executorName
	 * @return
	 */
	public static ThreadFactory newFactory(String executorName) {
		return new WorkerFactory(executorName);
	}

	/**
	 * <p>��ֹ�̼߳��������߳����Լ����ڵ��̳߳�<b>�ύ���񲢵ȴ�</b>�������ʱ���п��ܳ��ּ�����
	 * <p>�������̳߳�ֻ��һ���̣߳����ύ���ȴ������������ʱ����û�и����߳���ִ�г��е�����
	 * ��ô������Զ�ȴ���������̼߳�����
	 * <p>���̳߳����ø�����߳̿��Լ��ټ����Ŀ��ܣ���������ȫ���⼢����
	 * <p>��ֹ�߳����Լ������̳߳��ύ���ȴ����񣬿�����ȫ���⼢����ע�⣬���ύ���񣬲��ȴ���û������ġ�
	 * <p>�����ϸ�ڿ�ʵ�֡�
	 *
	 * @param executor
	 * @see mkdb.util.TimeoutExecutor
	 * @see mkdb.util.ScheduledTimeoutExecutor
	 */
	public final static void debugHunger(ThreadPoolExecutor executor) {
		if (Trace.isDebugEnabled()) {
			final Thread ct = Thread.currentThread();
			if (ct instanceof Worker) {
				final Worker wk = (Worker)ct;
				if (wk.threadFactory == executor.getThreadFactory())
					throw new RuntimeException("Say no to hunger!");
			}
		}
	}
}
