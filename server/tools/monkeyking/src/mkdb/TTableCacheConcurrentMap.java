package mkdb;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;


/**
 * 
 * ���� ConcurrentMap �� TTableCache ʵ�֡�
 *
 * @author cn
 *
 * @param <K>
 * @param <V>
 */
public class TTableCacheConcurrentMap<K, V> extends TTableCache<K, V> {
	private ConcurrentMap<K, TRecord<K, V>> map = mkdb.util.Misc.newConcurrentMap();
	private Runnable cleanWorker;
	private boolean cleanning = false;

	@Override
	void initialize(TTable<K, V> table, TableConf conf) {
		super.initialize(table, conf);

		cleanWorker = new Runnable() {
			@Override
			public void run() {
				if (setCleanning()) {
					TTableCacheConcurrentMap.this.cleanNow();
					resetCleanning();
				}
			}
		};
		// ����������������
		int delay = 3600 * 1000; // ���룬һСʱ��
		// Ϊ��ʹ������񲻻Ἧ����ĳ��ʱ���ִ�У��򵥴���һ�£������ʼ���ӳ�ʱ�䡣������ʲô�÷�����
		int initialDelay = Mkdb.random().nextInt(delay);
		Mkdb.executor().scheduleWithFixedDelay(cleanWorker, initialDelay, delay, TimeUnit.MILLISECONDS);
	}

	synchronized boolean setCleanning() {
		if (this.cleanning)
			return false;
		this.cleanning = true;
		return true;
	}

	synchronized void resetCleanning() {
		this.cleanning = false;
	}

	@Override
	public void clean() {
		this.cleanWorker.run();
	}

	private final void cleanNow() {
		final int capacity = super.getCapacity();
		if (capacity <= 0)
			return; // ��������

		final int size = this.getSize();
		if (size <= capacity)
			return; // �����㹻

		// ֱ������values�в������⡣�޸�Ϊʹ��TreeMap��һ����¼��ʱ����ʹ��һ�Ρ�
		// �ɵ�����ʽ���¡�
		// Object[] values = map.values().toArray();
		// java.util.Arrays.sort(values, TRecordComparator.singleton);

		PriorityQueue<AccessTimeRecord<K, V>> sorted = new PriorityQueue<AccessTimeRecord<K, V>>();
		for (TRecord<K, V> r : map.values()) // ֱ�����map��ֻ����һ���߳�������������
			sorted.add(new AccessTimeRecord<K, V>(r.getLastAccessTime(), r));

		// ÿ�ζ���� 255����see TTableCacheLRU��������Ͳ������ˡ�
		// ���Ǹ�����ֵ��ʵ��ɾ�����ܱ�nclean�١���ϸ�����ο������ѭ����
		for (int nclean = size - capacity + 255; nclean > 0; /* -- after remove */) {
			AccessTimeRecord<K, V> ar = sorted.poll();
			if (null == ar)
				break;
			if (ar.accessTime != ar.r.getLastAccessTime())
				continue; // ����󣬼�¼ʱ�������˸��£�ֱ��������
			if (super.tryRemoveRecord(ar.r)) // ���еĲ�Ƶ��������ɾ���Ƿ�ɹ���������ѭ����
				--nclean; // ɾ���ɹ��ż��ټ���
		}
	}

	static class AccessTimeRecord<K, V> implements Comparable<AccessTimeRecord<K, V>> {
		long accessTime;
		TRecord<K, V> r;

		@Override
		public int compareTo(AccessTimeRecord<K, V> o) {
			return accessTime < o.accessTime ? -1 : (accessTime == o.accessTime ? 0 : 1);
		}

		public boolean equals(Object obj) {
			if (obj instanceof AccessTimeRecord)
				return this.accessTime == ((AccessTimeRecord<?, ?>)obj).accessTime;
			return false;
		};

		AccessTimeRecord(long accessTime, TRecord<K, V> r) {
			this.accessTime = accessTime;
			this.r = r;
		}
	}

	@Override
	public void clear() {
		if (super.getTable().getPersistence() != Table.Persistence.MEMORY)
			throw new UnsupportedOperationException();
		map.clear();
	}

	@Override
	public void walk(CacheQuery<K, V> query) {
		super._walk_notify_query(map.values().toArray(), query);
	}

	@Override
	int getSize() {
		return map.size();
	}

	@Override
	TRecord<K, V> get(K k) {
		// ��¼����ʱ����
		final TRecord<K, V> r = map.get(k);
		if (null != r)
			r.access();
		return r;
	}

	@Override
	void addNoLog(K key, TRecord<K, V> r) {
		if (null != map.putIfAbsent(key, r))
			throw new XError("cache.addNoLog duplicate record");
	}

	@Override
	void add(K key, TRecord<K, V> r) {
		if (null != map.putIfAbsent(key, r))
			throw new XError("cache.addNoLog duplicate record");
		super.logAddRemove(key, r);

		// ע�⣺�����ȼ�������(map)�У��ż�¼��־��
		// TTableCacheLRU ��ʵ���߼����ȼ�¼��־�����������뵽�����С�
		// �ȼ�¼��־���ɿ�һЩ��
	}

	@Override
	TRecord<K, V> remove(K key) {
		return map.remove(key);
	}

}
