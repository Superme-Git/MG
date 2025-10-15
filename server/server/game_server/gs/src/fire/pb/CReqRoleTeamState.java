
package fire.pb;
import gnet.link.Onlines;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqRoleTeamState__ extends mkio.Protocol { }

/** 客户端请求其他玩家的组队情况
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqRoleTeamState extends __CReqRoleTeamState__ {
	@Override
	protected void process() {
		// protocol handle

		long reqRoleId = Onlines.getInstance().findRoleid(this);
		if(reqRoleId < 0) {
			return;
		}
		
		Long teamId = xtable.Roleid2teamid.select(roleid);
		Integer level = xtable.Properties.selectLevel(roleid);
		if (level == null)
		{
			return;
		}
		
		SAnswerRoleTeamState answer = new SAnswerRoleTeamState();
		answer.roleid = roleid;
		answer.level = level;
		answer.teamstate = (teamId == null)? 0: 1;
		Onlines.getInstance().send(reqRoleId, answer);
		//GsClient.sendToScene(new GAddUserScreen(reqRoleId,roleid));
			
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786464;

	public int getType() {
		return 786464;
	}

	public long roleid;

	public CReqRoleTeamState() {
	}

	public CReqRoleTeamState(long _roleid_) {
		this.roleid = _roleid_;
	}

	public final boolean _validator_() {
		if (roleid <= 0) return false;
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
		if (_o1_ instanceof CReqRoleTeamState) {
			CReqRoleTeamState _o_ = (CReqRoleTeamState)_o1_;
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

	public int compareTo(CReqRoleTeamState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

