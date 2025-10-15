package validator;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;
import mkdb.util.DatabaseMetaData.Bean.Variable;
import mkdb.util.DatabaseMetaData.Type.TypeId;
import xmerge.ForeignMeta;
import xmerge.Invoke;
import xmerge.TableStatistics;
import xmerge.XmergeConf;
import xmerge.ForeignMeta.TableDepends;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * 验证一个DB中所有数据表之间的外键关系是否完备
 * TableA(key) <-- TableB(key/value)
 * foreign关系是指：TableB中的key或者value(也可以是value中的某一项)foreign到TableA中的key
 * 完备的充要条件：TableB中foreign到TableA的项(key/value)的集合是TableA中key集合的子集
 * @output foreign关系不正确的key
 * */
public class ForeignValidator {
	private String DBDir;
	private Dbx DB;
	Map<String, TableDepends> roots;
	private ForeignMeta refMeta;
	TableStatistics statistic;
	
	private String outputDir  = null;
	private ForeignBeanDB FDB = null;
	private ForeignBeanDB.ForeignBeanTable outPutTable = null;
	boolean isOutput = false;
	
	public ForeignValidator() {
		refMeta = new ForeignMeta();
		refMeta.initDepRoots();
		roots = refMeta.getDepRoots();
		this.statistic = new TableStatistics();
    }
	
	public void setDB(String Dir){
		if(null == Dir || Dir.isEmpty())
			throw new Error("[ForeignValidator] setDB error... ...");
		else{
			this.DBDir = Dir;
			this.DB = Dbx.open(new File(Dir), DatabaseMetaData.getInstance());
		}
	}
	
	public String getDBDir(){
		return this.DBDir;
	}
	
	public void setOutputDir(String Dir){
		if(null == Dir || Dir.isEmpty())
			throw new Error("[ForeignValidator] setOutputDir error... ...");
		else{
			this.outputDir = Dir;
			this.FDB = new ForeignBeanDB(this.outputDir);
			this.outPutTable = this.FDB.openTable(XmergeConf.FOREIGN_TABLE_NAME, 128, 64);
			this.isOutput = true;
		}
	}
	
	public String getFDBDir(){
		return this.outputDir;
	}
	
	public void doValidate(){
		try{
			for(DatabaseMetaData.Table table : DB.getMetaData().getTables()){
				if(!table.isPersistence())
					continue;
				validateTable(table.getName());
			}
		}finally{
			this.DB.close();
			if(this.isOutput){
				this.outPutTable.close();
				this.FDB.close();
			}
		}
		return;//all tables validate ok
	}
	
	/**
	 * 在每一张表上walk，然后记录foreignBean信息以及foreign关系有误的日志
	 * */
	public void validateTable(String tablename){
		validateWalk walker = new validateWalk(tablename);
		Dbx.Table walkTable = DB.openTable(tablename);
		walkTable.walk(walker);
		walker.finish();
		return;
	}
	
	/**
	 * 该walk中修复该表所有foreign reference的情况
	 * */	
	public class validateWalk implements mkdb.Storage.IWalk{
		private String walkTable;
		private int count = 0;
		private DatabaseMetaData.Table tableMeta;
		private DatabaseMetaData.Type keyType;
		private DatabaseMetaData.Type valueType;
		
		private Map<String, Dbx.Table> refTableMap = new HashMap<String, Dbx.Table>();
		private Set<OctetsStream> keySet = new HashSet<OctetsStream>();
		private Set<OctetsStream> errKeySet = new HashSet<OctetsStream>();
		
		public validateWalk(String walkTable){
			this.walkTable = walkTable;
			this.tableMeta = DatabaseMetaData.getInstance().getTable(walkTable); 
			this.keyType = this.tableMeta.getKeyType();
			this.valueType = this.tableMeta.getValueType();
			initailize();
		}
		
		private void initailize(){
			Map<String, String> refMap = refMeta.getForeigns(walkTable);
			if(null != refMap){
				Iterator<Map.Entry<String, String>> it = refMap.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, String> entry = it.next();
					String refvar = entry.getKey();
					String reftable = entry.getValue();
					Dbx.Table table = DB.openTable(reftable);
					this.refTableMap.put(refvar, table);
				}
			}
		}
		
		private void saveAllTables(){
			for(Dbx.Table table : this.refTableMap.values()){
				table.save();
			}
			return;
		}
		
