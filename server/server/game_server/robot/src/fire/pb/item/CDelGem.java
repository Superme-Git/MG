
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CDelGem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CDelGem extends __CDelGem__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787702;

	public int getType() {
		return 787702;
	}

	public int keyinpack;
	public byte isequip;
	public int gempos;

	public CDelGem() {
	}

	public CDelGem(int _keyinpack_, byte _isequip_, int _gempos_) {
		this.keyinpack = _keyinpack_;
		this.isequip = _isequip_;
		this.gempos = _gempos_;
	}

	public final boolean _validator_() {
		if (keyinpack < 1) return false;
		if (gempos < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(keyinpack);
		_os_.marshal(isequip);
		_os_.marshal(gempos);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		keyinpack = _os_.unmarshal_int();
		isequip = _os_.unmarshal_byte();
		gempos = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CDelGem) {
			CDelGem _o_ = (CDelGem)_o1_;
			if (keyinpack != _o_.keyinpack) return false;
			if (isequip != _o_.isequip) return false;
			if (gempos != _o_.gempos) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += keyinpack;
		_h_ += (int)isequip;
		_h_ += gempos;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(keyinpack).append(",");
		_sb_.append(isequip).append(",");
		_sb_.append(gempos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CDelGem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = keyinpack - _o_.keyinpack;
		if (0 != _c_) return _c_;
		_c_ = isequip - _o_.isequip;
		if (0 != _c_) return _c_;
		_c_ = gempos - _o_.gempos;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

