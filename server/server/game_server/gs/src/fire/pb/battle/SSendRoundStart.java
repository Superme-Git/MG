
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendRoundStart__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendRoundStart extends __SSendRoundStart__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793435;

	public int getType() {
		return 793435;
	}

	public int time; // 回合可操作的时间，单位为秒，默认为30
	public int environment; // 战场环境，参考BattleEnviroment
	public java.util.HashMap<Integer,Integer> aiactions; // 回合读秒前需要播放的AIAction key=fighter id; value=aiaction id;

	public SSendRoundStart() {
		aiactions = new java.util.HashMap<Integer,Integer>();
	}

	public SSendRoundStart(int _time_, int _environment_, java.util.HashMap<Integer,Integer> _aiactions_) {
		this.time = _time_;
		this.environment = _environment_;
		this.aiactions = _aiactions_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(time);
		_os_.marshal(environment);
		_os_.compact_uint32(aiactions.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : aiactions.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		time = _os_.unmarshal_int();
		environment = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			aiactions.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendRoundStart) {
			SSendRoundStart _o_ = (SSendRoundStart)_o1_;
			if (time != _o_.time) return false;
			if (environment != _o_.environment) return false;
			if (!aiactions.equals(_o_.aiactions)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += time;
		_h_ += environment;
		_h_ += aiactions.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(time).append(",");
		_sb_.append(environment).append(",");
		_sb_.append(aiactions).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

