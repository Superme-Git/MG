
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SChangeClanInst__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SChangeClanInst extends __SChangeClanInst__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808525;

	public int getType() {
		return 808525;
	}

	public int claninstservice; // 进入副本服务编号

	public SChangeClanInst() {
	}

	public SChangeClanInst(int _claninstservice_) {
		this.claninstservice = _claninstservice_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(claninstservice);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		claninstservice = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SChangeClanInst) {
			SChangeClanInst _o_ = (SChangeClanInst)_o1_;
			if (claninstservice != _o_.claninstservice) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += claninstservice;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(claninstservice).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SChangeClanInst _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = claninstservice - _o_.claninstservice;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

