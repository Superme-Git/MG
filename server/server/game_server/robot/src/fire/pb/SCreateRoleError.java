
package fire.pb;

import java.util.concurrent.TimeUnit;

import robot.CreateRole;
import robot.LoginUI;
import mkio.ILoginIns;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SCreateRoleError__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SCreateRoleError extends __SCreateRoleError__ {
	
	static private class DelayCreateRole implements Runnable {

		private ILoginIns	instance;
		
		public DelayCreateRole( ILoginIns ins) {
			instance = ins;
		}
		@Override
		public void run() {
			//String name = ((LoginUI)instance.getLoginUI()).getUserName().substring(5);
			String name = ((LoginUI)instance.getLoginUI()).getLoginInstance().getParam().username;
			instance.send( CreateRole.generateCCreateRole(name));
		}
	}
	
	@Override
	protected void process() {
		mkdb.Trace.info("CreateRoleError : " + err);
		mkdb.Executor.getInstance().schedule( new DelayCreateRole( (ILoginIns)this.getContext()), 10, TimeUnit.SECONDS);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786440;

	public int getType() {
		return 786440;
	}

	public final static int CREATE_OK = 1; // 成功
	public final static int CREATE_ERROR = 2; // 失败
	public final static int CREATE_INVALID = 3; // 名称不合法
	public final static int CREATE_DUPLICATED = 4; // 重名
	public final static int CREATE_OVERCOUNT = 5; // 创建的新角色数量过多
	public final static int CREATE_OVERLEN = 6; // 角色名过长
	public final static int CREATE_SHORTLEN = 7; // 角色名过短
	public final static int CREATE_CREATE_GM_FORBID = 8; // GM禁止

	public int err; // 创建结果

	public SCreateRoleError() {
	}

	public SCreateRoleError(int _err_) {
		this.err = _err_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(err);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		err = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SCreateRoleError) {
			SCreateRoleError _o_ = (SCreateRoleError)_o1_;
			if (err != _o_.err) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += err;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(err).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SCreateRoleError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = err - _o_.err;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

