
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CTrackMission__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CTrackMission extends __CTrackMission__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805450;

	public int getType() {
		return 805450;
	}

	public int missionid; // 任务id
	public int track; // 任务追踪

	public CTrackMission() {
	}

	public CTrackMission(int _missionid_, int _track_) {
		this.missionid = _missionid_;
		this.track = _track_;
	}

	public final boolean _validator_() {
		if (track < 0 || track > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(missionid);
		_os_.marshal(track);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		missionid = _os_.unmarshal_int();
		track = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CTrackMission) {
			CTrackMission _o_ = (CTrackMission)_o1_;
			if (missionid != _o_.missionid) return false;
			if (track != _o_.track) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += missionid;
		_h_ += track;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missionid).append(",");
		_sb_.append(track).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CTrackMission _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = missionid - _o_.missionid;
		if (0 != _c_) return _c_;
		_c_ = track - _o_.track;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

