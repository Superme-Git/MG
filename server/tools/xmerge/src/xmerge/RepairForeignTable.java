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
	 * �������޸��������õ�������errorKeys������
	 * ��֤�Ż�������foreignInfoTable����key��Ӧ�Ĺ����¼�������޸ģ�˼·���£�
	 * 1. ����errorKeys������<key, newKey>.key�ͱ���depends.getName()������ForeignBeanKey
	 * 2. ����foreignInfoTable����ȡ��Ӧ��ForeignBeanValue���õ�Set<ForeignBeanKey>
	 * 3. ����ForeignBeanKey���¼�ı�����key����ȡ��Ӧ��¼Record<key, value>
	 *    a. ���ݱ�����depends.getName()��ȡrefVars��Ϣ������Ҫ����keyRef��valueRef���ɲ���
	 *    b. �����valueRef�����޸�Record.value
	 *    c. �����keyRef�����<newKey, value>��ɾ��Record���������Record.value
	 * ����keyRefʱ���ڸò�����ȻҪɾ��errorKeys��¼��   
	 */  
	public void repairRefTables(TableDepends depends) {
		// �ڲ�ȷ��һ��
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
			// ���ô�foreignDb�е�keyӳ���¼�������ݿ�ʱ��Ҫ����translateKey
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
			// <tableName, refTableName>֮�����kerRef��valueRefͬʱ���ڣ�keyRef���ϸ��ж�Ϊͬһ��key����д����������ж�ʱ����ʹ��ԭʼ��key
			if (RepairConf.getInstance().getForeignMeta().isKeyRef(tableName, refTableName)) {
				if (curMeta.getTable(refTableName).getKeyType().unmarshal(curKey.getBytes()).equals(keyObj)) {				
					doKeyRef();
					return;
				} else {
				}
			}
			// ��Ϊͬһ��keyʱ������Ϊ��keyObjΪvalueRef��refKey.value������isKeyRef() == false��������д���
			// �˴��ٶ���validator����֤refTableName.refKey����tableName.keySets()����ڵ�
			doValueRef();
			return;
		}
		
		private void doKeyRef() {
			if (refTable.exist(newKey)) {
				statistic.addKey(refTableName, keyObj);
				throw new RuntimeException("repair fail : allocateNewKey exist in refTable, " + refTableName + statistic.getKeyList(refTableName)); 
			}
			// �����Ѿ��ж�ΪkeyRef�����Բ���Ҫ��translateKey����ΪkeyRef���key�Ǻ�stub���keyһ����ĵģ�ͬʱҪ�������¼keyRef���key�ĸı����
			if (refTable.insert(newKey, curMeta.getTable(refTableName).getValueType().marshal(value)) && refTable.remove(curKey)) {
				// �ɹ��޸�refTable����Ӧ��key��¼����Ҫ��errorKeysDb��ɾ����Ӧ��¼����һ������
				errorKeysDb.openTable(refTableName).remove(curKey);
				keyChanged.put(new ForeignBeanKey(refTableName, curKey), new ForeignBeanKey(refTableName, newKey));
				refKeyCount.incrementAndGet();
				workdbCounter.incrementAndGet();
			} else {
				statistic.addKey(refTableName, keyObj);
				throw new RuntimeException("repair fail : " + refTableName + statistic.getKeyList(refTableName));
			}
			// keyRef��Ҫ�ݹ飬��refTableName��keyObj����ForeignBeanKey��ѯfdb
			repairByForeignBeanKey(refTableName, keyObj, newKeyObj);
		}
		
		private void doValueRef() {
			// valueRef��������ݿ����ʱ����Ҫ����translateKey����Ϊ�����ӳ���ϵ�����Ѿ���ΪkeyRef���޸������˸ı�
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
					// ����value��foreignKey��ֱ�Ӹ��£�����϶���������foreign�����key������
					if (value.equals(keyObj)) {
						refValueCount.incrementAndGet();
						value = newKeyObj;
					}
					return;
				}
				// ������һ����value
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
				// ����list/vector/setʱ�����newPathΪ�գ���V����ֱ��ΪforeignKey�����������
				case LIST:
				case VECTOR:
				case SET:
					// Collection<V>, V����ֱ��ΪforeignKey
					if (varPath.hasNext() == false) {
						if (var.getValueType().getTypeId() == TypeId.BINARY) {
							throw new UnsupportedOperationException("foreignKey.Type is binary, unsupported. varName = " + var.getTypeName());
						}
						// ��xgen����֤foreignKey����Ϊxbean������Ϊcbean
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
					// Map<K, V>, �账��K, V������foreignKey�Լ�K, VΪbean��������ΪforeignKey����������� ��׺���ж�
					String suffix = varPath.next();
					// ��xgen����֤foreignKey����Ϊxbean������Ϊcbean
					if (suffix.equals("<keyforeign>")) {
						// K����ΪforeignKey
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
						// V����ΪforeignKey
						if (var.getValueType().getTypeId() == TypeId.BINARY) {
							throw new UnsupportedOperationException("foreignKey.Type is binary, unsupported. varName = " + var.getTypeName());
						}				
						Map map = (Map)Invoke.getProperty(beanValue, var.getName());
						// �����������е�foreignKey
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
						// KΪBEAN�� ��Ӧ��Ϊcbean��������ΪforeignKey������ô��ò�����������Ӧ�ó��֣�cbean���������޸ģ�
						if (var.getKeyType().getTypeId() == TypeId.BEAN) {
							// �ص�suffix					
							Map map = (Map)Invoke.getProperty(beanValue, var.getName());
							mkdb.Trace.error("cbean shouldn't exist in Meta: " + curMeta.getBean(var.getKeyTypeName())== null);
							int footprint = varPath.getAccess();
							for (Object obj : map.keySet()) {
								varPath.setAccess(footprint);
								repairBean(obj, curMeta.getBean(var.getKeyTypeName()), varPath);
							}
						}
					} else if (suffix.equals("<value>")) {
						// VΪBEAN��������ΪforeignKey						
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
					// ����Ϊcbean����xbean��Ϊxbeanʱ��xgen����֤��������ΪforeignKey�������������ΪforeignKey
					// Ϊxbeanʱ��newPath����Ϊ�գ�Ҫ�����ң����cbeanΪforeignKeyʱ�������������ҲΪforeignKey
					// ������߼�����Ҫ����ϸ����һ��
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
