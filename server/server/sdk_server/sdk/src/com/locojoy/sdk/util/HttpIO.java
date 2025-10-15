package com.locojoy.sdk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class HttpIO {
	public final static String codepage = "UTF8"; 
	private static final String useragent = "locojoy/auany";
	
	public static String getRequest(String urlstring, HttpConfig config) throws IOException {
		return getRequest(urlstring, config, 0);
	}
	
	public static String getRequest( String urlstring, HttpConfig config ,int maxlinenum) throws IOException {
		if( mkdb.Trace.isDebugEnabled())
			mkdb.Trace.debug("HttpIO.getRequest url = " + urlstring);
		final URL url = new URL(urlstring);
		final HttpURLConnection conn = (HttpURLConnection)url.openConnection();	
		conn.setAllowUserInteraction(false);
		conn.setUseCaches(false);
		conn.setRequestProperty("User-Agent", useragent);
		conn.setConnectTimeout(config.connecttimeout);
		conn.setReadTimeout(config.readtimeout);
		int linenum = 0;
		final InputStream is = conn.getInputStream();
		try {
			InputStreamReader in = new InputStreamReader(is, codepage);
			BufferedReader buffer = new BufferedReader(in, 4096);
			StringBuilder resultDataBuilder = new StringBuilder();;
			while(true) {
				final String line = buffer.readLine();
				if(null == line)
					break;
				resultDataBuilder.append(line);
				linenum++;
				if(maxlinenum > 0 && linenum >= maxlinenum)
					break;
			}
			return resultDataBuilder.toString();
		} finally {
			is.close();
		}
	}
	
	public static String digestVarString(String src) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		final MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(src.getBytes(codepage));
		final byte[] md5Byte = md5.digest();
		return HttpIO.bytesToHexString(md5Byte);
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
