
package fire.pb.fushi.redpack;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RedPackInfo implements Marshal {
	public java.lang.String redpackid; // 红包Id
	public long roleid; // 角色Id
	public java.lang.String rolename; // 角色名
	public java.lang.String redpackdes; // 红包寄语
	public int redpackstate; // 红包状态
	public int fushi; // 符石数

	public RedPackInfo() {
		redpackid = "";
		rolename = "";
		redpackdes = "";
	}

	public RedPackInfo(java.lang.String _redpackid_, long _roleid_, java.lang.String _rolename_, java.lang.String _redpackdes_, int _redpackstate_, int _fushi_) {
		this.redpackid = _redpackid_;
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.redpackdes = _redpackdes_;
		this.redpackstate = _redpackstate_;
		this.fushi = _fushi_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(redpackid, "UTF-16LE");
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(redpackdes, "UTF-16LE");
		_os_.marshal(redpackstate);
		_os_.marshal(fushi);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		redpackid = _os_.unmarshal_String("UTF-16LE");
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		redpackdes = _os_.unmarshal_String("UTF-16LE");
		redpackstate = _os_.unmarshal_int();
		fushi = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RedPackInfo) {
			RedPackInfo _o_ = (RedPackInfo)_o1_;
			if (!redpackid.equals(_o_.redpackid)) return false;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (!redpackdes.equals(_o_.redpackdes)) return false;
			if (redpackstate != _o_.redpackstate) return false;
			if (fushi != _o_.fushi) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += redpackid.hashCode();
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += redpackdes.hashCode();
		_h_ += redpackstate;
		_h_ += fushi;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(redpackid.length()).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append("T").append(redpackdes.length()).append(",");
		_sb_.append(redpackstate).append(",");
		_sb_.append(fushi).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

