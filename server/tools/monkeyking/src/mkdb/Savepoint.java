package mkdb;

import java.util.*;

public final class Savepoint {
	// ��! LinkedHashMap ��Ȼû�а��෴˳�������iterator��
	Map<LogKey, Log> logs = new HashMap<LogKey, Log>();
	List<Log> addOrder = new ArrayList<Log>();
	int access = 0;

	int commit() {
		for (Log log : addOrder) log.commit();
		// ���෴˳���ύ���ռ�LogNotifyʱ�����Եõ����������ʼ�����ݡ�
		//for (int i = addOrder.size() - 1; i >= 0; --i) addOrder.get(i).commit();
		return addOrder.size();
	}

	int rollback() {
		// �������෴˳��ع���
		// ��set����map��keyʹ���Զ���xbeanʱ�������ڼ�������ǰ�޸�(hashCode�ᷢ���仯)��
		// ��ʱ��Ҫ�����෴˳��ع����Ȼع�������Ȼ����xbean����
		for (int i = addOrder.size() - 1; i >= 0; --i)
			addOrder.get(i).rollback();
		return addOrder.size();
	}

	public int size() {
		return addOrder.size();
	}

	public boolean isAccessSince(int a) {
		return a != access;
	}

	public int getAccess() {
		return access;
	}

	public Log get(LogKey key) {
		++ access;
		return logs.get(key);
	}

	public void add(LogKey key, Log log) {
		++ access;
		Log old = logs.put(key, log);
		if (null != old) {
			logs.put(key, old); // �ָ�ԭֵ���Ƚ϶���
			throw new XError("mkdb.Savepoint.add duplicate log");
		}
		addOrder.add(log);
	}

	public boolean addIfAbsent(LogKey key, Log log) {
		++ access;
		if (!logs.containsKey(key)) {
			logs.put(key, log);
			addOrder.add(log);
			return true;
		}
		return false;
	}

	///////////////////////////////////////////////////////////
	// test
	private static class L implements Log {
		private String name;
		L(String name) {
			this.name = name;
		}
		@Override
		public void commit() {
		}
		@Override
		public void rollback() {
		}
		@Override
		public String toString() {
			return name;
		}
	}
	public static void main(String args[]) {
		Savepoint sp = new Savepoint();
		XBean x1 = new XBean(null, null);
		XBean x2 = new XBean(null, null);
		XBean x3 = new XBean(null, null);
		sp.add(new LogKey(x3, "c"), new L("c"));
		sp.add(new LogKey(x2, "b"), new L("b"));
		sp.add(new LogKey(x1, "a"), new L("a"));
		System.out.println(sp.addOrder);
	}
}
