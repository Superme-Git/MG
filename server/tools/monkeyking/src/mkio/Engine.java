package mkio;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 管理 Selector。 管理网络配置。 管理事件派发：如果 attachment 是 Handle，就派发事件，否则，关闭连接。
 * 
 */
public final class Engine {
	private static Engine engine = new Engine();

	private volatile SelectorThread[] selectors = null;
	private AtomicInteger registerCount = new AtomicInteger();
	private ConcurrentMap<String, MkioConf> confs = mkdb.util.Misc.newConcurrentMap();
	private mkdb.util.MBeans.Manager mkiombeans = new mkdb.util.MBeans.Manager();

	private Engine() {
	}

	public static Engine getInstance() {
		return engine;
	}

	public static mkdb.util.MBeans.Manager mbeans() {
		return engine.mkiombeans;
	}

	/**
	 * 注册 XioConf 到引擎中。必须在 open 之前完成所有的配置注册。 当 Engine 关闭以后配置会被清除，需要重新注册。
	 * 
	 * @param conf
	 */
	public void register(MkioConf conf) {
		if (null != confs.putIfAbsent(conf.getName(), conf))
			throw new RuntimeException("duplicate XioConf name=" + conf.getName());
	}

	// public static XioConf getXioConf(String conf) {
	// return engine.confs.get(conf);
	// }
	//
	public static Manager getManager(String conf, String manager) {
		MkioConf x = engine.confs.get(conf);
		if (null == x)
			return null;
		return x.getManager(manager);
	}

	private static int DEFAULT_THREAD_NUMBER = 4;

	/**
	 * XioConf 装载配置的时候调用。 初始化流程的一部分。
	 */
	public synchronized void open() {
		open(DEFAULT_THREAD_NUMBER);
	}

	/**
	 * @param threadNumber
	 *            引擎线程数量。会被修订成 2 的幂。
	 */
	public synchronized void open(int threadNumber) {
		if (null != selectors)
			return;

		int capacity = 1;
		while (capacity < threadNumber)
			capacity <<= 1;
		if (capacity > 32)
			capacity = 32; // throw new IllegalArgumentException("BAD!
							// threadNumber > 32.");

		selectors = new SelectorThread[capacity];
		for (int i = 0; i < capacity; ++i) {
			selectors[i] = new SelectorThread("mkio.Engine." + i);
		}

		for (SelectorThread st : selectors) {
			st.start();
		}

		for (MkioConf conf : confs.values()) {
			conf.open();
		}

		mkdb.Trace.info("mkio.Engine started");
	}

	// public synchronized int getThreadNumber() {
	// return selectors.length;
	// }

	public synchronized void close() {
		if (null == selectors)
			return;

		for (MkioConf conf : confs.values())
			conf.close();

		confs.clear();

		for (SelectorThread sel : selectors) {
			sel.close();
		}

		selectors = null;
		this.mkiombeans.unregisterAll();
		mkdb.Trace.info("mkio.Engine stopped");
	}

	public static boolean isOpen() {
		// 不独立使用, 总是由 Mkdb 启动和停止.
		// Mkdb 的 isOpen 在开始stop就会设置，防止停止过程中的问题。
		// 把网络完全独立出来的话，这里就不需要判断 mkdb.Mkdb.isOpen();
		return // mkdb.Mkdb.isOpen() &&
		(null != engine.selectors);
	}

	public static void verify() {
		if (false == isOpen())
			throw new IllegalStateException("mkio.Engine is closed.");
	}

	///////////////////////////////////////////////////////////////////////
	// 应用接口

	/**
	 * not used
	 * 
	 * @return
	 */
	static SelectorThread[] selectors() {
		return engine.selectors;
	}

	/**
	 * 得到一个 Selector 线程。
	 * 
	 * @return
	 */
	static SelectorThread selector() {
		final int count = engine.registerCount.incrementAndGet();
		final int index = count & (engine.selectors.length - 1);
		return engine.selectors[index];
	}

	////////////////////////////////////////////////////////
	// Selector 事件监视和派发。内部实现。不公开。
	// private final static class Impl extends mkdb.ThreadHelper {
	// private Selector selector;
	// private Object registerGuard = new Object();
	//
	// Impl() {
	// super("mkio.Engine");
	// try {
	// selector = Selector.open();
	// } catch (java.io.IOException e) {
	// throw new IOError(e);
	// }
	// }
	//
	// @Override
	// public void run() {
	// while (isRunning()) {
	// try {
	// // 如果注册正在进行中，不要进入 select()。在这里等待完成。
	// synchronized (registerGuard) { }
	// // 如果这时候发生 wakeup，select 也会马上返回。wakeup 不会丢失。
	// if (selector.select() <= 0)
	// continue; // 没有事件，可能是wakeup或者shutdown。
	//
	// java.util.Set<SelectionKey> selected = selector.selectedKeys();
	// for (SelectionKey key : selected) {
	// if (!key.isValid())
	// continue; // key maybe cancel in loop
	// Handle handle = null;
	// try {
	// handle = (Handle)key.attachment();
	// handle.doHandle(key);
	// } catch (Throwable e) {
	// try {
	// key.channel().close();
	// } catch (Throwable _) {
	// /* skip */
	// }
	// try {
	// if (null != handle)
	// handle.doException(key, e);
	// } catch (Throwable _) {
	// // 注意，这里打印 doHandle 异常调用栈，而不是 doException 的异常调用栈。
	// mkdb.Trace.error("doException " + _, e);
	// }
	// }
	// }
	// selected.clear();
	//
	// } catch (Throwable e) {
	// mkdb.Trace.error("mkio.Engine.run", e);
	// }
	// }
	// }
	//
	// @Override
	// public synchronized void wakeup() {
	// selector.wakeup();
	//// super.wakeup();
	// }
	//
	// void close() {
	// shutdown();
	// try {
	// selector.close();
	// } catch (Throwable e) {
	// e.printStackTrace();
	// }
	// }
	// }
}
