package mkdb;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ������ͬ�����ֺ�key�����ܵõ�ͬһ��Lockeyʵ����
 * ���������������ܡ�
 */
public final class Lockeys {

	private final Locks locks = new Locks();	
	
	/**
	 * for test only
	 * @param name
	 * @param key
	 * @return
	 */
	public final boolean exist(String name, Object key) {
		return locks.exist(new Lockey(name, this.getLockId(name), key));
	}

	private final Lockey get(Lockey lockey) {
		// �Ż����ȿ���ǰ�����Ƿ���У������У�ֱ�ӷ���
		Transaction curTransaction = Transaction.current();
		if (curTransaction != null) {
			Lockey lockey1 = curTransaction.get(lockey);
			if (lockey1 != null) 
				return lockey1;
		}
		return locks.get(lockey);
	}

	public final int size() {
		return locks.size();
	}

	/////////////////////////////////////////////////////////////////////
	// û����������Mkdb.start ��ʱ�򣬵���һ�� getInstance
	private static Lockeys instance = new Lockeys();

	public static Lockeys getInstance() {
		return instance;
	}

	/**
	 *  key ����ʵ�� hashCode, equals, Comparable��
	 * @param name
	 * @param key
	 * @return Lockey ʵ��
	 * @deprecated
	 * 		ʹ�� �·��� get(TTable<?, ?> ttable, Object key).
	 */
	public static Lockey get(String name, Object key) {
		return instance.get(new Lockey(name, instance.getLockId(name), key));
	}

	// ��������������ԭ���ϣ�������ڲ���Ϣ��
	static Lockey get(String name, int index, Object key) {
		return instance.get(new Lockey(name, index, key));
	}

	/**
	 * ���ر�񵥸���¼����
	 * @param ttable
	 * @param key
	 * @return Lockey ʵ��
	 */
	public static Lockey get(TTable<?, ?> ttable, Object key) {
		return get(ttable.getLockName(), ttable.getLockId(), key);
	}

	/**
	 * ���ر������¼���������key������֪������ɢʱ��ʹ������������磺
	 * get(ttable, 1, 2, 3);
	 * @param ttable
	 * @param keys
	 * @return
	 */
	public static Lockey[] get(TTable<?, ?> ttable, Object ... keys) {
		final String lockName = ttable.getLockName();
		final int lockId = ttable.getLockId();
		final Lockey[] lockeys = new Lockey[keys.length];

		int i = 0;
		for (Object key : keys)
			lockeys[i++] = get(lockName, lockId, key);

		return lockeys;
	}

	/**
	 * ���ض����¼����
	 * @param ttable
	 * @param keys
	 * @return
	 */
	public static Lockey[] get(TTable<?, ?> ttable, Collection<?> keys) {
		final String lockName = ttable.getLockName();
		final int lockId = ttable.getLockId();
		final Lockey[] lockeys = new Lockey[keys.size()];

		int i = 0;
		for (Object key : keys)
			lockeys[i++] = get(lockName, lockId, key);

		return lockeys;
	}

	public static void lock(TTable<?, ?> table, Collection<?> keys) {
		lock(get(table, keys));
	}

	public static void lock(Lockey lockey) {
		Transaction.current().add(lockey);
	}
	
	/**
	 * ��ö�����¼���������Ľ���˳���Ҽ��뵱ǰ����
	 * ����ȷ֪����Ҫ��������¼ʱ��ʹ��������������Լ��������Ŀ��ܡ�
	 * @param lockeys ��
	 */
	public static void lock(Lockey[] lockeys) {
		java.util.Arrays.sort(lockeys);
		for (Lockey lockey : lockeys) {
			//mkdb.Trace.debug("add" + lockey);
			Transaction.current().add(lockey);
		}
	}
	/////////////////////////////////////////////////////////////////////
	// ����Ź���
	private final Map<String, Integer> lockIdMap = new HashMap<String, Integer>();
	private int autoId = 0;
	private final Lock syncIdMap = new ReentrantLock();

	/**
	 * <p>��������ı�š�����Ų�����ʱ����̬����һ����
	 * <p>���������Ҫͬ������������Ҫ���ң���ԭ���ϣ��Ժ󴴽�����ͨ��Table������ͨ�����������
	 * @param lockname
	 * @return
	 */
	final int getLockId(String lockname) {
		syncIdMap.lock();
		try {
			Integer i = lockIdMap.get(lockname);
			if (null != i)
				return i;
			++ autoId;
			lockIdMap.put(lockname, autoId);
			return autoId;
		} finally {
			syncIdMap.unlock();
		}
	}

	/**
	 * xdb����ʱ�������е�����š����Է��û���
	 * <p>���⣺�Ƿ�xdb���������ı�źͳ�ʼ��ʱ����ı�ţ�
	 * @param tables
	 */
	final void initializeLockIdMap(Collection<Table> tables) {
		Set<String> lockNames = new HashSet<String>();
		for (Table table : tables) {
			if (table instanceof TTable<?, ?>) { // ���Է��û��� 
				TTable<?, ?> ttable = (TTable<?, ?>)table;
				lockNames.add(ttable.getLockName());
			}
		}
		initializeLockIdMap(lockNames.toArray(new String[lockNames.size()]));
	}

	final void initializeLockIdMap(String[] lockNames) {
		// �����Ժ�����˳��Ͳ��������߼�ִ��˳����䡣������...(��Ҳ˵����)��
		Arrays.sort(lockNames);
		for (String lockName : lockNames)
			this.getLockId(lockName);
	}
}
