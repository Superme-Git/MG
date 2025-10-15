
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleBaseInfo implements Marshal {
	public long roleid;
	public java.lang.String rolename; // 名称
	public int rolelevel; // 等级
	public int roleschool; // 职业
	public long applytime; // 申请时间

	public RoleBaseInfo() {
		rolename = "";
	}

	public RoleBaseInfo(long _roleid_, java.lang.String _rolename_, int _rolelevel_, int _roleschool_, long _applytime_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.rolelevel = _rolelevel_;
		this.roleschool = _roleschool_;
		this.applytime = _applytime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(rolelevel);
		_os_.marshal(roleschool);
		_os_.marshal(applytime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		rolelevel = _os_.unmarshal_int();
		roleschool = _os_.unmarshal_int();
		applytime = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleBaseInfo) {
			RoleBaseInfo _o_ = (RoleBaseInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (rolelevel != _o_.rolelevel) return false;
			if (roleschool != _o_.roleschool) return false;
			if (applytime != _o_.applytime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += rolelevel;
		_h_ += roleschool;
		_h_ += (int)applytime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(rolelevel).append(",");
		_sb_.append(roleschool).append(",");
		_sb_.append(applytime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

