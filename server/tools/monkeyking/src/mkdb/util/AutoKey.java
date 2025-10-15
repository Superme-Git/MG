package mkdb.util;

import com.locojoy.base.Marshal.OctetsStream;

/**
 * 
 * ������Key�ӿڡ�
 * 
 * ʵ�ֱ�����ȷʵ�� equals, hashCode
 * 
 * @author cn
 *
 * @param <T>
 */

public interface AutoKey<T> {

	public T next();
	public T current();

	public String getName();

	// marshal �����ʽ: ���ͱ��(byte) + �Զ�������(...).
	public OctetsStream marshal(OctetsStream os);

	/**
	 * ���key�Ƿ���ȷ��
	 * �����Ҫ�������������ӡ�
	 * 
	 * @param key
	 * @throws IllegalArgumentException if key is bad.
	 */
	public void accept(T key);
}
