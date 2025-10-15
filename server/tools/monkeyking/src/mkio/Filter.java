package mkio;

import java.nio.ByteBuffer;

public class Filter {
	
	private final String name;

	public Filter(String name) {
		this.name = name;
	}

	public final String getName() {
		return this.name;
	}

	/**
	 * �����������ʵ�ֹ��˹��ܡ�
	 * <p>
	 * ���ʵ�ֲ���������������ô������it.doFilterNextOf(this, b, x);��������
	 * <p>
	 * <b>ʵ����������Ĺؼ��Ƕ���ByteBuffer����⡣</b>
	 * 
	 * @param it
	 *            ����������ָʾ�������������ݴ��ݸ���һ����������
	 * @param b
	 *            �������ݣ���ʱbΪreadģʽ��
	 * @param x
	 *            ��ǰ�������ӡ�
	 */
	protected void doFilter(Iterator it, ByteBuffer b, Mkio x) {
		it.doFilterNextOf(this, b, x);
	}

	@Override
	public String toString() {
		return name;
	}

	///////////////////////////////////////////////////////////////////
	/**
	 * �б��е�Filter�����ֱ���Ψһ�� ����ɾ����ʱ�����ֱ�����ڡ�
	 * ����ʱ��Filter������û�б��ӵ�����FilterList�еġ�Ҳ����isChained������false�� ���з��ʶ� synchronized
	 * (this) ���� Mkio.Input Mkio.Output Ҳ synchronized (this)
	 */
	public static class List {
		private final java.util.ArrayList<Filter> filters = new java.util.ArrayList<Filter>();

		private final int indexOf(String name) {
			for (int index = 0; index < filters.size(); ++index)
				if (filters.get(index).getName().equals(name))
					return index;
			return -2; // NOT -1, see addAfter
		}

		private final void add(int index, Filter f) {
			if (index < 0)
				throw new ArrayIndexOutOfBoundsException();
			if (indexOf(f.getName()) >= 0) // ���ִ���
				throw new IllegalStateException();
			filters.add(index, f);
		}

		public void addAfter(String name, Filter f) {
			synchronized (this) {
				add(indexOf(name) + 1, f);
			}
		}

		public void addBefore(String name, Filter f) {
			synchronized (this) {
				add(indexOf(name), f);
			}
		}

		public void addFirst(Filter f) {
			synchronized (this) {
				add(0, f);
			}
		}

		public void addLast(Filter f) {
			synchronized (this) {
				add(filters.size(), f);
			}
		}

		public void clear() {
			synchronized (this) {
				filters.clear();
			}
		}

		public Filter get(String name) {
			synchronized (this) {
				int index = indexOf(name);
				if (index < 0)
					return null;
				return filters.get(index);
			}
		}

		public Filter remove(String name) {
			synchronized (this) {
				return filters.remove(indexOf(name));
			}
		}

		public String toString() {
			synchronized (this) {
				return filters.toString();
			}
		}

		// package private
		final java.util.List<Filter> raw() {
			return filters;
		}
	}

	public static interface Iterator {
		public void doFilterNextOf(Filter f, ByteBuffer b, Mkio x);
	}
	
}
