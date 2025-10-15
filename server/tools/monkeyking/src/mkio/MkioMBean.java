package mkio;

public interface MkioMBean {
	/**
	 * @return readable interestOps. ��ע�������¼���
	 */
	public String getInterestOps();

	/**
	 * @return ���Ӵ����ߵ���Ϣ
	 */
	public String getCreatorInfo();

	/**
	 * @return Զ�����ӵ�ַ��Ϣ��
	 */
	public String getPeerInfo();

	public int getInputBufferSize();

	public int getInputBufferCapacity();

	public int getOutputBufferSize();

	public int getOutputBufferCapacity();

	/**
	 * @return �Ѿ������ buffer �ڴ棬�� capacity ����С��һ��ʱ�����ܴ���capacity��
	 */
	public int getOutputBufferAllocation();
}
