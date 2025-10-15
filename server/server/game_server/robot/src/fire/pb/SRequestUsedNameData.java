
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestUsedNameData__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestUsedNameData extends __SRequestUsedNameData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786505;

	public int getType() {
		return 786505;
	}

	public java.util.LinkedList<java.lang.String> usednames; // 曾用名
	public int itemkey; // 使用的改名符itemkey

	public SRequestUsedNameData() {
		usednames = new java.util.LinkedList<java.lang.String>();
	}

	public SRequestUsedNameData(java.util.LinkedList<java.lang.String> _usednames_, int _itemkey_) {
		this.usednames = _usednames_;
		this.itemkey = _itemkey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(usednames.size());
		for (java.lang.String _v_ : usednames) {
			_os_.marshal(_v_, "UTF-16LE");
		}
		_os_.marshal(itemkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			java.lang.String _v_;
			_v_ = _os_.unmarshal_String("UTF-16LE");
			usednames.add(_v_);
		}
		itemkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestUsedNameData) {
			SRequestUsedNameData _o_ = (SRequestUsedNameData)_o1_;
			if (!usednames.equals(_o_.usednames)) return false;
			if (itemkey != _o_.itemkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += usednames.hashCode();
		_h_ += itemkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(usednames).append(",");
		_sb_.append(itemkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

