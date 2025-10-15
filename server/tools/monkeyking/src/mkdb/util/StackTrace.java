package mkdb.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.ThreadInfo;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * �����̵߳� StackTrace ͳ���̵߳Ĺ����ֲ���
 * @see mkdb/docs/stacktrace.txt
 */
public class StackTrace {
	private final String nsClass[];
	private final String nsLock[];

	public StackTrace(String nsClass, String nsLock) {
		this(nsClass.split("[;,]"), nsLock.split("[;,]"));
	}

	private String[] foo(String [] nss) {
		List<String> list = new ArrayList<String>();
		for (String ns : nss) {
			list.add(ns.trim());
		}
		return list.toArray(new String[list.size()]);
	}

	public StackTrace(String [] nsClass, String [] nsLock) {
		this.nsClass = foo(nsClass);
		this.nsLock = foo(nsLock);
	}

	private String findNsClass(StackTraceElement ste) {
		for (String ns : nsClass)
			if (ste.getClassName().startsWith(ns))
				return ns;
		return null;
	}

	private String findNsLock(StackTraceElement ste) {
		for (String ns : nsLock)
			if (ste.getClassName().startsWith(ns))
				return ns;
		return null; //ste.getClassName(); // ����������
	}

	private void increment(Map<String, AtomicInteger> counter, String key) {
		if (null != key) {
			AtomicInteger value = counter.get(key);
			if (null == value)
				counter.put(key, value = new AtomicInteger(0));
			value.incrementAndGet();
		}
	}
	/**
	 * �̵߳Ĺ����ֲ�״̬��
	 * @return
	 */
	public Map<String, AtomicInteger> top() {
		final Map<String, AtomicInteger> counter = new HashMap<String, AtomicInteger>();

		final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		for (ThreadInfo tinfo : threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), Integer.MAX_VALUE)) {
			if (null == tinfo)
				continue;

			// lock count
			increment(counter, buildLockKeyFor(tinfo));

			// class count
			String namespace = null;
			int pos = 0;
			for (StackTraceElement ste : tinfo.getStackTrace()) {
				namespace = findNsClass(ste);
				if (null != namespace)
					break;
				++ pos;
			}

			increment(counter, buildClassKeyFor(tinfo, namespace, pos));
		}
		return counter;
	}

	public String buildLockKeyFor(ThreadInfo tinfo) {
		if (null != tinfo.getLockInfo()) {
			for (StackTraceElement ste : tinfo.getStackTrace()) {
				final String namespace = findNsLock(ste);
				if (null != namespace) {
					return " - " + ste.getClassName() + "." + ste.getMethodName()
							+ " (" + tinfo.getLockInfo().getClassName() + ")";
				}
			}
			return null; // " - " + tinfo.getLockInfo().getClassName();
		}
		return null;
	}

	/**
	 * ��������õķ������֣� KEY��
	 * @param thread ��ջ���߳�
	 * @param stes   ��ջ
	 * @param namespace �ҵ��ĸ���Ȥ�����ֿռ䡣����Ϊ null��������еĶ�ջԪ�ض����ڸ���Ȥ�����ֿռ��
	 * @param pos �ҵ��ĸ���Ȥ�����ֿռ�� stes ��ƫ��������
	 * @return key for count. skip count if return null. 
	 */
	public String buildClassKeyFor(ThreadInfo tinfo, String namespace, int pos) {
		if (null == namespace) {
			//System.err.println(thread + Arrays.toString(stes));
			// return null to skip careless StackTrace��
			return "[Others]"; // ���Ϸ��ţ���������������
		}
		final StackTraceElement[] stes = tinfo.getStackTrace();
		final StringBuilder sb = new StringBuilder();
		// 1
		// ɾ�����ֿռ䣨����������ʾ��ʱ����ÿ�Щ��
		sb.append(stes[pos].getClassName()).append(".").append(stes[pos].getMethodName());

		// 2
		// ����ҵ��Ķ�ջ���ǵ�һ�����ѵ�һ��Ҳ���뵽key���档
		// Ҳ����˵����ͬ���෽�����ã����ִ�е��ڲ���֧��ͬҲ�ᱻ�ֿ�ͳ�ơ�
		if (pos > 0) {
			sb.append(" (");
			sb.append(stes[0].getClassName()).append('.').append(stes[0].getMethodName());
			sb.append(")");
		}

		// 3
		// �߳�״̬�ֱ�ͳ��
		//sb.append(" ").append(tinfo.getThreadState());
		return sb.toString();
	}

	public static void main(String args[]) throws Exception {
		String x = "x, b,";
		for (String s : x.split("[;,]"))
			System.out.println("-" + s);
		for (int i = 0; i < 1; ++i) {
			for (Map.Entry<String, AtomicInteger> entry : new StackTrace("", "").top().entrySet())
				System.out.println(entry);
			System.out.println();
			Thread.sleep(100);
		}
	}
}
