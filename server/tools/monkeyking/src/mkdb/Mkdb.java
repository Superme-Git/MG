package mkdb;

import java.util.Map;
import java.util.Random;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Timer;


/**
 * xdb��һ���򵥵��ļ����ݿ⣬֧�ֱ���������񣬲��߱���֮��Ĺ�����ϵ��
 *
 */
public final class Mkdb implements MkdbMBean {
	private volatile MkdbConf    conf;
	private volatile Tables     tables;
	private volatile Angel      angel;
	private volatile Checkpoint checkpoint;

	private volatile boolean isOpen = false;

	private static Timer timer = new Timer("mkdb.Timer", true);

	private Random random = new Random(System.currentTimeMillis());
	private static Mkdb xdbinstance = new Mkdb();

	private mkdb.util.MBeans.Manager xdbmbeans = new mkdb.util.MBeans.Manager();

	public static mkdb.util.MBeans.Manager mbeans() {
		return xdbinstance.xdbmbeans;
	}

	public static Mkdb getInstance() {
		return xdbinstance;
	}

	// package private
	final Angel getAngel() {
		return angel;
	}

	// package private
//	final Checkpoint getCheckpoint() {
//		return checkpoint;
//	}

	public final Tables getTables() {
		return tables;
	}

	public static Timer timer() {
		return timer;
	}

	public static Random random() {
		return xdbinstance.random;
	}

	public final Executor getExecutor() {
		return Executor.getInstance();
	}

	public static Executor executor() {
		return Executor.getInstance();
	}

	public static boolean isOpen() {
		return xdbinstance.isOpen;
	}

	private Mkdb() {
		Runtime.getRuntime().addShutdownHook(new Thread("mkdb.ShutdownHook") {
			@Override
			public void run() {
				Trace.fatal("mkdb stop from ShutdownHook.");
				Mkdb.this.stop();
			}
		});
	}


	public final void setConf(MkdbConf conf) {
		if (null == conf)
			throw new NullPointerException();
		Trace.set(conf.getTrace());
		this.conf = conf;
		final Executor e = Executor.getInstance(); // volatile
		if (null != e)
			e.setCorePoolSize(conf.getCorePoolSize(), conf.getProcPoolSize(), conf.getSchedPoolSize());
		XBean._set_xdb_verify_(conf.isMkdbVerify());
	}

	public final MkdbConf getConf() {
		return conf;
	}

	public final boolean start() {
		return start(false);
	}

	/**
	 * ����XDB����������Լ�Ψһ���ͻ��˷���
	 * @deprecated Ϊ�˷�ֹ��Ӧ�ó����ʼ����û���ʱ�����յ�����������Ҫ��������ط����Ƴ١�
	 * �����µķ�ʽ��������
	 * <pre>
	 * mkdb.start();
	 * ... // application initialize
	 * mkdb.startNetwork();
	 * </pre>
	 */
	public final boolean startWithNetwork() {
		return start(true);
	}

	/**
	 * @deprecated see startWithNetwork
	 */ 
	public final boolean start(boolean network) {
		return start(network ? UNIQNAME : 0);
	}

	/**
	 * ������������
	 */
	public final static int NETWORK      = 1;

	/**
	 * ����Ψһ��������Ψһ�����������������档û��Ψһ��֧�֣�����ʹ��������(autoIncrement)
	 */
	public final static int UNIQNAME     = 2;

	/**
	 * ת���Լ���������
	 * @return new options
	 * @throws if translate error.
	 */
	private final int translateOptions(int options) {
		if (0 != (UNIQNAME & options))
			return options | NETWORK; // Ψһ����Ҫ����֧�֣��Զ����á�
		return options;
	}

	////////////////////////////////////////////////////////////////////
	// ������ֹ��ͬһ�����ݿ�Ŀ¼����������ʵ����ͬʱ������鲻�����˳���
	private File inuseFile;
	public static final String XDBINUSEFILENAME = "mkdb.inuse";
	private void deleteInuseFile() {
		this.inuseFile.delete();
	}
	private final void createInuseFile() {
		try {
			inuseFile = new File(conf.getDbHome(), XDBINUSEFILENAME);
			if (false == inuseFile.createNewFile())
				throw new XError("mkdb is still in active use(never use simultaneously) OR"
						+ "\n\t\tnot stop normally: 1 verify and repair the db(commendatory), "
						+ "2 delete the file '" + inuseFile.getPath() + "', "
						+ "3 start again)");
		}
		catch (java.io.IOException ex) {
			throw new XError(ex);
		}
	}
	////////////////////////////////////////////////////////////////////

