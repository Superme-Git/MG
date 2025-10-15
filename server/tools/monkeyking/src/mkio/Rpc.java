package mkio;

import org.w3c.dom.Element;

import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

import mkdb.Executor;
import mkdb.Procedure;

import com.locojoy.base.Marshal.Marshal;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Զ�̵��õļ�ʵ�֡�
 * 
 * �������������ϲ����̰߳�ȫ�ģ�ԭ���ϲ�������߳�ͬʱʹ��ͬһ��ʵ���� �ڲ����ò�ͬ�Ĺ����ڴﵽ���̵߳İ�ȫ�ԡ� 1
 * �������󣺴���rpc����д��������������ֻ��һ���߳���ִ�С� 2 ���������ʱ��timeout�̣߳�����io�̣߳���ʱͨ����������ʵ���̻߳��⣺
 * timeout,io�ڶ�ͨ���̰߳�ȫ�������Ĺ������õ�ʵ����ֻ��һ���߳��ܵõ�ʵ����
 */
public abstract class Rpc<A extends Marshal, R extends Marshal> extends Protocol
		implements java.io.Closeable, mkdb.Procedure.Done<mkdb.Procedure> {

	public static interface IOnClient<A extends Marshal, R extends Marshal> {
		public void onTimeout(Rpc<A, R> rpc, int code);

		public void onClient(Rpc<A, R> rpc);
	}

	private boolean isRequest;
	private int sid;
	private A argument;
	private R result;
	private IOnClient<A, R> iOnClient;

	// ���������ģ�submit���÷�ʽ�������ڵ���ʱ�������Ĺ����У����̵߳���
	private FutureX<R> future;

	public final int getSid() {
		return sid;
	}

	public final A getArgument() {
		return argument;
	}

	public final R getResult() {
		return result;
	}

	public IOnClient<A, R> getIOnClient() {
		return iOnClient;
	}

	public final Rpc<A, R> setIOnClient(IOnClient<A, R> iOnClient) {
		this.iOnClient = iOnClient;
		return this;
	}

	public final Rpc<A, R> setArgument(A argument) {
		this.argument = argument;
		return this;
	}

	public final Rpc<A, R> setResult(R result) {
		this.result = result;
		return this;
	}

	public final boolean isRequest() {
		return isRequest;
	}

	public final Rpc<A, R> setRequest(boolean isRequest) {
		this.isRequest = isRequest;
		return this;
	}

	@Override
	public void close() {
		if (null != future)
			future.setException(new IOError("AsynchronousClose"));
		else
			_onTimeout(-1); // �첽�ر�ʱ��ʹ��onTimeout�������
	}

	private final void _onTimeout(final int code) {
		if (null != this.iOnClient)
			this.iOnClient.onTimeout(this, code);
		else
			this.onTimeout(code);
	}

	private final void _onClient() {
		if (null != this.iOnClient)
			this.iOnClient.onClient(this);
		else
			this.onClient();
	}

	//////////////////////////////////////////////////////////////
	/**
	 * �첽����ӿڣ����ʹ�� submit ��ʽ���� rpc�������������ص��� ���ͻ���ʹ���첽��ʽ����RPCʱ����Ҫʵ�֡� -1
	 * �첽�ر�(AsynchronousClose) -2 û����Ӧ��ʱ(timeout)
	 */
	protected void onTimeout(final int code) {
		// ���ͻ���ʹ���첽��ʽ����RPCʱ����Ҫʵ�֡�
	}

	protected void onServer() {
		throw new UnsupportedOperationException("onServer of " + getClass().getName());
	}

	protected void onClient() {
		throw new UnsupportedOperationException("onClient of " + getClass().getName());
	}

	public Rpc() {
	}

	/**
	 * �첽���û���ͬ������Ĭ�ϵĳ�ʱ�� ��λ���롣
	 */
	public int getTimeout() {
		return 2000;
	}

	////////////////////////////////////////////
	// ���ýӿ�
	private static class Timeout implements Runnable {
		private Manager manager;
		private int sid;

		Timeout(Manager manager, int sid) {
			this.manager = manager;
			this.sid = sid;
		}

		public void run() {
			java.io.Closeable c = manager.removeContext(sid);
			if (null != c && c instanceof Rpc<?, ?>) {
				@SuppressWarnings("rawtypes")
				Rpc rpc = (Rpc) c;
				rpc._onTimeout(-2);
			}
		}
	}

	@Override
	public boolean send(Mkio to) {
		if (null == to)
			return false;
		isRequest = true;
		Manager manager = to.getCreator().getManager();
		sid = manager.addContext(this);
		boolean rc = super.checkSend(to, new OctetsStream().marshal(this));
		if (rc && null == future) // �첽���÷�ʽ�ĳ�ʱ��顣�� submit ��ʽ��
			Executor.getInstance().schedule(new Timeout(manager, sid), getTimeout(), TimeUnit.MILLISECONDS);
		return rc;
	}

	public final FutureX<R> submit(Mkio to) {
		if (null != to) {
			future = new FutureX<R>(this.getTimeout());
			if (send(to))
				return future;
			future = null;
		}
		throw new IOError(this.toString() + to);
	}

	public final FutureX<R> submit(Manager manager) {
		return submit(manager.get());
	}

	//////////////////////////////////////////////////////////////////
	// �ڲ�ʵ��
	/**
	 * �������ݽ����ͬ���ȴ����̡߳� ͨ���̳л�ñ����ӿڵĵ���Ȩ���� �޸�Ĭ��ʵ�ֵĳ�ʱ���á�
	 * 
	 * �Ӳ�������ȥִ�����FutureTask��ʵ����
	 */
	public static class FutureX<R> extends FutureTask<R> {
		private static Runnable dummy = new Runnable() {
			public void run() {
			}
		};
		private int defaulttimeout;

		public FutureX(int timeout) {
			super(dummy, null);
			this.defaulttimeout = timeout;
		}

		@Override
		public R get() {
			return get(defaulttimeout, TimeUnit.MILLISECONDS);
		}

		@Override
		public R get(long timeout, TimeUnit unit) {
			try {
				return super.get(timeout, unit);
			} catch (Exception e) {
				throw new IOError(e);
			}
		}

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected void set(R v) {
			super.set(v);
		}

		@Override
		protected void setException(Throwable t) {
			super.setException(t);
		}
	}

	/**
	 * ���洢���̺�rpc�Ĳ������Ϳ���ƥ�䡣
	 */
	public void checkProcedure(mkdb.Procedure p) {
		@SuppressWarnings("unchecked")
		mkio.rpc.Procedure<A, R> r = (mkio.rpc.Procedure<A, R>) p; // check
																	// subclass
		r.setArgument(argument); // check argument type
		r.setResult(result); // check result type
	}

	public static class Stub extends Protocol.Stub {
		private Class<mkdb.Procedure> onServerProcedure;

		public mkdb.Procedure newProcedure() throws Exception {
			if (null != onServerProcedure)
				return onServerProcedure.newInstance();
			return null;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Stub(Element self) throws Exception {
			super(self);
			String onServer = self.getAttribute("onServer");
			if (!onServer.isEmpty()) {
				onServerProcedure = (Class<mkdb.Procedure>) Class.forName(onServer);
				((Rpc) newInstance()).checkProcedure(onServerProcedure.newInstance());
			}
		}
	}

	@Override
	public final OctetsStream marshal(OctetsStream os) {
		if (isRequest)
			return os.marshal(sid | 0x80000000).marshal(argument);
		return os.marshal(sid).marshal(result);
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream os) throws MarshalException {
		sid = os.unmarshal_int();
		isRequest = (sid & 0x80000000) != 0;
		if (isRequest) {
			sid = sid & 0x7fffffff; // clear request mask
			return argument.unmarshal(os);
		}
		return result.unmarshal(os);
	}

	@Override
	public void dispatch(Protocol.Stub _stub) throws Exception {
		Stub stub = (Stub) _stub;
		if (isRequest) {
			mkdb.Procedure p = stub.newProcedure();
			if (null != p) {
				@SuppressWarnings("unchecked")
				mkio.rpc.Procedure<A, R> rp = (mkio.rpc.Procedure<A, R>) p;
				// ���ݲ���(�������)���ø��洢���̣���������
				rp.setArgument(argument);
				rp.setResult(result);
				rp.setConnection(getConnection());
				if (mkdb.Trace.isDebugEnabled())
					mkdb.Trace.debug("mkio.Rpc.request " + this.str() + " Procedure=" + p.getClass().getName());
				mkdb.Procedure.execute(p, this);
				return;
			}
			if (mkdb.Trace.isDebugEnabled())
				mkdb.Trace.debug("mkio.Rpc.request execute " + this.str());
			getManager().execute(this);
			return;
		}
		// response
		Rpc<A, R> rpc = getConnection().getCreator().getManager().removeContext(sid, this);
		if (null == rpc) {
			mkdb.Trace.info("mkio.Rpc.response context lost! " + this.str());
			return;
		}
		rpc.setRequest(false).setResult(result);
		if (null != rpc.future) {
			if (mkdb.Trace.isDebugEnabled())
				mkdb.Trace.debug("mkio.Rpc.response with submit " + rpc.str());
			rpc.future.set(rpc.result);
			return;
		}

		if (mkdb.Trace.isDebugEnabled())
			mkdb.Trace.debug("mkio.Rpc.response execute " + this.str());
		getManager().execute(rpc);
	}

	@Override
	public final void doDone(Procedure p) {
		isRequest = false;
		checkSend(getConnection(), new OctetsStream().marshal(this));
	}

	@Override
	protected final void process() {
		if (isRequest) {
			onServer();
			isRequest = false;
			checkSend(getConnection(), new OctetsStream().marshal(this));
		} else {
			_onClient();
		}
	}

	@Override
	public String toString() {
		return "(" + argument + ", " + result + ")";
	}
}
