package fire.pb.shop.utils;

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
 * http Э������
 * 
 * @author liangyanpeng
 *
 */
public class HttpPackage {

	private static Logger LOG = Logger.getLogger(HttpPackage.class);

	public static String postMethod(String url, Map<String, String> header, String body) {
		LOG.debug("����http����Ѷ��");
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
	
	public static String getMethod(String url, Map<String, String> header) {
		String result = "";
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
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
