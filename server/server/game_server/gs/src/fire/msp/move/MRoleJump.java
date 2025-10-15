
package fire.msp.move;

import fire.pb.buff.BuffAgent;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MRoleJump__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MRoleJump extends __MRoleJump__ {
	@Override
	protected void process() {
		// protocol handle
		new mkdb.Procedure()
		{
			@Override
			protected boolean process() throws Exception{return true;}
			private void addBuffPower(BuffAgent buffrole){}
			
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724927;

	public int getType() {
		return 724927;
	}

	public long roleid;
	public byte jumpstate; // 0=停止时发送，1=跳跃开始时发送,2=二段跳时发送
	public byte minus; // 扣除的气力值，为正

	public MRoleJump() {
	}

	public MRoleJump(long _roleid_, byte _jumpstate_, byte _minus_) {
		this.roleid = _roleid_;
		this.jumpstate = _jumpstate_;
		this.minus = _minus_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(jumpstate);
		_os_.marshal(minus);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		jumpstate = _os_.unmarshal_byte();
		minus = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MRoleJump) {
			MRoleJump _o_ = (MRoleJump)_o1_;
			if (roleid != _o_.roleid) return false;
			if (jumpstate != _o_.jumpstate) return false;
			if (minus != _o_.minus) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)jumpstate;
		_h_ += (int)minus;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(jumpstate).append(",");
		_sb_.append(minus).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MRoleJump _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = jumpstate - _o_.jumpstate;
		if (0 != _c_) return _c_;
		_c_ = minus - _o_.minus;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

