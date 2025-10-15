package mkio.rpc;

import com.locojoy.base.Marshal.Marshal;

/**
 * �洢���̺�Rpc��ʱ��Ҫʵ������ӿ�
 *
 * @param <A>
 * @param <R>
 */
public interface Procedure<A extends Marshal, R extends Marshal> {
	public void setArgument(A a);

	public void setResult(R r);

	public void setConnection(mkio.Mkio from);

	public A getArgument();

	public R getResult();

	public mkio.Mkio getConnection();
}
