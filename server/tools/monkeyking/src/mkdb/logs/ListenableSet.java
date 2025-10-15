package mkdb.logs;

public final class ListenableSet extends Listenable {
	private Note note;

	@Override
	@SuppressWarnings("rawtypes")
	public void setChanged(LogNotify ln) {
		// ��Ϊ�����������ڼ���Setǰ���Ǽ���Set�����bean���޸Ĳ��������Բ��ϸ�����Set�е�bean�����޸���
		// ������ʹ�õ�ʱ��һ��Ҫע�⣬���ܶԼ���Set��bean�����޸ģ���ΪSet������bean��hashCode
		if (!ln.isLast()) {
			//throw new IllegalStateException();
			return;
		}
		
		if (null == note) {
			note = ln.getNote();
		} else {
			((NoteSet)note).merge(ln.getNote()); // unchecked
		}
	}
	
	@Override
	public ListenableSet copy() {
		ListenableSet l = new ListenableSet();
		l.fullVarName = this.fullVarName;
		return l;
	}

	@Override
	public void logNotify(Object key, RecordState rs, ListenerMap listenerMap) {
		switch (rs) {
		case ADDED:
			listenerMap.notifyChanged(fullVarName, key);
			break;

		case REMOVED:
			listenerMap.notifyRemoved(fullVarName, key);
			break;

		case CHANGED:
			if (null != note)
				listenerMap.notifyChanged(fullVarName, key, note);
			break;
		}
		note = null;
	}
}

