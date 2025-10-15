package mkdb;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public final class Lockey implements java.lang.Comparable<Lockey> {
	private final String name;
	private final int id; // ����ţ�����Ψһ��
	private final Object key;
	private final int hashcode; // �Ż�
	private ReentrantLock lock;	

	/**
	 * �����¼����
	 * @param name
	 * @param id
	 * @param key
	 */
	Lockey(String name, int id, Object key) {
		this.name = name;
		this.id = id;
		this.key = key;

		int h = key.hashCode();
		this.hashcode = id ^ h ^ (h >> 12); // ��������localidӰ��
	}

	final void alloc() {
		if (null != lock)
			throw new XError("mkdb : xlock alloc");
		lock = new ReentrantLock();		
	}

//	public final boolean isLocked() {
//		return lock.isLocked();
//	}

	public final boolean isHeldByCurrentThread() {
		return lock.isHeldByCurrentThread();
	}

	public final String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public final Object getKey() {
		return key;
	}
	
	public final void unlock() {
		lock.unlock();
	}

	public final boolean tryLock() {
		return lock.tryLock();
	}

	public final boolean tryLock(long milliseconds) {
		return tryLock(milliseconds, TimeUnit.MILLISECONDS);
	}

	/**
	 * ���ɴ�ϵ�������
	 */
	final void _lock() {
		lock.lock();
	}

	public final void lock() {
		try {
			lock.lockInterruptibly();
		} catch (InterruptedException ex) {
			if (Worker.angelInterrupted())
				throw new XLockDead();
			throw new XLockInterrupted(this.toString());
		}
	}

	public final boolean tryLock(long time, TimeUnit unit) {
		try {
			return lock.tryLock(time, unit);
		} catch (InterruptedException ex){
			// tryLock �϶����������������Ҫȷ��findDeadlockedThreads����жϵġ�
			// �����Ȳ�����������
			throw new XLockInterrupted(this.toString());
		}
	}

	@Override
	public int compareTo(Lockey arg0) {
		if (id < arg0.id) return -1;
		if (id > arg0.id) return 1;

		// �����һ���������Ƚ�key
		@SuppressWarnings("unchecked")
		Comparable<Object> k1 = (Comparable<Object>)key;
		return k1.compareTo(arg0.key);
	}

	@Override
	public int hashCode() {
		return hashcode; // name.hashCode() ^ key.hashCode() ^ (key.hashCode() >> 12);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Lockey) {
			Lockey o = (Lockey)obj;
			return this.id == o.id && key.equals(o.key);
		}
		return false;
	}

	@Override
	public String toString() {
		return key.toString() + "@" + name;
	}
}
