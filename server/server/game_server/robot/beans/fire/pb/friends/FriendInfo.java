
package fire.pb.friends;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class FriendInfo implements Marshal {
	public fire.pb.friends.InfoBean friendinfobean;
	public int friendlevel; // 好友度

	public FriendInfo() {
		friendinfobean = new fire.pb.friends.InfoBean();
	}

	public FriendInfo(fire.pb.friends.InfoBean _friendinfobean_, int _friendlevel_) {
		this.friendinfobean = _friendinfobean_;
		this.friendlevel = _friendlevel_;
	}

	public final boolean _validator_() {
		if (!friendinfobean._validator_()) return false;
		if (friendlevel < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(friendinfobean);
		_os_.marshal(friendlevel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		friendinfobean.unmarshal(_os_);
		friendlevel = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof FriendInfo) {
			FriendInfo _o_ = (FriendInfo)_o1_;
			if (!friendinfobean.equals(_o_.friendinfobean)) return false;
			if (friendlevel != _o_.friendlevel) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += friendinfobean.hashCode();
		_h_ += friendlevel;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(friendinfobean).append(",");
		_sb_.append(friendlevel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

