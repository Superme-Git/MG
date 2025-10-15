
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SInviteJoinTeam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SInviteJoinTeam extends __SInviteJoinTeam__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794447;

	public int getType() {
		return 794447;
	}

	public long leaderroleid; // 队长id 只有op是2的时候才用 by changhao
	public java.lang.String invitername; // 邀请者名字 by changhao
	public int inviterlevel; // 邀请者等级 by changhao
	public int op; // 0是正常1是强制邀请2是队员邀请 by changhao

	public SInviteJoinTeam() {
		invitername = "";
	}

	public SInviteJoinTeam(long _leaderroleid_, java.lang.String _invitername_, int _inviterlevel_, int _op_) {
		this.leaderroleid = _leaderroleid_;
		this.invitername = _invitername_;
		this.inviterlevel = _inviterlevel_;
		this.op = _op_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(leaderroleid);
		_os_.marshal(invitername, "UTF-16LE");
		_os_.marshal(inviterlevel);
		_os_.marshal(op);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		leaderroleid = _os_.unmarshal_long();
		invitername = _os_.unmarshal_String("UTF-16LE");
		inviterlevel = _os_.unmarshal_int();
		op = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SInviteJoinTeam) {
			SInviteJoinTeam _o_ = (SInviteJoinTeam)_o1_;
			if (leaderroleid != _o_.leaderroleid) return false;
			if (!invitername.equals(_o_.invitername)) return false;
			if (inviterlevel != _o_.inviterlevel) return false;
			if (op != _o_.op) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)leaderroleid;
		_h_ += invitername.hashCode();
		_h_ += inviterlevel;
		_h_ += op;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(leaderroleid).append(",");
		_sb_.append("T").append(invitername.length()).append(",");
		_sb_.append(inviterlevel).append(",");
		_sb_.append(op).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

