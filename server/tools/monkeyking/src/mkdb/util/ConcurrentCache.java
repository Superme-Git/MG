package mkdb.util;

import java.util.Calendar;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import mkdb.Mkdb;

public abstract class ConcurrentCache<K, V> implements ConcurrentCacheMBean {
	static class Value<V> {
		private volatile long accessTime;
		private volatile V value;

		Value() {
		}

		Value(V value) {
			this.value = value;
		}

		final V access() {
			this.accessTime = System.nanoTime();
			return value;
		}
	}

	private String name;
	private ConcurrentMap<K, Value<V>> cache = mkdb.util.Misc.newConcurrentMap();
	private volatile ConcurrentCacheConf conf;
	private AtomicLong countGet = new AtomicLong();
	private AtomicLong countMiss = new AtomicLong();
	private final javax.management.ObjectName mbeanObjectName; // ��������������ע��

	public ConcurrentCache(String name) {
		this.name = name;
		this.conf = mkdb.Mkdb.getInstance().getConf().getConcurrentCacheConf(name);
		if (null == this.conf)
			throw new RuntimeException("ConcurrentCacheConf not found. ccache='" + name + "'");
		this.mbeanObjectName = Mkdb.mbeans().register(this, "mkdb:type=Caches,name=" + name);
	}

