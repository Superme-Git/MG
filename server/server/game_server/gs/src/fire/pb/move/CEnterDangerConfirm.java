
package fire.pb.move;

import fire.pb.map.Transfer;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CEnterDangerConfirm__ extends mkio.Protocol { }

/** 客户端发给服务器确认要进入危险场景
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CEnterDangerConfirm extends __CEnterDangerConfirm__ {
	@Override
	protected void process() {
		// protocol handle
		final Role role = RoleManager.getInstance().getRoleByProtocol(this);
		if(null == role) return;
		final fire.pb.scene.Scene rs = role.getScene();
		if(null == rs) return;
		
		fire.pb.scene.sPos.Position currentPos = role.getPos();
		
		if(rs.checkChannel(currentPos)){
			onChannel(currentPos, role);
		}else{
			fire.pb.talk.MessageMgr.sendMsgNotify(role.getRoleID(), 141679, null);
		}
	}
	
	private void onChannel(fire.pb.scene.sPos.Position currentPos, Role role){
		final fire.pb.scene.Scene rs = role.getScene();
		if(null == rs) return;
		final fire.pb.scene.GotoInfo to = rs.getGotoInfo(currentPos.toGridPos());
		if (null == to)	return;
		Transfer.justGoto(role.getRoleID(), to.getDestMapID(), to.getDestX(), to.getDestY(), SRoleEnterScene.QUEST);
//		role.justGoto(to.getDestMapID(), to.getDestX(), to.getDestY(), BlockInfo.FLOOR_0_Z, SRoleEnterScene.CHANNEAL);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790468;

	public int getType() {
		return 790468;
	}


	public CEnterDangerConfirm() {
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
		if (_o1_ instanceof CEnterDangerConfirm) {
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

	public int compareTo(CEnterDangerConfirm _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

