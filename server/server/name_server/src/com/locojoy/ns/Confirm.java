package com.locojoy.ns;

import mkdb.util.UniqName;
import xbean.NameState;

public class Confirm extends RpcProcedure {

	@Override
	protected int process(mkdb.TTable<String, NameState> table) {
		xbean.NameState value = table.get(getArgument().getName());
		if (null == value)
			return UniqName.RPC_NOT_EXISTS;

		if (value.getState() != xbean.NameState.STATE_ALLOCATE)
			return UniqName.RPC_CONFIRM_STATE;

		value.setState(xbean.NameState.STATE_CONFIRM);
		return UniqName.RPC_OK;
	}
}
