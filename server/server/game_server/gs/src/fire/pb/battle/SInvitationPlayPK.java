
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SInvitationPlayPK__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SInvitationPlayPK extends __SInvitationPlayPK__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793688;

	public int getType() {
		return 793688;
	}

	public long sourceid; // 发起邀请人的id
	public java.lang.String rolename; // 玩家名称
	public int rolelevel; // 等级
	public int teamnum; // 当前队伍人数

	public SInvitationPlayPK() {
		rolename = "";
	}

	public SInvitationPlayPK(long _sourceid_, java.lang.String _rolename_, int _rolelevel_, int _teamnum_) {
		this.sourceid = _sourceid_;
		this.rolename = _rolename_;
		this.rolelevel = _rolelevel_;
		this.teamnum = _teamnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(sourceid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(rolelevel);
		_os_.marshal(teamnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		sourceid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		rolelevel = _os_.unmarshal_int();
		teamnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SInvitationPlayPK) {
			SInvitationPlayPK _o_ = (SInvitationPlayPK)_o1_;
			if (sourceid != _o_.sourceid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (rolelevel != _o_.rolelevel) return false;
			if (teamnum != _o_.teamnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)sourceid;
		_h_ += rolename.hashCode();
		_h_ += rolelevel;
		_h_ += teamnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(sourceid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(rolelevel).append(",");
		_sb_.append(teamnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

