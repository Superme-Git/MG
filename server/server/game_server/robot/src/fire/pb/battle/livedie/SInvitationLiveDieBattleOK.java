
package fire.pb.battle.livedie;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SInvitationLiveDieBattleOK__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SInvitationLiveDieBattleOK extends __SInvitationLiveDieBattleOK__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793836;

	public int getType() {
		return 793836;
	}

	public long sourceid;
	public java.lang.String sourcename;
	public int selecttype; // 0单人   1组队

	public SInvitationLiveDieBattleOK() {
		sourcename = "";
	}

	public SInvitationLiveDieBattleOK(long _sourceid_, java.lang.String _sourcename_, int _selecttype_) {
		this.sourceid = _sourceid_;
		this.sourcename = _sourcename_;
		this.selecttype = _selecttype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(sourceid);
		_os_.marshal(sourcename, "UTF-16LE");
		_os_.marshal(selecttype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		sourceid = _os_.unmarshal_long();
		sourcename = _os_.unmarshal_String("UTF-16LE");
		selecttype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SInvitationLiveDieBattleOK) {
			SInvitationLiveDieBattleOK _o_ = (SInvitationLiveDieBattleOK)_o1_;
			if (sourceid != _o_.sourceid) return false;
			if (!sourcename.equals(_o_.sourcename)) return false;
			if (selecttype != _o_.selecttype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)sourceid;
		_h_ += sourcename.hashCode();
		_h_ += selecttype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(sourceid).append(",");
		_sb_.append("T").append(sourcename.length()).append(",");
		_sb_.append(selecttype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

