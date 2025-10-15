
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SItemAdded__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SItemAdded extends __SItemAdded__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787658;

	public int getType() {
		return 787658;
	}

	public java.util.ArrayList<fire.pb.item.ItemAddInfo> items;

	public SItemAdded() {
		items = new java.util.ArrayList<fire.pb.item.ItemAddInfo>();
	}

	public SItemAdded(java.util.ArrayList<fire.pb.item.ItemAddInfo> _items_) {
		this.items = _items_;
	}

	public final boolean _validator_() {
		for (fire.pb.item.ItemAddInfo _v_ : items)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(items.size());
		for (fire.pb.item.ItemAddInfo _v_ : items) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.item.ItemAddInfo _v_ = new fire.pb.item.ItemAddInfo();
			_v_.unmarshal(_os_);
			items.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SItemAdded) {
			SItemAdded _o_ = (SItemAdded)_o1_;
			if (!items.equals(_o_.items)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += items.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(items).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

