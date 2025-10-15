
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RuneCountInfo implements Marshal {
	public long roleid; // 角色roleid
	public java.lang.String rolename; // 名称
	public int level; // 等级
	public int school; // 职业
	public int position; // 职务
	public int givenum; // 捐献
	public int acceptnum; // 收取
	public int givelevel; // 收取

	public RuneCountInfo() {
		rolename = "";
	}

	public RuneCountInfo(long _roleid_, java.lang.String _rolename_, int _level_, int _school_, int _position_, int _givenum_, int _acceptnum_, int _givelevel_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.level = _level_;
		this.school = _school_;
		this.position = _position_;
		this.givenum = _givenum_;
		this.acceptnum = _acceptnum_;
		this.givelevel = _givelevel_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(position);
		_os_.marshal(givenum);
		_os_.marshal(acceptnum);
		_os_.marshal(givelevel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		position = _os_.unmarshal_int();
		givenum = _os_.unmarshal_int();
		acceptnum = _os_.unmarshal_int();
		givelevel = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RuneCountInfo) {
			RuneCountInfo _o_ = (RuneCountInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (position != _o_.position) return false;
			if (givenum != _o_.givenum) return false;
			if (acceptnum != _o_.acceptnum) return false;
			if (givelevel != _o_.givelevel) return false;
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
		_h_ += position;
		_h_ += givenum;
		_h_ += acceptnum;
		_h_ += givelevel;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(school).append(",");
		_sb_.append(position).append(",");
		_sb_.append(givenum).append(",");
		_sb_.append(acceptnum).append(",");
		_sb_.append(givelevel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

