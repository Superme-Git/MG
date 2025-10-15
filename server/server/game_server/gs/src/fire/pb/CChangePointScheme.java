
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CChangePointScheme__ extends mkio.Protocol { }

/** 客户端请求切换加点方案
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CChangePointScheme extends __CChangePointScheme__ {
	@Override
	protected void process() {
		// protocol handle
		// 数据合法性检查
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId <= 0)
			return;

		if (schemeid < 0 || schemeid == 0){
			return;
		}
		
		final xbean.Properties prop = xtable.Properties.select(roleId);
		if (null == prop)
			return;
		
		// 切换加点方案
		final PChangePointProc changepoint = new PChangePointProc(roleId, schemeid);
		changepoint.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786530;

	public int getType() {
		return 786530;
	}

	public int schemeid;

	public CChangePointScheme() {
	}

	public CChangePointScheme(int _schemeid_) {
		this.schemeid = _schemeid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(schemeid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		schemeid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CChangePointScheme) {
			CChangePointScheme _o_ = (CChangePointScheme)_o1_;
			if (schemeid != _o_.schemeid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += schemeid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(schemeid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CChangePointScheme _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = schemeid - _o_.schemeid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

