package mkdb;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import mkdb.logs.Listener;
import mkdb.logs.ListenerMap;
import mkdb.logs.LogRecord;

import com.locojoy.base.Marshal.MarshalException;

import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.Lock;

/**
 * xtable��xbean�Ĺ�������֧�ֲ������ʣ��Զ��־û��������棬ͳ���������ݡ�
 * ֻ����Procedure�в���xtable���κ��������ʷ�ʽ��xdb�����׳��쳣��
 * ������ʹ��Mapʵ�֣�Ҳ����˵���κ�xtable����Ψһ�ؼ��֡��ؼ��ֱ����ǿɱȽϵģ���Ҫʵ��Comparable��
 * Ŀǰֻ��������͵����ؼ������ͣ��պö��ǿɱȽϵġ�
 * ��xtable�õ���xbean�����ڲ�ʵ�������á�ֱ�Ӷ����ý��ж�ȡ���޸ģ�û�е����ı��������
 * <B> ��xtable�õ�����ֻ������ִ�й�������Ч�����Դ��ݸ�Ƕ�׹��̻����������ܱ��������� </B>
 *
 * @param <K>
 * @param <V>
 */
public abstract class TTable<K, V> extends Table implements TTableMBean {
	private String lockname; // initialize in open(...)
	private int lockId; // initialize in initialize(...)
	private TableConf conf;  // initialize in open��MkdbConf�б������õ����á�
	private mkdb.util.AutoKey<K> autoKey;
	private javax.management.ObjectName mbeanObjectName = null; // ��������������ע��

	/////////////////////////////////////////////////////////////////////
	public final K nextKey() {
		return autoKey.next(); // autoKey �Ĵ����������ʼ�������ﲻ֪��K���������͡�
	}

	public final mkdb.util.AutoKey<K> getAutoKey() {
		return autoKey;
	}

	/**
	 * ���key������ֵ�������ڷ���null��
	 * 
	 * �����Ƿ���ڶ������������ϸ�
	 * 
	 * @param key
	 * @return value�����á� 
	 */
	public final V get(K key) {
		return get(key, true);
	}

	/**
	 * ���key������ֵ����������ڣ�����value��
	 * 
	 * ���ֵ���ڣ������������򲻱�������
	 * 
	 * @param key
	 * @param value
	 * @return ���key���ڷ��ؼ�¼ value �����ã����򷵻ش����value��
	 */
	public final V get(K key, V value) {
		V cur = get(key, false);
		if (null != cur)
			return cur;
		return value;
	}

	/**
	 * �����¼�����ڣ��׳��쳣��
	 * @param key
	 * @param value
	 */
	public final void insert(K key, V value) {
		if (false == add(key, value))
			throw new XError("insert key=" + key + " value=" + value);
	}
	
	/**
	 * �����autokey,�����¼�����ڣ��׳��쳣��
	 * @author dc
	 * @param key
	 * @param value
	 */
	public final void insertUncheckedAutoKey(K paramK, V paramV) {
		if (false == add(paramK, paramV, false))
			throw new XError("insert key=" + paramK + " value=" + paramV);
	}

	/**
	 * ɾ����¼�������ڣ��׳��쳣��
	 * @param key
	 */
	public final void delete(K key) {
		if (false == remove(key))
			throw new XError("delete key=" + key);
	}

	/**
	 * ������ڣ�����£�����true����������
	 * ���򷵻�null��û�б�������
	 * @param key
	 * @param value
	 * @return �����Ƿ�ɹ�
	 */
	public final boolean update(K key, V value) {
		V cur = get(key, false);
		if (null == cur)
			return false;
		delete(key);
		insert(key, value);
		return true;
	}

	/**
	 * �����¼��
	 * @param key
	 * @param value
	 * @return �����¼���ڣ�����ԭ����ֵ�������һ�μ��룬����null
	 */
	public final V put(K key, V value) {
		V origin = get(key, true);
		if (null == origin) {
			insert(key, value);
			return null;
		}
		delete(key);
		insert(key, value);
		return origin;
	}

