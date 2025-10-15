
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBuyMedic__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBuyMedic extends __SBuyMedic__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808504;

	public int getType() {
		return 808504;
	}

	public int itemid; // 药品id
	public int itemnum;
	public int buyitemnum; // 当天已经购买数量

	public SBuyMedic() {
	}

	public SBuyMedic(int _itemid_, int _itemnum_, int _buyitemnum_) {
		this.itemid = _itemid_;
		this.itemnum = _itemnum_;
		this.buyitemnum = _buyitemnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(itemid);
		_os_.marshal(itemnum);
		_os_.marshal(buyitemnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemid = _os_.unmarshal_int();
		itemnum = _os_.unmarshal_int();
		buyitemnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBuyMedic) {
			SBuyMedic _o_ = (SBuyMedic)_o1_;
			if (itemid != _o_.itemid) return false;
			if (itemnum != _o_.itemnum) return false;
			if (buyitemnum != _o_.buyitemnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemid;
		_h_ += itemnum;
		_h_ += buyitemnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid).append(",");
		_sb_.append(itemnum).append(",");
		_sb_.append(buyitemnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SBuyMedic _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		_c_ = itemnum - _o_.itemnum;
		if (0 != _c_) return _c_;
		_c_ = buyitemnum - _o_.buyitemnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

