
package fire.pb.circletask;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshSpecialQuestState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshSpecialQuestState extends __SRefreshSpecialQuestState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807433;

	public int getType() {
		return 807433;
	}

	public int questid;
	public int state;

	public SRefreshSpecialQuestState() {
	}

	public SRefreshSpecialQuestState(int _questid_, int _state_) {
		this.questid = _questid_;
		this.state = _state_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		_os_.marshal(state);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshSpecialQuestState) {
			SRefreshSpecialQuestState _o_ = (SRefreshSpecialQuestState)_o1_;
			if (questid != _o_.questid) return false;
			if (state != _o_.state) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		_h_ += state;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(state).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshSpecialQuestState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questid - _o_.questid;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

