
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SMulDayLogin__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SMulDayLogin extends __SMulDayLogin__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787731;

	public int getType() {
		return 787731;
	}

	public int logindays; // 累计登录天数
	public java.util.HashMap<Integer,Long> rewardmap; // 七日登录礼包奖励数据(1.key-奖励配置ID 2.val-领取时间(0-未领取))

	public SMulDayLogin() {
		rewardmap = new java.util.HashMap<Integer,Long>();
	}

	public SMulDayLogin(int _logindays_, java.util.HashMap<Integer,Long> _rewardmap_) {
		this.logindays = _logindays_;
		this.rewardmap = _rewardmap_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(logindays);
		_os_.compact_uint32(rewardmap.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : rewardmap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		logindays = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			long _v_;
			_v_ = _os_.unmarshal_long();
			rewardmap.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SMulDayLogin) {
			SMulDayLogin _o_ = (SMulDayLogin)_o1_;
			if (logindays != _o_.logindays) return false;
			if (!rewardmap.equals(_o_.rewardmap)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += logindays;
		_h_ += rewardmap.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(logindays).append(",");
		_sb_.append(rewardmap).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

