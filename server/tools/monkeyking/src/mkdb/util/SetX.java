package mkdb.util;

import java.util.Set;
import java.util.AbstractSet;
import java.util.Iterator;

/**
 * �� java.util.HashSet ����һ�£�ʹ�� java.util.HashMap ʵ�֡�
 * �������
 *    1 �� key ������ map �� value �С�
 *    2 ����һ�� removex ������ɾ��ʱ���Ի��ԭ�������á�
 */
public final class SetX<E> extends AbstractSet<E> implements Set<E> {
	private transient java.util.HashMap<E, E> map;

	public SetX() {
		map = new java.util.HashMap<E, E>();
	}

	public SetX(int initialCapacity) {
		map = new java.util.HashMap<E, E>(initialCapacity);
	}

	public SetX(int initialCapacity, float loadFactor) {
		map = new java.util.HashMap<E, E>(initialCapacity, loadFactor);
	}

	@Override
	public Iterator<E> iterator() {
		return map.keySet().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	@Override
	public boolean add(E e) {
		if (null == e)
			throw new NullPointerException();
		if (map.containsKey(e))
			return false;
		map.put(e, e);
		return true;
	}

	public E removex(Object o) {
		return map.remove(o);
	}

	@Override
	public boolean remove(Object o) {
		return null != removex(o);
	}

	@Override
	public void clear() {
		map.clear();
	}

//    public static int hash(int h) {
//        // This function ensures that hashCodes that differ only by
//        // constant multiples at each bit position have a bounded
//        // number of collisions (approximately 8 at default load factor).
//        h ^= (h >>> 20) ^ (h >>> 12);
//        return h ^ (h >>> 7) ^ (h >>> 4);
//    }
//
//    @Override
//	public boolean containsAll(java.util.Collection<?> c) {
//		Iterator<?> e = c.iterator();
//		while (e.hasNext()) {
//			Object n = (Object)e.next();
//			E x = map.get(n);
//			if (null == x) {
//				Iterator<E> keys = map.keySet().iterator();
//				E e2 = keys.next();
//				Set<java.util.Map.Entry<E, E>> es = map.entrySet();
//				mkdb.Trace.debug(es);
//				java.util.Map.Entry<E, E> xx = es.iterator().next();
//				mkdb.Trace.debug(xx.getKey() == xx.getValue());
//				mkdb.Trace.debug(map.size());
//				mkdb.Trace.debug(hash(n.hashCode()) + " " + hash(e2.hashCode()));
//				mkdb.Trace.debug(n.equals(e2));
//				mkdb.Trace.debug(n == e2);
//				mkdb.Trace.debug(n + " " + e2);
//				mkdb.Trace.debug(c.getClass().getName());
//				return false;
//			}
//		}
//		return true;
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (o == this)
//			return true;
//
//		if (!(o instanceof Set))
//			throw new RuntimeException("!set");
//		java.util.Collection<?> c = (java.util.Collection<?>) o;
//		if (c.size() != size())
//			throw new RuntimeException("!size");
//		try {
//			return containsAll(c);
//		} catch (ClassCastException unused) {
//			throw new RuntimeException("!cast");
//		} catch (NullPointerException unused) {
//			throw new RuntimeException("!!null");
//		}
//	}
}
