package mkdb.util;

import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;
import mkdb.util.Elapse;

/**
 * <p> һ���򵥵ģ����� Dbx ��ͳ�ƿ�ܣ���֧��ֻ��ͳ�ƣ���
 * <ul> �������� browse -> decode[uncompress and unmarshal] -> process
 * <li> browse: ��Ӳ�̶�ȡ�ļ�ʱ�Ĳ�����ʵ�ֿ��ơ�
 * <li> decode: ���벢���� threadPoolSize �������ơ�
 * <li> process���Ƿ񲢷��� isConcurrent �������ơ�
 * </ul>
 * <p> ��ο�: main; Task.run; Stat.perform;
 */
public class Stat {
	/**
	 * ���� 
	 * <pre>
	 * �����У�
	 * cd snail/test
	 * java -cp "../mkdb/bin;../test/bin;../bin/jio.jar" mkdb.util.Stat -d 1
	 * 
	 * ִ�н����
	 * �����������ִ�л��׳��쳣��
	 * 1. testmerge��ԭ����classpathȱ��junit.jar��
	 * 2. flush3�������쳣�ǹ���ģ�see Stat.initializeSample()��
	 * </pre>
	 */
	public static void main(String[] args) throws Exception {

		// ��������
		String dbHome = "mkdb";
		String libDir = ".";
		int    debugLevel = -1;
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals("-db"))  { dbHome = args[++i]; continue; }
			if (args[i].equals("-lib")) { libDir = args[++i]; continue; }
			if (args[i].equals("-d"))   { debugLevel = Integer.parseInt(args[++i]); continue; }
		}

		// ��ʼ��ͳ��
		Stat stat = new Stat(libDir, dbHome, true);
		if (debugLevel > 0) stat.setDebugLevel(debugLevel);
		List<TableBrowser> browsers = stat.initializeSample();

		// ִ��ͳ��
		mkdb.util.Elapse elapse = new mkdb.util.Elapse();
		Stat.perform(browsers, 2);

		// ͳ�ƽ���������������ͳ�ƿ�ܱ������Ϣ
		System.out.println("total time=" + elapse.elapsed());
		Stat.reportSample(browsers);
		
		// ��ʵ����Ҫ���ű������˳��ʱ��ʹ�� newTableBrowser��TableBrowser.run��ɡ�
		// �ο� initializeSample �� Stat.perform��
	}

	private List<TableBrowser> initializeSample() {
		List<TableBrowser> browsers = new ArrayList<TableBrowser>();

		// ʵ�ʵ�ͳ�ƴ�����֪���Լ�Ҫ��ȡ��Щ��ġ�������ӣ������еı�񶼶�һ�顣
		for (DatabaseMetaData.Table dmdTable : this.databaseMetaData.getTables()) {
			if (!dmdTable.isPersistence())
				continue;

			browsers.add(this.newTableBrowser(dmdTable.getName(), new TableHandle() {
				@Override
				public void process(Object key, Object value) {
					// ǿ��ת���ɾ������ͣ�Ȼ��ʹ������
					// ����Ĵ����Ƿ��ڶ��߳���ִ�У��� Stat.isConcurrent ���Ƶġ�
					if (this.getTableBrowser().getWalker().getTable().getName().equals("flush3"))
						throw new RuntimeException("process exception test.");
				}
			}));
		}

		return browsers;
	}

	public static void reportSample(List<TableBrowser> browsers) {
		for (TableBrowser b : browsers) {
			final Walker w = b.getWalker();
			final Dbx.Table t = w.getTable();
			if (b.getException() != null) {
				System.out.println(t.getName() + " <EXCEPTION>");
				b.getException().printStackTrace();
				continue;
			}

			// ����
			Sum sumAll = new Sum();
			for (Sum sum : w.getSums())
				sumAll.add(sum);

			// skip empty table.
			if (sumAll.recordCount == 0)
				continue;

			System.out.println(t.getName() + " file(" + w.getTableFileSize() + ") " + sumAll);
			for (Sum sum : w.getSums())
				System.out.println("		" + sum);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////
	// ʵ��
	private int threadPoolSize;
	private int capacityOfQueue;
	private boolean isConcurrent;
	private DatabaseMetaData databaseMetaData;
	private Dbx dbx;

	private static int DEFAULT_THREADPOOLSIZE  = 4;
	private static int DEFAULT_CAPACITYOFQUEUE = 1024;

	/**
	 * �����¼ʱ�Ƿ�������ִ�С�
	 * @param isConcurrent
	 */
	public Stat(String libDir, String dbHome, boolean isConcurrent) {
		this(libDir, dbHome, DEFAULT_THREADPOOLSIZE, DEFAULT_CAPACITYOFQUEUE, isConcurrent, DatabaseMetaData.getInstance());
	}

	/**
	 * @param threadPoolSize   �����߳�������
	 * @param capacityOfQueue  ����������������������ó�MAX_VALUE���Է�ֹ�����̴߳��������ʱ�������ڴ治�㡣
	 * @param isConcurrent     ͳ�Ƽ����Ƿ񲢷�ִ�С�
	 * @param databaseMetaData ���ݿ���Ϣ��
	 */
	public Stat(String libDir, String dbHome, 
			int threadPoolSize, int capacityOfQueue, boolean isConcurrent,
			DatabaseMetaData databaseMetaData) {

		this.threadPoolSize   = threadPoolSize;
		this.capacityOfQueue  = capacityOfQueue;
		this.isConcurrent     = isConcurrent;
		this.databaseMetaData = databaseMetaData;

		Dbx.start(libDir);
		dbx = Dbx.open(new File(dbHome), databaseMetaData);
	}

	public static abstract class TableHandle {
		TableBrowser tableBrowser; // setup in newTableBrowser

		public TableBrowser getTableBrowser() {
			return tableBrowser;
		}

		public abstract void process(Object key, Object value);
	}

	public TableBrowser newTableBrowser(String tableName, TableHandle handle) {
		if (null == handle)
			throw new NullPointerException();

		DatabaseMetaData.Table dmdTable = databaseMetaData.getTable(tableName);

		if (null == dmdTable)
			throw new RuntimeException("table not found: " + tableName);

		// dbx.openTable ���жϳ־��ԣ�������ļ�������ʱ���Զ���������Ҫ��ȷ�жϡ�
		if (!dmdTable.isPersistence())
			throw new RuntimeException("table is not persistence: " + tableName);

		final Dbx.Table table = dbx.openTable(tableName);
		final Walker walker = this.newWalker(this, table, handle);
		final TableBrowser browser = new TableBrowser(walker);
		handle.tableBrowser = browser;
		return browser;
	}

	public static class TableBrowser implements Runnable {
		private Walker walker;
		private Throwable exception = null;

		public TableBrowser(Walker walker) {
			this.walker = walker;
		}

		@Override
		public void run() {
			try {
				walker.getTable()._browse(walker);
				walker.awaitTermination();
				exception = walker.getFirstTaskException();
			} catch (Throwable ex) {
				exception = ex;
			}
		}

		public Walker getWalker() {
			return walker;
		}

		public Throwable getException() {
			return exception;
		}
	}

	/**
	 * ������еı�������
	 * @param browsers
	 * @param concurrency ��������
	 * @throws InterruptedException
	 */
	public static void perform(List<TableBrowser> browsers, int concurrency) throws InterruptedException {
		if (concurrency > 1) {
			ThreadPoolExecutor browseExecutor = new ThreadPoolExecutor(
					concurrency, concurrency, 0, TimeUnit.NANOSECONDS,
					new LinkedBlockingQueue<Runnable>());
			for (TableBrowser browser : browsers)
				browseExecutor.execute(browser);
			browseExecutor.shutdown();
			browseExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // INFINITE
		} else {
			for (TableBrowser browser : browsers)
				browser.run();
		}
	}

	public DatabaseMetaData getDatabaseMetaData() {
		return databaseMetaData;
	}

	public int getCapacityOfQueue() {
		return capacityOfQueue;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}
	
	public boolean isConcurrent() {
		return isConcurrent;
	}

	protected Walker newWalker(Stat stat, Dbx.Table table, TableHandle handle) {
		return new Walker(stat, table, handle);
	}

	protected Task newTask(Walker walker) {
		return new Task(walker);
	}

	/**
	 * ÿ���̷ֿ߳�ͳ�ƣ���������Ҫ���ܡ���Щͳ�Ʋ�����Ҫ���̼߳��㣬���� uncompress�ٶȡ�
	 */
	public static class Sum {
		public long recordCount = 0;

		public long totalKeySize = 0; // never compressed
		public long totalCompressedValueSize = 0;
		public long totalValueSize = 0;

		public long totalUncompressTime = 0;
		public long totalUnmarshalTime = 0;
		public long totalProcessTime = 0;

		public void add(Sum r) {
			this.recordCount              += r.recordCount;

			this.totalKeySize             += r.totalKeySize;
			this.totalCompressedValueSize += r.totalCompressedValueSize;
			this.totalValueSize           += r.totalValueSize;

			this.totalUncompressTime      += r.totalUncompressTime;
			this.totalUnmarshalTime       += r.totalUnmarshalTime;
			this.totalProcessTime         += r.totalProcessTime;
		}

		public static String headers() {
			return "RecordCount	KeySize	CompressedValueSize	ValueSize	UncompressTime	UnmarshalTime	ProcessTime";
		}

		@Override
		public String toString() {
			return String.format("%d	%d	%d	%d	%d	%d	%d", this.recordCount
					, this.totalKeySize, this.totalCompressedValueSize, this.totalValueSize
					, this.totalUncompressTime, this.totalUnmarshalTime, this.totalProcessTime);
		}
	}

	public static class Walker implements mkdb.Storage.IWalk {
		private Stat stat;
		private Dbx.Table table;
		private Stat.TableHandle handle;
		private ThreadPoolExecutor executor;

		private Task task = null;
		private volatile Throwable firstTaskException = null;

		private List<Sum> sums = Collections.synchronizedList(new ArrayList<Sum>());
		private ThreadLocal<Sum> threadLocalSum = new ThreadLocal<Sum>() {
			@Override
			protected Sum initialValue() {
				Sum x = new Sum();
				sums.add(x);
				return x;
			};
		};

		Sum getThreadLocalSum() {
			return threadLocalSum.get();
		}

		public Walker(Stat stat, Dbx.Table table, Stat.TableHandle handle) {
			this.stat = stat;
			this.table = table;
			this.handle = handle;
			final int threadPoolSize = stat.getThreadPoolSize();
			this.executor = new ThreadPoolExecutor(
					threadPoolSize, threadPoolSize, 0, TimeUnit.NANOSECONDS,
					new LinkedBlockingQueue<Runnable>(stat.getCapacityOfQueue()));
		}

		public long getTableFileSize() {
			return table.getFile().length();
		}

		public Stat getStat() {
			return stat;
		}

		public Dbx.Table getTable() {
			return table;
		}

		public Stat.TableHandle getTableHandle() {
			return handle;
		}

		/**
		 * Note: �����е� data ��ѹ��������ݡ�
		 */
		@Override
		public boolean onRecord(byte[] key, byte[] data) {
			if (null == task)
				task = this.stat.newTask(this);

			if (task.prepare(key, data)) {
				executor.execute(task);
				task = null;
			}
			return null == firstTaskException;
		}

		void setFirstTaskException(Throwable exp) {
			synchronized (this) {
				if (null != firstTaskException)
					return;
				firstTaskException = exp;
			}
			executor.shutdownNow();
		}

		public Throwable getFirstTaskException() {
			return firstTaskException;
		}

		public void awaitTermination() throws InterruptedException {
			if (null != task) {
				if (null == firstTaskException) {
					try { executor.execute(task); } catch (Throwable ex) { /*skip*/ }
				}
				task = null;
			}
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS); // INFINITE
		}

		public List<Sum> getSums() {
			if (!executor.isTerminated())
				throw new IllegalStateException("walker is not terminated! table=" + this.table.getName());
			return sums;
		}
	}

	public static class Task implements Runnable {
		private static final int BULKCOUNT = 1024;

		private byte[][] keys = new byte[BULKCOUNT][];
		private byte[][] values = new byte[BULKCOUNT][];
		private int size = 0;

		private Walker walker;

		public Task(Walker walker) {
			this.walker = walker;
		}

		public Walker getWalker() {
			return walker;
		}

		/**
		 * @param key
		 * @param value
		 * @return true if full.
		 */
		public boolean prepare(byte[] key, byte[] value) {
			keys[size] = key;
			values[size] = value;
			++size;
			return BULKCOUNT == size;
		}

		@Override
		public void run() {
			try {
				_run();
			} catch (Throwable ex) {
				this.getWalker().setFirstTaskException(ex);
				if (this.getWalker().getStat().getDebugLevel() >= 5)
					ex.printStackTrace();
			}
		}

		private void _run() {
			Sum counter = this.getWalker().getThreadLocalSum();

			Elapse elapse = new Elapse();
			counter.recordCount += size;

			// uncompress
			byte[][] uncompressedValues = new byte[size][];
			for (int i = 0; i < size; ++i) {
				counter.totalKeySize += keys[i].length;
				counter.totalCompressedValueSize += values[i].length;
				uncompressedValues[i] = Dbx.Table._uncompress(values[i], values[i].length);
				counter.totalValueSize += uncompressedValues[i].length;
			}
			counter.totalUncompressTime += elapse.elapsedAndReset();

			// unmarshal
			final Dbx.Table table = this.getWalker().getTable();
			final DatabaseMetaData.Type keyType = table.getMetaData().getKeyType();
			final DatabaseMetaData.Type valueType = table.getMetaData().getValueType();
			Object[] objKeys = new Object[size];
			Object[] objValues = new Object[size];
			for (int i = 0; i < size; ++i) {
				objKeys[i] = keyType.unmarshal(keys[i]);
				objValues[i] = valueType.unmarshal(uncompressedValues[i]);
			}
			counter.totalUnmarshalTime += elapse.elapsed();

			// process
			final boolean isConcurrent = this.getWalker().getStat().isConcurrent();
			final Stat.TableHandle handle = this.getWalker().getTableHandle();
			if (isConcurrent)
				this.getWalker().getStat().lockConcurrent();

			try {
				elapse.reset();
				for (int i = 0; i < size; ++i) {
					handle.process(objKeys[i], objValues[i]);
				}
				counter.totalProcessTime += elapse.elapsed();
			} finally {
				if (isConcurrent)
					this.getWalker().getStat().unlockConcurrent();
			}
		}
	}

	private Lock concurrentLock = new ReentrantLock();
	private int debugLevel = 5;

	public void lockConcurrent() {
		this.concurrentLock.lock();
	}

	public void unlockConcurrent() {
		this.concurrentLock.unlock();
	}

	/**
	 * ���õ��Լ�������Խ�����Խ�ࡣĬ��Ϊ5��
	 * @param debugLevel
	 */
	public void setDebugLevel(int debugLevel) {
		this.debugLevel = debugLevel;
	}

	public int getDebugLevel() {
		return debugLevel;
	}
}
