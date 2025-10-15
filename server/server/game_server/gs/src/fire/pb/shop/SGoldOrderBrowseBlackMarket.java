
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGoldOrderBrowseBlackMarket__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGoldOrderBrowseBlackMarket extends __SGoldOrderBrowseBlackMarket__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810674;

	public int getType() {
		return 810674;
	}

	public java.util.ArrayList<fire.pb.shop.GoldOrder> salelist; // 售卖列表
	public java.util.ArrayList<fire.pb.shop.GoldOrder> buylist; // 购买列表

	public SGoldOrderBrowseBlackMarket() {
		salelist = new java.util.ArrayList<fire.pb.shop.GoldOrder>();
		buylist = new java.util.ArrayList<fire.pb.shop.GoldOrder>();
	}

	public SGoldOrderBrowseBlackMarket(java.util.ArrayList<fire.pb.shop.GoldOrder> _salelist_, java.util.ArrayList<fire.pb.shop.GoldOrder> _buylist_) {
		this.salelist = _salelist_;
		this.buylist = _buylist_;
	}

	public final boolean _validator_() {
		for (fire.pb.shop.GoldOrder _v_ : salelist)
			if (!_v_._validator_()) return false;
		for (fire.pb.shop.GoldOrder _v_ : buylist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(salelist.size());
		for (fire.pb.shop.GoldOrder _v_ : salelist) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(buylist.size());
		for (fire.pb.shop.GoldOrder _v_ : buylist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.GoldOrder _v_ = new fire.pb.shop.GoldOrder();
			_v_.unmarshal(_os_);
			salelist.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.GoldOrder _v_ = new fire.pb.shop.GoldOrder();
			_v_.unmarshal(_os_);
			buylist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGoldOrderBrowseBlackMarket) {
			SGoldOrderBrowseBlackMarket _o_ = (SGoldOrderBrowseBlackMarket)_o1_;
			if (!salelist.equals(_o_.salelist)) return false;
			if (!buylist.equals(_o_.buylist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += salelist.hashCode();
		_h_ += buylist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(salelist).append(",");
		_sb_.append(buylist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