	/**
	 * ���key��Ӧ��ֵ�Ѿ����ڣ����ع�����ֵ��
	 * �������µĹ���������null���������¼����ֵ������������������Ƿ��һ�μ��롣
	 * 
	 * ���������Ժ󣬱����˵�ǰ��¼������
	 * 
	 * @param key
	 * @param value
	 * @return ���key��Ӧ��ֵ�Ѿ����ڣ����ع�����ֵ�����򷵻�null��
	 */
	public final V putIfAbsent(K key, V value) {
		V origin = get(key, true);
		if (null == origin) {
			insert(key, value);
			return null;
		}
		return origin;
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 * ����һ����¼����autokey��顣 ����ԭʼ�ӿ�
	 * @param key
	 * @param value
	 * @param paramBoolean
	 * @return
	 */
	public final boolean add(K paramK, V paramV) {
		return add(paramK, paramV, true);
	}
	/**
	 * ����һ����¼��
	 * 
	 * ����ɹ��򱣳��������򲻱��֡�
	 * @author dc
	 * @param key
	 * @param value
	 * @param paramBoolean �Ƿ���autokey
	 * @return boolean ����Ƿ�ɹ���
	 */
	public final boolean add(K key, V value, boolean paramBoolean) {
		if (null == key)
			throw new NullPointerException("key is null");
		if (null == value)
			throw new NullPointerException("value is null");

		if ((paramBoolean) && null != this.autoKey)
			this.autoKey.accept(key);

		countAdd.incrementAndGet();
		Lockey lockey = Lockeys.get(this, key);
		lockey.lock();
		try {
			TRecord<K, V> r = cache.get(key);
			if (null != r) {
				Transaction.current().add(lockey);
				return r.add(value);
			}

			countAddMiss.incrementAndGet();
			if (_exist(key)) {
				countAddStorageMiss.incrementAndGet();
				return false;
			}

			cache.add(key, new TRecord<K, V>(this, value, lockey, TRecord.State.ADD));
			Transaction.current().add(lockey);
			return true;
		} finally {
			lockey.unlock();
		}
	}

	/**
	 * ɾ����¼��
	 * �����Ƿ�ɹ�������������
	 * @param key
	 * @return remove �Ƿ�ɹ���
	 */
	public final boolean remove(K key) {
		if (null == key)
			throw new NullPointerException("key is null");

		final Transaction currentT = Transaction.current();
		currentT.rmvCachedTRecord(this, key);

		countRemove.incrementAndGet();
		Lockey lockey = Lockeys.get(this, key);
		lockey.lock();
		try {
			TRecord<K, V> r = cache.get(key);
			if (null != r) {
				currentT.add(lockey);
				//Transaction.current().rmvCachedTRecord(this, r.getKey());
				return r.remove();
			}

			countRemoveMiss.incrementAndGet();
			boolean exists = _exist(key);
			if (false == exists)
				countRemoveStorageMiss.incrementAndGet();
			cache.add(key, new TRecord<K, V>(this, null, lockey, exists));
			currentT.add(lockey);
			return exists;
		} finally {
			lockey.unlock();
		}
	}

	/**
	 * ���Ҽ�¼������
	 * ��¼�����򷵻أ����ұ���������������ڣ�����ݲ���holdNull�����Ƿ񱣳�����
	 * 
	 * @param key
	 * @param holdNull
	 * @return value ������
	 */
	public final V get(K key, boolean holdNull) {
		if (null == key)
			throw new NullPointerException("key is null");
		
		countGet.incrementAndGet();

		/////////////////////////////////////////////////////
		// get from transaction cache first
		final Transaction currentT = Transaction.current();
		TRecord<K, V> rCached = currentT.getCachedTRecord(this, key);
		if (rCached != null)
			return rCached.getValue();

		/////////////////////////////////////////////////////
		// real get
		Lockey lockey = Lockeys.get(this, key);
		lockey.lock();
		try {
			TRecord<K, V> r = cache.get(key);
			if (null == r) {
				countGetMiss.incrementAndGet();
				V value = _find(key);
				if (null == value) {
					countGetStorageMiss.incrementAndGet();
					if (holdNull)
						currentT.add(lockey);
					return null;
				}

				r = new TRecord<K, V>(this, value, lockey, TRecord.State.INDB_GET);
				cache.addNoLog(key, r);
			}

			currentT.add(lockey);
			currentT.addCachedTRecord(this, r);
			return r.getValue();
		} finally {
			lockey.unlock();
		}
	}

	/**
	 * ѡ���¼�ֶΡ�
	 * ��������;
	 * �����ڻص�ʱ��������������ã���Ȼ���ɱ�������Ա��棬��Ҳ��Ҫ��ô��;
	 * ��������ڣ�����null������ͨ���ص� field.get() �õ����ֶεĿ���(���ɱ����ֱ�ӷ���)��
	 * <B>���ڲ���������XGEN������ʵ�ִ���ʱ��ֻ֧�ֲ��ɱ��ֶε�select(ͨ��ֱ�ӷ�������ʵ��)
	 * ������value��select(ͨ��xbean.toData()ʵ��)���ɱ����͵��ֶβ�֧�־ֲ�select��</B>
	 * @param key
	 * @param field
	 * @return 
	 */
	public final <F> F select(K key, TField<V, F> field) {
		if (null == key)
			throw new NullPointerException("key is null");

		countGet.incrementAndGet();
		Lockey lockey = Lockeys.get(this, key);
		lockey.lock();
		try {
			TRecord<K, V> r = cache.get(key);
			if (null == r) {
				countGetMiss.incrementAndGet();
				V value = _find(key);
				if (null == value) {
					countGetStorageMiss.incrementAndGet();
					return null;
				}

				r = new TRecord<K, V>(this, value, lockey, TRecord.State.INDB_GET);
				cache.addNoLog(key, r);
			}

			final V value = r.getValue();
			if (null == value)
				return null;
			return field.get(value);
		} finally {
			lockey.unlock();
		}
	}

	////////////////////////////////////////////////////////////////////
	private ThreadLocal<LogRecord<K, V>> logRecord = new ThreadLocal<LogRecord<K, V>>() {
		// ���ﲢ����ҪΪ�����̴߳�����ʵ����ֻ��ִ��������̲߳���Ҫ��
		// ��������Ϊֻ�� Worker �Ŵ�����Ϊ�˼���ϣ������ھ��Զ������ˡ�
		@Override
		protected LogRecord<K, V> initialValue() {
			return new LogRecord<K, V>(TTable.this.newValue());
		}
	};

	// ���еļ����ߡ�
	private ListenerMap listenerMap = new ListenerMap();

	private final String toFullVarName(String [] names) {
		// �ϸ������飬����ֱ�Ӱ�namesƴ�����Ϳ����ˡ�
		mkdb.logs.VarNames varNames = new mkdb.logs.VarNames(names);
		return new LogRecord<K, V>(TTable.this.newValue()).toFullVarName(varNames);
	}

	public final void removeListener(Listener l, String ... names) {
		listenerMap.remove(toFullVarName(names), l);
	}

	public final String addListener(Listener l, String ... names) {
		return listenerMap.add(toFullVarName(names), l);
	}

	public final boolean hasListener() {
		return listenerMap.hasListener();
	}

	@Override
	final void logNotify() {
		try {
			logRecord.get().logNotify(listenerMap);
		} catch (Throwable e) {
			// ��������ʱ,����������ռ�����,��һ����Ҫ��ʱ���ؽ���
			logRecord.remove();
			Trace.fatal("TTable.logNotify", e);
		}
	}

	////////////////////////////////////////////////////////////////////
	// record cache
	private TTableCache<K, V> cache;

	public final TTableCache<K, V> getCache() {
		return cache;
	}

	/////////////////////////////////////////////////////////////////////
	// �洢�ӿں�����ת����������
	private TStorage<K, V> storage;

	final TStorage<K, V> getStorage() {
		return storage;
	}

	// dirty record
	final void onRecordChanged(TRecord<K, V> r, mkdb.logs.LogNotify ln) {
		logRecord.get().onChanged(this, r, ln);

		if (r.getState() == TRecord.State.REMOVE)
			cache.remove(r.getKey()); // ˢ��ǰ�ͱ�ɾ���ˣ���� cache������ memory ���൱�����ɾ���ˡ�

		if (null != storage)
			storage.onRecordChange(r);
	}

	@Override
	protected final void initialize(Tables tables) {
		this.autoKey = bindAutoKey();
		this.lockId = Lockeys.getInstance().getLockId(lockname);
	}

	/**
	 * autoIncrement �ı����Ҫ����ʵ�������������ǰ����ģ��ģ���֪����ȷ�� K ���͡�
	 */ 
	protected mkdb.util.AutoKey<K> bindAutoKey() {
		return null;
	}

	public TableConf getConf() {
		return conf;
	}

	@Override
	final Storage open(MkdbConf xconf, Logger logger) {
		if (null != storage)
			throw new XError("table has opened : " + getName());

		conf = xconf.getTableConf(getName());
		if (null == conf)
			throw new XError("no configuration for table '" + getName() + "'");

		lockname = conf.getLockName();
		cache = TTableCache.newInstance(this, conf);

		switch (conf.getPersistence()) {
		case MEMORY: storage = null; break;
		case DB:     storage = new TStorage<K, V>(logger, xconf, conf); break;
		}
		this.mbeanObjectName = Mkdb.mbeans().register(this, "mkdb:type=Tables,name=" + getName());
		return storage;
	}

	@Override
	final void close() {
		if (null != storage) {
			storage.close();
			storage = null;
		}
		Mkdb.mbeans().unregister(this.mbeanObjectName);
	}

	public abstract V newValue();
	public abstract OctetsStream marshalKey(K key);
	public abstract OctetsStream marshalValue(V value);
	public abstract K unmarshalKey(OctetsStream os) throws MarshalException;
	public abstract V unmarshalValue(OctetsStream os) throws MarshalException;

	private final boolean _exist(K key) {
		if (null != storage)
			return storage.exist(key, this);
		return false;
	}

	private final V _find(K key) {
		if (null != storage)
			return storage.find(key, this);
		return null;
	}

	/**
	 * walkʱ�Ļص��ӿ�
	 */
	public static interface IWalk<K, V> {
		public boolean onRecord(K k, V v);
	}
	
	/**
	 * �������walkʱ����Ĵ�������ʹ��IWalkDetail�ӿڣ����Ի��û��unmarshalԭʼ����
	 */
	public static interface IWalkDetail<K, V> extends IWalk<K, V> {
		public boolean onError(byte[] key, byte[] data);
	}

	/**
	 * ��������Ѿ�flush��C++ҳ���е��������ݣ��������xdb.cache����ҳ
	 * �������ԣ�����ͬflush����������ͬcheckpoint����
	 * �������ԣ�����C++���ڴ�ҳ��
	 * 
	 * ����Ҫ��������ִ��
	 * @param iwalk
	 */
	public final void walk(final IWalk<K, V> iwalk) {
		if (null == iwalk)
			throw new NullPointerException();
		if (null != storage) {
			Lock flushLock = Mkdb.getInstance().getTables().flushReadLock();
			Lock checkpointLock = Mkdb.getInstance().getTables().checkpointLock();
			flushLock.lock();
			checkpointLock.lock();
			try {
				storage.walk(new Storage.IWalk() {
					@Override
					public boolean onRecord(byte[] key, byte[] data) {
						try {
							K k = unmarshalKey(OctetsStream.wrap(Octets.wrap(key, key.length)));
							V v = unmarshalValue(OctetsStream.wrap(Octets.wrap(data, data.length)));
							return iwalk.onRecord(k, v);
						} catch (Throwable e) {
							Trace.error("table:" + TTable.this.getTableName() + ",walk:" + iwalk.getClass().getName() + ",error:", e);
							if (iwalk instanceof IWalkDetail<?, ?>) {
								return ((IWalkDetail<K, V>)iwalk).onError(key, data);
							}
							return true;
						}
					}
				});
			} finally {
				flushLock.unlock();
				checkpointLock.unlock();
			}
		}
	}
	
	/**
	 * ֻ����������ļ�������
	 * @param ibrowse
	 */
	public final void browse(final IWalk<K, V> ibrowse) {
		// Ĭ��ʹ�� 128 ��ҳ������ȡ
		browse(ibrowse, 128);
	}
	
	/**
	 * ֻ����������ļ�������
	 * �������ԣ����Ժ�flush���̲��������ǲ���ͬcheckpoint����
	 * �������ԣ����ʱ�½�PageHash��ͨ��cache_sizeָ��PageHash��ҳ�����ƣ�
	 * 
	 * ����Ҫ��������ִ��
	 * @param iwalk
	 */
	public final void browse(final IWalk<K, V> ibrowse, int cache_size) {
		if (null == ibrowse)
			throw new NullPointerException();
		if (null != storage) {
			Lock checkpointLock = Mkdb.getInstance().getTables().checkpointLock();
			checkpointLock.lock();
			try {
				storage.browse(new Storage.IWalk() {
					@Override
					public boolean onRecord(byte[] key, byte[] data) {
						try {
							K k = unmarshalKey(OctetsStream.wrap(Octets.wrap(key, key.length)));
							V v = unmarshalValue(OctetsStream.wrap(Octets.wrap(data, data.length)));
							return ibrowse.onRecord(k, v);
						} catch (Throwable e) {
							Trace.error("table:" + TTable.this.getTableName() + ",browse:" + ibrowse.getClass().getName() + ",error:", e);
							if (ibrowse instanceof IWalkDetail<?, ?>) {
								return ((IWalkDetail<K, V>)ibrowse).onError(key, data);
							}
							return true;
						}
					}
				}, cache_size);
			} finally {
				checkpointLock.unlock();
			}
		}
	}

	/////////////////////////////////////////////////////
	// MBean
	private AtomicLong countAdd = new AtomicLong();
	private AtomicLong countAddMiss = new AtomicLong();
	private AtomicLong countAddStorageMiss = new AtomicLong();

	private AtomicLong countGet = new AtomicLong();
	private AtomicLong countGetMiss = new AtomicLong();
	private AtomicLong countGetStorageMiss = new AtomicLong();

	private AtomicLong countRemove = new AtomicLong();
	private AtomicLong countRemoveMiss = new AtomicLong();
	private AtomicLong countRemoveStorageMiss = new AtomicLong();

	@Override
	public String getTableName() {
		return getName();
	}

	@Override
	public String getLockName() {
		return lockname;
	}

	int getLockId() {
		return lockId;
	}

	@Override
	public Persistence getPersistence() {
		return conf.getPersistence();
	}

	@Override
	public String getPersistenceName() {
		return conf.getPersistence().name();
	}

	@Override
	public void setCacheCapacity(int capacity) {
		cache.setCapacity(capacity);
	}

	@Override
	public int getCacheCapacity() {
		return cache.getCapacity();
	}

	@Override
	public String getCacheClassName() {
		return cache.getClass().getName();
	}

	@Override
	public int getCacheSize() {
		return cache.getSize();
	}

	@Override
	public long getCountAdd() {
		return countAdd.get();
	}

	@Override
	public long getCountAddMiss() {
		return countAddMiss.get();
	}

	@Override
	public long getCountAddStorageMiss() {
		return countAddStorageMiss.get();
	}

	@Override
	public long getCountGet() {
		return countGet.get();
	}

	@Override
	public long getCountGetMiss() {
		return countGetMiss.get();
	}

	@Override
	public long getCountGetStorageMiss() {
		return countGetStorageMiss.get();
	}

	@Override
	public long getCountRemove() {
		return countRemove.get();
	}

	@Override
	public long getCountRemoveMiss() {
		return countRemoveMiss.get();
	}

	@Override
	public long getCountRemoveStorageMiss() {
		return countRemoveStorageMiss.get();
	}

	// compute format
	private String format(long miss, long ops) {
		return String.format("%.2f", (double)(ops-miss) / ops);
	}

	@Override
	public String getPercentAddHit() {
		return format(getCountAddMiss(), getCountAdd());
	}

	@Override
	public String getPercentGetHit() {
		return format(getCountGetMiss(), getCountGet());
	}

	@Override
	public String getPercentRemoveHit() {
		return format(getCountRemoveMiss(), getCountRemove());
	}

	@Override
	public String getPercentCacheHit() {
		return format(getCountAddMiss() + getCountRemoveMiss() + getCountGetMiss(),
				getCountAdd() + getCountRemove() + getCountGet());
	}

	@Override
	public long getStorageCountFlush() {
		return null != storage ? storage.getCountFlush() : -1;
	}

	@Override
	public long getStorageCountMarshal0() {
		return null != storage ? storage.getCountMarshal0() : -1;
	}

	@Override
	public long getStorageCountMarshalN() {
		return null != storage ? storage.getCountMarshalN() : -1;
	}

	@Override
	public long getStorageCountMarshalNTryFail() {
		return null != storage ? storage.getCountMarshalNTryFail() : -1;
	}

	@Override
	public long getStorageCountSnapshot() {
		return null != storage ? storage.getCountSnapshot() : -1;
	}

	@Override
	public long getStorageFlushKeySize() {
		return null != storage ? storage.flushKeySize : -1;
	}

	@Override
	public long getStorageFlushValueSize() {
		return null != storage ? storage.flushValueSize : -1;
	}
}
