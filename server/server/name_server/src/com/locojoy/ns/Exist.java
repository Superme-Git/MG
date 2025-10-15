package com.locojoy.ns;

import mkdb.TTable;
import mkdb.util.UniqName;
import xbean.NameState;

public class Exist extends RpcProcedure {

	@Override
	protected int process(TTable<String, NameState> table) {
		if (table.get(this.getArgument().getName()) != null)
			return UniqName.RPC_OK; // 只检查记录是否存在，不考虑当前名字的状态。
		return UniqName.RPC_NOT_EXISTS;
	}

}
