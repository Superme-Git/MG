
package fire.pb.fushi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqCharge__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqCharge extends __SReqCharge__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812455;

	public int getType() {
		return 812455;
	}

	public java.util.ArrayList<fire.pb.fushi.GoodInfo> goods;
	public int opendy; // 是否打开订阅 1代开 0关闭

	public SReqCharge() {
		goods = new java.util.ArrayList<fire.pb.fushi.GoodInfo>();
	}

	public SReqCharge(java.util.ArrayList<fire.pb.fushi.GoodInfo> _goods_, int _opendy_) {
		this.goods = _goods_;
		this.opendy = _opendy_;
	}

	public final boolean _validator_() {
		for (fire.pb.fushi.GoodInfo _v_ : goods)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(goods.size());
		for (fire.pb.fushi.GoodInfo _v_ : goods) {
			_os_.marshal(_v_);
		}
		_os_.marshal(opendy);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.fushi.GoodInfo _v_ = new fire.pb.fushi.GoodInfo();
			_v_.unmarshal(_os_);
			goods.add(_v_);
		}
		opendy = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqCharge) {
			SReqCharge _o_ = (SReqCharge)_o1_;
			if (!goods.equals(_o_.goods)) return false;
			if (opendy != _o_.opendy) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += goods.hashCode();
		_h_ += opendy;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(goods).append(",");
		_sb_.append(opendy).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

