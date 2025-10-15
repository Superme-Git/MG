package xmerge;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

import mkdb.Storage.IWalk;
import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

/**
 * �ϲ�����
 * <li>srcDB���ᱻ�޸ģ���κϲ���������һ����������б�ĺϲ�����
 * <li>key��ͻʱ������xmerge.xml�����ж�Ӧʵ�ֵ�merge�������кϲ�
 * <li>merge�ص��Ĵ����жϺϲ�����׽�쳣����¼��־
 * */
public final class Merger{
	private String resultDBDir = XmergeConf.getInstance().getResultDbHome();
	private Dbx srcDB = null;
	private Dbx destDB = null;
	private KnownMergers mergers = null;
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
	
	//ͳ������ֻ��¼Known_key_conflict�ص�ʱ���쳣�����ͷ����쳣��key
	TableStatistics statistic;
	boolean walkUnknownFailed;
	boolean walkKnownFailed;
	
	public Merger(){
		this.walkUnknownFailed = false;
		this.walkKnownFailed = false;
		this.statistic = new TableStatistics();
		this.mergers = new KnownMergers();
	}

	public void SetSrcDB(String srcDir) throws Exception{
		if ( null == srcDir || srcDir.isEmpty())
			throw new Exception("SetSrcDB error... ...");
		else{
			File src_db = new File("./"+srcDir);
			this.srcDB = Dbx.open(src_db, DatabaseMetaData.getInstance());
		}
	}
	
	/**
	 * notCopyָʾ�Ƿ���merger�����ļ�����
	 * */
	public void SetDestDB(String destDir, boolean notCopy) throws Exception{
		if (null == destDir || destDir.isEmpty())
			throw new Error("SetDestDB error... ...");
		File dest_db = new File(destDir);
		File tmp = null;
		if(notCopy){
			tmp = dest_db;
			SetResultDB(destDir);
		}else{
			//ֱ��copy�ɣ����ж�cp��ȥ��Ŀ���ַ������
			xmerge.Util.copyFolder(destDir, resultDBDir);
			tmp = new File(resultDBDir);
			SetResultDB(resultDBDir);
		}
		this.destDB = Dbx.open(tmp, DatabaseMetaData.getInstance());
	}
	
	public void SetResultDB(String resDir) throws Exception{
		if ( null == resDir || resDir.isEmpty())
			throw new Exception("resDir is invalid... ...");
		else
			this.resultDBDir = resDir;
	}

	public String getSrcDBDir(){
		return this.srcDB.getHome().getAbsolutePath();
	}
	
	public String getDestDBDir(){
		return this.destDB.getHome().getAbsolutePath();
	}
	
	public String getResultDBDir(){
		if(walkKnownFailed || walkUnknownFailed) 
			return null; 
		else
			return this.resultDBDir;
	}
	
	public void addIMerge(String tablename, IMerge merger){
		this.mergers.addImerge(tablename, merger);
	}
	
	public void removeIMerge(String tablename){
		this.mergers.removeImerge(tablename);
	}
	
	public IMerge getIMerge(String tablename){
		return this.mergers.getImerge(tablename);
	}
	
