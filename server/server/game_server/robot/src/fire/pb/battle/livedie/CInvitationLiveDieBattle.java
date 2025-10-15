
package fire.pb.battle.livedie;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CInvitationLiveDieBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CInvitationLiveDieBattle extends __CInvitationLiveDieBattle__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793833;

	public int getType() {
		return 793833;
	}

	public java.lang.String idorname;
	public int selecttype; // 0单人   1组队

	public CInvitationLiveDieBattle() {
		idorname = "";
	}

	public CInvitationLiveDieBattle(java.lang.String _idorname_, int _selecttype_) {
		this.idorname = _idorname_;
		this.selecttype = _selecttype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(idorname, "UTF-16LE");
		_os_.marshal(selecttype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		idorname = _os_.unmarshal_String("UTF-16LE");
		selecttype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CInvitationLiveDieBattle) {
			CInvitationLiveDieBattle _o_ = (CInvitationLiveDieBattle)_o1_;
			if (!idorname.equals(_o_.idorname)) return false;
			if (selecttype != _o_.selecttype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += idorname.hashCode();
		_h_ += selecttype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(idorname.length()).append(",");
		_sb_.append(selecttype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

