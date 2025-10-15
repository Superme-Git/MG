
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STeamError__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STeamError extends __STeamError__ {
	@Override
	protected void process() {
		// protocol handle
		robot.LoginRole role = robot.LoginRoleMgr.findLoginRole(this);
		if(role != null){
			role.getRoleTask().processProtocol(this);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794468;

	public int getType() {
		return 794468;
	}

	public int teamerror; // 参考TeamError的bean定义

	public STeamError() {
	}

	public STeamError(int _teamerror_) {
		this.teamerror = _teamerror_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(teamerror);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teamerror = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STeamError) {
			STeamError _o_ = (STeamError)_o1_;
			if (teamerror != _o_.teamerror) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += teamerror;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamerror).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(STeamError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = teamerror - _o_.teamerror;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

