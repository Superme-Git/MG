package mkdb.logs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ����������ݱ�������ߡ�ÿ�ű�ӵ��һ���Լ���listener�������
 * ���ܣ����ӣ�ɾ������ѯ�������ص���
 */
public final class ListenerMap {
	private Map<String, Set<Listener>> listenerMap = new HashMap<String, Set<Listener>>();
	private final Lock sync = new ReentrantLock();

	/**
	 * listener���������ڶ��߳�ֻ�����ʡ�
	 * �� listenerMap �����ı��ʱ������һ��������
	 */
	private volatile Map<String, Set<Listener>> listenerMapCopy = new HashMap<String, Set<Listener>>();

	// MUST under sync lock
	private void setListenerMapCopy() {
		Map<String, Set<Listener>> localCopy = new HashMap<String, Set<Listener>>();
		for (Map.Entry<String, Set<Listener>> e : listenerMap.entrySet()) {
			Set<Listener> values = new HashSet<Listener>();
			values.addAll(e.getValue());
			localCopy.put(e.getKey(), values);
		}
		this.listenerMapCopy = localCopy;
	}

	public final String add(String fullVarName, Listener l) {
		sync.lock();
		try {
			Set<Listener> ls = listenerMap.get(fullVarName);
			if (null == ls)
				listenerMap.put(fullVarName, ls = new HashSet<Listener>());
			if (!ls.add(l))
				throw new IllegalStateException("Listener has added");

			setListenerMapCopy();
		} finally {
			sync.unlock();
		}
		return fullVarName;
	}

	public final void remove(String fullVarName, Listener l) {
		sync.lock();
		try {
			Set<Listener> ls = listenerMap.get(fullVarName);
			if (null != ls) {
				boolean changed = ls.remove(l);
				if (ls.isEmpty())
					listenerMap.remove(fullVarName);

				if (changed)
					setListenerMapCopy();
			}
		} finally {
			sync.unlock();
		}
	}

	public final boolean hasListener() {
		final Map<String, Set<Listener>> localCopy = this.listenerMapCopy;
		return false == localCopy.isEmpty();
	}

	public final boolean hasListener(String fullVarName) {
		final Map<String, Set<Listener>> localCopy = this.listenerMapCopy;
		// empty Set will remove immediately. see this.remove
		return null != localCopy.get(fullVarName);
	}

	/////////////////////////////////////////////////////////////////////////
	final void notifyChanged(String fullVarName, Object key) {
		notify(ZZZ.CHANGED_ALL, fullVarName, key, null);
	}

	final void notifyRemoved(String fullVarName, Object key) {
		notify(ZZZ.REMOVED, fullVarName, key, null);
	}

	final void notifyChanged(String fullVarName, Object key, Note note) {
		notify(ZZZ.CHANGED_NOTE, fullVarName, key, note);
	}

	private static enum ZZZ {
		// ���ֵ���Listener�Ĳ�ͬ�ķ�������Ҫʱ�ٳ���
		CHANGED_ALL, CHANGED_NOTE, REMOVED
	}

	private final void notify(ZZZ zzz, String fullVarName, Object key, Note note) {
		final Map<String, Set<Listener>> localCopy = this.listenerMapCopy;
		final Set<Listener> listeners = localCopy.get(fullVarName);

		if (null == listeners)
			return;

		for (Listener l : listeners) {
			mkdb.Transaction trans = mkdb.Transaction.current();
			int spBefore = trans.currentSavepointId();
			int spBeforeAccess = spBefore > 0 ? trans.getSavepoint(spBefore).getAccess() : 0;

			try {
				switch (zzz) {
				case CHANGED_ALL: l.onChanged(key); break;
				case CHANGED_NOTE: l.onChanged(key, fullVarName, note); break;
				case REMOVED: l.onRemoved(key); break;
				}

			} catch (Throwable e) {
				mkdb.Trace.error("doChanged key=" + key + " name=" + fullVarName, e);

				/*
				 * �ص����������
				 *     spBefore     spAfter     rollback      desc
				 *     -----------------------------------------------------------
				 * (a) 0            0           NONE          ǰ��û�б����
				 * (b) 0            >0          spBefore + 1  ��ʼǰû�У��ص��д����˱���㡣
				 * (c) >0 Any       <spBefore   ERROR         ����
				 * (d) >0 Dirty     -           spBefore      ��ʼǰ�ı���㷢���˸ı�
				 * (e) >0 Clean     >spBefore   spBefore + 1
				 * (f) >0 Clean     =spBefore   NONE          �ص�û���޸Ĳ�����
				 * 
				 */
				int spAfter = trans.currentSavepointId();
				if (0 == spBefore) {
					if (spAfter > 0) // (b)
						trans.rollback(spBefore + 1);
					// else (a)
				} else {
					if (spAfter < spBefore) // (c)
						throw new IllegalStateException("spAfter < spBefore");

					if (trans.getSavepoint(spBefore).isAccessSince(spBeforeAccess)) // (d)
						trans.rollback(spBefore);
					else if (spAfter > spBefore) // (e)
						trans.rollback(spBefore + 1);
					// else (f)
				}
			}
		}
	}
}
