package mkdb.util;

import com.locojoy.base.Marshal.*;
import com.locojoy.base.Octets;
import java.util.*;

public final class UniqName {
	// fast reference. ���ÿ��ʹ�ö�Ҫȥ��ѯһ�Ρ�
	private static volatile UniqNameConf conf;
	private static volatile mkio.Manager manager;

	/**
	 * ��ʼ��Ψһ������
	 * 
	 * <p>ע���������ã���ѯ����������������á�
	 * <p>��MkdbConf�еõ�Ψһ�����á�
	 */
	public static void initialize() {
		initialize(mkdb.Mkdb.getInstance().getConf().getUniqNameConf());
	}

	/**
	 * ��ʼ��Ψһ������
	 * 
	 * <p>ע���������ã���ѯ����������������á�
	 * @param conf Ψһ������
	 */
	public static void initialize(UniqNameConf conf) {
		UniqName.conf = conf;
		manager = conf.getXioConf().getManager("Client");
		mkio.Engine.getInstance().register(conf.getXioConf());
	}

	public static void uninitialize() {
		conf = null;
		manager = null;
	}

	/**
	 * ���ַ��䡣 ��Ωһ���������������ַ���Э��(Allocate)�������ؽ����
	 * 
	 * �������ɹ������¼��־��������ɹ�ʱ���Զ�����ȷ������(Confirm)�� �������ʧ�ܣ��Զ������ͷ�����(Release)��
	 * 
	 * @param group
	 *            �������ڷ��飨�����ֿռ䣩���磺"role"(��ɫ��)��"clan"(������)
	 * @param name
	 *            ���֡�
	 * @return true ����ɹ���false ����ʧ�ܣ�
	 */
	public static boolean allocate(final String group, final String name) {
		final GroupName a = new GroupName(group, name);
		int rc = new Allocate(a).submit(manager).get().getValue();
		if (RPC_OK == rc) {
			// ֻ���ڳɹ� Allocate �Ժ�ż�¼��־��
			// ���ԣ�����������Ѿ��ɹ������ǽ����ʧ(���������)����ô�ع���Ȼ�޷�������
			// ��Ҫ�����ֶΡ��������ַ�������ʱ������Ϸ����������ȷ��δ��ȷ�ϵ����֡�
			mkdb.Savepoint sp = mkdb.Transaction.currentSavepoint();
			sp.addIfAbsent(new mkdb.LogKey(getVirtualBean(group), name), new NameLog(a));
			return true;
		}
		mkdb.Trace.warn("UniqName.Allocate " + a + " rc=" + rc);
		return false;
	}

	/**
	 * �ͷ����֡�
	 * 
	 * [ע��] ���������������еĴ���
	 * 
	 * @param group
	 * @param name
	 */
	public static void release(final String group, final String name) {
		release(new GroupName(group, name));
	}

	/**
	 * �ͷ����֡�
	 * 
	 * [ע��] ���������������еĴ���
	 * @param a
	 */
	public static void release(final GroupName a) {
		try {
			int rc = new Release(a).submit(manager).get().getValue();
			if (rc != RPC_OK)
				mkdb.Trace.error("UniqName.Release " + a + " rc=" + rc);
		} catch (Throwable e) {
			// skip all rollback error.
			mkdb.Trace.error("UniqName.Release " + a, e);
		}
	}

	/**
	 * �����Ƿ���ڡ�
	 * @return
	 *       RPC_OK               name exist
	 *       RPC_NOT_EXISTS       name not exist
     *       RPC_EXCEPTION        UniqName server exception
     *       RPC_GROUP_NOT_EXISTS group not exist
	 */
	public static int exist(final String group, final String name) {
		return new Exist(group, name).submit(manager).get().getValue();
	}

    /**
	 * ����ID����������������ķ�Χ�ڣ�������˳�����ID��
	 * 
	 * ��Ωһ������������ID�������󣬲����ؽ����
	 * �������ɹ������¼��־������Ժ�����ʧ���ˣ��Զ������ͷ�����(ReleaseId)��
	 * ʹ��ʱ������������������NullPointer�쳣�����Բ����жϷ��ؽ����
	 * @param group
	 *            �������ڷ��飨�����ֿռ䣩���磺"roleid"(��ɫ��)��"clan"(������)
	 * @param exceptId
	 *            ������ID��������ã��򷵻����ID��
	 * @return
	 *            ����ɹ����ص�ID��
	 *            null��ʾ����ʧ�ܣ�ʧ��ʱ���ڲ����¼��ϸ��־��
	 * 
	 */
	public static Long allocateId(final String group, long expectId) {
		final mkio.rpc.LongInt li = new AllocateId(group, expectId).submit(manager).get();
		if (RPC_OK == li.getCode()) {
			// ֻ���ڳɹ� AllocateId �Ժ�ż�¼��־��
			// ���ԣ�����������Ѿ��ɹ������ǽ����ʧ(���������)����ô�ع���Ȼ�޷�������
			mkdb.Savepoint sp = mkdb.Transaction.currentSavepoint();
			sp.addIfAbsent(new mkdb.LogKey(getVirtualBean(group), String.valueOf(li.getValue())),
					new IdLog(new GroupId(group, li.getValue())));
			return li.getValue();
		}
		if (mkdb.Trace.isInfoEnabled())
			mkdb.Trace.info("UniqName.AllocateId(" + group + "," + expectId + ") rc=" + li.getCode());
		return null;
	}

