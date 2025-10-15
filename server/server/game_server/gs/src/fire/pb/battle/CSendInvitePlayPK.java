
package fire.pb.battle;

import fire.pb.map.GridPos;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.scene.AreaInfo;
import fire.pb.util.MapUtil;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSendInvitePlayPK__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSendInvitePlayPK extends __CSendInvitePlayPK__ {
	public static final int PVP_LEVEL=20;//PVP等级需求
	@Override
	protected void process() {
		final long hostid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (-1 == hostid){
			return;
		}
		Role hostRole = RoleManager.getInstance().getRoleByID(hostid);
		if (hostRole == null)
			return ;
		//判断目标是否在擂台
		Role gRole = RoleManager.getInstance().getRoleByID(guestroleid);
		if (gRole == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 145001, null);
			return ;
		}
		GridPos gpos = gRole.getPos().toGridPos();
		AreaInfo gareaInfo = MapUtil.getAreaInfo(hostRole.getMapId(), gpos.getX(), gpos.getY());
		if(gareaInfo==null){
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162065, null);
			return ;
		}
		if(!gareaInfo.isQiecuoArea()){
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 162065, null);
			return ;
		}
		
		
		new PSendInvitePlayPK(hostid,guestroleid).submit();
		return;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793564;

	public int getType() {
		return 793564;
	}

	public long guestroleid; // 被邀请角色ID

	public CSendInvitePlayPK() {
	}

	public CSendInvitePlayPK(long _guestroleid_) {
		this.guestroleid = _guestroleid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(guestroleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		guestroleid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSendInvitePlayPK) {
			CSendInvitePlayPK _o_ = (CSendInvitePlayPK)_o1_;
			if (guestroleid != _o_.guestroleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)guestroleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(guestroleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSendInvitePlayPK _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(guestroleid - _o_.guestroleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

