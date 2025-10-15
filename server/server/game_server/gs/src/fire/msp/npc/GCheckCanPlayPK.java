
package fire.msp.npc;
import fire.pb.battle.PPlayPKFightView;
import fire.pb.scene.SceneClient;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;
import fire.pb.scene.sPos.GridPos;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCheckCanPlayPK__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCheckCanPlayPK extends __GCheckCanPlayPK__ {
	@Override
	protected void process() {
		Role hostRole = RoleManager.getInstance().getRoleByID(hostid);
		Role gestRole = RoleManager.getInstance().getRoleByID(guestid);
		GridPos hostGridPos = hostRole.getPos().toGridPos();
		GridPos guestGridPos = gestRole.getPos().toGridPos();
		
		//如果不是在切磋地图的，需要判断是否是同屏，如果在切磋地图就判断距离//不在擂台，判断是距离，这个在前面已经判断过了，这里就不需要处理了
		if (hostRole.getScene().getMapInfo().getBlockInfo().checkCanQiecuo(hostGridPos.getX(), hostGridPos.getY())
				&&gestRole.getScene().getMapInfo().getBlockInfo().checkCanQiecuo(guestGridPos.getX(), guestGridPos.getY())) {
			//在擂台
			if (!(hostRole.getScene().getMapID() == PPlayPKFightView.CQMAPID && gestRole.getScene().getMapID() == PPlayPKFightView.CQMAPID)){
				MessageMgr.sendMsgNotify(hostid, 120063, null);
				return ;
			}
			Team team = TeamManager.selectTeamByRoleId(hostid);
			if (team!=null){
				if(team.isInTeam(guestid)){
					fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 141389, null);
					return;
				}
			}
		}
		SceneClient.pSend(new MCheckCanPlayPK(hostid, guestid));
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730413;

	public int getType() {
		return 730413;
	}

	public long hostid;
	public long guestid;
	public int checklevel;

	public GCheckCanPlayPK() {
	}

	public GCheckCanPlayPK(long _hostid_, long _guestid_, int _checklevel_) {
		this.hostid = _hostid_;
		this.guestid = _guestid_;
		this.checklevel = _checklevel_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(hostid);
		_os_.marshal(guestid);
		_os_.marshal(checklevel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		hostid = _os_.unmarshal_long();
		guestid = _os_.unmarshal_long();
		checklevel = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCheckCanPlayPK) {
			GCheckCanPlayPK _o_ = (GCheckCanPlayPK)_o1_;
			if (hostid != _o_.hostid) return false;
			if (guestid != _o_.guestid) return false;
			if (checklevel != _o_.checklevel) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)hostid;
		_h_ += (int)guestid;
		_h_ += checklevel;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(hostid).append(",");
		_sb_.append(guestid).append(",");
		_sb_.append(checklevel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GCheckCanPlayPK _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(hostid - _o_.hostid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(guestid - _o_.guestid);
		if (0 != _c_) return _c_;
		_c_ = checklevel - _o_.checklevel;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

