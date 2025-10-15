
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SFormationsMap__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SFormationsMap extends __SFormationsMap__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794551;

	public int getType() {
		return 794551;
	}

	public java.util.HashMap<Integer,fire.pb.FormBean> formationmap; // key=光环id  value=光环等级

	public SFormationsMap() {
		formationmap = new java.util.HashMap<Integer,fire.pb.FormBean>();
	}

	public SFormationsMap(java.util.HashMap<Integer,fire.pb.FormBean> _formationmap_) {
		this.formationmap = _formationmap_;
	}

	public final boolean _validator_() {
		for (java.util.Map.Entry<Integer, fire.pb.FormBean> _e_ : formationmap.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(formationmap.size());
		for (java.util.Map.Entry<Integer, fire.pb.FormBean> _e_ : formationmap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.FormBean _v_ = new fire.pb.FormBean();
			_v_.unmarshal(_os_);
			formationmap.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SFormationsMap) {
			SFormationsMap _o_ = (SFormationsMap)_o1_;
			if (!formationmap.equals(_o_.formationmap)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += formationmap.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(formationmap).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

