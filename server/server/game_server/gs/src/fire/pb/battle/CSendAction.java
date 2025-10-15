
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSendAction__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSendAction extends __CSendAction__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(-1 == roleid)
			return;
		new PRoleOperation(roleid, (isrole == 1), action,(autooperate == 1)).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793436;

	public int getType() {
		return 793436;
	}

	public fire.pb.battle.Operation action; // 操作
	public byte isrole; // 角色操作=1，宠物操作=0
	public byte autooperate; // 为1代表是自动，默认为0

	public CSendAction() {
		action = new fire.pb.battle.Operation();
	}

	public CSendAction(fire.pb.battle.Operation _action_, byte _isrole_, byte _autooperate_) {
		this.action = _action_;
		this.isrole = _isrole_;
		this.autooperate = _autooperate_;
	}

	public final boolean _validator_() {
		if (!action._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(action);
		_os_.marshal(isrole);
		_os_.marshal(autooperate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		action.unmarshal(_os_);
		isrole = _os_.unmarshal_byte();
		autooperate = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSendAction) {
			CSendAction _o_ = (CSendAction)_o1_;
			if (!action.equals(_o_.action)) return false;
			if (isrole != _o_.isrole) return false;
			if (autooperate != _o_.autooperate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += action.hashCode();
		_h_ += (int)isrole;
		_h_ += (int)autooperate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(action).append(",");
		_sb_.append(isrole).append(",");
		_sb_.append(autooperate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSendAction _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = action.compareTo(_o_.action);
		if (0 != _c_) return _c_;
		_c_ = isrole - _o_.isrole;
		if (0 != _c_) return _c_;
		_c_ = autooperate - _o_.autooperate;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

