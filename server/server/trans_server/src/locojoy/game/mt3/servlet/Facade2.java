package locojoy.game.mt3.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import locojoy.game.mt3.async.ExecutorTask;
import locojoy.game.mt3.task.StorageTask2;
import locojoy.game.mt3.utils.Conf;
import locojoy.game.mt3.utils.StateCodeEnum;

/**
 * 接收客户端翻译数据
 * 
 * 不阻塞直接返回下载地址给客户端，异步执行与讯飞语音识别
 * 
 * @author liangyanpeng
 * 
 * http://ip:port/transServer/playback
 */
@WebServlet("/playback")
public class Facade2 extends HttpServlet {

	private static final Logger LOG = Logger.getLogger("BATTLE");

	private static final long serialVersionUID = 1L;

	protected ExecutorTask _exc = new ExecutorTask();

	public Facade2() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int contentLength = request.getContentLength();

		if (LOG.isDebugEnabled()) {
			LOG.debug(request.getHeader("user-agent"));
			LOG.debug(request.getHeader("accept"));
			LOG.debug(request.getHeader("Content-Type"));
			LOG.debug(contentLength);
		}

		// 获取body内容
		BufferedReader rd = request.getReader();
		StringBuilder json = new StringBuilder();
		String tmp;
		while ((tmp = rd.readLine()) != null) {
			json.append(tmp);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("内容[" + json.toString() + "]");
			LOG.debug("char数组[" + json.capacity() + "]");
			LOG.debug("byte数组[" + json.toString().getBytes().length + "]");
		}

		// 长度不一致
		if (json.capacity() < contentLength) {
			LOG.error("Read and upload data no same!");
			return;
		}

		JSONObject o = new JSONObject(json.toString());
		if (LOG.isDebugEnabled()) {
			LOG.debug(o.toString(4));
		}

		byte[] decodedSpeech = Base64.getDecoder().decode(o.getString("speech"));
	//	String decodedSpeechStr = new String(decodedSpeech, Charset.forName("UTF-8"));
		String fileName = o.getString("filename");
	//	o.put("speech", decodedSpeechStr);
		_exc.excAsync(new StorageTask2(decodedSpeech, Conf.getPlayBackDir(),fileName)); // 异步存储
		
		JSONObject params = new JSONObject();
		params.put("code", StateCodeEnum.SUCCESS.value());
		params.put("address", Conf.getDownPlayBackLink() + fileName);
		params.put("buffsize", decodedSpeech.length);
		params.put("filesize", decodedSpeech.length);
		response.getWriter().append(params.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
}
