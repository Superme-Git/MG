
package fire.pb;
import gnet.link.Onlines;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetLineConfig__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetLineConfig extends __CSetLineConfig__ {
	@Override
	protected void process() {
		final long roleid = Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		new PSetLineConfig(roleid, this).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786551;

	public int getType() {
		return 786551;
	}

	public java.util.HashMap<Integer,Integer> configmap;

	public CSetLineConfig() {
		configmap = new java.util.HashMap<Integer,Integer>();
	}

	public CSetLineConfig(java.util.HashMap<Integer,Integer> _configmap_) {
		this.configmap = _configmap_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(configmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : configmap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			configmap.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetLineConfig) {
			CSetLineConfig _o_ = (CSetLineConfig)_o1_;
			if (!configmap.equals(_o_.configmap)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += configmap.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(configmap).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

