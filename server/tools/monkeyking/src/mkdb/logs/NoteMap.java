package mkdb.logs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class NoteMap<K, V> extends Note {
	private final Set<K>    added     = new HashSet<K>();
	private final Map<K, V> removed   = new HashMap<K, V>();
	private final Map<K, V> replaced  = new HashMap<K, V>();
	private List<V> changed; // setup by ListenableMap

	final void setChanged(List<V> changed) {
		this.changed = changed;
	}

	/**
	 * �ϲ���ʱ�򣬰Ѻ�������־����һ�Ρ�����added,replaced����logPut�Ľ����
	 * @param note
	 */
	final void merge(Note note) {
		@SuppressWarnings("unchecked")
		NoteMap<K, V> another = (NoteMap<K, V>)note;
		// �ϲ�ʱҪ�� note ��ʱ��˳��ͨ�档 see Transaction._real_commit_ �ύ����㡣
		for (K k : another.added)
			this.logPut(k, null, null);
		for (Map.Entry<K, V> e : another.removed.entrySet())
			this.logRemove(e.getKey(), e.getValue());
		for (Map.Entry<K, V> e : another.replaced.entrySet())
			this.logPut(e.getKey(), e.getValue(), e.getValue());
	}

	public final Set<K> getAdded() {
		return added;
	}

	/**
	 * ��put�����滻���ļ�ֵ������ֵ(value)Ϊ�滻ǰ�ġ�
	 * @return
	 */
	public final Map<K, V> getReplaced() {
		return replaced;
	}

	/**
	 * ��remove����ɾ���ļ�ֵ������ֵ(key, value)Ϊɾ��ǰ�ġ�
	 * @return
	 */
	public final Map<K, V> getRemoved() {
		return removed;
	}

	/**
	 * ͨ��get���ص����ã�ֱ���޸�ֵ(value)����־��
	 * ���ܰ����ѱ�ɾ�����ļ�ֵ��
	 * @return
	 */
	public final List<V> getChanged() {
		return this.changed;
	}

	// map ����ĸı䣬������ List<V> changed��
	final boolean isMapChanged() {
		return (!added.isEmpty()) || (!removed.isEmpty()) || (!replaced.isEmpty());
	}

	final void clear() {
		this.added.clear();
		this.removed.clear();
		this.replaced.clear();
		this.changed = null;
	}

	final void logRemove(K key, V value ) {
		if (!this.added.remove(key)) {
			// ����Ǹ��ǵģ���ʱv��������ģ�����value��replaceʱ�ġ�
			V v = this.replaced.remove(key);
			this.removed.put(key, v == null ? value : v);
		} // else ɾ��������¼��ɾ����־���ɡ�
	}

	final void logPut(K key, V origin, V value) {
		if (this.added.contains(key))
			return; // ����������Ŀ����Ȼ������������־��

		// �ж��Ƿ����ɾ������
		V v = this.removed.remove(key);
		if (null != v) {
			// ɾ���Ժ��ٴμ��룬������һ����ɾ����������־���ݣ���־ת�Ƶ�������־�С�
			// ��ʱ��Ŀ���ڡ��������ٴμ����value����ǰɾ����value�Ƿ�һ����
			this.replaced.put(key, v);
			return;
		}
		if (this.replaced.containsKey(key))
			return; // �ٴθ�����Ŀ��������һ�ε���־��

		// ��һ�θ��ǡ���ʱ����origin����added����replaced��־��
		if (null == origin)
			this.added.add(key);
		else
			this.replaced.put(key, origin);
	}

	@Override
	public String toString() {
		return "added=" + added + " replaced=" + replaced + " removed=" + removed + " changed=" + changed;
	}
}
