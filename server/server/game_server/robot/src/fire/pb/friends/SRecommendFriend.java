
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRecommendFriend__ extends mkio.Protocol { }

/** 服务器返回推荐好友
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRecommendFriend extends __SRecommendFriend__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806577;

	public int getType() {
		return 806577;
	}

	public java.util.ArrayList<fire.pb.friends.InfoBean> friendinfobeanlist;

	public SRecommendFriend() {
		friendinfobeanlist = new java.util.ArrayList<fire.pb.friends.InfoBean>();
	}

	public SRecommendFriend(java.util.ArrayList<fire.pb.friends.InfoBean> _friendinfobeanlist_) {
		this.friendinfobeanlist = _friendinfobeanlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.friends.InfoBean _v_ : friendinfobeanlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(friendinfobeanlist.size());
		for (fire.pb.friends.InfoBean _v_ : friendinfobeanlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.friends.InfoBean _v_ = new fire.pb.friends.InfoBean();
			_v_.unmarshal(_os_);
			friendinfobeanlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRecommendFriend) {
			SRecommendFriend _o_ = (SRecommendFriend)_o1_;
			if (!friendinfobeanlist.equals(_o_.friendinfobeanlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += friendinfobeanlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(friendinfobeanlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

