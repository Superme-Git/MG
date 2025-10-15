package fire.pb.http;

import java.util.concurrent.Future;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import fire.pb.main.Gs;

/**
 * @author dc
 * http同步请求工具
 */
public class HttpUtil {
	public static final Logger logger = Logger.getLogger("SYSTEM");
	
	public static JSONObject execHttpRequest(HttpGet req) {
		try {
			Future<HttpResponse> future = Gs.getHttpClient().execute(req, null);
			HttpResponse response = future.get();
			int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                logger.debug("HttpUtil.execHttpRequest.HttpEntity:" + result);
                if(result == null || result.isEmpty()){
                	logger.error("HttpUtil.execHttpRequest completed, but HttpEntity to String is null!");
                	return null;
                }
                JSONObject jsonret = JSONObject.fromObject(result);
                return jsonret;
            } else {
                throw new ClientProtocolException("HttpUtil.execHttpRequest,Unexpected http response status: " + status);
            }
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 应用宝的请求处理
	 * @param req
	 * @return
	 */
	public static JSONObject execHttpYYBRequest(HttpGet req) {
		try {
			Future<HttpResponse> future = Gs.getHttpClient().execute(req, null);
			HttpResponse response = future.get();
			int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                logger.debug("HttpUtil.execHttpYYBRequest.HttpEntity:" + result);
                if (result == null || result.isEmpty()) {
                	logger.error("HttpUtil.execHttpYYBRequest completed, but HttpEntity to String is null!");
                	return null;
                }
                JSONObject jsonret = JSONObject.fromObject(result);
                return jsonret;
            } else {
            	logger.error("HttpUtil.execHttpYYBRequest,Unexpected http response status: " + status);
            	return null;
            }
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
