
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRetRoleProp__ extends mkio.Protocol { }

/** 返回玩家请求的属性对应的属性值
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRetRoleProp extends __SRetRoleProp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786454;

	public int getType() {
		return 786454;
	}

	public long roleid;
	public java.util.HashMap<Integer,Float> datas; // 属性值对

	public SRetRoleProp() {
		datas = new java.util.HashMap<Integer,Float>();
	}

	public SRetRoleProp(long _roleid_, java.util.HashMap<Integer,Float> _datas_) {
		this.roleid = _roleid_;
		this.datas = _datas_;
	}

	public final boolean _validator_() {
		if (roleid <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.compact_uint32(datas.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : datas.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			float _v_;
			_v_ = _os_.unmarshal_float();
			datas.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRetRoleProp) {
			SRetRoleProp _o_ = (SRetRoleProp)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!datas.equals(_o_.datas)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += datas.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(datas).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

