
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMasterRequestResult__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMasterRequestResult extends __CMasterRequestResult__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816460;

	public int getType() {
		return 816460;
	}

	public final static int REFUSE = 0; // 拒绝
	public final static int ACCEPT = 1; // 接受
	public final static int OVERTIME = 2; // 超时

	public long masterid; // 徒弟id
	public int result; // 结果

	public CMasterRequestResult() {
	}

	public CMasterRequestResult(long _masterid_, int _result_) {
		this.masterid = _masterid_;
		this.result = _result_;
	}

	public final boolean _validator_() {
		if (result < 0 || result >= 3) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(masterid);
		_os_.marshal(result);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		masterid = _os_.unmarshal_long();
		result = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMasterRequestResult) {
			CMasterRequestResult _o_ = (CMasterRequestResult)_o1_;
			if (masterid != _o_.masterid) return false;
			if (result != _o_.result) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)masterid;
		_h_ += result;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(masterid).append(",");
		_sb_.append(result).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CMasterRequestResult _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(masterid - _o_.masterid);
		if (0 != _c_) return _c_;
		_c_ = result - _o_.result;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

