
package fire.pb.fushi.redpack;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RedPackRoleHisInfo implements Marshal {
	public long roleid; // 角色Id
	public java.lang.String rolename; // 角色名
	public int redpackmoney; // 红包金额

	public RedPackRoleHisInfo() {
		rolename = "";
	}

	public RedPackRoleHisInfo(long _roleid_, java.lang.String _rolename_, int _redpackmoney_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.redpackmoney = _redpackmoney_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(redpackmoney);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		redpackmoney = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RedPackRoleHisInfo) {
			RedPackRoleHisInfo _o_ = (RedPackRoleHisInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (redpackmoney != _o_.redpackmoney) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += redpackmoney;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(redpackmoney).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

