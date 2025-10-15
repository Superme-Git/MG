package locojoy.game.mt3.sdk.ifly;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import locojoy.game.mt3.utils.Conf;
import locojoy.game.mt3.utils.HttpPackage;

public class Trans {

	private static Logger LOG = Logger.getLogger("TRANS");

	/**
	 * 将语音转换为文本输出，音频数据存在于消息体中。
	 *
	 * @param msg 终端传入的消息协议
	 * @return 识别的文本内容
	 */
	public static String speechToWordage(JSONObject msg) {
		String token = Token.getInstance().getToken();
		LOG.debug("token=" + token);
		if (token == null) {
			LOG.error("TOKEN 为空~!!!!!");
			return "";
		}
		StringBuilder url = new StringBuilder();
		url.append(Conf.getIflyUrl()).append("?svc=iat&token=").append(token).append("&auf=").append(msg.getString("auf")).append("&aue=")
				.append(msg.getString("aue")).append("&ent=").append(msg.getString("ent"));
		Map<String, String> header = new HashMap<String, String>();
		header.put("X-Par", token);
		header.put("Content-Type", "binary");
		LOG.debug("URL=" + url);
		String resultb64 = postMethod(url.toString(), header, msg.getString("speech"));
		String result = new String(Base64.decodeBase64(resultb64), Charset.forName("utf-8"));
		try {
			JSONObject json = new JSONObject(result);
			if (LOG.isDebugEnabled()) {
				LOG.debug(json.toString(4));
			}
			result = json.getString("result");
		} catch (JSONException e) {
			LOG.error("Trans.speechToWordage", e);
		}
		return result;
	}
	
	public static String postMethod(String url, Map<String, String> header, String body) {
		LOG.debug("发送http请求到讯飞");
		String result = "";
		PrintWriter out = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(40000);
			for (String key : header.keySet()) {
				conn.setRequestProperty(key, header.get(key));
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(body);
			out.flush();
			result = HttpPackage.responseMethod(conn);
		} catch (Exception e) {
			LOG.error("HttpPackage.postMethod", e);
		} finally {
			if (null != out) {
				out.close();
			}
		}
		return result;
	}
}