package mkio;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ���� Selector�� �����������á� �����¼��ɷ������ attachment �� Handle�����ɷ��¼������򣬹ر����ӡ�
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
	 * ע�� XioConf �������С������� open ֮ǰ������е�����ע�ᡣ �� Engine �ر��Ժ����ûᱻ�������Ҫ����ע�ᡣ
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
	 * XioConf װ�����õ�ʱ����á� ��ʼ�����̵�һ���֡�
	 */
	public synchronized void open() {
		open(DEFAULT_THREAD_NUMBER);
	}

	/**
	 * @param threadNumber
	 *            �����߳��������ᱻ�޶��� 2 ���ݡ�
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
		// ������ʹ��, ������ Mkdb ������ֹͣ.
		// Mkdb �� isOpen �ڿ�ʼstop�ͻ����ã���ֹֹͣ�����е����⡣
		// ��������ȫ���������Ļ�������Ͳ���Ҫ�ж� mkdb.Mkdb.isOpen();
		return // mkdb.Mkdb.isOpen() &&
		(null != engine.selectors);
	}

	public static void verify() {
		if (false == isOpen())
			throw new IllegalStateException("mkio.Engine is closed.");
	}

	///////////////////////////////////////////////////////////////////////
	// Ӧ�ýӿ�

	/**
	 * not used
	 * 
	 * @return
	 */
	static SelectorThread[] selectors() {
		return engine.selectors;
	}

	/**
	 * �õ�һ�� Selector �̡߳�
	 * 
	 * @return
	 */
	static SelectorThread selector() {
		final int count = engine.registerCount.incrementAndGet();
		final int index = count & (engine.selectors.length - 1);
		return engine.selectors[index];
	}

	////////////////////////////////////////////////////////
	// Selector �¼����Ӻ��ɷ����ڲ�ʵ�֡���������
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
	// // ���ע�����ڽ����У���Ҫ���� select()��������ȴ���ɡ�
	// synchronized (registerGuard) { }
	// // �����ʱ���� wakeup��select Ҳ�����Ϸ��ء�wakeup ���ᶪʧ��
	// if (selector.select() <= 0)
	// continue; // û���¼���������wakeup����shutdown��
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
	// // ע�⣬�����ӡ doHandle �쳣����ջ�������� doException ���쳣����ջ��
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
