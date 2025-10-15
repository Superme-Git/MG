package mkdb;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.Executors;

/**
 * 
 * �������񻷾������Ⱥ�ִ�д洢���̡�
 * �ڷ���XLockDead����ʱ���漴�ӳ�һ��ʱ���ٴ�ִ�д洢���̡�
 *
 */
class ProcedureFuture<P extends Procedure> implements RunnableFuture<P> {
	/**
	 * ֻ�д����̺߳͵�ǰ�����̻߳��޸ģ����Ტ���޸ġ������ж����ȡ�̡߳�
	 */
	private volatile Future<P> future;
	private final P p;
	private final Procedure.Done<P> done;
	/**
	 * ����ִ������ʱ�������̻߳��л������κ�ʱ��ֻ�����һ�������ߡ�
	 */
	private int ranTimes = 0;

	public ProcedureFuture(P p) {
		this.p = p;
		future = Mkdb.executor().getProcedureTimeoutExecutor().submit(this, p, p.getConf().getMaxExecutionTime());
		this.done = null;
	}

	public ProcedureFuture(P p, Procedure.Done<P> done) {
		this.p = p;
		this.done = done;
		future = Mkdb.executor().getProcedureTimeoutExecutor().submit(this, p, p.getConf().getMaxExecutionTime());
	}

	private void done() {
		p.fetchTasks();
		// �洢�����ڲ��ύ�������ɷ���Э���̳߳���ִ�С�
		Mkdb.executor().execute(new Runnable() {
			@Override
			public void run() {
				p.runLastTasks();
			}
		});

		if (null != done) {
			try {
				done.doDone(p);
			} catch (Throwable e) {
				// ��ʱ�����Ѿ����������Ѵ��󱨸���ȴ�future���̣߳�����¼������־��
				// ��Ȼ�첽����(execute)��ͬ������(submit)��һ����˵����ͬʱʹ�á�
				// ��ͬʱʹ��ʱ��doDone�Ĵ���ֻ����־�м�¼����Ӱ��ȴ�future���̡߳�
				// see Procedure.execute(p, done);
				Trace.error("doDone", e);
			}
		}
	}

	/**
	 * ִ�д洢���̡�����������������������ִ�С�
	 * ��ʼ���ʱ���������Ψһ��������Ƚӿڡ�ֻ��ͨ���̳� Procedure������ͨ���������ʹ������
	 * Ϊ�˷���洢���̵ĵ��ã����� Procedure.call ��Ҳ�ܸ�����Ҫ�������񣬵����߱����������������������
	 * see Procedure.call
	 * <b>���棬�����������ȫ(�̲߳���ȫ������Ҳ����ȫ)��������������á�</b>
	 * ԭ�����������Ƕ�����ڲ�Runnableʵ���У���ȫ�������ˡ�
	 * ��Ϊ���ڵ�ʵ�ַ�ʽ�Ǳ���newһ���µĶ��󣬽�Լ�ڴ棬������ܡ�
	 * ���ⲿ�õ�Future�Ժ����ͨ��ǿ��ת�������ʵ����������
	 */
	@Override
	public void run() {
		++ ranTimes;

		try {
			// ���������ִ�д洢���̡�
			try {
				Transaction.create().perform(p);
			} finally {
				Transaction.destroy(); // safe if create fail
			}
			done();

		} catch (XLockDead e) {
			if (ranTimes >= p.getConf().getRetryTimes()) {
				done();
				throw new XAngelError(); // �ﵽ����ظ�����.�������մ���.
			}
			int delay = Mkdb.random().nextInt(p.getConf().getRetryDelay());
			future = Mkdb.executor().getScheduledTimeoutExecutor().schedule(
					Executors.callable(this, p), delay, TimeUnit.MILLISECONDS,
					p.getConf().getMaxExecutionTime());
			throw e; // ������������future��ϵ�ǰ�ļ��Ӷ������¼��ӡ�

		} catch (Error error) {
			done();
			throw error;

		} catch (Throwable e) {
			done();
			throw new XError(e); // ����������,����Ҫ��װһ��,ֱ���ӳ�ȥ��?
		}
	}

	/////////////////////////////////////////////////////////////////////
	// Future implement
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	@Override
	public P get() throws InterruptedException, ExecutionException {
		for (;;) {
			try {
				future.get();
				return p;
			} catch (ExecutionException e) {
				if (!(e.getCause() instanceof XLockDead))
					throw e;
				//Trace.debug("skip " + e);
			}
		}
	}

	@Override
	public P get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		for (;;) {
			try {
				future.get(timeout, unit);
				return p;
			} catch (ExecutionException e) {
				if (!(e.getCause() instanceof XLockDead))
					throw e;
				//Trace.debug("skip " + e);
			}
		}
	}

	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return future.isDone();
	}
}
