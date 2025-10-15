package mkdb;

import mkdb.TRecord.State;
import mkdb.logs.AddRemoveInfo;
import mkdb.logs.LogNotify;

/**
 * ��񻺴�ӿ��ࡣ
 *
 * @param <K>
 * @param <V>
 */
public abstract class TTableCache<K, V> {
	private volatile TTable<K, V> table; // �ڳ�ʼ��ʱ����һ�Σ���֧���޸ġ�
	private volatile CacheRemovedHandle<K, V> removedhandle;
	private volatile int capacity;

	////////////////////////////////////////////////////////////////////
	// ����ͳ�ʼ����
	TTableCache() {
		// ȫ���ĳ�ʼ���Ͳ������ö�����initialize�����С�
		// ���ڹ��캯���г�ʼ����
	}

	/**
	 * ��ʼ��cache��
	 * <p><b>������������������Լ������úͳ�ʼ��ʵ�֡�</b>
	 * <p><b>��������ʱ�������super.initialize�Գ�ʼ�����ࡣ</b>
	 * @param table
	 * @param conf
	 */
	void initialize(TTable<K, V> table, TableConf conf) {
		this.table = table;
		this.capacity = conf.getCacheCapacity();
	}

	/**
	 * ���� cache ʵ������������
	 * @param <K>
	 * @param <V>
	 * @param table
	 * @return
	 */
	static <K, V> TTableCache<K, V> newInstance(TTable<K, V> table, TableConf conf) {
		String cacheClass = conf.getOtherAttr("cacheClass").trim();
		if (cacheClass.isEmpty()) {
			cacheClass = Mkdb.getInstance().getConf().getDefaultTableCache();
			if (cacheClass.isEmpty())
				//cacheClass = "mkdb.TTableCacheConcurrentMap"; // default cache implement
				cacheClass = "mkdb.TTableCacheLRU"; // default cache implement
		}

		try {
			@SuppressWarnings("unchecked")
			TTableCache<K, V> theCache = (TTableCache<K, V>)Class.forName(cacheClass).newInstance();
			theCache.initialize(table, conf);
			return theCache;
		} catch (Throwable e) {
			throw new XError(e);
		}
	}

	/**
	 * ��� cache��
	 * Removes all of the mappings from this map. The map will be empty after this call returns.
	 * ������еĻ����¼����ʹ��¼����ġ�
	 * @throws UnsupportedOperationException ���������ڴ��
	 */
	public abstract void clear();

	/**
	 * ���������档���������������ļ�¼�������ݲ��ᱻ����
	 */
	public abstract void clean();

	/**
	 * ���� cache�����ڳ־ñ��������ֻ�ܿ��������еļ�¼��
	 * <b>���������ʹ�á�</b>
	 * @see CacheQuery
	 */
	public abstract void walk(CacheQuery<K, V> query);

	/**
	 * �õ������еļ�¼���������ֵ�����ο������������߼��жϡ�
	 * @return ��¼����
	 */
	abstract int getSize();

	public final TTable<K, V> getTable() {
		return table;
	}

	/**
	 * @return ��������
	 */
	public final int getCapacity() {
		return this.capacity;
	}

	/**
	 * ���û���������
	 * @param capacity
	 */
	public synchronized void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	//////////////////////////////////////////////////////////////////
	// CacheRemovedHandle ��������ܿ��������Ǻ����á�
	/**
	 * �ڴ��ļ�¼�����������Ʊ��������ʱ����ص����õĽӿ�ͨ��Ӧ�á�
	 * <p>ע�⣺1. ֻ���ڴ����Ҫ�ص���2. ����ɾ������ص���
	 * @see CacheRemovedHandle
	 * @param handle
	 */
	public synchronized void setRemovedhandle(CacheRemovedHandle<K, V> handle) {
		this.removedhandle = handle;
	}

	/**
	 * 
	 * @return CacheRemovedHandle
	 */
	public final CacheRemovedHandle<K, V> getRemovedhandle() {
		return this.removedhandle;
	}

	// package private
	abstract TRecord<K, V> get(K k);
	abstract void addNoLog(K key, TRecord<K, V> r);
	abstract void add(K key, TRecord<K, V> r);
	abstract TRecord<K, V> remove(K k);

