package mkdb.util;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * һ������־�����ݿ⡣
 *
 * <p>���������������ݵ��뵼��������ת��������Ҫ�����޸ġ�
 * <p>�̰߳�ȫ���ṩ��������DB.lock(), Table.lock()��
 */
public class Dbx {
	//////////////////////////////////////////////////////////////////////
	// db.h JNI native library load helper
	/**
	 * load native library��
	 * <p>if libname exist in jar, load from jar; else if (null != libpath) load from libpath; else load from dbhome.getParent;
	 * 
	 * @param libpath
	 * @param dbhome
	 * @param libname library name. ���Ϊ null; �����$(os.arch)�������֡�
	 */
	public static void load(File libpath, File dbhome, String libname) {
		if ( null == libname)
			libname = System.mapLibraryName("db_" + System.getProperty("os.arch"));

		String abspath="";
		File of = null;
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(libname);
		if (null != is){
			try {
				String base = libname;
				String ext = null;
				int idx = libname.lastIndexOf(".");
				if (-1 != idx) {
					base = libname.substring(0, idx);
					ext = libname.substring(idx);
				}
				of = File.createTempFile(base, ext, new File(System.getProperty("java.io.tmpdir")));
				abspath = of.getAbsolutePath();
				OutputStream os = new FileOutputStream(of);
				try {
					byte[] buffer = new byte[8192];
					int rc = 0;
					while ((rc = is.read(buffer)) > 0) {
						os.write(buffer, 0, rc);
					}
				} finally {
					os.close();
					
				}
			}catch(IOException e){
				throw new RuntimeException(e);
			}finally {
				try {
					is.close();
					mkdb.Trace.info("System.load(" + abspath + ")");
					System.load(abspath);
					of.delete();
				} catch (IOException ignore) {
				}
			}
		}else{
			String absoluteLibPath = null != libpath
				? new File(libpath, libname).getAbsolutePath()
				: new File(dbhome.getParentFile(), libname).getAbsolutePath();
			mkdb.Trace.info("System.load(" + absoluteLibPath + ")");
			System.load( absoluteLibPath );
		}
		
	}

	public static void load(File libpath, File dbhome) {
		load(libpath, dbhome, null);
	}

	public static void load(String libpath) {
		load(new File(libpath), null, null);
	}

	//////////////////////////////////////////////////////////////////////////
	// Dbx ����
	public final static class Manager {
		private final Lock lock = new ReentrantLock();
		private Map<String, Dbx> dbxs = new HashMap<String, Dbx>();
		private volatile Timer timer;
		private boolean opened = false;
		private volatile boolean saveAtExit = true;

		public Manager() {
			Runtime.getRuntime().addShutdownHook(new Thread("mkdb.util.Dbx.ShutdownHook") {
				@Override
				public void run() {
					Manager.this.stop();
				}
			});
		}

		public boolean isOpen() {
			lock.lock();
			try {
				return opened;
			} finally {
				lock.unlock();
			}
		}

		/**
		 * @param libpath    library path.
		 * @param saveAtExit save at exit.
		 * @return
		 */
		public boolean start(String libpath, boolean saveAtExit) {
			mkdb.Trace.openIf(); // �����־û�д����һ����
			lock.lock();
			try {
				if (opened)
					return false;
				mkdb.Trace.info("mkdb.util.Dbx start ...");
				this.saveAtExit = saveAtExit;
				Dbx.load(libpath);
				timer = new Timer("mkdb.util.Dbx.Timer", true);
				opened = true;
				return true;
			} finally {
				lock.unlock();
			}
		}

		public void stop() {
			stop(saveAtExit);
		}

		public void stop(boolean save) {
			lock.lock();
			try {
				if (opened) {
					mkdb.Trace.info("mkdb.util.Dbx stop begin");
					// remove in loop. ���ﲢ����Ҫ��������ر����ݿ⡣
					if (save)
						for (Dbx dbx : Manager.this.getDbxs()) dbx.close();
					else
						for (Dbx dbx : Manager.this.getDbxs()) dbx.destroy();
					assert(Manager.this.getDbxs().length == 0);

					timer.cancel();
					timer = null;
					opened = false;
					mkdb.Trace.info("mkdb.util.Dbx stop end");
				}
			} finally {
				lock.unlock();
			}
		}

		public void setSaveAtExit(boolean saveAtExit) {
			this.saveAtExit = saveAtExit;
		}

