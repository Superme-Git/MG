
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SXiuLiFailTimes__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SXiuLiFailTimes extends __SXiuLiFailTimes__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787477;

	public int getType() {
		return 787477;
	}

	public int packid;
	public int keyinpack;
	public int failtimes;

	public SXiuLiFailTimes() {
	}

	public SXiuLiFailTimes(int _packid_, int _keyinpack_, int _failtimes_) {
		this.packid = _packid_;
		this.keyinpack = _keyinpack_;
		this.failtimes = _failtimes_;
	}

	public final boolean _validator_() {
		if (packid < 1) return false;
		if (failtimes < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(packid);
		_os_.marshal(keyinpack);
		_os_.marshal(failtimes);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packid = _os_.unmarshal_int();
		keyinpack = _os_.unmarshal_int();
		failtimes = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SXiuLiFailTimes) {
			SXiuLiFailTimes _o_ = (SXiuLiFailTimes)_o1_;
			if (packid != _o_.packid) return false;
			if (keyinpack != _o_.keyinpack) return false;
			if (failtimes != _o_.failtimes) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packid;
		_h_ += keyinpack;
		_h_ += failtimes;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packid).append(",");
		_sb_.append(keyinpack).append(",");
		_sb_.append(failtimes).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SXiuLiFailTimes _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = packid - _o_.packid;
		if (0 != _c_) return _c_;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		_c_ = failtimes - _o_.failtimes;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

