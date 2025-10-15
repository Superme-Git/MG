
package openau;

import com.locojoy.sdk.PlatManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS

abstract class __AuAnyLogin__ extends mkio.Rpc<openau.AuAnyLoginArg, openau.AuAnyLoginRes> { }
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class AuAnyLogin extends __AuAnyLogin__ {
	@Override
	protected void onServer() {
		PlatManager.onAuAnyLogin(getConnection(), getArgument(), getResult());
	}

	@Override
	protected void onClient() {
		// response handle
	}

	@Override
	protected void onTimeout(int code) {
		// client only. 当使用 submit 方式调用 rpc 时，不会产生这个回调。
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public int getType() {
		return 8903;
	}

	public AuAnyLogin() {
		super.setArgument(new openau.AuAnyLoginArg());
		super.setResult(new openau.AuAnyLoginRes());
	}

	public AuAnyLogin(openau.AuAnyLoginArg argument) {
		super.setArgument(argument);
		super.setResult(new openau.AuAnyLoginRes());
	}

	public int getTimeout() {
		return 1000 * 20;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}

