package mkdb;

/**
 * Ӧ�ò��ܲ���������󣬱�����xdb������
 * 
 * @see Procedure
 * 
 */
public class XError extends Error {
	private static final long serialVersionUID = -2753495176885937511L;

	XError() {
	}

	XError(String message) {
		super(message);
	}

	XError(Throwable cause) {
		super(cause);
	}

	XError(String message, Throwable cause) {
		super(message, cause);
	}
}
