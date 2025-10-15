
package fire.pb.clan.fight;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleBattleFieldRank implements Marshal {
	public java.lang.String rolename; // 玩家名字 by changhao
	public int rolescroe; // 玩家积分 by changhao

	public RoleBattleFieldRank() {
		rolename = "";
	}

	public RoleBattleFieldRank(java.lang.String _rolename_, int _rolescroe_) {
		this.rolename = _rolename_;
		this.rolescroe = _rolescroe_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(rolescroe);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolename = _os_.unmarshal_String("UTF-16LE");
		rolescroe = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleBattleFieldRank) {
			RoleBattleFieldRank _o_ = (RoleBattleFieldRank)_o1_;
			if (!rolename.equals(_o_.rolename)) return false;
			if (rolescroe != _o_.rolescroe) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolename.hashCode();
		_h_ += rolescroe;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(rolescroe).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