	/**
	 * �ϲ�����
	 * */
	public boolean doMerge() throws Exception{
		this.walkKnownFailed = false;
		this.walkUnknownFailed = false;
		try{
			//����srcDB�е�tables��ֻ����MetaData�����õ���������
			for(DatabaseMetaData.Table table : srcDB.getMetaData().getTables()){
				if(!table.isPersistence()) 
					continue;
				Dbx.Table src_table = srcDB.openTable(table.getName());
				try{
					boolean isKnown = XmergeConf.getInstance().isKnownConflict(table.getName());
					/**
					 * <li>��src_table��walk���޸�dest_table
					 * <li>Known_key_conflict�ı���walk��Unknown_key_conflict�ı���_walk��
					 * <li>����walkUnknownFailed��ǩ���ж�walkʱ�Ƿ����(Unknown_key_conflict)
					 * <li>����walkKnownFailed��ǩ���ж�merge known_key_conflict�Ƿ����쳣
					 **/
					if(isKnown){
						knownWalker walker = new knownWalker(table.getName());
						src_table.walk(walker);	
						if(Merger.this.walkKnownFailed){
							mkdb.Trace.fatal("mylogic error exit at table: " + table.getName());
							throw new Error("IMerge callback exception, exit xmerge... ...");
						}else{
							walker.finish();
						}
					}else{
						unknownWalker walker = new unknownWalker(table.getName());
						src_table._walk(walker);	
						if(Merger.this.walkUnknownFailed){
							mkdb.Trace.info("Unknown table merge error exit at " + table.getName());
							return false;
						}else{
							walker.finish();
						}
					}
				}finally{
					destDB.openTable(table.getName()).close();
					src_table.close();
				}
			}
			mergeSys();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			srcDB.close();
			destDB.close();xmerge.Util.deleteDir(new File("asdf"));
			if(Merger.this.walkKnownFailed)
				rollbackAll();
		}
		
		//���еı��ϲ��ɹ��ˣ���Ҷ�����Ъ��
		if(this.statistic.hasLog()){
			String date = dateFormat.format(Calendar.getInstance().getTime());
			FileWriter log = new FileWriter("merge_unknown_"+date.toString()+".log");
			log.write(this.statistic.toString());
			log.close();
			this.statistic.clearStatistic();
		}
		return true;
	}

