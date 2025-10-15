
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPreviousMasters__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPreviousMasters extends __SPreviousMasters__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816451;

	public int getType() {
		return 816451;
	}

	public java.util.HashMap<Long,fire.pb.master.MasterPrenticeBaseData> masters;

	public SPreviousMasters() {
		masters = new java.util.HashMap<Long,fire.pb.master.MasterPrenticeBaseData>();
	}

	public SPreviousMasters(java.util.HashMap<Long,fire.pb.master.MasterPrenticeBaseData> _masters_) {
		this.masters = _masters_;
	}

	public final boolean _validator_() {
		for (java.util.Map.Entry<Long, fire.pb.master.MasterPrenticeBaseData> _e_ : masters.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(masters.size());
		for (java.util.Map.Entry<Long, fire.pb.master.MasterPrenticeBaseData> _e_ : masters.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _k_;
			_k_ = _os_.unmarshal_long();
			fire.pb.master.MasterPrenticeBaseData _v_ = new fire.pb.master.MasterPrenticeBaseData();
			_v_.unmarshal(_os_);
			masters.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPreviousMasters) {
			SPreviousMasters _o_ = (SPreviousMasters)_o1_;
			if (!masters.equals(_o_.masters)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += masters.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(masters).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

