
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SCloseGoodLocks__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SCloseGoodLocks extends __SCloseGoodLocks__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786579;

	public int getType() {
		return 786579;
	}

	public byte status;
	public byte closetype;

	public SCloseGoodLocks() {
	}

	public SCloseGoodLocks(byte _status_, byte _closetype_) {
		this.status = _status_;
		this.closetype = _closetype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(status);
		_os_.marshal(closetype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		status = _os_.unmarshal_byte();
		closetype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SCloseGoodLocks) {
			SCloseGoodLocks _o_ = (SCloseGoodLocks)_o1_;
			if (status != _o_.status) return false;
			if (closetype != _o_.closetype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)status;
		_h_ += (int)closetype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(status).append(",");
		_sb_.append(closetype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SCloseGoodLocks _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = status - _o_.status;
		if (0 != _c_) return _c_;
		_c_ = closetype - _o_.closetype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

