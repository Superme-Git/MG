package com.pwrd.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.locojoy.base.Marshal.OctetsStream;
import com.pwrd.dbx.DbxManager;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

public class ConflictChecker implements Runnable {
	private static Log logger = LogFactory.getLog(ConflictChecker.class);

	/**
	 * ����ϲ������ݿ⼯�ϣ����ݿ�ʵ����<code>DbxManager</code>����
	 */
	private Set<String> dbxNames = new HashSet<String>();
	// private Map<String, Dbx> dbxs = new HashMap<String, Dbx>();

	/**
	 * �������ݿ���ͬһ�ű������������ݿ��е�rolenames����key���ϣ� ���ǵ���ͻ�Ĵ��ڣ����ʹ��Entry<Object,
	 * String>��ʾ��key�� һ�γ��ֵ����ݿ⡣
	 */
	private Map<Object, String> keysOfOneTableInDbxs = new HashMap<Object, String>();
	/**
	 * ����ͻ��ӳ�䡣��¼��ÿ������ڵ�ӳ�伯�ϡ�
	 */
	private Map<String, Map<Object, Conflict>> table2Conflicts = new HashMap<String, Map<Object, Conflict>>();
	/**
	 * ÿ�����ݿ⽨��һ����־�ļ���������������ݿ��д��ڵĳ�ͻ��
	 */
	private Map<String, BufferedWriter> dbx2Logs = new HashMap<String, BufferedWriter>();

	/*
	 * public ConflictChecker(String[] dbpaths) { for(int i = 0; i <
	 * dbpaths.length; ++ i) { File file = new File(dbpaths[i]); Dbx dbx =
	 * Dbx.open(file, DatabaseMetaData.getInstance()); dbxs.put(file.getName(),
	 * dbx); File logFile = new File(file.getName() + ".log");
	 * if(!logFile.exists()) { try { logFile.createNewFile(); } catch
	 * (IOException e) { e.printStackTrace(); } } BufferedWriter bw = null; try
	 * { bw = new BufferedWriter(new FileWriter(logFile)); } catch (IOException
	 * e) { e.printStackTrace(); } dbx2Logs.put(file.getName(), bw); } }
	 */

