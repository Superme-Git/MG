package com.locojoy.ns;

import mkdb.util.UniqName;
import xbean.NameState;

public class Release extends RpcProcedure {

	@Override
	protected int process(mkdb.TTable<String, NameState> table) {
		boolean exists = table.remove(getArgument().getName());
		mkdb.Trace.info(getClass().getName() + getArgument() + " exists=" + exists);
		return UniqName.RPC_OK; // ignore error. always ok
	}
}
