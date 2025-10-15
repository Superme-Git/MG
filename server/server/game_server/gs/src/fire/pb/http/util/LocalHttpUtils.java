/**
 * 
 */
package fire.pb.http.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import fire.log.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * http服务工具
 */
public class LocalHttpUtils {
	public final static String codepage = "UTF8"; 
	public static Logger logger = Logger.getLogger("SYSTEM");

	/** 
     * MD5加密 
     */  
    @SuppressWarnings("null")
	public static String getMD5Str(String str) {  
        MessageDigest messageDigest = null;  
  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
  
            messageDigest.reset();  
  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {  
            System.out.println("NoSuchAlgorithmException caught!");  
            System.exit(-1);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
  
        byte[] byteArray = messageDigest.digest();  
  
        StringBuffer md5StrBuff = new StringBuffer();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
        return md5StrBuff.toString();  
    }
	
	/**
	 * 将http请求参数转换为map对象
	 */
	public static Map<String,String> getParameters( HttpExchange exchange) throws Exception {
		String args = getQueryString(exchange);
		final Map<String,String> result = new HashMap<String,String>();
		if(args == null){
			return null;
		}
		final String[] kvs = args.split( "&");
		for( final String kv : kvs) {
			final int index = kv.indexOf( '=');
			if( -1 == index)
				continue;
			final String key = kv.substring( 0, index);
			final String value = URLDecoder.decode( kv.substring( index + 1), codepage);
			result.put( key, value);
		}
		return result;
	}
	
	/**
	 * 将http输入流转化为字符串
	 */
	public static String getQueryString( HttpExchange exchange) throws IOException {
		if( 0 == exchange.getRequestMethod().compareToIgnoreCase( "GET"))
			return exchange.getRequestURI().getQuery();
		String requestBodyString = getRequestBodyString(exchange);
		
		if(requestBodyString.length() == 0)
			return exchange.getRequestURI().getQuery();
		return requestBodyString;
	}
	
	public static String getRequestBodyString(HttpExchange exchange) throws IOException {
		final BufferedReader bufferreader = new BufferedReader(new InputStreamReader( exchange.getRequestBody(), "UTF-8"));
		final StringBuilder resultDataBuilder = new StringBuilder();
		while( true) {
			final String line = bufferreader.readLine();
			if( null == line)
				break;
			resultDataBuilder.append( line);
		}
		return resultDataBuilder.toString();
	}
    
    /**
     * RSA公钥验证
     */
    public static boolean RSACheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature.getInstance("MD5withRSA");

            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));

            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
