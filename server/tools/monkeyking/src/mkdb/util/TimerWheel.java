package mkdb.util;

import java.util.*;
import java.util.concurrent.*;

/**
 * TODO Timer And Timeout. �ݲ�ʵ���ˣ�����ʱ�ٿ��ǡ�Ҫ��̫���ˡ�
 * <p><b>����</b>
 * <ul>
 * <li>����ע����Ƴ� Timer��
 * <li>���ȣ����롣
 * <li>���������ڴ棬cancel ʱ���ϴӶ������Ƴ���
 * <li>����ִ�з�ʽ��1. �ڵ����߳���ֱ�����С�2. �ŵ��̳߳������С�
 * <li>���������ڿ���Ҫ��ʱ�����ɵ���������� Timer �ڵ���ǰ�ͱ� cancel��������������
 * </ul>
 * <p><b>����</b>
 * ʹ�÷�ʽ��ʵ��ѡ��interface or abstract class or wrapper��
 * �߶Ȳ������������������У���ѡ�����Ǻ���Ҫ��
 * 
 */
public final class TimerWheel {
	private final ScheduledExecutorService service;
	private final Wheel[] wheels;
	private volatile int cursor;
	private final int WHEEL = 500;

	public TimerWheel(ScheduledExecutorService service, int initialCapacity) {
		if (initialCapacity < 0 || initialCapacity > 0x40000000)
			throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);

		int capacity = 1;
		while (capacity < initialCapacity)
			capacity <<= 1;

		this.service = service;
		this.wheels = new Wheel[capacity];
	}

	// ScheduledThreadPoolExecutor
	ScheduledFuture<?> schedule(Runnable command, long delay) {
		return this.schedule(command, delay, TimeUnit.MILLISECONDS);
	}

	ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		if (command == null || unit == null)
			throw new NullPointerException();

		if (delay < 0) delay = 0;
		long index = delay / WHEEL;
		if (index >= wheels.length)
			return service.schedule(command, delay, unit);

		final Task<Object> task = new Task<Object>(command, delay);
		wheels[((int)index + cursor) & (wheels.length - 1)].add(task);
		return task;
	}

	static final class Task<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {
		private final long period;

		Task(Runnable command, long period) {
			super(command, null);
			this.period = period;
		}

		@Override
		public boolean isPeriodic() {
			return 0 != this.period;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return 0;
		}

		@Override
		public int compareTo(Delayed o) {
			return 0;
		}

        /**
         * Runs a periodic task.
         */
        private void runPeriodic() {
        	/*
            boolean ok = super.runAndReset();
            boolean down = isShutdown();
            // Reschedule if not cancelled and not shutdown or policy allows
            if (ok && (!down ||
                       (getContinueExistingPeriodicTasksAfterShutdownPolicy() &&
                        !isTerminating()))) {
                long p = period;
                if (p > 0)
                    time += p;
                else
                    time = now() - p;
                ScheduledThreadPoolExecutor.super.getQueue().add(this);
            }
            // This might have been the final executed delayed
            // task.  Wake up threads to check.
            else if (down)
                interruptIdleWorkers();
            */
        }

        /**
         * Overrides FutureTask version so as to reset/requeue if periodic.
         */
        public void run() {
            if (isPeriodic())
                runPeriodic();
            else
                super.run();
        }
	}

	static interface Wheel {
		public void add(Task<?> task);
		public void remove(Task<?> task);
	}

	static final class WheelList implements Wheel {
		private final List<Task<?>> tasks = new LinkedList<Task<?>>();
		public void add(Task<?> task) {
			tasks.add(task);
		}
		public void remove(Task<?> task) {
			tasks.remove(task);
		}
	}

	static final class WheelPriority implements Wheel {
		private final PriorityQueue<Task<?>> tasks = new PriorityQueue<Task<?>>();
		public WheelPriority(WheelList list) {
			for (Task<?> task : list.tasks)
				this.tasks.add(task);
		}
		public void add(Task<?> task) {
			tasks.add(task);
		}
		public void remove(Task<?> task) {
			tasks.remove(task);
		}
	}

	public static void main(String args[]) throws Exception {
		long min = Long.MAX_VALUE;
		long max = 0;
		long total = 0;
		long count = 1000;
		long elapsed = System.currentTimeMillis();
		for (int i = 0; i < count; ++i) {
			long before = System.currentTimeMillis();
			Thread.sleep(1);
			long d = System.currentTimeMillis() - before;
			if (d < min) min = d;
			if (d > max) max = d;
			total += d;
		}
		elapsed = System.currentTimeMillis() - elapsed;
		System.out.println("min=" + min);
		System.out.println("max=" + max);
		System.out.println("count=" + count);
		System.out.println("total=" + total);
		System.out.println("elapsed=" + elapsed);
	}
}
