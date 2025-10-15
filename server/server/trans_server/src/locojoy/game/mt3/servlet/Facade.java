package locojoy.game.mt3.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import locojoy.game.mt3.async.ExecutorTask;
import locojoy.game.mt3.task.IatTask;
import locojoy.game.mt3.task.StorageTask;
import locojoy.game.mt3.task.TalTask;
import locojoy.game.mt3.utils.Conf;
import locojoy.game.mt3.utils.FileUtils;
import locojoy.game.mt3.utils.StateCodeEnum;

/**
 * 接收客户端翻译数据
 * 
 * 不阻塞直接返回下载地址给客户端，异步执行与讯飞语音识别
 * 
 * @author liangyanpeng
 * 
 * http://ip:port/transServer/trans
 */
@WebServlet("/trans")
public class Facade extends HttpServlet {

	public static Logger LOG = Logger.getLogger("TRANS");
	
	private static final long serialVersionUID = 1L;

	protected ExecutorTask _exc = new ExecutorTask();

	public Facade() {
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
			LOG.debug("Request service is " + o.get("svc"));
		}

		String svc = o.getString("svc");// 服务类型
		int sec = o.getInt("sec");// 服务类型
		//String fileName = FileUtils.generateFileName();// 文件名
		String fileName = o.getString("uuid");
		String httpLink = null;// 服务器地址
		// 检测音频是否有数据
		String speech = o.getString("speech");
		if (speech == null || speech.isEmpty()) {
			LOG.error("speech数据不能是空!s");
			this.retLink(response, "", "", svc, 0, StateCodeEnum.SPEECH_ISNULL);
			return;
		}
		
		// 语音处理
		if ("iat".equals(svc)) {
//			_exc.excAsync(new IatTask(o, fileName));// 异步语音识别,存储翻译结果
			_exc.excAsync(new StorageTask(o, Conf.getIatVoiceDir(),fileName));// 异步存储音频
//			new IatTask(o, fileName).asyncMethod();  // 同步存储音频,语音识别,存储翻译结果
			httpLink = Conf.getDownIatLink();
		} else if ("tal".equals(svc)) {
			_exc.excAsync(new TalTask(o, fileName));
			httpLink = Conf.getDownTalLink();
		} else {
			this.retLink(response, "", "", "", 0, StateCodeEnum.SVC_ERROR);
		}

		// 返回下载链接
		this.retLink(response, httpLink, fileName, svc, sec, StateCodeEnum.SUCCESS);
	}

	/**
	 * 返回下载链接
	 */
	public void retLink(HttpServletResponse response, final String httpLink, final String fileName, final String svc,
			final int sec, final StateCodeEnum stateCode) {
		PrintWriter writer = null;

		StringBuilder ret = new StringBuilder("{");
		ret.append("ret:\"").append(stateCode.value()).append("\",");
		ret.append("svc:\"").append(svc).append("\",");
		ret.append("sec:").append(sec).append(",");
		ret.append("result:\"").append(fileName).append("\"");
		ret.append("}");

		try {
			writer = response.getWriter();
			writer.append(ret);
			writer.flush();
		} catch (IOException e) {
			LOG.error("返回下载链接 ", e);
		} finally {
			if (null != writer)
				writer.close();
		}
	}

}
