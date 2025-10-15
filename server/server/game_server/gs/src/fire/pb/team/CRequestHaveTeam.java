
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestHaveTeam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 询问是否有队
 * @author changhao
 *
 */
public class CRequestHaveTeam extends __CRequestHaveTeam__ {
	@Override
	protected void process() {
		// protocol handle
		
		Long teamid = xtable.Roleid2teamid.select(roleid);
		
		SRequestHaveTeam msg = new SRequestHaveTeam();
		msg.ret = 0;
		if (teamid != null)
		{
			msg.ret = 1;
		}
		
		final long r = gnet.link.Onlines.getInstance().findRoleid(this);
		if (r < 0)
			return;
		
		gnet.link.Onlines.getInstance().send(r, msg);			
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794515;

	public int getType() {
		return 794515;
	}

	public long roleid; // 某个人是否有队 by changhao

	public CRequestHaveTeam() {
	}

	public CRequestHaveTeam(long _roleid_) {
		this.roleid = _roleid_;
	}

	public final boolean _validator_() {
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
		if (_o1_ instanceof CRequestHaveTeam) {
			CRequestHaveTeam _o_ = (CRequestHaveTeam)_o1_;
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

	public int compareTo(CRequestHaveTeam _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

