package com.locojoy.ns;

import mkdb.util.UniqName;
import xbean.NameState;

public final class Allocate extends RpcProcedure {

	@Override
	protected int process(mkdb.TTable<String, NameState> table) {
		mkdb.Trace.debug(getClass().getName() + getArgument());

		xbean.NameState value = xbean.Pod.newNameState();
		value.setLocalid(getArgument().getLocalid());
		value.setPeerip(getConnection().getPeer().toString());
		value.setState(xbean.NameState.STATE_ALLOCATE);
		value.setTime(System.currentTimeMillis());

		return table.add(getArgument().getName(), value) ? UniqName.RPC_OK : UniqName.RPC_DUPLICATE;
	}
}
