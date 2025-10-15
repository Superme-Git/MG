
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPetRecover__ extends mkio.Protocol { }

/** 客户端请求宠物找回
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPetRecover extends __CPetRecover__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;

		PPetRecover proc = new PPetRecover(roleId, uniqid);
		proc.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788585;

	public int getType() {
		return 788585;
	}

	public long uniqid; // 唯一id

	public CPetRecover() {
	}

	public CPetRecover(long _uniqid_) {
		this.uniqid = _uniqid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(uniqid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		uniqid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPetRecover) {
			CPetRecover _o_ = (CPetRecover)_o1_;
			if (uniqid != _o_.uniqid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)uniqid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(uniqid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPetRecover _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(uniqid - _o_.uniqid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

