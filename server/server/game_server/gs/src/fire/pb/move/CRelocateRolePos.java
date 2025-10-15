
package fire.pb.move;
import fire.pb.scene.Scene;
import fire.pb.scene.movable.SceneTeam;
import fire.pb.scene.sPos.Position;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRelocateRolePos__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRelocateRolePos extends __CRelocateRolePos__ {
	@Override
	protected void process() {
		final fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByProtocol(this);
		if(null == role)
			return;
		if(sceneid != role.getScene().getSceneID())
		{
			Scene.LOG.info("CRelocateRolePos发来的场景跟角色当前的场景不一致，角色名称：" + role.getName());
			return;
		}
		if(SceneTeam.TEAM_NORMAL == role.getTeamState()){
			Scene.LOG.error("队伍中普通队员发来Relocate消息\t" + role.getName());
			return;
		}
		if(! role.checkNeedCheckPos())
			return;
		
		role.clearNeedCheckPos();
		
		final Position  pos = new Position(curpos.x, curpos.y, locz);
		if(role.getScene().checkBlock(pos.toGridPos(), pos.getWalkBlockType()))
		{
			role.broadResetPosition();
			return;//重置到阻挡点时，不允许
		}
		if(SRelocateRolePos.MAX_DISTANCE < pos.getDistance(role.getPos()))
		{
			role.broadResetPosition();
			return;//重置距离过远时，不允许
		}
		
		// 只调整位置,不更新屏幕信息,就会导致角色遗留问题
		role.updatePosition(pos);
		role.clearCurDestPos();
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790447;

	public int getType() {
		return 790447;
	}

	public long sceneid;
	public fire.pb.move.Pos curpos;
	public byte locz; // RelocateRolePos的z坐标

	public CRelocateRolePos() {
		curpos = new fire.pb.move.Pos();
	}

	public CRelocateRolePos(long _sceneid_, fire.pb.move.Pos _curpos_, byte _locz_) {
		this.sceneid = _sceneid_;
		this.curpos = _curpos_;
		this.locz = _locz_;
	}

	public final boolean _validator_() {
		if (!curpos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(sceneid);
		_os_.marshal(curpos);
		_os_.marshal(locz);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		sceneid = _os_.unmarshal_long();
		curpos.unmarshal(_os_);
		locz = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRelocateRolePos) {
			CRelocateRolePos _o_ = (CRelocateRolePos)_o1_;
			if (sceneid != _o_.sceneid) return false;
			if (!curpos.equals(_o_.curpos)) return false;
			if (locz != _o_.locz) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)sceneid;
		_h_ += curpos.hashCode();
		_h_ += (int)locz;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(sceneid).append(",");
		_sb_.append(curpos).append(",");
		_sb_.append(locz).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRelocateRolePos _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(sceneid - _o_.sceneid);
		if (0 != _c_) return _c_;
		_c_ = curpos.compareTo(_o_.curpos);
		if (0 != _c_) return _c_;
		_c_ = locz - _o_.locz;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

