
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetMarketUpPrice__ extends mkio.Protocol { }

/** 获取摆摊上架价格
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetMarketUpPrice extends __CGetMarketUpPrice__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810651;

	public int getType() {
		return 810651;
	}

	public int containertype; // 1背包 2宠物栏 3拍卖宠物栏 4拍卖背包
	public int key; // 摆摊容器中的key

	public CGetMarketUpPrice() {
	}

	public CGetMarketUpPrice(int _containertype_, int _key_) {
		this.containertype = _containertype_;
		this.key = _key_;
	}

	public final boolean _validator_() {
		if (containertype < 1 || containertype > 4) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(containertype);
		_os_.marshal(key);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		containertype = _os_.unmarshal_int();
		key = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetMarketUpPrice) {
			CGetMarketUpPrice _o_ = (CGetMarketUpPrice)_o1_;
			if (containertype != _o_.containertype) return false;
			if (key != _o_.key) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += containertype;
		_h_ += key;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(containertype).append(",");
		_sb_.append(key).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetMarketUpPrice _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = containertype - _o_.containertype;
		if (0 != _c_) return _c_;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

