
package fire.pb.product;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SProductMadeUp__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SProductMadeUp extends __SProductMadeUp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 803442;

	public int getType() {
		return 803442;
	}

	public int maketype; // type为1代表是打造生成的物品，为2是炼药练成的药品，为3代表是烹饪成功的食品
	public int itemkey;

	public SProductMadeUp() {
	}

	public SProductMadeUp(int _maketype_, int _itemkey_) {
		this.maketype = _maketype_;
		this.itemkey = _itemkey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(maketype);
		_os_.marshal(itemkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		maketype = _os_.unmarshal_int();
		itemkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SProductMadeUp) {
			SProductMadeUp _o_ = (SProductMadeUp)_o1_;
			if (maketype != _o_.maketype) return false;
			if (itemkey != _o_.itemkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += maketype;
		_h_ += itemkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(maketype).append(",");
		_sb_.append(itemkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SProductMadeUp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = maketype - _o_.maketype;
		if (0 != _c_) return _c_;
		_c_ = itemkey - _o_.itemkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

