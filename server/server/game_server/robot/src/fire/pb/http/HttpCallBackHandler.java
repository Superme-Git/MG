package fire.pb.http;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;

/**
 * @author dc
 *         http异步回调接口包装
 */
public abstract class HttpCallBackHandler implements FutureCallback<HttpResponse> {

	protected abstract boolean process(JSONObject json);

	@Override
	public void completed(final HttpResponse response) {
		try {
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				if (result == null || result.isEmpty()) {
					mkdb.Trace.error("HttpCallBackHandler completed, but HttpEntity to String is null!");
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
		mkdb.Trace.error("http response failed:" + ex + ";thread:" + Thread.currentThread().getId());
	}

	@Override
	public void cancelled() {
		mkdb.Trace.error("http response cancelled;thread:" + Thread.currentThread().getId());
	}
}
