package xmerge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.locojoy.base.Marshal.OctetsStream;

import mkdb.Const;
import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

public class Allocators {
	private Map<String, IAllocator> stubs = new HashMap<String, IAllocator>();
	private DatabaseMetaData curMeta;
	private Dbx.Table sysTable;
	private Map<Integer, mkdb.util.AutoKeys> autos = new HashMap<Integer, mkdb.util.AutoKeys>();

	public Allocators() {
		Iterator<Map.Entry<String, String>> itr = XmergeConf.getInstance().getAllocators().entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			String tableName = entry.getKey();
			String className = entry.getValue();			
			try {
				Class<?> allocatorClass;
				allocatorClass = Class.forName(className);
				Object instance = allocatorClass.newInstance();
				if (!(instance instanceof IAllocator))
					throw new RuntimeException("invalid IAllocator");
				stubs.put(tableName, (IAllocator)instance);		
			} catch (Exception e) {
				throw new RuntimeException(e);
			}				
		}
	}
	
	public void initialize(Dbx db) {
		curMeta = DatabaseMetaData.getInstance();
		sysTable = db.newTable("_sys_");
	}
	
	public void addAllocator(String tableName, IAllocator allocator) {
		stubs.put(tableName, allocator);
	}
	
	public void destory() {
		Iterator<Map.Entry<Integer, mkdb.util.AutoKeys>> itr = autos.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<Integer, mkdb.util.AutoKeys> entry = itr.next();
			Integer localId = entry.getKey();
			mkdb.util.AutoKeys autoKeys = entry.getValue();
			OctetsStream keyOfAutoKeys = new OctetsStream().marshal("mkdb.util.AutoKeys." + String.valueOf(localId), mkdb.Const.IO_CHARSET);
			OctetsStream newValue = autoKeys.encodeValue(0);
			if (false == sysTable.replace(keyOfAutoKeys, newValue))
				throw new RuntimeException("save _sys_ autoKeys fail.");
		}		
	}
	
	public OctetsStream allocateKey(String tableName, Object oldKey) {
		DatabaseMetaData.Table tableMeta = curMeta.getTable(tableName);
		switch (tableMeta.getKeyType().getTypeId()) {
		case LONG:
		{
			Long newKey;
			if (tableMeta.isAutoKey()) {
				newKey = (Long) allocateAutoLongKey(tableName, (Long)oldKey);
			} else {
				newKey = (Long) allocateByOut(tableName, oldKey);
			}
			return new OctetsStream().marshal(newKey);
		}
		case STRING:
		{
			String newKey = (String) allocateByOut(tableName, oldKey);
			return new OctetsStream().marshal(newKey, mkdb.Const.IO_CHARSET);
		}
		case INT:
		{
			Integer newKey = (Integer) allocateByOut(tableName, oldKey);
			return new OctetsStream().marshal(newKey);
		}		
		// for test
		case FLOAT:
		case SHORT:
		default:
			throw new IllegalArgumentException("unsupport IAllocator type for table " + tableName +
					"[keyType=" + tableMeta.getKeyType() + "]");	
		}
	}

	private Object allocateByOut(String tableName, Object oldKey) {
		IAllocator worker = stubs.get(tableName);
		if (worker == null) {
			throw new RuntimeException("No registe IAllocator for table " + tableName);
		}
		return worker.allocateKey(tableName, oldKey);
	}
	
	public static final int STEP = 4096;      // 自增长步长
	private int getLocalId(long curKey) { return (int)(curKey % STEP); }
	private Long nextKey(Long next) { return next + STEP; }
	
	private Object allocateAutoLongKey(String tableName, Long oldKey) {
		try {
			int localId = getLocalId(oldKey);
			mkdb.util.AutoKeys autoKeys = autos.get(localId);
			if (autoKeys == null) {
				OctetsStream keyOfAutoKeys = new OctetsStream().marshal("mkdb.util.AutoKeys." + String.valueOf(localId), mkdb.Const.IO_CHARSET);
				OctetsStream valueOfAutoKeys = sysTable.find(keyOfAutoKeys);
				OctetsStream oldKeyOfAutoKeys = new OctetsStream().marshal("mkdb.util.AutoKeys", Const.IO_CHARSET);
				OctetsStream oldValueOfAutoKeys = sysTable.find(oldKeyOfAutoKeys);
				if (null != oldValueOfAutoKeys) {
					if (null != valueOfAutoKeys)
						throw new RuntimeException("autoKeys corrupt: new? old?");
					sysTable.replaceUnsafe(keyOfAutoKeys, oldValueOfAutoKeys);
					sysTable.removeUnsafe(oldKeyOfAutoKeys);
					autoKeys = new mkdb.util.AutoKeys(oldValueOfAutoKeys, localId, true);
				} else {
					autoKeys = new mkdb.util.AutoKeys(valueOfAutoKeys, localId, false);
				}
				autos.put(localId, autoKeys);
			}			
			
			mkdb.Trace.info(autoKeys.getAutoKeyLong(tableName));			
			Long next = (Long) Invoke.getProperty(autoKeys.getAutoKeyLong(tableName), "next");
			mkdb.Trace.debug("cur : " + next);
			Long tmp = nextKey(next);
			Invoke.setProperty(autoKeys.getAutoKeyLong(tableName), "next", tmp);
			mkdb.Trace.debug("after:" + Invoke.getProperty(autoKeys.getAutoKeyLong(tableName), "next"));
			return tmp;
		} catch (Exception e) {
			throw new RuntimeException("allocateAutoLongKey set autoKeys fail.");
		}		
	}
}