package com.locojoy.ns;

import java.util.*;

import mkdb.util.UniqName;
import mkdb.util.UniqName.GroupId;

public final class AllocateId extends mkdb.Procedure implements mkio.rpc.Procedure<GroupId, mkio.rpc.LongInt> {

	private GroupId argument;
	private mkio.rpc.LongInt result;
	private mkio.Mkio from;

	public AllocateId() {
		argument = new GroupId();
		result = new mkio.rpc.LongInt(0, UniqName.RPC_EXCEPTION);
	}

	////////////////////////////////////////////////////////////
	// mkio.rpc.Procedure 实现
	@Override
	public void setArgument(GroupId a) {
		this.argument = a;
	}

	@Override
	public void setResult(mkio.rpc.LongInt r) {
		this.result = r;
	}

	@Override
	public GroupId getArgument() {
		return this.argument;
	}

	@Override
	public mkio.rpc.LongInt getResult() {
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

	//////////////////////////////////////////////////////////
	// id 范围配置.
	private static final class IdRange {
		private final long idmin;
		private final long idmax;

		IdRange(mkdb.TableConf conf) {
			this.idmin = conf.getOtherAttrLong("idmin", FREE_HEAD_KEY + 1);
			this.idmax = conf.getOtherAttrLong("idmax", Long.MAX_VALUE);
			if (this.idmin <= FREE_HEAD_KEY || this.idmax < this.idmin)
				throw new IllegalArgumentException("invalid id range! table=" + conf.getName());
		}

		final boolean validate(long id) {
			return id >= idmin && id < idmax;
		}
	}

	private static final Map<String, IdRange> idranges = new HashMap<String, IdRange>();

	public static final void initialize(mkdb.MkdbConf xconf) {
		for (mkdb.TableConf tconf : xconf.getTableConfs().values()) {
			if (tconf.getOtherAttr("value").equals("IdState"))
				idranges.put(tconf.getName(), new IdRange(tconf));
		}
	}

	public static final long NEXT_SEED_KEY = 0;
	public static final long FREE_HEAD_KEY = 1;

	////////////////////////////////////////////////////////////////
	// Mkdb.Procedure 实现
	@Override
	protected boolean process() throws Exception {
		// find table by group
		mkdb.Table table = mkdb.Mkdb.getInstance().getTables().getTable(getArgument().getGroup());
		if (null == table) {
			this.result.setValue(UniqName.RPC_GROUP_NOT_EXISTS);
			mkdb.Trace.warn(getClass().getName() + getArgument() + " result=" + result);
			return false;
		}

		// cast table to allocateId-table
		@SuppressWarnings("unchecked")
		mkdb.TTable<Long, xbean.IdState> idtable = (mkdb.TTable<Long, xbean.IdState>)table;

		// allocate
		IdRange idRange = idranges.get(idtable.getName());
		long nextId = 0;
		synchronized(idtable) { // 表锁。 see ReleaseId
			nextId = allocate(idtable, idRange);
		}

		// setup result
		this.result.setValue(nextId); // 总是返回分配得到的ID，不管是否正确。便于调试。
		if (idRange.validate(nextId)) {
			// 验证分配得到的Id并且设置结果代码。
			this.result.setCode(UniqName.RPC_OK);
			mkdb.Trace.info(getClass().getName() + getArgument() + " result=" + result);
			return true;
		}

		this.result.setCode(UniqName.RPC_ALLOCID_ERROR);
		mkdb.Trace.warn(getClass().getName() + getArgument() + " result=" + result);

		// 只在发生异常时才回滚。对于分配的逻辑错误（如，没有可用的ID）并不回滚。
		// 因为在分配过程中，即时分配失败，也可能更改了分配状态，此时仍然需要提交。
		//    [上帝保佑]，程序永远不要执行到这个分支。
		return true;
	}

	// inner helper 设置分配属性，不做任何判断。
	private final xbean.IdState allocate(xbean.IdState idFree) {
		idFree.setNextid(NEXT_SEED_KEY);
		idFree.setLocalid(this.getArgument().getLocalid());
		idFree.setPeerip(this.getConnection().getPeer().toString());
		idFree.setTime(System.currentTimeMillis());
		return idFree;
	}

	// 分配ID
	private final long allocate(mkdb.TTable<Long, xbean.IdState> idtable, IdRange idRange) {
		FreeList freeList = new FreeList(idtable);
		// 判断 expectId 是否未被分配过。
		if (idRange.validate(this.argument.getId())) {
			xbean.IdState idFree = idtable.get(this.argument.getId());
			if (null == idFree) {
				idtable.insert(this.argument.getId(), allocate(xbean.Pod.newIdState()));
				return this.argument.getId();
			}
			idFree = freeList.removeIfFree(idFree);
			if (null != idFree) {
				allocate(idFree);
				return this.argument.getId();
			}
			// expectId 已被占用。按一般方式分配。
		}

		// 查找空闲链表。
		for (xbean.IdState idFirst = freeList.removeFirst(); null != idFirst; idFirst = freeList.removeFirst()) {
			long freeId = idFirst.getNextid(); // 先取出暂存的，后面会修改。see freeList.removeFirst
			if (idRange.validate(freeId)) {
				allocate(idFirst);
				return freeId;
			}
			// 此分支是由于配置(idmin, idmax)改变引起的。否则不会进行循环，要嘛一次找到，要嘛为空。
			idtable.remove(freeId); // 从空闲链表中删除不合法的Id。
		}

		// allocate now ...
		xbean.IdState seed = idtable.get(NEXT_SEED_KEY);
		if (null == seed) {
			// 第一次分配。初始化并加入到表中。
			seed = xbean.Pod.newIdState();
			allocate(seed); // 在种子里面记录一些信息。
			seed.setNextid(idRange.idmin); // 初始化为第一个可用id
			idtable.insert(NEXT_SEED_KEY, seed);
		}
		if (seed.getNextid() < idRange.idmin)
			seed.setNextid(idRange.idmin); // 配置范围最小值提高到当前种子后面。修订一次。

		// 放到外面是为了优化循环。不过循环应该几乎不发生。就这么放着吧。
		xbean.IdState idAlloc = xbean.Pod.newIdState();
		// 此循环很恐怖，最坏的情况下会扫描完所有的Id空间。增加一个 maxTry 来控制循环次数?
		for (long allocId = seed.getNextid(); allocId < idRange.idmax; ++ allocId ) {
			if (idtable.add(allocId, idAlloc)) {
				seed.setNextid(allocId + 1); // 递增seed。
				allocate(idAlloc);           // 设置当前空闲ID为“已分配”。
				return allocId;
			}
			/* allocId 已经存在。这是由于 expectId 提前占用了 seed 之后的位置造成的。此时Id有两种状态。
			 * （1）Id不是空闲的，此时只能往后找，这个没得说；
			 * （2）Id是空闲的，就是说 expectId 已经被 Release 了。
			 *     此时有个办法避免这种情况发生。在Release的时候，如果发现Id在seed后面，
			 *     则直接删除而不链接到空闲链表中。
			 */
			// 情况（2）已经避免掉的情况下，下面的处理是没有意义的。会浪费一次 idtable.get 操作。
			for (xbean.IdState idFree = freeList.removeIfFree(idtable.get(allocId)); null != idFree; /**/) {
				allocate(idFree);
				return allocId;
			} // for 在这里实际上不是循环。仅仅是局部变量限制在本地，不扩散变量的范围。
		}
		seed.setNextid(idRange.idmax); // 查找失败。
		return NEXT_SEED_KEY; // allocate error
	}

	/**
	 * Id空闲链表。
	 * 
	 */
	static final class FreeList {
		private final mkdb.TTable<Long, xbean.IdState> idList;

		public FreeList(final mkdb.TTable<Long, xbean.IdState> idtable) {
			this.idList = idtable;
		}

		/**
		 * 如果节点是空闲的，删除节点并返回节点，否则返回null。
		 * @param idFree
		 * @return idFree if idFree is free，or null if idFree is allocated.
		 */
		public xbean.IdState removeIfFree(xbean.IdState idFree) {
			if (AllocateId.NEXT_SEED_KEY == idFree.getNextid())
				return null;

			xbean.IdState idNext = idList.get(idFree.getNextid());
			xbean.IdState idPrev = idList.get(idFree.getTime());

			if (null != idNext && null != idPrev 
					&& idNext.getNextid() != AllocateId.NEXT_SEED_KEY
					&& idPrev.getNextid() != AllocateId.NEXT_SEED_KEY) {
				// remove from list
				idNext.setTime(idFree.getTime());
				idPrev.setNextid(idFree.getNextid());
				return idFree;
			}
			// reset list
			xbean.IdState head = idList.get(AllocateId.FREE_HEAD_KEY);
			if (null != head) {
				mkdb.Trace.error(this.getClass().getName() + ".remove: broken!");
				head.setNextid(AllocateId.FREE_HEAD_KEY);
				head.setTime(AllocateId.FREE_HEAD_KEY);
			} else
				mkdb.Trace.fatal(this.getClass().getName() + ".remove: no head!");
			return idFree;
		}

		/**
		 * 移出第一个节点并返回。返回的时候把这个节点的id保存在 IdState.nextid 中。
		 * @return null if empty or error;
		 */
		public xbean.IdState removeFirst() {
			xbean.IdState head = idList.get(AllocateId.FREE_HEAD_KEY);
			if (null == head) // 链表还没建立，相当于为空。
				return null;

			long firstId = head.getNextid();
			if (AllocateId.FREE_HEAD_KEY == firstId) // 链表为空
				return null;

			xbean.IdState idFirst = idList.get(firstId);
			if (null == idFirst) {
				// 空闲链表错误：指向的ID不存在。把链表置空。跳出循环。
				head.setNextid(AllocateId.FREE_HEAD_KEY);
				head.setTime(AllocateId.FREE_HEAD_KEY);
				mkdb.Trace.error(this.getClass().getName() + ".removeFirst: broken first!");
				return null;
			}

			if (AllocateId.NEXT_SEED_KEY == idFirst.getNextid()) {
				// 空闲链表错误：指向的ID已经被分配了。把链表置空。跳出循环。
				head.setNextid(AllocateId.FREE_HEAD_KEY);
				head.setTime(AllocateId.FREE_HEAD_KEY);
				mkdb.Trace.error(this.getClass().getName() + ".removeFirst: dirty!");
				return null;
			}

			xbean.IdState idNext = idList.get(idFirst.getNextid());
			if (null == idNext) {
				head.setNextid(AllocateId.FREE_HEAD_KEY);
				head.setTime(AllocateId.FREE_HEAD_KEY);
				mkdb.Trace.error(this.getClass().getName() + ".removeFirst: broken next!");
				return null;
			}

			idNext.setTime(AllocateId.FREE_HEAD_KEY);
			head.setNextid(idFirst.getNextid());

			idFirst.setNextid(firstId); // 把id临时保存在这里返回。
			return idFirst;
		}

		/**
		 * 把存在的节点idState加到链表头。
		 * @param id
		 * @param idState
		 */
		public void addFirst(long id, xbean.IdState idState) {
			if (AllocateId.NEXT_SEED_KEY != idState.getNextid())
				throw new IllegalArgumentException(this.getClass().getName() + ".addFirst: NOTALLOCATED!");

			xbean.IdState head = idList.get(AllocateId.FREE_HEAD_KEY);
			if (null == head) {
				// 第一次释放。创建链表。
				head = xbean.Pod.newIdState();
				head.setNextid(AllocateId.FREE_HEAD_KEY);
				head.setTime(AllocateId.FREE_HEAD_KEY);
				idList.insert(AllocateId.FREE_HEAD_KEY, head);
			}

			long nextId = head.getNextid();
			xbean.IdState idNext = idList.get(nextId);
			if (null == idNext) {
				// 链表错误，置为空表。
				head.setNextid(AllocateId.FREE_HEAD_KEY);
				head.setTime(AllocateId.FREE_HEAD_KEY);
				idNext = head;
				nextId = AllocateId.FREE_HEAD_KEY;
				mkdb.Trace.error(this.getClass().getName() + ".addFirst: broken!");
			}
			// previous
			idNext.setTime(id);
			idState.setTime(AllocateId.FREE_HEAD_KEY);
			// next
			idState.setNextid(nextId);
			head.setNextid(id);
		}

		private class FreeIterator implements java.util.Iterator<Long> {
			private Long next = null;

			FreeIterator() {
				xbean.IdState head = idList.get(AllocateId.FREE_HEAD_KEY);
				if (null == head) // 链表还没建立，相当于为空。
					return;

				long firstId = head.getNextid();
				if (AllocateId.FREE_HEAD_KEY == firstId) // 链表为空
					return;

				next = firstId;
			}

			@Override
			public boolean hasNext() {
				return null != next;
			}

			@Override
			public Long next() {
				if (null == next)
					throw new IllegalStateException();
				Long current = next;
				next = idList.get(next).getNextid();
				if (next == AllocateId.FREE_HEAD_KEY) // last
					next = null;
				return current;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}

		/**
		 * must in procedure
		 * @return
		 */
		public java.util.Iterator<Long> iterator() {
			return new FreeIterator();
		}
	}
}
