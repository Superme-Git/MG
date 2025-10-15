
package fire.pb.fushi.redpack;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RedPackRoleTip implements Marshal {
	public int modeltype; // 红包类型
	public java.lang.String redpackid; // 红包Id
	public java.lang.String rolename; // 角色名
	public int fushi; // 符石数

	public RedPackRoleTip() {
		redpackid = "";
		rolename = "";
	}

	public RedPackRoleTip(int _modeltype_, java.lang.String _redpackid_, java.lang.String _rolename_, int _fushi_) {
		this.modeltype = _modeltype_;
		this.redpackid = _redpackid_;
		this.rolename = _rolename_;
		this.fushi = _fushi_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(modeltype);
		_os_.marshal(redpackid, "UTF-16LE");
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(fushi);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		redpackid = _os_.unmarshal_String("UTF-16LE");
		rolename = _os_.unmarshal_String("UTF-16LE");
		fushi = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RedPackRoleTip) {
			RedPackRoleTip _o_ = (RedPackRoleTip)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (!redpackid.equals(_o_.redpackid)) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (fushi != _o_.fushi) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += redpackid.hashCode();
		_h_ += rolename.hashCode();
		_h_ += fushi;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append("T").append(redpackid.length()).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(fushi).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

