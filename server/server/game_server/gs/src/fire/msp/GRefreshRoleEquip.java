
package fire.msp;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRefreshRoleEquip__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRefreshRoleEquip extends __GRefreshRoleEquip__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if (null == role)
			return;
		
		role.marshal();
		fire.pb.move.SRoleComponentsChange send = new fire.pb.move.SRoleComponentsChange();
		send.roleid = roleid;
		fire.pb.map.Role.getPlayerComponents(roleid, send.components);
		send.spritetype = 0;
		role.sendWhoSeeMe(send);
		fire.pb.scene.movable.SceneTeam team = fire.pb.scene.manager.SceneTeamManager.getInstance().getTeamByID(roleid);
		if (team == null) {
			gnet.link.Onlines.getInstance().send(roleid, send);
			return;
		}
		java.util.Set<Long> roleids = new java.util.HashSet<Long>();
		for (fire.pb.scene.movable.Role member : team.getMembers()) {
			roleids.add(member.getRoleID());
		}
		if (!roleids.isEmpty())
			gnet.link.Onlines.getInstance().send(roleids, send);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720911;

	public int getType() {
		return 720911;
	}

	public long roleid; // 用户id

	public GRefreshRoleEquip() {
	}

	public GRefreshRoleEquip(long _roleid_) {
		this.roleid = _roleid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRefreshRoleEquip) {
			GRefreshRoleEquip _o_ = (GRefreshRoleEquip)_o1_;
			if (roleid != _o_.roleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GRefreshRoleEquip _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

