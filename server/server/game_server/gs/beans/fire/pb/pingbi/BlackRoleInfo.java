
package fire.pb.pingbi;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class BlackRoleInfo implements Marshal {
	public long roleid; // 角色id
	public java.lang.String name; // 角色名称
	public short level; // 角色等级
	public int shape; // 头像
	public byte school; // 职业

	public BlackRoleInfo() {
		name = "";
	}

	public BlackRoleInfo(long _roleid_, java.lang.String _name_, short _level_, int _shape_, byte _school_) {
		this.roleid = _roleid_;
		this.name = _name_;
		this.level = _level_;
		this.shape = _shape_;
		this.school = _school_;
	}

	public final boolean _validator_() {
		if (roleid < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(shape);
		_os_.marshal(school);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		name = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_short();
		shape = _os_.unmarshal_int();
		school = _os_.unmarshal_byte();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof BlackRoleInfo) {
			BlackRoleInfo _o_ = (BlackRoleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!name.equals(_o_.name)) return false;
			if (level != _o_.level) return false;
			if (shape != _o_.shape) return false;
			if (school != _o_.school) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += name.hashCode();
		_h_ += level;
		_h_ += shape;
		_h_ += (int)school;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(school).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

