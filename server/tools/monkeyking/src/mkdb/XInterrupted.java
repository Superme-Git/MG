package mkdb;

/**
 * 
 * �̱߳� interrupt ʱ�׳�����쳣���������治��Ҫ�ر�����жϡ�
 * <b>δ�á�</b>
 * 
 */
public class XInterrupted extends RuntimeException {
	static final long serialVersionUID = -7927226240291624476L;

	public XInterrupted() {
	}

	public XInterrupted(String message) {
		super(message);
	}

	public XInterrupted(Throwable e) {
		super(e);
	}

	public XInterrupted(String message, Throwable e) {
		super(message, e);
	}
}
