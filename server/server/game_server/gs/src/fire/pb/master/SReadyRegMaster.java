
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReadyRegMaster__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReadyRegMaster extends __SReadyRegMaster__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816434;

	public int getType() {
		return 816434;
	}

	public java.util.ArrayList<fire.pb.master.Master> masters;

	public SReadyRegMaster() {
		masters = new java.util.ArrayList<fire.pb.master.Master>();
	}

	public SReadyRegMaster(java.util.ArrayList<fire.pb.master.Master> _masters_) {
		this.masters = _masters_;
	}

	public final boolean _validator_() {
		for (fire.pb.master.Master _v_ : masters)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(masters.size());
		for (fire.pb.master.Master _v_ : masters) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.master.Master _v_ = new fire.pb.master.Master();
			_v_.unmarshal(_os_);
			masters.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReadyRegMaster) {
			SReadyRegMaster _o_ = (SReadyRegMaster)_o1_;
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

