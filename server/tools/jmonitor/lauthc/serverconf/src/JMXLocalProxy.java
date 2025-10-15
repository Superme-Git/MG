import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class JMXLocalProxy {

	// /////////////////////////////////////////////////////
	// //

	static class Netio {
		static final int BUF_SIZE = 1024 * 16;

		ByteBuffer sbuf = ByteBuffer.allocateDirect(BUF_SIZE);
		SelectionKey key;
		boolean closing = false;

		Netio to;

		Netio( ) {
		}

		void close() throws IOException {
			if (null != key) {
				key.channel().close();
				key = null;
			}
			to.closing = true;
			if (null != to.key) {
				to.key.interestOps(to.key.interestOps() | SelectionKey.OP_WRITE);
			}
		}
	}

	// /////////////////////////////////////////////////////
	// //

	static class PollTask implements Runnable, Comparable<PollTask> {
		private Selector sel;

		int size() {
			return sel.keys().size();
		}

		public int compareTo(PollTask task) {
			return size() - task.size();
		}

		PollTask() throws IOException {
			this.sel = Selector.open();
		}

		synchronized void register(SelectableChannel ch, int ops) throws ClosedChannelException {
			ch.register(sel.wakeup(), ops);
		}

		synchronized void registerAndLink(SelectableChannel channel, int ops) throws IOException {
			sel.wakeup();
			Netio local = new Netio();
			local.key = channel.register(sel, ops, local);

			Netio dest = new Netio();
			dest.to = local;
			local.to = dest;

			SocketChannel c = SocketChannel.open();
			c.configureBlocking(false);
			c.connect( JMXLocalProxy.proxyto);
			c.register(sel, SelectionKey.OP_CONNECT, dest);
		}

		private void doAccept(SelectionKey key) throws IOException {
			SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
			channel.configureBlocking(false);
			pollPolicy.addChannelAndLink(channel, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			
			System.out.println( "accept : " + channel);
		}

		private void doConnect(SelectionKey key) throws IOException {
			Netio io = (Netio) key.attachment();
			SocketChannel channel = (SocketChannel) key.channel();

			if (!channel.finishConnect())
				throw new IOException();

			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			io.key = key;
		}

		private void doRead(SelectionKey key) throws IOException {
			Netio io = (Netio) key.attachment();
			if (io.closing)
				return;

			SocketChannel channel = (SocketChannel) key.channel();
			if (channel.read(io.to.sbuf) == -1)
				throw new IOException();

			if (!io.to.sbuf.hasRemaining())
				key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
			if (io.to.sbuf.position() > 0 && null != io.to.key)
				io.to.key.interestOps(io.to.key.interestOps() | SelectionKey.OP_WRITE);
		}

		private void doWrite(SelectionKey key) throws IOException {
			Netio io = (Netio) key.attachment();
			SocketChannel channel = (SocketChannel) key.channel();

			io.sbuf.flip();
			channel.write(io.sbuf);
			io.sbuf.compact();

			if (io.closing) {
				if (io.sbuf.position() == 0)
					io.close();
				return;
			}

			if (io.sbuf.position() == 0)
				key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
			if (io.sbuf.hasRemaining() && null != io.to.key)
				io.to.key.interestOps(io.to.key.interestOps() | SelectionKey.OP_READ);
		}

		private void doClose(SelectionKey sk) {
			Netio io = (Netio) sk.attachment();
			try {
				io.close();
			} catch (IOException e) {
			}
		}

		public void run() {
			try {
				synchronized (this) {
				}
				sel.selectedKeys().clear();
				sel.select();
				for (SelectionKey key : sel.selectedKeys()) {
					try {
						if (key.isAcceptable()) {
							try {
								doAccept(key);
							} catch (Exception e) {
							}
						} else {
							if (key.isConnectable()) {
								try {
									doConnect(key);
								} catch (Exception e) {
									doClose(key);
								}
							}
							if (key.isReadable()) {
								try {
									doRead(key);
								} catch (IOException e) {
									doClose(key);
								}
							}
							if (key.isWritable()) {
								try {
									doWrite(key);
								} catch (IOException e) {
									doClose(key);
								}
							}
						}
					} catch (CancelledKeyException e) {
					}
				}

			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				pollPolicy.schedule(this);
			}
		}
	}

	// /////////////////////////////////////////////////////
	// //

	static class FixedCpuPoll {
		
		
		private final Queue<PollTask> q;
		private final ExecutorService p;

		FixedCpuPoll(int ncpu) throws IOException {
			q = new PriorityQueue<PollTask>();
			p = Executors.newFixedThreadPool(ncpu, new ThreadFactory() {
				public Thread newThread(Runnable r) {
					Thread thr = new Thread(r);
					thr.setName("PollService-" + thr.getId());
					return thr;
				}
			});

			for (int i = 0; i < ncpu; i++) {
				PollTask task = new PollTask();
				q.add(task);
				p.execute(task);
			}
		}

		void schedule(PollTask task) {
			p.execute(task);
		}

		void addChannel(SelectableChannel channel, int ops) throws IOException {
			synchronized (q) {
				PollTask poll = q.poll();
				try {
					poll.register(channel, ops);
				} finally {
					q.offer(poll);
				}
			}
		}

		void addChannelAndLink(SelectableChannel channel, int ops) throws IOException {
			synchronized (q) {
				PollTask poll = q.poll();
				try {
					poll.registerAndLink(channel, ops);
				} finally {
					q.offer(poll);
				}
			}
		}
	}

	// /////////////////////////////////////////////////////
	// //

	static FixedCpuPoll pollPolicy = null;
	static SocketAddress	proxyto; // TODO	

	static public boolean addServer( int port, final String gsip) throws IOException {
		if( null != pollPolicy)
			return false;
		pollPolicy = new FixedCpuPoll( 1);
		proxyto = new InetSocketAddress( gsip, port);
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		channel.socket().bind(new InetSocketAddress( port), 0);
		channel.socket().setReuseAddress(true);
		pollPolicy.addChannel(channel, SelectionKey.OP_ACCEPT);
		return true;
	}

	// /////////////////////////////////////////////////////
	// //
	
	static public boolean addServer( int jmxport, int zoneid, final String gsip) throws IOException {
		return addServer( jmxport + zoneid, gsip);
	}

	public static void main(String[] argv) throws IOException {
		addServer( Integer.parseInt( argv[1]), argv[0]);
	}

}
