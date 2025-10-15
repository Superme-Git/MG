package mkio;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.ObjectName;

/**
 * Socket ����
 * 
 * �����¼������Ͷ�д������ buffer ͨ���ӿ� Filter �������ݼӽ��ܣ�ѹ������߼�Э��㡣
 * 
 */
public class Mkio implements Handle, java.io.Closeable, MkioMBean {
	public static final int OP_READ = SelectionKey.OP_READ;
	public static final int OP_WRITE = SelectionKey.OP_WRITE;

	private boolean open = true; // ������ֻ֤�ر�һ�Ρ�

	// �̰߳�ȫ�� �����Ա��������final���������κα�����
	private final SelectionKey key;
	private final Creator creator;
	private final SocketAddress peer; // �ر��Ժ󿴲���peer����ʼ�ͱ���һ����
	private final Input input;
	private final Output output;
	private final ObjectName objname;

	public Mkio(Creator creator, SelectorThread selector, SocketChannel sc, int ops, Input input, Output output) {
		this.creator = creator;
		final Socket so = sc.socket();
		this.peer = so.getRemoteSocketAddress();
		this.input = input;
		this.output = output;
		this.key = selector.register(sc, ops, this);

		ObjectName objname = null;
		try {
			objname = Engine.mbeans().register(this,
					"mkio:type=" + creator.getManager().getMBeanName() + ",name=" + so.getInetAddress() + "-"
							+ so.getPort() + "@" + creator.getManager().incrementAndGetNewXioCount());
		} catch (Throwable e) {
			mkdb.Trace.warn("MBean.register mkio. creator=" + this.getCreatorInfo(), e);
		}
		this.objname = objname;
	}

	public final Creator getCreator() {
		return creator;
	}

	public InetSocketAddress getPeer() {
		return (InetSocketAddress) peer;
	}

	public final Input getInput() {
		return input;
	}

	public final Output getOutput() {
		return output;
	}

	// ��Ҫ public ��
	private final SocketChannel getSocketChannel() {
		return (SocketChannel) key.channel();
	}

	private synchronized boolean getAndClearOpen() {
		// ����ȷ���Ƿ����ֱ��ʹ�� key.channel().isOpen()��
		// ����Ҳ�ɱ�����Ϲ�����
		boolean isOpen = this.open;
		this.open = false;
		return isOpen;
	}

	@Override
	public void close() {
		close(null);
	}

	public void close(Throwable ex) {
		// С���������⣬�μ� Manager.close ���� synchronized(mkios) ���� mkio.close() �ĵط���
		try {
			if (getAndClearOpen()) {
				key.channel().close();
				creator.onClose(this, ex);
				Engine.mbeans().unregister(objname);
			}
		} catch (Throwable e) {
			// skip
		}
	}

	@Override
	public String toString() {
		return new StringBuilder().append("Mkio").append(peer).append('-').append(creator).toString();
	}

	// net event setup
	public final void interestOps(int ops) {
		key.interestOps(ops);
		key.selector().wakeup();
	}

	public final void interestOps(int remove, int add) {
		int cur = key.interestOps();
		int ops = (cur & ~remove) | add;
		if (cur != ops)
			this.interestOps(ops);
	}

	// maybe override
	public boolean write(ByteBuffer b) {
		try {
			if (!key.isValid())
				return false;
			return output.write(b, this);
		} catch (IOError e) {
			if (mkdb.Trace.isDebugEnabled())
				mkdb.Trace.debug(this, e); // IOError ������־ֻ��debugʱ��¼��
		} catch (Throwable e) {
			mkdb.Trace.error(this, e);
		}
		close();
		return false;
	}

	private Lock outputQueueLock = new ReentrantLock();
	private List<ByteBuffer> outputQueue = new ArrayList<ByteBuffer>();