	private void mergeSys() {
		Dbx.Table srcSysTable = srcDB.newTable("_sys_");
		srcSysTable.walk(new IWalk() {
			@Override
			public boolean onRecord(byte[] _key, byte[] _data) {
				try {
					OctetsStream key = OctetsStream.wrap(Octets.wrap(_key));
					OctetsStream value = OctetsStream.wrap(Octets.wrap(_data));
					
					String str = new String();
					str = key.unmarshal_String(mkdb.Const.IO_CHARSET);
					int localId = (int) str.charAt(str.length() - 1);
					mkdb.util.AutoKeys autos = new mkdb.util.AutoKeys(value, localId, false);
					
					if (destDB.newTable("_sys_").exist(key)) {
						mkdb.Trace.info("mergeSys insert faile. key:" + str + " value:" + autos);
						throw new RuntimeException("_sys_ table has conflict key...");						
					}
					mkdb.Trace.info("mergeSys insert OK. key:" + str + " value:" + autos);
					destDB.newTable("_sys_").insert(key, value);
				} catch (MarshalException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}			
		});
	}

	public void rollbackAll(){
		xmerge.Util.deleteDir(this.destDB.getHome().getAbsoluteFile());
	}

	/**
	 * Unknown_key_conflict _walk implementation
	 * */
	public class unknownWalker implements mkdb.Storage.IWalk {
		private Dbx.Table destTable;
		private long count = 0;
		
		unknownWalker(String dest_table_name) {
			this.destTable = destDB.openTable(dest_table_name);
		}
		
		@Override
		public boolean onRecord(byte[] _key, byte[] _data) {
			++count;
			if (count > XmergeConf.MAX_WALK_COUNT) {
				destTable.save();
				count = 0;
			}
			
			OctetsStream src_key = OctetsStream.wrap(Octets.wrap(_key));
			OctetsStream src_value = OctetsStream.wrap(Octets.wrap(_data));
			if(this.destTable._insert(src_key, src_value)){
				return true;
			}else{
				mkdb.Trace.info("Unknown key conflict occured in table " + destTable.getName());
				Merger.this.walkUnknownFailed = true;
				return false;
			}
		}
		
		public void finish(){
			mkdb.Trace.info("Merge " + destTable.getName() + " finish");
		}
	}
	/**
	 * Known_key_conflict walk implementation
	 * */
	public class knownWalker implements mkdb.Storage.IWalk {
		private final DatabaseMetaData.Type keyType;
		private Dbx.Table destTable;
		private IMerge merger;
		private long count = 0;

		knownWalker(String dest_table_name){
			DatabaseMetaData.Table tableMeta = DatabaseMetaData.getInstance().getTable(dest_table_name);
			this.keyType = tableMeta.getKeyType();
			this.destTable = destDB.openTable(dest_table_name);
			this.merger = XmergeConf.getInstance().getMerger(this.destTable.getName());
		}

		
		public boolean onRecord(byte[] _key, byte[] _data) {
			++count;
			if (count > XmergeConf.MAX_WALK_COUNT) {
				destTable.save();
				count = 0;
			}
			
			OctetsStream src_key = OctetsStream.wrap(Octets.wrap(_key));
			OctetsStream src_value = OctetsStream.wrap(Octets.wrap(_data));

			if(this.destTable.insert(src_key, src_value)){
				return true;
			}else{
				try{
						OctetsStream dest_value = this.destTable.find(src_key);
						if(mergeKnownConflictKey(src_key, src_value, dest_value)){
							return this.destTable.replace(src_key, dest_value);
						}else{
							if(Merger.this.walkKnownFailed){
								return false;
							}else{
								/**
								 * ������Ϊ����false��һ�ֿɴ�����߼����������־��¼
								 * Ŀǰֻ��¼UserMergeImplement����
								 * ����ʱ��key�Լ��������
								 * */
								OctetsStream tmp_key = OctetsStream.wrap(Octets.wrap(_key));
								Object key = this.keyType.unmarshal(tmp_key);
								Merger.this.statistic.addKey(this.destTable.getName(), key);
								Merger.this.statistic.incrementCount(this.destTable.getName());
								return true;
							}
						}
				    }catch(Exception e){
				    	mkdb.Trace.debug("mergeKnownConflictKey throw exception... ...");
				    	e.printStackTrace();
						Merger.this.walkKnownFailed = true;
				    	return false;
				    }
			}
		}

		/**
		 * ����mergers�в��Ҷ�̬��ӵ�IMerge�������û�У����xmerge�����л�ȡ��̬IMerge����
		 * */
		private boolean mergeKnownConflictKey(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception{
			IMerge obj = mergers.getImerge(this.destTable.getName());
			if(null == obj) {
				obj = this.merger;
				mergers.addImerge(this.destTable.getName(), obj);
			}
			try{
				if(obj.merge(src_key, src_value, dest_value))
					return true;
			}catch(Exception e){
				mkdb.Trace.debug("userMergeImplement throw exception");
				Merger.this.walkKnownFailed = true;
				return false;
			}
			return false;
		}
		public void finish() {
			mkdb.Trace.info("Merge " + destTable.getName() + " finish");
		}
	}
	
	
	//for test only
	public class testWalker implements mkdb.Storage.IWalk {
		private final DatabaseMetaData.Type keyType;
		private final DatabaseMetaData.Type valueType; 
		private int count = 0;
		
		testWalker(DatabaseMetaData.Table tableMetaData) {
			this.keyType = tableMetaData.getKeyType();
			this.valueType = tableMetaData.getValueType();
		}
		public boolean onRecord(byte[] _key, byte[] _data) {
			++count;
			Object key = this.keyType.unmarshal(_key);
			Object value = this.valueType.unmarshal(_data);
			mkdb.Trace.debug("	key=" + key + " value=" + value);
			return true;
		}
		public void printCount(){
			mkdb.Trace.debug(count);
		}
	}
	/**
	 * ֻ�Ǹ��򵥵Ĳ��Է���
	 * */
	public void benchMerge(File home, DatabaseMetaData.Table table) throws Exception{
		//�ϲ����˼����
		mkdb.util.Dbx.start("test");
		Dbx db = Dbx.open(home, DatabaseMetaData.getInstance());
		Dbx.Table Dtable = db.openTable(table.getName());
		testWalker walker = new testWalker(table);
		Dtable.walk(walker);
		walker.printCount();
	}
}
