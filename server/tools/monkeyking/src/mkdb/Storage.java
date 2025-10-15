package mkdb;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * �洢��ӿ�
 * 
 * <ul>
 * <li> �޸Ĳ�������������ѯ����������
 * <li>exist,find,walk <b>����</b>
 * <li>firstKey, nextKey <b>����</b>
 * <li>_walk, _find <b>����</b>
 * <li>replaceUnsafe,insertUnsafe,removeUnsafe <b>����</b>
 * <li>replace, insert, remove <b>����</b>
 * <li>_replace, _insert <b>����</b>
 * <li>browse <b>���������ܺ�checkpoint����</b>
 * </ul>
 * 
 * <p> Storageʵ���ϲ���Ҫ�� abstract����������һЩ������ʵ�� mkdb ʱ���������ء�
 * �� Storage �����޸ĺ͵���ʱ����Щ�����ᱻ����Ϊ abstract�����ڷ����Ƿ�������©����
 *
 * @see ȫ����������docs/lock.mht
 * @see share/commom/db.h
 *
 */
public abstract class Storage {
	private final Lock lock = new ReentrantLock();
	private final File file;
	private final Logger logger;

	public final void lock() {
		lock.lock();
	}

	public final void lockInterruptibly() {
		try {
			lock.lockInterruptibly();
		} catch (InterruptedException ex) {
			throw new XLockInterrupted("Storage:" + file.toString());
		}
	}

	public final void unlock() {
		lock.unlock();
	}

	///////////////////////////////////////////////////////////////
	// checkpoint: marshalN -> lock(w){ marshal0 + snapshot } -> flush -> checkpoint
	int marshalN() { return 0; }
	int marshal0() { return 0; }
	int snapshot() { return 0; }
	int flush()    { return 0; }
	/*
	�޸ĵ�ʱ��ʹ�� abstract�������©��
	abstract int marshalN();
	abstract int marshal0();
	abstract int snapshot();
	abstract int flush();
	*/
	/**
	 * ���Ժ󣬲��ܸ������á�
	 */
	public Storage(Logger logger, MkdbConf xconf, TableConf tconf) {
		this(logger, xconf.getTableHome(), tconf.getName(), tconf.getCacheHigh(), tconf.getCacheLow());
	}

	public Storage(Logger logger, File tableHome, String tableName, int cacheHigh, int cacheLow) {
		// ��ס���ݱ��ļ�����ֹ���������ñ��޸ġ�
		file = new File(tableHome, tableName); // table.getName());

		handle = open(logger.getHandle(), file.toString(), cacheHigh, cacheLow);
		if (0 == handle)
			throw new XError("Storage open faild : " + file);
		this.logger = logger;
	}

	public final Logger getLogger() {
		return logger;
	}

	//////////////////////////////////////////////////////////////////
	// ���ݴ�ȡ�ӿڡ�
	/**
	 * ����key�Ƿ���ڡ�<b>����Locked</b>��
	 */
	public final boolean exist(OctetsStream key) {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");

			return exist(handle, key.array(), key.size());
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ���Ҳ����ؼ�¼������������򷵻�null��<b>����Locked</b>��
	 */
	public final OctetsStream find(OctetsStream key) {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");

			byte [] bytes = find(handle, key.array(), key.size());
			if (null == bytes)
				return null;

			return OctetsStream.wrap(Octets.wrap(bytes, bytes.length));

		} finally {
			lock.unlock();
		}
	}

