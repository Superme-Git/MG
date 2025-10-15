package mkdb;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.lang.management.ThreadInfo;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import mkdb.util.TimeoutManager;

/**
 * THIS IS NOT AN ANGEL.
 * <p>�������������������жϻ��е��̡߳������ѡ�жϵ��̡߳�ֻ�����̵߳ȴ������ǿɱ��жϵģ��жϲ��ܳɹ���
 */

public final class Angel extends ThreadHelper {
	/**
	 * @return root thread group
	 */
	public static ThreadGroup getRootThreadGroup() {
		ThreadGroup root = Thread.currentThread().getThreadGroup();
		for (ThreadGroup p = root.getParent(); null != p; p = p.getParent())
			root = p;
		return root;
	}

	/**
	 * @return all thread in system
	 */
	public static Map<Long, Thread> getAllThreads() {
		return enumerate(getRootThreadGroup(), true);
	}

	/**
	 * ö��ָ���߳����µ��̡߳�
	 * @param   group     �߳���
     * @param   recurse   a flag indicating whether also to include threads
     *                    in thread groups that are subgroups of the thread group.
	 * @return thread in the group
	 */
	public static Map<Long, Thread> enumerate(ThreadGroup group, boolean recurse) {
		Thread [] threads = new Thread[256];
		while (true) {
			int size = group.enumerate(threads, recurse);
			if (size < threads.length) {
				Map<Long, Thread> m = new HashMap<Long, Thread>();
				for (int i = 0; i < size; ++i) {
					Thread thread = threads[i];
					if (null != thread)
						m.put(thread.getId(), thread);
				}
				return m;
			}
			threads = new Thread[threads.length * 2];
		}
	}

	////////////////////////////////////////////////
	private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
	private final ThreadGroup rootThreadGroup;
	private volatile int interruptCount = 0;
	private int sleepIdleMs = 1000;

	public int getInterruptCount() {
		return interruptCount;
	}

	Angel() {
		super("mkdb.Angel");
		rootThreadGroup = getRootThreadGroup();
	}

	@Override
	public void run() {
		while (isRunning()) {
			try {
				if (detect()) {
					sleepIdleMs = 1000;
				} else {
					sleepIdleMs += 1000;
					if (sleepIdleMs > Mkdb.getInstance().getConf().getAngelPeriod())
						sleepIdleMs = Mkdb.getInstance().getConf().getAngelPeriod();
				}
				sleepIdle(sleepIdleMs);
			} catch (Throwable ex) {
				Trace.error("angel run " + ex);
			}
		}
	}

	/**
	 * ThreadInfo ���ֻ��ӡ��8���ջ��Ϣ see ThreadInfo.MAX_FRAMES
	 */
	private final static int MAXDEPTH = 255;