		public Timer getTimer() {
			return timer;
		}

		/**
		 * open a DB ( NullLogger only )
		 * @param home
		 * @param metaData
		 * @return
		 */
		public Dbx open(File home, DatabaseMetaData metaData) {
			String homepath = toCanonicalPath(home);
			lock.lock();
			try {
				Dbx dbx = dbxs.get(homepath);
				if ( null == dbx ) {
					dbx = new Dbx(home, metaData);
					dbxs.put(homepath, dbx);
				}
				return dbx;
			} finally {
				lock.unlock();
			}
		}

		// helper
		private static String toCanonicalPath(File file) {
			try {
				return file.getCanonicalPath();
			} catch (IOException x) {
				throw new RuntimeException(x);
			}
		}

		public Dbx[] getDbxs() {
			lock.lock();
			try {
				return dbxs.values().toArray(new Dbx[dbxs.size()]);
			} finally {
				lock.unlock();
			}
			
		}

		// called when Dbx close and destroy
		private void removeDbx(Dbx dbx) {
			String homepath = toCanonicalPath(dbx.getHome());
			lock.lock();
			try {
				dbxs.remove(homepath);
			} finally {
				lock.unlock();
			}
		}

	}

	private static final Manager manager = new Manager();

	public static Manager getManager() {
		return manager;
	}

	public static boolean start(String libpath) {
		return manager.start(libpath, true);
	}

	public static void stop() {
		manager.stop();
	}

	public static Dbx open(File dbhome, DatabaseMetaData metaData) {
		if (!manager.isOpen())
			throw new IllegalStateException("Dbx is stopped"); 
		return manager.open(dbhome, metaData);
	}

	////////////////////////////////////////////////////////////
	// Dbx ʵ��
	private final Lock dbLock = new ReentrantLock(); // ���� DB �����в�����
	private final File home;      // database directory
	private final File tableHome; // table directory
	private final File logHome;    // log directory
	private final DatabaseMetaData metaData;
	private final Map<String, Table> tables = new HashMap<String, Table>();
	private boolean closed = false;

	/**
	 * �̳��� mkdb.Storage��
	 * @see mkdb.Storage
	 */
	public class Table extends mkdb.Storage {
		private final String name;
		private final DatabaseMetaData.Table metaData; // fast reference
		private boolean walking = false;
		private boolean browsing = false;

		Table(mkdb.Logger logger, String tableName, int cacheHigh, int cacheLow) {
			super(logger, Dbx.this.tableHome, tableName, cacheHigh, cacheLow);
			this.name = tableName;
			this.metaData = Dbx.this.getMetaData().getTable(tableName);
		}

		/**
		 * �����������ݿ�
		 * @return
		 */
		public Dbx getDatabase() {
			return Dbx.this;
		}

		public DatabaseMetaData.Table getMetaData() {
			return metaData;
		}
		/**
		 * @return ������
		 */
		public String getName() {
			return name;
		}

		/**
		 * lock outside.
		 */
		private void assertNotWalking() {
			if (this.walking)
				throw new ConcurrentModificationException("walking!");
		}

//		/**
//		 * lock outside.
//		 */
//		private void assertNotBrowsing() {
//			if (this.browsing)
//				throw new ConcurrentModificationException("browsering!");
//		}

		/**
		 * lock outside.
		 */
		private void assertNotWalkingAndBrowsing() {
			if (this.walking || this.browsing)
				throw new ConcurrentModificationException("walking or browsering!");
		}

		/**
		 * �����޸ġ�<b>����Locked</b>
		 */
		public void save() {
			super.lock();
			try {
				assertNotWalkingAndBrowsing();
				try {
					this.getLogger().checkpoint(this, System.currentTimeMillis());
				} catch (Throwable e) {
					mkdb.Trace.fatal("Table.save: halt program", e);
					Runtime.getRuntime().halt(54321);
				}
			} finally {
				super.unlock();
			}
		}

		/**
		 * �رձ������档<b>����Locked</b>
		 */
		public void destroy() {
			super.lock();
			try {
				assertNotWalkingAndBrowsing();
				Dbx.this.removeTable(this.getName());
				super.close();
			} finally {
				super.unlock();
			}
		}

		/**
		 * �رձ��ύ���档<b>����Locked</b>
		 */
		@Override
		public void close() {
			super.lock();
			try {
				assertNotWalkingAndBrowsing();
				this.save();
				Dbx.this.removeTable(this.getName());
				super.close();
			} finally {
				super.unlock();
			}
		}

