
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMarketDown__ extends mkio.Protocol { }

/** 摆摊下架
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMarketDown extends __CMarketDown__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810644;

	public int getType() {
		return 810644;
	}

	public int downtype; // 下架类型1道具,2宠物
	public int key; // 摆摊容器中的key

	public CMarketDown() {
	}

	public CMarketDown(int _downtype_, int _key_) {
		this.downtype = _downtype_;
		this.key = _key_;
	}

	public final boolean _validator_() {
		if (downtype < 1 || downtype > 2) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(downtype);
		_os_.marshal(key);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		downtype = _os_.unmarshal_int();
		key = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMarketDown) {
			CMarketDown _o_ = (CMarketDown)_o1_;
			if (downtype != _o_.downtype) return false;
			if (key != _o_.key) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += downtype;
		_h_ += key;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(downtype).append(",");
		_sb_.append(key).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CMarketDown _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = downtype - _o_.downtype;
		if (0 != _c_) return _c_;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

