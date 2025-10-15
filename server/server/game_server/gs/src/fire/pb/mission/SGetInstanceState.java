
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetInstanceState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetInstanceState extends __SGetInstanceState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805473;

	public int getType() {
		return 805473;
	}

	public java.util.HashMap<Integer,fire.pb.mission.InstanceInfo> instances;

	public SGetInstanceState() {
		instances = new java.util.HashMap<Integer,fire.pb.mission.InstanceInfo>();
	}

	public SGetInstanceState(java.util.HashMap<Integer,fire.pb.mission.InstanceInfo> _instances_) {
		this.instances = _instances_;
	}

	public final boolean _validator_() {
		for (java.util.Map.Entry<Integer, fire.pb.mission.InstanceInfo> _e_ : instances.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(instances.size());
		for (java.util.Map.Entry<Integer, fire.pb.mission.InstanceInfo> _e_ : instances.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.mission.InstanceInfo _v_ = new fire.pb.mission.InstanceInfo();
			_v_.unmarshal(_os_);
			instances.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetInstanceState) {
			SGetInstanceState _o_ = (SGetInstanceState)_o1_;
			if (!instances.equals(_o_.instances)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += instances.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(instances).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

