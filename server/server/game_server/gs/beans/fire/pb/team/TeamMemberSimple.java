
package fire.pb.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TeamMemberSimple implements Marshal {
	public long roleid;
	public java.lang.String rolename;
	public int level;
	public int school;
	public int shape;

	public TeamMemberSimple() {
		rolename = "";
	}

	public TeamMemberSimple(long _roleid_, java.lang.String _rolename_, int _level_, int _school_, int _shape_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.level = _level_;
		this.school = _school_;
		this.shape = _shape_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(shape);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TeamMemberSimple) {
			TeamMemberSimple _o_ = (TeamMemberSimple)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (shape != _o_.shape) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += level;
		_h_ += school;
		_h_ += shape;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(school).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

