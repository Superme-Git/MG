package fire.pb.http.handler;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import fire.log.Logger;
import net.sf.json.JSONObject;

/**
 * GM查询在线角色信息 author yebin @ 2016年1月19日
 */
public class GMHttpGoldOrderUpHandler implements HttpHandler {
	public static Logger logger = Logger.getLogger("GMHTTP");

	@Override
	public void handle(HttpExchange exchange) {
		try {
			JSONObject jsonObj = new JSONObject();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			final OutputStream os = exchange.getResponseBody();
			jsonObj.accumulate("code", 1);
			jsonObj.accumulate("message", "测试");
			os.write(jsonObj.toString().getBytes(Charset.forName("utf-8")));
		} catch (Exception e) {
			logger.error("GM_HttpToolHandler exception=", e);
		} finally {
			exchange.close();
		}
	}
}
