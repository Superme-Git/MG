
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STrackedMissions__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STrackedMissions extends __STrackedMissions__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805461;

	public int getType() {
		return 805461;
	}

	public java.util.HashMap<Integer,fire.pb.mission.TrackedMission> trackedmissions; // 任务id

	public STrackedMissions() {
		trackedmissions = new java.util.HashMap<Integer,fire.pb.mission.TrackedMission>();
	}

	public STrackedMissions(java.util.HashMap<Integer,fire.pb.mission.TrackedMission> _trackedmissions_) {
		this.trackedmissions = _trackedmissions_;
	}

	public final boolean _validator_() {
		for (java.util.Map.Entry<Integer, fire.pb.mission.TrackedMission> _e_ : trackedmissions.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(trackedmissions.size());
		for (java.util.Map.Entry<Integer, fire.pb.mission.TrackedMission> _e_ : trackedmissions.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.mission.TrackedMission _v_ = new fire.pb.mission.TrackedMission();
			_v_.unmarshal(_os_);
			trackedmissions.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STrackedMissions) {
			STrackedMissions _o_ = (STrackedMissions)_o1_;
			if (!trackedmissions.equals(_o_.trackedmissions)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += trackedmissions.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(trackedmissions).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

