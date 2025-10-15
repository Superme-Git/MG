package mkdb.logs;

import java.util.HashMap;
import java.util.Map;

import mkdb.TRecord;
import mkdb.TTable;
import mkdb.Transaction;
import mkdb.TRecord.State;

/**
 * �ռ��޸���־������ڡ�
 * ��xdb.TTable����
 * ÿ�������߳����Լ���ʵ����
 * 
 * @author cn
 *
 * @param <K>
 * @param <V>
 */
public final class LogRecord<K, V> {
	private Map<K, LogR<K, V>> changed = new HashMap<K, LogR<K, V>>();
	private Listenable seed;
	private int hasListener; // 0 δ��ʼ����1 �� -1 û��

	public LogRecord(Object o) {
		this.seed = (o instanceof mkdb.XBean) ? ((mkdb.XBean)o).newListenable() : new mkdb.logs.ListenableChanged();
		this.seed.setVarName(TRecord.RECORD_VARNAME).makeFullVarName("");
	}

	public String toFullVarName(VarNames names) {
		if (!TRecord.RECORD_VARNAME.equals(names.next()))
			throw new IllegalStateException();
		return seed.toFullVarName(names);
	}

	public void onChanged(TTable<K, V> table, TRecord<K, V> r, LogNotify ln) {
		/*
		 * û���˼�����ʱ�򣬲��ռ���־��see LogRecord.onChanged��
		 * �����ύ��һ���޸���־�����ʱ�򣬼�¼ hasListener ״̬��
		 * ����һ�������ύ�����б��ֲ��䣬�ύ����Ժ���logNotify��������״̬��
		 * ���˵�һ�������ﲻʹ��hasListener()�������жϣ���Ϊ�˷�ֹ���ռ����ݱ���Ĺ����У�
		 * ���listener�����仯��������ݱ���ռ�����ȫ��
		 * ԭ���ǣ�Ҫ��ȫ���ռ���Ҫ��ȫ�����ԡ�
		 */
		if (hasListener == 0)
			hasListener = table.hasListener() ? 1 : -1;

		Transaction.current().addLogNotifyTTable(table);

		if (hasListener == -1)
			return;

		LogR<K, V> lr = changed.get(r.getKey());
		if (null == lr)
			changed.put(r.getKey(), lr = new LogR<K, V>(r, seed.copy()));
		if (ln.isAddRemove()) {
			/**
			 * {Merge}ֻ�������е�һ��Savepoint��addRemoveInfo����������Listener֪ͨ�ж�
			 * �����ڸ���Savepoint����˳���ύ
			 */
			if (null == lr.addRemoveInfo) {
				lr.addRemoveInfo = ln.getAddRemoveInfo();
			}
		} else {
			ln.pop();
			lr.value.setChanged(ln);
		}
	}

	public void logNotify(ListenerMap listenerMap) {
		hasListener = 0;
		for (LogR<K, V> lr : changed.values())
			lr.value.logNotify(lr.r.getKey(), lr.getRecordState(), listenerMap);
		changed.clear();
	}

	private static class LogR<K, V> {
		private TRecord<K, V> r;
		private AddRemoveInfo addRemoveInfo;
		private Listenable value;

		LogR(TRecord<K, V> r, Listenable value) {
			this.r = r;
			this.value = value;
		}
		
		private mkdb.logs.RecordState getRecordState() {
			if (null != addRemoveInfo) {
				// �����xdb.record.watch.state.xlsx�ļ�����ֵ��
				if (addRemoveInfo.isCreateCache() && State.ADD == addRemoveInfo.getSavedState() && State.REMOVE == r.getState()) {
					return mkdb.logs.RecordState.NONE;	  // ��ֵ���1��
				} else if (!addRemoveInfo.isCreateCache() && State.ADD == addRemoveInfo.getSavedState() && State.REMOVE == r.getState()) {
					return mkdb.logs.RecordState.REMOVED;  // ��ֵ���2��
				} else if (State.ADD == addRemoveInfo.getSavedState() && State.ADD == r.getState()) {
					return mkdb.logs.RecordState.ADDED;    // ��ֵ���3��4��
				} else if (State.INDB_GET == addRemoveInfo.getSavedState() && State.INDB_REMOVE == r.getState()) {
					return mkdb.logs.RecordState.REMOVED;  // ��ֵ���5��6��
				} else if (State.INDB_GET == addRemoveInfo.getSavedState() && State.INDB_ADD == r.getState()) {
					return mkdb.logs.RecordState.ADDED;    // ��ֵ���7��8��
				} else if (State.INDB_REMOVE == addRemoveInfo.getSavedState() && State.INDB_ADD == r.getState()) {
					return mkdb.logs.RecordState.ADDED;    // ��ֵ���9��10��
				} else if (addRemoveInfo.isCreateCache() && State.INDB_REMOVE == addRemoveInfo.getSavedState() && State.INDB_REMOVE == r.getState()) {
					return mkdb.logs.RecordState.REMOVED;  // ��ֵ���11��
				} else if (!addRemoveInfo.isCreateCache() && State.INDB_REMOVE == addRemoveInfo.getSavedState() && State.INDB_REMOVE == r.getState()) {
					return mkdb.logs.RecordState.NONE;     // ��ֵ���12��
				} else if (addRemoveInfo.isCreateCache() && State.REMOVE == addRemoveInfo.getSavedState() && State.REMOVE == r.getState()) {
					return mkdb.logs.RecordState.NONE;     // ��ֵ���13��
				} else if (addRemoveInfo.isCreateCache() && State.REMOVE == addRemoveInfo.getSavedState() && State.ADD == r.getState()) {
					return mkdb.logs.RecordState.ADDED;    // ��ֵ���14��
				} else if (!addRemoveInfo.isCreateCache() && State.INDB_ADD == addRemoveInfo.getSavedState() && State.INDB_ADD == r.getState()) {
					return mkdb.logs.RecordState.ADDED;    // ��ֵ���15��
				} else if (!addRemoveInfo.isCreateCache() && State.INDB_ADD == addRemoveInfo.getSavedState() && State.INDB_REMOVE == r.getState()) {
					return mkdb.logs.RecordState.REMOVED;  // ��ֵ���16��
				}
				else {
					throw new IllegalStateException("AddRemoveState Error! isCreateCache = " + addRemoveInfo.isCreateCache() + 
							", SavedState = " + addRemoveInfo.getSavedState() + ", State = " + r.getState());
				}
			}
			return mkdb.logs.RecordState.CHANGED;
		}
	}
}
