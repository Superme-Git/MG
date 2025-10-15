
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 角色染色相关命令
*/
public class RoleColorType implements Marshal , Comparable<RoleColorType>{
	public int colorpos1; // 部位1
	public int colorpos2; // 部位2

	public RoleColorType() {
	}

	public RoleColorType(int _colorpos1_, int _colorpos2_) {
		this.colorpos1 = _colorpos1_;
		this.colorpos2 = _colorpos2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(colorpos1);
		_os_.marshal(colorpos2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		colorpos1 = _os_.unmarshal_int();
		colorpos2 = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleColorType) {
			RoleColorType _o_ = (RoleColorType)_o1_;
			if (colorpos1 != _o_.colorpos1) return false;
			if (colorpos2 != _o_.colorpos2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += colorpos1;
		_h_ += colorpos2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(colorpos1).append(",");
		_sb_.append(colorpos2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(RoleColorType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = colorpos1 - _o_.colorpos1;
		if (0 != _c_) return _c_;
		_c_ = colorpos2 - _o_.colorpos2;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

