package test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;

import locojoy.game.mt3.utils.FileUtils;
import locojoy.game.mt3.utils.HttpPackage;

public class Test {

	public static void main(String[] args) {
		
//		String APP_CONF = "/log4j.properties";
//		PropertyConfigurator log4j = new PropertyConfigurator();
//		log4j.configure(FileUtils.getClassPath() + APP_CONF);
		
		File file = new File("iflytek02.wav");
		byte[] testFile = FileUtils.loadFile(file);
		
		JSONObject params = new JSONObject();
		params.put("svc", "iat");
		params.put("sec", 10);
		params.put("mac", "17-01-2c-5d-3e-2f");
		params.put("auf", "audio/L16;rate=16000");
		params.put("ent", "sms16k");
		params.put("aue", "raw");
		params.put("speech", DatatypeConverter.printBase64Binary(testFile));
		params.put("ogg", DatatypeConverter.printBase64Binary(testFile));
		System.out.println(params.toString(4));
		
		System.out.println("##############上传###################");
//		String url = "http://42.62.98.140:9862/transServer/trans";
		String url = "http://127.0.0.1:8080/transServer/trans";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		String ret = HttpPackage.postMethod(url, headers, params.toString());
		JSONObject json = new JSONObject(ret);
		System.out.println(json.toString(4));
/*		
		//下载
		System.out.println("##############下载###################");
		Map<String, String> headers2 = new HashMap<String, String>();
		headers.put("Content-Type", "application/octet-stream; charset=utf-8");
		String result = json.getString("result");
		String downUrl = "http://192.168.29.161:8080/iat/" + result + ".txt";
//		String downUrl = "http://192.168.32.39:8080/iat/" + result + ".txt";
		String ret2 = HttpPackage.postMethod(downUrl, headers2, params.toString());
		System.out.println(ret2);
		
		*/
	}
	
	
}
