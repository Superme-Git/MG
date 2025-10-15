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
	// mkio.rpc.Procedure ʵ��
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
	// id ��Χ����.
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
	// Mkdb.Procedure ʵ��
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
		synchronized(idtable) { // ������ see ReleaseId
			nextId = allocate(idtable, idRange);
		}

		// setup result
		this.result.setValue(nextId); // ���Ƿ��ط���õ���ID�������Ƿ���ȷ�����ڵ��ԡ�
		if (idRange.validate(nextId)) {
			// ��֤����õ���Id�������ý�����롣
			this.result.setCode(UniqName.RPC_OK);
			mkdb.Trace.info(getClass().getName() + getArgument() + " result=" + result);
			return true;
		}

		this.result.setCode(UniqName.RPC_ALLOCID_ERROR);
		mkdb.Trace.warn(getClass().getName() + getArgument() + " result=" + result);

		// ֻ�ڷ����쳣ʱ�Żع������ڷ�����߼������磬û�п��õ�ID�������ع���
		// ��Ϊ�ڷ�������У���ʱ����ʧ�ܣ�Ҳ���ܸ����˷���״̬����ʱ��Ȼ��Ҫ�ύ��
		//    [�ϵ۱���]��������Զ��Ҫִ�е������֧��
		return true;
	}

	// inner helper ���÷������ԣ������κ��жϡ�
	private final xbean.IdState allocate(xbean.IdState idFree) {
		idFree.setNextid(NEXT_SEED_KEY);
		idFree.setLocalid(this.getArgument().getLocalid());
		idFree.setPeerip(this.getConnection().getPeer().toString());
		idFree.setTime(System.currentTimeMillis());
		return idFree;
	}

	// ����ID
	private final long allocate(mkdb.TTable<Long, xbean.IdState> idtable, IdRange idRange) {
		FreeList freeList = new FreeList(idtable);
		// �ж� expectId �Ƿ�δ���������
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
			// expectId �ѱ�ռ�á���һ�㷽ʽ���䡣
		}

		// ���ҿ�������
		for (xbean.IdState idFirst = freeList.removeFirst(); null != idFirst; idFirst = freeList.removeFirst()) {
			long freeId = idFirst.getNextid(); // ��ȡ���ݴ�ģ�������޸ġ�see freeList.removeFirst
			if (idRange.validate(freeId)) {
				allocate(idFirst);
				return freeId;
			}
			// �˷�֧����������(idmin, idmax)�ı�����ġ����򲻻����ѭ����Ҫ��һ���ҵ���Ҫ��Ϊ�ա�
			idtable.remove(freeId); // �ӿ���������ɾ�����Ϸ���Id��
		}

		// allocate now ...
		xbean.IdState seed = idtable.get(NEXT_SEED_KEY);
		if (null == seed) {
			// ��һ�η��䡣��ʼ�������뵽���С�
			seed = xbean.Pod.newIdState();
			allocate(seed); // �����������¼һЩ��Ϣ��
			seed.setNextid(idRange.idmin); // ��ʼ��Ϊ��һ������id
			idtable.insert(NEXT_SEED_KEY, seed);
		}
		if (seed.getNextid() < idRange.idmin)
			seed.setNextid(idRange.idmin); // ���÷�Χ��Сֵ��ߵ���ǰ���Ӻ��档�޶�һ�Ρ�

		// �ŵ�������Ϊ���Ż�ѭ��������ѭ��Ӧ�ü���������������ô���Űɡ�
		xbean.IdState idAlloc = xbean.Pod.newIdState();
		// ��ѭ���ֲܿ����������»�ɨ�������е�Id�ռ䡣����һ�� maxTry ������ѭ������?
		for (long allocId = seed.getNextid(); allocId < idRange.idmax; ++ allocId ) {
			if (idtable.add(allocId, idAlloc)) {
				seed.setNextid(allocId + 1); // ����seed��
				allocate(idAlloc);           // ���õ�ǰ����IDΪ���ѷ��䡱��
				return allocId;
			}
			/* allocId �Ѿ����ڡ��������� expectId ��ǰռ���� seed ֮���λ����ɵġ���ʱId������״̬��
			 * ��1��Id���ǿ��еģ���ʱֻ�������ң����û��˵��
			 * ��2��Id�ǿ��еģ�����˵ expectId �Ѿ��� Release �ˡ�
			 *     ��ʱ�и��취�������������������Release��ʱ���������Id��seed���棬
			 *     ��ֱ��ɾ���������ӵ����������С�
			 */
			// �����2���Ѿ������������£�����Ĵ�����û������ġ����˷�һ�� idtable.get ������
			for (xbean.IdState idFree = freeList.removeIfFree(idtable.get(allocId)); null != idFree; /**/) {
				allocate(idFree);
				return allocId;
			} // for ������ʵ���ϲ���ѭ���������Ǿֲ����������ڱ��أ�����ɢ�����ķ�Χ��
		}
		seed.setNextid(idRange.idmax); // ����ʧ�ܡ�
		return NEXT_SEED_KEY; // allocate error
	}

	/**
	 * Id��������
	 * 
	 */
	static final class FreeList {
		private final mkdb.TTable<Long, xbean.IdState> idList;

		public FreeList(final mkdb.TTable<Long, xbean.IdState> idtable) {
			this.idList = idtable;
		}

		/**
		 * ����ڵ��ǿ��еģ�ɾ���ڵ㲢���ؽڵ㣬���򷵻�null��
		 * @param idFree
		 * @return idFree if idFree is free��or null if idFree is allocated.
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
		 * �Ƴ���һ���ڵ㲢���ء����ص�ʱ�������ڵ��id������ IdState.nextid �С�
		 * @return null if empty or error;
		 */
		public xbean.IdState removeFirst() {
			xbean.IdState head = idList.get(AllocateId.FREE_HEAD_KEY);
			if (null == head) // ����û�������൱��Ϊ�ա�
				return null;

			long firstId = head.getNextid();
			if (AllocateId.FREE_HEAD_KEY == firstId) // ����Ϊ��
				return null;

			xbean.IdState idFirst = idList.get(firstId);
			if (null == idFirst) {
				// �����������ָ���ID�����ڡ��������ÿա�����ѭ����
				head.setNextid(AllocateId.FREE_HEAD_KEY);
				head.setTime(AllocateId.FREE_HEAD_KEY);
				mkdb.Trace.error(this.getClass().getName() + ".removeFirst: broken first!");
				return null;
			}

			if (AllocateId.NEXT_SEED_KEY == idFirst.getNextid()) {
				// �����������ָ���ID�Ѿ��������ˡ��������ÿա�����ѭ����
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

			idFirst.setNextid(firstId); // ��id��ʱ���������ﷵ�ء�
			return idFirst;
		}

		/**
		 * �Ѵ��ڵĽڵ�idState�ӵ�����ͷ��
		 * @param id
		 * @param idState
		 */
		public void addFirst(long id, xbean.IdState idState) {
			if (AllocateId.NEXT_SEED_KEY != idState.getNextid())
				throw new IllegalArgumentException(this.getClass().getName() + ".addFirst: NOTALLOCATED!");

			xbean.IdState head = idList.get(AllocateId.FREE_HEAD_KEY);
			if (null == head) {
				// ��һ���ͷš���������
				head = xbean.Pod.newIdState();
				head.setNextid(AllocateId.FREE_HEAD_KEY);
				head.setTime(AllocateId.FREE_HEAD_KEY);
				idList.insert(AllocateId.FREE_HEAD_KEY, head);
			}

			long nextId = head.getNextid();
			xbean.IdState idNext = idList.get(nextId);
			if (null == idNext) {
				// ���������Ϊ�ձ�
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
				if (null == head) // ����û�������൱��Ϊ�ա�
					return;

				long firstId = head.getNextid();
				if (AllocateId.FREE_HEAD_KEY == firstId) // ����Ϊ��
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
