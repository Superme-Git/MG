
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAddPickupScreen__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAddPickupScreen extends __SAddPickupScreen__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790445;

	public int getType() {
		return 790445;
	}

	public java.util.LinkedList<fire.pb.move.PickUpItem> pickuplist;

	public SAddPickupScreen() {
		pickuplist = new java.util.LinkedList<fire.pb.move.PickUpItem>();
	}

	public SAddPickupScreen(java.util.LinkedList<fire.pb.move.PickUpItem> _pickuplist_) {
		this.pickuplist = _pickuplist_;
	}

	public final boolean _validator_() {
		for (fire.pb.move.PickUpItem _v_ : pickuplist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(pickuplist.size());
		for (fire.pb.move.PickUpItem _v_ : pickuplist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.move.PickUpItem _v_ = new fire.pb.move.PickUpItem();
			_v_.unmarshal(_os_);
			pickuplist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAddPickupScreen) {
			SAddPickupScreen _o_ = (SAddPickupScreen)_o1_;
			if (!pickuplist.equals(_o_.pickuplist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pickuplist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pickuplist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

