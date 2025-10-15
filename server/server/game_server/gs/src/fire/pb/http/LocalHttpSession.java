/**
 * 
 */
package fire.pb.http;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import fire.log.Logger;
import fire.pb.http.handler.GMHttpExecuteHandler;
import fire.pb.http.handler.GMHttpGoldOrderUpHandler;
import fire.pb.http.handler.GMHttpSelectHandler;

/**
 * 
 * 处理http访问请求
 * 
 * @author yebin
 *
 */
public class LocalHttpSession {

	private final HttpServer httpServer;
	public static Logger logger = Logger.getLogger("SYSTEM");
	private int port;

	public LocalHttpSession(String ip, int port) throws Exception {
		httpServer = HttpServer.create(new InetSocketAddress(ip, port), 0);
		final ExecutorService executor = Executors.newCachedThreadPool();
		httpServer.setExecutor(executor);
		this.port = port;
	}

	public LocalHttpSession(int port) throws Exception {
		httpServer = HttpServer.create(new InetSocketAddress(port), 0);
		final ExecutorService executor = Executors.newCachedThreadPool();
		httpServer.setExecutor(executor);
		this.port = port;
	}

	public void start() {
		httpServer.createContext("/gm", new GMHttpExecuteHandler()); // gm调用
		httpServer.createContext("/gmgetinfo", new GMHttpSelectHandler()); // gm查询在线角色信息
		httpServer.createContext("/Joy.Market/gameapi/market.do", new GMHttpGoldOrderUpHandler()); // 金币订单上架
		httpServer.createContext("/Joy.Market/gameapi/delist.do", new GMHttpGoldOrderUpHandler()); // 金币订单下架
		httpServer.start();
		logger.info("localhttpserver start port : " + port);
	}

	public void stop() {
		httpServer.stop(0);
	}
}
