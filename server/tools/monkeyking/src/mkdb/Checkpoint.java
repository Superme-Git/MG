package mkdb;

import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * <pre>
 * XDB �����������Ż�
 * 
 * ����Ŀ�ʼ
 *    �ԡ�
 *    �ܽ᣺ ������ʲô��
 *
 * �����ʵ��
 *    Table ��Ҫʵ����������������add, remove, get, ���ݲ�������Map��һ���� MapCache������XDB�Ŀ����£�
 *    ��һ���� MapData������ db.h �ı��� xDB ���ܿ��Ƶġ��Ȳ����ǰ��޸ı��浽 MspData ��ʱ��Ϊ������
 *    �����������յĽ���ǵõ�TRecord��5��״̬��
 *    ��ʱ�����е������ܹ����ˣ�ֻ�����е��޸Ķ����ᱻ���������������ݲ�����£�
 *    �ܽ᣺ TTable(add, remove, get) on TTableCache + TStorage
 *
 * ��������񲢷�
 *    ���е�������ʶ��� Tables.flushReadLock ��ִ�С������� Tables.flushWriteLock ��ִ�У�
 *    ������̰�����ϵ�л�(marshal)���޸ļ�¼״̬��Cache(snapshot)��д��(flush)��
 *    ͨ�� flushLock �ı����������濪ʼʱ��ʵ����ֹͣ�����е����񣬷�ֹ������ִ���е�����������ݱ���������
 *    ����������Ժ��ͷ�flushWriteLock���������ܿ�ʼִ�С�
 *    �����δ�Ż��汾��XDB�Ĳ�����ơ�
 *    �ܼۣ� Tables.flushWriteLock { marshal + snapshot + flush }
 *
 * ��������
 *    ���ڱ�����Ҫֹͣ�������񣬻���ɲ����ϵ�ƿ�����Ż���Ŀ�ľ����ñ���ʱ��ֹͣ�����ʱ�価���̡�
 *
 * �Ż�˼·
 *    �� marshal �� flush �����ŵ� flushLock ֮��ִ�С�
 *    ���� marshal ���ü�¼�������񲢷�ִ�У�
 *    snapshot ��Ȼ�� flushLock ֮��ִ�У�����ʣ�µĹ������٣�ִ�еĺܿ죻
 *    flush �Ͱ� snapshot �Ŀ�������д�룬�����ǲ���Ҫ���ġ����� snapshot ִ�к�������
 *    ��Ϊ���е������Ѿ�������ˣ�����ʱ��¼ʵ���ϻ�û��ˢ�µ�Storage�У���ʱ��Ҫ�޸�ԭ��ֱ��
 *    ��ѯ�ײ����ݵĽӿڣ��������ȴ����ڱ�������в�ѯ������snapshot�ͻᱻ���񲢷����ʣ���Ҫ������
 *
 * �ܽ᣺ ����ʵ�ִ� Checkpoint.checkpoint(MkdbConf ) ��ʼ��.
 * </pre>
 */
final class Checkpoint extends ThreadHelper implements CheckpointMBean {
	private Tables tables;

	// ֻ�� Checkpoint �̻߳��޸���Щֵ�� JMX��Ҫ��ȡ���ø� volatile ��ʾһ�¡�
	private volatile long marshalNCount = 0;
	private volatile long marshal0Count = 0;
	private volatile long snapshotCount = 0;
	private volatile long flushCount   = 0;
	private volatile int  checkpointCount = 0;

	private volatile long marshalNTotalTime = 0;
	private volatile long snapshotTotalTime = 0; // ���� marshal0 ��ʱ��
	private volatile long flushTotalTime = 0;
	private volatile long checkpointTotalTime = 0;

	private volatile long nextMarshalTime;
	private volatile long nextCheckpointTime;
	private volatile long nextFullBackupTime;
	private volatile long nextIncBackupTime;

	private Backup backup; // �����Ҫ�鿴����״̬��������Ҫ�̰߳�ȫ����ʱ��ʵ��������ܡ�

	private boolean fullBackupNow = false; // �Ƿ���Ҫ���Ͽ�ʼһ��ȫ���ݡ�
	private Object fullBackupWaitQueue = new Object(); // �ȴ�ȫ���ݽ�����
	private boolean checkpointNow = false; // ����ִ��һ��checkpoint
	private Object checkpointWaitQueue = new Object(); // checkpointִ�еȴ����С�

	private volatile boolean allowBackup = true;

	// Σ�ղ����������ڲ��ԡ�
	private boolean allowCheckpointXXX = true; // �Ƿ�����ִ��DB.H��checkpoint��

	@Override
	public boolean isAllowBackup() {
		return allowBackup;
	}

	@Override
	public void setAllowBackup(boolean allowBackup) {
		this.allowBackup = allowBackup;
		if (false == allowBackup)
			this.fullBackupDone(); // α�챸�ݽ��������ѱ�������
	}

	@Override
	public void fullBackup(long waitTimeout) throws InterruptedException {
		if ( false == allowBackup )
			throw new IllegalStateException("backup disabled");

		synchronized(this.fullBackupWaitQueue) {
			this.fullBackupNow = true;
			this.wakeup();
			if ( waitTimeout >= 0 )
				this.fullBackupWaitQueue.wait(waitTimeout);
		}

		if ( false == allowBackup ) // ���ﲢ�������Ƿ񱸷ݱ���ֹ��������������ɡ�
			throw new IllegalStateException("backup disabled");
	}

	private boolean fullBackupNow() {
		synchronized(this.fullBackupWaitQueue) { return this.fullBackupNow; }
	}

	private void fullBackupDone() {
		synchronized(this.fullBackupWaitQueue) {
			this.fullBackupNow = false;
			this.fullBackupWaitQueue.notifyAll();
		}
	}

	@Override
	public void checkpoint(long waitTimeout) throws InterruptedException {
		synchronized(this.checkpointWaitQueue) {
			this.checkpointNow = true;
			this.wakeup();
			if ( waitTimeout >= 0 )
				this.checkpointWaitQueue.wait(waitTimeout);
		}
	}

	private boolean checkpointNow() {
		synchronized(this.checkpointWaitQueue) { return this.checkpointNow; }
	}

	private void checkpointDone() {
		synchronized(this.checkpointWaitQueue) {
			this.checkpointNow = false;
			this.checkpointWaitQueue.notifyAll();
		}
	}

	private class Backup extends ThreadHelper {
		private boolean isFull;

		Backup(boolean isFull) {
			super("mkdb.Backup");
			this.isFull = isFull;
			super.start();
		}

		@Override
		public void run() {
			// �����κδ���ֻ��¼��־��
			try {
				if (this.isFull) {
					Checkpoint.this.tables.backupFull();
					Checkpoint.this.fullBackupDone();
				} else {
					Checkpoint.this.tables.backupInc();
				}
			} catch (Throwable ex) {
				Trace.error("backup", ex);
			}
		}
	}

	Checkpoint(Tables tables) {
		super("mkdb.Checkpoint");

		this.tables = tables;

		long now = System.currentTimeMillis();
		MkdbConf conf = Mkdb.getInstance().getConf();

		nextMarshalTime = now + conf.getMarshalPeriod();
		nextCheckpointTime = now + conf.getCheckpointPeriod();
		nextFullBackupTime = fixedRateFullTime(now, conf);
		nextIncBackupTime = fixedRateIncTime(now, conf);
		allowBackup = conf.isAllowBackup();
		allowCheckpointXXX = conf.isAllowCheckpointXXX();

		Mkdb.mbeans().register(this, "mkdb:type=Mkdb,name=Checkpoint");
	}

	private final long fixedRateFullTime(long now, MkdbConf conf) {
		int fullPeriod = conf.getBackupFullPeriod();
		return now / fullPeriod * fullPeriod + fullPeriod
				+ mkdb.Mkdb.random().nextInt(1800) * 1000;
	}

