package mkio;

/**
 * ���������ӵ�ʵ�֡�
 *
 * �������ӽ����κι����ʺ����ڴ������ķ�������
 */
public class ManagerNone extends Manager {
	@Override
	protected void addMkio(Mkio mkio) {
	}

	@Override
	public Mkio get() {
		return null;
	}

	@Override
	protected void removeMkio(Mkio mkio, Throwable e) {
	}

	@Override
	public int size() {
		return 0;
	}
	
}
