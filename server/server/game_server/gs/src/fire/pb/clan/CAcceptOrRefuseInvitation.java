
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAcceptOrRefuseInvitation__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAcceptOrRefuseInvitation extends __CAcceptOrRefuseInvitation__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleId<0 || hostroleid<0){
			return;
		}
		if(this.accept>0){
			new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					return fire.pb.clan.srv.ClanBaseManager.getInstance().invitationClan(roleId, hostroleid);
				}
			}.submit();
		}else{
			fire.pb.talk.MessageMgr.sendMsgNotify(hostroleid, 145012, null);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808470;

	public int getType() {
		return 808470;
	}

	public long hostroleid; // 邀请人roleid
	public byte accept; // 0拒绝,1接受

	public CAcceptOrRefuseInvitation() {
	}

	public CAcceptOrRefuseInvitation(long _hostroleid_, byte _accept_) {
		this.hostroleid = _hostroleid_;
		this.accept = _accept_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(hostroleid);
		_os_.marshal(accept);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		hostroleid = _os_.unmarshal_long();
		accept = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAcceptOrRefuseInvitation) {
			CAcceptOrRefuseInvitation _o_ = (CAcceptOrRefuseInvitation)_o1_;
			if (hostroleid != _o_.hostroleid) return false;
			if (accept != _o_.accept) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)hostroleid;
		_h_ += (int)accept;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(hostroleid).append(",");
		_sb_.append(accept).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAcceptOrRefuseInvitation _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(hostroleid - _o_.hostroleid);
		if (0 != _c_) return _c_;
		_c_ = accept - _o_.accept;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

