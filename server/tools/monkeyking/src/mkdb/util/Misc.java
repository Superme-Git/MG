package mkdb.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ���߰���������䡣
 * ���з��ġ�
 */
public class Misc {

	private static Misc misc = new Misc();

	public static Misc getInstance() {
		return misc;
	}

	/////////////////////////////////////////////////////////////////////////////
	// ConcurrentHashMap Ĭ�ϵĲ�������Ϊ16���������ˣ����ڶ�16���ˡ�
	//                   һ����˵������������һ��Ӧ��ϵͳ��ʹ��һ���������Ǻ���ġ�
	//                   �ڴ��ṩͳһ��������ڣ������Ҫ�޸ģ���ȽϷ��㡣
	//
	// ����ʹ������ĸ������������� ConcurrentMap��
	private volatile int concurrencyLevel = 256;

	/**
	 * ���ò��������µĲ�������ֻӰ���½��� ConcurrentMap��
	 */
	public synchronized void setConcurrencyLevel(int concurrencyLevel) {
		this.concurrencyLevel = concurrencyLevel;
	}

	public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
		return new ConcurrentHashMap<K, V>(16, 0.75f, misc.concurrencyLevel);
	}

	public static <K, V> ConcurrentMap<K, V> newConcurrentMap(int initialCapacity) {
		return new ConcurrentHashMap<K, V>(initialCapacity, 0.75f, misc.concurrencyLevel);
	}

	public static <K, V> ConcurrentMap<K, V> newConcurrentMap(
			int initialCapacity, float loadFactor, int concurrencyLevel) {
		return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor, concurrencyLevel);
	}
}
