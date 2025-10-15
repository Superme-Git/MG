package xmerge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;
import xmerge.ForeignMeta.TableDepends;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/*
 * Repair��ȡerror_keys_db�����д洢���׶η��ֵĴ���unkonwn_key_conflict�ı���Ӧ�ļ�¼��
 * ������Ҫkey���ɣ�����ҪChecker�ṩerror_keys_db·��  
 */
public class Repair {
	private Dbx errorKeysDb;
	private Dbx resultDb;
	private Dbx otherDb;
	private DatabaseMetaData curMeta;
	private TableStatistics statistic = RepairConf.getInstance().getStatistic();
	private Allocators keyAllocators;
	/*
	 * ����errorKeysDbHome�еĴ���key��Ϣ�޸�srcDbHomeĿ¼�µ����ݿ⣬
	 * �޸���srcDbHome��ֱ�ӽ��У����ٵ�������һ�����ݿ����޸ģ�ʹ����������������
	 */
	public Repair() {
		curMeta = DatabaseMetaData.getInstance();
		RepairConf.getInstance().initialize();
		// Ĭ�ϵĹ���Ŀ¼ΪdestDbHome������ʹ��srcDbHome��ΪotherDb���вο�
		RepairConf.getInstance().setWorkDbHome(XmergeConf.getInstance().getDestDbHome());
		RepairConf.getInstance().setOtherDbHome(XmergeConf.getInstance().getSrcDbHome());
		keyAllocators = new Allocators();
    }
	
	protected void finalize() {
		clean();
    }
	
	public void addAllocator(String tableName, IAllocator allocator) {
		keyAllocators.addAllocator(tableName, allocator);
	}
	
	public void setWorkDbHome(String dbHome) {
		RepairConf.getInstance().setWorkDbHome(dbHome);
	}
	
	public void setOtherDbHome(String dbHome) {
		RepairConf.getInstance().setOtherDbHome(dbHome);
	}
	
	private void initialize() throws FileNotFoundException, IOException {
		Dbx.start(".");
		// ��ֱ��ʹ��errorKeysDbHome�����ǿ���һ�ݣ���Ϊ�޸������п����޸�errorKeysDbHome��ļ�¼
		Util.copyFolder(XmergeConf.getInstance().getErrorKeysDbHome(), RepairConf.getInstance().getTempErrorKeysDbHome());
		Util.copyFolder(RepairConf.getInstance().getWorkDbHome(), RepairConf.getInstance().getResultDbHome());
		
		errorKeysDb = Dbx.open(new File(RepairConf.getInstance().getTempErrorKeysDbHome()), curMeta);
		resultDb = Dbx.open(new File(RepairConf.getInstance().getResultDbHome()), curMeta);
		otherDb = Dbx.open(new File(RepairConf.getInstance().getOtherDbHome()), curMeta);
		keyAllocators.initialize(resultDb);
	}
	
	private void clean() {
		File errorKeysDir = new File(RepairConf.getInstance().getTempErrorKeysDbHome());
		Util.deleteDir(errorKeysDir);
	}
			
	private void openTables(Dbx db) {
		for (DatabaseMetaData.Table tableMetaData : db.getMetaData().getTables()) {
			if (!tableMetaData.isPersistence())
				continue;
			db.openTable(tableMetaData.getName());
		}
	}

	/*
	 * �޸����߼�������IWalk()����errorKeysDb
	 */
	public boolean repairDb() {
		try {
			initialize();
			openTables(errorKeysDb);
			openTables(resultDb);
			openTables(otherDb);
			
			Iterator<Map.Entry<String, TableDepends>> itr = RepairConf.getInstance().getDepRoots().entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, TableDepends> entry = itr.next();
				TableDepends depends = entry.getValue();
				repairTable(depends);				
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			errorKeysDb.close();
			resultDb.close();
			otherDb.close();
			keyAllocators.destory();
			clean();
		}
		return true;
	}
	
