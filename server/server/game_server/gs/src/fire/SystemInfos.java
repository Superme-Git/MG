package fire;

import java.net.InetAddress;


public class SystemInfos {
	//是Windows操作系统return true
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}	
	/**判断ip版本*/
	public static int getVersion(InetAddress addr) {
		byte[] address = addr.getAddress();
		if (address.length == 4)
			return 4;
		else if (address.length == 16) {
			return 6;
		} else {
			return -1;
		}
	}
}
