package robot.protocol;

import com.locojoy.base.Marshal.*;
import com.locojoy.base.Octets;

import java.util.*;

import mkdb.util.UniqNameConf;

public final class UniqNameUtils {

	// fast reference. 免得每次使用都要去查询一次。
	private static volatile UniqNameConf conf;

	private static volatile mkio.Manager manager;

	/**
	 * 初始化唯一名服务。
	 * <p>
	 * 注册网络配置，查询并缓存网络服务引用。
	 * <p>
	 * 从MkdbConf中得到唯一名配置。
	 */
	public static void initialize() {
		initialize(mkdb.Mkdb.getInstance().getConf().getUniqNameConf());
	}

	/**
	 * 初始化唯一名服务。
	 * <p>
	 * 注册网络配置，查询并缓存网络服务引用。
	 * 
	 * @param conf 唯一名配置
	 */
	public static void initialize(UniqNameConf conf) {
		UniqNameUtils.conf = conf;
		manager = conf.getXioConf().getManager("Client");
		mkio.Engine.getInstance().register(conf.getXioConf());
	}

	public static void uninitialize() {
		conf = null;
		manager = null;
	}

	/**
	 * 名字分配。 向惟一名服务器发送名字分配协议(Allocate)，并返回结果。
	 * 如果分配成功，会记录日志，在事务成功时，自动发送确认请求(Confirm)。 如果事务失败，自动发送释放请求(Release)。
	 * 
	 * @param group
	 *           名字所在分组（即名字空间）。如："role"(角色名)，"clan"(公会名)
	 * @param name
	 *           名字。
	 * @return true 分配成功；false 分配失败；
	 */
	public static boolean allocate(final String group, final String name) {
		final GroupName a = new GroupName(group, name);
		int rc = new Allocate(a).submit(manager).get().getValue();
		if (RPC_OK == rc) {
			return true;
		}
		mkdb.Trace.warn("UniqName.Allocate " + a + " rc=" + rc);
		return false;
	}

	/**
	 * 释放名字。
	 * [注意] 这个方法会忽略所有的错误。
	 * 
	 * @param group
	 * @param name
	 */
	public static void release(final String group, final String name) {
		release(new GroupName(group, name));
	}

	/**
	 * 释放名字。
	 * [注意] 这个方法会忽略所有的错误。
	 * 
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
	 * 名字是否存在。
	 * 
	 * @return
	 *         RPC_OK name exist
	 *         RPC_NOT_EXISTS name not exist
	 *         RPC_EXCEPTION UniqName server exception
	 *         RPC_GROUP_NOT_EXISTS group not exist
	 */
	public static int exist(final String group, final String name) {
		return new Exist(group, name).submit(manager).get().getValue();
	}

	/**
	 * 分配ID。服务器会在允许的范围内，尽量按顺序分配ID。
	 * 向惟一名服务器发送ID分配请求，并返回结果。
	 * 如果分配成功，会记录日志，如果以后事务失败了，自动发送释放请求(ReleaseId)。
	 * 使用时，如果不处理错误，允许NullPointer异常，可以不用判断返回结果。
	 * 
	 * @param group
	 *           名字所在分组（即名字空间）。如："roleid"(角色名)，"clan"(公会名)
	 * @param exceptId
	 *           期望的ID。如果可用，则返回这个ID。
	 * @return
	 *         分配成功返回的ID；
	 *         null表示分配失败，失败时，内部会记录详细日志；
	 */
	public static Long allocateId(final String group, long expectId) {
		final mkio.rpc.LongInt li = new AllocateId(group, expectId).submit(manager).get();
		if (RPC_OK == li.getCode()) {
			// 只能在成功 AllocateId 以后才记录日志。
			// 所以，如果服务器已经成功，但是结果丢失(由于网络层)，那么回滚仍然无法工作。
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
	 * 分配ID。服务器会在允许的范围内，尽量按顺序分配ID。
	 * 向惟一名服务器发送ID分配请求，并返回结果。
	 * 使用时，如果不处理错误，允许NullPointer异常，可以不用判断返回结果。
	 * 
	 * @param group
	 *           名字所在分组（即名字空间）。如："roleid"(角色名)，"clan"(公会名)
	 * @return
	 *         分配成功返回的ID；
	 *         null表示分配失败，失败时，内部会记录详细日志；
	 */
	public static Long allocateId(final String group) {
		return allocateId(group, 0);
	}

	/**
	 * 释放ID。可以再次被分配。
	 * [注意] 忽略所有错误。
	 */
	public static void releaseId(final String group, final long id) {
		releaseId(new GroupId(group, id));
	}

	/**
	 * 释放ID。可以再次被分配。
	 * [注意] 忽略所有错误。
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

	// /////////////////////////////////////////////////////////////////////////////
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
			 * 考虑去掉 Confirm 这步操作。除了维护时可以得到多一点信息。没有很大作用。
			 */
			int rc = new Confirm(a).submit(manager).get().getValue();
			if (rc != RPC_OK) {
				/*
				 * 也许这里抛出异常会更好。
				 * 确认失败时，名字仍然处于分配状态，其他人不可能会得到这个名字。
				 * 此时名字状态不正确，但不会造成致命问题。
				 * 所以现在仅仅记录日志，不报告这个错误，也不中断事务的提交。
				 */
				mkdb.Trace.error("UniqName.Confirm " + a + " rc=" + rc);
			}
		}

		@Override
		public void rollback() {
			UniqNameUtils.release(a);
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
			UniqNameUtils.releaseId(g);
		}
	}

	public static final int RPC_OK = 12345; // 处理成功

	public static final int RPC_DUPLICATE = 1; // 名字重复

	public static final int RPC_NOT_EXISTS = 2; // 名字不存在

	// public static final int RPC_LOCAL_EXCEPTION = 3; // 本地异常。不捕捉本地异常，直接抛出去。
	public static final int RPC_EXCEPTION = 4; // 服务器处理异常

	public static final int RPC_GROUP_NOT_EXISTS = 5; // 名字组不存在

	public static final int RPC_CONFIRM_STATE = 6; // confirm 状态不正确。

	public static final int RPC_ALLOCID_ERROR = 7; // AllocateId 分配失败。

	// /////////////////////////////////////////////////////////////////////
	// 名字分配协议
	public static class GroupName implements Marshal {

		private String group;

		private String name;

		private int localid;

		private Octets reserve = new Octets();

		public GroupName() {
		}

		public GroupName(String group, String name) {
			this.group = group;
			this.name = name;
			this.localid = UniqNameUtils.conf.getLocalId();
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

	// ///////////////////////////////////////////////////////////
	// ID 分配协议
	public static final class GroupId implements Marshal {

		private String group;

		private long id;

		private int localid;

		private Octets reserve = new Octets();

		public GroupId() {
		}

		public GroupId(final String group, final long id) {
			this.group = group;
			this.id = id;
			this.localid = UniqNameUtils.conf.getLocalId();
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