	/**
	 * @deprecated see startWithNetwork
	 */ 
	public final synchronized boolean start(int options) {
		if (mkdb.util.Dbx.getManager().isOpen())
			throw new IllegalAccessError("i hate dbx.");

		if (isOpen)
			return false;

		if (null == conf) // for test: ���Դ����ʼ����Щ��
			setConf(new MkdbConf("../test/mkdb.xml"));

		conf.getTableHome().mkdirs();
		conf.getLogHome().mkdirs();

		//������ݿ��ṹ�Ƿ�ƥ�䡣
		if(!mkdb.util.DatabaseMetaData.getInstance().isSame(conf.getDbHome()))
			throw new RuntimeException("Compare metadata fail,should run xtransform?");
		mkdb.util.DatabaseMetaData.getInstance().createXML(conf.getDbHome());
		
		Trace.open(conf);
		this.createInuseFile();
		Trace.fatal("mkdb start begin");
		{
			// ȷ�������þ�̬ʵ�����������Ժ���߳�ͬʱװ��ʱ���߳����⡣
			// ��Ȼ�����ClassLoaderȷ��װ��һ�����ǲ������ģ���ô�Ͳ���Ҫ���������д�ȷ�ϡ�
			Lockeys.getInstance();
			new XBean(null, null);
		}

		options = translateOptions(options);
		try {
			Class<?> cls = Class.forName("xtable._Tables_");
			Object instance = cls.newInstance();
			if (!(instance instanceof Tables))
				throw new XError("invalid xtable.Tables");

			Executor.start(conf.getProcedureConf().getMaxExecutionTime(),
					conf.getCorePoolSize(), conf.getProcPoolSize(), conf.getSchedPoolSize(),
					conf.getTimeoutPeriod());

			// MORE CHECK ?
			tables = (Tables)instance;
			tables.open(conf);

			checkpoint = new Checkpoint(tables);
			angel = new Angel();

			isOpen = true; // ������,Mkdb�Ѿ�������ɡ�����������������ݿ⣬�����ñ�־��

			if (0 != (UNIQNAME & options))
				mkdb.util.UniqName.initialize(); // MUST initialize before open 
			if (0 != (NETWORK & options))
				mkio.Engine.getInstance().open();

			mbeans().register(this, "Mkdb:type=Mkdb");

			checkpoint.start();
			angel.start();

			Trace.fatal("mkdb start end");
			return true;

		} catch (XError e) {
			close();
			throw e;
		} catch (Throwable e) {
			close();
			throw new XError(e);
		}
	}

	/**
	 * ��������Ψһ�������硣
	 */
	public void startNetwork() {
		mkdb.util.UniqName.initialize(); // MUST initialize before open 
		mkio.Engine.getInstance().open();
	}

	private final synchronized void close() {
		isOpen = false;
		Trace.fatal("mkdb stop begin");
		mbeans().unregisterAll();
		mkdb.util.UniqName.uninitialize();
		mkio.Engine.getInstance().close();

		Executor.stop();

		if (null != angel) {
			angel.shutdown();
			angel = null;
		}

		if (null != checkpoint) {
			checkpoint.shutdown();
			checkpoint = null;
		}

		if (null != tables) {
			tables.close();
			tables = null;
		}
		this.deleteInuseFile();
		Trace.fatal("mkdb stop end");
	}

	public final void stop() {
		// try { Thread.sleep(60000); } catch (Exception ex) { } // delay stop. debug
		synchronized (this)	{
			if (false == isOpen)
				return;
			isOpen = false; // �������ñ�־
		}
		final StopHandle handle = this.stopHandle;
		if (null != handle)
			handle.beforeStop();
		this.close();
		if (null != handle)
			handle.afterStop();
	}

	private volatile StopHandle stopHandle = null;
	public void setStopHandle(StopHandle stopHandle) {
		this.stopHandle = stopHandle;
	}

	public static interface StopHandle {
		/**
		 * XDB ֹͣǰ���á�
		 * ��ʱXDB���ݵȹ��ܶ���ʹ�á�������isOpen�Ѿ�Ϊfalse�������������־�Ĺ��ܻ���Ӱ�졣
		 */
		public void beforeStop();
		/**
		 * XDB ֹͣ����á�
		 */
		public void afterStop();
	}

	///////////////////////////////////////////////////////////////////////////////
	// mbean implement
	@Override
	public void shutdown(String iamsure) {
		if (iamsure.equals("iamsure")) {
			this.stop();
			Trace.fatal("halt program from jmx");
			Runtime.getRuntime().halt(0);
		}
	}

	@Override
	public int getAngleInterruptCount() {
		return this.angel.getInterruptCount();
	}

	@Override
	public long getTransactionCount() {
		return Transaction.getTotalCount();
	}

	@Override
	public long getTransactionFalse() {
		return Transaction.getTotalFalse();
	}

	@Override
	public long getTransactionException() {
		return Transaction.getTotalException();
	}

	@Override
	public void testAlive(long timeout)
		throws InterruptedException, ExecutionException, TimeoutException {
		this.getExecutor().testAlive(timeout);
		// more test ...
	}

	public CheckpointMBean getCheckpointMBean() {
		return checkpoint;
	}

	@Override
	public Map<String, AtomicInteger> top(String nsClass, String nsLock) {
		return new mkdb.util.StackTrace(nsClass, nsLock).top();
	}
}
