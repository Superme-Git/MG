package mkdb.util;

import java.util.LinkedList;

import mkdb.Procedure;

/**
 * ������˳�򣬰���ִ�д洢���̡��κ�ʱ�����ֻ��һ���洢��������ִ�С�
 * 
 * �ڲ����첽�����£�Ϊ����ȷ����ĳЩ���⣬��Ҫ��Ƹ��ӵ�Э�顣
 * OneByOne ���Ⲣ����˳���Ժ����Э�������ס�
 * 
 * ���ܣ�
 *   1 �������ơ��ﵽ�������add �����׳� RuntimeException��
 *   2 �������ƣ�(δʵ��)������ʱ����ƴ洢����������
 *   3 ͳ�ƣ�(δʵ��)��Ū������ͳ�ƣ�
 *   4 shutdown��(δʵ��)��a) �ȴ����еĶ�ִ�����˲ŷ��� b)�ж�����ִ�еģ����ػ�û��ִ�еĴ洢���̡�
 *   
 * @author cn
 *
 */
public class ProcedureOneByOne {
	private int maxsize;
	private LinkedList<mkdb.Procedure> onebyone = new LinkedList<mkdb.Procedure>();
	private mkdb.Procedure.Done<mkdb.Procedure> done = new mkdb.Procedure.Done<mkdb.Procedure>() {
		@Override
		public void doDone(Procedure p) {
			synchronized (ProcedureOneByOne.this) {
				Procedure f = onebyone.removeFirst();
				assert(f == p); // ����OneByOne���⡣
				if (onebyone.size() > 0)
					mkdb.Procedure.execute(onebyone.peekFirst(), done);
			}
		}
	};

	public ProcedureOneByOne() {
		this.maxsize = 0; // unlimited
	}

	public ProcedureOneByOne(int maxsize) {
		this.maxsize = maxsize;
	}

	public void add(mkdb.Procedure p) {
		synchronized (this) {
			if (maxsize > 0 && onebyone.size() > maxsize)
				throw new RuntimeException("out of capacity! maxsize=" + maxsize);
			onebyone.addLast(p);
			if (onebyone.size() == 1)
				mkdb.Procedure.execute(onebyone.peekFirst(), done);
		}
	}

	//////////////////////////////////////////////////////////////
	// ��ʱ��Ҫ��������ķ�������Щ���ܻ�ûȷ����
	public mkdb.Procedure peekDebugOnly() {
		synchronized (this) {
			return onebyone.peekFirst();
		}
	}

	public int sizeDebugOnly() {
		synchronized (this) {
			return onebyone.size();
		}
	}
}
