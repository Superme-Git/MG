package mkio;

import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.w3c.dom.Element;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * �������ӽ�������
 * 
 */
public class Acceptor extends Creator implements Handle {
	// ���Զ��ǳ�ʼ���Ժ�ֻ���������̱߳�����
	private int backlog;
	// ���̶߳�д��Engine�̶߳���
	private volatile SelectionKey server;

	@Override
	public void onClose(Mkio mkio, Throwable e) {
		super.getManager().removeMkio(mkio, e);
	}

	@Override
	public synchronized void close() {
		try {
			if (null != server) {
				server.channel().close();
				server = null; // �������´�.
			}
		} catch (Throwable e) {
			// skip
		}
	}

	@Override
	public synchronized void open() {
		Engine.verify();
		try {
			if (null == server) {
				ServerSocketChannel ssc = ServerSocketChannel.open();
				ServerSocket ss = ssc.socket();
				ss.setReuseAddress(true);
				// xxx ֻ�����õ� ServerSocket �У��Ժ� Accept ������ͨ���̳л��Ƶõ�������á�
				ss.setReceiveBufferSize(super.getReceiveBufferSize());
				ss.bind(super.getAddress(), backlog);
				server = Engine.selector().register(ssc, SelectionKey.OP_ACCEPT, this);
			}
		} catch (Exception e) {
			throw new java.lang.IllegalStateException(e);
		}
	}

	////////////////////////////////////////////////////////////////
	// �¼�����
	@Override
	public void doHandle(SelectionKey key) throws Throwable {
		SocketChannel sc = null;
		try {
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			sc = ssc.accept();
			if (null != sc) {
				Socket so = sc.socket();
				so.setSendBufferSize(super.getSendBufferSize());
				so.setTcpNoDelay(super.isTcpNoDelay());
				sc.configureBlocking(false);
				Engine.selector().execute(this, super.getManager(), sc);
				// super.getManager().addXio(newMkio(sc));
			}
		} catch (Throwable e) {
			if (null != sc)
				sc.close();
			// skip all error
			// mkdb.Trace.debug(this, e);
		}
	}

	@Override
	public void doException(SelectionKey key, Throwable e) throws Throwable {
		mkdb.Trace.error(this.toString() + " " + key, e);
	}

	/////////////////////////////////////////////////////////////////////
	protected void parse(Manager manager, org.w3c.dom.Element self) {
		super.parse(manager, self);

		String localIp = self.getAttribute("localIp");
		int localPort = Integer.parseInt(self.getAttribute("localPort"));
		super.initNameAndAddress(self.getAttribute("name"), new InetSocketAddress(localIp, localPort));

		this.backlog = Integer.parseInt(self.getAttribute("backlog"));
	}

	public static Acceptor create(Manager manager, Element self) throws Exception {
		String clsName = self.getAttribute("class");
		Acceptor acceptor = clsName.isEmpty() ? new Acceptor() : (Acceptor) Class.forName(clsName).newInstance();
		acceptor.parse(manager, self);
		return acceptor;
	}
}
