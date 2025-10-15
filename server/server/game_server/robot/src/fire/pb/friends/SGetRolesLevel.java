
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetRolesLevel__ extends mkio.Protocol { }

/** 返回一批角色等级
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetRolesLevel extends __SGetRolesLevel__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806646;

	public int getType() {
		return 806646;
	}

	public java.util.HashMap<Long,Integer> roleslevel; // 一批角色id -> 角色等级 (每次最多返回30个)
	public int gettype;

	public SGetRolesLevel() {
		roleslevel = new java.util.HashMap<Long,Integer>();
	}

	public SGetRolesLevel(java.util.HashMap<Long,Integer> _roleslevel_, int _gettype_) {
		this.roleslevel = _roleslevel_;
		this.gettype = _gettype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(roleslevel.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : roleslevel.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(gettype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _k_;
			_k_ = _os_.unmarshal_long();
			int _v_;
			_v_ = _os_.unmarshal_int();
			roleslevel.put(_k_, _v_);
		}
		gettype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetRolesLevel) {
			SGetRolesLevel _o_ = (SGetRolesLevel)_o1_;
			if (!roleslevel.equals(_o_.roleslevel)) return false;
			if (gettype != _o_.gettype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += roleslevel.hashCode();
		_h_ += gettype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleslevel).append(",");
		_sb_.append(gettype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

