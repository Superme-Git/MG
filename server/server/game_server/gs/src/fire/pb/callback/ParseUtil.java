package fire.pb.callback;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author dc
 * 一些解析工具
 */
public class ParseUtil {
	public final static String codepage = "UTF8"; 
	
	public static String digestVarString(String src) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(src.getBytes(codepage));
		final byte[] md5Byte = md5.digest();
		return ParseUtil.bytesToHexString(md5Byte);
	}
	
	private static final String HEX_CHARS = "0123456789abcdef";
	public static String bytesToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_CHARS.charAt(b[i] >>> 4 & 0x0F));
			sb.append(HEX_CHARS.charAt(b[i] & 0x0F));
		}
		return sb.toString();
	}
	static private int hexCharToByte(char c) {
		if(c >= '0' && c <= '9')
			return (c - '0');
		c = Character.toLowerCase(c);
		if(c >= 'a' && c <= 'f')
			return (c - 'a' + 10);
		throw new RuntimeException("stringToHexBytes bad format char " + c);
	}
	
	public static byte[] stringToHexBytes(String s) {
		final int length = s.length() / 2;
		final byte[] bs = new byte[length];
		for (int i = 0; i < length; i++) {
			final char c0 = s.charAt(i * 2);
			final char c1 = s.charAt(i * 2 + 1);
			
			final int b0 = hexCharToByte(c0);
			final int b1 = hexCharToByte(c1);
			bs[i] = (byte) ((b0 << 4 | b1) & 0x000000FF);
		}
		return bs;
	}
	
	public static Map<String,String> argsToStringMap(String args) throws UnsupportedEncodingException {
		final Map<String,String> result = new HashMap<String,String>();
		final String[] kvs = args.split("&");
		for(final String kv : kvs) {
			final int index = kv.indexOf('=');
			if(-1 == index)
				continue;
			final String key = kv.substring(0, index);
			final String value = URLDecoder.decode(kv.substring(index + 1), codepage);
			result.put(key, value);
		}
		return result;
	}
	
	//base64解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if(s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//base64加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		if(b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}
}
