package validator;

import mkdb.util.DatabaseMetaData;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * 生成foreign关系表时key的结构 
 * @modified ly 8.28
 * */
public class ForeignBeanKey implements Comparable<ForeignBeanKey>{
	private String tableName;
	private OctetsStream key;
	
	
	public ForeignBeanKey(){
		this.tableName = null;
		this.key = null; 
	}
	
	public ForeignBeanKey(String tableName, OctetsStream key){
		this.tableName = tableName;
		this.key = OctetsStream.wrap(Octets.wrap(key.getBytes()));
	}
	
	public ForeignBeanKey(String tableName, byte[] _key){
		this.tableName = tableName;
		this.key = OctetsStream.wrap(Octets.wrap(_key));
	}
	
	public ForeignBeanKey(String tableName, Object key, DatabaseMetaData.Type type){
		this.tableName = tableName;
		OctetsStream os = type.marshal(key);
		this.key = OctetsStream.wrap(Octets.wrap(os.getBytes()));
	}
	
	public String getClassName() {
		return "validator.ForeignBeanKey";
	}

	public Class<?> getClazz() {
		return this.getClass();
	}

	public String getName() {
		return "ForeignBeanKey";
	}

	public String getTableName(){
		return this.tableName;
	}
	
	public OctetsStream getKey(){
		return this.key;
	}
	
	public DatabaseMetaData.Type getKeyType(){
		DatabaseMetaData.Type type = DatabaseMetaData.getInstance().getTable(this.tableName).getKeyType();
		if(!type.isAny())
			return type;
		else
			throw new RuntimeException(type + " is Any");
	}

	public boolean isConstant() {
		return true;
	}

	public OctetsStream marshal(OctetsStream os) {
		os.marshal(this.tableName, mkdb.Const.IO_CHARSET);
		os.marshal(this.key.getBytes());
		return os;
	}

	public long sizeof() {
		return 2L * (this.tableName.length()) + this.getKeyType().sizeof();
	}

	public OctetsStream unmarshal(OctetsStream os) {
		try {
			this.tableName = os.unmarshal_String(mkdb.Const.IO_CHARSET);
			byte[] bytes = os.unmarshal_bytes();
			this.key = OctetsStream.wrap(Octets.wrap(bytes, bytes.length));
			return os;
		} catch (MarshalException x) {
			throw new RuntimeException(x);
		}
	}
	
	@Override
	public boolean equals(Object obj){
		ForeignBeanKey fk = null;
		if(obj instanceof ForeignBeanKey) {
			fk = (ForeignBeanKey)obj;
			return (this.hashCode() == fk.hashCode());
		}
		return false;	
	}
	@Override
	public String toString(){
		try{
			Object obj = this.getKeyType().unmarshal(this.key.getBytes());
			return this.tableName+ "-->" +obj;
			}catch(Exception e){
			throw new RuntimeException(e); 
		}
	}
	
	@Override
	public int compareTo(ForeignBeanKey arg0) {
		return this.hashCode() - arg0.hashCode();
	}
	
	@Override
	public int hashCode() {
		return tableName.hashCode() + java.util.Arrays.hashCode(key.getBytes());
	}
}