	/**
	 * ��������
	 * @param records
	 * @param query
	 */
	final void _walk_notify_query(Object[] records, CacheQuery<K, V> query) {
		for (Object _r : records) {
			@SuppressWarnings("unchecked")
			final TRecord<K, V> r = (TRecord<K, V>)_r; // unchecked
			final Lockey lock = r.getLockey();
			lock.lock();
			try {
				final V value = r.getValue();
				if (null != value) // ��ɾ���ļ�¼���ص���
					query.onQuery(Consts.toConst(r.getKey()), Consts.toConst(value));
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * �ڵ�ǰ�����������־������ӻ���ɾ������ʱ����cache����ӵļ�¼��Ҫ��¼��־��ʹ������ع�ʱ��������ݡ�
	 * <p><b>�����������������ʵ��</b>
	 * @param key
	 * @param r
	 */
	final void logAddRemove(K key, TRecord<K, V> r) {
		Transaction.currentSavepoint().add(r.getLogKey(), new LogAddRemove(key, r));
	}

	private class LogAddRemove implements Log {
		private K key;
		private State saved_state;
		private TRecord<K, V> record;

		LogAddRemove(K key, TRecord<K, V> record) {
			this.key         = key;
			this.record      = record;
			this.saved_state = record.getState();
		}

		@Override
		public void commit() {
			// �¼���Cache�ļ�¼����record��commit���xdbLogNotify�����ﲻ����
			if (!record.commit())
				TTableCache.this.remove(key);
			record.getTable().onRecordChanged(record, new LogNotify(new AddRemoveInfo(true, saved_state)));
		}

		@Override
		public void rollback() {
			if (record.rollback())
				TTableCache.this.remove(key);
		}
	}

	/**
	 * ɾ����¼������¼״̬����ʱ��
	 * <p><b>���棺</b>�����������remove(K key)���������ɾ����
	 * ���ܲ�������iterator��ѭ����ʹ�á�
	 * @param record
	 * @return
	 */
	final boolean tryRemoveRecord(TRecord<K, V> record) {
		// ���ּ�¼����ֻ�������������ʹ�õļ�¼�� 
		// ���� lru ���� access order��ͨ�����ϵļ�¼������ʹ���С�
		final Lockey lockey = record.getLockey();
		final K key = record.getKey();
		if (false == lockey.tryLock()) {
			Trace.debug("cleaner IS IT RARE? " + record + " size=" + getSize());
			// ԭ�������� continue����������ʧ�ܣ������˳�ѭ����ȽϺá�
			// һ����˵��ֻ���ڴ������ӻ��߱����޸ļ�¼ʱ�ᷢ�����������
			return false;
		}
		try {
			final TStorage<K, V> storage = record.getTable().getStorage();
			if (null == storage) {
				//Trace.debug("cleaner remove " + record + " size=" + lru.size());
				remove(key);
				// MEMORY ����һ���ص�,֪ͨһ��Ӧ�á�
				this.notifyRemovedhandle(record);
				return true;
			}

			if (storage.isChangedOrUnknown(key)) {
				//Trace.debug("cleaner isChanged " + record + " size=" + lru.size());
				// ԭ�������� continue�����ּ�¼��ʹ�û���״̬�޷��жϣ������˳�ѭ����ȽϺá�
				// һ����˵��ֻ���ڴ������ӻ��߱����޸ļ�¼ʱ�ᷢ�����������
				return false;
			}

			//Trace.debug("cleaner remove " + record + " size=" + lru.size());
			remove(key);
			return true;
		} finally {
			lockey.unlock();
		}
	}

	final void notifyRemovedhandle(TRecord<K, V> record) {
		final CacheRemovedHandle<K, V> handle = this.getRemovedhandle();
		if (null != handle)
			Mkdb.executor().execute(new Removed<K, V>(record, handle));
	}

	private static class Removed<K, V> implements Runnable {
		K key;
		V value;
		CacheRemovedHandle<K, V> handle;

		Removed(TRecord<K, V> r, CacheRemovedHandle<K, V> handle) {
			this.key = r.getKey();
			this.value = r.getValue();
			this.handle = handle;
		}

		@Override
		public void run() {
			handle.recordRemoved(key, value);
		}
	}

}
