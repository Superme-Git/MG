package mkio;

import java.nio.channels.*;
import java.net.InetSocketAddress;

/**
 * �����ߡ���ȡ�͹���������á�
 */
public abstract class Creator {
	// ���Զ��ǳ�ʼ���Ժ�ֻ���������̱߳�����
	private Manager manager;
	private int inputBufferSize;
	private int outputBufferSize;
	private boolean keepOutputBuffer;
	private boolean keepInputBuffer;

	private boolean tcpNoDelay; // setup after accept
	private int receiveBufferSize; // ������ServerSocket�ϣ�accept����������ͨ���̳л�á�
	private int sendBufferSize; // setup after accept

	private String name = ""; // ugly. �������г�ʼ����
	private InetSocketAddress address; // ugly. �������г�ʼ����

	/**
	 * �������ã����Ҽ�ס������ manager�� �������ؽ����Լ�����չ���ҵ���������������������á�
	 * 
	 * @param manager
	 * @param self
	 */
	protected void parse(Manager manager, org.w3c.dom.Element self) {
		this.manager = manager;

		this.inputBufferSize = Helper.roudup(Integer.parseInt(self.getAttribute("inputBufferSize")));
		this.keepInputBuffer = !self.getAttribute("keepInputBuffer").equals("false");
		this.outputBufferSize = Helper.roudup(Integer.parseInt(self.getAttribute("outputBufferSize")));
		this.keepOutputBuffer = !self.getAttribute("keepOutputBuffer").equals("false"); // default
																						// true

		this.sendBufferSize = Integer.parseInt(self.getAttribute("sendBufferSize"));
		this.receiveBufferSize = Integer.parseInt(self.getAttribute("receiveBufferSize"));
		this.tcpNoDelay = self.getAttribute("tcpNoDelay").equals("true");
	}

	public final int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public final int getSendBufferSize() {
		return sendBufferSize;
	}

	public final boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public final int getOutputBufferSize() {
		return outputBufferSize;
	}

	public final int getInputBufferSize() {
		return inputBufferSize;
	}

	/**
	 * �Ƿ񱣳�InputBuffer�� false=�ͷ��ڴ棬���bufferΪ��; true=�����ͷ��ڴ档
	 */
	public final boolean isKeepInputBuffer() {
		return keepInputBuffer;
	}

	/**
	 * �Ƿ񱣳�OutputBuffer�� false=�ͷ��ڴ棬���bufferΪ��; true=�����ͷ��ڴ档
	 */
	public final boolean isKeepOutputBuffer() {
		return this.keepOutputBuffer;
	}

	public final Manager getManager() {
		return manager;
	}

	//////////////////////////////////////////////////////////////////////
	// ���½ӿ�Ŀǰ�������Ÿ��ⲿ������Ҫ��չ�Լ���Creatorʵ���ٿ��ų�ȥ��
	// ��Ϊopen close��ʹ�ÿ��ܻ���Ҫ������Ŀǰ�������������̰߳�ȫ���涨�岻����ȷ��
	/**
	 * ������Ӵ���������ί��Manager������Ӵ������ο� Manager.newMkio��
	 */
	Mkio newMkio(SelectorThread selector, SocketChannel sc) {
		return manager.newMkio(this, selector, sc);
	}

	/**
	 * �����Ӵ��������ɵ����ӹر�ʱ�ص�����ӿڽ��д����ο� Manager.removeXio��
	 * 
	 * @param mkio
	 *            �رյ����ӡ�
	 * @param e
	 *            �رյ�ԭ�򣬿���Ϊnull��
	 */
	abstract void onClose(Mkio mkio, Throwable e);

	/**
	 * �����Ӵ�������Engine����ʱ���á�
	 */
	abstract void open();

	/**
	 * �ر����Ӵ�������Engine�ر�ʱ���á�
	 */
	abstract void close();

	public final String getName() {
		return name;
	}

	/**
	 * remote or local���ο� Acceptor, Connector����ª�ġ�
	 */
	public final InetSocketAddress getAddress() {
		return address;
	}

	/**
	 * ��ʼ�����ֺ������ַ��ֻ����������װ������ʱ���á�
	 * 
	 * @param address
	 */
	protected final void initNameAndAddress(String name, InetSocketAddress address) {
		this.name = name;
		this.address = address;
		if (this.name.isEmpty())
			this.name = this.address.toString();
	}

	@Override
	public String toString() {
		return this.getAddress() + "@" + this.getManager().getName(); // +
																		// local;
	}

}
