
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSignList__ extends mkio.Protocol { }

/** 签名列表
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSignList extends __SSignList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806683;

	public int getType() {
		return 806683;
	}

	public java.util.HashMap<Long,java.lang.String> signcontentmap; // 签名列表 key=角色id value=签名内容

	public SSignList() {
		signcontentmap = new java.util.HashMap<Long,java.lang.String>();
	}

	public SSignList(java.util.HashMap<Long,java.lang.String> _signcontentmap_) {
		this.signcontentmap = _signcontentmap_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(signcontentmap.size());
		for (java.util.Map.Entry<Long, java.lang.String> _e_ : signcontentmap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue(), "UTF-16LE");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _k_;
			_k_ = _os_.unmarshal_long();
			java.lang.String _v_;
			_v_ = _os_.unmarshal_String("UTF-16LE");
			signcontentmap.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSignList) {
			SSignList _o_ = (SSignList)_o1_;
			if (!signcontentmap.equals(_o_.signcontentmap)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += signcontentmap.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(signcontentmap).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

