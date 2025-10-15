
package fire.pb.team.teammelon;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleRollInfo implements Marshal {
	public long roleid;
	public java.lang.String rolename;
	public int roll; // 如果是0就是放弃了 by changhao

	public RoleRollInfo() {
		rolename = "";
	}

	public RoleRollInfo(long _roleid_, java.lang.String _rolename_, int _roll_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.roll = _roll_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(roll);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		roll = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleRollInfo) {
			RoleRollInfo _o_ = (RoleRollInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (roll != _o_.roll) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += roll;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(roll).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

