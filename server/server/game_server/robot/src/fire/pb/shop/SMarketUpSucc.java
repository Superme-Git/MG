
package fire.pb.shop;

import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.task.BuyAndSell;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SMarketUpSucc__ extends mkio.Protocol { }

/** 上架珍品成功
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SMarketUpSucc extends __SMarketUpSucc__ {
	@Override
	protected void process() {
		// protocol handle
		LoginRole role =  LoginRoleMgr.findLoginRole(this);
		if(role == null)
			return;
		if(role.getRoleTask() instanceof BuyAndSell) // 摆摊机器人
			role.onRoleAddProtocol(this);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810654;

	public int getType() {
		return 810654;
	}

	public int israrity; // 0非珍品 1珍品

	public SMarketUpSucc() {
	}

	public SMarketUpSucc(int _israrity_) {
		this.israrity = _israrity_;
	}

	public final boolean _validator_() {
		if (israrity < 0 || israrity > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(israrity);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		israrity = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SMarketUpSucc) {
			SMarketUpSucc _o_ = (SMarketUpSucc)_o1_;
			if (israrity != _o_.israrity) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += israrity;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(israrity).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SMarketUpSucc _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = israrity - _o_.israrity;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

