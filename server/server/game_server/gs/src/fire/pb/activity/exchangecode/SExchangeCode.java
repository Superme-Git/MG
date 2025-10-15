
package fire.pb.activity.exchangecode;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SExchangeCode__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SExchangeCode extends __SExchangeCode__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 819199;

	public int getType() {
		return 819199;
	}

	public int flag; // 1 = 成功
	public java.util.ArrayList<fire.pb.activity.exchangecode.ExchangeCodeRetInfo> iteminfos;

	public SExchangeCode() {
		iteminfos = new java.util.ArrayList<fire.pb.activity.exchangecode.ExchangeCodeRetInfo>();
	}

	public SExchangeCode(int _flag_, java.util.ArrayList<fire.pb.activity.exchangecode.ExchangeCodeRetInfo> _iteminfos_) {
		this.flag = _flag_;
		this.iteminfos = _iteminfos_;
	}

	public final boolean _validator_() {
		for (fire.pb.activity.exchangecode.ExchangeCodeRetInfo _v_ : iteminfos)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(flag);
		_os_.compact_uint32(iteminfos.size());
		for (fire.pb.activity.exchangecode.ExchangeCodeRetInfo _v_ : iteminfos) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		flag = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.activity.exchangecode.ExchangeCodeRetInfo _v_ = new fire.pb.activity.exchangecode.ExchangeCodeRetInfo();
			_v_.unmarshal(_os_);
			iteminfos.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SExchangeCode) {
			SExchangeCode _o_ = (SExchangeCode)_o1_;
			if (flag != _o_.flag) return false;
			if (!iteminfos.equals(_o_.iteminfos)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += flag;
		_h_ += iteminfos.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(flag).append(",");
		_sb_.append(iteminfos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

