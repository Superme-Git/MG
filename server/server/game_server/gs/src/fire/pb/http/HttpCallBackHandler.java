package fire.pb.http;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @author dc
 * http异步回调接口包装
 */
public abstract class HttpCallBackHandler implements FutureCallback<HttpResponse>{
	private static final Logger logger = Logger.getLogger("SYSTEM");
	
	protected abstract boolean process(JSONObject json);
	
	@Override
    public void completed(final HttpResponse response) {
		logger.info("http response completed:" + response.getStatusLine() + ";thread:" + Thread.currentThread().getId());
		try {
			int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                logger.debug(result);
                if(result == null || result.isEmpty()){
                	logger.error("HttpCallBackHandler completed, but HttpEntity to String is null!");
                	return;
                }
                JSONObject jsonret = JSONObject.fromObject(result);
                process(jsonret);
            } else {
                throw new ClientProtocolException("Unexpected http response status: " + status);
            }
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }

    @Override
    public void failed(final Exception ex) {
    	logger.error("http response failed:" + ex + ";thread:" + Thread.currentThread().getId());
    }

    @Override
    public void cancelled() {
    	logger.error("http response cancelled;thread:" + Thread.currentThread().getId());
    }
}