	public ConflictChecker(String[] _dbxNames) {
		for (int i = 0; i < _dbxNames.length; ++i) {
			dbxNames.add(_dbxNames[i]);
			File logFile = new File(_dbxNames[i] + ".log");
			if (!logFile.exists()) {
				try {
					logFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(logFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
			dbx2Logs.put(_dbxNames[i], bw);
		}
	}

	/**
	 * ��������ݿ��еļ�¼��ͻ��������¼���ڳ�ͻ�����ҽ������ǵ� <code>Key</code>��ͬ��
	 */
	public void checkConflicts() {
		// ���μ��ÿһ�ű��еĳ�ͻ
		for (DatabaseMetaData.Table tableDef : DatabaseMetaData.getInstance().getTables()) {
			if (!tableDef.isPersistence())
				continue;
			logger.info("Start checking table " + tableDef.getName());
			// �ڴ�����һ�ű�֮ǰ���������һ�ű��еļ�����
			keysOfOneTableInDbxs.clear();
			IWalkImpl impl = new IWalkImpl(tableDef);
			// ��ÿ�����ݿ�����ű����walk����
			for (String dbxName : dbxNames) {
				impl.setDbxName(dbxName);
				Dbx.Table table = DbxManager.getInstance().getDbx(dbxName).openTable(tableDef.getName());
				table.walk(impl);
			}
			logger.info("End checking table " + tableDef.getName());
		}
	}

	/**
	 * ��ÿ�����ݿ���ڵĳ�ͻ��¼�������־�ļ��С�
	 * 
	 * @throws IOException
	 */
	public void outputConflicts() throws IOException {
		for (String tableName : table2Conflicts.keySet())
			logger.info(tableName);
		for (Map.Entry<String, Map<Object, Conflict>> entry : table2Conflicts.entrySet()) {
			String tableName = entry.getKey();
			Map<Object, Conflict> conflicts = entry.getValue();
			if (conflicts == null)
				return;
			DatabaseMetaData.Table tableDef = DatabaseMetaData.getInstance().getTable(tableName);
			for (Map.Entry<Object, Conflict> conflict : conflicts.entrySet()) {
				Object confKey = conflict.getKey();

				String dbList = "(";
				String[] dbNames = new String[conflict.getValue().getOccurrenceDbs().size()];
				dbNames = conflict.getValue().getOccurrenceDbs().toArray(dbNames);
				int i = 0;
				for (; i < dbNames.length - 1; ++i)
					dbList += dbNames[i] + ", ";
				dbList += dbNames[i] + ")";

				for (String db : conflict.getValue().getOccurrenceDbs()) {
					OctetsStream rawValue = DbxManager.getInstance().getDbx(db).openTable(tableName)
							.find(tableDef.getKeyType().marshal(confKey));
					Object confValue = rawValue == null ? "null" : tableDef.getValueType().unmarshal(rawValue);
					String msg = "Table " + entry.getKey() + " : (" + confKey.toString() + ") -> ("
							+ confValue.toString() + ")" + " exists in ";
					msg += dbList + "\n";
					dbx2Logs.get(db).write(msg);
				}
			}
		}
	}

	public void closeLogs() {
		for (BufferedWriter bw : dbx2Logs.values()) {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		checkConflicts();
		try {
			outputConflicts();
		} catch (IOException e) {
			logger.error("Fail to output conflict information");
			e.printStackTrace();
		}
		closeLogs();
	}

	private class IWalkImpl implements mkdb.Storage.IWalk {
		private DatabaseMetaData.Table _def;
		private String _dbxName;

		public IWalkImpl(DatabaseMetaData.Table def) {
			_def = def;
		}

		public void setDbxName(String dbxName) {
			_dbxName = dbxName;
		}

		@Override
		public boolean onRecord(byte[] key, byte[] data) {
			Object _key = _def.getKeyType().unmarshal(key);
			if (keysOfOneTableInDbxs.containsKey(_key)) // An conflict has
														// occurred
			{
				Map<Object, Conflict> conflicts = table2Conflicts.get(_def.getName());
				if (conflicts == null) {
					conflicts = new HashMap<Object, Conflict>();
					table2Conflicts.put(_def.getName(), conflicts);
				}
				// �жϸü������Ƿ�����˳�ͻ��¼
				if (conflicts.containsKey(_key))
					conflicts.get(_key).addOccurrenceDb(_dbxName);
				else {
					Conflict confOccur = new Conflict(_key);
					confOccur.addOccurrenceDb(keysOfOneTableInDbxs.get(_key));
					confOccur.addOccurrenceDb(_dbxName);
					conflicts.put(_key, confOccur);
				}
			} else
				keysOfOneTableInDbxs.put(_key, _dbxName);

			return true;
		}
	}

	/**
	 * һ��<code>Conflict</code>�����ʾĳ�����һ��<code>Key</code>�ڲ�����
	 * �Ķ�����ݿ��д��ڳ�ͻ�����������������ݿ��д��ڸ�<code>Key</code>���ö��� ��¼�˷�����ͻ��<code>Key</code>�Լ���
	 * <code>Key</code>���ڵ��������ݿ⡣
	 * 
	 * @author wp853
	 *
	 */
	public static class Conflict {
		/**
		 * ������ͻ��<code>Key</code>��
		 */
		private Object key;
		/**
		 * ��ͻ�������������ݿ⡣
		 */
		private Set<String> occurrenceDbs;

		public Conflict(Object k) {
			key = k;
			occurrenceDbs = new HashSet<String>();
		}

		public void addOccurrenceDb(String db) {
			occurrenceDbs.add(db);
		}

		public Set<String> getOccurrenceDbs() {
			return Collections.unmodifiableSet(occurrenceDbs);
		}

		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj instanceof Conflict) {
				Conflict occur = (Conflict) obj;
				return (key == null ? occur.key == null : key.equals(occur.key));
			}
			return false;
		}

		public int hashCode() {
			return key == null ? 0 : key.hashCode();
		}
	}

	public static void main(String[] args) {
		int beginIndex = 0;
		for (int i = 0; i < args.length; ++i) {
			if (!args[i].equals("-dbs"))
				continue;
			else {
				beginIndex = i + 1;
				break;
			}
		}
		String[] dbs = new String[args.length - beginIndex];
		for (int i = 0; i < dbs.length; ++i)
			dbs[i] = args[i + beginIndex];
		for (String dbxPath : dbs)
			DbxManager.getInstance().addDbx(dbxPath);
		ConflictChecker checker = new ConflictChecker(DbxManager.getInstance().getDbxNames().toArray(new String[0]));
		checker.checkConflicts();
		try {
			checker.outputConflicts();
		} catch (IOException e) {
			e.printStackTrace();
		}
		checker.closeLogs();
	}
}
