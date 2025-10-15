
package gnet;



import fire.pb.fushi.FushiManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __DiscountAnnounce__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class DiscountAnnounce extends __DiscountAnnounce__ {
	@Override
	protected void process() {
		
		FushiManager.updateMerchantDiscount(discount);
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8064;

	public int getType() {
		return 8064;
	}

	public java.util.ArrayList<gnet.MerchantDiscount> discount;
	public int reserved1;
	public com.locojoy.base.Octets reserved2;

	public DiscountAnnounce() {
		discount = new java.util.ArrayList<gnet.MerchantDiscount>();
		reserved2 = new com.locojoy.base.Octets();
	}

	public DiscountAnnounce(java.util.ArrayList<gnet.MerchantDiscount> _discount_, int _reserved1_, com.locojoy.base.Octets _reserved2_) {
		this.discount = _discount_;
		this.reserved1 = _reserved1_;
		this.reserved2 = _reserved2_;
	}

	public final boolean _validator_() {
		for (gnet.MerchantDiscount _v_ : discount)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(discount.size());
		for (gnet.MerchantDiscount _v_ : discount) {
			_os_.marshal(_v_);
		}
		_os_.marshal(reserved1);
		_os_.marshal(reserved2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			gnet.MerchantDiscount _v_ = new gnet.MerchantDiscount();
			_v_.unmarshal(_os_);
			discount.add(_v_);
		}
		reserved1 = _os_.unmarshal_int();
		reserved2 = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof DiscountAnnounce) {
			DiscountAnnounce _o_ = (DiscountAnnounce)_o1_;
			if (!discount.equals(_o_.discount)) return false;
			if (reserved1 != _o_.reserved1) return false;
			if (!reserved2.equals(_o_.reserved2)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += discount.hashCode();
		_h_ += reserved1;
		_h_ += reserved2.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(discount).append(",");
		_sb_.append(reserved1).append(",");
		_sb_.append("B").append(reserved2.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

