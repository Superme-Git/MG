package mkdb.logs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class ListenableBean extends Listenable {
	private boolean changed = false;
	private Map<String, Listenable> vars = new HashMap<String, Listenable>();

	public void add(Listenable la) {
		if (null != vars.put(la.getVarName(), la))
			throw new IllegalStateException();
	}

	@Override
	public String toFullVarName(VarNames names) {
		if (names.hasNext())
			return vars.get(names.next()).toFullVarName(names);
		return super.fullVarName;
	}

	@Override
	public void makeFullVarName(String base) {
		super.makeFullVarName(base);
		for (Listenable la : vars.values())
			la.makeFullVarName(super.fullVarName);
	}
	
	@Override
	public ListenableBean copy() {
		ListenableBean l = new ListenableBean();
		l.fullVarName = this.fullVarName;
		for (Entry<String, Listenable> e : vars.entrySet()) {
			l.vars.put(e.getKey(), e.getValue().copy());
		}
		return l;
	}

	@Override
	public void setChanged(LogNotify ln) {
		this.changed = true;
		// ln ��ʱ�϶����ǿյģ�bean�����иı䶼������vars��������û���޸Ĳ����ġ�
		vars.get(ln.pop().getVarname()).setChanged(ln);
	}

	@Override
	public void logNotify(Object key, RecordState rs, ListenerMap listenerMap) {
		switch (rs) {
		case NONE:
			// �¼�NONE ����Ҫ����
			return;
		case ADDED:
			listenerMap.notifyChanged(fullVarName, key);
			break;

		case REMOVED:
			listenerMap.notifyRemoved(fullVarName, key);
			break;

		case CHANGED:
			if (changed) {
				listenerMap.notifyChanged(fullVarName, key, null);
			} else
				return; // �Ż��������ǰbeanû���޸ģ������϶�Ҳû�С�see setChanged
			break;
		}
		changed = false;
		for (Listenable la : vars.values())
			la.logNotify(key, rs, listenerMap);
	}
}

