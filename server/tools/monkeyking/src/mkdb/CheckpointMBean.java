package mkdb;

public interface CheckpointMBean {

	public int getCountCheckpoint();
	public long getCountMarshalN();
	public long getCountMarshal0();
	public long getCountFlush();
	public long getCountSnapshot();

	// checkpoint ���ʱ��ͳ�ƣ���λ����
	public long getTotalTimeMarshalN();
	public long getTotalTimeSnapshot();
	public long getTotalTimeFlush();
	public long getTotalTimeCheckpoint();

	// ʱ��
	public String getTimeOfNextFlush();
	public String getTimeOfNextCheckpoint();
	public String getTimeOfNextFullBackup();
	public String getTimeOfNextIncBackup();

	// method
	/**
	 * ���Ͻ���һ��ȫ���ݡ�
	 *
	 * ����� backupDelay �ڼ䣬�򲻻����Ͽ�ʼ������ȴ���Delay������backupDelay ���ȼ����������ߡ�
	 * ������ڽ���ȫ���ݣ��򲻻������µ�ȫ���ݡ������Ҫ�ȴ�ȫ���ݽ�������ȴ���ǰ���ڽ��е�ȫ���ݡ�
	 *    ���fullBackup���󣬿���ֻ������һ��ȫ���ݡ�
	 *
	 * @param waitTimeout ==0 �ȴ����ݽ�����>0�����ȴ���ô��ʱ�䣻<0���ȴ���
	 * @throws InterruptedException
	 */
	public void fullBackup(long waitTimeout) throws InterruptedException;

	/**
	 * ����ִ��checkpoint������ 
	 * ͬʱ����������ֻ��ִ��һ�β�������������Ժ�ȫ���ȴ��̶߳��ᱻ���ѡ�
	 * ������ڽ��б��ݣ������������Ͽ�ʼ�������ڱ��ݽ�����ſ�ʼ��
	 * 
	 * @param waitTimeout waitTimeout ==0 �ȴ�����������>0�����ȴ���ô��ʱ�䣻<0���ȴ���
	 * @throws InterruptedException
	 */
	public void checkpoint(long waitTimeout) throws InterruptedException;

	/**
	 * �Ƿ�������
	 * @return
	 */
	public boolean isAllowBackup();

	/**
	 * ���ñ��ݿ��ء�
	 * @param allow
	 */
	public void setAllowBackup(boolean allow);

	public int getPeriodCheckpoint();

	public void setPeriodCheckpoint(int period);
}