		/**
		 * �����¼�������¼�Ѿ������򷵻�false��<b>����Locked</b>��
		 * <p><b>value ������ѹ�����������</b>
		 * @return ͨ������ֵ�������
		 */
		@Override
		public boolean _insert(OctetsStream key, OctetsStream value) {
			super.lock();
			try {
				assertNotWalking();
				return super._insert(key, value);
			} finally {
				super.unlock();
			}
		}

		/**
		 * �����¼����������򸲸ǣ��������������ӡ�<b>����Locked</b>��
		 * <p><b>value ������ѹ�����������</b>
		 * @return ͨ������ֵ�������
		 */
		@Override
		public boolean _replace(OctetsStream key, OctetsStream value) {
			super.lock();
			try {
				assertNotWalking();
				return super._replace(key, value);
			} finally {
				super.unlock();
			}
		}

		/**
		 * �����¼����������򸲸ǣ��������������ӡ�<b>����Locked</b>��
		 * @return ͨ������ֵ�������
		 */
		@Override
		public boolean replace(OctetsStream key, OctetsStream value) {
			super.lock();
			try {
				assertNotWalking();
				return super.replace(key, value);
			} finally {
				super.unlock();
			}
		}

		/**
		 * �����¼�������¼�Ѿ����ڷ���false��<b>����Locked</b>��
		 * @return ͨ������ֵ�������
		 */
		@Override
		public boolean insert(OctetsStream key, OctetsStream value) {
			super.lock();
			try {
				assertNotWalking();
				return super.insert(key, value);
			} finally {
				super.unlock();
			}
		}

		/**
		 * ɾ����¼�������¼���������׳��쳣��<b>����Locked</b>��
		 * @return ͨ������ֵ�������
		 */
		@Override
		public boolean remove(OctetsStream key) {
			super.lock();
			try {
				assertNotWalking();
				return super.remove(key);
			} finally {
				super.unlock();
			}
		}

		/**
		 * ֱ�ӱ��������ļ���<b>����Locked</b>��
		 * ���洢˳�򷵻ؼ�¼����¼���ݱ���ѹ��״̬��
		 * @Deprecated ����ȫ�����ڰ�����ҳ����Ϊ������ҳ�Ŀ��ܣ��ᵼ�´���Ľ������������
		 */
		@Override
		public void _browse(IWalk iw, int cachesize) {
			super.lock();
			try {
				// ������������룬��Ȼ�ײ�֧�֡�
				// ����������룬�� browsering ������boolean���ͣ�Ӧ�ĳ�int���м�����
				assertNotWalkingAndBrowsing();
				this.browsing = true;
				try {
					super._browse(iw, cachesize);
				} finally {
					this.browsing = false;
				}
			} finally {
				super.unlock();
			}
		}

		/**
		 * ֱ�ӱ��������ļ���<b>����Locked</b>��
		 * ���洢˳�򷵻ؼ�¼����¼���ݱ���ѹ��״̬��
		 * @Deprecated ����ȫ�����ڰ�����ҳ����Ϊ������ҳ�Ŀ��ܣ��ᵼ�´���Ľ������������
		 */
		public void _browse(IWalk iw) {
			this._browse(iw, 128);
		}

		/**
		 * ֱ�ӱ��������ļ���<b>����Locked</b>��
		 * ���洢˳�򷵻ؼ�¼��
		 * @Deprecated ����ȫ�����ڰ�����ҳ����Ϊ������ҳ�Ŀ��ܣ��ᵼ�´���Ľ������������
		 */
		public void browse(IWalk ib) {
			this.browse(ib, 128);
		}

		/**
		 * ֱ�ӱ��������ļ���<b>����Locked</b>��
		 * ���洢˳�򷵻ؼ�¼��
		 * @Deprecated ����ȫ�����ڰ�����ҳ����Ϊ������ҳ�Ŀ��ܣ��ᵼ�´���Ľ������������
		 */
		@Override
		public void browse(IWalk ib, int cachesize) {
			super.lock();
			try {
				// ������������룬��Ȼ�ײ�֧�֡�
				// ����������룬�� browsering ������boolean���ͣ�Ӧ�ĳ�int���м�����
				assertNotWalkingAndBrowsing();
				this.browsing = true;
				try {
					super.browse(ib, cachesize);
				} finally {
					this.browsing = false;
				}
			} finally {
				super.unlock();
			}
		}

