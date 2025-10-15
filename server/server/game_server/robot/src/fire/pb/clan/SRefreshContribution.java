
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshContribution__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshContribution extends __SRefreshContribution__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808497;

	public int getType() {
		return 808497;
	}

	public int currentcontribution; // 当前帮贡

	public SRefreshContribution() {
	}

	public SRefreshContribution(int _currentcontribution_) {
		this.currentcontribution = _currentcontribution_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(currentcontribution);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		currentcontribution = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshContribution) {
			SRefreshContribution _o_ = (SRefreshContribution)_o1_;
			if (currentcontribution != _o_.currentcontribution) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += currentcontribution;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(currentcontribution).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshContribution _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = currentcontribution - _o_.currentcontribution;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

