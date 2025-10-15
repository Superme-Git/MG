package mkdb;

import com.locojoy.base.Marshal.OctetsStream;

import mkdb.logs.AddRemoveInfo;
import mkdb.logs.LogNotify;

/**
 * ��¼ʵ�֡��ڲ�ʵ���ࡣ
 * �����¼��״̬������
 *
 * @param <K> key type
 * @param <V> value type
 */
public final class TRecord<K, V> extends XBean {
	private V value;
	private final Lockey lockey;
	private State state;

	///////////////////////////////////////
	// see TTableCacheConcurrentMap
	private volatile long lastAccessTime = System.nanoTime();

	void access() {
		this.lastAccessTime = System.nanoTime();
	}

	public final long getLastAccessTime() {
		return lastAccessTime;
	}

	///////////////////////////////////////
	public static enum State {
		INDB_GET,    // ��ѯװ�ؽ���
		INDB_REMOVE, // ɾ���������ݿ��д��ڸü�¼��
		INDB_ADD,    // ������¼�������ݿ��д��ڸü�¼���������ԭ��ɾ��������û��flush����ִ�������ӡ�
		ADD,         // �����ļ�¼
		REMOVE,      // ����������û��flush����ִ����ɾ������״̬��¼ֻ��������������У���������ͻ��cache��ɾ����
	}

	@Override
	public final String toString() {
		return getTable().getName() + "," + lockey + "," + state;
	}

	final TTable<K, V> getTable() {
		@SuppressWarnings("unchecked")
		TTable<K, V> table = (TTable<K, V>)xdbParent();
		return table;
	}

	public final K getKey() {
		@SuppressWarnings("unchecked")
		K key = (K)lockey.getKey();
		return key;
	}

	private final OctetsStream marshalKey() {
		return getTable().marshalKey(getKey());
	}

	private final OctetsStream marshalValue() {
		// ���ж�value�Ƿ�null���Ƿ�����ֱ�����쳣��
		return getTable().marshalValue(value);
	}

	public final State getState() {
		return state;
	}

	/**
	 * flush��ʱ����Ҫ�޸�state���Ƚϲ�ˬ��
	 *
	 * @param state
	 */
	final void setState(State state) {
		this.state = state;
	}

	@Override
	final void xdbLogNotify(mkdb.logs.LogNotify notify) {
		getTable().onRecordChanged(this, notify);
		//Trace.debug("Record changed state = " + state + " key=" + lockey.getKey());
	}

	/**
	 * �ύʱ,CacheAddLogͨ��������record״̬��
	 * @return true������cache�� false�����cache��
	 */
	final boolean commit() {
		switch (state) {
//		case INDB_REMOVE: break;
//		case INDB_ADD:    break;
//		case ADD:         break;
		case REMOVE:   return false;
		case INDB_GET: throw new XError("mkdb:invalid record state");
		}
		// Trace.debug("Record add/remove state = " + state + " key=" + lockey.getKey());
		return true;
	}

	/**
	 * �ع�ʱ��CacheAddLogͨ��������record״̬��
	 * @return true�����cache�� false������cache��
	 */
	final boolean rollback() {
//		switch (state) {
		// �ع���ʱ���ϸ��顣
//		case INDB_GET: throw new XError("mkdb:invalid record state");
//		case INDB_REMOVE: break;
//		case INDB_ADD: break;
//		case ADD: break; 
//		case REMOVE: break;
//		}
		return true;
	}

	final Lockey getLockey() {
		return lockey;
	}

	public static final String RECORD_VARNAME = "value";

	final LogKey getLogKey() {
		return new LogKey(this, RECORD_VARNAME);
	}

	TRecord(TTable<K, V> table, V value, Lockey lockey, boolean remove_exists) {
		this(table, value, lockey, remove_exists ? State.INDB_REMOVE : State.REMOVE);
	}

	TRecord(TTable<K, V> table, V value, Lockey lockey, State state) {
		super(table, RECORD_VARNAME);

		// key ֻ���ǳ�����������������Ҫ MkdbParent��
		//Logs.xdbParent(lockey.getKey(), this, RECORD_VARNAME);
		if (null != value) { // ɾ��ʱ�������ļ�¼���� value Ϊ null��
			Logs.xdbParent(value, this, RECORD_VARNAME, State.INDB_GET != state);
		}

		this.value  = value;
		this.lockey = lockey;
		this.state  = state;
	}

	private final void _remove() {
		Logs.xdbParent(value, null, null);
		Transaction.currentSavepoint().addIfAbsent(getLogKey(), new LogAddRemove());
		value = null;
	}

	final boolean remove() {
		switch (state) {
		case INDB_GET: _remove(); state = State.INDB_REMOVE; return true;
		case INDB_ADD: _remove(); state = State.INDB_REMOVE; return true;
		case ADD:      _remove(); state = State.REMOVE; return true;
		// case REMOVE: return false;
		// case INDB_REMOVE: return false;
		}
		return false;
	}

	private final void _add(V value) {
		Logs.xdbParent(value, this, RECORD_VARNAME);
		Transaction.currentSavepoint().addIfAbsent(getLogKey(), new LogAddRemove());
		this.value = value;
	}

	final boolean add(V value) {
		switch (state) {
//		case INDB_GET: return false;
//		case INDB_ADD: return false;
//		case ADD:      return false;
		case INDB_REMOVE: _add(value); state = State.INDB_ADD; return true;
		case REMOVE:      _add(value); state = State.ADD; return true;
		}
		return false;
	}