		/**
		 * ͨ������������<b>����Locked</b>��
		 * ������˳�򷵻ؼ�¼����¼���ݱ���ѹ��״̬��
		 */
		@Override
		public void _walk(IWalk iw) {
			super.lock();
			try {
				// ������������룬��Ȼ�ײ�֧�֡�
				// ����������룬�� browsing ������boolean���ͣ�Ӧ�ĳ�int���м�����
				assertNotWalkingAndBrowsing();
				this.walking = true;
				try {
					super._walk(iw);
				} finally {
					this.walking = false;
				}
			} finally {
				super.unlock();
			}
		}

		/**
		 * ͨ������������<b>����Locked</b>��
		 * ������˳�򷵻ؼ�¼
		 */
		@Override
		public void walk(IWalk iw) {
			super.lock();
			try {
				// ������������룬��Ȼ�ײ�֧�֡�
				// ����������룬�� browsering ������boolean���ͣ�Ӧ�ĳ�int���м�����
				assertNotWalkingAndBrowsing();
				this.walking = true;
				try {
					super.walk(iw);
				} finally {
					this.walking = false;
				}
			} finally {
				super.unlock();
			}
		}

	}

	/**
	 * �������ݿ�ʵ����
	 * @param home         database home directory.
	 * @param metaData     database meta data.
	 */
	private Dbx(File home, DatabaseMetaData metaData) {
		if (mkdb.Mkdb.isOpen())
			throw new IllegalAccessError("i hate mkdb.");

		if (new File(home, mkdb.Mkdb.XDBINUSEFILENAME).exists())
			throw new IllegalAccessError("mkdb inuse.");

		// ��̬���� Dbx.start ʱ�Ѿ�װ�ء�
		// ����Dbx�����ڱ�����䶯ʱʹ�ã�����Ͳ����DatabaseMetaData�� 
		new Integrity(null, home).verify();

		// Integrity ���Ѿ������homeĿ¼��
//		if (!home.isDirectory() || !home.exists())
//			throw new IllegalStateException("dbhome : " + home + " (!isDirectory || !exists())");

		this.home = home;
		this.tableHome = new File(home, mkdb.MkdbConf.DATADIR);
		this.logHome = new File(home, mkdb.MkdbConf.LOGSDIR);
		this.metaData = metaData;

		this.tableHome.mkdirs();
		this.logHome.mkdirs();
	}

	/**
	 * �ر����ݿ⣬
	 */
	public void destroy() {
		dbLock.lock();
		try {
			if (false == closed) {
				// ѭ����ɾ����
				for (Table table : this.getTables())
					table.destroy();
				Dbx.getManager().removeDbx(this);
				closed = true;
			}
		} finally {
			dbLock.unlock();
		}
	}

	/**
	 * �ر����ݿ⡣�����޸ġ�
	 */
	public void close() {
		dbLock.lock();
		try {
			if (false == closed) {
				/* ???????????????????????????????????????????????????????????????????????????
				 * ʹ�ò�ͬ�� NullLogger �����ݿ��еĲ��ֱ���޸ĺ���ʹ�� GlobalLogger ���Ƿ���ȷ��
				 * �������ַ�ʽ�����������Թ�������1���������������б�֤����2����������Ҳû���⡣
				 */
				// 1. �޸����б�� magic-page ���ᱻˢ�¡�
//				for (DatabaseMetaData.Table table : metaData.getTables())
//					openTable(table.getName()).close();
//				// ToDo ���GlobalLogger������������־��rm -rf dblogs/*

				// 2. ֻ�رյ�ǰ�򿪵ı�
				for (Table table : this.getTables())
					table.close();
				//////////////////////////////////////////////////////////////////////////////
				Dbx.getManager().removeDbx(this);
				closed = true; // must after openTable
				assert(this.tables.isEmpty());
			}
		} finally {
			dbLock.unlock();
		}
	}

	/**
	 * �Ƿ��Ѿ��رա�
	 * @return
	 */
	public boolean isClosed() {
		dbLock.lock();
		try {
			return closed;
		} finally {
			dbLock.unlock();
		}
	}

	/**
	 * @return Database Home
	 */
	public File getHome() {
		return home;
	}

