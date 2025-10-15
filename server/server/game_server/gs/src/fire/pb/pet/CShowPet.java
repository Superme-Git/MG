
package fire.pb.pet;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CShowPet__ extends mkio.Protocol { }

/** 客户端请求展示宠物
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CShowPet extends __CShowPet__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0 || !fire.pb.StateCommon.isOnline(roleid))
			return;

		if (petkey < 1)
			return;

		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		if (team != null) {
			if (!team.isTeamLeader(roleid)) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 141146, null);
				return;
			}
		}

		PShowPetProc proc = new PShowPetProc(roleid, petkey);
		proc.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788433;

	public int getType() {
		return 788433;
	}

	public int petkey;

	public CShowPet() {
	}

	public CShowPet(int _petkey_) {
		this.petkey = _petkey_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CShowPet) {
			CShowPet _o_ = (CShowPet)_o1_;
			if (petkey != _o_.petkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CShowPet _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

