
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SShowedBeginnerTips__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SShowedBeginnerTips extends __SShowedBeginnerTips__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786461;

	public int getType() {
		return 786461;
	}

	public java.util.HashSet<Integer> tipid; // 已经显示过的tip id
	public int pilottype; // 适配引导类型

	public SShowedBeginnerTips() {
		tipid = new java.util.HashSet<Integer>();
	}

	public SShowedBeginnerTips(java.util.HashSet<Integer> _tipid_, int _pilottype_) {
		this.tipid = _tipid_;
		this.pilottype = _pilottype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(tipid.size());
		for (Integer _v_ : tipid) {
			_os_.marshal(_v_);
		}
		_os_.marshal(pilottype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			tipid.add(_v_);
		}
		pilottype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SShowedBeginnerTips) {
			SShowedBeginnerTips _o_ = (SShowedBeginnerTips)_o1_;
			if (!tipid.equals(_o_.tipid)) return false;
			if (pilottype != _o_.pilottype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += tipid.hashCode();
		_h_ += pilottype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tipid).append(",");
		_sb_.append(pilottype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

