package locojoy.game.mt3.sdk.ifly;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import locojoy.game.mt3.utils.Conf;
import locojoy.game.mt3.utils.HttpPackage;

/**
 * ����
 */
public class Token {
	
	private static Logger LOG = Logger.getLogger("TRANS");

	private Token() {}
	
	public static Token getInstance() {
		return TokenHolder.INSTANCE;
	}
	
	/**
	 * token����ʱ��
	 */
	public long tokenCreateTime = 0L;

	/**
	 * token
	 */
	private String token = null;

	private String _xPar = null;

	public void createToken() {
		String url = Conf.getIflyUrl() + "?svc=token";
		// ��ʼ��WebPAI�����࣬����X-Par_����
		setXpar("imei=1111&imsi=1111&mac=1111&appid=558920c4");
		setToken(getToken(url));
		tokenCreateTime = System.currentTimeMillis();
		LOG.info("toke��ʼ�����");
	}

	/**
	 * ��X_par������������webAPI�ĸ�����ʹ�ã��˷�����IflytekWebapi�е�һ�������õķ���
	 * 
	 * @param xPar
	 */
	public void setXpar(String xPar) {
		_xPar = Base64.encodeBase64String(xPar.getBytes());
	}

	/**
	 * ���ýӿڻ�ȡ����Ҫ�ķ������ơ�
	 * 
	 * @return token
	 */
	public String getToken(String url) {
		String token = "";
		if (_xPar == null) {
			return "please set xpar";
		}
		Map<String, String> header = new HashMap<String, String>();
		header.put("X-Par", _xPar);
		String resultb64 = HttpPackage.getMethod(url, header);
		if (resultb64 == null || "".equals(resultb64)) {
			LOG.error("��ȡtokenʧ�ܣ�������");
			return null;
		}
		String result = new String(Base64.decodeBase64(resultb64));
		try {
			JSONObject json = new JSONObject(result);
			token = json.getString("token");
		} catch (JSONException e) {
			LOG.error("��ȡtokenʧ��: ", e);
		}
		return token;
	}

	public synchronized String getToken() {
		return token;
	}

	public synchronized void setToken(String token) {
		this.token = token;
	}
	
	private static class TokenHolder {
		private final static Token INSTANCE = new Token();
	}

}
