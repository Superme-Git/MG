
package fire.pb.huoban;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSwitchZhenfa__ extends mkio.Protocol { }

/** 改变阵容的光环
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSwitchZhenfa extends __CSwitchZhenfa__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		new PSwitchZhenfa(roleid, zhenrongid, zhenfaid).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818842;

	public int getType() {
		return 818842;
	}

	public int zhenrongid;
	public int zhenfaid;

	public CSwitchZhenfa() {
	}

	public CSwitchZhenfa(int _zhenrongid_, int _zhenfaid_) {
		this.zhenrongid = _zhenrongid_;
		this.zhenfaid = _zhenfaid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(zhenrongid);
		_os_.marshal(zhenfaid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		zhenrongid = _os_.unmarshal_int();
		zhenfaid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSwitchZhenfa) {
			CSwitchZhenfa _o_ = (CSwitchZhenfa)_o1_;
			if (zhenrongid != _o_.zhenrongid) return false;
			if (zhenfaid != _o_.zhenfaid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += zhenrongid;
		_h_ += zhenfaid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(zhenrongid).append(",");
		_sb_.append(zhenfaid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSwitchZhenfa _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = zhenrongid - _o_.zhenrongid;
		if (0 != _c_) return _c_;
		_c_ = zhenfaid - _o_.zhenfaid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

