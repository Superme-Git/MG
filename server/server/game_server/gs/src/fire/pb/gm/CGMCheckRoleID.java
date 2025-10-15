
package fire.pb.gm;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGMCheckRoleID__ extends mkio.Protocol { }

/** GM  请求角色ID检测
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGMCheckRoleID extends __CGMCheckRoleID__ {
	@Override
	protected void process() {
		// protocol handle
		final long gmroleid=gnet.link.Onlines.getInstance().findRoleid(this);
		if (gmroleid < 0)
			return;
		final int userID=((gnet.link.Dispatch)this.getContext()).userid;
		if (!GMInteface.gmPriv(userID)) return;
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop == null){
			int msgid = fire.pb.util.FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop, "gm.checkroleid.unexist");
			fire.pb.talk.MessageMgr.sendMsgNotify(gmroleid, msgid, null);
			return;
		}
	
		fire.pb.move.RoleSimpleInfo simpleinfo = new fire.pb.move.RoleSimpleInfo(roleid, prop.getRolename(),prop.getShape(),prop.getSchool(),prop.getLevel(), 0);
		SGMCheckRoleID send = new SGMCheckRoleID(simpleinfo);
		gnet.link.Onlines.getInstance().send(gmroleid, send);
		StringBuffer str = new StringBuffer("CheckRoleID:");
		str.append(roleid);
		//GMInteface.doGmOperateLog(userID, str.toString());
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 791434;

	public int getType() {
		return 791434;
	}

	public long roleid;

	public CGMCheckRoleID() {
	}

	public CGMCheckRoleID(long _roleid_) {
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
		if (_o1_ instanceof CGMCheckRoleID) {
			CGMCheckRoleID _o_ = (CGMCheckRoleID)_o1_;
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

	public int compareTo(CGMCheckRoleID _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

