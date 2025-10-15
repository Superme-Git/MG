
package fire.pb.fushi.redpack;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RedPackRoleRecord implements Marshal {
	public int modeltype; // 红包类型
	public java.lang.String redpackid; // 红包Id
	public long roleid; // 角色Id
	public java.lang.String rolename; // 角色名
	public int school; // 职业
	public int shape; // 外形
	public int redpackmoney; // 红包金额
	public long time; // 时间

	public RedPackRoleRecord() {
		redpackid = "";
		rolename = "";
	}

	public RedPackRoleRecord(int _modeltype_, java.lang.String _redpackid_, long _roleid_, java.lang.String _rolename_, int _school_, int _shape_, int _redpackmoney_, long _time_) {
		this.modeltype = _modeltype_;
		this.redpackid = _redpackid_;
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.school = _school_;
		this.shape = _shape_;
		this.redpackmoney = _redpackmoney_;
		this.time = _time_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(modeltype);
		_os_.marshal(redpackid, "UTF-16LE");
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(school);
		_os_.marshal(shape);
		_os_.marshal(redpackmoney);
		_os_.marshal(time);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		redpackid = _os_.unmarshal_String("UTF-16LE");
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		school = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		redpackmoney = _os_.unmarshal_int();
		time = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RedPackRoleRecord) {
			RedPackRoleRecord _o_ = (RedPackRoleRecord)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (!redpackid.equals(_o_.redpackid)) return false;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (school != _o_.school) return false;
			if (shape != _o_.shape) return false;
			if (redpackmoney != _o_.redpackmoney) return false;
			if (time != _o_.time) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += redpackid.hashCode();
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += school;
		_h_ += shape;
		_h_ += redpackmoney;
		_h_ += (int)time;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append("T").append(redpackid.length()).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(school).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(redpackmoney).append(",");
		_sb_.append(time).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

