package mkdb.util;

import java.util.concurrent.ConcurrentMap;
import java.util.Map.Entry;

/**
 * �;��ȳ�ʱ��ʱ��ʵ�֡����ӳٵ���ʱִ���������ԣ�
 * <ul>
 * <li><b>ר�õĶ�ʱ��ʵ�֣�not well-defined.</b>
 * <li><b>���ʺ��������ȴ���ͬʱ���ڵĳ�ʱ�������ڽ�����ʵ�� mkdb.Executor �ĳ�ʱ������</b>
 * <li>�����ڳ�ʱ����ʵ���Ϻ��ٷ��������Ρ�
 * <li>�;��ȡ��뼶��
 * <li>����ConcurrentMapʵ�֣���߲����ԡ�
 * <li>���������ڴ棨�����ͷŶ������ã���remove �� cancel��
 * <li>��ʱ����ʱ��ֱ���ڵ����߳���ִ�С�����Ŀ����������Ǻܿ���ɵġ�
 * <li>����Ŀ��Runnable��ò�����ʵ��Object��hashCode��equals��������ܶ�Ч����ɽϴ�Ӱ�졣
 * <li>��ʱ����ʹ�ýӿ�Runnable�����Ƕ����µĽӿ�Timeout������������ͬһ������ʵ�֡�
 * </ul>
 */
public final class TimeoutManager implements Runnable {
	private static TimeoutManager instance = new TimeoutManager();

	/**
	 * @deprecated ����ʹ�õ���ģʽ������������ֹͣ�Ĺ�������ʹ�÷��㡣����˰ɡ�
	 * @return
	 */
	public static TimeoutManager getInstance() {
		return instance;
	}

	/////////////////////////////////////////////////////////////////
	private ConcurrentMap<Timeout, Long> targets = mkdb.util.Misc.newConcurrentMap();

	public static interface Timeout {
		/**
		 * ��ʱ����ʱ�ص����������������ʵ��һЩ�򵥿�ݵĲ�����
		 */
		public void onTimeout();
	}

	public TimeoutManager() {
	}

	/**
	 * ���ȳ�ʱ����
	 * @param target ִ��Ŀ�ꡣ
	 * @param timeout ��λΪ���롣��Ȼʵ�ʾ������뼶��
	 * @return ���� target��
	 */
	public Timeout schedule(Timeout target, long timeout) {
		if (target == null)
			throw new NullPointerException();

		if (timeout < 0)
			timeout = 0;

		targets.put(target, System.currentTimeMillis() + timeout);
		return target;
	}

	/**
	 * ɾ����ȡ����ʱ�����������
	 * @param target
	 * @return
	 */
	public boolean remove(Timeout target) {
		return null != targets.remove(target);
	}

	/**
	 * @deprecated ���������̲߳���ȫ�ģ�ֻ����һ���߳��е��á�
	 */
	public void run() {
		long now = System.currentTimeMillis();
		for (Entry<Timeout, Long> e : targets.entrySet()) {
			final Timeout target = e.getKey();
			if (e.getValue() <= now && remove(target)) {
				try {
					target.onTimeout();
				} catch (Throwable x) {
					mkdb.Trace.error("Timeout.target.run", x);
				}
			}
		}
	}

	/**
	 * @return the number of in this timeout.
	 */
	public int size() {
		return targets.size();
	}

	public void clear() {
		targets.clear();
	}
}
