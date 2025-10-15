
package fire.pb.battle.pvp1;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class PvP1RoleSingleWin implements Marshal {
	public long roleid; // 角色ID
	public java.lang.String rolename; // 角色名称
	public int combonum; // 连胜次数

	public PvP1RoleSingleWin() {
		rolename = "";
	}

	public PvP1RoleSingleWin(long _roleid_, java.lang.String _rolename_, int _combonum_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.combonum = _combonum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(combonum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		combonum = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PvP1RoleSingleWin) {
			PvP1RoleSingleWin _o_ = (PvP1RoleSingleWin)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (combonum != _o_.combonum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += combonum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(combonum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

