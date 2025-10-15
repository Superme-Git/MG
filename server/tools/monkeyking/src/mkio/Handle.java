package mkio;

import java.nio.channels.SelectionKey;

/**
 * MkIO �����¼�����ӿڡ�
 * <p>
 * ��������ӿ�û�ж���close������ doClose����ط��������ڹر�������Ҫʵ�������д��� <b>�ο���doException��˵����</b>
 */
public interface Handle {
	/**
	 * Engine ͨ����������ɷ��� Selector �õ����¼���
	 * 
	 * @param key
	 * @throws Throwable
	 */
	public void doHandle(SelectionKey key) throws Throwable;

	/**
	 * Engine �ڵ��� doHandle ʱ�������׽���쳣���͵�����������������
	 * <p>
	 * <b>ע�⣬�ڵ���֮ǰ����· channel �Ѿ����رա�</b>���˵�ʱΪʲô��ôʵ���ˣ�
	 * ���ܵ�ԭ���������ڲ��ѹؼ���Դ(channel)��ǰ�ͷŵ�����ֹ�����Ĵ�������©�� ���ǣ��� doHandle
	 * ����Ӧ�������ر�����ʱ����Ҫ�Լ���ȷ�Ĺر�channel�� Ҳ����˵��doException �� doHandle ���� channel
	 * ���ͷŹ��򲻴�һ�¡�
	 * <p>
	 * ���飬����doHandle��doException�����������رգ���������һ��closeʵ�֡� ��closeʵ������ȷ�ر�
	 * channel.close��
	 * 
	 * @param key
	 * @param e
	 * @throws Throwable
	 */
	public void doException(SelectionKey key, Throwable e) throws Throwable;
}
