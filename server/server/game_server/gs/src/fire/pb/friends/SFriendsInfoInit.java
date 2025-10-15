
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SFriendsInfoInit__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SFriendsInfoInit extends __SFriendsInfoInit__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806433;

	public int getType() {
		return 806433;
	}

	public java.util.ArrayList<fire.pb.friends.FriendInfo> friends;
	public short friendnumlimit; // 好友人数限制
	public byte refusestrangermsg; // 0=接收陌生人消息 1=拒绝陌生人信息

	public SFriendsInfoInit() {
		friends = new java.util.ArrayList<fire.pb.friends.FriendInfo>();
	}

	public SFriendsInfoInit(java.util.ArrayList<fire.pb.friends.FriendInfo> _friends_, short _friendnumlimit_, byte _refusestrangermsg_) {
		this.friends = _friends_;
		this.friendnumlimit = _friendnumlimit_;
		this.refusestrangermsg = _refusestrangermsg_;
	}

	public final boolean _validator_() {
		for (fire.pb.friends.FriendInfo _v_ : friends)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(friends.size());
		for (fire.pb.friends.FriendInfo _v_ : friends) {
			_os_.marshal(_v_);
		}
		_os_.marshal(friendnumlimit);
		_os_.marshal(refusestrangermsg);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.friends.FriendInfo _v_ = new fire.pb.friends.FriendInfo();
			_v_.unmarshal(_os_);
			friends.add(_v_);
		}
		friendnumlimit = _os_.unmarshal_short();
		refusestrangermsg = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SFriendsInfoInit) {
			SFriendsInfoInit _o_ = (SFriendsInfoInit)_o1_;
			if (!friends.equals(_o_.friends)) return false;
			if (friendnumlimit != _o_.friendnumlimit) return false;
			if (refusestrangermsg != _o_.refusestrangermsg) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += friends.hashCode();
		_h_ += friendnumlimit;
		_h_ += (int)refusestrangermsg;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(friends).append(",");
		_sb_.append(friendnumlimit).append(",");
		_sb_.append(refusestrangermsg).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

