package com.world2.utils;
import javax.servlet.http.HttpServletRequest;
public class CommonTools {
    
    public static String getRemoteIp(HttpServletRequest request) {
	    String remoteAddr = request.getHeader("X-Forwarded-For");
	    if (remoteAddr == null) {
	        remoteAddr = request.getRemoteAddr();
	    } else {
	        String[] ips = remoteAddr.split(",");
	        if (ips != null && ips.length > 1) {
	            remoteAddr = ips[ips.length - 1];
	        }
	    }
	    return remoteAddr.trim();
    }
}