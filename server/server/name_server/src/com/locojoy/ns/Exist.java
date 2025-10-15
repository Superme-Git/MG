package com.locojoy.ns;

import mkdb.TTable;
import mkdb.util.UniqName;
import xbean.NameState;

public class Exist extends RpcProcedure {

	@Override
	protected int process(TTable<String, NameState> table) {
		if (table.get(this.getArgument().getName()) != null)
			return UniqName.RPC_OK; // ֻ����¼�Ƿ���ڣ������ǵ�ǰ���ֵ�״̬��
		return UniqName.RPC_NOT_EXISTS;
	}

}
