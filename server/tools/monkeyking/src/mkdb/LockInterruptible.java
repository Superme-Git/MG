package mkdb;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/**
 * ReentrantLock �İ�װ������ lock, tryLock ��ʵ�֣������´���
 * <ol>
 * <li>lock �����ĳ�ʹ�� lockInterruptibly������ Angel �����ж�����
 * <li>���� InterruptedException�� ���� ProcedureFuture ��������
 * <li>����ʹ�ã����ô���������� InterruptedException��
 * </ol>
 * 
 * <pre>
 * 1 synchronized ͬ����
 * synchronized(object) {
 *     // ... critical section
 * }
 * �滻��
 * lock.lock();
 * try {
 *     // ... critical section
 * } finally {
 *     lock.unlock();
 * }
 * ��� critical section �ڰ����� try ��䣬��Ҫ��ͼ������� try...finally �ϲ���
 * ����ԭ����
 * 
 * 2 condition(synchronized and Object.wait and Object.notify) ͬ��������
 * ʹ�� Condition �� await �� signal ����� Object �� wait �� notify��
 * ͨ�� LockInterruptible.newCondition �������ο� Condition, Lock��
 * </pre>
 */
public final class LockInterruptible extends ReentrantLock {
	private static final long serialVersionUID = -3127211387464574647L;

	public LockInterruptible() {
	}

	public LockInterruptible(boolean fair) {
		super(fair);
	}

	@Override
	public void lock() {
		try {
			super.lockInterruptibly();
		} catch (InterruptedException ex) {
			if (mkdb.Worker.angelInterrupted())
				throw new mkdb.XLockDead();
			throw new XLockInterrupted("lock");
		}
	}

	@Override
	public void lockInterruptibly() {
		try {
			super.lockInterruptibly();
		} catch (InterruptedException ex) {
			if (mkdb.Worker.angelInterrupted())
				throw new mkdb.XLockDead();
			throw new XLockInterrupted("lockInterruptibly");
		}
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) {
		try {
			return super.tryLock(time, unit);
		} catch (InterruptedException ex){
			// tryLock �϶����������������Ҫȷ�� findDeadlockedThreads ����жϵġ�
			// �����Ȳ����������� see mkdb.Lockey.tryLock��
			throw new XLockInterrupted("tryLock");
		}
	}
}
