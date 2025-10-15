
package fire.pb.battle.pvp3;


import fire.pb.map.Role;
import fire.pb.map.RoleManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPvP3MyInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPvP3MyInfo extends __CPvP3MyInfo__ {
	@Override
	protected void process() {

		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId <= 0)
			return;

		Role mapRole = RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole == null)
			return;

		final int grade = PvP3Helper.getRaceGradeByMapId(mapRole.getMapId());
		if (grade <= 0)
			return;

		new mkdb.Procedure() {
			@Override
			protected boolean process() {

				PvP3RaceProxy race = PvP3RaceProxy.getPvP3RaceProxy(grade);
				if (race.isOpening() == false) {
					return false;
				}
				PvP3RoleProxy pvpRoleProxy = PvP3RoleProxy.getPvP3RoleProxy(roleId, true);
				pvpRoleProxy.sendSPvP3MyInfo(race.isReady(roleId));
				return true;
			}

		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793633;

	public int getType() {
		return 793633;
	}


	public CPvP3MyInfo() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPvP3MyInfo) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPvP3MyInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

