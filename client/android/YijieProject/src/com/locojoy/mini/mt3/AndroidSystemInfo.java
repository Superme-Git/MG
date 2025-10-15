package com.locojoy.mini.mt3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

public class AndroidSystemInfo {

	private final static String kCpuInfoMaxFreqFilePath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";

	public static int getMaxCpuFreq() {
		int result = 0;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(kCpuInfoMaxFreqFilePath);
			br = new BufferedReader(fr);
			String text = br.readLine();
			result = Integer.parseInt(text.trim());
			result = result / 1000; // unit:MHZ
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static String getOSVersion() {
		String strOSVersion = android.os.Build.VERSION.RELEASE;
		return strOSVersion;
	}

	public static int getOSVersionCode() {
		int iOSVersion = android.os.Build.VERSION.SDK_INT;
		return iOSVersion;
	}

	public static String getCpuName() {
		FileReader fr = null;
		BufferedReader br = null;
		String result = "notknown";
		try {
			fr = new FileReader("/proc/cpuinfo");
			br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split(":\\s+", 2);
			if (array.length > 1) {
				result = array[1];
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	private static float m_totalCpuTime = 0.f;
	private static float m_processCpuTime = 0.f;
	public static float getProcessCpuRate()
	{
        float totalCpuTime = getTotalCpuTime();
        float processCpuTime = getAppCpuTime();
        
        if (totalCpuTime <= m_totalCpuTime)
        {
        	return 0.f;
        }
        else
        {
	        float cpuRate = 100 * (processCpuTime - m_processCpuTime) / (totalCpuTime - m_totalCpuTime);
	        m_processCpuTime = processCpuTime;
	        m_totalCpuTime = totalCpuTime;
	        return cpuRate;
        }
	}
    
	// 鑾峰彇绯荤粺鎬籆PU浣跨敤鏃堕棿
    private static long getTotalCpuTime()
    {
        String[] cpuInfos = null;
        try
        {
            BufferedReader reader = new BufferedReader(
            		new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }
    
	// 鑾峰彇搴旂敤鍗犵敤鐨凜PU鏃堕棿
    private static long getAppCpuTime()
    {
        String[] cpuInfos = null;
        try
        {
            int pid = android.os.Process.myPid();
            BufferedReader reader = new BufferedReader(
            		new InputStreamReader(new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long appCpuTime = Long.parseLong(cpuInfos[13])
                + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                + Long.parseLong(cpuInfos[16]);
        return appCpuTime;
    }

	public static int getCpuCount() {
		class CpuFilter implements FileFilter {
			public boolean accept(File pathname) {
				boolean bResult = false;
				try {
					bResult = Pattern.matches("cpu[0-9]", pathname.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				return bResult;
			}
		}

		int count = 0;
		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			count = files.length;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public static int getTotalMemSize() {
		int sizeMemory = 0;
		String str1 = "/proc/meminfo";
		String str2;
		String[] arrayOfString;
		FileReader localFileReader = null;
		BufferedReader localBufferedReader = null;
		try {
			localFileReader = new FileReader(str1);
			localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();

			arrayOfString = str2.split("\\s+");
			if (arrayOfString.length > 1) {
				sizeMemory = Integer.valueOf(arrayOfString[1]).intValue() / 1024; // KB/1024=MB
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (localFileReader != null) {
				try {
					localFileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (localBufferedReader != null) {
				try {
					localBufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sizeMemory;
	}

	public static int GetUsedMemory() {
		int nResult = GameApp.getApp().GetUsedMemory();
		return nResult;
	}
	
	public static int GetAvailableMemory() {
		int nResult = GameApp.getApp().GetAvailableMemory();
		return nResult;
	}

	public static String getProductModel() {
		String strModel = android.os.Build.MODEL;
		return strModel;
	}

	public static int getCurNetWorkType() {
		int nResult = GameApp.getApp().getCurNetWorkType();
		return nResult;
	}

	public static String getNetEnvir() {
		String strResult = GameApp.getApp().getNetEnvir();
		return strResult;
	}
	
	public static boolean IsNetworkConnected() {
		boolean bResult = GameApp.getApp().IsNetworkConnected();
		return bResult;
	}

	public static String getMacAddress() {
		String strResult = GameApp.getApp().getMacAddress();
		return strResult;
	}

	public static String getScreenResolution() {
		String strResult = GameApp.getApp().getScreenResolution();
		return strResult;
	}

	public static String getScreenWidthInfo() {
		String strResult = GameApp.getApp().getScreenWidthInfo();
		return strResult;
	}

	public static String getScreenHeightInfo() {
		String strResult = GameApp.getApp().getScreenHeightInfo();
		return strResult;
	}

	public static long getAvailabeSpace() {
		long blockSize = 0;
		long availableBlocks = 0;
		File path;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory();// 婵☆偓鎷锋繛鏉戔敇D闁告绱曢埞鏍⒒缂堢姴鈻忛柣顫妿濞堟垿鎯勯鑲╃Э
		} else {
			path = Environment.getDataDirectory();
		}
		StatFs stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public static String getVersionName() {
		String strVersionName = "";
		Context context = GameApp.getApp();
		try {
			strVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			strVersionName = "";
			e.printStackTrace();
		}
		return strVersionName;
	}

	public static String getPhoneNumber() {
		String strPhoneNumber = "";
		Context context = GameApp.getApp();
		try {
			TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			strPhoneNumber = tMgr.getLine1Number();
			if (strPhoneNumber == null) {
				strPhoneNumber = "";
			}
		} catch (Exception e) {
			strPhoneNumber = "";
			e.printStackTrace();
		}
		return strPhoneNumber;
	}
	
	public static String getDeviceID() {
		String strDeviceID = "";
		try {
			TelephonyManager tMgr = (TelephonyManager)GameApp.getApp().getSystemService(Context.TELEPHONY_SERVICE);
			strDeviceID = tMgr.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDeviceID;
	}
	
	public static String getProvider() {
		String strProvider = "";
		
		String net = GameApp.getApp().getActiveNetWork();
		ArrayList<String> infos = GameApp.getApp().getNetWorkList();
		if (net.equals("NO CONNECTION") || net.equals("WIFI")) {
			if (infos.size() > 1) {
				infos.remove("WIFI");
				net = infos.get(0);
			}
		}
		
		if (net.equals("3gwap") || net.equals("uniwap")
				|| net.equals("3gnet") || net.equals("uninet")) {
			strProvider = "Unicom";
		} else if (net.equals("cmnet") || net.equals("cmwap")) {
			strProvider = "Mobile";
		} else if (net.equals("ctnet") || net.equals("ctwap")) {
			strProvider = "Telecom";
		} else {
			strProvider = getProviderByTeleInfo();
		}
		
		return strProvider;
	}
	
	private static String getProviderByTeleInfo() {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) GameApp.getApp().getSystemService(Context.TELEPHONY_SERVICE);
			String operator = telephonyManager.getSimOperator();
			if (operator == null || operator.equals("")) {
				operator = telephonyManager.getSubscriberId();
			}
			if (operator == null || operator.equals("")) {
			}
			if (operator != null && !operator.equals("")) {
				if (operator.startsWith("46000")
						|| operator.startsWith("46002")
						|| operator.startsWith("46007")) {
					return "Mobile";
				} else if (operator.startsWith("46001")
						|| operator.startsWith("46006")) {
					return "Unicom";
				} else if (operator.startsWith("46003")
						|| operator.startsWith("46005")) {
					return "Telecom";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	// get screen brightness
	public static float getCurBrightness()
	{
		int screenBrightness = android.provider.Settings.System.getInt(GameApp.getApp().getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);
		return (float)screenBrightness / 255.f;
	}
	
	public static void openURL(String url)
	{
		GameApp.getApp().openURL(url);
	}
	
	// set screen brightness
	public static void setCurBrightness(float brightness)
	{
		// 是否将自动亮度调整为手动亮度
		boolean bAutoToManual = false;
		
        // adjust to manual brightness
        try
        {
            if(android.provider.Settings.System.getInt(GameApp.getApp().getContentResolver(),
            		android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE) == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
            {    
                 android.provider.Settings.System.putInt(GameApp.getApp().getContentResolver(),
                      android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                      android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                 bAutoToManual = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        // brightness will not < 1 in scope 0~255
        if(brightness < 1 / 255.f)
        {
        	brightness = 1 / 255.f;
        }
        
        //set activity brightness锛�0~1锛�
        final float lpBrightness = brightness;
        GameApp.getApp().runOnUiThread(new Runnable(){
        	public void run()
        	{
                WindowManager.LayoutParams lp = GameApp.getApp().getWindow().getAttributes();
                lp.screenBrightness = lpBrightness;
                GameApp.getApp().getWindow().setAttributes(lp);
        	}
        });

		// 若将自动亮度调整为手动亮度，则调整回自动亮度
        if (bAutoToManual) {
            try
            {
                if(android.provider.Settings.System.getInt(GameApp.getApp().getContentResolver(),
                		android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE) != android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
                {    
                     android.provider.Settings.System.putInt(GameApp.getApp().getContentResolver(),
                          android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                          android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	public static float getBatteryRatio() {
		float fRatio = GameApp.getApp().getBatteryRatio();
		return fRatio;
	}
	
	public static boolean isBatteryCharging()
	{
		boolean bCharging = GameApp.getApp().isBatteryCharging();
		return bCharging;
	}

}
