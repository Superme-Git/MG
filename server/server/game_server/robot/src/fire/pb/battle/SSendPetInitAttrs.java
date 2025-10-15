
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendPetInitAttrs__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendPetInitAttrs extends __SSendPetInitAttrs__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793456;

	public int getType() {
		return 793456;
	}

	public java.util.HashMap<Integer,Float> petinitattrs; // 进战斗时主角宠物的二级属性

	public SSendPetInitAttrs() {
		petinitattrs = new java.util.HashMap<Integer,Float>();
	}

	public SSendPetInitAttrs(java.util.HashMap<Integer,Float> _petinitattrs_) {
		this.petinitattrs = _petinitattrs_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(petinitattrs.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : petinitattrs.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			float _v_;
			_v_ = _os_.unmarshal_float();
			petinitattrs.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendPetInitAttrs) {
			SSendPetInitAttrs _o_ = (SSendPetInitAttrs)_o1_;
			if (!petinitattrs.equals(_o_.petinitattrs)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petinitattrs.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petinitattrs).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

