
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SQueryLimit__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SQueryLimit extends __SQueryLimit__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810638;

	public int getType() {
		return 810638;
	}

	public int querytype; // 查询类型   1商品限购次数查询  2商品限售次数查询
	public java.util.ArrayList<fire.pb.shop.GoodsLimit> goodslimits; // 查询商品id

	public SQueryLimit() {
		goodslimits = new java.util.ArrayList<fire.pb.shop.GoodsLimit>();
	}

	public SQueryLimit(int _querytype_, java.util.ArrayList<fire.pb.shop.GoodsLimit> _goodslimits_) {
		this.querytype = _querytype_;
		this.goodslimits = _goodslimits_;
	}

	public final boolean _validator_() {
		if (querytype < 1 || querytype > 2) return false;
		for (fire.pb.shop.GoodsLimit _v_ : goodslimits)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(querytype);
		_os_.compact_uint32(goodslimits.size());
		for (fire.pb.shop.GoodsLimit _v_ : goodslimits) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		querytype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.GoodsLimit _v_ = new fire.pb.shop.GoodsLimit();
			_v_.unmarshal(_os_);
			goodslimits.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SQueryLimit) {
			SQueryLimit _o_ = (SQueryLimit)_o1_;
			if (querytype != _o_.querytype) return false;
			if (!goodslimits.equals(_o_.goodslimits)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += querytype;
		_h_ += goodslimits.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(querytype).append(",");
		_sb_.append(goodslimits).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

