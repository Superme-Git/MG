package mkdb;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

/**
 * 
 * ���� LinkedHashMap �� TTableCache ʵ�֡�
 * 
 * @author cn
 *
 * @param <K>
 * @param <V>
 */
public final class TTableCacheLRU<K, V> extends TTableCache<K, V> {

	private LinkedHashMap<K, TRecord<K, V>> lru;

	///////////////////////////////////////////////////////////////////////////
	@Override
	void initialize(mkdb.TTable<K, V> table, TableConf conf) {
		super.initialize(table, conf); // ���Ǳ���ġ�

		lru = new LinkedHashMap<K, TRecord<K, V>>(16, 0.75f, true) {
			static final long serialVersionUID = 3287086177585999328L;

			@Override
			protected boolean removeEldestEntry(Entry<K, TRecord<K, V>> eldest) {
				final int capacity = getCapacity();
				if (capacity <= 0)
					return false; // ��������

				if (size() <= capacity)
					return false; // �����㹻

				cleaner.start();
				return false; // let cleaner do it
			}
		};
	}

	private final Cleaner cleaner = new Cleaner();

	private final class Cleaner implements Runnable {
		private boolean working = false;

		public void start() {
			synchronized (lru) {
				if (false == working) {
					Mkdb.executor().execute(this);
					working = true;
				}
			}
		}

		@Override
		public void run() {
			final Lock readLock = Mkdb.getInstance().getTables().flushReadLock();
			readLock.lock();
			try {
				Object[] eldests = null;
				synchronized (lru) {
					try {
						//Trace.debug("cleaner start ...");
						final int capacity = getCapacity();
						final int size = lru.size();
						if (size > capacity) {
							final int nclean = Math.min(size - capacity + 255, size);;
							eldests = new Object[nclean];
							Iterator<Entry<K, TRecord<K, V>>> eit = lru.entrySet().iterator();
							for (int i = 0; i < nclean; ++ i) {
								eldests[i] = eit.next().getValue();
							}
						}
					} finally {
						working = false;
					}
				}
				if (null != eldests) {
					for (Object obj : eldests) {
						@SuppressWarnings("unchecked")
						final TRecord<K, V> record = (TRecord<K, V>)obj;
						if (false == TTableCacheLRU.this.tryRemoveRecord(record))
							break; // cleaner ����������ʱ�ͻᱻִ�У��Ƚ�Ƶ��������ʧ�������˳�ѭ����
					}
				}
			} finally {
				readLock.unlock();
			}
		}
	}

	@Override
	public void clear() {
		if (super.getTable().getPersistence() != Table.Persistence.MEMORY)
			throw new UnsupportedOperationException();
		synchronized (lru) {
			this.lru.clear();
		}
	}

	@Override
	public void clean() {
		// lru ���治��Ҫ��������
	}

	////////////////////////////////////////////////////////////
	@Override
	int getSize() {
		synchronized (lru) {
			return lru.size();
		}
	}

	@Override
	public void walk(CacheQuery<K, V> query) {
		final Object[] records;
		synchronized (lru) {
			records = lru.values().toArray();
		}
		super._walk_notify_query(records, query);
//		synchronized (lru) {
//			for (TRecord<K, V> r : lru.values()) {
//				V value = r.get();
//				if (null != value) // ��ɾ���ļ�¼���ص���
//					query.onQuery(Consts.toConst(r.getKey()), Consts.toConst(value));
//			}
//		}
	}
	////////////////////////////////////////////////////////////
	// data manage
	@Override
	TRecord<K, V> get(K key) {
		synchronized (lru) {
			return lru.get(key);
		}
	}

	@Override
	void addNoLog(K key, TRecord<K, V> r) {
		synchronized (lru) {
			if (lru.containsKey(key)) // guard
				throw new XError("cache.addNoLog duplicate record");
			lru.put(key, r);
		}
	}

	// ���뵽cache��.��Ӧ�����ݿ����: Add OR Remove
	@Override
	void add(K key, TRecord<K, V> r) {
		synchronized (lru) {
			if (lru.containsKey(key)) // guard
				throw new XError("cache.add duplicate record");
			super.logAddRemove(key, r);
			lru.put(key, r);
		}
	}

	@Override
	TRecord<K, V> remove(K key) {
		synchronized (lru) {
			return lru.remove(key);
		}
	}
}
