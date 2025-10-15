
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRemoveUserScreen__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRemoveUserScreen extends __SRemoveUserScreen__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790438;

	public int getType() {
		return 790438;
	}

	public java.util.LinkedList<Long> roleids; // 角色
	public java.util.LinkedList<Long> npcids; // NPC

	public SRemoveUserScreen() {
		roleids = new java.util.LinkedList<Long>();
		npcids = new java.util.LinkedList<Long>();
	}

	public SRemoveUserScreen(java.util.LinkedList<Long> _roleids_, java.util.LinkedList<Long> _npcids_) {
		this.roleids = _roleids_;
		this.npcids = _npcids_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(roleids.size());
		for (Long _v_ : roleids) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(npcids.size());
		for (Long _v_ : npcids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			roleids.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			npcids.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRemoveUserScreen) {
			SRemoveUserScreen _o_ = (SRemoveUserScreen)_o1_;
			if (!roleids.equals(_o_.roleids)) return false;
			if (!npcids.equals(_o_.npcids)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += roleids.hashCode();
		_h_ += npcids.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleids).append(",");
		_sb_.append(npcids).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