	private final long fixedRateIncTime(long now, MkdbConf conf) {
		int fullPeriod = conf.getBackupFullPeriod();
		int incPeriod = conf.getBackupIncPeriod();
		return now / fullPeriod * fullPeriod + (now % fullPeriod) / incPeriod * incPeriod + incPeriod;
	}

	private void checkpoint(final long now, MkdbConf conf) {
		try {
			if (conf.getMarshalPeriod() >= 0 && nextMarshalTime <= now && conf.isAutoMarshal()) {
				nextMarshalTime = now + conf.getMarshalPeriod();
				long start = System.nanoTime();
				int countMarshalN = 0;
				for (Storage storage : tables.getStorages())
					countMarshalN += storage.marshalN();
				this.marshalNCount += countMarshalN;
				this.marshalNTotalTime += System.nanoTime() - start;
				Trace.info("marshalN=*/" + countMarshalN);
			}

			// ����������ڽ��У���ʹ������checkpointNow��Ҳ��������ִ�У����ǵȴ���һ��ѭ���ٴγ��ԡ�
			final int checkpointPeriod = conf.getCheckpointPeriod();
			if (checkpointPeriod >= 0 && false == this.backuping()
					&& (this.checkpointNow() || nextCheckpointTime <= now)) {
				nextCheckpointTime = now + checkpointPeriod;
				checkpoint(conf);
			}

		} catch (Throwable e) {
			Trace.fatal("halt program", e);
			Runtime.getRuntime().halt(54321);
		}
	}

	private final mkdb.util.Elapse elapse = new mkdb.util.Elapse();

	private void checkpoint(MkdbConf conf) {
		Trace.info("---------------- begin ----------------");
		final List<Storage> storages = this.tables.getStorages();

		//////////////////////////////////////////
		// marshalN
		if (conf.getMarshalN() < 1)
			Trace.warn("marshalN disabled");

		elapse.reset();
		for (int i = 1; i <= conf.getMarshalN(); ++i) {
			int countMarshalN = 0;
			for (Storage storage : storages)
				countMarshalN += storage.marshalN();
			this.marshalNCount += countMarshalN;
			Trace.info("marshalN=" + i + "/" + countMarshalN);
		}
		this.marshalNTotalTime += elapse.elapsed();

		//////////////////////////////////////////
		// snapshot
		{
			int countSnapshot = 0;
			int countMarshal0 = 0;
			Lock lock = tables.flushWriteLock();
			lock.lock();
			elapse.reset();
			try {
				for (Storage storage : storages)
					countMarshal0 += storage.marshal0();
				for (Storage storage : storages)
					countSnapshot += storage.snapshot();
			} finally {
				lock.unlock();
			}

			final long snapshotTime = elapse.elapsedAndReset();
			if (snapshotTime / 100000 > conf.getSnapshotFatalTime())
				Trace.fatal("snapshot time=" + snapshotTime + " snapshot=" + countSnapshot + " marshal0=" + countMarshal0);

			this.marshal0Count += countMarshal0;
			this.snapshotTotalTime += snapshotTime;
			this.snapshotCount += countSnapshot;
			Trace.info("snapshot=" + countSnapshot + " marshal0=" + countMarshal0);
		}

		//////////////////////////////////////////
		// flush
		int countFlush = 0;
		for (Storage storage : storages)
			countFlush += storage.flush();
		this.flushCount += countFlush;
		this.flushTotalTime += elapse.elapsedAndReset();
		Trace.info("flush=" + countFlush);

		//////////////////////////////////////////
		// checkpoint
		if (countFlush > 0 && allowCheckpointXXX) {
			final Lock lock = this.tables.checkpointLock();
			lock.lock();
			try {
				this.tables.getLogger().checkpoint(storages, System.currentTimeMillis());
			} finally {
				lock.unlock();
			}
			this.checkpointTotalTime += elapse.elapsedAndReset();
			Trace.info("checkpoint");
		}
		++ checkpointCount;
		this.checkpointDone();
		Trace.info("----------------- end -----------------");
	}

