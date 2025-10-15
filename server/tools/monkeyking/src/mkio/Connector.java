package mkio;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;
import org.w3c.dom.Element;

import mkdb.Executor;

import java.net.InetSocketAddress;

/**
 * ������������
 * 
 */
public class Connector extends Creator {
	// ��ʼ���Ժ���ֻ����.
	private boolean reconnect;

	// ��Ҫ���̱߳�����
	private int connectDelay;
	private boolean open = false;

	@Override
	public void onClose(Mkio mkio, Throwable e) {
		super.getManager().removeMkio(mkio, e);
		if (reconnect && Engine.isOpen())
			_connect(true);
	}

	public synchronized int getConnectDelay() {
		return connectDelay;
	}

	private synchronized void _connect(boolean force) {
		if (force || !open) {
			open = true;
			Engine.verify();
			// executor ��Ҫ�� engine ����ǰ���г�ʼ����
			Executor.getInstance().schedule(new Connect(), getConnectDelay(), TimeUnit.SECONDS);
		}
	}

	@Override
	void open() {
		_connect(false);
	}

	////////////////////////////////////////////////////////
	// ����

	@Override
	public synchronized void close() {
		open = false;
	}

	////////////////////////////////////////////////////////
	// connect implement
	private final void doConnectSuccess(SocketChannel sc, Connect connect) {
		// reset connectDelay after a success connect
		synchronized (this) {
			connectDelay = 0;
		}
		connect.selector.execute(this, super.getManager(), sc);
		// super.getManager().addXio(mkio);
	}

	private final void doConnectAbort(Throwable e, Connect connect) {
		synchronized (this) {
			// reduce delay after a fail connect
			if (connectDelay == 0) {
				connectDelay = 1;
			} else {
				connectDelay *= 2;
				if (connectDelay > 60)
					connectDelay = 60;
			}
		}
		try {
			super.getManager().onConnectAbort(this, e);
		} catch (Throwable e_) {
			/* skip */ }
		_connect(true);
	}

	class Connect implements Handle, Runnable {
		SelectorThread selector;

		@Override
		public void doHandle(SelectionKey key) throws Exception {
			if (key.isConnectable()) {
				SocketChannel sc = (SocketChannel) key.channel();
				if (sc.finishConnect()) {
					Connector.this.doConnectSuccess(sc, this);
					return;
				}
			}
			// finishConnect ���� throw ���� true�������������Ӧ�÷�����
			throw new java.lang.IllegalStateException();
		}

		@Override
		public void doException(SelectionKey key, Throwable e) throws Throwable {
			Connector.this.doConnectAbort(e, this);
		}

		@Override
		public void run() {
			if (!Engine.isOpen())
				return;
			SocketChannel sc = null;
			try {
				sc = SocketChannel.open();

				sc.socket().setTcpNoDelay(Connector.this.isTcpNoDelay());
				sc.socket().setReceiveBufferSize(Connector.this.getReceiveBufferSize());
				sc.socket().setSendBufferSize(Connector.this.getSendBufferSize());

				sc.configureBlocking(false);
				selector = Engine.selector();
				if (sc.connect(Connector.this.getAddress())) {
					Connector.this.doConnectSuccess(sc, this);
				} else {
					selector.register(sc, SelectionKey.OP_CONNECT, this);
				}
			} catch (Throwable e) {
				if (null != sc)
					try {
						sc.close();
					} catch (Throwable e_) {
					}
				Connector.this.doConnectAbort(e, this);
			}
		}
	}

	//////////////////////////////////////////////////////////////
	protected void parse(Manager manager, org.w3c.dom.Element self) {
		super.parse(manager, self);

		String remoteIp = self.getAttribute("remoteIp");
		int remotePort = Integer.parseInt(self.getAttribute("remotePort"));
		super.initNameAndAddress(self.getAttribute("name"), new InetSocketAddress(remoteIp, remotePort));

		this.reconnect = !self.getAttribute("reconnect").equals("false"); // default
																			// true
	}

	public static Connector create(Manager manager, Element self) throws Exception {
		String clsName = self.getAttribute("class");
		Connector connector = clsName.isEmpty() ? new Connector() : (Connector) Class.forName(clsName).newInstance();
		connector.parse(manager, self);
		return connector;
	}
}
