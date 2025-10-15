package fire.pb.cross;

import java.lang.reflect.Method;

import fire.pb.cross.TableData;
import fire.pb.util.OctetsUtil;
import mkdb.Bean;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * 用来在原服和跨服传输角色数据的类
 * @author dc
 * 
 */
public class TransferDataManager {
	
	static class SingletonHolder {
		static TransferDataManager singleton = new TransferDataManager();
	}
	
	public static TransferDataManager getInstance() {
		return SingletonHolder.singleton;
	}
	
	private TransferDataManager() {}
	
	private static final String[] STANDARD_TYPE_ARRAY = {"short", "int", "long", "float", "boolean", "string", "binary", "set", "list", "vector", "map", "treemap"};
	
	/**
	 * 这个方法在原服调用
	 * 
	 * @param tableName
	 * @param beanName
	 * @param tableKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Octets getTableData(String tableName, String beanName, Object tableKey) {
		final mkdb.TTable<Object, Object> table = (mkdb.TTable<Object, Object>)
				mkdb.Mkdb.getInstance().getTables().getTable(tableName);
		if (table == null)
			return null;
		Object data = table.get(tableKey);
		if (data == null) return null;
		if (!isStandardType(beanName)) {
			OctetsStream os = new OctetsStream();
			mkdb.Bean bean = (mkdb.Bean) data;
			return os.marshal(bean);
		} else {
			return getStandDardOctets(beanName,data);
		}
	}
	
	private Octets getStandDardOctets(String beanName, Object data) {
		if (data == null) return null;
		OctetsStream os = new OctetsStream();
		if (beanName.equals("boolean")) {
			return os.marshal((Boolean)data);
		} else if (beanName.equals("short")) {
			return os.marshal((Short)data);
		} else if (beanName.equals("int")) {
			return os.marshal((Integer)data);
		} else if (beanName.equals("long")) {
			return os.marshal((Long)data);
		} else if (beanName.equals("float")) {
			return os.marshal((Float)data);
		} else if (beanName.equals("string")) {
			return os.marshal((String)data);
		} else 
			throw new RuntimeException("不支持的转换类型。type=" + beanName);
		
	}
	
	/**
	 * 根据表名，键值产生一个协议对象，发至跨服服务器
	 * 
	 * @param tableName
	 * @param tableKey
	 * @param tableValue
	 * @return
	 */
	public TableData genTableData(String tableName, long tableKey, mkdb.Bean tableValue) {
		TableData tableData = new TableData();
		tableData.tablename = tableName;
		tableData.keydata = OctetsUtil.toOctets(tableKey);
		tableData.valuedata = new OctetsStream().marshal(tableValue);
		return tableData;
	}

	/**
	 * 这个方法在跨服服务器调用
	 * 
	 * @param tableName
	 * @param beanName
	 * @param tableKey
	 * @param octets
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void setTableData(String tableName, String keyType, String valueType, Octets keyData,Octets valueData) throws Exception {
//		if (!CrossManager.getInstance().isInCrossServer()) {
//			CrossManager.logger.error("非跨服服务器调用TransferDataManager.setTableData方法");
//			return;
//		}
		final mkdb.TTable<Object, Object> table = (mkdb.TTable<Object, Object>)
			mkdb.Mkdb.getInstance().getTables().getTable(tableName);
		if (table == null)
			return;
		Object tableKey = getOriginalDataByType(keyType, keyData);
		table.remove(tableKey); //把以前的记录删掉
		Object tableValue = getOriginalDataByType(valueType, valueData);
		table.insertUncheckedAutoKey(tableKey, tableValue);
		//table.insert(tableKey, tableValue);//该接口会检查autokey规则，跨服无法使用
	}
	
	/**
	 * 根据类型和数据Octets还原回原始数据对象
	 * 
	 * @param type
	 * @param octets
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Object getOriginalDataByType(String type, Octets octets) throws Exception {
		OctetsStream os = new OctetsStream(octets); 
		if (!isStandardType(type)) {
			Class<? extends mkdb.Bean> cls = (Class<? extends Bean>) Class.forName("xbean.Pod");
			Method method = cls.getDeclaredMethod("new" + type);
			mkdb.Bean bean = (mkdb.Bean) method.invoke(cls);
			bean.unmarshal(os);
			return bean;
		} else {
			Object object = null;
			if (type.equals("boolean")) {
				object = os.unmarshal_boolean();
			} else if (type.equals("short")) {
				object = os.unmarshal_short();
			} else if (type.equals("int")) {
				object = os.unmarshal_int();
			} else if (type.equals("long")) {
				object = os.unmarshal_long();
			} else if (type.equals("float")) {
				object = os.unmarshal_float();
			} else if (type.equals("string")) {
				object = os.unmarshal_String();
			} else 
				throw new RuntimeException("不支持的转换类型。type=" + type);
			return object;
		}
	}
	
	private boolean isStandardType(String typeStr) {
		if (typeStr == null || typeStr.trim().equals("")) return false;
		for (String str : STANDARD_TYPE_ARRAY)
			if (typeStr.trim().equals(str))
				return true;
		return false;
	}
	
	
	/**
	 * 这个方法在原服上调用
	 * 
	 * @param tableName
	 * @param beanName
	 * @param tableKey
	 * @param octets
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void setTableDataSrcServer(String tableName, String keyType, String valueType, Octets keyData,Octets valueData) throws Exception {
		if (CrossManager.getInstance().isInCrossServer()) {
			CrossManager.logger.error("Exception:原服务器调用TransferDataManager.setTableDataSrcServer方法");
			return;
		}
		
		if(!checkModifySrcServer(tableName)){
			CrossManager.logger.error("Exception:原服务器 setTableDataSrcServer.setTableDataSrcServer方法");
			return;
		}
		
		final mkdb.TTable<Object, Object> table = (mkdb.TTable<Object, Object>)
			mkdb.Mkdb.getInstance().getTables().getTable(tableName);
		if (table == null){
			CrossManager.logger.error("Exception:原服TransferDataManager.setTableDataSrcServer方法  table=null");
			return;
		}
		Object tableKey = getOriginalDataByType(keyType, keyData);
		table.remove(tableKey); //把以前的记录删掉
		Object tableValue = getOriginalDataByType(valueType, valueData);
		table.insertUncheckedAutoKey(tableKey, tableValue);
		//table.insert(tableKey, tableValue);//该接口会检查autokey规则，跨服无法使用
	}
	
	/**
	 * 在原服上对表的数据修改 增删要谨慎  增加了原服表的检查步骤
	 * @param tableName
	 * @return
	 */
	public boolean checkModifySrcServer(String tableName){
		
		if(tableName.equals("hslj") ||
				tableName.equals("crossmatrix")|| 
				tableName.equals("hsljteams")){
			return true;
		}
		return false;
	}
	
	
}