	/**
	 * ��������������Դ�����������
	 */
	private final boolean detect() {
		// �����������̡߳� ���ܰ���������� �Լ��ȴ����ϵ��̡߳�
		long[] deadlockedThreadIds = threadMXBean.findDeadlockedThreads();
		if (null == deadlockedThreadIds)
			return false;

		// ���������߳���Ϣӳ�䡣
		Map<Long, ThreadInfo> deadlockedThreads = new HashMap<Long, ThreadInfo>();
		for (ThreadInfo tinfo : threadMXBean.getThreadInfo(deadlockedThreadIds, 
					threadMXBean.isObjectMonitorUsageSupported(),
					threadMXBean.isSynchronizerUsageSupported())) {
			try {
				// getLockOwnerId == -1�����ڵȴ��������߳�ӵ�е���. �϶����ǻ���һ���֡�
				// ����������������ܷ�����findDeadlockedThreads�Ľ���С�
				// ������֣�һ���ǲ���������ɵģ����̱߳������ˡ�����򵥵ĺ��Ե���
				if (null != tinfo && tinfo.getLockOwnerId() != -1)
					deadlockedThreads.put(tinfo.getThreadId(), tinfo);
			} catch (Exception e) {
				// �������ʣ� �ڹ��������У��̷߳����˱䶯���������ִ���
				if (Trace.isInfoEnabled())
					Trace.info("angel critical exeption");
			}
		}

		/*
		 * ���е��̡߳������ṩThreadId��Thread��ת����java���ṩ����ת����
		 * mkdb.Worker ����ת�������������Խ��̣߳���Ҫö��ϵͳ�ڵ������̣߳����������жϵȲ�����
		 * �ڼ������У������Ҫʹ�òų�ʼ����������������̱߳���̬���������ٵĲ����ԣ�
		 * ���'���е��߳�'������һ�μ������Ч�����Ҳ���֤���е�ת�����Ҷ��ܳɹ���
		 */
		Map<Long, Thread> allThreads = null;

		// ������������ӻ��������ѡһ���̣߳�����ִ���жϲ�����
		while ( !deadlockedThreads.isEmpty() ) {
			Map<Long, ThreadInfo> cycle = new HashMap<Long, ThreadInfo>();
			ThreadInfo tinfo = deadlockedThreads.entrySet().iterator().next().getValue();
			do {
				if (null != cycle.put(tinfo.getThreadId(), tinfo)) {
					// cycle found.

					// random_sample interrupting thread from cycle.
					//final ThreadInfo[] _cycle_ = cycle.values().toArray(new ThreadInfo[cycle.size()]);
					//final ThreadInfo interruptingThreadInfo = _cycle_[mkdb.Mkdb.random().nextInt(_cycle_.length)];
					final ThreadInfo interruptingThreadInfo = tinfo;

					// dump interrupt info
					StringBuilder sb = new StringBuilder("Angel.interrupt thread \"");
					sb.append(interruptingThreadInfo.getThreadName()).append("\" Id=").append(interruptingThreadInfo.getThreadId());
					sb.append(" in cycle:\n");
					for (ThreadInfo info : cycle.values()) // ��ӡ��ʱ��ѹ��ڻ��ϵ��߳�Ҳ�������
						dumpThreadInfoTo(info, sb);
					Trace.fatal(sb);

					// interrupt thread
					allThreads = interrupt(interruptingThreadInfo, allThreads);

					// break and try to find another cycle
					break;
				}
			} while ((tinfo = deadlockedThreads.get(tinfo.getLockOwnerId())) != null);
			// ɾ���Ѿ���������̡߳�cycle�������Ļ�����������Щ�ȴ��Ļ��ѱ�����ʣ�µĹ���֦�ڡ�
			deadlockedThreads.keySet().removeAll(cycle.keySet());
		}
		return true;
	}

	private void dumpThreadInfoTo(ThreadInfo tinfo, StringBuilder sb) {
		sb.append("\"" + tinfo.getThreadName() + "\"" + " Id="
				+ tinfo.getThreadId() + " " + tinfo.getThreadState());
		if (tinfo.getLockName() != null) {
			sb.append(" on " + tinfo.getLockName());
		}
		if (tinfo.getLockOwnerName() != null) {
			sb.append(" owned by \"" + tinfo.getLockOwnerName() + "\" Id=" + tinfo.getLockOwnerId());
		}
		if (tinfo.isSuspended()) {
			sb.append(" (suspended)");
		}
		if (tinfo.isInNative()) {
			sb.append(" (in native)");
		}
		sb.append('\n');
		StackTraceElement[] stackTrace = tinfo.getStackTrace();
		int i = 0;
		for (; i < stackTrace.length && i < MAXDEPTH; i++) {
			StackTraceElement ste = stackTrace[i];
			sb.append("\tat " + ste.toString());
			sb.append('\n');
			if (i == 0 && tinfo.getLockInfo() != null) {
				Thread.State ts = tinfo.getThreadState();
				switch (ts) {
				case BLOCKED:
					sb.append("\t-  blocked on " + tinfo.getLockInfo());
					sb.append('\n');
					break;
				case WAITING:
					sb.append("\t-  waiting on " + tinfo.getLockInfo());
					sb.append('\n');
					break;
				case TIMED_WAITING:
					sb.append("\t-  waiting on " + tinfo.getLockInfo());
					sb.append('\n');
					break;
				default:
				}
			}

			for (MonitorInfo mi : tinfo.getLockedMonitors()) {
				if (mi.getLockedStackDepth() == i) {
					sb.append("\t-  locked " + mi);
					sb.append('\n');
				}
			}
		}
		if (i < stackTrace.length) {
			sb.append("\t...");
			sb.append('\n');
		}

		LockInfo[] locks = tinfo.getLockedSynchronizers();
		if (locks.length > 0) {
			sb.append("\n\tNumber of locked synchronizers = " + locks.length);
			sb.append('\n');
			for (LockInfo li : locks) {
				sb.append("\t- " + li);
				sb.append('\n');
			}
		}
		sb.append('\n');
	}

