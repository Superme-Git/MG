package mkio;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Selector �¼�������ɷ���
 *
 */
public final class SelectorThread extends Thread {
	private volatile boolean running = true;
	private final Selector selector;
	private final Lock registerLock = new ReentrantLock();

	//////////////////////////////////////////////////////////////
	// �������߳�ִ�е�����
	private List<Runnable> tasks = new ArrayList<Runnable>();
	private final Lock tasksLock = new ReentrantLock();

	// ����Xio���� SocketChannelע�ᵽSelector�У���Xio���뵽Manager�С�
	final class RegisterSocketChannel implements Runnable {
		private Creator creator;
		private Manager manager;
		private SocketChannel sc;

		RegisterSocketChannel(Creator creator, Manager manager, SocketChannel sc) {
			this.creator = creator;
			this.manager = manager;
			this.sc = sc;
		}

		@Override
		public void run() {
			manager.addMkio(creator.newMkio(SelectorThread.this, sc));
		}
	}

	/**
	 * �ύ SocketChannel ע������
	 * 
	 * @param creator
	 * @param manager
	 * @param sc
	 */
	void execute(Creator creator, Manager manager, SocketChannel sc) {
		execute(new RegisterSocketChannel(creator, manager, sc));
	}

	/**
	 * package private
	 * 
	 * @param task
	 */
	void execute(Runnable task) {
		tasksLock.lock();
		try {
			tasks.add(task);
			selector.wakeup();
		} finally {
			tasksLock.unlock();
		}
	}

	private void executeNow() {
		List<Runnable> rt = null;
		tasksLock.lock();
		try {
			if (0 == tasks.size())
				return; // û������
			rt = tasks;
			tasks = new ArrayList<Runnable>();
		} finally {
			tasksLock.unlock();
		}
		for (Runnable task : rt) {
			try {
				task.run();
			} catch (Throwable e) {
				mkdb.Trace.error("mkio.SelectorThread.executeNow : " + task.getClass().getName(), e);
			}
		}
	}

	public SelectorThread(String threadName) {
		super(threadName);
		this.setDaemon(true);

		try {
			selector = Selector.open();
		} catch (java.io.IOException e) {
			throw new IOError(e);
		}
	}

	public SelectionKey register(SelectableChannel sc, int ops, Handle handle) {
		try {
			sc.configureBlocking(false);
			registerLock.lock();
			try {
				// �������߳�ִ��registerʱ��wakeup�ᵼ��һ�ζ��໽�ѡ�
				// �������ӽ������Ǻܷ�æ��Ӧ�������ⲻ��
				// ����ͨ���ж��Ƿ��߳��������Ƿ����wakeup��
				if (Thread.currentThread() != this)
					this.selector.wakeup(); // ���ᶪʧ��
				return sc.register(this.selector, ops, handle);
			} finally {
				registerLock.unlock();
			}
		} catch (java.io.IOException e) {
			throw new IOError(e);
		}
	}

	public void close() {
		this.running = false;
		this.selector.wakeup();

		// join
		while (true) {
			try {
				this.join();
				break;
			} catch (Throwable ex) {
				mkdb.Trace.warn(this.getClass().getName() + "close. ignore ex=" + ex);
			}
		}

		try {
			selector.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (running) {
			try {
				executeNow();

				// ���ע�����ڽ����У���Ҫ���� select()��������ȴ���ɡ�
				registerLock.lock();
				try {
					/* nothing */ } finally {
					registerLock.unlock();
				}
				// ��������ʱ�䴰�� wakeup������� select �����Ϸ��ء�wakeup ���ᶪʧ��
				if (selector.select() <= 0)
					continue; // û���¼��������� wakeup ���� shutdown��

				java.util.Set<SelectionKey> selected = selector.selectedKeys();
				for (SelectionKey key : selected) {
					if (!key.isValid())
						continue; // key maybe cancel in loop
					Handle handle = null;
					try {
						handle = (Handle) key.attachment();
						handle.doHandle(key);
					} catch (Throwable e) {
						try {
							key.channel().close();
						} catch (Throwable e_) {
							/* skip */
						}
						try {
							if (null != handle)
								handle.doException(key, e);
						} catch (Throwable e_) {
							// ע�⣬�����ӡ doHandle �쳣����ջ�������� doException ���쳣����ջ��
							mkdb.Trace.error("doException " + e_, e);
						}
					}
				}
				selected.clear();

			} catch (Throwable e) {
				mkdb.Trace.error("mkio.Engine.run", e);
			}
		}
	}
}