	/**
	 * �������ǰ�� write ������buffer���ܱ����ã����뵽��������Ժ��� Mkio ����
	 * 
	 * @param buffer
	 */
	public boolean add(ByteBuffer buffer) {
		if (key.isValid()) {
			outputQueueLock.lock();
			try {
				outputQueue.add(buffer);
				this.interestOps(0, OP_WRITE);
			} finally {
				outputQueueLock.unlock();
			}
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////
	// net event process
	@Override
	public void doHandle(SelectionKey key) throws Throwable {
		if (key.isReadable()) {
			if (-1 == input.doRead(this)) {
				close();
				return;
			}
		}

		if (key.isWritable()) {
			// 1. �����õ���ǰ������У�������������ڣ���
			// �˷�ʽ������ǳ���æ�����о����кܶ����Э��ʱ��Ч�ʽϸߡ�
			// �������æµʱ��new ArrayList �����һ���˷ѡ���Ҫ�Ľ���
			List<ByteBuffer> outputQ = null;
			outputQueueLock.lock();
			try {
				if (0 != outputQueue.size()) {
					outputQ = outputQueue;
					outputQueue = new ArrayList<ByteBuffer>();
				}
			} finally {
				outputQueueLock.unlock();
			}

			// 2. д����������У�������������⣩��
			if (null != outputQ) {
				for (ByteBuffer buffer : outputQ) {
					// ���������������ܣ���������ѹ���ȵȡ�
					// �����ܣ�ֱ��д�뵽 SocketChannel �У�see output.doNextFilter��
					if (false == output.write(buffer, this)) {
						// output ���Ĵ���������Э��������Ϣ�������Ѿ���ʧ���޷���¼��ϸ��־��
						mkdb.Trace.error("output buffer overflow. " + this);
						break;
					}
				}
			}

			// 3. ˢ��������棨������������⣩��
			int remaining = output.doWrite(this);

			// 4. ���������¼���������������ڣ���
			if (0 == remaining) {
				outputQueueLock.lock();
				try {
					if (outputQueue.isEmpty())
						this.interestOps(OP_WRITE, 0);
				} finally {
					outputQueueLock.unlock();
				}
			}
		}
	}

	@Override
	public void doException(SelectionKey key, Throwable e) throws Throwable {
		// channel�Ѿ���close�ˣ������jmx��unregister��������ڴ�й¶
		close(e);
	}

	/**
	 * ����������˳�����list�е�filter
	 */
	public static final class Input extends Filter.List implements Filter.Iterator {
		private int index;
		private ByteBuffer buffer;

		@Override
		public void doFilterNextOf(Filter f, ByteBuffer b, Mkio x) {
			if (raw().get(index) != f)
				throw new IllegalStateException();
			raw().get(++index).doFilter(this, b, x);
		}

		// public ����,���ڲ���. under lock
		public void doInput(ByteBuffer b, Mkio x) {
			index = 0;
			raw().get(index).doFilter(this, b, x); // ����������������������׳��쳣���ر����ӡ�
		}

		public int getBufferSize() {
			synchronized (this) {
				return null == buffer ? 0 : buffer.position();
			}
		}

		public int getBufferCapacity() {
			synchronized (this) {
				return null == buffer ? 0 : buffer.limit();
			}
		}

		private int doRead(Mkio x) throws java.io.IOException {
			synchronized (this) {
				if (null == buffer)
					buffer = ByteBuffer.allocate(x.creator.getInputBufferSize());
				int rc = x.getSocketChannel().read(buffer);
				if (rc > 0) {
					buffer.flip();
					doInput(buffer, x);
					buffer.compact();
					// if (0 == buffer.position() && !
					// x.creator.isKeepInputBuffer()) {
					// buffer = null; // buffer �����Ժ��ͷ������ڴ�
					// }
				}
				// �����뻺��û�пռ�ʱ���׳��쳣���ر����ӡ����뻺�����������Э����Ĵ�С��
				if (null != buffer && buffer.remaining() == 0)
					throw new IOError("OutofInputBuffer " + x.creator);
				return rc;
			}
		}
	}

	/**
	 * �������ݼ�˳�����list�е�filter
	 */
	public static final class Output extends Filter.List implements Filter.Iterator {
		private int index;
		private OutputBuffer buffer; // δ�Ż�ǰʹ�� ByteBuffer��
		private final int maxSize; // read only

		public int getBufferSize() {
			synchronized (this) {
				return null == buffer ? 0 : buffer.position();
			}
		}

		public int getBufferCapacity() {
			return maxSize; // final and read only
		}

		public int getBufferAllocation() {
			synchronized (this) {
				return null == buffer ? 0 : buffer.allocation();
			}
		}

		public Output() {
			maxSize = Helper.MAX_BUFFER_SIZE;
		}

		public Output(Creator creator) {
			maxSize = creator.getOutputBufferSize();
		}

		/**
		 * public for test
		 * <p>
		 * ʹ�� Filter ���������ݣ���д����������С�
		 * 
		 * @param b
		 * @param x
		 * @return
		 */
		public boolean write(ByteBuffer b, Mkio x) {
			synchronized (this) {
				int bufferSize = null == buffer ? 0 : buffer.position();
				if (bufferSize + b.remaining() > maxSize)
					return false;

				index = raw().size();
				doNextFilter(--index, b, x);
				return true;
			}
		}

		// under lock
		@Override
		public void doFilterNextOf(Filter f, ByteBuffer b, Mkio x) {
			if (raw().get(index) != f)
				throw new IllegalStateException();
			doNextFilter(--index, b, x);
		}

		// under lock
		private void doNextFilter(int next, ByteBuffer b, Mkio x) {
			if (next >= 0) {
				raw().get(next).doFilter(this, b, x);
				return;
			}

			// try Write Direct If buffer isEmpty
			/*
			 * if (null == buffer || buffer.position() == 0) { try {
			 * x.getSocketChannel().write(b); } catch (java.io.IOException e) {
			 * throw new IOError(e.getMessage()); } } //
			 */

			if (b.remaining() > 0) {
				// buffer = Helper.realloc(buffer, b.remaining());
				if (null == buffer)
					buffer = new OutputBuffer();
				buffer.put(b);
				x.interestOps(0, Mkio.OP_WRITE);
			}
		}

		/**
		 * �ѻ��������ˢ�µ������С�
		 * 
		 */
		private final int doWrite(Mkio mkio) throws java.io.IOException {
			synchronized (this) {
				// ���ֱ�Ӱ�����д�뵽 SocketChannel��buffer ����Ϊ null��
				// �ο�������� "try Write Direct If buffer isEmpty" �·���ע�͵��Ĵ��롣
				if (null == buffer)
					return 0;

				buffer.output(mkio.getSocketChannel());
				/*
				 * buffer.flip(); x.getSocketChannel().write(buffer);
				 * buffer.compact();
				 */
				final int remaining = buffer.size();
				if ((0 == remaining) && (false == mkio.creator.isKeepOutputBuffer())) {
					buffer = null; // �����Ժ��ͷ������ڴ�
				}
				return remaining;
			}
		}
	}

	/////////////////////////////////////////////////////////////////////
	// MBean implement

	@Override
	public String getInterestOps() {
		StringBuilder sb = new StringBuilder();
		int ops = this.key.interestOps();
		if (0 != (ops & SelectionKey.OP_ACCEPT))
			sb.append("+Accept");
		if (0 != (ops & SelectionKey.OP_CONNECT))
			sb.append("+Connect");
		if (0 != (ops & SelectionKey.OP_READ))
			sb.append("+Read");
		if (0 != (ops & SelectionKey.OP_WRITE))
			sb.append("+Write");
		return sb.toString();
	}

	@Override
	public String getCreatorInfo() {
		final StringBuilder sb = new StringBuilder();
		final Manager manager = creator.getManager();
		final MkioConf conf = manager.getConf();
		sb.append("MkioConf.").append(conf.getName());
		sb.append("/");
		sb.append(manager.getClass().getName()).append(".").append(manager.getName());
		sb.append("/");
		sb.append(creator.getClass().getName()).append(".").append(creator.getName());
		return sb.toString();
	}

	@Override
	public String getPeerInfo() {
		return peer.toString();
	}

	@Override
	public int getInputBufferSize() {
		return this.input.getBufferSize();
	}

	@Override
	public int getInputBufferCapacity() {
		return this.input.getBufferCapacity();
	}

	@Override
	public int getOutputBufferSize() {
		return this.output.getBufferSize();
	}

	@Override
	public int getOutputBufferCapacity() {
		return this.output.getBufferCapacity();
	}

	@Override
	public int getOutputBufferAllocation() {
		return this.output.getBufferAllocation();
	}
	
}
