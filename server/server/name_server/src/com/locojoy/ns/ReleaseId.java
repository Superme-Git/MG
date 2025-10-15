package com.locojoy.ns;

import mkdb.util.UniqName;
import mkdb.util.UniqName.GroupId;

public final class ReleaseId extends mkdb.Procedure implements mkio.rpc.Procedure<GroupId, mkio.rpc.Int> {
	private GroupId argument;
	private mkio.rpc.Int result;
	private mkio.Mkio from;

	public ReleaseId() {
		argument = new GroupId();
		result = new mkio.rpc.Int(UniqName.RPC_EXCEPTION);
	}

	////////////////////////////////////////////////////////////
	// mkio.rpc.Procedure 实现
	@Override
	public void setArgument(GroupId a) {
		this.argument = a;
	}

	@Override
	public void setResult(mkio.rpc.Int r) {
		this.result = r;
	}

	@Override
	public GroupId getArgument() {
		return this.argument;
	}

	@Override
	public mkio.rpc.Int getResult() {
		return this.result;
	}

	@Override
	public void setConnection(mkio.Mkio from) {
		this.from = from;
	}

	@Override
	public mkio.Mkio getConnection() {
		return from;
	}

	////////////////////////////////////////////////////////////////
	// Mkdb.Procedure 实现
	@Override
	protected boolean process() throws Exception {
		// find table by group
		mkdb.Table table = mkdb.Mkdb.getInstance().getTables().getTable(this.argument.getGroup());
		if (null == table) {
			this.result.setValue(UniqName.RPC_GROUP_NOT_EXISTS);
			mkdb.Trace.info(getClass().getName() + this.argument + " result=" + result);
			return false;
		}

		// always OK
		this.result.setValue(UniqName.RPC_OK);

		// cast table to allocateId-table
		@SuppressWarnings("unchecked")
		mkdb.TTable<Long, xbean.IdState> idtable = (mkdb.TTable<Long, xbean.IdState>)table;

		// release ...
		long releaseId = argument.getId();
		synchronized(idtable) { // 表锁。see AllocateId
			xbean.IdState idRelease = idtable.get(releaseId);
			if (null == idRelease) {
				mkdb.Trace.warn(getClass().getName() + argument + " NOTEXIST!");
				return true;
			}

			if (AllocateId.NEXT_SEED_KEY != idRelease.getNextid()) {
				mkdb.Trace.warn(getClass().getName() + argument + " NOTALLOCATED!");
				return true;
			}

			// remove directly OR
			xbean.IdState seed = idtable.get(AllocateId.NEXT_SEED_KEY);
			if (null == seed) {
				mkdb.Trace.fatal(getClass().getName() + argument + " IMPOSSIBLE!");
				// release 时， idNextSeed 不可能为null。记个 log 继续往下走。

			} else if (releaseId >= seed.getNextid()) {

				// 直接删除，不加入到空闲链表中。参考 AllocateId.allocate。
				idtable.remove(releaseId);
				mkdb.Trace.info(getClass().getName() + argument + " REMOVE!");
				return true;
			}

			new AllocateId.FreeList(idtable).addFirst(releaseId, idRelease);
		}

		mkdb.Trace.info(getClass().getName() + argument + " ADDTOLIST");
		return true;
	}
}
