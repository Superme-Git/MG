
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SOffLineMsgMessageToRole__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SOffLineMsgMessageToRole extends __SOffLineMsgMessageToRole__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806537;

	public int getType() {
		return 806537;
	}

	public java.util.LinkedList<fire.pb.friends.offLineMsgBean> offlinemsglist;

	public SOffLineMsgMessageToRole() {
		offlinemsglist = new java.util.LinkedList<fire.pb.friends.offLineMsgBean>();
	}

	public SOffLineMsgMessageToRole(java.util.LinkedList<fire.pb.friends.offLineMsgBean> _offlinemsglist_) {
		this.offlinemsglist = _offlinemsglist_;
	}

	public final boolean _validator_() {
		for (fire.pb.friends.offLineMsgBean _v_ : offlinemsglist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(offlinemsglist.size());
		for (fire.pb.friends.offLineMsgBean _v_ : offlinemsglist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.friends.offLineMsgBean _v_ = new fire.pb.friends.offLineMsgBean();
			_v_.unmarshal(_os_);
			offlinemsglist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SOffLineMsgMessageToRole) {
			SOffLineMsgMessageToRole _o_ = (SOffLineMsgMessageToRole)_o1_;
			if (!offlinemsglist.equals(_o_.offlinemsglist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += offlinemsglist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(offlinemsglist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

