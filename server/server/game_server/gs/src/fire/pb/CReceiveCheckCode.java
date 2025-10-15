package fire.pb;
import fire.pb.tel.utils.CheckCodeUtils;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReceiveCheckCode__ extends mkio.Protocol { }

/** 发送短信验证
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class CReceiveCheckCode extends __CReceiveCheckCode__ {

	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0) {
			return;
		}
		//得到验证码
		CheckCodeUtils.getCheckCode(roleid, checkcodetype);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786570;

	public int getType() {
		return 786570;
	}

	public byte checkcodetype; // 2 道具安全锁 3藏宝阁

	public CReceiveCheckCode() {
	}

	public CReceiveCheckCode(byte _checkcodetype_) {
		this.checkcodetype = _checkcodetype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(checkcodetype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		checkcodetype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReceiveCheckCode) {
			CReceiveCheckCode _o_ = (CReceiveCheckCode)_o1_;
			if (checkcodetype != _o_.checkcodetype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)checkcodetype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(checkcodetype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReceiveCheckCode _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = checkcodetype - _o_.checkcodetype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}
