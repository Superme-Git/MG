
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SClanInvitation__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SClanInvitation extends __SClanInvitation__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808462;

	public int getType() {
		return 808462;
	}

	public long hostroleid; // 邀请人roleid
	public java.lang.String hostrolename; // 邀请人名称
	public int clanlevel; // 公会等级
	public java.lang.String clannname; // 公会名称
	public byte invitetype; // 邀请类型 0-普通人员邀请(进入申请列表) 1-帮主/副帮主邀请(直接进入公会)

	public SClanInvitation() {
		hostrolename = "";
		clannname = "";
	}

	public SClanInvitation(long _hostroleid_, java.lang.String _hostrolename_, int _clanlevel_, java.lang.String _clannname_, byte _invitetype_) {
		this.hostroleid = _hostroleid_;
		this.hostrolename = _hostrolename_;
		this.clanlevel = _clanlevel_;
		this.clannname = _clannname_;
		this.invitetype = _invitetype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(hostroleid);
		_os_.marshal(hostrolename, "UTF-16LE");
		_os_.marshal(clanlevel);
		_os_.marshal(clannname, "UTF-16LE");
		_os_.marshal(invitetype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		hostroleid = _os_.unmarshal_long();
		hostrolename = _os_.unmarshal_String("UTF-16LE");
		clanlevel = _os_.unmarshal_int();
		clannname = _os_.unmarshal_String("UTF-16LE");
		invitetype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SClanInvitation) {
			SClanInvitation _o_ = (SClanInvitation)_o1_;
			if (hostroleid != _o_.hostroleid) return false;
			if (!hostrolename.equals(_o_.hostrolename)) return false;
			if (clanlevel != _o_.clanlevel) return false;
			if (!clannname.equals(_o_.clannname)) return false;
			if (invitetype != _o_.invitetype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)hostroleid;
		_h_ += hostrolename.hashCode();
		_h_ += clanlevel;
		_h_ += clannname.hashCode();
		_h_ += (int)invitetype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(hostroleid).append(",");
		_sb_.append("T").append(hostrolename.length()).append(",");
		_sb_.append(clanlevel).append(",");
		_sb_.append("T").append(clannname.length()).append(",");
		_sb_.append(invitetype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

