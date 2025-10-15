package fire.pb.callback;

/**
 * @author dc
 * 仅仅用于tcp方式callback服务器测试
 */
public class Program {

	public static void main(String[] args) {
		
		new SocketServer("127.0.0.1", 8800).run();
	}
}
