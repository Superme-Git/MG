
package fire.pb.battle.livedie;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SInvitationLiveDieBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SInvitationLiveDieBattle extends __SInvitationLiveDieBattle__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793834;

	public int getType() {
		return 793834;
	}

	public long objectid;
	public java.lang.String objectname;
	public int selecttype; // 0单人   1组队
	public int costmoney;

	public SInvitationLiveDieBattle() {
		objectname = "";
	}

	public SInvitationLiveDieBattle(long _objectid_, java.lang.String _objectname_, int _selecttype_, int _costmoney_) {
		this.objectid = _objectid_;
		this.objectname = _objectname_;
		this.selecttype = _selecttype_;
		this.costmoney = _costmoney_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(objectid);
		_os_.marshal(objectname, "UTF-16LE");
		_os_.marshal(selecttype);
		_os_.marshal(costmoney);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		objectid = _os_.unmarshal_long();
		objectname = _os_.unmarshal_String("UTF-16LE");
		selecttype = _os_.unmarshal_int();
		costmoney = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SInvitationLiveDieBattle) {
			SInvitationLiveDieBattle _o_ = (SInvitationLiveDieBattle)_o1_;
			if (objectid != _o_.objectid) return false;
			if (!objectname.equals(_o_.objectname)) return false;
			if (selecttype != _o_.selecttype) return false;
			if (costmoney != _o_.costmoney) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)objectid;
		_h_ += objectname.hashCode();
		_h_ += selecttype;
		_h_ += costmoney;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(objectid).append(",");
		_sb_.append("T").append(objectname.length()).append(",");
		_sb_.append(selecttype).append(",");
		_sb_.append(costmoney).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