	/*
	 * �Ż����޸�ʱ����load���е�errorKey��Ȼ������ñ�����й������еļ�¼�������޸�
	 */
	private void repairTable(TableDepends depends) {
		// ���޸���, load errorKeys
		{
			RepairForeignTable foreigner = new RepairForeignTable();
			newKeySets.clear();
			Dbx.Table table = errorKeysDb.getTable(depends.getName());
			Walker repairer = new Walker(depends, foreigner);
			table.walk(repairer);
			if (repairer.getCount() != 0) {
				mkdb.Trace.info("Repairing " + depends.getName() + " contains " + repairer.getCount() + " errorKey.");
				// ���޸�foreign��errorKeys�ļ�¼
				foreigner.repairRefTables(depends);
			}
		}
		// ���޸�keyRef����
		{
			Iterator<Map.Entry<String, TableDepends>> itr = depends.getRefKeyTables().entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, TableDepends> entry = itr.next();
				TableDepends subDepends = entry.getValue();
				repairTable(subDepends);				
			}
		}
	}

	// ��ȡ�޸�������ݿ�Ŀ¼������û����repairDb()ʧ�ܵ��������ʱ��ʹ�����Լ�����
	public String getResultDbHome() {
		return RepairConf.getInstance().getResultDbHome();
	}
	
	private Set<Object> newKeySets = new HashSet<Object>();
	class Walker implements mkdb.Storage.IWalk {
		private Dbx.Table table;
		private DatabaseMetaData.Table tableMetaData;
		private RepairForeignTable foreigner;
		private long count = 0;

		Walker(TableDepends depends, RepairForeignTable foreigner) {
			this.foreigner = foreigner;
			this.tableMetaData = curMeta.getTable(depends.getName());
			this.table = resultDb.getTable(depends.getName());
		}

		@Override
		public boolean onRecord(byte[] _key, byte[] _data) {
			++ count;
			OctetsStream key = OctetsStream.wrap(Octets.wrap(_key));
			OctetsStream value = table.find(key);
			if (value == null) {
				statistic.addKey(table.getName(), tableMetaData.getKeyType().unmarshal(_key));
				throw new RuntimeException("repair fail : " + table.getName() + statistic.getKeyList(table.getName()));
			}
			
			Object keyObj = tableMetaData.getKeyType().unmarshal(_key);
			Dbx.Table otherTable = otherDb.openTable(table.getName());
			int reAllocate = 0;
			OctetsStream newKey;
			Object newKeyObj;
			do {
				newKey = keyAllocators.allocateKey(tableMetaData.getName(), keyObj);
				newKeyObj = tableMetaData.getKeyType().unmarshal(newKey);
				reAllocate++;
				if (reAllocate > 1) {
					mkdb.Trace.warn("try allocateNewKey " + reAllocate + " times for table " + table.getName());
				}
			} while (reAllocate < RepairConf.MAX_ALLOCATE_COUNT && (newKeySets.contains(newKeyObj) || 
					table.exist(newKey) || otherTable.exist(newKey)));
			if (reAllocate >= RepairConf.MAX_ALLOCATE_COUNT)
				throw new RuntimeException("allocateNewKey fail, key = " + tableMetaData.getKeyType().unmarshal(_key));
			
			if (table.remove(key) && table.insert(newKey, value)) {
				
			} else {
				statistic.addKey(table.getName(), tableMetaData.getKeyType().unmarshal(_key));
				throw new RuntimeException("repair fail : " + table.getName() + statistic.getKeyList(table.getName()));
			}
			mkdb.Trace.debug("allocateNewKey for " + table.getName() + "[" + keyObj + " -> " + newKeyObj + "]");
			foreigner.insertErrorKey(keyObj, newKeyObj);
			newKeySets.add(newKeyObj);
			
			// ��cache
			if (count % XmergeConf.MAX_WALK_COUNT == 0) {
				table.save();
				otherTable.save();
			}
			return true;
		}
		
		public long getCount() {
			return count;
		}
	}
			
	public static void main(String args[]) {
		XmergeConf.SetConf("./test/xmerge.xml");
		XmergeConf.getInstance().setSrcDbHome("./test/src_db");
		XmergeConf.getInstance().setDestDbHome("./test/dest_db");
		XmergeConf.getInstance().setErrorKeysDbHome("./test/err_db");
		XmergeConf.getInstance().setResultDbHome("./test/result_db");
		XmergeConf.getInstance().setForeignInfoDbHome("./test/fdb");
		XmergeConf.getInstance().setForeignInfoTableName("foreigntable");
		Dbx.start("./test");
		ICheckDupKey check= new CheckDupKey();
		if (check.checkDB(XmergeConf.getInstance().getSrcDbHome(), XmergeConf.getInstance().getDestDbHome())) {
			mkdb.Trace.info("CheckDb OK goto Repair... ...");
			XmergeConf.getInstance().setErrorKeysDbHome(check.getDupKeyDBPath());	
			Repair worker = new Repair();
			if (worker.repairDb())
				mkdb.Trace.info("Repair OK, result home : " + worker.getResultDbHome());	
		}
	}
}