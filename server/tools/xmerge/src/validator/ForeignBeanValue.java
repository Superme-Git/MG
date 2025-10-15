package validator;

import java.util.HashSet;
import java.util.Set;

import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * 生成foreign关系表时value的结构 
 * */
public class ForeignBeanValue{

	private Set<ForeignBeanKey> keySet;
	
	public ForeignBeanValue(){
		this.keySet = new HashSet<ForeignBeanKey>();
	}
	
	public ForeignBeanValue(Set<ForeignBeanKey> keySet){
		this.keySet = keySet;
	}

	public String getClassName() {
		return "validator.ForeignBeanValue";
	}

	public Class<?> getClazz() {
		return this.getClass();
	}

	public String getName() {
		return "ForeignBeanValue";
	}

	public Set<ForeignBeanKey> getKeySet(){
		return this.keySet;
	}

	public boolean existKey(ForeignBeanKey key){
		return this.keySet.contains(key);
	}
	
	public boolean addKey(ForeignBeanKey key){
		return this.keySet.add(key);
	}
	
	public boolean removeKey(ForeignBeanKey key){
		return this.keySet.remove(key);
	}
	
	public boolean isConstant() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.compact_uint32(this.keySet.size());
		for (ForeignBeanKey _v_ : this.keySet) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	public long sizeof() {
		long len = 0L;
		for(ForeignBeanKey v : this.keySet)
			len += v.sizeof();
		return len;
	}

	public OctetsStream unmarshal(OctetsStream _os_) {
		try {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				ForeignBeanKey _v_ = new ForeignBeanKey();
				 _v_.unmarshal(_os_);
				this.keySet.add(_v_);
			}
			return _os_;
		} catch (MarshalException x) {
			throw new RuntimeException(x);
		}
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		for (ForeignBeanKey v : this.keySet) {
			sb.append(v.toString()).append(" ");
		}
		sb.append(" ]");
		return sb.toString();
	}
}
