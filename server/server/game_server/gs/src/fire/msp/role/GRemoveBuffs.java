
package fire.msp.role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRemoveBuffs__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRemoveBuffs extends __GRemoveBuffs__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730906;

	public int getType() {
		return 730906;
	}

	public long roleid; // roleid
	public java.util.LinkedList<Integer> rmbuffs; // buffs

	public GRemoveBuffs() {
		rmbuffs = new java.util.LinkedList<Integer>();
	}

	public GRemoveBuffs(long _roleid_, java.util.LinkedList<Integer> _rmbuffs_) {
		this.roleid = _roleid_;
		this.rmbuffs = _rmbuffs_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.compact_uint32(rmbuffs.size());
		for (Integer _v_ : rmbuffs) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			rmbuffs.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRemoveBuffs) {
			GRemoveBuffs _o_ = (GRemoveBuffs)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rmbuffs.equals(_o_.rmbuffs)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rmbuffs.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(rmbuffs).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

