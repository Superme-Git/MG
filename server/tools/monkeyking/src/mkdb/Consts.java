package mkdb;

import java.util.*;

/**
 * �����޸�����ת�������ࡣ������װ mkdb.consts.* �������ʹ�á�
 */
public final class Consts {
	public static <E> List<E> constList(List<E> list) {
		return new mkdb.consts.ConstList<E>(list);
	}

	public static <E> Set<E> constSet(Set<E> w) {
		return new mkdb.consts.ConstSet<E, Set<E>>(w);
	}

	public static <K, V> Map<K, V> constMap(Map<K, V> map) {
		return new mkdb.consts.ConstMap<K, V, Map<K, V>>(map);
	}

	public static <K, V> NavigableMap<K, V> constNavigableMap(NavigableMap<K, V> w) {
		return new mkdb.consts.ConstNavigableMap<K, V>(w);
	}

	public static <T> T[] toConst(T[] a) {
		for (int i = 0; i < a.length; ++i) {
			a[i] = toConst(a[i]);
		}
		return a;
	}

	public static <T> T toConst(T t) {
		if(null != t && t instanceof Bean) {
			@SuppressWarnings("unchecked")
			T e = (T)((Bean)t).toConst();
			return e;
		}
		return t;
	}
}
