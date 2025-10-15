
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMarketAttentionBrowse__ extends mkio.Protocol { }

/** 摆摊关注浏览
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMarketAttentionBrowse extends __CMarketAttentionBrowse__ {
	@Override
	protected void process() {
		// protocol handle
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				return fire.pb.shop.srv.market.MarketManager.getInstance().attentionBrowse(roleId, attentype);
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810660;

	public int getType() {
		return 810660;
	}

	public int attentype; // 关注状态 1购买，2公示

	public CMarketAttentionBrowse() {
	}

	public CMarketAttentionBrowse(int _attentype_) {
		this.attentype = _attentype_;
	}

	public final boolean _validator_() {
		if (attentype < 1 || attentype > 3) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(attentype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		attentype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMarketAttentionBrowse) {
			CMarketAttentionBrowse _o_ = (CMarketAttentionBrowse)_o1_;
			if (attentype != _o_.attentype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += attentype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(attentype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CMarketAttentionBrowse _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = attentype - _o_.attentype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

