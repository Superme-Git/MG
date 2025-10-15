package mkdb.logs;

/**
 * ֻ��¼�Ƿ����˱仯������¼�仯����ϸ��Ϣ��
 */
public final class ListenableChanged extends Listenable {
	private boolean changed = false;

	@Override
	public void setChanged(LogNotify ln) {
		this.changed = true;
		// ���ںܶ����Ͷ�ֻ��¼�Ƿ�����ln �������Ը���Ĳ�Ρ�
	}
	
	@Override
	public ListenableChanged copy() {
		ListenableChanged l = new ListenableChanged();
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
			if (changed)
				listenerMap.notifyChanged(fullVarName, key, null);
			break;
		}
		changed = false;
	}
}
