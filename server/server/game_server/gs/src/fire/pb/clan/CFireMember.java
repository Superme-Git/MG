
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CFireMember__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CFireMember extends __CFireMember__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				return fire.pb.clan.srv.ClanAuthManager.getInstance().fireMemeberClan(roleid, memberroleid, reasontype);
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808467;

	public int getType() {
		return 808467;
	}

	public long memberroleid; // 公会成员的id
	public int reasontype; // 事由id 参考 FireReasonType

	public CFireMember() {
	}

	public CFireMember(long _memberroleid_, int _reasontype_) {
		this.memberroleid = _memberroleid_;
		this.reasontype = _reasontype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(memberroleid);
		_os_.marshal(reasontype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		memberroleid = _os_.unmarshal_long();
		reasontype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CFireMember) {
			CFireMember _o_ = (CFireMember)_o1_;
			if (memberroleid != _o_.memberroleid) return false;
			if (reasontype != _o_.reasontype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)memberroleid;
		_h_ += reasontype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(memberroleid).append(",");
		_sb_.append(reasontype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CFireMember _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(memberroleid - _o_.memberroleid);
		if (0 != _c_) return _c_;
		_c_ = reasontype - _o_.reasontype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

