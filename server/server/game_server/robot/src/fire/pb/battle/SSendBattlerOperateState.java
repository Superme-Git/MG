
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendBattlerOperateState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendBattlerOperateState extends __SSendBattlerOperateState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793450;

	public int getType() {
		return 793450;
	}

	public int battleid; // 回合操作开始时，所有人状态为准备中，服务器不用发
	public int state; // 1为准备中，2为操作完毕（请等待状态）3为代表掉线

	public SSendBattlerOperateState() {
	}

	public SSendBattlerOperateState(int _battleid_, int _state_) {
		this.battleid = _battleid_;
		this.state = _state_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(battleid);
		_os_.marshal(state);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		battleid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendBattlerOperateState) {
			SSendBattlerOperateState _o_ = (SSendBattlerOperateState)_o1_;
			if (battleid != _o_.battleid) return false;
			if (state != _o_.state) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += battleid;
		_h_ += state;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(battleid).append(",");
		_sb_.append(state).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSendBattlerOperateState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = battleid - _o_.battleid;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

