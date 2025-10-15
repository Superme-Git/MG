
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshMissionValue__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshMissionValue extends __SRefreshMissionValue__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805449;

	public int getType() {
		return 805449;
	}

	public int missionid; // 任务id
	public int missionidvalue; // 任务状态值
	public int missionidround; // 任务的环数

	public SRefreshMissionValue() {
	}

	public SRefreshMissionValue(int _missionid_, int _missionidvalue_, int _missionidround_) {
		this.missionid = _missionid_;
		this.missionidvalue = _missionidvalue_;
		this.missionidround = _missionidround_;
	}

	public final boolean _validator_() {
		if (missionid < 1001 || missionid >  999999) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(missionid);
		_os_.marshal(missionidvalue);
		_os_.marshal(missionidround);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		missionid = _os_.unmarshal_int();
		missionidvalue = _os_.unmarshal_int();
		missionidround = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshMissionValue) {
			SRefreshMissionValue _o_ = (SRefreshMissionValue)_o1_;
			if (missionid != _o_.missionid) return false;
			if (missionidvalue != _o_.missionidvalue) return false;
			if (missionidround != _o_.missionidround) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += missionid;
		_h_ += missionidvalue;
		_h_ += missionidround;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missionid).append(",");
		_sb_.append(missionidvalue).append(",");
		_sb_.append(missionidround).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshMissionValue _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = missionid - _o_.missionid;
		if (0 != _c_) return _c_;
		_c_ = missionidvalue - _o_.missionidvalue;
		if (0 != _c_) return _c_;
		_c_ = missionidround - _o_.missionidround;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

