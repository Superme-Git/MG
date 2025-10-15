
package fire.pb.master;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleBaseData implements Marshal {
	public long roleid; // roleid
	public java.lang.String nickname; // 名字

	public RoleBaseData() {
		nickname = "";
	}

	public RoleBaseData(long _roleid_, java.lang.String _nickname_) {
		this.roleid = _roleid_;
		this.nickname = _nickname_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(nickname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		nickname = _os_.unmarshal_String("UTF-16LE");
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleBaseData) {
			RoleBaseData _o_ = (RoleBaseData)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += nickname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(nickname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