	private boolean backuping() {
		if (null == backup)
			return false;
		if (backup.isAlive())
			return true;
		backup.joinAssuring();
		backup = null;
		return false;
	}

	private final void backup(long now, MkdbConf conf) {
		// ���ݹ��̴�����ֻ��¼��־��
		try {
			if (this.fullBackupNow() || nextFullBackupTime <= now) {
				nextFullBackupTime = fixedRateFullTime(now, conf);
				// ÿ�ο�ʼ�������ݣ������°���һ����������ʱ�䡣
				nextIncBackupTime = fixedRateIncTime(now, conf);
				backup = new Backup(true);
			} else if (nextIncBackupTime <= now) {
				nextIncBackupTime = fixedRateIncTime(now, conf);
				/*
				 * �����������ݿ��޷�����ֱ��ʹ�ã���Ҫrestore����ʹ�á�
				 * ����Ŀǰû��restore���ߣ�����������ܴ�û�б��ù���
				 * �����Ȳ�ʵ���������ݣ����˹����Ժ��ٿ���֧�֡�
				 */
				//backup = new Backup(false);
			}
		} catch (Throwable ex) {
			Trace.error("backup", ex);
		}
	}

	@Override
	public void run() {
		MkdbConf conf = Mkdb.getInstance().getConf();
		long backupDelayTime = System.currentTimeMillis() + conf.getBackupDelay();
		while (super.isRunning()) {
			long now = System.currentTimeMillis();
			this.checkpoint(now, conf);

			if ( allowBackup && now > backupDelayTime && !backuping() )
				this.backup(now, conf);

			super.sleepIdle(100); // ��⾫�ȣ��൱����С�����
		}

		// final checkpoint process

		// wait backup done
		try {
			if (this.backuping()) {
				Trace.fatal("wait backup done ...");
				backup.joinAssuring(); // wait backup done
				backup = null;
			}
		} catch (Throwable e) {
			Trace.error("backup", e);
		}

		// final checkpoint
		Trace.fatal("final checkpoint begin");
		checkpoint(conf);
		Trace.fatal("final checkpoint end");
	}

	// ��ص���

	@Override
	public long getCountMarshalN() {
		return marshalNCount;
	}

	@Override
	public long getCountMarshal0() {
		return marshal0Count;
	}

	@Override
	public long getCountFlush() {
		return this.flushCount;
	}

	@Override
	public long getTotalTimeCheckpoint() {
		return this.checkpointTotalTime;
	}

	@Override
	public long getTotalTimeFlush() {
		return this.flushTotalTime;
	}

	@Override
	public long getTotalTimeSnapshot() {
		return snapshotTotalTime;
	}

	@Override
	public int getCountCheckpoint() {
		return checkpointCount;
	}

	@Override
	public long getTotalTimeMarshalN() {
		return marshalNTotalTime;
	}

	public long getNextFlushTime() {
		return nextMarshalTime;
	}

	public long getNextCheckpointTime() {
		return nextCheckpointTime;
	}

	public long getNextFullBackupTime() {
		return nextFullBackupTime;
	}

	public long getNextIncBackupTime() {
		return nextIncBackupTime;
	}

	@Override
	public String getTimeOfNextCheckpoint() {
		return Trace.dateFormat.format(this.getNextCheckpointTime());
	}

	@Override
	public String getTimeOfNextFlush() {
		return Trace.dateFormat.format(this.getNextFlushTime());
	}

	@Override
	public String getTimeOfNextFullBackup() {
		return Trace.dateFormat.format(this.getNextFullBackupTime());
	}

	@Override
	public String getTimeOfNextIncBackup() {
		return Trace.dateFormat.format(this.getNextIncBackupTime());
	}

	@Override
	public int getPeriodCheckpoint() {
		return mkdb.Mkdb.getInstance().getConf().getCheckpointPeriod();
	}

	@Override
	public void setPeriodCheckpoint(int period) {
		mkdb.Mkdb.getInstance().getConf().setCheckpointPeriod(period);
	}

	@Override
	public long getCountSnapshot() {
		return this.snapshotCount;
	}
}
