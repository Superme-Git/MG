package mkdb;

public class ThreadHelper extends Thread {
	private volatile boolean running = true;
	private boolean idle = true;

	public ThreadHelper(String name) {
		super(name);
		this.setDaemon(true);
	}

	public ThreadHelper(String name, boolean daemon) {
		super(name);
		this.setDaemon(daemon);
	}

	public final boolean isRunning() {
		return running;
	}

	/**
	 * �����ж��쳣������ȴ��߳̽�����
	 */
	public final void joinAssuring() {
		while (true) {
			try {
				join();
				break;
			} catch ( Throwable ex ) {
				Trace.warn(this.getClass().getName() + "shutdown. ignore ex=" + ex);
			}
		}
	}

	public void shutdown() {
		running = false;
		wakeup();
		joinAssuring();
	}

	public synchronized void wakeup() {
		idle = false;
		this.notify();
	}

	/**
	 * 
	 * ����ǰ�߳�һ��ʱ�䡣
	 * 
	 * ͨ�� shutdown ���� wakeup ��ϡ�
	 * 
	 * @param ms
	 */
	public final synchronized void sleepIdle(long ms) {
		try {
			if (idle)
				this.wait(ms);
		} catch (InterruptedException ex) {
			Trace.warn(this.getClass().getName() + "sleepOut. ex=" + ex);
		} finally {
			idle = true;
		}
	}

	Runnable cock() {
		return new Runnable() {
			@Override
			public void run() {
				ThreadHelper.this.wakeup();
			}
		};
	}
}
