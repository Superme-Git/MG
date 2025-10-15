package com.locojoy.ns;

import mkdb.util.UniqName;
import mkdb.util.UniqName.GroupName;

public abstract class RpcProcedure extends mkdb.Procedure
	implements mkio.rpc.Procedure<GroupName, mkio.rpc.Int> {

	private GroupName argument;
	private mkio.rpc.Int result;
	private mkio.Mkio from;

	RpcProcedure() {
		argument = new GroupName();
		result = new mkio.rpc.Int(UniqName.RPC_EXCEPTION);
	}

	@Override
	public void setArgument(GroupName a) {
		argument = a;
	}

	@Override
	public void setResult(mkio.rpc.Int r) {
		result = r;
	}

	@Override
	public void setConnection(mkio.Mkio from) {
		this.from = from;
	}

	@Override
	public GroupName getArgument() {
		return argument;
	}

	@Override
	public mkio.rpc.Int getResult() {
		return result;
	}

	@Override
	public mkio.Mkio getConnection() {
		return from;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean process() throws Exception {
		mkdb.Table table = mkdb.Mkdb.getInstance().getTables().getTable(getArgument().getGroup());
		if (null == table) {
			this.getResult().setValue(UniqName.RPC_GROUP_NOT_EXISTS);
			mkdb.Trace.info(getClass().getName() + getArgument() + " result=" + result);
			return false;
		}
		this.getResult().setValue(process((mkdb.TTable<String, xbean.NameState>)table));
		if (UniqName.RPC_OK == this.getResult().getValue())
			return true;

		mkdb.Trace.info(getClass().getName() + getArgument() + " result=" + result);
		return false;
	}

	// 子类中处理，返回处理结果。
	protected abstract int process(mkdb.TTable<String, xbean.NameState> table);
}
