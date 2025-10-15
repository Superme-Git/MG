
package fire.msp.task;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MScenarioQuestVerifyPosFail__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MScenarioQuestVerifyPosFail extends __MScenarioQuestVerifyPosFail__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 739908;

	public int getType() {
		return 739908;
	}

	public long roleid;
	public int scenarioquestid;

	public MScenarioQuestVerifyPosFail() {
	}

	public MScenarioQuestVerifyPosFail(long _roleid_, int _scenarioquestid_) {
		this.roleid = _roleid_;
		this.scenarioquestid = _scenarioquestid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(scenarioquestid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		scenarioquestid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MScenarioQuestVerifyPosFail) {
			MScenarioQuestVerifyPosFail _o_ = (MScenarioQuestVerifyPosFail)_o1_;
			if (roleid != _o_.roleid) return false;
			if (scenarioquestid != _o_.scenarioquestid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += scenarioquestid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(scenarioquestid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MScenarioQuestVerifyPosFail _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = scenarioquestid - _o_.scenarioquestid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