	/**
	 * ����ID����������������ķ�Χ�ڣ�������˳�����ID��
	 * ��Ωһ������������ID�������󣬲����ؽ����
	 * ʹ��ʱ������������������NullPointer�쳣�����Բ����жϷ��ؽ����
	 * @param group
	 *            �������ڷ��飨�����ֿռ䣩���磺"roleid"(��ɫ��)��"clan"(������)
	 * @return
	 *            ����ɹ����ص�ID��
	 *            null��ʾ����ʧ�ܣ�ʧ��ʱ���ڲ����¼��ϸ��־��
	 */
	public static Long allocateId(final String group) {
		return allocateId(group, 0);
	}

    /**
	 * �ͷ�ID�������ٴα����䡣
	 * 
	 * [ע��] �������д���
	 */
	public static void releaseId(final String group, final long id) {
		releaseId(new GroupId(group, id));
	}
    /**
	 * �ͷ�ID�������ٴα����䡣
	 * 
	 * [ע��] �������д���
	 */
	public static void releaseId(final GroupId g) {
		try {
			int rc = new ReleaseId(g).submit(manager).get().getValue();
			if (rc != RPC_OK)
				mkdb.Trace.error("UniqName.ReleaseId " + g + " rc=" + rc);
		} catch (Throwable e) {
			// skip all rollback error.
			mkdb.Trace.error("UniqName.ReleaseId " + g, e);
		}
    }

    ///////////////////////////////////////////////////////////////////////////////
	private static Map<String, mkdb.XBean> virtual = new HashMap<String, mkdb.XBean>();
	
	private static mkdb.XBean getVirtualBean(final String group) {
		synchronized (virtual) {
			mkdb.XBean b = virtual.get(group);
			if (null == b)
				virtual.put(group, b = new mkdb.XBean(null, null));
			return b;
		}
	}

	private static class NameLog implements mkdb.Log {
		private final GroupName a;

		NameLog(GroupName a) {
			this.a = a;
		}
		@Override
		public void commit() {
			/*
			 * ����ȥ�� Confirm �ⲽ����������ά��ʱ���Եõ���һ����Ϣ��û�кܴ����á�
			 */
			int rc = new Confirm(a).submit(manager).get().getValue();
			if (rc != RPC_OK) {
				/*
				 * Ҳ�������׳��쳣����á�
				 * 
				 * ȷ��ʧ��ʱ��������Ȼ���ڷ���״̬�������˲����ܻ�õ�������֡�
				 * ��ʱ����״̬����ȷ������������������⡣
				 * �������ڽ�����¼��־���������������Ҳ���ж�������ύ��
				*/
				mkdb.Trace.error("UniqName.Confirm " + a + " rc=" + rc);
			}
		}

		@Override
		public void rollback() {
			UniqName.release(a);
		}
	}

	private static class IdLog implements mkdb.Log {
		private final GroupId g;

		IdLog(GroupId g) {
			this.g = g;
		}
		@Override
		public void commit() {
		}

		@Override
		public void rollback() {
			UniqName.releaseId(g);
		}
	}

	public static final int RPC_OK = 12345;           // ����ɹ�
	public static final int RPC_DUPLICATE = 1;        // �����ظ�
	public static final int RPC_NOT_EXISTS = 2;       // ���ֲ�����
	//public static final int RPC_LOCAL_EXCEPTION = 3;  // �����쳣������׽�����쳣��ֱ���׳�ȥ��
	public static final int RPC_EXCEPTION = 4;        // �����������쳣
	public static final int RPC_GROUP_NOT_EXISTS = 5; // �����鲻����
	public static final int RPC_CONFIRM_STATE = 6;    // confirm ״̬����ȷ��

	public static final int RPC_ALLOCID_ERROR = 7;    // AllocateId ����ʧ�ܡ�

	///////////////////////////////////////////////////////////////////////
	// ���ַ���Э��
	public static class GroupName implements Marshal {
		private String group;
		private String name;
		private int    localid;
		private Octets reserve = new Octets();

		public GroupName() {
		}

		public GroupName(String group, String name) {
			this.group = group;
			this.name = name;
			this.localid = UniqName.conf.getLocalId();
		}

		public String getGroup() {
			return group;
		}

		public String getName() {
			return name;
		}

		public int getLocalid() {
			return localid;
		}