	public DatabaseMetaData getMetaData() {
		return metaData;
	}

	/**
	 * �������д򿪵ı�
	 * @return
	 */
	public Table[] getTables() {
		dbLock.lock();
		try {
			return tables.values().toArray(new Table[tables.size()]);
		} finally {
			dbLock.unlock();
		}
	}

	public Table getTable(String tableName) {
		dbLock.lock();
		try {
			return tables.get(tableName);
		} finally {
			dbLock.unlock();
		}
	}

	private final Table removeTable(String tableName) {
		dbLock.lock();
		try {
			return this.tables.remove(tableName);
		} finally {
			dbLock.unlock();
		}
	}
	/**
	 * ���ݱ����򿪱���������Ѿ��򿪣��򷵻�����cache��cacheHigh=512, cacheLow=128
	 */
	public Table openTable(String tableName) {
		return openTable(tableName, 512, 128);
	}

	/**
	 * ���ݱ����ô򿪱���������Ѿ��򿪣��򷵻�����
	 */
	public Table openTable(mkdb.TableConf tconf) {
		return openTable(tconf.getName(), tconf.getCacheHigh(), tconf.getCacheLow());
	}

	/**
	 * �½����(����Meta�ж���ı��)���������Ѿ��򿪣��򷵻�����
	 * @param tableName
	 * @return
	 */
	public Table newTable(String tableName) {
		return newTable(tableName, 512, 128);
	}

	/**
	 * �½����(����Meta�ж���ı��)���������Ѿ��򿪣��򷵻�����
	 * @param tableName
	 * @param cacheHigh
	 * @param cacheLow
	 * @return
	 */
	public Table newTable(String tableName, int cacheHigh, int cacheLow) {
		if (null != this.metaData.getTable(tableName))
			throw new IllegalStateException("table in MetaData: " + tableName);
		return _openTable(tableName, cacheHigh, cacheLow);
	}

	/**
	 * �򿪱���������Ѿ��򿪣��򷵻�����
	 */
	public Table openTable(String tableName, int cacheHigh, int cacheLow) {
		if (null == this.metaData.getTable(tableName))
			throw new IllegalStateException("table not found in MetaData: " + tableName);
		return _openTable(tableName, cacheHigh, cacheLow);
	}

	/**
	 * �򿪱���������Ѿ��򿪣��򷵻�����
	 */
	private Table _openTable(String tableName, int cacheHigh, int cacheLow) {
		dbLock.lock();
		try {
			if (closed)
				throw new IllegalStateException("Database has closed.");
			Table table = tables.get(tableName);
			if (null == table) {
				table = new Table(new mkdb.Logger(), tableName, cacheHigh, cacheLow);
				tables.put(tableName, table);
			}
			return table;
		} finally {
			dbLock.unlock();
		}
	}

	/////////////////////////////////////////////////////////
	// ����
	private TimerTask autoSaveTask = null;

	/**
	 * �������ݡ���ʹû���޸�Ҳ��Ҫ���á��������db.h��cache��
	 * ���������ʱ�����е����ݶ��ᱻ�����ڴ档
	 */
	public void save() {
		dbLock.lock();
		try {
			if (closed)
				throw new IllegalStateException("Database has closed.");

			for (Table table : tables.values())
				table.save();
		} finally {
			dbLock.unlock();
		}
	}

	public final void lock() {
		dbLock.lock();
	}
	
	public final void lockInterruptibly() {
		try {
			dbLock.lockInterruptibly();
		} catch (InterruptedException ex) {
			throw new mkdb.XLockInterrupted("DBNullLogger:" + tableHome);
		}
	}

	public final void unlock() {
		dbLock.unlock();
	}

	/**
	 * �Զ���ʱ���沢�����db.h��cache����������һ���߳���ִ�С�
	 * ������̰߳�ȫû�а��գ����Լ�����save()������ÿ����10�����¼������һ�Ρ�
	 * @param period ��������롣
	 */
	public void setAutoSave(long period) {
		dbLock.lock();
		try {
			if (null != autoSaveTask) {
				autoSaveTask.cancel();
				autoSaveTask = null;
			}
			if (period > 0) {
				autoSaveTask = new TimerTask() {
					@Override
					public void run() {
						Dbx.this.save();
					}
				};
				Dbx.getManager().getTimer().schedule(autoSaveTask, period, period);
			}
		} finally {
			dbLock.unlock();
		}
	}
}
