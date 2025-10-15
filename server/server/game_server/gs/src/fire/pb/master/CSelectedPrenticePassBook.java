
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSelectedPrenticePassBook__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSelectedPrenticePassBook extends __CSelectedPrenticePassBook__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816454;

	public int getType() {
		return 816454;
	}

	public long prenticeid; // 徒弟id
	public int itemkey; // 物品的bagkey

	public CSelectedPrenticePassBook() {
	}

	public CSelectedPrenticePassBook(long _prenticeid_, int _itemkey_) {
		this.prenticeid = _prenticeid_;
		this.itemkey = _itemkey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(prenticeid);
		_os_.marshal(itemkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		prenticeid = _os_.unmarshal_long();
		itemkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSelectedPrenticePassBook) {
			CSelectedPrenticePassBook _o_ = (CSelectedPrenticePassBook)_o1_;
			if (prenticeid != _o_.prenticeid) return false;
			if (itemkey != _o_.itemkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)prenticeid;
		_h_ += itemkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prenticeid).append(",");
		_sb_.append(itemkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSelectedPrenticePassBook _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(prenticeid - _o_.prenticeid);
		if (0 != _c_) return _c_;
		_c_ = itemkey - _o_.itemkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

