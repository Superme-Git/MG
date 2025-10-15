package xmerge;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import com.locojoy.base.Marshal.OctetsStream;

import mkdb.util.DatabaseMetaData;
import mkdb.util.DatabaseMetaData.Bean;
import mkdb.util.DatabaseMetaData.Bean.Variable;
import mkdb.util.DatabaseMetaData.Type.TypeId;
import mkdb.util.Dbx;
import validator.ForeignBeanDB;
import validator.ForeignBeanDB.ForeignBeanTable;
import validator.ForeignBeanKey;
import validator.ForeignBeanValue;
import xmerge.ForeignMeta.TableDepends;
import xmerge.ForeignMeta.VarPath;

public class RepairForeignTable {
	private Dbx errorKeysDb;
	private Dbx resultDb;
	private ForeignBeanDB foreignInfoDb;
	private ForeignBeanTable foreignInfoTable;
	private DatabaseMetaData curMeta;
	private String tableName;
	private Map<Object, Object> errorKeys = new HashMap<Object, Object>();
	private TableStatistics statistic = RepairConf.getInstance().getStatistic();
	private static Map<ForeignBeanKey, ForeignBeanKey> keyChanged = new HashMap<ForeignBeanKey, ForeignBeanKey>();
	private static AtomicLong fdbCounter = new AtomicLong(0);
	private static AtomicLong workdbCounter = new AtomicLong(0);
	private AtomicLong refKeyCount = new AtomicLong(0);	
	private AtomicLong refValueCount = new AtomicLong(0);
	
	RepairForeignTable() {
		curMeta = DatabaseMetaData.getInstance();		
		errorKeysDb = Dbx.open(new File(RepairConf.getInstance().getTempErrorKeysDbHome()), curMeta);
		resultDb = Dbx.open(new File(RepairConf.getInstance().getResultDbHome()), curMeta);
		foreignInfoDb = new ForeignBeanDB(XmergeConf.getInstance().getForeignInfoDbHome());
		foreignInfoTable = foreignInfoDb.openTable(XmergeConf.getInstance().getForeignInfoTableName(), 512, 512);
	}
		
	public void insertErrorKey(Object key, Object newKey) {
		errorKeys.put(key, newKey);
	}
		
	public int getErrorKeySize() {
		return errorKeys.size();
	}
	
	/*
	 * 遍历并修复所有引用到这个表的errorKeys的数据
	 * 验证优化，根据foreignInfoTable查找key对应的管理记录，进行修改，思路如下：
	 * 1. 遍历errorKeys，根据<key, newKey>.key和表名depends.getName()，生成ForeignBeanKey
	 * 2. 查找foreignInfoTable，获取对应的ForeignBeanValue，得到Set<ForeignBeanKey>
	 * 3. 根据ForeignBeanKey里记录的表名及key，获取相应记录Record<key, value>
	 *    a. 根据表名及depends.getName()获取refVars信息，这里要区分keyRef和valueRef，可并存
	 *    b. 如存在valueRef，先修复Record.value
	 *    c. 如存在keyRef，添加<newKey, value>，删除Record，否则更新Record.value
	 * 存在keyRef时，在该步骤依然要删除errorKeys记录项   
	 */  
	public void repairRefTables(TableDepends depends) {
		// 内部确保一下
		if (getErrorKeySize() == 0) 
			return;
		this.tableName = depends.getName();
		for (Iterator<Entry<Object, Object>> itr = errorKeys.entrySet().iterator(); itr.hasNext(); ) {
			Entry<Object, Object> entry = itr.next();
			repairByForeignBeanKey(tableName, entry.getKey(), entry.getValue());
		}
		if (workdbCounter.get() > XmergeConf.MAX_WALK_COUNT) {
			resultDb.save();
			errorKeysDb.save();
			workdbCounter.set(0L);
		}
		if (refKeyCount.get() != 0 || refValueCount.get() != 0)
			mkdb.Trace.info("\t Reparing foreignRecord contains " + refKeyCount.get() + " errorRecord, " + refValueCount.get() + " errorVar.");
	}