		@Override
		public OctetsStream marshal(OctetsStream os) {
			os.marshal(group, "UTF-16LE");
			os.marshal(name, "UTF-16LE");
			os.marshal(localid);
			os.marshal(reserve);
			return os;
		}
		@Override
		public OctetsStream unmarshal(OctetsStream os) throws MarshalException {
			group = os.unmarshal_String("UTF-16LE");
			name = os.unmarshal_String("UTF-16LE");
			localid = os.unmarshal_int();
			reserve = os.unmarshal_Octets();
			return os;
		}

		@Override
		public String toString() {
			return "(" + group + ":" + name + ":" + localid + ")";
		}
	}

	public static final class Exist extends mkio.Rpc<GroupName, mkio.rpc.Int> {
		public Exist() {
			setArgument(new GroupName()).setResult(new mkio.rpc.Int());
		}
		public Exist(final GroupName a) {
			setArgument(a).setResult(new mkio.rpc.Int());
		}
		public Exist(final String group, final String name) {
			setArgument(new GroupName(group, name)).setResult(new mkio.rpc.Int());
		}
		@Override
		public int getType() {
			return 1;
		}
	}

	public static final class Allocate extends mkio.Rpc<GroupName, mkio.rpc.Int> {
		public Allocate() {
			setArgument(new GroupName()).setResult(new mkio.rpc.Int());
		}
		public Allocate(final GroupName a) {
			setArgument(a).setResult(new mkio.rpc.Int());
		}
		public Allocate(final String group, final String name) {
			setArgument(new GroupName(group, name)).setResult(new mkio.rpc.Int());
		}
		@Override
		public int getType() {
			return 2;
		}
	}

	public static final class Confirm extends mkio.Rpc<GroupName, mkio.rpc.Int> {
		public Confirm() {
			setArgument(new GroupName()).setResult(new mkio.rpc.Int());
		}
		public Confirm(final GroupName a) {
			setArgument(a).setResult(new mkio.rpc.Int());
		}
		public Confirm(final String group, final String name) {
			setArgument(new GroupName(group, name)).setResult(new mkio.rpc.Int());
		}
		@Override
		public int getType() {
			return 3;
		}
	}

	public static final class Release extends mkio.Rpc<GroupName, mkio.rpc.Int> {
		public Release() {
			setArgument(new GroupName()).setResult(new mkio.rpc.Int());
		}
		public Release(final GroupName a) {
			setArgument(a).setResult(new mkio.rpc.Int());
		}
		public Release(final String group, final String name) {
			setArgument(new GroupName(group, name)).setResult(new mkio.rpc.Int());
		}
		@Override
		public int getType() {
			return 4;
		}
	}

	/////////////////////////////////////////////////////////////
	// ID ����Э��
	public static final class GroupId implements Marshal {
		private String group;
		private long   id;
		private int    localid;
		private Octets reserve = new Octets();

		public GroupId() {
		}

		public GroupId(final String group, final long id) {
			this.group = group;
			this.id = id;
			this.localid = UniqName.conf.getLocalId();
		}

		public String getGroup() {
			return group;
		}

		public long getId() {
			return this.id;
		}

		public int getLocalid() {
			return localid;
		}

		@Override
		public OctetsStream marshal(OctetsStream os) {
			os.marshal(group, "UTF-16LE");
			os.marshal(id);
			os.marshal(localid);
			os.marshal(reserve);
			return os;
		}
		@Override
		public OctetsStream unmarshal(OctetsStream os) throws MarshalException {
			group = os.unmarshal_String("UTF-16LE");
			id = os.unmarshal_long();
			localid = os.unmarshal_int();
			reserve = os.unmarshal_Octets();
			return os;
		}

		@Override
		public String toString() {
			return "(" + group + ":" + id + ":" + localid + ")";
		}
	}

	public static final class AllocateId extends mkio.Rpc<GroupId, mkio.rpc.LongInt> {
		public AllocateId() {
			setArgument(new GroupId()).setResult(new mkio.rpc.LongInt());
		}
		public AllocateId(final String group, final long expectId) {
			setArgument(new GroupId(group, expectId)).setResult(new mkio.rpc.LongInt());
		}
		public AllocateId(final GroupId g) {
			setArgument(g).setResult(new mkio.rpc.LongInt());
		}
		@Override
		public int getType() {
			return 10;
		}
	}

	public static final class ReleaseId extends mkio.Rpc<GroupId, mkio.rpc.Int> {
		public ReleaseId() {
			setArgument(new GroupId()).setResult(new mkio.rpc.Int());
		}
		public ReleaseId(final String group, final long id) {
			setArgument(new GroupId(group, id)).setResult(new mkio.rpc.Int());
		}
		public ReleaseId(final GroupId g) {
			setArgument(g).setResult(new mkio.rpc.Int());
		}
		@Override
		public int getType() {
			return 11;
		}
	}
}