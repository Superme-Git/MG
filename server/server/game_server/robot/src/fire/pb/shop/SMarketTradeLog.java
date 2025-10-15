
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SMarketTradeLog__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SMarketTradeLog extends __SMarketTradeLog__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810646;

	public int getType() {
		return 810646;
	}

	public java.util.ArrayList<fire.pb.shop.LogBean> buylog; // 买记录
	public java.util.ArrayList<fire.pb.shop.LogBean> salelog; // 卖记录

	public SMarketTradeLog() {
		buylog = new java.util.ArrayList<fire.pb.shop.LogBean>();
		salelog = new java.util.ArrayList<fire.pb.shop.LogBean>();
	}

	public SMarketTradeLog(java.util.ArrayList<fire.pb.shop.LogBean> _buylog_, java.util.ArrayList<fire.pb.shop.LogBean> _salelog_) {
		this.buylog = _buylog_;
		this.salelog = _salelog_;
	}

	public final boolean _validator_() {
		for (fire.pb.shop.LogBean _v_ : buylog)
			if (!_v_._validator_()) return false;
		for (fire.pb.shop.LogBean _v_ : salelog)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(buylog.size());
		for (fire.pb.shop.LogBean _v_ : buylog) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(salelog.size());
		for (fire.pb.shop.LogBean _v_ : salelog) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.LogBean _v_ = new fire.pb.shop.LogBean();
			_v_.unmarshal(_os_);
			buylog.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.LogBean _v_ = new fire.pb.shop.LogBean();
			_v_.unmarshal(_os_);
			salelog.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SMarketTradeLog) {
			SMarketTradeLog _o_ = (SMarketTradeLog)_o1_;
			if (!buylog.equals(_o_.buylog)) return false;
			if (!salelog.equals(_o_.salelog)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += buylog.hashCode();
		_h_ += salelog.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(buylog).append(",");
		_sb_.append(salelog).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

