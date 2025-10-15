package robot.friend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author xinmanpei 好友空间http处理
 *
 */

public class FriendHttp {
	private static Logger LOG = Logger.getLogger(FriendHttp.class);

	public static String postMethod(String url, Map<String, String> header, String body) {
		LOG.debug("开始http任务");
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
			result = responseMethod(conn);
		} catch (Exception e) {
			LOG.error("HttpPackage.postMethod", e);
		} finally {
			if (null != out) {
				out.close();
			}
		}
		return result;
	}
	
	public static String getMethod(String url, Map<String, String> header, Map<String, String> content) {
		String result = "";
		String url_new = url + "?";
		for (String c_key : content.keySet()){
			url_new += c_key + "=" + content.get(c_key) + "&";
		}
		
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url_new).openConnection();
			for (String key : header.keySet()) {
				conn.setRequestProperty(key, header.get(key));
			}
			conn.setDoOutput(true);
			result = responseMethod(conn);
		} catch (Exception e) {
			LOG.error("HttpPackage.getMethod", e);
		}
		return result;
	}

	public static String responseMethod(HttpURLConnection conn) {
		StringBuilder response = new StringBuilder();
		BufferedReader rd = null;
		try {
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				LOG.error("HttpPackage.response error code : " + conn.getResponseCode());
				return "";
			}
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
			String line;
			while ((line = rd.readLine()) != null) {	
				response.append(line);
				response.append('\r');
			}
		} catch (Exception e) {
			LOG.error("HttpPackage.responseMethod", e);
		} finally {
			if (null != rd) {
				try {
					rd.close();
				} catch (IOException e) {
					LOG.error("HttpPackage.responseMethod close read stream", e);
				}
			}
		}
		return response.toString();
	}

}
