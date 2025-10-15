package mkdb.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * ����ConcurrentCache
 *
 */
public class ConcurrentCaches {
	private Worker worker;

	private Map<String, ConcurrentCache<?, ?>> ccaches = new HashMap<String, ConcurrentCache<?, ?>>();

	private static Object lock = new Object();
	private static ConcurrentCaches instance;

	public static ConcurrentCaches getInstance() {
		return instance;
	}

	protected void onStart() {
	}

	protected void onStop() {
		
	}

	public static ConcurrentCaches start() {
		synchronized (lock) {
			if (null != instance)
				throw new RuntimeException("ConcurrentCaches already load.");

			try {
				Class<?> cls = Class.forName("xcache._ConcurrentCaches_");
				Object inst = cls.newInstance();
			
				if (!(inst instanceof ConcurrentCaches))
					throw new RuntimeException("invalid ConcurrentCaches - subclass.");
				instance = (ConcurrentCaches)inst;
			} catch (Exception ex) {
				throw new RuntimeException("ConcurrentCaches.start", ex);
			}
			instance._start();
			return instance;
		}
	}

	private final void _start() {
		this.onStart();

		// setup clean timer
		for (ConcurrentCache<?, ?> cc : this.ccaches.values()) {
			cc.setupCleanTimer();
		}

		this.worker = new Worker();
		this.worker.start();

		mkdb.Trace.fatal("mkdb.util.ConcurrentCaches start end.");
		
	}

	public static void stop() {
		synchronized (lock) {
			if (null != instance) {
				instance._stop();
				mkdb.Trace.fatal("mkdb.util.ConcurrentCaches stop end.");
				instance = null;
			}
		}
	}

	private final void _stop() {
		this.onStop();
		for (ConcurrentCache<?, ?> cc : ccaches.values())
			cc.close();
		if (null != this.worker) {
			this.worker.shutdown();
			this.worker = null;
		}
	}

	/**
	 * ��cacheʵ�����뵽�������С�
	 * @param ccache
	 */
	public synchronized void add(ConcurrentCache<?, ?> ccache) {
		if (ccaches.get(ccache.getName()) != null)
			throw new RuntimeException("duplicate ccache! ccache='" + ccache.getName() + "'");
		ccaches.put(ccache.getName(), ccache);
	}

	/**
	 * �Ѹ��µ�ά��������뵽�������Ķ����С��˷������� start�ɹ�����Ч��
	 * @param task
	 */
	public final void add(Runnable task) {
		worker.queue.add(task);
	}

	static class Worker extends mkdb.ThreadHelper {
		private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

		Worker() {
			super("mkdb.util.ConcurrentCacheManager");
		}
		@Override
		public void run() {
			while (super.isRunning()) {
				try {
					Runnable task = queue.poll(50, TimeUnit.MILLISECONDS);
					if (null == task)
						continue;
					task.run();
				} catch (Throwable e) {
					mkdb.Trace.error("ConcurrentCacheManager.Worker.run()", e);
				}
			}
		}
	}

}
