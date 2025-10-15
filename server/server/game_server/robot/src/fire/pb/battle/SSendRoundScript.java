
package fire.pb.battle;

import robot.LoginRole;
import robot.LoginRoleMgr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendRoundScript__ extends mkio.Protocol { }

/** battle conduct protocol end
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendRoundScript extends __SSendRoundScript__ {
	@Override
	protected void process() {
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if(role != null && role.getRoleTask() != null)
			role.getRoleTask().processProtocol(this);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793457;

	public int getType() {
		return 793457;
	}

	public java.util.LinkedList<fire.pb.battle.NewResultItem> playitem;
	public java.util.LinkedList<fire.pb.battle.AIOperation> aiactions; // 随战斗脚本播放的客户端AI动作
	public java.util.HashMap<Integer,Float> rolechangedattrs; // 回合结束时主角属性的变化（战斗中属性实时变化）
	public java.util.HashMap<Integer,Float> petchangedattrs; // 回合结束时主角宠物属性的变化
	public java.util.HashMap<Integer,Integer> fighterfinallyhps; // 回合结束时战斗者血量的最终值
	public java.util.HashMap<Integer,Integer> fighterfinallymps; // 回合结束时战斗者兰量的最终值

	public SSendRoundScript() {
		playitem = new java.util.LinkedList<fire.pb.battle.NewResultItem>();
		aiactions = new java.util.LinkedList<fire.pb.battle.AIOperation>();
		rolechangedattrs = new java.util.HashMap<Integer,Float>();
		petchangedattrs = new java.util.HashMap<Integer,Float>();
		fighterfinallyhps = new java.util.HashMap<Integer,Integer>();
		fighterfinallymps = new java.util.HashMap<Integer,Integer>();
	}

	public SSendRoundScript(java.util.LinkedList<fire.pb.battle.NewResultItem> _playitem_, java.util.LinkedList<fire.pb.battle.AIOperation> _aiactions_, java.util.HashMap<Integer,Float> _rolechangedattrs_, java.util.HashMap<Integer,Float> _petchangedattrs_, java.util.HashMap<Integer,Integer> _fighterfinallyhps_, java.util.HashMap<Integer,Integer> _fighterfinallymps_) {
		this.playitem = _playitem_;
		this.aiactions = _aiactions_;
		this.rolechangedattrs = _rolechangedattrs_;
		this.petchangedattrs = _petchangedattrs_;
		this.fighterfinallyhps = _fighterfinallyhps_;
		this.fighterfinallymps = _fighterfinallymps_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.NewResultItem _v_ : playitem)
			if (!_v_._validator_()) return false;
		for (fire.pb.battle.AIOperation _v_ : aiactions)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(playitem.size());
		for (fire.pb.battle.NewResultItem _v_ : playitem) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(aiactions.size());
		for (fire.pb.battle.AIOperation _v_ : aiactions) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(rolechangedattrs.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : rolechangedattrs.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(petchangedattrs.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : petchangedattrs.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(fighterfinallyhps.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : fighterfinallyhps.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(fighterfinallymps.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : fighterfinallymps.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.NewResultItem _v_ = new fire.pb.battle.NewResultItem();
			_v_.unmarshal(_os_);
			playitem.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.AIOperation _v_ = new fire.pb.battle.AIOperation();
			_v_.unmarshal(_os_);
			aiactions.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			float _v_;
			_v_ = _os_.unmarshal_float();
			rolechangedattrs.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			float _v_;
			_v_ = _os_.unmarshal_float();
			petchangedattrs.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			fighterfinallyhps.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			fighterfinallymps.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendRoundScript) {
			SSendRoundScript _o_ = (SSendRoundScript)_o1_;
			if (!playitem.equals(_o_.playitem)) return false;
			if (!aiactions.equals(_o_.aiactions)) return false;
			if (!rolechangedattrs.equals(_o_.rolechangedattrs)) return false;
			if (!petchangedattrs.equals(_o_.petchangedattrs)) return false;
			if (!fighterfinallyhps.equals(_o_.fighterfinallyhps)) return false;
			if (!fighterfinallymps.equals(_o_.fighterfinallymps)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += playitem.hashCode();
		_h_ += aiactions.hashCode();
		_h_ += rolechangedattrs.hashCode();
		_h_ += petchangedattrs.hashCode();
		_h_ += fighterfinallyhps.hashCode();
		_h_ += fighterfinallymps.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(playitem).append(",");
		_sb_.append(aiactions).append(",");
		_sb_.append(rolechangedattrs).append(",");
		_sb_.append(petchangedattrs).append(",");
		_sb_.append(fighterfinallyhps).append(",");
		_sb_.append(fighterfinallymps).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

