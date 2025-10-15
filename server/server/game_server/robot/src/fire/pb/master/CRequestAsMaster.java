
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestAsMaster__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestAsMaster extends __CRequestAsMaster__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816458;

	public int getType() {
		return 816458;
	}

	public long prenticeid; // 请求徒弟的id

	public CRequestAsMaster() {
	}

	public CRequestAsMaster(long _prenticeid_) {
		this.prenticeid = _prenticeid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(prenticeid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		prenticeid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestAsMaster) {
			CRequestAsMaster _o_ = (CRequestAsMaster)_o1_;
			if (prenticeid != _o_.prenticeid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)prenticeid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prenticeid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestAsMaster _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(prenticeid - _o_.prenticeid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

