
package fire.pb.skill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUpdateInborn__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUpdateInborn extends __SUpdateInborn__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800446;

	public int getType() {
		return 800446;
	}

	public byte flag; // 0默认方式  1=一键
	public java.util.HashMap<Integer,Integer> inborns; // key = ID，value=等级。里面没有的为0级

	public SUpdateInborn() {
		inborns = new java.util.HashMap<Integer,Integer>();
	}

	public SUpdateInborn(byte _flag_, java.util.HashMap<Integer,Integer> _inborns_) {
		this.flag = _flag_;
		this.inborns = _inborns_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(flag);
		_os_.compact_uint32(inborns.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : inborns.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		flag = _os_.unmarshal_byte();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			inborns.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUpdateInborn) {
			SUpdateInborn _o_ = (SUpdateInborn)_o1_;
			if (flag != _o_.flag) return false;
			if (!inborns.equals(_o_.inborns)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)flag;
		_h_ += inborns.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(flag).append(",");
		_sb_.append(inborns).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

