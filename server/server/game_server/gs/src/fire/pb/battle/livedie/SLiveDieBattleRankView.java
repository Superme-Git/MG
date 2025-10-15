
package fire.pb.battle.livedie;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SLiveDieBattleRankView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SLiveDieBattleRankView extends __SLiveDieBattleRankView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793843;

	public int getType() {
		return 793843;
	}

	public int modeltype; // 类型
	public java.util.ArrayList<fire.pb.battle.livedie.LDVideoRoleInfoDes> rolefightlist; // 对手信息

	public SLiveDieBattleRankView() {
		rolefightlist = new java.util.ArrayList<fire.pb.battle.livedie.LDVideoRoleInfoDes>();
	}

	public SLiveDieBattleRankView(int _modeltype_, java.util.ArrayList<fire.pb.battle.livedie.LDVideoRoleInfoDes> _rolefightlist_) {
		this.modeltype = _modeltype_;
		this.rolefightlist = _rolefightlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.livedie.LDVideoRoleInfoDes _v_ : rolefightlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(modeltype);
		_os_.compact_uint32(rolefightlist.size());
		for (fire.pb.battle.livedie.LDVideoRoleInfoDes _v_ : rolefightlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.livedie.LDVideoRoleInfoDes _v_ = new fire.pb.battle.livedie.LDVideoRoleInfoDes();
			_v_.unmarshal(_os_);
			rolefightlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SLiveDieBattleRankView) {
			SLiveDieBattleRankView _o_ = (SLiveDieBattleRankView)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (!rolefightlist.equals(_o_.rolefightlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += rolefightlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append(rolefightlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

