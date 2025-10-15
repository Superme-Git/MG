
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshItemTimeout__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshItemTimeout extends __SRefreshItemTimeout__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787438;

	public int getType() {
		return 787438;
	}

	public int packid;
	public int itemkey;
	public long timeout;

	public SRefreshItemTimeout() {
	}

	public SRefreshItemTimeout(int _packid_, int _itemkey_, long _timeout_) {
		this.packid = _packid_;
		this.itemkey = _itemkey_;
		this.timeout = _timeout_;
	}

	public final boolean _validator_() {
		if (packid <= 0) return false;
		if (itemkey <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(packid);
		_os_.marshal(itemkey);
		_os_.marshal(timeout);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packid = _os_.unmarshal_int();
		itemkey = _os_.unmarshal_int();
		timeout = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshItemTimeout) {
			SRefreshItemTimeout _o_ = (SRefreshItemTimeout)_o1_;
			if (packid != _o_.packid) return false;
			if (itemkey != _o_.itemkey) return false;
			if (timeout != _o_.timeout) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packid;
		_h_ += itemkey;
		_h_ += (int)timeout;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packid).append(",");
		_sb_.append(itemkey).append(",");
		_sb_.append(timeout).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshItemTimeout _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = packid - _o_.packid;
		if (0 != _c_) return _c_;
		_c_ = itemkey - _o_.itemkey;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(timeout - _o_.timeout);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

