
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshMaxNaiJiu__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshMaxNaiJiu extends __SRefreshMaxNaiJiu__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787476;

	public int getType() {
		return 787476;
	}

	public int packid;
	public int keyinpack;
	public int maxendure;

	public SRefreshMaxNaiJiu() {
	}

	public SRefreshMaxNaiJiu(int _packid_, int _keyinpack_, int _maxendure_) {
		this.packid = _packid_;
		this.keyinpack = _keyinpack_;
		this.maxendure = _maxendure_;
	}

	public final boolean _validator_() {
		if (packid < 1) return false;
		if (maxendure < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(packid);
		_os_.marshal(keyinpack);
		_os_.marshal(maxendure);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packid = _os_.unmarshal_int();
		keyinpack = _os_.unmarshal_int();
		maxendure = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshMaxNaiJiu) {
			SRefreshMaxNaiJiu _o_ = (SRefreshMaxNaiJiu)_o1_;
			if (packid != _o_.packid) return false;
			if (keyinpack != _o_.keyinpack) return false;
			if (maxendure != _o_.maxendure) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packid;
		_h_ += keyinpack;
		_h_ += maxendure;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packid).append(",");
		_sb_.append(keyinpack).append(",");
		_sb_.append(maxendure).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshMaxNaiJiu _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = packid - _o_.packid;
		if (0 != _c_) return _c_;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		_c_ = maxendure - _o_.maxendure;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