	private void repairByForeignBeanKey(String tableName, Object keyObj, Object newKeyObj) {
		if (fdbCounter.get() > XmergeConf.MAX_WALK_COUNT) {
			foreignInfoTable.save();
			fdbCounter.set(0L);
		}
		OctetsStream key = curMeta.getTable(tableName).getKeyType().marshal(keyObj);
		ForeignBeanKey fKey = new ForeignBeanKey(tableName, key);
		ForeignBeanValue fValue = foreignInfoTable.find(fKey);
		fdbCounter.incrementAndGet();
		if (fValue == null) {
			return; 
		}
		for (Iterator<ForeignBeanKey> it = fValue.getKeySet().iterator(); it.hasNext(); ) {
			ForeignBeanKey originalKey = it.next();
			String refTableName = originalKey.getTableName();
			OctetsStream curKey = originalKey.getKey();
			Dbx.Table refTable = resultDb.openTable(refTableName);			
			// 利用从foreignDb中的key映射记录访问数据库时需要进行translateKey
			OctetsStream refValue = refTable.find(translateKey(originalKey).getKey());
			workdbCounter.incrementAndGet();
			if (refValue == null) {
				statistic.addKey(refTableName, refTable.getMetaData().getKeyType().unmarshal(curKey.getBytes()));
				throw new RuntimeException("repair fail : refValue is null, " + refTableName + statistic.getKeyList(refTableName));
			}
			Object value = curMeta.getTable(refTableName).getValueType().unmarshal(refValue);
			Repairer worker = new Repairer(tableName, originalKey);
			worker.repair(value, keyObj, newKeyObj);
		}
	}
	
	private ForeignBeanKey translateKey(ForeignBeanKey fKey) {
		return keyChanged.containsKey(fKey) ? keyChanged.get(fKey) : fKey;
	}

	class Repairer {
		private String tableName;
		private String refTableName;
		private OctetsStream curKey;
		private TableDepends depends = null;
		private Dbx.Table refTable;
		
		private Object value;
		private Object keyObj;
		private Object newKeyObj;
		private OctetsStream newKey;
		
		public Repairer(String tableName, ForeignBeanKey r) {
			this.tableName = tableName;
			this.refTableName = r.getTableName();
			this.curKey = r.getKey();		
			this.depends = RepairConf.getInstance().getForeignMeta().findRefDepends(tableName, refTableName);
			this.refTable = resultDb.openTable(refTableName);
		}
		
		public void repair(Object value, Object keyObj, Object newKeyObj) {
			this.value = value;
			this.keyObj = keyObj;
			this.newKeyObj = newKeyObj;
			this.newKey = curMeta.getTable(tableName).getKeyType().marshal(newKeyObj);
			
			if (depends != null) {
				repairValue();
			}
			// <tableName, refTableName>之间可能kerRef和valueRef同时存在，keyRef在严格判断为同一个key后进行处理，这里在判断时还是使用原始的key
			if (RepairConf.getInstance().getForeignMeta().isKeyRef(tableName, refTableName)) {
				if (curMeta.getTable(refTableName).getKeyType().unmarshal(curKey.getBytes()).equals(keyObj)) {				
					doKeyRef();
					return;
				} else {
				}
			}
			// 不为同一个key时，则认为是keyObj为valueRef到refKey.value，按照isKeyRef() == false的情况进行处理
			// 此处假定由validator来保证refTableName.refKey是在tableName.keySets()里存在的
			doValueRef();
			return;
		}
		
		private void doKeyRef() {
			if (refTable.exist(newKey)) {
				statistic.addKey(refTableName, keyObj);
				throw new RuntimeException("repair fail : allocateNewKey exist in refTable, " + refTableName + statistic.getKeyList(refTableName)); 
			}
			// 由于已经判定为keyRef，所以不需要做translateKey，因为keyRef表的key是和stub表的key一起更改的，同时要在这里记录keyRef表的key的改变情况
			if (refTable.insert(newKey, curMeta.getTable(refTableName).getValueType().marshal(value)) && refTable.remove(curKey)) {
				// 成功修复refTable中相应的key记录后，需要从errorKeysDb中删除相应记录，不一定存在
				errorKeysDb.openTable(refTableName).remove(curKey);
				keyChanged.put(new ForeignBeanKey(refTableName, curKey), new ForeignBeanKey(refTableName, newKey));
				refKeyCount.incrementAndGet();
				workdbCounter.incrementAndGet();
			} else {
				statistic.addKey(refTableName, keyObj);
				throw new RuntimeException("repair fail : " + refTableName + statistic.getKeyList(refTableName));
			}
			// keyRef需要递归，拿refTableName，keyObj构造ForeignBeanKey查询fdb
			repairByForeignBeanKey(refTableName, keyObj, newKeyObj);
		}
		
