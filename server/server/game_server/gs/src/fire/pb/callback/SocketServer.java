package fire.pb.callback;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * @author dc
 * 监听TCP方式的充值回调
 */
public class SocketServer implements Runnable {
	public static final Logger logger = Logger.getLogger("RECHARGE");
	
	private String strIp;

	private int iPort;

	private Selector sSelector;

	private ByteBuffer inBuf = ByteBuffer.allocate(1024);

	public SocketServer(String strIp, int iPort) {
		this.strIp = strIp;
		this.iPort = iPort;

		try {
			sSelector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.socket().bind(new InetSocketAddress(strIp, iPort));
			ssc.register(sSelector, SelectionKey.OP_ACCEPT);
			logger.info("充值Callback Server listen at " + strIp + ":" + iPort);

			while (true) {
				sSelector.select();
				Iterator<SelectionKey> it = sSelector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					switch (key.readyOps()) {
					case SelectionKey.OP_ACCEPT:
						onAccept(key);
						break;
					case SelectionKey.OP_CONNECT:
						break;
					case SelectionKey.OP_READ:
						onRead(key);
						break;
					case SelectionKey.OP_WRITE:
						break;
					}
					it.remove();
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void onAccept(SelectionKey key) {
		try {
			ServerSocketChannel server = (ServerSocketChannel) key.channel();
			SocketChannel sc = server.accept();
			sc.configureBlocking(false);
			sc.register(sSelector, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void onRead(SelectionKey key) {
		SocketChannel sc = (SocketChannel) key.channel();
		inBuf.clear();

		try {
			int iReaded = sc.read(inBuf);
			//System.out.println("Read " + iReaded + " bytes");
			if (iReaded < 0) {
				sc.close();
				return;
			}
		} catch (IOException e) {
			key.cancel();
			try {
				sc.close();
			} catch (IOException ex) {
			}
			return;
		}
		inBuf.flip();
		Message revMsg = new Message(inBuf);
		MessageHandler.handleMessage(revMsg, sc);
	}
}
