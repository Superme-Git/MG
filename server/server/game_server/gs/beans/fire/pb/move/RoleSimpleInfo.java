
package fire.pb.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleSimpleInfo implements Marshal {
	public long roleid;
	public java.lang.String name;
	public int shape;
	public int school;
	public int level;
	public int camptype;

	public RoleSimpleInfo() {
		name = "";
	}

	public RoleSimpleInfo(long _roleid_, java.lang.String _name_, int _shape_, int _school_, int _level_, int _camptype_) {
		this.roleid = _roleid_;
		this.name = _name_;
		this.shape = _shape_;
		this.school = _school_;
		this.level = _level_;
		this.camptype = _camptype_;
	}

	public final boolean _validator_() {
		if (roleid <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(shape);
		_os_.marshal(school);
		_os_.marshal(level);
		_os_.marshal(camptype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		name = _os_.unmarshal_String("UTF-16LE");
		shape = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		camptype = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleSimpleInfo) {
			RoleSimpleInfo _o_ = (RoleSimpleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!name.equals(_o_.name)) return false;
			if (shape != _o_.shape) return false;
			if (school != _o_.school) return false;
			if (level != _o_.level) return false;
			if (camptype != _o_.camptype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += name.hashCode();
		_h_ += shape;
		_h_ += school;
		_h_ += level;
		_h_ += camptype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(school).append(",");
		_sb_.append(level).append(",");
		_sb_.append(camptype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

