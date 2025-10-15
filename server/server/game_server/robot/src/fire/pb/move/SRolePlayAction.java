
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRolePlayAction__ extends mkio.Protocol { }

/** 服务器广播角色做某个动作
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRolePlayAction extends __SRolePlayAction__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790488;

	public int getType() {
		return 790488;
	}

	public long roleid;
	public byte actionid; // 如果为0，则为取消当前的持续性动作

	public SRolePlayAction() {
	}

	public SRolePlayAction(long _roleid_, byte _actionid_) {
		this.roleid = _roleid_;
		this.actionid = _actionid_;
	}

	public final boolean _validator_() {
		if (roleid < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(actionid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		actionid = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRolePlayAction) {
			SRolePlayAction _o_ = (SRolePlayAction)_o1_;
			if (roleid != _o_.roleid) return false;
			if (actionid != _o_.actionid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)actionid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(actionid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRolePlayAction _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = actionid - _o_.actionid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

