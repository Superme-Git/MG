
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CQueryLimit__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CQueryLimit extends __CQueryLimit__ {
	@Override
	protected void process() {
		// protocol handle
		final gnet.link.Role role = gnet.link.Onlines.getInstance().find(this);
		if (role == null)
			return;
		long roleId = role.getRoleid();
		
		new PQueryLimit(roleId, querytype, goodsids).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810637;

	public int getType() {
		return 810637;
	}

	public int querytype; // 查询类型  1商品限购次数查询  2商品限售次数查询
	public java.util.ArrayList<Integer> goodsids; // 查询商品id

	public CQueryLimit() {
		goodsids = new java.util.ArrayList<Integer>();
	}

	public CQueryLimit(int _querytype_, java.util.ArrayList<Integer> _goodsids_) {
		this.querytype = _querytype_;
		this.goodsids = _goodsids_;
	}

	public final boolean _validator_() {
		if (querytype < 1 || querytype > 2) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(querytype);
		_os_.compact_uint32(goodsids.size());
		for (Integer _v_ : goodsids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		querytype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			goodsids.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CQueryLimit) {
			CQueryLimit _o_ = (CQueryLimit)_o1_;
			if (querytype != _o_.querytype) return false;
			if (!goodsids.equals(_o_.goodsids)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += querytype;
		_h_ += goodsids.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(querytype).append(",");
		_sb_.append(goodsids).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

