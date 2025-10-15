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
 * ���տͻ��˷�������
 * 
 * ������ֱ�ӷ������ص�ַ���ͻ��ˣ��첽ִ����Ѷ������ʶ��
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

		// ��ȡbody����
		BufferedReader rd = request.getReader();
		StringBuilder json = new StringBuilder();
		String tmp;
		while ((tmp = rd.readLine()) != null) {
			json.append(tmp);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("����[" + json.toString() + "]");
			LOG.debug("char����[" + json.capacity() + "]");
			LOG.debug("byte����[" + json.toString().getBytes().length + "]");
		}

		// ���Ȳ�һ��
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
		_exc.excAsync(new StorageTask2(decodedSpeech, Conf.getPlayBackDir(),fileName)); // �첽�洢
		
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
