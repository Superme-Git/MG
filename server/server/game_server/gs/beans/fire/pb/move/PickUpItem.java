
package fire.pb.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class PickUpItem implements Marshal , Comparable<PickUpItem>{
	public long uniqueid; // 物品的实例ID
	public int baseid; // 物品的模板ID
	public fire.pb.move.Pos pos; // 位置

	public PickUpItem() {
		pos = new fire.pb.move.Pos();
	}

	public PickUpItem(long _uniqueid_, int _baseid_, fire.pb.move.Pos _pos_) {
		this.uniqueid = _uniqueid_;
		this.baseid = _baseid_;
		this.pos = _pos_;
	}

	public final boolean _validator_() {
		if (!pos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(uniqueid);
		_os_.marshal(baseid);
		_os_.marshal(pos);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		uniqueid = _os_.unmarshal_long();
		baseid = _os_.unmarshal_int();
		pos.unmarshal(_os_);
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PickUpItem) {
			PickUpItem _o_ = (PickUpItem)_o1_;
			if (uniqueid != _o_.uniqueid) return false;
			if (baseid != _o_.baseid) return false;
			if (!pos.equals(_o_.pos)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)uniqueid;
		_h_ += baseid;
		_h_ += pos.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(uniqueid).append(",");
		_sb_.append(baseid).append(",");
		_sb_.append(pos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(PickUpItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(uniqueid - _o_.uniqueid);
		if (0 != _c_) return _c_;
		_c_ = baseid - _o_.baseid;
		if (0 != _c_) return _c_;
		_c_ = pos.compareTo(_o_.pos);
		if (0 != _c_) return _c_;
		return _c_;
	}

}

