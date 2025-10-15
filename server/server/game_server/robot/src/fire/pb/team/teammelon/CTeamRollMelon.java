
package fire.pb.team.teammelon;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CTeamRollMelon__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CTeamRollMelon extends __CTeamRollMelon__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794523;

	public int getType() {
		return 794523;
	}

	public long melonid; // 分赃id by changhao
	public int status; // 1是ROLL 0是放弃 by changhao

	public CTeamRollMelon() {
	}

	public CTeamRollMelon(long _melonid_, int _status_) {
		this.melonid = _melonid_;
		this.status = _status_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(melonid);
		_os_.marshal(status);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		melonid = _os_.unmarshal_long();
		status = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CTeamRollMelon) {
			CTeamRollMelon _o_ = (CTeamRollMelon)_o1_;
			if (melonid != _o_.melonid) return false;
			if (status != _o_.status) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)melonid;
		_h_ += status;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(melonid).append(",");
		_sb_.append(status).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CTeamRollMelon _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(melonid - _o_.melonid);
		if (0 != _c_) return _c_;
		_c_ = status - _o_.status;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

