package mkdb.logs;

/**
 * ע����̸����ࡣ
 * 
 * Ŀǰֻ������һ����ʵ���Ͽ��Բ������顣
 * 
 * @author cn
 *
 */
public class VarNames {
	private String [] path;
	private int pos;

	public VarNames(String ... path) {
		this.path = path;
		this.pos = 0;
	}

	public String currentFullVarName() {
		if (path.length == 0)
			return ".";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < pos; ++i) {
			sb.append('.').append(path[i]);
		}
		return sb.toString();
	}

	public String next() {
		return path[pos++];
	}

	public boolean hasNext() {
		return pos < path.length;
	}

	public void assertLast() {
		if (hasNext())
			throw new IllegalStateException();
	}

	public void reset() {
		this.pos = 0;
	}
}
