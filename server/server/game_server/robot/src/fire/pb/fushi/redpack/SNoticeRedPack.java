package fire.pb.fushi.redpack;

import robot.LoginRole;
import robot.LoginRoleMgr;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SNoticeRedPack__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class SNoticeRedPack extends __SNoticeRedPack__ {

	@Override
	protected void process() {
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if (role != null && role.getRoleTask() != null)
			role.getRoleTask().processProtocol(this);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812542;

	public int getType() {
		return 812542;
	}

	public fire.pb.fushi.redpack.RedPackRoleTip redpackroletip; // 红包信息

	public SNoticeRedPack() {
		redpackroletip = new fire.pb.fushi.redpack.RedPackRoleTip();
	}

	public SNoticeRedPack(fire.pb.fushi.redpack.RedPackRoleTip _redpackroletip_) {
		this.redpackroletip = _redpackroletip_;
	}

	public final boolean _validator_() {
		if (!redpackroletip._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(redpackroletip);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		redpackroletip.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SNoticeRedPack) {
			SNoticeRedPack _o_ = (SNoticeRedPack)_o1_;
			if (!redpackroletip.equals(_o_.redpackroletip)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += redpackroletip.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(redpackroletip).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}
