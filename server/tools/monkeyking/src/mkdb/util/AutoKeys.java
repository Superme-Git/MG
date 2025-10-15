package mkdb.util;

import java.util.*;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * ����������key��
 *
 */
public final class AutoKeys {
	private final int localInitValue; // �������ã��½�AutoKey�ĳ�ʼֵ��
	@SuppressWarnings("rawtypes")
	private HashMap<AutoKey, AutoKey> autoKeys = new HashMap<AutoKey, AutoKey>();
	private boolean dirty = false;

	/**
	 * �������ֻ�ȡ AutoKey��
	 * 
	 * [�Ż�] Ϊ�˱���ÿ�ν��в��ң������ݿ�����ʱ��������Լ���Ӧ�� AutoKey �ļ�¼�����Դ����á�
	 * [����] Ӧ�ÿ���ʹ����������ᴴ����������Key����Ҫע�⣬��ͱ�������ˡ�����ʱ���滮�����ֿռ䡣
	 *
	 * @see TTable.bindAutoKey()
	 * @param name key�����֡�
	 * @return
	 */
	public synchronized AutoKeyLong getAutoKeyLong(String name) {
		AutoKeyLong first = new AutoKeyLong(name, this.localInitValue);
		@SuppressWarnings("rawtypes")
		AutoKey found = autoKeys.get(first);
		if (null != found)
			return (AutoKeyLong)found;
		add(first);
		dirty = true;
		return first;
	}

	public synchronized boolean isDirty() {
		return this.dirty;
	}

	public synchronized Object[] toArray() {
		return autoKeys.keySet().toArray();
	}

	@Override
	public String toString() {
		Object [] keys = toArray();
		String [] strs = new String[keys.length];
		for (int i = 0; i < keys.length; ++i)
			strs[i] = keys[i].toString();
		Arrays.sort(strs);
		return Arrays.toString(strs);
	}

	////////////////////////////////////////////////////////
	// encode decode
	/**
	 * @param password ʹ��0���ɡ�
	 */
	public synchronized OctetsStream encodeValue(long password) {
		OctetsStream os = new OctetsStream();
		os.marshal(autoKeys.size());
		@SuppressWarnings("rawtypes")
		Iterator<AutoKey> it = autoKeys.keySet().iterator();
		while (it.hasNext()) {
			it.next().marshal(os);
		}
		if (password == -1922908697795782568L)
			this.dirty = false; // ���벢��ȡһ�����ݾ���������ı�ǡ��������������
		return os;
	}

	@SuppressWarnings("rawtypes")
	private void add(AutoKey a) {
		// has under lock
		if (null != autoKeys.put(a, a))
			throw new IllegalStateException("Duplicate AutoKey Found");
	}

	public AutoKeys(OctetsStream os, int localInitValue, boolean dirty) {
		this.localInitValue = localInitValue;
		this.dirty = dirty;
		if (os != null) {
			try {
				for (int size = os.unmarshal_int(); size > 0; --size) {
					switch (os.unmarshal_byte()) {
					case TYPEID_LONG: add(new AutoKeyLong(os)); break;
					}
				}
			} catch (MarshalException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	private final synchronized void setDirty() {
		this.dirty = true;
	}

	public static final byte TYPEID_LONG = 1;
	private final class AutoKeyLong implements mkdb.util.AutoKey<Long> {
		private final String name;
		private final int initValue;

		public static final int STEP = 4096;      // ������������
		public static final int STEP_MASK = 4095; // ���������롣

		private long next;

		@Override
		public void accept(Long key) {
			if ((key & STEP_MASK) != this.initValue || key <= 0)
				throw new IllegalArgumentException("invalid autokey " + String.valueOf(key));

			synchronized (this) {
				if ( key <= next )
					return;
				next = key;
			}
			AutoKeys.this.setDirty();
		}

		@Override
		public Long next() {
			mkdb.Transaction.verify(); // ����������ֻ����������ʹ�á�
			AutoKeys.this.setDirty();
			synchronized (this) {
				final long tmp = next + STEP;
				if (tmp <= 0)
					throw new IllegalStateException("autokey expired!");
				next = tmp;
				return next;
			}
		}

		@Override
		public Long current() {
			synchronized (this) { return next; }
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof AutoKeyLong) {
				AutoKeyLong o = (AutoKeyLong)obj;
				return this.initValue == o.initValue && this.name.equals(o.name);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return initValue ^ name.hashCode();
		}

		@Override
		public String toString() {
			return "(" + name + "," + initValue + "," + current() + ")";
		}

		////////////////////////////////////////////////////////
		// encode decode

		@Override
		public OctetsStream marshal(OctetsStream os) {
			os.marshal(AutoKeys.TYPEID_LONG);
			os.marshal(name, "UTF-16LE");
			os.marshal(initValue);
			os.marshal(current());
			return os;
		}

		public AutoKeyLong(OctetsStream os) throws MarshalException {
			// typeid ��AutoKeys decode��ʱ�ѱ�ȡ�ߡ�
			this.name = os.unmarshal_String("UTF-16LE");
			this.initValue = os.unmarshal_int();
			this.next = os.unmarshal_long();
		}

		public AutoKeyLong(String name, int initValue) {
			if (initValue < 0 || initValue >= STEP)
				throw new IllegalArgumentException("AutoKey Invalid initValue=" + initValue);

			this.name = name;
			this.initValue = initValue;
			next = initValue;
		}
	}
}
