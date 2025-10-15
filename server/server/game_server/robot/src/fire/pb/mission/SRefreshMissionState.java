
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshMissionState__ extends mkio.Protocol { }

/** 刷新剧情任务状态
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshMissionState extends __SRefreshMissionState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805447;

	public int getType() {
		return 805447;
	}

	public int missionid; // 任务id
	public int missionstatus; // 任务状态

	public SRefreshMissionState() {
	}

	public SRefreshMissionState(int _missionid_, int _missionstatus_) {
		this.missionid = _missionid_;
		this.missionstatus = _missionstatus_;
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
		_os_.marshal(missionstatus);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		missionid = _os_.unmarshal_int();
		missionstatus = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshMissionState) {
			SRefreshMissionState _o_ = (SRefreshMissionState)_o1_;
			if (missionid != _o_.missionid) return false;
			if (missionstatus != _o_.missionstatus) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += missionid;
		_h_ += missionstatus;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missionid).append(",");
		_sb_.append(missionstatus).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshMissionState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = missionid - _o_.missionid;
		if (0 != _c_) return _c_;
		_c_ = missionstatus - _o_.missionstatus;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

