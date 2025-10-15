package snail.tools;

import java.util.*;
import java.io.*;

import javax.management.*;
import sun.tools.jconsole.*;
import java.lang.reflect.*;
import java.util.concurrent.*;

/**
 * JMX 访问辅助类。
 * 
 * JAVA程序通过参数 -Dcom.sun.management.jmxremote.port 启动JMX代理。
 * 
 * 当通过 attach 连接到本地进程时，本地进程可以不用启动代理服务，也不需要用户名密码。
 * 当通过 RMI 协议连接到远程进程时，远程进程需要启动代理服务，也需要提供远程地址、用户名和密码。
 *
 */
public class Jmxc {
	private ProxyClient proxy;

	private final void connect() throws Exception {
		this.proxy.disconnect();
		Method connect = this.proxy.getClass().getDeclaredMethod("connect", (Class<?>[])null);
		connect.setAccessible(true);
		connect.invoke(this.proxy, (Object[])null);
	}

	/**
	 * 连接到本地进程。
	 * @param pid 本地进程ID。
	 * @throws Exception
	 */
	public Jmxc(int pid) throws Exception {
		if ( !Tool.isLocalAttachAvailable() )
			throw new LocalAttachNotAvailable();
		this.proxy = ProxyClient.getProxyClient(LocalVirtualMachine.getLocalVirtualMachine(pid));
		this.connect();
	}

	/**
	 * 连接到本地进程。
	 * @param lvm 本地进程虚拟机。
	 * @throws Exception
	 */
	public Jmxc(LocalVirtualMachine lvm) throws Exception {
		this.proxy = ProxyClient.getProxyClient(lvm);
		this.connect();
	}

	/**
	 * 连接到url指定目标。 
	 * @param url
	 * @param userName
	 * @param password
	 * @throws Exception
	 */
	public Jmxc(String url, String userName, String password) throws Exception {
		this.proxy = ProxyClient.getProxyClient(url, userName, password);
		this.connect();
	}

	/**
	 * 连接到主机名和端口指定的目标。
	 * @param hostName
	 * @param port
	 * @param userName
	 * @param password
	 * @throws Exception
	 */
	public Jmxc(String hostName, int port, String userName, String password) throws Exception {
		this.proxy = ProxyClient.getProxyClient(hostName, port, userName, password);
		this.connect();
	}

	/**
	 * 关闭连接。
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (null != this.proxy) {
			this.proxy.disconnect();
			this.proxy = null;
		}
	}

	public ProxyClient getProxy() {
		return proxy;
	}

	public Set<ObjectName> queryNames(String domain) throws Exception {
		ObjectName name = null;
		if (null != domain)
			name = new ObjectName(domain + ":*");
		return this.proxy.getSnapshotMBeanServerConnection().queryNames(name, null);
	}

	public Object invoke(String bean, String operation, Object[] params, String[] signature) throws Exception {
		ObjectName mbeanName = new ObjectName(bean);
		return this.proxy.getMBeanServerConnection().invoke(mbeanName, operation, params, signature);
	}

	////////////////////////////////////////////////////////////////////////
	// 命令行辅助函数。

	/**
	 * 根据命令行提供的参数建立JMX连接。
	 */
	public static Jmxc connect(Options options) throws Exception {
		final long timeout = Long.parseLong(options.getValue("-t"));
		return timeout > 0
				? connect(options.getValue("-c"), options.getValue("-u"), options.getValue("-p"),
						timeout, TimeUnit.MILLISECONDS)
				: connect(options.getValue("-c"), options.getValue("-u"), options.getValue("-p"));
	}

	/**
	 * jmx 连接所需要的参数选项定义。
	 */
	public static String[][] options() {
		String[][] _options_ = {
				{"-u", "username", "username that can be used when making the connection.", null },
				{"-p", "password", "password that can be used when making the connection.", null },
				{"-c", "connection", "connection = pid || host:port || JMX URL (service:jmx:<protocol>://...).", "!" },
				{null, null, "   pid  : target processID or mainClassName.", null},
				{null, null, "   host : remote host name or ip.", null},
				{null, null, "   port : remote port.", null},
				{"-t", "timeout", "timeout/ms. 0 infinite", "5000" },
		};
		return _options_;
	}

	/**
	 * 连接辅助函数
	 * @param connection
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Jmxc connect(String connection, String username, String password) throws Exception {
		// verify parameter
		if (null == connection)
			throw new IllegalArgumentException("connection is null");
		connection = connection.trim();
		if (connection.isEmpty())
			throw new IllegalArgumentException("connection is empty");

		// 不检查参数 username, password; 如有需要，最终使用报错。

		// JMX URL
		if (connection.startsWith("service:jmx:"))
			return new Jmxc(connection, username, password);

		// host:port
		int index = connection.lastIndexOf(":");
		if (index >= 0) {
			String host = connection.substring(0, index);
			String port = connection.substring(index + 1).trim();
			return new Jmxc(host, port.isEmpty() ? 0 : Integer.parseInt(port), username, password);
		}

		// pid or mainclass
		Integer pid = null;
		try {
			pid = Integer.valueOf(connection);
		} catch (NumberFormatException x) {
			for (Ps.Info info : Ps.psx(null)) {
				if (info.getMainClassName().equals(connection)) {
					pid = info.getPid();
					System.out.println("find pid by mainClassName: " + info);
					break;
				}
			}
		}
		if (null == pid)
			throw new RuntimeException("connection: invalid pid");
		return new Jmxc(pid);
	}

	public MBeanInfo getMBeanInfo(String objectName) throws Exception {
		return this.proxy.getSnapshotMBeanServerConnection().getMBeanInfo(new ObjectName(objectName));
	}

	/**
	 * 带超时的连接辅助函数，从网上拷贝来的，稍作修改，原址如下：
	 * @see http://weblogs.java.net/blog/emcmanus/archive/2007/05/making_a_jmx_co_1.html
	 * @param connection
	 * @param username
	 * @param password
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws IOException
	 */
	public static Jmxc connect(
			final String connection, final String username, final String password,
			long timeout, TimeUnit unit)
			throws IOException {

		final BlockingQueue<Object> mailbox = new ArrayBlockingQueue<Object>(1);
		ExecutorService executor = Executors.newSingleThreadExecutor(daemonThreadFactory);
		executor.submit(new Runnable() {
			public void run() {
				try {
					Jmxc jxmc = Jmxc.connect(connection, username, password);
					if (!mailbox.offer(jxmc))
						jxmc.close();
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
			throw new RuntimeException("Connect timed out: " + connection);
		if (result instanceof Jmxc)
			return (Jmxc) result;
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
