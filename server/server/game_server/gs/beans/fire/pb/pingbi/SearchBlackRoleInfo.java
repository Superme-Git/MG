
package fire.pb.pingbi;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SearchBlackRoleInfo implements Marshal {
	public long roleid;
	public java.lang.String name;
	public short rolelevel;
	public byte school;
	public byte online; // 0=不在线 1=在线
	public int shape; // 头像
	public byte camp; // 0中立  1部落 2联盟

	public SearchBlackRoleInfo() {
		name = "";
	}

	public SearchBlackRoleInfo(long _roleid_, java.lang.String _name_, short _rolelevel_, byte _school_, byte _online_, int _shape_, byte _camp_) {
		this.roleid = _roleid_;
		this.name = _name_;
		this.rolelevel = _rolelevel_;
		this.school = _school_;
		this.online = _online_;
		this.shape = _shape_;
		this.camp = _camp_;
	}

	public final boolean _validator_() {
		if (roleid < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(rolelevel);
		_os_.marshal(school);
		_os_.marshal(online);
		_os_.marshal(shape);
		_os_.marshal(camp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		name = _os_.unmarshal_String("UTF-16LE");
		rolelevel = _os_.unmarshal_short();
		school = _os_.unmarshal_byte();
		online = _os_.unmarshal_byte();
		shape = _os_.unmarshal_int();
		camp = _os_.unmarshal_byte();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SearchBlackRoleInfo) {
			SearchBlackRoleInfo _o_ = (SearchBlackRoleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!name.equals(_o_.name)) return false;
			if (rolelevel != _o_.rolelevel) return false;
			if (school != _o_.school) return false;
			if (online != _o_.online) return false;
			if (shape != _o_.shape) return false;
			if (camp != _o_.camp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += name.hashCode();
		_h_ += rolelevel;
		_h_ += (int)school;
		_h_ += (int)online;
		_h_ += shape;
		_h_ += (int)camp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(rolelevel).append(",");
		_sb_.append(school).append(",");
		_sb_.append(online).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(camp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

