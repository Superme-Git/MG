
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRecommendsNames__ extends mkio.Protocol { }

/** 创建角色重名时，服务器回给客户端推荐名字
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRecommendsNames extends __SRecommendsNames__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786476;

	public int getType() {
		return 786476;
	}

	public java.util.ArrayList<com.locojoy.base.Octets> recommendnames;

	public SRecommendsNames() {
		recommendnames = new java.util.ArrayList<com.locojoy.base.Octets>();
	}

	public SRecommendsNames(java.util.ArrayList<com.locojoy.base.Octets> _recommendnames_) {
		this.recommendnames = _recommendnames_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(recommendnames.size());
		for (com.locojoy.base.Octets _v_ : recommendnames) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			recommendnames.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRecommendsNames) {
			SRecommendsNames _o_ = (SRecommendsNames)_o1_;
			if (!recommendnames.equals(_o_.recommendnames)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += recommendnames.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(recommendnames).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