		private void saveErrorRefKey(){
			if(!this.errKeySet.isEmpty()){
				xmerge.Util.Log log = new xmerge.Util.Log(new File("."), "validate.log", false); 
				StringBuffer sb = new StringBuffer();
				for(OctetsStream _key : this.errKeySet){
					Object key = this.keyType.unmarshal(_key.getBytes());
					sb.append("Table[").append(this.walkTable).append("] key:").append(key).append("\t");
				}
				log.println(sb.toString());
				log.close();
			}
			this.errKeySet.clear();
		}
		
		private void saveKeyRef(OctetsStream pkey, OctetsStream skey, String parentTableName){
			if(ForeignValidator.this.isOutput){
				ForeignBeanKey parentKey = new ForeignBeanKey(parentTableName, pkey);
				ForeignBeanKey sonKey = new ForeignBeanKey(this.walkTable, skey);
				ForeignValidator.this.outPutTable.insert(parentKey, sonKey);
			}
			return;
		}
		
		public boolean onRecord(byte[] key, byte[] data) {
			++count;
			if(count > XmergeConf.MAX_WALK_COUNT){
				saveAllTables();
				if (ForeignValidator.this.isOutput){
					ForeignValidator.this.outPutTable.save();	
				}
				count = 0;
			}
			OctetsStream son_key = OctetsStream.wrap(Octets.wrap(key));
			OctetsStream son_value = OctetsStream.wrap(Octets.wrap(data));
			Object value = this.valueType.unmarshal(data);
			
			Iterator<Map.Entry<String, Dbx.Table>> it = this.refTableMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, Dbx.Table> entry = it.next();
				String var = entry.getKey();
				Dbx.Table table = entry.getValue();
				if(var.equals("key")){// key ref
					if(table.exist(son_key)){
						saveKeyRef(son_key, son_key, table.getName());
					}else{
						this.errKeySet.add(son_key);
					}
				}else if(var.equals("value")){// value ref
					if(table.exist(son_value)){
						saveKeyRef(son_value, son_key, table.getName());
					}else{
						this.errKeySet.add(son_key);
					}
				}else{//value.* ref
					validateBeanValue(value, son_key, var.substring(var.indexOf('.') + 1), this.tableMeta.getValueName(), table);
					for(OctetsStream rkey: keySet){
						saveKeyRef(rkey, son_key, table.getName());
					}
					keySet.clear();
				}
			}
			return true;
		}

		@SuppressWarnings("rawtypes")
		private void validateBeanValue(Object son_value, OctetsStream son_key, String varPath, String beanName, Dbx.Table table){
			/**
			 * 通过curVarName判断bean当前的变量名
			 * 通过nextVarPath判断之后是否还有变量
			 **/
			String curVarName = null;
			String nextVarPath = "";//isEmpty()
			if (varPath.indexOf('.') != -1){
				curVarName = varPath.substring(0, varPath.indexOf('.'));
				nextVarPath = varPath.substring(varPath.indexOf('.')+1);
			}
			else 
				curVarName = varPath;
			//bean的类型和bean的variable对象
			
			DatabaseMetaData.Bean beanType = DatabaseMetaData.getInstance().getBean(beanName);
			Variable var = beanType.getVariable(curVarName);
			
			//bean value中可能的各种Type
			switch (var.getTypeType().getTypeId()) {
				case BOOLEAN:
				case INT:			
				case SHORT:
				case LONG:
				case FLOAT:
				case STRING:
					try{
						//这个地方先通过reflect得到value中的field（object），然后又要序列化为octetsStream？？？
						Object foreignKey = Invoke.getProperty(son_value, var.getName());
						OctetsStream key = var.getTypeType().marshal(foreignKey);
						if(table.exist(key))
							keySet.add(key);
						else
							this.errKeySet.add(son_key);
					}catch(Exception e){
						e.printStackTrace();
					}
					break;
				case LIST:
				case VECTOR:
				case SET:
					if (nextVarPath.isEmpty()) {
						if (var.getValueType().getTypeId() == TypeId.BINARY) {
							System.err.println("foreignKey.Type is binary, need callback.");
						}
						try {
							Collection cons = (Collection)Invoke.getProperty(son_value, var.getName());
							for (Object foreignKey : cons) {
								OctetsStream key = var.getValueType().marshal(foreignKey);
								if(table.exist(key))
									keySet.add(key);
								else
									this.errKeySet.add(son_key);
							}	
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {//bean
						if (var.getValueType().getTypeId() == TypeId.BEAN) {
							try {
									Collection cons = (Collection)Invoke.getProperty(son_value, var.getName());
									for (Object obj : cons) {
										validateBeanValue(obj, son_key, nextVarPath, var.getValueTypeName(), table);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
						}				
					}
					break;
				case MAP:
				case TREEMAP:
					String prefix = null;
					if( -1 == nextVarPath.indexOf('.'))//到var的最后了
						prefix = nextVarPath;
					else
						prefix = nextVarPath.substring(0, nextVarPath.indexOf('.'));
					/**
					 * 有4种情况：
					 * <li>map的key不是cbean，foreign到其他table的key，直接验证
					 * <li>map的value不是(c|x)bean，foreign到其他table的key，直接验证
					 * <li>map的key是cbean，该cbean中的某一个field foreign到其他table的key，递归
					 * <li>map的value是(c|x)bean，该bean中的某一个field foreign到其他table的key，递归
					 * */
					if (prefix.equals("<keyforeign>")){//第一种情况
						if (var.getKeyType().getTypeId() == TypeId.BINARY) {
							System.err.println("foreignKey.Type is binary, need callback.");
						}
						try {
							Map map = (Map)Invoke.getProperty(son_value, var.getName());
							java.util.Iterator it = map.entrySet().iterator();
							while(it.hasNext()){
								java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
								Object foreignKey = entry.getKey();
								OctetsStream key = var.getKeyType().marshal(foreignKey);
								if(table.exist(key))
									keySet.add(key);
								else
									this.errKeySet.add(son_key);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (prefix.equals("<valueforeign>")) {//第二种情况
						if (var.getValueType().getTypeId() == TypeId.BINARY) {
							System.err.println("foreignKey.Type is binary, need callback.");
						}				
						Map map;
						try {
							map = (Map)Invoke.getProperty(son_value, var.getName());
							// 遍历更改所有的foreignKey
							java.util.Iterator it = map.entrySet().iterator();
							while(it.hasNext()){
								java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
								Object foreignKey = entry.getValue();
								OctetsStream key = var.getValueType().marshal(foreignKey);
								if(table.exist(key))
									keySet.add(key);
								else
									this.errKeySet.add(son_key);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (prefix.equals("<key>")) {//第三种情况
						// K为BEAN， 则应该为cbean，其子项为foreignKey，可能么？
						if (var.getKeyType().getTypeId() == TypeId.BEAN) {
							// 截掉prefix，是key bean里面的field foreign，后面肯定还有
							String nextPath = nextVarPath.substring(nextVarPath.indexOf('.') + 1);
							Map map;
							try {
									map = (Map)Invoke.getProperty(son_value, var.getName());
									for (Object obj : map.keySet()) {
										validateBeanValue(obj, son_key, nextPath, var.getKeyTypeName(), table);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
						}else{
							
						}
					} else if (prefix.equals("<value>")) {//第四种情况
						// V为BEAN，其子项为foreignKey 
						if (var.getValueType().getTypeId() == TypeId.BEAN) {
							// 截掉prefix
							String nextPath = nextVarPath.substring(nextVarPath.indexOf('.') + 1);
							Map map;
							try {
									map = (Map)Invoke.getProperty(son_value, var.getName());
									for (Object obj : map.values()) {
										validateBeanValue(obj, son_key, nextPath, var.getValueTypeName(), table);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
					}else{
						System.err.println("foreignMeta error when prefix is " + prefix);
					}
					break;
				case BEAN:
					/**
					 * xbean不可能为key，不能直接foreign到key，因此只有当nextVarPath为空时才有可能为cbean
					 * */
					if (!nextVarPath.isEmpty()) {//xbean
						try {
								Object obj = Invoke.getProperty(son_value, var.getName());
								validateBeanValue(obj, son_key, nextVarPath, var.getTypeName(), table);
							} catch (Exception e) {
								e.printStackTrace();
							}
					}else{//cbean
						try {
								Object foreignKey = Invoke.getProperty(son_value, var.getName());
								OctetsStream key = var.getValueType().marshal(foreignKey);
								if(table.exist(key))
									keySet.add(key);
								else
									this.errKeySet.add(son_key);
							} catch (Exception e) {
								e.printStackTrace();
							}
					}	
					break;
				case BINARY:
					System.err.println("foreignKey.Type is binary, need callback.");
					break;
				default:
					throw new IllegalArgumentException("unknown type = " + var.getTypeType());
			}
		}
		public void finish(){
			saveErrorRefKey();
			mkdb.Trace.debug("Validate finish at table ["+ this.walkTable +"]... ...");
		}
	}
}
