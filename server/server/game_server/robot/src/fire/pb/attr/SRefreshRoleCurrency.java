
package fire.pb.attr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshRoleCurrency__ extends mkio.Protocol { }

/** 刷新人物通货的消息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshRoleCurrency extends __SRefreshRoleCurrency__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 799437;

	public int getType() {
		return 799437;
	}

	public java.util.HashMap<Integer,Long> datas; // 变量值ID，变量值

	public SRefreshRoleCurrency() {
		datas = new java.util.HashMap<Integer,Long>();
	}

	public SRefreshRoleCurrency(java.util.HashMap<Integer,Long> _datas_) {
		this.datas = _datas_;
	}

	public final boolean _validator_() {
		for (java.util.Map.Entry<Integer, Long> _e_ : datas.entrySet()) {
			if (_e_.getKey().intValue() <= 0) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(datas.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : datas.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			long _v_;
			_v_ = _os_.unmarshal_long();
			datas.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshRoleCurrency) {
			SRefreshRoleCurrency _o_ = (SRefreshRoleCurrency)_o1_;
			if (!datas.equals(_o_.datas)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += datas.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(datas).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

