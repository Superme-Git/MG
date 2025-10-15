package mkdb;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ExecutorMBean {
	public long getExecutorTaskCount();
	public long getExecutorCompletedTaskCount();
	public int  getExecutorActiveCount();
	public int  getExecutorPoolSize();
	public String getExecutorState();

	public long getProcedureTaskCount();
	public long getProcedureCompletedTaskCount();
	public int  getProcedureActiveCount();
	public int  getProcedurePoolSize();
	public String getProcedureState();

	public long getScheduledTaskCount();
	public long getScheduledCompletedTaskCount();
	public int  getScheduledActiveCount();
	public int  getScheduledPoolSize();
	public String getScheduledState();

	/**
	 * Tries to remove from the work queue all Future tasks that have been cancelled.
	 * see java.util.concurrent.ThreadPoolExecutor.purge
	 * @param iamsure ��������"iamsure"����ֹ�������
	 */
	public void purgeExecutor(String iamsure);

	/**
	 * Tries to remove from the work queue all Future tasks that have been cancelled.
	 * see java.util.concurrent.ThreadPoolExecutor.purge
	 * @param iamsure ��������"iamsure"����ֹ�������
	 */
	public void purgeScheduled(String iamsure);
	public void purgeProcedure(String iamsure);

	public int getExecutorCorePoolSize();
	public void setExecutorCorePoolSize(int corePoolSize);

	public int getProcedureCorePoolSize();
	public void setProcedureCorePoolSize(int corePoolSize);

	public int getScheduledCorePoolSize();
	public void setScheduledCorePoolSize(int corePoolSize);

	public long getExecutorDefaultTimeout();
	public void setExecutorDefaultTimeout(long timeout);

	public long getScheduledDefaultTimeout();
	public void setScheduledDefaultTimeout(long timeout);

	public long getProcedureDefaultTimeout();
	public void setProcedureDefaultTimeout(long timeout);

	/**
	 * Executor �Ƿ���š��������ر�ʾ���ߡ��쳣��ʾ�Ѿ���������ִ�д���
	 * @param timeout ���ü�ⳬʱʱ�䣬��λ���롣С��500���룬����500������м�顣ʵ�ʼ��ʱ����ܳ������ֵ��
	 * @throws CancellationException if the computation was cancelled
	 * @throws ExecutionException if the computation threw an exception
	 * @throws InterruptedException if the current thread was interrupted while waiting
	 * @throws TimeoutException if the wait timed out
	 */
	public void testAlive(long timeout)
		throws InterruptedException, ExecutionException, TimeoutException;
}
