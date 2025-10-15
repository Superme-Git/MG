package mkio;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public final class Helper {
	public static final int MAX_BUFFER_SIZE = 0x40000000; // 1G

	/**
	 * @param size
	 * @throws IllegalArgumentException
	 *             if size < 0 || size >= 0x40000000
	 * @return ���� 2^n ���Ҳ�С��size
	 */
	public static int roudup(int size) {
		if (size < 0 || size > MAX_BUFFER_SIZE)
			throw new IllegalArgumentException("mkio.Helper.roundup size=" + size);
		int capacity = 16;
		while (size > capacity)
			capacity <<= 1;
		return capacity;
	}

	/**
	 * ���·����ڴ档 �� buffer ��ʣ��ռ䲻��incrementʱ����չbuffer�ڴ档 �����ڴ��ʱ�� roundup ��ʽ������
	 * 
	 * @param buffer
	 *            ��ǰbuffer������Ϊnull��
	 * @param increment
	 *            ��Ҫ���ӵĿռ䡣
	 * @return ʣ��ռ��㹻��buffer��
	 */
	public static ByteBuffer realloc(ByteBuffer buffer, int increment) {
		if (null == buffer) {
			return ByteBuffer.allocate(roudup(increment));
		}

		if (buffer.remaining() < increment) {
			buffer.flip(); // prepare to read
			return ByteBuffer.allocate(roudup(buffer.limit() + increment)).put(buffer);
		}

		return buffer;
	}

	/**
	 * �������ʹ���Ƿ�Խ�硣
	 * 
	 * ������������ʵ�����棺 get(byte [] dst, int offset, int length)��
	 *
	 * jdk��ʵ�ֶ��д˼�飬������ǵ�ʵ��ֻ�ǵ���jdk���Ͳ����ر��顣 ֻ��������Ҫ������дdst��Ϊ�˱���ǰ�����д�ɹ�����������дʧ�ܣ�
	 * ����ڿ�ʼ��дdstʹ���������֮ǰ��ǰ���߽硣
	 * 
	 * ��Ҫʱ�ٿ���������������ڽ����ڰ��ڡ�
	 * 
	 * @param off
	 * @param len
	 * @param size
	 */
	static void checkBounds(int off, int len, int size) {
		if ((off | len | (off + len) | (size - (off + len))) < 0)
			throw new IndexOutOfBoundsException();
	}

	/**
	 * ��������ʽ����� IpAddress �� port ת���� InetSocketAddress��
	 */
	public static java.net.InetSocketAddress inetSocketAddress(int address, int port) {
		return new java.net.InetSocketAddress(inetAddress(address), port);
	}

	/**
	 * �� InetAddress ת�������� int �С�
	 * 
	 * @throws IOError
	 *             if addr is not a ip4 address
	 */
	public static int ip4(java.net.InetAddress addr) {
		int ip = 0;
		byte[] addrs = addr.getAddress();
		if (addrs.length != 4)
			throw new IOError(addr + " is not a ip4 address");
		ip |= (addrs[3] << 24) & 0xff000000;
		ip |= (addrs[2] << 16) & 0x00ff0000;
		ip |= (addrs[1] << 8) & 0x0000ff00;
		ip |= (addrs[0]) & 0x000000ff;
		return ip;
	}

	/**
	 * ��������ʽ����� IpAddress ת���� InetAddress��
	 */
	public static java.net.InetAddress inetAddress(int address) {
		try {
			byte[] addr = new byte[4];
			addr[0] = (byte) ((address >>> 24) & 0xFF);
			addr[1] = (byte) ((address >>> 16) & 0xFF);
			addr[2] = (byte) ((address >>> 8) & 0xFF);
			addr[3] = (byte) (address & 0xFF);
			return java.net.InetAddress.getByAddress(addr);
		} catch (UnknownHostException e) {
			throw new IOError(e);
		}
	}

}
