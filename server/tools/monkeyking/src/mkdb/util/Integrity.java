package mkdb.util;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * <p>Mkdb ���ڽ���checkpointʱ�������쳣ֹͣ�����������쳣�ػ����������п��ܴ��ڲ�һ��״̬��
 * �ڼ���ʹ�����ݿ�ʱ����Ҫ�޸���xdb���Զ����ͳ����޸���һ��״̬������xdbӦ�ò���Ҫ����������⡣
 * ������޸�֮ǰʹ��Dbx������������ȥ�������ݣ��ͻ���������𻵡�
 * 
 * <p><b>��������������޸����ֲ�һ��״̬��</b> Dbx �ڴ�ʱ����ʹ���������޸�����״̬��
 * 
 * <p>������Զ���ʹ�á�
 */
public class Integrity {
	private final File logHome;
	private final File tableHome;

	private mkdb.Logger logger;
	private List<mkdb.Storage> tables = new ArrayList<mkdb.Storage>();
	private DatabaseMetaData metaData = null;

	/**
	 * ����ָ�����ݿ�Ŀ¼��֤���������ԡ�
	 * 
	 * @param libpath ��̬������·������̬���Ѿ�������װ�ڣ���Ϊnull��
	 * @param home ���ݿ�Ŀ¼
	 */
	public Integrity(String libpath, File home) {
		this(libpath, home, null);
	}

	/**
	 * ����ָ�����ݿ�Ŀ¼��֤���������ԡ�
	 * ͬʱ��֤DatabaseMetaData�����ı�������Ŀ¼�а����ı���ļ�һ�¡�
	 * 
	 * <p> <b>ֻ����ȷ�����û����ɾʱ������Ҫ��֤DatabaseMetaData��</b>
	 * 
	 * @param libpath ��̬������·������̬���Ѿ�������װ�ڣ���Ϊnull��
	 * @param home
	 * @param metaData Ϊnullʱ������֤DatabaseMetaData����Ϣ��
	 */
	public Integrity(String libpath, File home, DatabaseMetaData metaData) {
		if (null != libpath)
			Dbx.load(libpath);

		if (!home.isDirectory() || !home.exists())
			throw new IllegalStateException("dbhome : " + home + " (!isDirectory || !exists())");

		this.logHome = new File(home, mkdb.MkdbConf.LOGSDIR).getAbsoluteFile();
		this.tableHome = new File(home, mkdb.MkdbConf.DATADIR);
		this.metaData = metaData;
	}

	/**
	 * ��Ϊ Storage ������Ϊ abstract���޷�ֱ��ʹ�ã���Ȼ��ʵ���ϲ���Ҫ��ô������
	 * �����̳�Storage��ʹ��ʵ�������Խ��У���������ʵ�ֺ������κι��ܡ�
	 * @see mkdb.Storage
	 */
	private class Table extends mkdb.Storage {
		Table(mkdb.Logger logger, File tableHome, String tableName, int cacheHigh, int cacheLow) {
			super(logger, tableHome, tableName, cacheHigh, cacheLow);
		}
	}

	/**
	 * �Ƚ�metadata��Ϣ
	 * @return true,û���ҵ�metadata.xml�������ͬ��false,����ͬ
	 */
	public boolean checkMetaData(){	
		return metaData.isSame(this.tableHome.getParentFile());		
	}
	/**
	 * �����Լ����޸���
	 */
	public void verify() {
		////////////////////////////////////////////////////////////////
		// ������ݿ�Ŀ¼
		int magic = 0;
		if (this.logHome.exists())   magic += 1;
		if (this.tableHome.exists()) magic += 2;

		switch (magic) {
		// ���ݺ���־Ŀ¼�������ڡ�һ����˵�����ǿյ����ݿ⣬��Ϊ����ȷ�ġ�
		case 0: println("ok! it is an empty db."); return;
		// ������ȷ������Ŀ¼��
		case 1: case 2: throw new RuntimeException("is it a dbhome?");
		// �ðɣ�����Ŀ¼���ڡ� 
		case 3: break;
		}

		////////////////////////////////////////////////////////////////
		// ��֤MetaData��Ϣ��Ŀ¼�а����ı���ļ�һ�¡�
		final Set<String> tableNames = new HashSet<String>();
		for (File tableFile : this.tableHome.listFiles()) {
			if (!tableFile.isFile()) {
				println(tableFile + " is not a file! skip.");
				continue;
			}
			tableNames.add(tableFile.getName());
		}
		if (null != this.metaData) {
			final Set<String> tableMetas = new HashSet<String>();
			for (DatabaseMetaData.Table table : metaData.getTables()) {
				if (table.isPersistence()) // �����ڴ��
					tableMetas.add(table.getName());
			}
			if (!tableNames.equals(tableMetas))
				throw new RuntimeException("DatabaseMetaData?");
		}

		////////////////////////////////////////////////////////////////
		// ��������֤��
		final String logdir = this.logHome.toString() + File.separator; // db.h need a separator
		try {
			// ��������ڱ�������  mkdb.Tables.open(MkdbConf conf) һ�¡�
			// һ���������Σ���������������޸�����Ҫ�ر������´����б��
			for (int i = 0; i < 2; ++i) {
				logger = new mkdb.Logger(logdir, 4096);
				for (String tableName : tableNames) {
					tables.add(new Table(logger, this.tableHome, tableName, 512, 128));
				}
				int v = logger.verify();
				if (0 == v) {
					println("verify success!");
					break;
				}
				if (1 == v && 0 == i) {
					println("redo success! verify again.");
					close();
					continue;
				}
				throw new RuntimeException("db corrupt @" + i);
			}
		} finally {
			close(); // ����ʲô������ر���־�ͱ��
		}
	}

	private void close() {
		for (mkdb.Storage storage : tables) {
			//println(storage.getFile());
			storage.close();
		}
		tables.clear();

		if (null != logger) {
			logger.close();
			logger = null;
		}
	}

	private void println(Object message) {
		System.out.println(message);
	}

	public static void main(String args[]) {
		new Integrity("../test", new File("../test/mkdb")).verify();
	}
}
