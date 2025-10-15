
package fire.pb.attr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SApplyYingFuExprience__ extends mkio.Protocol { }

/** 服务器回复角色盈福经验
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SApplyYingFuExprience extends __SApplyYingFuExprience__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 799439;

	public int getType() {
		return 799439;
	}

	public long exprience; // 盈福经验

	public SApplyYingFuExprience() {
	}

	public SApplyYingFuExprience(long _exprience_) {
		this.exprience = _exprience_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(exprience);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		exprience = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SApplyYingFuExprience) {
			SApplyYingFuExprience _o_ = (SApplyYingFuExprience)_o1_;
			if (exprience != _o_.exprience) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)exprience;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(exprience).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SApplyYingFuExprience _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(exprience - _o_.exprience);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

