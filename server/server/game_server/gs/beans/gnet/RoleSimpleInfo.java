
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleSimpleInfo implements Marshal {
	public long roleid;
	public com.locojoy.base.Octets rolename;
	public int level;

	public RoleSimpleInfo() {
		rolename = new com.locojoy.base.Octets();
	}

	public RoleSimpleInfo(long _roleid_, com.locojoy.base.Octets _rolename_, int _level_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.level = _level_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename);
		_os_.marshal(level);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_Octets();
		level = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleSimpleInfo) {
			RoleSimpleInfo _o_ = (RoleSimpleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (level != _o_.level) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += level;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("B").append(rolename.size()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

