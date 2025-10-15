package validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import mkdb.Storage.IWalk;
import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * 生成foreign关系表的db和table结构 
 **/
public class ForeignBeanDB {

	private String db_dir = null;
	private DatabaseMetaData db_meta = DatabaseMetaData.getInstance();
	mkdb.util.Dbx db = null;
	public static final String ForeignDB = "ForeignDB";
	
	public ForeignBeanDB(String db_dir){
		this.db_dir = db_dir;
		this.db =  openDB();
	}
	
	private mkdb.util.Dbx openDB(){
		if(null == this.db_dir || this.db_dir.isEmpty())
			throw new RuntimeException("db_dir is invalid... ...");
		File f = new File(this.db_dir);
		f.mkdir();
		return Dbx.open(f, this.db_meta);
	}
	
	public ForeignBeanTable openTable(String tablename, int cacheHigh, int cacheLow){
		return new ForeignBeanTable(tablename, cacheHigh, cacheLow);
	}
	
	public void close(){
		this.db.close();
	}
	
	public class ForeignBeanTable{
		mkdb.util.Dbx.Table table;
		
		public ForeignBeanTable(String tablename, int cacheHigh, int cacheLow){
			this.table = db.newTable(tablename, cacheHigh, cacheLow);
		}
		
		public String getName(){
			return this.table.getName();
		}
		
		public boolean insert(ForeignBeanKey key, ForeignBeanValue value){
			OctetsStream os_key = new OctetsStream();
			OctetsStream os_value = new OctetsStream();
			key.marshal(os_key);
			value.marshal(os_value);
			return this.table.insert(os_key, os_value);
		}
		
		public boolean insert(byte[] _key, byte[] _value){
			OctetsStream key = OctetsStream.wrap(Octets.wrap(_key));
			OctetsStream value = OctetsStream.wrap(Octets.wrap(_value));
			return this.table.insert(key, value);
		}
		
		public boolean insert(OctetsStream key, OctetsStream value){
			return this.table.insert(key, value);
		}
		
		public boolean replace(ForeignBeanKey key, ForeignBeanValue value){
			OctetsStream os_key = new OctetsStream();
			OctetsStream os_value = new OctetsStream();
			key.marshal(os_key);
			value.marshal(os_value);
			return this.replace(os_key, os_value);
		}
		
		public boolean replace(OctetsStream key, OctetsStream value){
			return this.table.replace(key, value);
		}
		
		public ForeignBeanValue find(ForeignBeanKey key){
			OctetsStream os_key = new OctetsStream();
			key.marshal(os_key);
			ForeignBeanValue value = new ForeignBeanValue();
			OctetsStream os_value = this.table.find(os_key);
			if(null != os_value){
				value.unmarshal(os_value);
				return value;
			}else{
				return null;
			}
		}
		
		public ForeignBeanValue find(byte[] _key){
			OctetsStream os = OctetsStream.wrap(Octets.wrap(_key));
			ForeignBeanKey key = new ForeignBeanKey();
			if(null != os){
				key.unmarshal(os);
				return this.find(key);
			}
			else 
				return null;
		}
		
		public ForeignBeanValue find(OctetsStream key){
			return this.find(key.getBytes());
		}
		
		public boolean remove(ForeignBeanKey key){
			OctetsStream os_key = new OctetsStream();
			key.marshal(os_key);
			return this.table.remove(os_key);
		}
		
		public boolean remove(OctetsStream key){
			return this.table.remove(key);
		}
		
		/**
		 * 在表中key对应的set中加入一个foreignKey
		 * */
		public boolean insert(ForeignBeanKey key, ForeignBeanKey foreignKey){
			ForeignBeanValue value = this.find(key);
			if(null != value){
				if(value.existKey(foreignKey)){
					return false;
				}else{
						if(value.addKey(foreignKey)){
								return this.replace(key, value);
						}
				}
			}else{
				value = new ForeignBeanValue();
				if(value.addKey(foreignKey))
					return this.insert(key, value);
			}
			return false;
		}
		
		/**
		 * 在表中key对应的set中删除一个foreignKey
		 * */
		public boolean remove(ForeignBeanKey key, ForeignBeanKey foreignKey){
			ForeignBeanValue value = this.find(key);
			if(null != value){
				if(value.existKey(foreignKey)){
					if(value.removeKey(foreignKey)){
							return this.replace(key, value);
					}
				}
			}
			return false;
		}
		
		/**
		 * 在表中key对应的set中查找一个foreignKey是否存在
		 * */
		public boolean exist(ForeignBeanKey key, ForeignBeanKey foreignKey){
			ForeignBeanValue value = this.find(key);
			if(null != value){
				return value.existKey(foreignKey);
			}
			return false;
		}
		
		public void save(){
			this.table.save();
		}
		
		public void close(){
			this.table.close();
		}
		
		//only for test
		public void walk(IWalk iw){
			this.table.walk(iw);
		}
	}
	
	static class testWalker implements mkdb.Storage.IWalk{
		private int count = 0;
		private ForeignBeanDB.ForeignBeanTable table;
		
		public testWalker(ForeignBeanDB.ForeignBeanTable table){
			this.table = table;
		}
		
		public boolean onRecord(byte[] _key, byte[] _data) {
			++count;
			ForeignBeanKey key = new ForeignBeanKey();
			key.unmarshal(OctetsStream.wrap(Octets.wrap(_key)));
			ForeignBeanValue value = new ForeignBeanValue();
			value.unmarshal(OctetsStream.wrap(Octets.wrap(_data)));
			Object kk = key.getKeyType().unmarshal(key.getKey().getBytes());
			mkdb.Trace.debug("ForeignDB key=" + key + " value=" + value + "kk=" + kk);
			return true;
		}
		public void finish(){
			mkdb.Trace.debug("walk ["+this.table.getName()+"]:"+ count);
		}
	}
	
	//test
	public static void main(String[] args) throws FileNotFoundException{
		mkdb.util.Dbx.start("./bin");
		ForeignBeanDB FDB = new ForeignBeanDB("./foreignDB1k");
		ForeignBeanDB.ForeignBeanTable outPutTable = FDB.openTable("foreigntable", 128, 64);
		testWalker walker = new testWalker(outPutTable);
		PrintStream f = new PrintStream(new File("dbout.log"));
		System.setOut(f);
		outPutTable.walk(walker);
		walker.finish();
		outPutTable.close();
		FDB.close();
		
		mkdb.util.Dbx.stop();
	}
}
