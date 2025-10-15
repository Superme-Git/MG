
package fire.pb.battle.livedie;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class LDTeamRoleInfoDes implements Marshal {
	public long roleid; // 角色Id
	public java.lang.String rolename; // 角色名
	public int shape; // 角色造型
	public int level; // 角色等级
	public int school; // 职业

	public LDTeamRoleInfoDes() {
		rolename = "";
	}

	public LDTeamRoleInfoDes(long _roleid_, java.lang.String _rolename_, int _shape_, int _level_, int _school_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.shape = _shape_;
		this.level = _level_;
		this.school = _school_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(shape);
		_os_.marshal(level);
		_os_.marshal(school);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		shape = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof LDTeamRoleInfoDes) {
			LDTeamRoleInfoDes _o_ = (LDTeamRoleInfoDes)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (shape != _o_.shape) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += shape;
		_h_ += level;
		_h_ += school;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(level).append(",");
		_sb_.append(school).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

