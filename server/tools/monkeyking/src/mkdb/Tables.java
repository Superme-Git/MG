package mkdb;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.*;
import java.text.SimpleDateFormat;

public class Tables {
	private final Map<String, Table> tables = new HashMap<String, Table>();
	private final ReentrantReadWriteLock flushLock = new ReentrantReadWriteLock(); 
	private final Lock checkpointLock = new ReentrantLock();
	private Logger logger;

	// Table ���� Storage �򿪹رա����� Table��Storage �ǿ�ѡ�ġ�
	// ���ﵥ������һ������Storage���б��������ݱ���ʡ�
	private List<Storage> storages = new ArrayList<Storage>();
	private TableSys tableSys = new TableSys();

	public final TableSys getTableSys() {
		return tableSys;
	}

	public final Table getTable(String name) {
		return tables.get(name);
	}

	public final Collection<Table> getTables() {
		return tables.values();
	}

	protected Tables () {
		// add system table
		add(tableSys);
	}

	protected final void add(Table table) {
		if (null != tables.put(table.getName(), table))
			throw new XError("duplicate table name " + table.getName());
	}

	final void open(MkdbConf conf) {
		if (null != logger)
			throw new XError("tables opened");

		mkdb.util.Dbx.load(conf.getLibdbPath(), conf.getDbHome());

		// db.h need a separator
		String logdir = conf.getLogHome().toString() + File.separator; 
		// һ���������Σ���������������޸�����Ҫ�ر������´����б��
		for (int i = 0; i < 2; ++i) {
			logger = new Logger(logdir, conf.getLogPages());
			for (Table table : tables.values()) {
				Storage storage = table.open(conf, logger);
				if (null != storage)
					storages.add(storage);
			}
			int v = logger.verify();
			if (0 == v) // success
				break;
			if (1 == v && 0 == i) {
				// ��һ�η����ɻָ��Դ���ʱ�����³���һ�Ρ�close, and retry
				close();
				continue;
			}
			throw new XError("db corrupt @" + i);
		}

		// <<�� table.initialize ǰ>>�ȸ����еı�������id�������뿴�ڲ�ʵ�֡�
		mkdb.Lockeys.getInstance().initializeLockIdMap(tables.values());
		for (Table table : tables.values()) {
			table.initialize(this);
		}
	}

	final void close() {
		storages.clear();

		for (Table table : tables.values())
			table.close();

		if (null != logger) {
			logger.close();
			logger = null;
		}		
	}

	final Lock flushReadLock() {
		return flushLock.readLock();
	}

	final boolean isHeldFlushWriteLockByCurrentThread() {
		return flushLock.writeLock().isHeldByCurrentThread();
	}

	final Lock flushWriteLock() {
		return flushLock.writeLock();
	}

	final void logNotify() {
		for (Table table : tables.values()) {
			table.logNotify();
		}
	}

	final Lock checkpointLock() {
		return checkpointLock;
	}

	///////////////////////////////////////////////////////////////////////
	// �����κδ��󣬾���ֹ���򡣲��������
	/*
	final int flush() {
		Lock lock = flushrwlock.writeLock();
		lock.lock();
		int count = 0;
		for (Storage storage : storages)
			count += storage.flush();
		lock.unlock();
		return count;
	}
	*/

	final List<Storage> getStorages() {
		return storages;
	}

	final Logger getLogger() {
		return logger;
	}

	private void copy(File src, File destDir) throws Exception {
		if (Trace.isDebugEnabled())
			Trace.debug("copy " + src + " " + destDir);

		InputStream is = new FileInputStream(src);
		try {
			OutputStream os = new FileOutputStream(new File(destDir, src.getName()));
			try {
				byte [] buffer = new byte[8192];
				int rc = 0;
				while ((rc = is.read(buffer)) > 0) {
					os.write(buffer, 0, rc);
				}
			} finally {
				os.close();
			}
		} finally {
			is.close();
		}
	}

	private static SimpleDateFormat sdf;
	static {
		sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setLenient(false);
	}

	public static final boolean isBackupDir(File backup) {
		try {
			sdf.parse(backup.getName());
			return true;
		} catch (java.text.ParseException e) {
			return false;
		}
	}

	// ȫ���ݣ�ɾ������־��
	final void backupFull() throws Throwable {
		MkdbConf conf = Mkdb.getInstance().getConf();
		//File tableHome = conf.getTableHome();

		String backupName = sdf.format(Calendar.getInstance().getTime());
		File backupDir = new File(conf.getBackupDir(), backupName);

		Trace.warn("backup FULL begin");

		// tables: copy data file
		File dataDir = new File(backupDir, MkdbConf.DATADIR);
		dataDir.mkdirs();
		for (Storage storage : storages)
			copy(storage.getFile(), dataDir);

		// logger: removeOlder and copy
		File logsDir = new File(backupDir, MkdbConf.LOGSDIR);
		logsDir.mkdirs();
		logger.removeOlder();
		for (File log : Logger.listFiles())
			copy(log, logsDir);

		new FileOutputStream(new File(backupDir, "database.backup.done")).close();
		Trace.warn("backup FULL end");
	}

	public static SortedMap<String, File> backups() {
		return backups(Mkdb.getInstance().getConf().getBackupDir());
	}

	public static SortedMap<String, File> backups(File home) {
		SortedMap<String, File> sorted = new TreeMap<String, File>();
		File [] files = home.listFiles();
		if (null != files) {
			for (File file : files) {
				if (file.isDirectory() && isBackupDir(file))
					sorted.put(file.getName(), file);
			}
		}
		return sorted;
	}

	// ��������
	final void backupInc() throws Throwable {
		Trace.warn("backup INCREMENT begin");

		SortedMap<String, File> backups = backups();
		if (backups.isEmpty()) {
			Trace.warn("backup INCREMENT end. NO FULL BACKUP FOUND.");
			return;
		}
		File lastBackupLogsDir = new File(backups.get(backups.lastKey()), MkdbConf.LOGSDIR);
		SortedMap<String, File> lastBackupLogs = Logger.sortedFiles(lastBackupLogsDir);
		if (Trace.isDebugEnabled())
			mkdb.Trace.debug(lastBackupLogs.values());
		for (File log : Logger.listFiles()) {
			if (false == lastBackupLogs.containsKey(log.getName()))
				copy(log, lastBackupLogsDir);
		}
		// ȫ����ʱ���϶�������һ����־��Ҳ����˵��lastBackupLogs�϶���Ϊ�գ�
		// ���һ�α��ݹ�ȥ����־��Ҫ���¿�����
		if (!lastBackupLogs.isEmpty())
			copy(lastBackupLogs.get(lastBackupLogs.lastKey()), lastBackupLogsDir);

		Trace.warn("backup INCREMENT end");
	}
}
