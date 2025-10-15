
package fire.msp.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class Pos implements Marshal , Comparable<Pos>{
	public int x; // x
	public int y; // y

	public Pos() {
	}

	public Pos(int _x_, int _y_) {
		this.x = _x_;
		this.y = _y_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(x);
		_os_.marshal(y);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		x = _os_.unmarshal_int();
		y = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Pos) {
			Pos _o_ = (Pos)_o1_;
			if (x != _o_.x) return false;
			if (y != _o_.y) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += x;
		_h_ += y;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(x).append(",");
		_sb_.append(y).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(Pos _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = x - _o_.x;
		if (0 != _c_) return _c_;
		_c_ = y - _o_.y;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

