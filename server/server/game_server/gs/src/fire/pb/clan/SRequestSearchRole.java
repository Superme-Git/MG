
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestSearchRole__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestSearchRole extends __SRequestSearchRole__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808523;

	public int getType() {
		return 808523;
	}

	public fire.pb.clan.InvitationRoleInfo invitationroleinfolist;

	public SRequestSearchRole() {
		invitationroleinfolist = new fire.pb.clan.InvitationRoleInfo();
	}

	public SRequestSearchRole(fire.pb.clan.InvitationRoleInfo _invitationroleinfolist_) {
		this.invitationroleinfolist = _invitationroleinfolist_;
	}

	public final boolean _validator_() {
		if (!invitationroleinfolist._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(invitationroleinfolist);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		invitationroleinfolist.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestSearchRole) {
			SRequestSearchRole _o_ = (SRequestSearchRole)_o1_;
			if (!invitationroleinfolist.equals(_o_.invitationroleinfolist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += invitationroleinfolist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(invitationroleinfolist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

