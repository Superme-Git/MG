package mkdb;

/**
 * xbeanʵ�����Ƕ��������ݶ������е�ʵ���������á�
 * �κ�xbean���ֻ������һ��xbean��xbean֮�䲻��������ظ������á�
 * һ���򵥵�Υ����
 * <pre>
 * class A {}
 * class B { List<A> getListA(); }
 * A a = xbean.Pod.newA();
 * B b = xbean.Pod.newB();
 * List<A> lista = b.getListA();
 * lista.add(a);
 * lista.add(a); // �ظ����룬�׳� XManagedError
 * </pre>
 * 
 * @see Procedure
 * 
 */
public class XManagedError extends XError {
	static final long serialVersionUID = 7269011645942640931L;
	XManagedError() {
	}
	XManagedError(String message) {
		super(message);
	}
}
