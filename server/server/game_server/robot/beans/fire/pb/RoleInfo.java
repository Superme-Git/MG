
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleInfo implements Marshal {
	public long roleid; // ID
	public java.lang.String rolename; // 名称
	public int school; // 人物职业
	public int shape; // 角色id
	public int level; // 等级
	public java.util.HashMap<Byte,Integer> components; // 角色换装信息key值参考SpriteComponents的枚举，value为0代表脱下某件换装
	public long rolecreatetime; // 角色创建时间

	public RoleInfo() {
		rolename = "";
		components = new java.util.HashMap<Byte,Integer>();
	}

	public RoleInfo(long _roleid_, java.lang.String _rolename_, int _school_, int _shape_, int _level_, java.util.HashMap<Byte,Integer> _components_, long _rolecreatetime_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.school = _school_;
		this.shape = _shape_;
		this.level = _level_;
		this.components = _components_;
		this.rolecreatetime = _rolecreatetime_;
	}

	public final boolean _validator_() {
		if (roleid < 1) return false;
		if (level < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(school);
		_os_.marshal(shape);
		_os_.marshal(level);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(rolecreatetime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		school = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		rolecreatetime = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleInfo) {
			RoleInfo _o_ = (RoleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (school != _o_.school) return false;
			if (shape != _o_.shape) return false;
			if (level != _o_.level) return false;
			if (!components.equals(_o_.components)) return false;
			if (rolecreatetime != _o_.rolecreatetime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += school;
		_h_ += shape;
		_h_ += level;
		_h_ += components.hashCode();
		_h_ += (int)rolecreatetime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(school).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(level).append(",");
		_sb_.append(components).append(",");
		_sb_.append(rolecreatetime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

