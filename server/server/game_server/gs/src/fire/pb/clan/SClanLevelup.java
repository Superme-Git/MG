
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SClanLevelup__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SClanLevelup extends __SClanLevelup__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808473;

	public int getType() {
		return 808473;
	}

	public java.util.HashMap<Integer,Integer> change; // 1大厅2金库3药店4旅馆 by changhao
	public java.util.HashMap<Integer,Integer> costmax; // 1大厅2金库3药店4旅馆 升级需要的资金最大值
	public int money; // 返回最新公会资金 by changhao

	public SClanLevelup() {
		change = new java.util.HashMap<Integer,Integer>();
		costmax = new java.util.HashMap<Integer,Integer>();
	}

	public SClanLevelup(java.util.HashMap<Integer,Integer> _change_, java.util.HashMap<Integer,Integer> _costmax_, int _money_) {
		this.change = _change_;
		this.costmax = _costmax_;
		this.money = _money_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(change.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : change.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(costmax.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : costmax.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(money);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			change.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			costmax.put(_k_, _v_);
		}
		money = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SClanLevelup) {
			SClanLevelup _o_ = (SClanLevelup)_o1_;
			if (!change.equals(_o_.change)) return false;
			if (!costmax.equals(_o_.costmax)) return false;
			if (money != _o_.money) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += change.hashCode();
		_h_ += costmax.hashCode();
		_h_ += money;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(change).append(",");
		_sb_.append(costmax).append(",");
		_sb_.append(money).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

