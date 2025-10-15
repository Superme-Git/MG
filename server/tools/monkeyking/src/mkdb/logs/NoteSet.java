package mkdb.logs;

import java.util.HashSet;
import java.util.Set;

public class NoteSet<E extends Object> extends Note {
	private final Set<E> added = new HashSet<E>();
	private final Set<E> removed = new HashSet<E>();
	private final Set<E> old = new HashSet<E>(); // ����ʼʱ�Ѿ����ڵ����һ�α�ɾ��ʱ��¼��

	/**
	 * �ϲ���ʱ�򣬰Ѻ�������־����һ�Ρ�
	 * @param note
	 */
	final void merge(Note note) {
		@SuppressWarnings("unchecked")
		NoteSet<E> another = (NoteSet<E>)note;
		// �ϲ�ʱҪ�� note ��ʱ��˳��ͨ�档 see Transaction._real_commit_ �ύ����㡣
		for (E e : another.added)   this.logAdd(e);
		for (E e : another.removed) this.logRemove(e);
	}

	final void logAdd(E e) {
		if (false == this.removed.remove(e)) {
			this.added.add(e);
		}
	}

	final void logRemove(E e) {
		if (false == this.added.remove(e)) {
			this.removed.add(e);
			// һ������ɾ����Ҫ��¼�����Զ��ԭʼ���ݣ����ڻع�
			if (false == this.old.contains(e))
				this.old.add(e);
		}
	}

	public final Set<E> getAdded() {
		return added;
	}

	public final Set<E> getRemoved() {
		return removed;
	}
	
	public final Set<E> getOld() {
		return old;
	}

	public final boolean isSetChanged() {
		return (false == added.isEmpty()) || (false == removed.isEmpty());
	}

	public final void clear() {
		added.clear();
		removed.clear();
	}

	@Override
	public String toString() {
		return "added=" + added + " removed=" + removed;
	}
}

