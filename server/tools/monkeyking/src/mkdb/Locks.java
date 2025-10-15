package mkdb;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mkdb.util.WeakHashSet;

/**
 * <p>Locksԭ��ʹ�� WeakHashMap��������Ч��̫�ͣ�
 * <p>1. ÿ�β�ѯ������ͼȥ����; 2. ��������Ч�ʵ�.
 * <p>������Ŀ�ľ������Ч�ʡ�
 */
public final class Locks {
	/**
	 * The maximum number of segments to allow; used to bound constructor
	 * arguments.
	 */
	private static final int MAX_SEGMENTS = 1 << 16; // slightly conservative
	private final int segmentShift;
	private final int segmentMask;
	private Segment segments[];

	/* ---------------- hash�㷨��ӳ����������� ConcurrentHashMap. -------------- */
	/**
	 * Returns the segment that should be used for key with given hash.
	 * 
	 * @param lockey
	 *            the Lockey
	 * @return the segment
	 */
	private final Segment segmentFor(Lockey lockey) {
		/**
		 * Applies a supplemental hash function to a given hashCode, which
		 * defends against poor quality hash functions. This is critical because
		 * ConcurrentHashMap uses power-of-two length hash tables, that
		 * otherwise encounter collisions for hashCodes that do not differ in
		 * lower or upper bits.
		 */
		// Spread bits to regularize both segment and index locations,
		// using variant of single-word Wang/Jenkins hash.
		int h = lockey.hashCode();
		h += (h << 15) ^ 0xffffcd7d;
		h ^= (h >>> 10);
		h += (h << 3);
		h ^= (h >>> 6);
		h += (h << 2) + (h << 14);
		int hash = h ^ (h >>> 16);

		int index = (hash >>> segmentShift) & segmentMask;
		return segments[index];
	}

	public Locks() {
		this(1024);
	}

	public Locks(int concurrencyLevel) {
		if (concurrencyLevel <= 0)
			throw new IllegalArgumentException();

		if (concurrencyLevel > MAX_SEGMENTS)
			concurrencyLevel = MAX_SEGMENTS;

		// Find power-of-two sizes best matching arguments
		int sshift = 0;
		int ssize = 1;
		while (ssize < concurrencyLevel) {
			++sshift;
			ssize <<= 1;
		}
		this.segmentShift = 32 - sshift;
		this.segmentMask = ssize - 1;
		this.segments = new Segment[ssize];
        for (int i = 0; i < this.segments.length; ++i)
            this.segments[i] = new Segment();
	}

	/* ------------- ʵ�� ---------------*/
	static final class Segment {
		private final WeakHashSet<Lockey> locks;
		private final Lock sync = new ReentrantLock();

		Segment() {
			locks = new WeakHashSet<Lockey>();
		}

		boolean exist(Lockey key) {
			// ��Ҫsync��get�����̰߳�ȫ��
			sync.lock();
			try {
				return locks.get(key) != null;
			} finally {
				sync.unlock();
			}
		}

		Lockey get(Lockey key) {			
			sync.lock();
			try {
				Lockey weakey = locks.add(key);
				if (key == weakey)
					key.alloc(); // new key
				return weakey;
			} finally {
				sync.unlock();
			}
		}

		int size() {
			return locks.size();
		}
	}

	public boolean exist(Lockey lockey) {
		return this.segmentFor(lockey).exist(lockey);
	}

	public Lockey get(Lockey lockey) {
		return this.segmentFor(lockey).get(lockey);
	}

	/**
	 * �����������������ܰ����Ѿ���gc���յġ����ڲ���ִ�У����ֵֻ�����ڹ۲졣
	 * @return
	 */
	public int size() {
		int ssize = 0;
		for (Segment segment : this.segments)
			ssize += segment.size();
		return ssize;
	}

}