		private void doValueRef() {
			// valueRef表进行数据库访问时，需要进行translateKey，因为最初的映射关系可能已经因为keyRef的修复发生了改变
			OctetsStream trueKey = translateKey(new ForeignBeanKey(refTableName, curKey)).getKey();
			if (refTable.replace(trueKey, curMeta.getTable(refTableName).getValueType().marshal(value))) {
				refKeyCount.incrementAndGet();
				workdbCounter.incrementAndGet();
			} else {
				statistic.addKey(refTableName, refTable.getMetaData().getKeyType().unmarshal(curKey.getBytes()));
				throw new RuntimeException("repair fail : " + refTableName + statistic.getKeyList(refTableName));	
			}
		}
		
		private void repairValue() {
			DatabaseMetaData.Table tableMetaData = curMeta.getTable(depends.getName());
			DatabaseMetaData.Bean bean = curMeta.getBean(tableMetaData.getValueName());
			for (VarPath varPath : depends.getVars()) {
				if (varPath.startWithKey())
					continue;
				if (varPath.onlyWithValue()) {
					// 整个value是foreignKey，直接更新，里面肯定不会再有foreign到这个key的项了
					if (value.equals(keyObj)) {
						refValueCount.incrementAndGet();
						value = newKeyObj;
					}
					return;
				}
				// 跳过第一个即value
				varPath.setAccess(1);
				repairBean(value, bean, varPath);
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private boolean repairBean(Object beanValue, Bean bean, VarPath varPath) {
			try {
				if (varPath.hasNext() == false) {
					return false;
				}
				String varName = varPath.next();
				Variable var = bean.getVariable(varName);
				switch (var.getTypeType().getTypeId()) {
				case BOOLEAN:
				case INT:			
				case SHORT:
				case LONG:
				case FLOAT:
				case STRING:
					Object curKey = Invoke.getProperty(beanValue, var.getName());
					if (curKey.equals(keyObj)) {
						Invoke.invokeMethod(beanValue, "set" + toUpperCaseInitial(var.getName()), new Object[] {newKeyObj});
						refValueCount.incrementAndGet();
					}
					break;		
				// 出现list/vector/set时，如果newPath为空，则V本身直接为foreignKey，否则继续找
				case LIST:
				case VECTOR:
				case SET:
					// Collection<V>, V本身直接为foreignKey
					if (varPath.hasNext() == false) {
						if (var.getValueType().getTypeId() == TypeId.BINARY) {
							throw new UnsupportedOperationException("foreignKey.Type is binary, unsupported. varName = " + var.getTypeName());
						}
						// 由xgen来保证foreignKey不能为xbean，允许为cbean
						Collection cons = (Collection)Invoke.getProperty(beanValue, var.getName());
						Collection keyToAdd = new ArrayList<Object>();
						Iterator itr = cons.iterator();
						while (itr.hasNext()) {
							Object obj = itr.next();
							if (obj.equals(keyObj)) {
								keyToAdd.add(newKeyObj);
								itr.remove();							
							}
						}
						for (Object obj : keyToAdd) {
							cons.add(obj);
							refValueCount.incrementAndGet();
						}
					} else {						
						if (var.getValueType().getTypeId() == TypeId.BEAN) {
							Collection cons = (Collection)Invoke.getProperty(beanValue, var.getName());
							int footprint = varPath.getAccess();
							for (Object obj : cons) {
								varPath.setAccess(footprint);
								repairBean(obj, curMeta.getBean(var.getValueTypeName()), varPath);
							}
						}				
					}
					break;
				case MAP:
				case TREEMAP:
					// Map<K, V>, 需处理K, V本身是foreignKey以及K, V为bean，其子项为foreignKey的情况，根据 后缀来判断
					String suffix = varPath.next();
					// 由xgen来保证foreignKey不能为xbean，允许为cbean
					if (suffix.equals("<keyforeign>")) {
						// K本身为foreignKey
						if (var.getKeyType().getTypeId() == TypeId.BINARY) {
							throw new UnsupportedOperationException("foreignKey.Type is binary, unsupported. varName = " + var.getTypeName());
						}
						Map map = (Map)Invoke.getProperty(beanValue, var.getName());
						Map toAdd = new HashMap<Object, Object>();
						Iterator itr = map.entrySet().iterator();
				        while (itr.hasNext()) {
				        	Map.Entry entry = (Map.Entry)itr.next();
				        	Object key = entry.getKey();
				        	Object v = entry.getValue();
				        	if (key.equals(keyObj)) {
								toAdd.put(newKeyObj, v);
								itr.remove();
							}
				        }
				        itr = toAdd.entrySet().iterator();
				        while (itr.hasNext()) {
				        	Map.Entry entry = (Map.Entry)itr.next();
				        	map.put(entry.getKey(), entry.getValue());
				        	refValueCount.incrementAndGet();
				        }
					} else if (suffix.equals("<valueforeign>")) {
						// V本身为foreignKey
						if (var.getValueType().getTypeId() == TypeId.BINARY) {
							throw new UnsupportedOperationException("foreignKey.Type is binary, unsupported. varName = " + var.getTypeName());
						}				
						Map map = (Map)Invoke.getProperty(beanValue, var.getName());
						// 遍历更改所有的foreignKey
						Iterator itr = map.entrySet().iterator();
				        while (itr.hasNext()) {
				        	Map.Entry entry = (Map.Entry)itr.next();
				        	Object k = entry.getKey();
				        	Object v = entry.getValue();
				        	if (v.equals(keyObj)) {
								map.put(k, newKeyObj);
								refValueCount.incrementAndGet();
							}
				        }
					} else if (suffix.equals("<key>")) {
						// K为BEAN， 则应该为cbean，其子项为foreignKey，可能么？貌似这种情况不应该出现？cbean不允许单独修改？
						if (var.getKeyType().getTypeId() == TypeId.BEAN) {
							// 截掉suffix					
							Map map = (Map)Invoke.getProperty(beanValue, var.getName());
							mkdb.Trace.error("cbean shouldn't exist in Meta: " + curMeta.getBean(var.getKeyTypeName())== null);
							int footprint = varPath.getAccess();
							for (Object obj : map.keySet()) {
								varPath.setAccess(footprint);
								repairBean(obj, curMeta.getBean(var.getKeyTypeName()), varPath);
							}
						}
					} else if (suffix.equals("<value>")) {
						// V为BEAN，其子项为foreignKey						
						if (var.getValueType().getTypeId() == TypeId.BEAN) {
							Bean vbean = curMeta.getBean(var.getValueTypeName());
							Map map = (Map)Invoke.getProperty(beanValue, var.getName());
							int footprint = varPath.getAccess();
							for (Object obj : map.values()) {
								varPath.setAccess(footprint);
								repairBean(obj, vbean, varPath);
							}
						}
					}
					break;
				case BEAN:
					// 可能为cbean或者xbean，为xbean时由xgen来保证其自身不能为foreignKey，但其子项可以为foreignKey
					// 为xbean时，newPath不会为空，要继续找，如果cbean为foreignKey时，则其子项不可能也为foreignKey
					// 这里的逻辑还需要再仔细考虑一下
					if (varPath.hasNext()) {						
						Object obj = Invoke.getProperty(beanValue, var.getName());
						repairBean(obj, curMeta.getBean(var.getTypeName()), varPath);
					} else {
						// cbean
						Object key = Invoke.getProperty(beanValue, var.getName());
						if (key.equals(keyObj)) {
							Invoke.invokeMethod(beanValue, "set" + toUpperCaseInitial(var.getName()), new Object[] {newKeyObj});
							refValueCount.incrementAndGet();
						}
					}					
					break;
				case BINARY:
				case ANY :
				default:
					throw new IllegalArgumentException("unknown type = " + var.getTypeType());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return true;
		}
		
		private String toUpperCaseInitial(String str) {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
	}
}
