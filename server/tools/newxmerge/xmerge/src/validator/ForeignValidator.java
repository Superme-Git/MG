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
 * ��֤һ��DB���������ݱ�֮��������ϵ�Ƿ��걸
 * TableA(key) <-- TableB(key/value)
 * foreign��ϵ��ָ��TableB�е�key����value(Ҳ������value�е�ĳһ��)foreign��TableA�е�key
 * �걸�ĳ�Ҫ������TableB��foreign��TableA����(key/value)�ļ�����TableA��key���ϵ��Ӽ�
 * @output foreign��ϵ����ȷ��key
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
	 * ��ÿһ�ű���walk��Ȼ���¼foreignBean��Ϣ�Լ�foreign��ϵ�������־
	 * */
	public void validateTable(String tablename){
		validateWalk walker = new validateWalk(tablename);
		Dbx.Table walkTable = DB.openTable(tablename);
		walkTable.walk(walker);
		walker.finish();
		return;
	}
	
	/**
	 * ��walk���޸��ñ�����foreign reference�����
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
			 * ͨ��curVarName�ж�bean��ǰ�ı�����
			 * ͨ��nextVarPath�ж�֮���Ƿ��б���
			 **/
			String curVarName = null;
			String nextVarPath = "";//isEmpty()
			if (varPath.indexOf('.') != -1){
				curVarName = varPath.substring(0, varPath.indexOf('.'));
				nextVarPath = varPath.substring(varPath.indexOf('.')+1);
			}
			else 
				curVarName = varPath;
			//bean�����ͺ�bean��variable����
			
			DatabaseMetaData.Bean beanType = DatabaseMetaData.getInstance().getBean(beanName);
			Variable var = beanType.getVariable(curVarName);
			
			//bean value�п��ܵĸ���Type
			switch (var.getTypeType().getTypeId()) {
				case BOOLEAN:
				case INT:			
				case SHORT:
				case LONG:
				case FLOAT:
				case STRING:
					try{
						//����ط���ͨ��reflect�õ�value�е�field��object����Ȼ����Ҫ���л�ΪoctetsStream������
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
					if( -1 == nextVarPath.indexOf('.'))//��var�������
						prefix = nextVarPath;
					else
						prefix = nextVarPath.substring(0, nextVarPath.indexOf('.'));
					/**
					 * ��4�������
					 * <li>map��key����cbean��foreign������table��key��ֱ����֤
					 * <li>map��value����(c|x)bean��foreign������table��key��ֱ����֤
					 * <li>map��key��cbean����cbean�е�ĳһ��field foreign������table��key���ݹ�
					 * <li>map��value��(c|x)bean����bean�е�ĳһ��field foreign������table��key���ݹ�
					 * */
					if (prefix.equals("<keyforeign>")){//��һ�����
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
					} else if (prefix.equals("<valueforeign>")) {//�ڶ������
						if (var.getValueType().getTypeId() == TypeId.BINARY) {
							System.err.println("foreignKey.Type is binary, need callback.");
						}				
						Map map;
						try {
							map = (Map)Invoke.getProperty(son_value, var.getName());
							// �����������е�foreignKey
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
					} else if (prefix.equals("<key>")) {//���������
						// KΪBEAN�� ��Ӧ��Ϊcbean��������ΪforeignKey������ô��
						if (var.getKeyType().getTypeId() == TypeId.BEAN) {
							// �ص�prefix����key bean�����field foreign������϶�����
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
					} else if (prefix.equals("<value>")) {//���������
						// VΪBEAN��������ΪforeignKey 
						if (var.getValueType().getTypeId() == TypeId.BEAN) {
							// �ص�prefix
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
					 * xbean������Ϊkey������ֱ��foreign��key�����ֻ�е�nextVarPathΪ��ʱ���п���Ϊcbean
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
