
package fire.pb.fushi.spotcheck;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSellSpotCard__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSellSpotCard extends __CSellSpotCard__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812635;

	public int getType() {
		return 812635;
	}

	public int sellnum; // 寄售数量
	public int sellprice; // 寄售单价

	public CSellSpotCard() {
	}

	public CSellSpotCard(int _sellnum_, int _sellprice_) {
		this.sellnum = _sellnum_;
		this.sellprice = _sellprice_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(sellnum);
		_os_.marshal(sellprice);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		sellnum = _os_.unmarshal_int();
		sellprice = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSellSpotCard) {
			CSellSpotCard _o_ = (CSellSpotCard)_o1_;
			if (sellnum != _o_.sellnum) return false;
			if (sellprice != _o_.sellprice) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += sellnum;
		_h_ += sellprice;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(sellnum).append(",");
		_sb_.append(sellprice).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSellSpotCard _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = sellnum - _o_.sellnum;
		if (0 != _c_) return _c_;
		_c_ = sellprice - _o_.sellprice;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

