package mkdb.logs;

public abstract class Listenable {
	private String varName;
	protected String fullVarName; // ����ÿ��ʹ�õ�ʱ���졣

	public final Listenable setVarName(String varName) {
		this.varName = varName;
		return this;
	}

	public final String getVarName() {
		return varName;
	}

	public final String getFullVarName() {
		return fullVarName;
	}

	////////////////////////////////////////////////////////
	// Override
	public String toFullVarName(VarNames names) {
		names.assertLast();
		return this.fullVarName;
	}

	public void makeFullVarName(String base) {
		this.fullVarName = base + "." + this.varName;
	}
	
	public abstract Listenable copy();
	public abstract void setChanged(LogNotify ln);
	public abstract void logNotify(Object key, RecordState rs, ListenerMap listenerMap);
}