	final V getValue() {
		return value;
		// value ��ɾ���Ժ�ᱻ���ó�null��ֱ�ӷ��ؼ��ɡ�
		// ������������״̬�����Ҽ��������ڵ��ԡ�
		/*
		switch (state) {
		case INDB_GET:    return value;
		case INDB_ADD:    return value;
		case ADD:         return value;
		case INDB_REMOVE: return null;
		case REMOVE:      return null;
		}
		return null;
		*/
	}

	private class LogAddRemove implements Log {
		private V saved_value;
		private State saved_state;

		LogAddRemove() {
			saved_value = value;
			saved_state = state;
		}

		@Override
		public void commit() {
			xdbLogNotify(new LogNotify(new AddRemoveInfo(false, saved_state)));
		}

		@Override
		public void rollback() {
			value = saved_value;
			state = saved_state;
		}

		@Override
		public String toString() {
			return "state=" + saved_state + " value=" + saved_value;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	// �� snapshot ֮ǰ����Щ����ֻ�� Checkpoint �߳���д��
	// �� snapshot ֮�������״̬��Ϊֻ�����Ժ��Ƕ�ȡ������
	// ����Ҫ��������
	private OctetsStream   snapshotKey   = null;
	private OctetsStream   snapshotValue = null;
	private State          snapshotState = null;

	/**
	 * ����ϵ�л��������񲢷�ִ�С�<b>��¼��.</b>
	 */
	final boolean tryMarshalN() {
		if (!lockey.tryLock())
			return false;
		try {
			marshal0();
		} finally {
			lockey.unlock();
		}
		return true;
	}

	/**
	 * ϵ�л�����������ȫ�������ɵ�������ɡ�ʹ��ʱ�����ڼ�¼������flush���ı����¡�
	 */
	final void marshal0() {
		// ʹ�ú�flushһ�µ�״̬������encode�� ������ value �Ƿ�Ϊ null ���жϡ�
		switch (state) {
		case INDB_GET: case ADD: case INDB_ADD:
			snapshotKey = this.marshalKey();
			snapshotValue = this.marshalValue();
			break;
		case INDB_REMOVE:
			snapshotKey = this.marshalKey();
			break;
		case REMOVE:
			// see TStorage.marshalN()
			break;
		}
	}

	/**
	 * ��ɼ�¼״̬���º�cache���¡�
	 * �����񻥳⡣snapshot �� flush ����ִ�С�
	 */
	final void snapshot() {
		snapshotState = this.state;
		switch (state) {
		case INDB_GET:
			// do nothing
			break;
		case INDB_REMOVE:
			getTable().getCache().remove(getKey());
			break;
		case INDB_ADD:
			setState(TRecord.State.INDB_GET);
			break;
		case ADD:
			setState(TRecord.State.INDB_GET);
			break; 
		case REMOVE:
			// see TStorage.marshalN()
			Trace.info("snapshot REMOVE record=" + this); // ��߼��𿴿�����Ƶ�ʡ�Ӧ�ú��١�
			getTable().getCache().remove(getKey());
			break;
		}
	}

	/**
	 * �����޸ĵļ�¼�����ݿ��С�����Checkpoint�߳��е��á�
	 * flush�����񲢷�ִ�У�����ֻ�����ʡ�
	 * @param storage
	 * @return false ��ʾ��ʱ��¼��REMOVE״̬������ͳ���á�
	 */
	final boolean flush(TStorage<K, V> storage) {
		switch (snapshotState) {
		case INDB_GET:
			storage.flushKeySize += snapshotKey.size();
			storage.flushValueSize += snapshotValue.size();
			storage.replaceUnsafe(snapshotKey, snapshotValue);
			break;
		case INDB_ADD:
			storage.flushKeySize += snapshotKey.size();
			storage.flushValueSize += snapshotValue.size();
			storage.replaceUnsafe(snapshotKey, snapshotValue);
			break;
		case ADD:
			storage.flushKeySize += snapshotKey.size();
			storage.flushValueSize += snapshotValue.size();
			storage.insertUnsafe(snapshotKey, snapshotValue);
			break; 
		case INDB_REMOVE:
			storage.flushKeySize += snapshotKey.size();
			storage.removeUnsafe(snapshotKey);
			break;
		case REMOVE:
			// see TStorage.marshalN()
			return false;
		}
		return true;
	}

	/**
	 * �̲߳���ȫ������ʱ������
	 */
	final void clear() {
		this.snapshotKey = null;
		this.snapshotValue = null;
		this.snapshotState = null;
	}

	//////////////////////////////////////////////
	/**
	 * ���ݼ�¼״̬�жϼ�¼�Ƿ���ڣ�<b>�Ѿ����ڼ�¼��������</b>
	 */
	final boolean exist() {
		Trace.info("TRecord.exist " + this);
		switch (snapshotState) {
		case INDB_GET: case INDB_ADD: case ADD:
			return true;
		case INDB_REMOVE: case REMOVE:
			return false;
		}
		throw new XError("invalid record state");
	}

	/**
	 * ���ݼ�¼״̬���ؽ�Ҫд��db�ļ�¼ֵ��<b>�Ѿ����ڼ�¼��������</b>
	 */
	final OctetsStream find() {
		Trace.info("TRecord.find " + this);
		switch (snapshotState) {
		case INDB_GET: case INDB_ADD: case ADD:
			return snapshotValue;
		case INDB_REMOVE: case REMOVE:
			return null;
		}
		throw new XError("invalid record state");
	}
}
