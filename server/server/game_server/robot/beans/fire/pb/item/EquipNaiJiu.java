
package fire.pb.item;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class EquipNaiJiu implements Marshal , Comparable<EquipNaiJiu>{
	public int keyinpack;
	public int endure;

	public EquipNaiJiu() {
	}

	public EquipNaiJiu(int _keyinpack_, int _endure_) {
		this.keyinpack = _keyinpack_;
		this.endure = _endure_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(keyinpack);
		_os_.marshal(endure);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		keyinpack = _os_.unmarshal_int();
		endure = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof EquipNaiJiu) {
			EquipNaiJiu _o_ = (EquipNaiJiu)_o1_;
			if (keyinpack != _o_.keyinpack) return false;
			if (endure != _o_.endure) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += keyinpack;
		_h_ += endure;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(keyinpack).append(",");
		_sb_.append(endure).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(EquipNaiJiu _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		_c_ = endure - _o_.endure;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

