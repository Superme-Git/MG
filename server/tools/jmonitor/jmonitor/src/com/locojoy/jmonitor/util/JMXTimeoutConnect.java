package com.locojoy.jmonitor.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXTimeoutConnect {
	public static JMXConnector connectWithTimeout(final JMXServiceURL url, long timeout, TimeUnit unit) throws IOException {
		final BlockingQueue<Object> mailbox = new ArrayBlockingQueue<Object>(1);
		ExecutorService executor = Executors.newSingleThreadExecutor(daemonThreadFactory);
		executor.submit(new Runnable() {
			public void run() {
				try {
					JMXConnector connector = JMXConnectorFactory.connect(url);
					if (!mailbox.offer(connector))
						connector.close();
				} catch (Throwable t) {
					mailbox.offer(t);
				}
			}
		});
		Object result;
		try {
			result = mailbox.poll(timeout, unit);
			if (result == null) {
				if (!mailbox.offer(""))
					result = mailbox.take();
			}
		} catch (InterruptedException e) {
			throw initCause(new InterruptedIOException(e.getMessage()), e);
		} finally {
			executor.shutdown();
		}
		if (result == null)
			throw new SocketTimeoutException("Connect timed out: " + url);
		if (result instanceof JMXConnector)
			return (JMXConnector) result;
		try {
			throw (Throwable) result;
		} catch (IOException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Error e) {
			throw e;
		} catch (Throwable e) {
			// In principle this can't happen but we wrap it anyway
			throw new IOException(e.toString(), e);
		}
	}

	private static <T extends Throwable> T initCause(T wrapper, Throwable wrapped) {
		wrapper.initCause(wrapped);
		return wrapper;
	}

	private static class DaemonThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable r) {
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		}
	}

	private static final ThreadFactory daemonThreadFactory = new DaemonThreadFactory();
}
