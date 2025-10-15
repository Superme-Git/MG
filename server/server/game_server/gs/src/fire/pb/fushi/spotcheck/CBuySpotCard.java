
package fire.pb.fushi.spotcheck;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBuySpotCard__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CBuySpotCard extends __CBuySpotCard__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid < 0) {
			return;
		}
		new PBuySpotCard(roleid,buynum,buyprice).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812633;

	public int getType() {
		return 812633;
	}

	public int buynum; // 求购数量
	public int buyprice; // 求购单价

	public CBuySpotCard() {
	}

	public CBuySpotCard(int _buynum_, int _buyprice_) {
		this.buynum = _buynum_;
		this.buyprice = _buyprice_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(buynum);
		_os_.marshal(buyprice);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		buynum = _os_.unmarshal_int();
		buyprice = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CBuySpotCard) {
			CBuySpotCard _o_ = (CBuySpotCard)_o1_;
			if (buynum != _o_.buynum) return false;
			if (buyprice != _o_.buyprice) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += buynum;
		_h_ += buyprice;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(buynum).append(",");
		_sb_.append(buyprice).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CBuySpotCard _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = buynum - _o_.buynum;
		if (0 != _c_) return _c_;
		_c_ = buyprice - _o_.buyprice;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