	/**
	 * �ж��̣߳��Ե����д���
	 */
	private final Map<Long, Thread> interrupt(ThreadInfo tinfo, Map<Long, Thread> allThreads) {
		try {
			Worker worker = Worker.get(tinfo.getThreadId());
			if (worker != null) {
				worker.angelInterrupt();
				interruptCount ++;
				return allThreads;
			}

			// �������̲߳���Workerʱ������ϵͳ�ڵ������̣߳�Ҳ�����жϡ�
			if (null == allThreads)
				allThreads = enumerate(rootThreadGroup, true);

			Thread thread = allThreads.get(tinfo.getThreadId());
			if (null != thread) {
				thread.interrupt();
				interruptCount ++;
				return allThreads;
			}
			if (Trace.isInfoEnabled())
				Trace.info("Angle.logonly: thread not found. " + tinfo);

		} catch ( Throwable e ) {
			Trace.fatal("Angle.interrupt " + tinfo, e);
		}
		return allThreads;
	}

	/**
	 * ���ϳ�ʱ���Ρ�������ִ��ʱ���м�ʱ���������ʱ�������ж�����
	 * ���timeout <= 0������������ֱ�ӷ��ء�
	 * @param <V>
	 * @param task
	 * @param timeout
	 * @return decorate task with timeout if timeout > 0. 
	 */
	public static <V> Callable<V> decorate(Callable<V> task, long timeout) {
		return timeout > 0 ? new TimeoutCallable<V>(task, timeout) : task;
	}

	/**
	 * ���ϳ�ʱ���Ρ�������ִ��ʱ���м�ʱ���������ʱ�������ж�����
	 * ���timeout <= 0������������ֱ�ӷ��ء�
	 * @param <V>
	 * @param task
	 * @param timeout
	 * @return decorate task with timeout if timeout > 0. 
	 */
	public static <V> Callable<V> decorate(Runnable task, V result, long timeout) {
		final Callable<V> callable = Executors.callable(task, result);
		return timeout > 0 ? new TimeoutCallable<V>(callable, timeout) : callable;
	}

	/**
	 * ���ϳ�ʱ���Ρ�������ִ��ʱ���м�ʱ���������ʱ�������ж�����
	 * ���timeout <= 0������������ֱ�ӷ��ء�
	 * @param <V>
	 * @param task
	 * @param timeout
	 * @return decorate task with timeout if timeout > 0. 
	 */
	public static Callable<?> decorateCallable(Runnable task, long timeout) {
		return decorate(task, null, timeout);
	}

	/**
	 * ���ϳ�ʱ���Ρ�������ִ��ʱ���м�ʱ���������ʱ�������ж�����
	 * ���timeout <= 0������������ֱ�ӷ��ء�
	 * <b>ThreadPoolExecutor.execute ר�ð汾�������ٴ���һ�� Callable ����</b>
	 * @param task
	 * @param timeout
	 * @return decorate task with timeout if timeout > 0.
	 */
	public static Runnable decorateRunnable(Runnable task, long timeout) {
		return timeout > 0 ? new TimeoutRunnable(task, timeout) : task;
	}

	static class TimeoutRunnable implements Runnable, TimeoutManager.Timeout {
		private final Runnable inner;
		private final long timeout;
		private volatile Thread runner;

		public TimeoutRunnable(Runnable task, long timeout) {
			if (null == task)
				throw new NullPointerException();
			this.inner = task;
			this.timeout = timeout;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			if (timeout > 0) {
				runner = Thread.currentThread();
				final TimeoutManager tm = TimeoutManager.getInstance();
				tm.schedule(this, timeout);
				try {
					inner.run();
				} finally {
					tm.remove(this);
					runner = null;
				}
			} else
				inner.run();
		}

		@Override
		public void onTimeout() {
			final Thread r = runner;
			if (r != null) {
				if (r instanceof mkdb.Worker) {
					((mkdb.Worker)r).angelInterrupt();
				} else
					r.interrupt();
			}
		}
	}

	static class TimeoutCallable<V> implements Callable<V>, TimeoutManager.Timeout {
		private final Callable<V> inner;
		private final long timeout;
		private volatile Thread runner;

		public TimeoutCallable(Callable<V> task, long timeout) {
			if (null == task)
				throw new NullPointerException();
			this.inner = task;
			this.timeout = timeout;
		}

		@SuppressWarnings("deprecation")
		@Override
		public V call() throws Exception {
			if (timeout > 0) {
				runner = Thread.currentThread();
				final TimeoutManager tm = TimeoutManager.getInstance();
				tm.schedule(this, timeout);
				try {
					return inner.call();
				} finally {
					tm.remove(this);
					runner = null;
				}
			} else
				return inner.call();
		}

		@Override
		public void onTimeout() {
			final Thread r = runner;
			if (r != null) {
				if (r instanceof mkdb.Worker) {
					((mkdb.Worker)r).angelInterrupt();
				} else
					r.interrupt();
			}
		}
	}
}
