package com.locojoy.mini.mt3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxEditText;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxRenderer;
import org.cocos2dx.lib.Cocos2dxVideoHelper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.locojoy.wojmt3.yj.R;

import com.snowfish.cn.ganga.base.IUtils;
import com.snowfish.cn.ganga.base.SFActionCallback;
import com.snowfish.cn.ganga.base.SFNativeAdapter;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import cn.shuzilm.core.*;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class GameApp extends Cocos2dxActivity {
	//加载c++代码库，如果需要加载其他的库文件，请将gangaOnlineUnityHelper放最前面.
    static {
        System.loadLibrary("gangaOnlineUnityHelper");
    }
	
	public final static int UPDATE_ENGINE_NOTIFY_MSG = 1;
	public final static int UPDATE_ENGINE_NOTIFY_START = 2;
	public final static int UPDATE_ENGINE_NOTIFY_END = 3;
	public final static int UPDATE_ENGINE_NOTIFY_STEP = 4;
	public final static int UPDATE_ENGINE_NOTIFY_LOCALVERSION = 5;
	public final static int UPDATE_ENGINE_NOTIFY_LATESTVERSION = 6;
	public final static int UPDATE_ENGINE_NOTIFY_DOWNLOADSIZE = 7;
	public final static int UPDATE_ENGINE_NOTIFY_DOWNLOADEND = 8;
	public final static int UPDATE_ENGINE_NOTIFY_DOWNLOADTOOLARGE = 9;
	public final static int UPDATE_ENGINE_NOTIFY_FAIL = 10;
	public final static int UPDATE_ENGINE_NOTIFY_FORM = 11;
	public final static int UPDATE_ENGINE_NOTIFY_SYSTEMBAR_DELAY_HIDE = 12;

	private native void setUpBreakpad(String filepath);
	private native String getHttpNoticeUrl();
	private static native String getHttpShareUrl();

	public static AudioManager mAudioManager;

	private static GameApp gameApp = null;

	private static String m_FilesDir = null;
	private static String m_CacheDir = null;
	
	private int m_FilesCount = 0;
	private int m_CopyCount = 0;
	private float m_TotalSize = 0;
	private static String m_PackageName;
	
	private final String TAG = "GameApp";
	private final String cfgFileName = "ljsdk.cfg";
	
	private TextView m_UpdateInfo;
	private TextView m_CurVerLabel;
	private TextView m_CurVer;
	private TextView m_NewVerLabel;
	private TextView m_NewVer;
	private TextView m_Percent;
	private TextView m_GameTips;
	private TextView m_GameTitle;
	private RelativeLayout m_RL;
	private Button m_BtnCancel;
	private Button m_BtnOK;
	
	private int m_FormType;
	private String m_AppUrl;
	private int m_webViewWidth = 0;
	private int m_webViewHeight = 0;
	
	private static ClipboardManager m_clipboardmanager;

	private InitThread m_InitThread;
	private UpdateThread m_UpdateThread;
	private WGListThread m_WGListThread;
	private static Handler m_InitHandler;
	private static Handler m_UpdateHandler;
	
	public static native void nativeStartResourceUpdate();

	public static native void nativeContinueResourceUpdate(int reslut);
	
	public static native int nativeGetNotifyEnable(int id);
	
	public static native void nativeSetChannelName(String channelName);
	
	public static native void nativeSetIsYingYongbao();
	
	public static native void nativeExitGame();
	public static native void nativeDestroy();
	
	private static String[][] m_Files;
	private String[] Dir = {
			"cache",
			"res",
	};
	
	private List<String>m_EmuList = new ArrayList<String>();
	private List<String>m_GAList = new ArrayList<String>();
    
	
	// 手机方向监听
    OrientationEventListener mOrientationListener;
	
	private Cocos2dxVideoHelper mVideoHelper;

	public static GameApp getApp() {
		return gameApp;
	}

	public GameApp() {
		gameApp = this;
	}
	
	public boolean isUseDuCilnetSdk()
	{	
		try {
			File df = new File(GameApp.getCacheOutDir() + "/res/" + "2863654426");//clientsetting_android.ini
			InputStream ins = null;
			if (df.exists()) {
				ins = new FileInputStream(df);
			}
			else{
				ins = getApp().getAssets().open("res/2863654426");
			}	
			
			if(ins != null)
			{
				Properties pp = new Properties();
				pp.load(ins);
				String flag = pp.get("IsUseDuClientSdk").toString();
				ins.close();
				if(flag.equals("1")){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(isUseDuCilnetSdk()){
			String strMyChannelId = String.valueOf(GetChannelId());
			Log.i("Main.go", "strMyChannelId.. " + strMyChannelId);
			Main.go(getApplicationContext(), strMyChannelId, null);	
		}
		
		m_clipboardmanager = (ClipboardManager)GameApp.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
		
		setUpBreakpad(getCacheOutDir());
		
		clearDeviceHeap();
		
		// 隐藏虚拟按键
		//hideNavigation();
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.mt_update);
		
		// -----------------------------------viewupdate start------------------
		// assign
		m_UpdateInfo = (TextView) findViewById(R.id.mt3_update_textView1);
		m_Percent = (TextView) findViewById(R.id.mt3_update_textView2);
		m_GameTitle = (TextView) findViewById(R.id.mt3_update_textView3);
		m_GameTips = (TextView) findViewById(R.id.mt3_update_textView4);
		
		m_CurVerLabel = (TextView) findViewById(R.id.mt3_update_textViewCurVerLabel);
		m_CurVerLabel.setVisibility(View.INVISIBLE);
		m_CurVer = (TextView) findViewById(R.id.mt3_update_TextViewCurVer);
		m_CurVer.setVisibility(View.INVISIBLE);
		m_NewVerLabel = (TextView) findViewById(R.id.mt3_update_TextViewNewVerLabel);
		m_NewVerLabel.setVisibility(View.INVISIBLE);
		m_NewVer = (TextView) findViewById(R.id.mt3_update_TextViewNewVer);
		m_NewVer.setVisibility(View.INVISIBLE);
		
		m_RL = (RelativeLayout) findViewById(R.id.mt3_update_RelativeLayout);
		m_BtnCancel =(Button) findViewById(R.id.mt3_update_button1);
		m_BtnOK =(Button) findViewById(R.id.mt3_update_button2);
		
		//初始化外挂模拟器黑名单
		InitBlackList();
		
		// 隐藏虚拟按键
		hideNavigation();
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			View decorView = getWindow().getDecorView();
			decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {				
				@Override
				public void onSystemUiVisibilityChange(int visibility) {		
					if(visibility == 0 || ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0))
					{
						m_UpdateHandler.sendEmptyMessageDelayed(UPDATE_ENGINE_NOTIFY_SYSTEMBAR_DELAY_HIDE,5000);
					}
				}
			});
			
		}

		m_InitHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					
					break;
				case 1:
					m_Percent.setText((CharSequence) msg.obj);
					break;
				case 2:
					m_Percent.setVisibility(View.GONE);
					m_UpdateInfo.setText((CharSequence) msg.obj);

					NotifyStart("Start Update");
					break;
				}
			}
		};
		
		// msg handler
		m_UpdateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String strUpdateFailTip = "更新失败";
				String strUpdateRetryTip = "请确保网络畅通后，重试！";
				String strRetry = "重试";
				String strExit = "退出";
				String strGoon = "继续";
				try {
					strUpdateFailTip = GameApp.getApp().getString(R.string.mt3_strinfo_update_fail_tip);
					strUpdateRetryTip = GameApp.getApp().getString(R.string.mt3_strinfo_update_retry_tip);
					strRetry = GameApp.getApp().getString(R.string.mt3_strinfo_retry);
					strExit = GameApp.getApp().getString(R.string.mt3_strinfo_exit);
					strGoon = GameApp.getApp().getString(R.string.mt3_strinfo_go_on);
				} catch (Exception e) {
					e.printStackTrace();
				}

				switch (msg.what) {
				case UPDATE_ENGINE_NOTIFY_FORM:{
					m_FormType = msg.arg1;
					m_AppUrl = (String) msg.obj;
					switch(msg.arg1){
					case -1:
						m_GameTips.setText(getString(R.string.mt3_getinternetfail));
						break;
					case 1:
						m_GameTips.setText(getString(R.string.mt3_getversionfail));
						break;
					case 2:
						m_GameTips.setText(getString(R.string.mt3_versionisold));
						break;
					case 3:
						m_GameTips.setText(getString(R.string.mt3_getfilelistfail));
						break;
					case 4:
						m_TotalSize = msg.arg2;
						if (m_TotalSize >= 1024){
							m_GameTips.setText(String.format(getString(R.string.mt3_strinfo_big_update_tip), String.format("%.1f", m_TotalSize/1024) + "MB"));
						}
						else{
							m_GameTips.setText(String.format(getString(R.string.mt3_strinfo_big_update_tip), String.valueOf(m_TotalSize) + "KB"));
						}
						m_GameTips.setTextSize(16);
						break;
					case 5:
						m_GameTips.setText(getString(R.string.mt3_strinfo_download_fail_tip));
						break;
					}
					m_RL.setVisibility(View.VISIBLE);
				}
					break;
				case UPDATE_ENGINE_NOTIFY_MSG: {
					m_UpdateInfo.setText((CharSequence) msg.obj);
				}
					break;
				case UPDATE_ENGINE_NOTIFY_START: {
					m_UpdateInfo.setText((CharSequence) msg.obj);
					StartUpdate();
				}
					break;
				case UPDATE_ENGINE_NOTIFY_END: {
					//LJStatistics.getInstance().init(gameApp, "10024", "208800000", "b18a26ffc632752987bd24a7bf0353f3");
					
					//鍒濆鍖栧鎸傛ā鎷熷櫒榛戝悕鍗�
					InitBlackList();
					
					initializeGame();
					
					SFNativeAdapter.init_listener(getApp(), new SFActionCallback() {
			            @Override
			            public void callback(Runnable run) {
			                runOnGLThread(run); 
			            }
			        });
				}
					break;
				case UPDATE_ENGINE_NOTIFY_FAIL: {
					AlertDialog.Builder alert = new AlertDialog.Builder(GameApp.this);
					alert.setTitle(strUpdateFailTip);
					alert.setMessage(strUpdateRetryTip);
					alert.setPositiveButton(strRetry, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					alert.setNegativeButton(strExit, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							GameApp.getApp().finish();
							System.exit(0);
						}
					});
					alert.setCancelable(false);
					alert.create().show();
				}
					break;
				case UPDATE_ENGINE_NOTIFY_STEP: {
					m_UpdateInfo.setText(GameApp.getApp().getString(R.string.mt3_strinfo_downloading_tip) +   msg.arg1 + "%");
				}
					break;
				case UPDATE_ENGINE_NOTIFY_LOCALVERSION: {
					m_CurVerLabel.setVisibility(View.VISIBLE);
					m_CurVer.setVisibility(View.VISIBLE);
					m_CurVer.setText((CharSequence) msg.obj);
				}
					break;
				case UPDATE_ENGINE_NOTIFY_LATESTVERSION: {
					m_NewVerLabel.setVisibility(View.VISIBLE);
					m_NewVer.setVisibility(View.VISIBLE);
					m_NewVer.setText((CharSequence) msg.obj);
				}
					break;
				case UPDATE_ENGINE_NOTIFY_DOWNLOADSIZE: {
					String str = String.format("%dKB", m_TotalSize/1024);
					m_UpdateInfo.setText((CharSequence) msg.obj + str);
				}
					break;
				case UPDATE_ENGINE_NOTIFY_DOWNLOADEND: {
				}
					break;
				case UPDATE_ENGINE_NOTIFY_DOWNLOADTOOLARGE: {
					AlertDialog.Builder alert = new AlertDialog.Builder(GameApp.this);
					alert.setTitle("");

					float size = Integer.parseInt(String.valueOf((CharSequence)msg.obj));
					String msgBigUpdateTip = "";
					if (size >= 1024)
					{
						msgBigUpdateTip = "检测到新版本可以更新，本次更新包文件大小约" + String.format("%.1f", size/1024) + "MB,建议在wifi环境下载，点击确定下载更新，点击取消退出游戏";
					}
					else{
						msgBigUpdateTip = "检测到新版本可以更新，本次更新包文件大小约" + String.valueOf(size) + "KB,建议在wifi环境下载，点击确定下载更新，点击取消退出游戏";
					}
					
//					try {
//						String strBigUpdateTip = GameApp.getApp().getString(R.string.mt3_strinfo_big_update_tip);
//						msgBigUpdateTip = String.format(strBigUpdateTip, String.valueOf((CharSequence)msg.obj));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}

					alert.setMessage(msgBigUpdateTip);

					alert.setPositiveButton(strGoon, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					alert.setNegativeButton(strExit, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							GameApp.getApp().finish();
							System.exit(0);
						}
					});
					alert.setCancelable(false);
					alert.create().show();
				}
					break;
				case UPDATE_ENGINE_NOTIFY_SYSTEMBAR_DELAY_HIDE:{
					GameApp.getApp().hideNavigation();
				}
					break;
				default: {
				}
				}
			}
		};
		
		m_BtnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				m_RL.setVisibility(View.GONE);
				if (m_FormType != -1 && m_FormType != 4) {
					nativeContinueResourceUpdate(-1);
				}
				else{
	    			GameApp.getApp().finish();
					System.exit(0);
				}
			}
		});
		
		m_BtnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				m_RL.setVisibility(View.GONE);
				int result = -1;
				switch(m_FormType){
				case -1:
					NotifyStart("Start Update");
					break;
				case 1:
					result = 2;
					break;
				case 2:
					result = -1;
					Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(m_AppUrl));
					startActivity(it);
					break;
				case 3:
					result = 4;
					break;
				case 4:
					result = 6;
					break;
				case 5:
					result = 6;
					break;
				}
				
				if(m_FormType != -1){
					nativeContinueResourceUpdate(result);
				}
			}
		});
		
		m_PackageName = getApplicationInfo().packageName;
		
		StartInit();
		
		// 侦听电池电量：初始化
		initBatteryStateMonitor();

		// 侦听手机方向变化
		mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL)
		{
	    	@Override
	    	public void onOrientationChanged(int orientation) {
		    	if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
		    	} else if (orientation < 10 || orientation > 350) {
		    	} else if (orientation < 100 && orientation > 80) {
		        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		    	} else if (orientation < 190 && orientation > 170) {
		    	} else if (orientation < 280 && orientation > 260) {
		        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		    	} else {
		    	}
	    	}
	    };
	    // 若可以侦听，则开启侦听，否则关闭
		if (mOrientationListener.canDetectOrientation()) {
			mOrientationListener.enable();
		} else {
			mOrientationListener.disable();
		}
		
		initBaiduLocationSDK();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint({ "InlinedApi", "NewApi" })
	public void hideNavigation() {
		int version = android.os.Build.VERSION.SDK_INT;
		Window window = getWindow();
		if (version >= Build.VERSION_CODES.KITKAT) {
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE | 
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | 
					View.SYSTEM_UI_FLAG_FULLSCREEN |					
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
					);
		}
		else
		{
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE );
		}
	}
	
	public void openURL(String url)
	{
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		this.startActivity(i);
	}
	
	//-----------------------------------------反外挂部分-----start--------------------------------------------
	public static native void nativeSendWGInfo();
	public static native String nativeGetVoiceAddress();
	
	public static void StartWGListener()
	{
		// Handler 必须在UI线程中创建
		getApp().runOnUiThread(new Runnable(){
			@Override
			public void run() {
				//检测是否使用了外挂并添加到定时器
				final Handler handle_GA = new Handler(){
					public void handleMessage(Message msg)
					{
						super.handleMessage(msg);
						//检测是否使用了模拟器
						
						// 涉及Lua调用，必须在GL线程中调用
						getApp().runOnGLThread(new Runnable(){
							@Override
							public void run() {
								if(getApp().isUseEmulator()){
									nativeSendWGInfo();
								}
								
								if(getApp().isUseGameAssister()){
									nativeSendWGInfo();
								}
							}
						});
					}
				};
				
				//初始化定时器
				TimerTask tt = new TimerTask() {
					@Override
					public void run() {
						Message msg = new Message();
						handle_GA.sendMessage(msg);
					}
				};
				//启动定时器
				Timer time = new Timer();
				time.schedule(tt, 0, 300*1000);
			}
		});
	}
	
	public class WGListThread extends Thread {
		@Override
		public void run() {
			// 反外挂黑名单拉取地址：基础 URL 来自 Native 层 nativeGetVoiceAddress()；
			// 切换服务器 IP/域名时：在 Native 层修改返回的基础地址，或在 Java 层增加覆盖配置逻辑。
			String address = nativeGetVoiceAddress();
			FileDownloader.DownloadOneFile(address + "/transServer/wg/blacklist.txt", GameApp.getCacheOutDir() + "/res/" + "1288823573", false);
			try {
				File df = new File(GameApp.getCacheOutDir() + "/res/" + "1288823573");//balcklist.txt
				InputStream ins = null;
				if (df.exists()) {
					ins = new FileInputStream(df);
				}
				else{
					ins = getApp().getAssets().open("res/1288823573");
				}
				
				if(ins != null)
				{
					InputStreamReader inputreader = new InputStreamReader(ins);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String content = "";
					boolean isEmu = true;
					while((content = buffreader.readLine()) != null){

						if(content.equals("emulator:"))
						{
							isEmu = true;
							continue;
						}
						
						if(content.equals("gameAssister:"))
						{
							isEmu = false;
							continue;
						}
						
						if (isEmu) {
							m_EmuList.add(content);
						}
						else{
							m_GAList.add(content);
						}
					}
					
					buffreader.close();
					inputreader.close();
				}
				
				ins.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void InitBlackList()
	{
		m_WGListThread = new WGListThread();
		m_WGListThread.start();
	}
	
	public boolean isUseEmulator()
	{
		String device = Build.DEVICE;
		String model = Build.MODEL;
		String product = Build.PRODUCT;
		
		String result = "";
		try {
			String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
			ProcessBuilder cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			StringBuffer sb = new StringBuffer();
			String readLine = "";
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((readLine = responseReader.readLine()) != null) {
				sb.append(readLine);
			}
			responseReader.close();
			
			result = sb.toString().toLowerCase();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//Toast.makeText(getApplicationContext(), "device:" + device + " model:" + model + " product:" + product, Toast.LENGTH_LONG).show();
		
		if(m_EmuList.contains(product) || (result.contains("intel(r)") && m_EmuList.contains("unknown")))
		{
			//Toast.makeText(getApplicationContext(), "请不要在模拟器运行游戏，游戏退出。", Toast.LENGTH_LONG).show();
			return true;
		}
		
		return false;
	}
	
	public boolean isUseGameAssister()
	{
		ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : infos) {
			String name = info.processName;
			
			if (m_GAList.contains(name)) {
				
				//Toast.makeText(getApplicationContext(), "检测到非法程序，游戏退出。", Toast.LENGTH_LONG).show();
				return true;
			}
	
		}
		
		return false;
	}
	//-----------------------------------------反外挂部分-----end--------------------------------------------
	
	private void GetFilesCount() {
		m_Files = new String[Dir.length][];
		try {
			for (int i = 0; i < Dir.length; i++) {
				m_Files[i] = this.getResources().getAssets().list(Dir[i]);
				m_FilesCount += m_Files[i].length;
			}
			m_FilesCount -= Dir.length - 2;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void NotifyMsg_Init(String msg, int Type) {
		Message smsg = new Message();
		smsg.obj = msg;
		smsg.what = Type;
		m_InitHandler.sendMessage(smsg);
	}
	
	private void CopyAssetsWithoutVersion(String assetDir) {
		
		File tmp = new File(getCacheOutDir() + "/res/");
		if (!tmp.exists()) {
			tmp.mkdirs();
		}
		
		for (int i = 0; i < Dir.length; i++) {
			File mkDir = new File(assetDir + Dir[i]);
			if (!mkDir.exists()) {
				mkDir.mkdirs();
			}
		}

		for (int i = 0; i < m_Files.length; i++) {
			try {				
				for (int j = 0; j < m_Files[i].length; j++) {
					File outFile = new File(assetDir + Dir[i], m_Files[i][j]);
					if (outFile.isDirectory()) {
						continue;
					}
					if (!outFile.exists())
					{
						InputStream in = getAssets().open(Dir[i] + "/" + m_Files[i][j]);

						OutputStream out = new FileOutputStream(outFile, true);

						// Transfer bytes from in to out
						byte[] buf = new byte[1024];
						int len;
						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						out.close();
						in.close();
					}
					
					++m_CopyCount;
					
					DecimalFormat df = new DecimalFormat("0.0");
					String val = df.format((((float) (m_CopyCount) / m_FilesCount) * 100));
					
					NotifyMsg_Init(val + "%", 1);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class InitThread extends Thread {
		@Override
		public void run() {
			// GetFilesCount();

			// NotifyMsg_Init("", 0);

			// CopyAssetsWithoutVersion(getAssetsOutDir()+"/");

			// NotifyMsg_Init(getString(R.string.copyfinishtip), 2);

			for (int i = 0; i < Dir.length; i++) {
				File mkDir = new File(getAssetsOutDir() + "/" + Dir[i]);
				if (!mkDir.exists()) {
					mkDir.mkdirs();
				}
			}

			// 去掉文件
			try {
				String file[] = { "res/fl.ljpi", "res/ver.ljvi",
						"res/2863654426",	//"res/cfg/clientsetting_android.ini"
						"res/3656893336"};	//"res/cfg/android_notify.xml"
				for (int j = 0; j < file.length; j++) {
					File df = new File(getAssetsOutDir() + "/" + file[j]);

					if (df.exists()) {
						df.delete();
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				String file[] = { "res/fl.ljpi", "res/ver.ljvi",
						"res/2863654426",	//"res/cfg/clientsetting_android.ini"
						"res/3656893336"};	//"res/cfg/android_notify.xml"
				for (int j = 0; j < file.length; j++) {
					File outFile = new File(getAssetsOutDir() + "/" + file[j]);

					InputStream in = getAssets().open(file[j]);

					OutputStream out = new FileOutputStream(outFile, true);

					// Transfer bytes from in to out
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}

					out.close();
					in.close();

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			NotifyMsg_Init("", 2);
		}
	}
	
	public class UpdateThread extends Thread {
		@Override
		public void run() {
			nativeStartResourceUpdate();
		}
	}

	private void StartInit() {
		m_InitThread = new InitThread();
		m_InitThread.start();
	}
	
	private void StartUpdate() {
		m_UpdateThread = new UpdateThread();
		m_UpdateThread.start();
	}
	
	public static String getAssetsOutDir() {
		if (m_FilesDir != null) {
			return m_FilesDir;
		} else {
			if (GameApp.getApp().getExternalFilesDir(null) != null) {
				m_FilesDir = GameApp.getApp().getExternalFilesDir(null).getAbsolutePath();
			} else {
				m_FilesDir = GameApp.getApp().getFilesDir().getAbsolutePath();
			}
			return m_FilesDir;
		}
	}
	
	public static String getCacheOutDir() {
//		if (m_CacheDir != null) {
//			return m_CacheDir;
//		} else {
//			if (GameApp.getApp().getExternalCacheDir() != null) {
//				m_CacheDir = GameApp.getApp().getExternalCacheDir().getAbsolutePath();
//			} else {
//				m_CacheDir = GameApp.getApp().getCacheDir().getAbsolutePath();
//			}
//			return m_CacheDir;
//		}
		
		return getAssetsOutDir() + "/cache";
	}
	
	public static String getLogOutDir() {
		if (GameApp.getApp().getExternalCacheDir() != null) {
			return GameApp.getApp().getExternalCacheDir().getAbsolutePath();
		} else {
			return GameApp.getApp().getCacheDir().getAbsolutePath();
		}
	}

	protected void onDestroy() {
		super.onDestroy();

	    // 关闭游戏时，关闭侦听
		mOrientationListener.disable();

		SFOnlineHelper.onDestroy(this);
		
		System.exit(0);
	}
	
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onRestart() {

		super.onRestart();

		SFOnlineHelper.onRestart(this);
	}
	protected void onStop() {
		super.onStop();
		
		SFOnlineHelper.onStop(this);
	}
	
	@Override
	public void onTrimMemory(int level) {
		Log.e(TAG, " onTrimMemory ... level:" + level);
		
		System.gc();
		if (mGLView != null) {
			mGLView.notifyMemoryWarning();
		}
		
//		switch (level) {
//		case TRIM_MEMORY_COMPLETE:			// 内存不足，并且该进程在后台进程列表最后一个，马上就要被清
//			break;
//		case TRIM_MEMORY_MODERATE:			// 内存不足，并且该进程在后台进程列表的中部
//			break;
//		case TRIM_MEMORY_BACKGROUND:		// 内存不足，并且该进程是后台进
//			break;
//		case TRIM_MEMORY_UI_HIDDEN:			// 内存不足，并且该进程的UI已经不可见了
//			break;
//		case TRIM_MEMORY_RUNNING_CRITICAL:	// 内存不足(后台进程不足3，并且该进程优先级比较高，需要清理内
//			break;
//		case TRIM_MEMORY_RUNNING_LOW:		// 内存不足(后台进程不足5，并且该进程优先级比较高，需要清理内
//			break;
//		case TRIM_MEMORY_RUNNING_MODERATE:	// 内存不足(后台进程超过5，并且该进程优先级比较高，需要清理内
//			break;
//		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initializeGame() {
		if (detectOpenGLES20()) {
			// get the packageName,it's used to set the resource path
			String packageName = getApplication().getPackageName();
			super.setPackageName(packageName);

			getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

			// FrameLayout
			ViewGroup.LayoutParams framelayout_params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			FrameLayout framelayout = new FrameLayout(this);
			framelayout.setLayoutParams(framelayout_params);
			setContentView(framelayout);

			// Cocos2dxGLSurfaceView
			mGLView = new Cocos2dxGLSurfaceView(this);
			framelayout.addView(mGLView);

			mGLView.setEGLContextClientVersion(2);
			mGLView.setCocos2dxRenderer(new Cocos2dxRenderer());
			
			// 输入法相
			LayoutInflater inflater = LayoutInflater.from(this);
			View inputView = inflater.inflate(R.layout.input_ui, null);
			Button btnConfirm = (Button)inputView.findViewById(R.id.btn_confirm);
			Button btnCancel = (Button)inputView.findViewById(R.id.btn_cancel);
			Cocos2dxEditText editText = (Cocos2dxEditText)inputView.findViewById(R.id.edittext_input);
			editText.setMainView(mGLView);
			mGLView.setInputField(editText, btnCancel, btnConfirm);
			framelayout.addView(inputView);
			
			// 隐藏虚拟按键
			//hideNavigation(); // 有虚拟按键的安卓机型需要单独做适配，这里先去掉，已修复一个严重bug
			
			mVideoHelper = new Cocos2dxVideoHelper(this, framelayout);
			
		} else {
			finish();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus)
		{
			m_UpdateHandler.sendEmptyMessageDelayed(UPDATE_ENGINE_NOTIFY_SYSTEMBAR_DELAY_HIDE,5000);
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 隐藏虚拟按键
		hideNavigation();
				
		SFOnlineHelper.onResume(this);
		
		//LJStatistics.getInstance().onResume(gameApp);
		if (mGLView != null) {
			mGLView.onResume();
		}
		mAudioManager = (AudioManager) getSystemService(android.app.Service.AUDIO_SERVICE);
		
		// 侦听电池电量：注册广播接收器
		registerReceiver(batteryLevelRcvr, batteryLevelFilter);
		
		//取消本地推送
		try {
			LocalNotificationManager.enableNotification(3);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		SFOnlineHelper.onPause(this);
		
		if (mGLView != null) {
			mGLView.onPause();
		
			//开启本地推送
			this.runOnGLThread(new Runnable() {
				public void run() {
					try {
						LocalNotificationManager.enableNotification(1);
						LocalNotificationManager.enableNotification(2);
					} catch (Exception e) { e.printStackTrace(); }
				}
			});
		}
		
		// 侦听电池电量：注销广播接收
		unregisterReceiver(batteryLevelRcvr);
	}

	private boolean detectOpenGLES20() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return (info.reqGlEsVersion >= 0x20000);
	}

	public int GetUsedMemory() {
		int nResult = 0;
		
		try {
			MemoryInfo MemInfoArr = new MemoryInfo();
			Debug.getMemoryInfo(MemInfoArr);
			nResult = MemInfoArr.getTotalPrivateDirty()/1024;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return nResult;
	}
	
	public int GetAvailableMemory()
	{
		int nResult = 0;
		try {
			ActivityManager actMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
			actMgr.getMemoryInfo(mi);
			long regFreeSize = mi.availMem / (1024 * 1024);
			nResult = (int) regFreeSize;
			if (nResult < 1) {
				nResult = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return nResult;
	}

	public int getCurNetWorkType() {
		ConnectivityManager connectiMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectiMgr != null) {
			try {
				NetworkInfo info = connectiMgr.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getType() == ConnectivityManager.TYPE_WIFI) {
						return 1;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public String getNetEnvir() {
		ConnectivityManager connectiMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectiMgr != null) {
			try {
				NetworkInfo info = connectiMgr.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					return info.getTypeName();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "NO CONNECTION";
	}
	
	public String getActiveNetWork() {
		ConnectivityManager connectiMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectiMgr != null) {
			try {
				NetworkInfo info = connectiMgr.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					if (info.getTypeName().equals("WIFI")) {
						return info.getTypeName();
					} else {
						return info.getExtraInfo(); // cmwap/cmnet/wifi/uniwap/uninet
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "NO CONNECTION";
	}
	
	public ArrayList<String> getNetWorkList() {
		ArrayList<String> list = new ArrayList<String>();
		
		ConnectivityManager connectiMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectiMgr != null) {
			try {
				NetworkInfo[] infos = connectiMgr.getAllNetworkInfo();
				if (infos != null) {
					for (int i = 0; i < infos.length; i++) {
						NetworkInfo info = infos[i];
						String name = null;
						if (info.getType() == ConnectivityManager.TYPE_WIFI) {
							name = info.getTypeName();
						} else {
							name = info.getExtraInfo();
						}
						if (name != null && !list.contains(name)) {
							list.add(name);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean IsNetworkConnected() {
		ConnectivityManager connectiMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectiMgr != null) {
			try {
				NetworkInfo info = connectiMgr.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public String getMacAddress() {
		String macAddress = "00:00:00:00:00";
		WifiManager wifiMgr = null;
		try {
			wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (info != null) {
			macAddress = info.getMacAddress();
		}
		return macAddress;
	}

	public String getScreenResolution() {
		String strResolution = "";
		DisplayMetrics disMetrics = new DisplayMetrics();
		try {
			getWindowManager().getDefaultDisplay().getMetrics(disMetrics);
			strResolution = disMetrics.widthPixels + "x" + disMetrics.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResolution;
	}

	public String getScreenWidthInfo() {
		String strWidth = "";
		DisplayMetrics disMetrics = new DisplayMetrics();
		try {
			getWindowManager().getDefaultDisplay().getMetrics(disMetrics);
			strWidth = disMetrics.widthPixels + "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strWidth;
	}

	public String getScreenHeightInfo() {
		String strHeight = "";
		DisplayMetrics disMetrics = new DisplayMetrics();
		try {
			getWindowManager().getDefaultDisplay().getMetrics(disMetrics);
			strHeight = disMetrics.heightPixels + "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strHeight;
	}

	public static boolean checkNetwork(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED)
						return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
			return true;

		case KeyEvent.KEYCODE_VOLUME_DOWN:
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
			return true;
			
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			return true;
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	public static void RemoveDir(String dir) {
		File file = new File(dir);
		if (file.exists()) {
			DeleteRecuresive(file);
		}
	}

	private static void DeleteRecuresive(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (String i : children) {
				File temp = new File(dir, i);
				if (temp.isDirectory()) {
					DeleteRecuresive(temp);
				} else {
					final File to = new File(temp.getAbsolutePath() + System.currentTimeMillis());
					temp.renameTo(to);
					to.delete();
				}
			}
			final File to = new File(dir.getAbsolutePath() + System.currentTimeMillis());
			dir.renameTo(to);
			to.delete();
		} else {
			final File to = new File(dir.getAbsolutePath() + System.currentTimeMillis());
			dir.renameTo(to);
			to.delete();
		}
	}
	
	// -----------------------------------notify jni start ------------------

	public static void NotifyMsg(String msg) {
		Message smsg = new Message();
		smsg.obj = msg;
		smsg.what = UPDATE_ENGINE_NOTIFY_MSG;
		m_UpdateHandler.sendMessage(smsg);
	}

	public static void NotifyStart(String msg) {
		Message smsg = new Message();
		smsg.obj = msg;
		smsg.what = UPDATE_ENGINE_NOTIFY_START;
		m_UpdateHandler.sendMessage(smsg);
	}

	public static void NotifyEnd() {
		Message smsg = new Message();
		smsg.what = UPDATE_ENGINE_NOTIFY_END;
		m_UpdateHandler.sendMessage(smsg);
	}

	public static void NotifyFail() {
		Message smsg = new Message();
		smsg.what = UPDATE_ENGINE_NOTIFY_FAIL;
		m_UpdateHandler.sendMessage(smsg);
	}

	public static void NotifyStep(int step) {
		Message smsg = new Message();
		smsg.arg1 = step;
		smsg.what = UPDATE_ENGINE_NOTIFY_STEP;
		m_UpdateHandler.sendMessage(smsg);
	}

	public static void NotifyLocalVersion(String localversion) {
		Message smsg = new Message();
		smsg.obj = localversion;
		smsg.what = UPDATE_ENGINE_NOTIFY_LOCALVERSION;
		m_UpdateHandler.sendMessage(smsg);
	}

	public static void NotifyLatestVersion(String newversion) {
		Message smsg = new Message();
		smsg.obj = newversion;
		smsg.what = UPDATE_ENGINE_NOTIFY_LATESTVERSION;
		m_UpdateHandler.sendMessage(smsg);
	}

	public static void NotifyDownLoadSize(String sizeString) {
		Message smsg = new Message();
		smsg.obj = sizeString;
		smsg.what = UPDATE_ENGINE_NOTIFY_DOWNLOADSIZE;
		m_UpdateHandler.sendMessage(smsg);
	}

	public static void NotifyDownloadEnd() {
		Message smsg = new Message();
		smsg.what = UPDATE_ENGINE_NOTIFY_DOWNLOADEND;
		m_UpdateHandler.sendMessage(smsg);
	}

	public static void NotifyDownLoadSizeTooLarge(String total) {
		Message smsg = new Message();
		smsg.obj = total;
		smsg.what = UPDATE_ENGINE_NOTIFY_DOWNLOADTOOLARGE;
		m_UpdateHandler.sendMessage(smsg);
	}
	
	public static void NotifyShowForm(int type, int notifysize, String urlkey) {
		Message smsg = new Message();
		smsg.obj = urlkey;
		smsg.arg1 = type;
		smsg.arg2 = notifysize;
		smsg.what = UPDATE_ENGINE_NOTIFY_FORM;
		m_UpdateHandler.sendMessage(smsg);
	}
	
	public static String NotifyMsgByKey(String msg) {
		String ret = "";
		if(msg == "upmgrstr11")
		{
			ret = getApp().getString(R.string.mt3_upmgrstr11);
		}
		else if(msg == "upmgrstr21")
		{
			ret = getApp().getString(R.string.mt3_upmgrstr21);
		}
		else if(msg == "upmgrstr31")
		{
			ret = getApp().getString(R.string.mt3_upmgrstr31);
		}
		else if(msg == "upmgrstr32")
		{
			ret = getApp().getString(R.string.mt3_upmgrstr32);
		}
		else if(msg == "upmgrstr61")
		{
			ret = getApp().getString(R.string.mt3_upmgrstr61);
		}
		else if(msg == "upmgrstr71")
		{
			ret = getApp().getString(R.string.mt3_upmgrstr71);
		}
		
		return ret;
	}
	
	// -----------------------------------notify jni end ------------------
	
	
	static {
		System.loadLibrary("locSDK6a");
		System.loadLibrary("du");
		System.loadLibrary("game");
	};
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	// 侦听电池电量
	private BroadcastReceiver batteryLevelRcvr;
	private IntentFilter batteryLevelFilter;
	private float batteryRatio = 0.f;
	private boolean mIsBatteryCharging = false;
	private void initBatteryStateMonitor() {
		batteryLevelRcvr = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				int rawlevel = intent.getIntExtra("level", -1);
				int scale = intent.getIntExtra("scale", -1);
				int status = intent.getIntExtra("status", -1);
//				int health = intent.getIntExtra("health", -1);

				if (rawlevel >= 0 && scale > 0) {
					batteryRatio = (float)rawlevel / (float)scale;
				}
				
//				if (BatteryManager.BATTERY_HEALTH_OVERHEAT == health) {
//				} else {
//					switch (status) {
//					case BatteryManager.BATTERY_STATUS_UNKNOWN:
//						break;
//					case BatteryManager.BATTERY_STATUS_CHARGING:
//						break;
//					case BatteryManager.BATTERY_STATUS_DISCHARGING:
//					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
//						break;
//					case BatteryManager.BATTERY_STATUS_FULL:
//						break;
//					default:
//						break;
//					}
//				}
				
				mIsBatteryCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING);
			}
		};
		
		batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	}
	public float getBatteryRatio() {
		return batteryRatio;
	}
	
	public boolean isBatteryCharging()
	{
		return mIsBatteryCharging;
	}

	// 公告
	private HTML5WebView m_webView = null;
	public static void openUpdateview(int width, int height) {
		getApp().openWebview(width, height);
	}
	public static void closeUpdateView() {
		getApp().removeWebView();
	}
	public static void hideUpdateview() {
		getApp().hideWebview();
	}
	public static void resumeUpdateview() {
		getApp().resumeWebView();
	}
	private void openWebview(final int width, final int height) {
		this.runOnUiThread(new Runnable() {	//在UI线程里处
			public void run() {
				try {
					m_webView = new HTML5WebView(getApp(), width, height);
					// 公告页面 URL 来源于 Native 层的 getHttpNoticeUrl()；
					// 如需切换到自定义服务器地址：可在 Native 层返回新 URL，或改 Java 逻辑改为从 strings.xml/配置文件读取。
					m_webView.loadUrl(GameApp.this.getHttpNoticeUrl()); // for example : "http://42.62.47.211:130/mt3/ios_notice.html"
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void removeWebView() {
		this.runOnUiThread(new Runnable() {	//在UI线程里处
			public void run() {
				if (m_webView != null) {
					try {
						m_webView.destroyDrawingCache();
						m_webView.clearCache(true);
						m_webView.destroy();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					m_webView = null;
				}
				
				//CookieManager.getInstance().removeAllCookie();
				//CookieSyncManager.createInstance(getApp());
				//CookieSyncManager.getInstance().sync();
			}
		});
	}
	private void hideWebview() {
		this.runOnUiThread(new Runnable() {	//在UI线程里处理
			public void run() {
				if (m_webView != null) {
					try {
						m_webView.mGoneBecauseHideWebview = true;
						m_webView.setVisibility(View.GONE);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	private void resumeWebView() {
		this.runOnUiThread(new Runnable() {	//在UI线程里处理
			public void run() {
				if (m_webView != null) {
					try {
						m_webView.mGoneBecauseHideWebview = false;
						m_webView.setVisibility(View.VISIBLE);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public static void onRegistercb(String platformId, String roleId, String roleName, String serverId ){
		//LJStatistics.getInstance().onRegister(gameApp, platformId, roleId, roleName, serverId);
	}

	public static void onLogincb(String platformId, String roleId, String roleName, String serverId ){
		//LJStatistics.getInstance().onRegister(gameApp, platformId, roleId, roleName, serverId);
	}
	public static void onPaycb(String platformId, String roleId, String roleName, String serverId, 
			String appOrder, String channelOrder, String productId, String productPrice ){
		//LJStatistics.getInstance().onPay(gameApp, platformId, roleId, roleName, serverId,LJStatistics.PAY_TYPE.OTHER,appOrder,channelOrder,productId,productPrice);
	}
	
	// 上传dump文件，同时上传打包成同名ZIP文件的LOG
	public static void uploadDumpFile() {
		new Thread() {
			@Override
			public void run() {
				String dumpFileName = "";
				
				FTPUtils ftp = FTPUtils.getInstance();
				ftp.setLoginInfo(getHttpShareUrl(), 21, "collect_infoUSER", "TRx87dx@S0DXx8dC");	// "120.26.89.140"
				boolean bConneted = ftp.openConnect();
				if (bConneted) {
					dumpFileName = ftp.uploadFile(getCacheOutDir(), ".dmp", "/");
					ftp.closeConnect();
				}
				FTPUtils.destroyInstance();
				
				if (!dumpFileName.equals("")) {
					PackAndUploadZipFile(GameApp.getLogOutDir(), ".log", dumpFileName + ".zip");
				}
			}
		}.start();
	}
	
	public static void AndroidShowMQView(){
		getApp().conversation();
	}
	
	/**
	 * 将指定路径中的指定后缀的所有文件按指定的名称打包成Zip文件并上传到FTP服务器
	 */
	public static void PackAndUploadZipFile(String path, String suffix, String zipFileName) {
		String filePath = path;
		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}
		
		Log.e("mt3", "PackAndUploadZipFile : filePath="+filePath+",suffix="+suffix+",zipFileName="+zipFileName);
		
		final File zipFile = PackZipFile(filePath, suffix, zipFileName);
		if (zipFile != null) {
			final String localFile = filePath + zipFileName;
			final String filename = zipFileName;
			new Thread() {
				@Override
				public void run() {
					FTPUtils ftp = FTPUtils.getInstance();
					ftp.setLoginInfo(getHttpShareUrl(), 21, "collect_infoUSER", "TRx87dx@S0DXx8dC");	// "120.26.89.140"
					boolean bOpenConnectSucceed = ftp.openConnect();
					boolean bUploadSucceed = false;
					if (bOpenConnectSucceed) {
						bUploadSucceed = ftp.uploadSingleFile(localFile, filename, "/");
						ftp.closeConnect();
						zipFile.delete();
					}
					FTPUtils.destroyInstance();
					
					Log.e("mt3", "PackAndUploadZipFile : bOpenConnectSucceed="+bOpenConnectSucceed+",bUploadSucceed="+bUploadSucceed);
				}
			}.start();
		}
	}
	
	/**
	 * 将指定路径中的指定后缀的所有文件按指定的名称打包成Zip文件
	 */
	public static File PackZipFile(String filePath, String suffix, String zipFileName) {
		String localFile = filePath + zipFileName;
		File zipFile = new File(localFile);
		if (zipFile.exists()) {
			zipFile.delete();
		}
		
		try {
			OutputStream os = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(os);
			
			byte[] buffer = new byte[4096];
			int byteread = 0;
			
			File destDir = new File(filePath);
			for (File file : destDir.listFiles()) {
				String fileName = file.getName();
				if (suffix.equals("") || fileName.endsWith(suffix)) {
					try {
						ZipEntry entry = new ZipEntry(fileName);
						InputStream is = new FileInputStream(file);
						zos.putNextEntry(entry);
						while ((byteread = is.read(buffer)) != -1) {
							zos.write(buffer, 0, byteread);
						}
						is.close();
						zos.closeEntry();
					}
					catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
			zos.flush();
			os.flush();
			zos.close();
			os.close();
			return zipFile;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
    /**
     * 咨询客服
     *
     * @param v
     */
    public void conversation(/*View v*/) {
    }
    
    public static void showExitDialog()
    {
    	getApp().runOnUiThread(new Runnable() {	//在UI线程里处
			public void run() {
		    	AlertDialog.Builder exitGame = new AlertDialog.Builder(getApp());
		    	String l_title = getApp().getString(R.string.mt3_exittip);
		    	String l_confirmText = getApp().getString(R.string.mt3_exityes);
		    	String l_cancelText = getApp().getString(R.string.mt3_exitno);
			
		    	exitGame.setMessage(l_title);
		    	exitGame.setPositiveButton(l_confirmText, new DialogInterface.OnClickListener()
		    	{  	 
		    		public void onClick(DialogInterface dialog, int which)
		    		{
	    				nativeDestroy();

		    			getApp().finish();
		    		}   
		    	});  
		    	exitGame.setNegativeButton(l_cancelText, new DialogInterface.OnClickListener()
		    	{   
		    		public void onClick(DialogInterface dialog, int which)
		    		{                      
		    		}   
		    	});
		    	exitGame.create().show();
			}
		});
    }
    
    public static void ExitGame(){
    	getApp().finish();
	}
    
    /**
     * Android 调用ChartBoost接口
     */
    public static void AndroidCallChartBoost() {
    }
    
    /**
     * 获取易接子渠道的ChannelId
     */
    public static int GetYJChannelId() {
    	int nChannlId = IUtils.getMetaDataInt(getApp(), "WJMT31JIE");
    	return nChannlId;
    }
    //td
	public static void tdInit(){
	}
    /**
     * 获取Locojoy子渠道的ChannelId
     */
    public static int GetLocojoyChannelId() {
    	int nChannlId = 0;
    	return nChannlId;
	}
    
    /**
     * 获取渠道的ChannelId
     */
    public static int GetChannelId() {
		return GetYJChannelId();
    }
    
    /**
     * 删除指定路径下的指定后缀的文件（可选择包含子目录）
     */
    public static int DelFileArrOfPath(String basePath, String suffix, boolean bLoop) {
    	int cnt = 0;
    	
		File file = new File(basePath);
		File fileList[] = file.listFiles();
		for (File f : fileList) {
			if (f.isDirectory()) {
				if (bLoop) {
					cnt += DelFileArrOfPath(f.getAbsolutePath(), suffix, bLoop);
				}
			} else {
				String fileName = f.getName();
				if (suffix.equals("") || fileName.endsWith(suffix)) {
					f.delete();
					cnt++;
				}
			}
        }
		
    	return cnt;
    }
    
    /**
     * 清理手机内存
     */
    public void clearDeviceHeap() {
    	new Thread(new Runnable(){
    		@Override
    		public void run() {
                ActivityManager am = (ActivityManager)getApp().getSystemService(Context.ACTIVITY_SERVICE);
                List<RunningAppProcessInfo> apps = am.getRunningAppProcesses();
  
                long beforeMem = AndroidSystemInfo.GetAvailableMemory();
                
                int count = 0;
                if (apps != null) {
                	for (RunningAppProcessInfo app : apps) {
                        // importance 该进程的重要程度，分为几个级别，数值越低就越重要。
                        // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                        // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                        if (app.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                            String[] pkgList = app.pkgList;
                            for (int j = 0; j < pkgList.length; ++j) {
                                Log.e(TAG, "It will be killed, package name : " + pkgList[j]);
                                am.killBackgroundProcesses(pkgList[j]);
                                count++;
                            }
                        }
                    }
                }
  
                long afterMem = AndroidSystemInfo.GetAvailableMemory();
                
                Log.e(TAG, "clear " + count + " process, free " + (afterMem - beforeMem) + "M");
    		}
    	}).start();
    }
    
    // 定位相关
    
    private LocationClient mLocationClient = null;
    private BDLocationListener mLocationListener = null;
    
    public void initBaiduLocationSDK()
    {
    	if(mLocationClient == null)
    	{
    		Context ctx = getApplicationContext();
    		mLocationClient = new LocationClient(ctx);
    		mLocationListener = new MyLocationListener();
    		mLocationClient.registerLocationListener(mLocationListener);
    		
    		LocationClientOption option = new LocationClientOption();
			int locMode = LocationClientOption.NetWorkFirst;
			option.setPriority(locMode);
			option.setAddrType("all");
			option.setOpenGps(true);
    		mLocationClient.setLocOption(option);
    	}
    }
    
    public void startLocationClient()
    {
    	if(mLocationClient != null && !mLocationClient.isStarted())
    	{
    		GameApp.getApp().runOnGLThread(new Runnable(){
    			public void run() {
    				nativeClearLocations();
    			}
    		});
    		mLocationClient.start();
    	}
    }
    
    public void stopLocationClient()
    {
    	if(mLocationClient != null && mLocationClient.isStarted())
    	{
    		mLocationClient.stop();
    	}
    }
    
    public static void startDetectLocation()
    {
    	if(gameApp != null)
    	{
    		gameApp.startLocationClient();
    	}
    }
    
    public static void stopDetectLocation()
    {
    	if(gameApp != null)
    	{
    		gameApp.stopLocationClient();
    	}
    }
    
    public static native void nativeClearLocations();
    public static native void nativeAddLocation(double longitude, double latitude, String name);
    public static native void nativeFinishLocationDetect(boolean bSuccess);
	
	public static String getClipBoardText()
	{
		ClipData data = m_clipboardmanager.getPrimaryClip();
		if (data != null){
			if (data.getItemCount() > 0 ){
				Item item = data.getItemAt(0);
				return item.getText().toString();
			}
		}
			
		return "";
	}
}

class MyLocationListener implements BDLocationListener
{

	@Override
	public void onReceiveLocation(final BDLocation location)
	{
		GameApp.getApp().runOnGLThread(new Runnable(){
			public void run() {
				GameApp.stopDetectLocation();
				
				int locType = location.getLocType();
				
				boolean bSuccess = false;
				if(locType == BDLocation.TypeGpsLocation || locType == BDLocation.TypeNetWorkLocation || locType == BDLocation.TypeOffLineLocation || locType == BDLocation.TypeCacheLocation)
				{
					double longitude = location.getLongitude();
					double latitude = location.getLatitude();
					
					String province = location.getProvince();
					String city = location.getCity();
					String district = location.getDistrict();
					
					String name = "";
					
					if(province != null && province.length() > 0)
					{
						name = appendLocationName(name, province);
					}
					
					if(city != null && city.length() > 0 && province != null && !city.equals(province))
					{
						name = appendLocationName(name, city);
					}
					
					if(district != null && district.length() > 0)
					{
						name = appendLocationName(name, district);
					}
					
					GameApp.nativeAddLocation(longitude, latitude, name);
					bSuccess = true;
				}
				
				GameApp.nativeFinishLocationDetect(bSuccess);
			}
		});
	}
	
	private String appendLocationName(String oldName, String val)
	{
		String newName = oldName;
		if(newName.length() > 0)
		{
			newName += " . ";
		}
		newName += val;
		return newName;
	}
	
}
