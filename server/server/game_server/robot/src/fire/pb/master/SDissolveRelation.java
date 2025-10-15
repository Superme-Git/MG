
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SDissolveRelation__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SDissolveRelation extends __SDissolveRelation__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816450;

	public int getType() {
		return 816450;
	}

	public int relation; // 关系,师傅or徒弟
	public int initiative; // 是否主动 0为被动,1为主动,2为自动
	public long playerid; // 玩家id

	public SDissolveRelation() {
	}

	public SDissolveRelation(int _relation_, int _initiative_, long _playerid_) {
		this.relation = _relation_;
		this.initiative = _initiative_;
		this.playerid = _playerid_;
	}

	public final boolean _validator_() {
		if (relation < 0 || relation > 1) return false;
		if (initiative < 0 || initiative > 2) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(relation);
		_os_.marshal(initiative);
		_os_.marshal(playerid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		relation = _os_.unmarshal_int();
		initiative = _os_.unmarshal_int();
		playerid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SDissolveRelation) {
			SDissolveRelation _o_ = (SDissolveRelation)_o1_;
			if (relation != _o_.relation) return false;
			if (initiative != _o_.initiative) return false;
			if (playerid != _o_.playerid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += relation;
		_h_ += initiative;
		_h_ += (int)playerid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(relation).append(",");
		_sb_.append(initiative).append(",");
		_sb_.append(playerid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SDissolveRelation _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = relation - _o_.relation;
		if (0 != _c_) return _c_;
		_c_ = initiative - _o_.initiative;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(playerid - _o_.playerid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

