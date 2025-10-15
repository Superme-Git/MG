package mkdb;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

/**
 * Mkdb�Ĺ���ӿڡ�
 * ������Ҫ�������ӡ�
 */
public interface MkdbMBean {

	/**
	 * ֹͣ�������������Ժ���������ᱻֹͣ��������������ء�
	 * @param iamsure ��������"iamsure"����ֹ�������
	 */
	public void shutdown(String iamsure);

	public int getAngleInterruptCount();

	public long getTransactionCount();
	public long getTransactionFalse();
	public long getTransactionException();

	/**
	 * �����Ƿ���š��������ر�ʾ���š��׳��쳣��ʾ�Ѿ���������ִ�д���
	 * @see ExecutorMBean.testAlive
	 * @param timeout ���ü�ⳬʱʱ�䣬��λ���롣С��500���룬����500������м�顣ʵ�ʼ��ʱ����ܳ������ֵ��
	 * @throws CancellationException if the computation was cancelled
	 * @throws ExecutionException if the computation threw an exception
	 * @throws InterruptedException if the current thread was interrupted while waiting
	 * @throws TimeoutException if the wait timed out
	 */
	public void testAlive(long timeout)
		throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * ʹ�� ��,�� �ָ�������ֿռ䡣��������˳��ʹ�����ֿռ䡣�磬"mkdb.;mkio;"
	 * @param nsClass
	 * @param nsLock
	 */
	public Map<String, AtomicInteger> top(String nsClass, String nsLock);
}
