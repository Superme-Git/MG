
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetActivityInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetActivityInfo extends __SGetActivityInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805537;

	public int getType() {
		return 805537;
	}

	public java.util.ArrayList<fire.pb.mission.ActivityInfo> activityinfos; // 活动信息

	public SGetActivityInfo() {
		activityinfos = new java.util.ArrayList<fire.pb.mission.ActivityInfo>();
	}

	public SGetActivityInfo(java.util.ArrayList<fire.pb.mission.ActivityInfo> _activityinfos_) {
		this.activityinfos = _activityinfos_;
	}

	public final boolean _validator_() {
		for (fire.pb.mission.ActivityInfo _v_ : activityinfos)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(activityinfos.size());
		for (fire.pb.mission.ActivityInfo _v_ : activityinfos) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.mission.ActivityInfo _v_ = new fire.pb.mission.ActivityInfo();
			_v_.unmarshal(_os_);
			activityinfos.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetActivityInfo) {
			SGetActivityInfo _o_ = (SGetActivityInfo)_o1_;
			if (!activityinfos.equals(_o_.activityinfos)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += activityinfos.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(activityinfos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

