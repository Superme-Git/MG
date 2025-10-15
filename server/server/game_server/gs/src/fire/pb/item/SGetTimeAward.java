
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetTimeAward__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetTimeAward extends __SGetTimeAward__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787507;

	public int getType() {
		return 787507;
	}

	public int awardid;
	public long waittime;

	public SGetTimeAward() {
	}

	public SGetTimeAward(int _awardid_, long _waittime_) {
		this.awardid = _awardid_;
		this.waittime = _waittime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(awardid);
		_os_.marshal(waittime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		awardid = _os_.unmarshal_int();
		waittime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetTimeAward) {
			SGetTimeAward _o_ = (SGetTimeAward)_o1_;
			if (awardid != _o_.awardid) return false;
			if (waittime != _o_.waittime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += awardid;
		_h_ += (int)waittime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(awardid).append(",");
		_sb_.append(waittime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SGetTimeAward _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = awardid - _o_.awardid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(waittime - _o_.waittime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

