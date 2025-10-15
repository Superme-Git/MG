
package fire.pb.move;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRoleStop__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRoleStop extends __CRoleStop__ {
	@Override
	protected void process() {
		// protocol handle
		final Role role = RoleManager.getInstance().getRoleByProtocol(this);
		if(null == role)
			return;
		if(sceneid != role.getScene().getSceneID())
			return;
		
		role.clearCurDestPos();
		
		if(! role.checkMoveUnit()) return;
		
		//checkmove一次
		if(!CCheckMove.checkmove(role, role.getScene().getSceneID(), pos, poslist, System.currentTimeMillis()))
		{
			return;	
		}
		
		role.clearCurDestPos();

		/*不广播 final SRoleStop snd = new SRoleStop();
		snd.roleid = role.getRoleID();
		snd.pos = role.getPos().toProtocolPos();
		role.sendWhoSeeMe(snd);*/
		/*不广播2 final SRoleMove snd = new SRoleMove();
		snd.roleid = role.getRoleID();
		snd.destpos = role.getPos().toProtocolPos();
		role.sendWhoSeeMe(snd);*/

	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790442;

	public int getType() {
		return 790442;
	}

	public java.util.LinkedList<fire.pb.move.Pos> poslist; // 路径上的关键点
	public fire.pb.move.Pos pos; // 位置
	public long sceneid;

	public CRoleStop() {
		poslist = new java.util.LinkedList<fire.pb.move.Pos>();
		pos = new fire.pb.move.Pos();
	}

	public CRoleStop(java.util.LinkedList<fire.pb.move.Pos> _poslist_, fire.pb.move.Pos _pos_, long _sceneid_) {
		this.poslist = _poslist_;
		this.pos = _pos_;
		this.sceneid = _sceneid_;
	}

	public final boolean _validator_() {
		for (fire.pb.move.Pos _v_ : poslist)
			if (!_v_._validator_()) return false;
		if (!pos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(poslist.size());
		for (fire.pb.move.Pos _v_ : poslist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(pos);
		_os_.marshal(sceneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.move.Pos _v_ = new fire.pb.move.Pos();
			_v_.unmarshal(_os_);
			poslist.add(_v_);
		}
		pos.unmarshal(_os_);
		sceneid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRoleStop) {
			CRoleStop _o_ = (CRoleStop)_o1_;
			if (!poslist.equals(_o_.poslist)) return false;
			if (!pos.equals(_o_.pos)) return false;
			if (sceneid != _o_.sceneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += poslist.hashCode();
		_h_ += pos.hashCode();
		_h_ += (int)sceneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(poslist).append(",");
		_sb_.append(pos).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