	final synchronized void close() {
		Mkdb.mbeans().unregister(this.mbeanObjectName);
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public long getCountGet() {
		return this.countGet.get();
	}

	@Override
	public long getCountMiss() {
		return this.countMiss.get();
	}

	// compute format
	private String format(long miss, long ops) {
		return String.format("%.2f", (double)(ops-miss) / ops);
	}

	@Override
	public String getPercentGetHit() {
		return format(countMiss.get(), countGet.get());
	}

	@Override
	public int getSize() {
		return cache.size();
	}

	@Override
	public int getCapacity() {
		return conf.getCapacity();
	}

	@Override
	public void setCapacity(int capacity) {
		conf.setCapacity(capacity);
	}

	public ConcurrentCacheConf getConf() {
		return conf;
	}

	public synchronized void setConf(ConcurrentCacheConf conf) {
		this.conf = conf;
	}

	/**
	 * �� Listener �����жϼ�¼�����Ƿ���ڡ��������ڻ���ʱ�Ž��и��¡�
	 * @param key
	 * @return
	 */
	public final boolean existInCache(K key) {
		return cache.get(key) != null;
	}

	/**
	 * �������ʱ������ȡ���ڵĻ����¼������á��������ڻ���ʱ�Ž��и��¡�
	 * @param key
	 * @return
	 */
	protected final V peek(K key) {
		final Value<V> vv = cache.get(key);
		if (null == vv)
			return null;
		return vv.value;
	}

	/**
	 * ��ѯ��¼�����cache��û�����ȥxdbװ�ء�
	 * <p>���صļ�¼��Զ����Ϊnull�����ǣ����صļ�¼�ڲ�����������ݿ���Ϊnull��
	 * @param key
	 * @return
	 */
	public final V get(K key) {
		this.countGet.incrementAndGet();
		Value<V> vv = cache.get(key);
		if (null == vv) {
			this.countMiss.incrementAndGet();
			vv = new Value<V>();
			final Value<V> vv2 = cache.putIfAbsent(key, vv);
			if (null != vv2)
				vv = vv2;
		}
		if (null == vv.value) {
			synchronized(vv) {
				if (null == vv.value)
					vv.value = realGet(key);
			}
		}
		return vv.access();
	}

	/**
	 * ��ʹ����ʵ�����������ʹ���߸������������ȡ���б����װ�ɳ���¼�����
	 * @param key
	 * @return
	 */
	protected abstract V realGet(K key);
	//protected abstract mkdb.Lockey[] realGetLocks(K key);

	///////////////////////////////////////////////////////////////////////////
	// �����Լ�ɾ��
	/**
	 * ʵ�������������������»��������Listener�õ��µ����ݣ�ʹ�����������
	 * ͨ����ֵ��ʽ�������ݸ��µ�cache�С�
	 * @param key
	 * @param value
	 */
	protected void update(K key, V value) {
		final Value<V> vv = cache.get(key);
		if (null != vv) // ������¼����ʱ�Ž��и��²�����
			vv.value = value;
	}

	private java.util.TimerTask cleanTimer;

	public synchronized void setupCleanTimer() {
		setupCleanTimer(conf.getHourOfDay(), conf.getMinute());
	}

	// ��������cache�Ķ�ʱ��
	public synchronized void setupCleanTimer(int hourOfDay, int minute) {
		if (null != cleanTimer)
			return;

		Calendar firstTime = Calendar.getInstance();
		firstTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
		firstTime.set(Calendar.MINUTE, minute);
		firstTime.set(Calendar.SECOND, 0);
		firstTime.set(Calendar.MILLISECOND, 0);
		// �ѳ�ʼʱ��ɢ�е�һ��Сʱ�С���ִֹ֪ͨ��clean��
		firstTime.add(Calendar.SECOND, mkdb.Mkdb.random().nextInt(3600));
		if (firstTime.before(Calendar.getInstance()))
			firstTime.add(java.util.Calendar.DAY_OF_MONTH, 1); // tomorrow. nextday

		cleanTimer = new java.util.TimerTask() {
			public void run() {
				ConcurrentCaches.getInstance().add(new Runnable() {
					@Override
					public void run() {
						ConcurrentCache.this.clean();
					}
				});
			}
		};
		mkdb.Mkdb.timer().schedule(cleanTimer, firstTime.getTime(), 24 * 3600 * 1000); // every day
	}

	public synchronized void clearCleanTimer() {
		if (null != cleanTimer) {
			cleanTimer.cancel();
			cleanTimer = null;
		}
	}

	/**
	 * ����lruԭ�򣬶�ʱ����������ݡ�
	 * ����Cacheʱ�����ö�ʱ������ʱִ�С�
	 */
	void clean() {
		final int c = conf.getCapacity();
		if (c <= 0)
			return; // ��������

		final int s = cache.size();
		if (s <= c)
			return; // �����㹻

		// ����ʹ��TreeMap�����ʱ���������������һ���Զ�ȡ���ж�accessTime��
		// ���������㷨���ܶ�ζ�ȡaccessTime���в������⡣
		PriorityQueue<AccessTimeEntry<K, V>> sorted = new PriorityQueue<AccessTimeEntry<K, V>>();
		for (Entry<K, Value<V>> e : cache.entrySet()) // ֱ�����map��ֻ����һ���߳�������������
			sorted.add(new AccessTimeEntry<K, V>(e.getValue().accessTime, e));

		// ���Ǹ�����ֵ��ʵ��ɾ�����ܱ�nclean�١���ϸ�����ο������ѭ����
		for (int nclean = s - c + 255; nclean > 0; /* -- after remove */) {
			AccessTimeEntry<K, V> akv = sorted.poll();
			if (null == akv)
				break;
			if (akv.accessTime != akv.kv.getValue().accessTime)
				continue; // ����󣬼�¼ʱ�������˸��£�ֱ��������
			cache.remove(akv.kv.getKey());
			--nclean;
		}
	}

	static class AccessTimeEntry<K, V> implements Comparable<AccessTimeEntry<K, V>> {
		long accessTime;
		Entry<K, Value<V>> kv;

		@Override
		public int compareTo(AccessTimeEntry<K, V> o) {
			return accessTime < o.accessTime ? -1 : (accessTime == o.accessTime ? 0 : 1);
		}

		public boolean equals(Object obj) {
			if (obj instanceof AccessTimeEntry)
				return this.accessTime == ((AccessTimeEntry<?, ?>)obj).accessTime;
			return false;
		};

		AccessTimeEntry(long accessTime, Entry<K, Value<V>> kv) {
			this.accessTime = accessTime;
			this.kv = kv;
		}
	}
}
