
package fire.msp.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MStartShowNpcBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MStartShowNpcBattle extends __MStartShowNpcBattle__ {
	@Override
	protected void process() {
		// protocol handle
		new fire.pb.battle.ShowBattle( roleid, teamsize, npcuniqueid, npcbaseid, areaconf ).launch();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 727897;

	public int getType() {
		return 727897;
	}

	public long roleid;
	public int teamsize;
	public long npcuniqueid;
	public int npcbaseid;
	public int areaconf;

	public MStartShowNpcBattle() {
	}

	public MStartShowNpcBattle(long _roleid_, int _teamsize_, long _npcuniqueid_, int _npcbaseid_, int _areaconf_) {
		this.roleid = _roleid_;
		this.teamsize = _teamsize_;
		this.npcuniqueid = _npcuniqueid_;
		this.npcbaseid = _npcbaseid_;
		this.areaconf = _areaconf_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(teamsize);
		_os_.marshal(npcuniqueid);
		_os_.marshal(npcbaseid);
		_os_.marshal(areaconf);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		teamsize = _os_.unmarshal_int();
		npcuniqueid = _os_.unmarshal_long();
		npcbaseid = _os_.unmarshal_int();
		areaconf = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MStartShowNpcBattle) {
			MStartShowNpcBattle _o_ = (MStartShowNpcBattle)_o1_;
			if (roleid != _o_.roleid) return false;
			if (teamsize != _o_.teamsize) return false;
			if (npcuniqueid != _o_.npcuniqueid) return false;
			if (npcbaseid != _o_.npcbaseid) return false;
			if (areaconf != _o_.areaconf) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += teamsize;
		_h_ += (int)npcuniqueid;
		_h_ += npcbaseid;
		_h_ += areaconf;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(teamsize).append(",");
		_sb_.append(npcuniqueid).append(",");
		_sb_.append(npcbaseid).append(",");
		_sb_.append(areaconf).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MStartShowNpcBattle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = teamsize - _o_.teamsize;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npcuniqueid - _o_.npcuniqueid);
		if (0 != _c_) return _c_;
		_c_ = npcbaseid - _o_.npcbaseid;
		if (0 != _c_) return _c_;
		_c_ = areaconf - _o_.areaconf;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

