package mkdb;

import java.util.List;
import java.util.Set;

import mkdb.logs.LogList;
import mkdb.logs.LogMap;
import mkdb.logs.LogNavigableMap;
import mkdb.logs.LogSet;
import mkdb.util.SetX;

import java.util.Map;
import java.util.NavigableMap;

public final class Logs {

	////////////////////////////////////////////////////////////////////
	// ��������־ֱ�Ӽ�¼������㡣
	public static void logIf(LogKey key) {
		Savepoint sp = Transaction.currentSavepoint();
		if (null == sp.get(key)) {
			Log log = key.create();
			sp.add(key, log);
		}
	}

	////////////////////////////////////////////////////////////////////
	// ������װ��ʵ����¼�������У��޸���־�������޸�ʱ�����ݵ�ǰ����㶯̬��ȡ�� 
	public static <E> List<E> logList(LogKey key, List<E> wrapped) {
		Map<LogKey, Object> wrappers = Transaction.current().wrappers;
		Object exist = wrappers.get(key);
		if (null == exist) {
			LogList<E> log = new LogList<E>(key, wrapped);
			wrappers.put(key, log);
			return log;
		}
		@SuppressWarnings("unchecked")
		LogList<E> log = (LogList<E>)exist;
		return log;
	}

	public static <K, V> Map<K, V> logMap(LogKey key, Map<K, V> wrapped) {
		Map<LogKey, Object> wrappers = Transaction.current().wrappers;
		Object exist = wrappers.get(key);
		if (null == exist) {
			LogMap<K, V, Map<K, V>> log = new LogMap<K, V, Map<K, V>>(key, wrapped);
			wrappers.put(key, log);
			return log;
		}
		@SuppressWarnings("unchecked")
		LogMap<K, V, Map<K, V>> log = (LogMap<K, V, Map<K, V>>)exist;
		return log;
	}

	public static <K, V> NavigableMap<K, V> logNavigableMap(LogKey key, NavigableMap<K, V> wrapped) {
		Map<LogKey, Object> wrappers = Transaction.current().wrappers;
		Object exist = wrappers.get(key);
		if (null == exist) {
			LogNavigableMap<K, V> log = new LogNavigableMap<K, V>(key, wrapped);
			wrappers.put(key, log);
			return log;
		}
		@SuppressWarnings("unchecked")
		LogNavigableMap<K, V> log = (LogNavigableMap<K, V>)exist;
		return log;
	}

	public static <E> Set<E> logSet(LogKey key, SetX<E> wrapped) {
		Map<LogKey, Object> wrappers = Transaction.current().wrappers;
		Object exist = wrappers.get(key);
		if (null == exist) {
			LogSet<E> log = new LogSet<E>(key, wrapped);
			wrappers.put(key, log);
			return log;
		}
		@SuppressWarnings("unchecked")
		LogSet<E> log = (LogSet<E>)exist;
		return log;
	}

	//////////////////////////////////////////////////////////
	/** 
     * Returns true if instances of the given class is known to be immutable; 
     * false if we don't know.
     */
	public static boolean isImmutable(@SuppressWarnings("rawtypes") Class t) {
		// ��ʹ��Ƶ���̶��ж�
		return t == String.class
				|| t == Integer.class || t == Long.class
				|| t == Boolean.class || t == Short.class
				|| t == Byte.class || t == Character.class
				|| t == Float.class || t == Double.class
				|| t.isPrimitive()
				//|| t == BigInteger.class || t == BigDecimal.class || t.isEnum()
				;
	}

	/** 
     * Returns true if instances of the given class is known to be immutable; 
     * false if we don't know.
     */
	public static boolean isImmutable(Object obj) {
		return isImmutable(obj.getClass());
	}

	/**
	 * bean managed setup.
	 * 
	 * @param bean    bean to setup
	 * @param parent  parent reference
	 * @param varname name in parent
	 * @param log ��¼��־��������ʧ��ʱ�ع����ָ��ɵ�parent��varname��
	 * 	      �����ݴ�db.h��һ��װ�ؽ���ʱ������Ҫ��¼��־���������óɹ���@see TRecord
	 * @exception NullPointerException if bean is null.
	 * @exception XManagedError if setup fail or bean can not be managed.
	 */
	public static void xdbParent(Object bean, XBean parent, String varname, boolean log) {
		if (null == bean)
			throw new NullPointerException();

		if (bean instanceof Bean) {
			// ��ֹ XBean.Data �������
			if (!(bean instanceof XBean))
				throw new XManagedError();

			((XBean)bean).xdbParent(parent, varname, log);
			return;
		}

		// ��Ϊ����Any���ͣ������޷����������ˡ�
		/*
		if (isImmutable(bean))
			return;
		throw new XManagedError();
		*/
	}

	public static void xdbParent(Object bean, XBean parent, String varname) {
		Logs.xdbParent(bean, parent, varname, true);
	}

	/**
	 * ���beanû�б�����.
	 * 
	 * @param bean
	 * @exception NullPointerException if bean is null.
	 * @exception XManagedError if bean has managed or bean can not be managed.
	 */
	public static void xdbManagedCheck(Object bean) {
		if (null == bean)
			throw new NullPointerException();

		if (bean instanceof Bean) {
			// ��ֹ XBean.Data �������
			if (!(bean instanceof XBean))
				throw new XManagedError();

			if (((XBean)bean).xdbManaged())
				throw new XManagedError();

			return;
		}

		// ��Ϊ����Any���ͣ������޷����������ˡ�
		/*
		if (isImmutable(bean))
			return;
		throw new XManagedError();
		*/
	}

	public static void logNotify(XBean xbean, mkdb.logs.LogNotify notify) {
		xbean.xdbLogNotify(notify);
	}
}
