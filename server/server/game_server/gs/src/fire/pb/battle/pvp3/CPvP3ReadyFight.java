
package fire.pb.battle.pvp3;


import fire.pb.map.Role;
import fire.pb.map.RoleManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPvP3ReadyFight__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPvP3ReadyFight extends __CPvP3ReadyFight__ {
	@Override
	protected void process() {

		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId <= 0)
			return;

		Role mapRole = RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole == null)
			return;
		int grade = PvP3Helper.getRaceGradeByMapId(mapRole.getMapId());
		if (grade <= 0)
			return;

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				PvP3RaceProxy race = PvP3RaceProxy.getPvP3RaceProxy(grade);
				if (ready == 1) {
					return race.roleReady(roleId);
				}
				return race.roleUnready(roleId);
			}

		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793643;

	public int getType() {
		return 793643;
	}

	public byte ready; // 1:准备，0：取消准备

	public CPvP3ReadyFight() {
	}

	public CPvP3ReadyFight(byte _ready_) {
		this.ready = _ready_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ready);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ready = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPvP3ReadyFight) {
			CPvP3ReadyFight _o_ = (CPvP3ReadyFight)_o1_;
			if (ready != _o_.ready) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)ready;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ready).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPvP3ReadyFight _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = ready - _o_.ready;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

