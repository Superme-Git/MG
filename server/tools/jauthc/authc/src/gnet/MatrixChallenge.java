
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MatrixChallenge__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MatrixChallenge extends __MatrixChallenge__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 551;

	public int getType() {
		return 551;
	}

	public int algorithm;
	public int nonce;

	public MatrixChallenge() {
		nonce = 0;
	}

	public MatrixChallenge(int _algorithm_, int _nonce_) {
		this.algorithm = _algorithm_;
		this.nonce = _nonce_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(algorithm);
		_os_.marshal(nonce);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		algorithm = _os_.unmarshal_int();
		nonce = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MatrixChallenge) {
			MatrixChallenge _o_ = (MatrixChallenge)_o1_;
			if (algorithm != _o_.algorithm) return false;
			if (nonce != _o_.nonce) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += algorithm;
		_h_ += nonce;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(algorithm).append(",");
		_sb_.append(nonce).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MatrixChallenge _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = algorithm - _o_.algorithm;
		if (0 != _c_) return _c_;
		_c_ = nonce - _o_.nonce;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

