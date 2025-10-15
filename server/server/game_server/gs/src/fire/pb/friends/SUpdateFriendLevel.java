
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUpdateFriendLevel__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUpdateFriendLevel extends __SUpdateFriendLevel__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806536;

	public int getType() {
		return 806536;
	}

	public int currentfriendlevel; // 当前的友好度
	public long friendid;

	public SUpdateFriendLevel() {
	}

	public SUpdateFriendLevel(int _currentfriendlevel_, long _friendid_) {
		this.currentfriendlevel = _currentfriendlevel_;
		this.friendid = _friendid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(currentfriendlevel);
		_os_.marshal(friendid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		currentfriendlevel = _os_.unmarshal_int();
		friendid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUpdateFriendLevel) {
			SUpdateFriendLevel _o_ = (SUpdateFriendLevel)_o1_;
			if (currentfriendlevel != _o_.currentfriendlevel) return false;
			if (friendid != _o_.friendid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += currentfriendlevel;
		_h_ += (int)friendid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(currentfriendlevel).append(",");
		_sb_.append(friendid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SUpdateFriendLevel _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = currentfriendlevel - _o_.currentfriendlevel;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(friendid - _o_.friendid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

