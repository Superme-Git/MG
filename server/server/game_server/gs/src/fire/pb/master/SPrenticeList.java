
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPrenticeList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPrenticeList extends __SPrenticeList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816453;

	public int getType() {
		return 816453;
	}

	public java.util.ArrayList<fire.pb.master.MasterPrenticeBaseData> prentice;
	public int itemkey; // 物品的bagkey

	public SPrenticeList() {
		prentice = new java.util.ArrayList<fire.pb.master.MasterPrenticeBaseData>();
	}

	public SPrenticeList(java.util.ArrayList<fire.pb.master.MasterPrenticeBaseData> _prentice_, int _itemkey_) {
		this.prentice = _prentice_;
		this.itemkey = _itemkey_;
	}

	public final boolean _validator_() {
		for (fire.pb.master.MasterPrenticeBaseData _v_ : prentice)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(prentice.size());
		for (fire.pb.master.MasterPrenticeBaseData _v_ : prentice) {
			_os_.marshal(_v_);
		}
		_os_.marshal(itemkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.master.MasterPrenticeBaseData _v_ = new fire.pb.master.MasterPrenticeBaseData();
			_v_.unmarshal(_os_);
			prentice.add(_v_);
		}
		itemkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPrenticeList) {
			SPrenticeList _o_ = (SPrenticeList)_o1_;
			if (!prentice.equals(_o_.prentice)) return false;
			if (itemkey != _o_.itemkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += prentice.hashCode();
		_h_ += itemkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prentice).append(",");
		_sb_.append(itemkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

