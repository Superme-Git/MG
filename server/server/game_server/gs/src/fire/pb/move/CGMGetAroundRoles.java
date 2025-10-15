
package fire.pb.move;

import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGMGetAroundRoles__ extends mkio.Protocol { }

/** GM 请求gm周围所有的玩家信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGMGetAroundRoles extends __CGMGetAroundRoles__ {
	@Override
	protected void process() {
		// protocol handle
		final int userID=((gnet.link.Dispatch)this.getContext()).userid;
		if (!fire.pb.gm.GMInteface.gmPriv(userID)) return;
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)	return;
		
		final fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if (role == null)return;
		
		final fire.pb.scene.Scene rs = role.getScene();
		if(null == rs) return;
		
		SGMGetAroundRoles send = new SGMGetAroundRoles();
		for(Role r : role.getAroundRoles().values())
		{
			fire.pb.move.RoleSimpleInfo roleinfo = new fire.pb.move.RoleSimpleInfo();
			roleinfo.name = r.getName();
			roleinfo.roleid = r.getRoleID();
			roleinfo.camptype = r.getCamp();
			send.roles.add(roleinfo);
		}
		//SGMGetAroundRoles send = new SGMGetAroundRoles(role.scene.getGmAroundRoleIDs(role.getPos().getScreenIndex(), role));
		gnet.link.Onlines.getInstance().send(roleid, send);	
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790469;

	public int getType() {
		return 790469;
	}


	public CGMGetAroundRoles() {
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
		if (_o1_ instanceof CGMGetAroundRoles) {
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

	public int compareTo(CGMGetAroundRoles _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