	/**
	 * �����¼����������򸲸ǣ��������������ӡ�<b>����Locked</b>��
	 * @throws ͨ���쳣�������
	 */
	public final void replaceUnsafe(OctetsStream key, OctetsStream value) {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");

			if (false == replace(handle, key.array(), key.size(), value.array(), value.size()))
				throw new XError("replace faild");
		} finally {
			lock.unlock();
		}
	}

	/**
	 * �����¼�������¼�Ѿ��������׳��쳣��<b>����Locked</b>��
	 * @throws ͨ���쳣�������
	 */
	public final void insertUnsafe(OctetsStream key, OctetsStream value) {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");

			if (false == insert(handle, key.array(), key.size(), value.array(), value.size()))
				throw new XError("insert faild");
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ɾ����¼�������¼���������׳��쳣��<b>����Locked</b>��
	 * @throws ͨ���쳣�������
	 */
	public final void removeUnsafe(OctetsStream key) {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");

			if (false == remove(handle, key.array(), key.size()))
				throw new XError("remove faild");
		} finally {
			lock.unlock();
		}
	}

	/**
	 * �����¼����������򸲸ǣ��������������ӡ�<b>����Not Locked</b>��
	 * @return ͨ������ֵ�������
	 */
	public boolean replace(OctetsStream key, OctetsStream value) {
		if (0 == handle)
			throw new XError("storage has closed");

		return replace(handle, key.array(), key.size(), value.array(), value.size());
	}

	/**
	 * �����¼�������¼�Ѿ����ڷ���false��<b>����Not Locked</b>��
	 * @return ͨ������ֵ�������
	 */
	public boolean insert(OctetsStream key, OctetsStream value) {
		if (0 == handle)
			throw new XError("storage has closed");

		return insert(handle, key.array(), key.size(), value.array(), value.size());
	}

	/**
	 * ɾ����¼�������¼���������׳��쳣��<b>����Not Locked</b>��
	 * @return ͨ������ֵ�������
	 */
	public boolean remove(OctetsStream key) {
		if (0 == handle)
			throw new XError("storage has closed");

		return remove(handle, key.array(), key.size());
	}

	/**
	 * ͨ������������<b>����Locked</b>��
	 */
	public void walk(IWalk iw) {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");
			walk(handle, iw);
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * ֱ�ӱ��������ļ�����Ҫ��checkpoint���⡣<b>����Not Lock</b>��
	 */
	public void browse(IWalk ib, int cachesize) {
		if (0 == handle)
			throw new XError("storage has closed");
		browse(handle, ib, cachesize);
	}

	public static interface IWalk {
		/**
		 * ��¼�ص��ӿڡ�
		 * @param key
		 * @param data
		 * @return true = continue walk; false = break walk;
		 */
		public boolean onRecord(byte[] key, byte[] data);
	}

	/**
	 * ͨ������������<b>����Locked</b>�����ݱ���ѹ��״̬��
	 */
	public void _walk(IWalk iw) {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");
			_walk(handle, iw);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ֱ�ӱ��������ļ���<b>����Not Locked</b>�����ݱ���ѹ��״̬��
	 */
	public void _browse(IWalk iw, int cachesize) {
		if (0 == handle)
			throw new XError("storage has closed");
		_browse(handle, iw, cachesize);
	}

	/**
	 * ���Ҳ����ؼ�¼������������򷵻�null��<b>����Locked</b>�����ݱ���ѹ��״̬��
	 */
	public final OctetsStream _find(OctetsStream key) {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");

			byte [] bytes = _find(handle, key.array(), key.size());
			if (null == bytes)
				return null;

			return OctetsStream.wrap(Octets.wrap(bytes, bytes.length));

		} finally {
			lock.unlock();
		}
	}

	/**
	 * �����¼����������򸲸ǣ��������������ӡ�<b>����Not Locked</b>��
		 * <p><b>value ������ѹ�����������</b>
	 * @return ͨ������ֵ�������
	 */
	public boolean _replace(OctetsStream key, OctetsStream value) {
		if (0 == handle)
			throw new XError("storage has closed");

		return _replace(handle, key.array(), key.size(), value.array(), value.size());
	}

	/**
	 * �����¼�������¼�Ѿ������򷵻�false��<b>����Not Locked</b>��
		 * <p><b>value ������ѹ�����������</b>
	 * @return ͨ������ֵ�������
	 */
	public boolean _insert(OctetsStream key, OctetsStream value) {
		if (0 == handle)
			throw new XError("storage has closed");

		return _insert(handle, key.array(), key.size(), value.array(), value.size());
	}

	/**
	 * ѹ�����̰߳�ȫ��
	 */
	public final static OctetsStream _compress(OctetsStream data) {
		byte [] result = _compress(data.array(), data.size());
		return OctetsStream.wrap(Octets.wrap(result, result.length));
	}

	/**
	 * ��ѹ�����̰߳�ȫ��
	 */
	public final static OctetsStream _uncompress(OctetsStream data) {
		byte [] result = _uncompress(data.array(), data.size());
		return OctetsStream.wrap(Octets.wrap(result, result.length));
	}

	/**
	 * ���ر�ĵ�һ��key��<b>����Locked</b>��
	 * @return null if storage is empty.
	 */
	public final OctetsStream firstKey() {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");

			byte [] result = firstKey(handle);
			if (null == result)
				return null;
			return OctetsStream.wrap(Octets.wrap(result, result.length));
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ����ָ��key��������һ��key������˳�򣩡�<b>����Locked</b>��
	 * @param key
	 * @return null if EOF.
	 */
	public final OctetsStream nextKey(OctetsStream key) {
		lock.lock();
		try {
			if (0 == handle)
				throw new XError("storage has closed");

			byte [] result = nextKey(handle, key.array(), key.size());
			if (null == result)
				return null;
			return OctetsStream.wrap(Octets.wrap(result, result.length));
		} finally {
			lock.unlock();
		}
	}

	//////////////////////////////////////////////////////////////////////////
	// native implement
	private long handle;

	private native void walk(long handle, IWalk iw);
	private native void browse(long handle, IWalk iw, int cachesize);
	private native boolean exist(long handle, byte [] key, int keysize);
	private native byte[] find(long handle, byte [] key, int keysize);
	private native boolean replace(long handle, byte [] key, int keysize, byte [] value, int valuesize);
	private native boolean insert(long handle, byte [] key, int keysize, byte [] value, int valuesize);
	private native boolean remove(long handle, byte [] key, int keysize);

	private native long open(long logger, String file, int cachehight, int cachelow);
	private native void close(long handle);

	// checkpoint support
	private native void snapshot_create(long handle);
	private native void snapshot_release(long handle);

	// raw
	private native void _walk(long handle, IWalk iw);
	private native void _browse(long handle, IWalk iw, int cachesize);
	private native byte[] _find(long handle, byte [] key, int keysize);
	private native boolean _replace(long handle, byte [] key, int keysize, byte [] value, int valuesize);
	private native boolean _insert(long handle, byte [] key, int keysize, byte [] value, int valuesize);

	private native byte[] firstKey(long handle);
	private native byte[] nextKey(long handle, byte[] key, int keysize);

	public static native byte[] _compress(byte[] data, int datasize);
	public static native byte[] _uncompress(byte[] data, int datasize);

	/**
	 * ����ļ���Ϣ��
	 * @return
	 */
	public final File getFile() {
		return file;
	}

	/**
	 * �رձ��Ժ����еķ��ʶ���ʧ�ܡ�
	 */
	public void close() {
		if (0 == handle)
			throw new XError("storage has closed");
		close(handle);
		handle = 0;
	}

	@Override
	protected void finalize() throws Throwable {
		if (0 != handle)
			close(handle);
		super.finalize();
	}

	/**
	 * �������ա�
	 */
	public final void snapshot_create() {
		if (0 == handle)
			throw new XError("storage has closed");
		snapshot_create(handle);
	}

	/**
	 * �ͷſ��ա�
	 */
	public final void snapshot_release() {
		if (0 == handle)
			throw new XError("storage has closed");
		snapshot_release(handle);
	}

	private class KeyIterator implements java.util.Iterator<OctetsStream> {
		private OctetsStream cur = null;
		private OctetsStream next;

		KeyIterator() {
			next = Storage.this.firstKey();
		}

		@Override
		public boolean hasNext() {
			return null != next;
		}

		@Override
		public OctetsStream next() {
			if (null == cur)
				throw new IllegalStateException();
			cur = next;
			next = Storage.this.nextKey(cur);
			return cur;
		}

		@Override
		public void remove() {
			if (null == cur)
				throw new IllegalStateException();
			Storage.this.remove(cur); // skip error
			cur = null;
		}
	}

	/**
	 * ��װ firstKey, nextKey
	 * ��˼�������þ��á�
	 */
	public final java.util.Iterator<OctetsStream> iterator() {
		return new KeyIterator();
	}
}
