package mkio;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;

/**
 * ���ר�û���
 *
 * <p>
 * ʹ�� ByteBuffer �����������Ĳ���ϵ�У�flip, write, compact�� ���� compact
 * ������Ҫ��δд��������ƶ�����ͷ��������Ƚϴ��ʱ��Ӱ��Ч�ʡ�
 * 
 * <p>
 * �����ʵ��������������������������������������ڴ濽���������������£�
 * <ol>
 * <li>put �������ڻ������������
 * <li>output ������������д����������.
 * <li>get �������ӻ�������ȡ����
 * </ol>
 */
public final class OutputBuffer {
	private int size = 0; // number of bytes in this buffer
	private final int piece; // size of piece

	// ready for get. no compact.
	private ArrayDeque<ByteBuffer> outputs = new ArrayDeque<ByteBuffer>();
	// ready for put. have some data.
	private ByteBuffer tail = null;
	// ready for put. is empty.
	private ArrayDeque<ByteBuffer> buffers = new ArrayDeque<ByteBuffer>();

	public OutputBuffer() {
		this(524288); // default piece = 512k
	}

	public OutputBuffer(int piece) {
		this.piece = mkio.Helper.roudup(piece);
	}

	/**
	 * This method transfers the bytes remaining in the given source buffer into
	 * this buffer.
	 * 
	 * @see java.nio.ByteBuffer
	 * @param src
	 *            ByteBuffer. MUST NOT BE DIRECT
	 * @return this
	 */
	public final OutputBuffer put(ByteBuffer src) {
		this.size += src.remaining();
		while (src.remaining() > 0) {
			// allocate
			if (null == tail) {
				tail = buffers.pollFirst();
				if (null == tail)
					tail = ByteBuffer.allocateDirect(piece);
			}
			// check size and put
			int len = tail.remaining() > src.remaining() ? src.remaining() : tail.remaining();
			if (tail.put((ByteBuffer) src.slice().limit(len)).remaining() == 0) {
				tail.flip(); // ready for output
				outputs.addLast(tail);
				tail = null;
			}
			src.position(src.position() + len); // fetch position
		}
		return this;
	}

	public final OutputBuffer put(byte[] src) {
		return put(ByteBuffer.wrap(src, 0, src.length));
	}

	public final OutputBuffer put(byte[] src, int offset, int length) {
		return put(ByteBuffer.wrap(src, offset, length));
	}

	/**
	 * This method transfers bytes from this buffer into the given destination
	 * channel.
	 * 
	 * @param channel
	 * @return
	 * @throws IOException
	 */
	public final OutputBuffer output(SocketChannel channel) throws IOException {
		if (null != tail) {
			/*
			 * ��ʱ tail ����û�����������¼��ִ���ʽ�� 1 ���� output ���� tail �� outputs
			 * �����Ѿ����㹻�����ݿ����ʱ���Ȳ�����tail�� ���ַ�ʽ��Ҫ����������ݲ����㹻�ġ��Ƚ��鷳��
			 *
			 * 2 �����ָ�tail �� tail ���뵽 outputs �У� write������� channel �С� tail =
			 * pollLast().compact(); �ָ�tail��
			 *
			 * 3 ����tail �������ָ�tail���ο�2��Ŀǰ�������������
			 */
			tail.flip(); // ready for output
			outputs.addLast(tail);
			tail = null;
		}

		// ����ÿ����Ƭ�൱���㹻���ˣ������￴����û��Ҫ���� toArray��
		// ��ôд���Դ��������������
		// 1. ��һ����Ƭʣ�����ݺ���ʱ��һ�ε��þͰѺ��������һ��д��ȥ��
		// 2. ���� tail �Ĵ���ԭ�򣬷�Ƭ��û��װ���ġ�
		// �ܵ���˵���ʵ������ͦ�࣬�޸ĳ�ʹ��һ��ѭ����һ������Ƭ��д������tail�Ĵ�����

		this.size -= channel.write(outputs.toArray(new ByteBuffer[outputs.size()]));
		// collect empty piece
		while (!outputs.isEmpty()) {
			if (outputs.peekFirst().remaining() > 0)
				break;
			buffers.addLast((ByteBuffer) outputs.pollFirst().clear());
		}
		return this;
	}

	/**
	 * This method transfers bytes from this buffer into the given destination
	 * array.
	 * 
	 * @throws BufferUnderflowException
	 *             If there are fewer than <tt>length</tt> bytes remaining in
	 *             this buffer
	 */
	public final OutputBuffer get(byte[] dst, int offset, int length) {
		Helper.checkBounds(offset, length, dst.length);
		if (this.size < length)
			throw new BufferUnderflowException();

		this.size -= length;
		while (length > 0) {
			ByteBuffer first = outputs.peekFirst();
			if (null == first)
				break;
			int r = first.remaining();
			if (r > length)
				r = length;
			if (first.get(dst, offset, r).remaining() == 0)
				// outputs.removeFirst();
				buffers.add((ByteBuffer) outputs.pollFirst().flip());
			offset += r;
			length -= r;
		}
		if (length > 0) {
			// ��ͷ�Ѿ�������С��������û��ȡ������ζ�Ż���һЩ�� tail �У���ʱ tail �϶����� null��
			tail.flip();
			tail.get(dst, offset, length);
			tail.compact();
		}
		return this;
	}

	public final OutputBuffer get(byte[] dst) {
		return get(dst, 0, dst.length);
	}

	/**
	 * @return The number of bytes in this buffer
	 */
	public final int size() {
		return this.size;
	}

	/**
	 * same as size()
	 * 
	 * @return The number of bytes in this buffer
	 */
	public final int position() {
		return this.size;
	}

	/**
	 * @return size of piece
	 */
	public final int piece() {
		return this.piece;
	}

	/**
	 * @return ��ǰ�ѷ�����ڴ�������
	 */
	public final int allocation() {
		int n = outputs.size() + buffers.size();
		if (null != tail)
			++n;
		return n * piece;
	}
}
