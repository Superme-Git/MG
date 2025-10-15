package com.locojoy.sdk;

import java.util.HashMap;
import java.util.Map;

import com.locojoy.mini.mt3.GameApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

public class GameSDK {

	private static Activity _gameActivity = null;
	
	private static LocojoySDK sdk = new LocojoySDK();
	
	private static SdkListener listener = new SdkListener() {
		
		@Override
		public void onCallBack(final int code, final String msg) {
			// TODO Auto-generated method stub
    		GameApp.getApp().runOnGLThread(new Runnable(){
    			public void run() {
					switch (code) {
					case SdkListener.RET_INIT_SUCCESS:
						initCb(1, sdk.getChannel());
						break;
					case SdkListener.RET_INIT_FAIL:
						initCb(0, sdk.getChannel());
						break;
					case SdkListener.RET_LOGIN_NO_NEED:
						break;
					case SdkListener.RET_LOGIN_SUCCESS:
						String strDeviceid = "";
						loginCb(1, sdk.getChannel(), sdk.getUserID(), sdk.getSession(), strDeviceid);
						break;
					case SdkListener.RET_LOGIN_FAIL:
						loginCb(0, sdk.getChannel(), "", "", "");
						break;
					case SdkListener.RET_LOGOUT_SUCCESS:
						logoutCb(1);
						break;
					case SdkListener.RET_LOGIN_SUCCESS_YYB:
						String plat = sdk.getPlatformName();
						YYBLoginCb(1, sdk.getChannel(), sdk.getUserID(), sdk.getSession(), sdk.getOpenId(), sdk.getOpenKey(), sdk.getpayToken(plat), sdk.getpf(), sdk.getpfKey(), sdk.getZoneid(), plat);
						break;
					case SdkListener.RET_LOGIN_FAIL_YYB:
						String platform = sdk.getPlatformName();
						YYBLoginCb(0, sdk.getChannel(), "", "", "", "", "", "", "", msg, platform);
						break;
					case SdkListener.RET_LOGOUT_FAIL:
						logoutCb(0);
						break;
					case SdkListener.RET_ACCOUNTSWITCH_SUCCESS:
						switchAccountCb(1, sdk.getChannel(), sdk.getUserID(), sdk.getSession());
						break;
					case SdkListener.RET_ACCOUNTSWITCH_FAIL:
						switchAccountCb(0, sdk.getChannel(), "", "");
						break;
					case SdkListener.RET_EXIT:
						exitSdkCb();
						break;
						
					case SdkListener.RET_PAY_SUCCESS:
						payCb(1, sdk.getOrder());
						break;
					case SdkListener.RET_PAY_FAIL:
						payCb(0, sdk.getOrder());
						break;
					}
    			}
    		});
		}
	};
	
	/**
     * 向SDK设置游戏的当前Activity，如果游戏有多个Activity，对于需要调用本类中的方法的Activity，在调用方法前都应该调用此方法设置当前的Activity。
     * 注意：此方法须从Java中调用的，不应从C++中调用。
     */
    public static void setCurrentActivity(Activity gameActivity)
    {
        _gameActivity = gameActivity;
        sdk.setCurrentActivity(_gameActivity);
    }
    
    // 用于检查渠道是否支持logout、switchAccount、enterPlatform、showToolBar、hideToolBar
    public static int isFunctionSupported(String func) {
    	if (sdk.isFunctionSupported(func))
    		return 1;
    	return 0;
    }
	
    /**
     * 注意：此方法必须从Java中调用，不应从c++调用。
     */
	public static void init()
	{
		sdk.setSdkListener(listener);
		sdk.init();
	}
	
	public static void login()
	{
		_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sdk.login();
			}
		});
	}
	
	public static void wxLogin()
	{
		_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sdk.wxLogin();
			}
		});
	}
	
	public static void logout()
	{
		_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sdk.logout();
			}
		});
	}
	
	public static void switchAccount() {
		_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sdk.switchAccount();
			}
		});
	}
	
	public static void exitSdk()
	{
		_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (sdk.isFunctionSupported("exit"))
					sdk.exit();
				else
					showExitDialog();
			}
		});
	}
	
	public static void enterPlatform() {
		_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sdk.enterPlatform();
			}
		});
	}
	
	public static void pay(final String pid, final String pname, final String pdesc, final int pprice, 
			final int pcount, final int prate, final String uid, final String uname, final int ulevel, final int ubalance, final String serverId, final String ext)
    {
		_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Map<String, String> req = new HashMap<String, String>();
				req.put(LdSdk.PRODUCT_ID, pid);
				req.put(LdSdk.PRODUCT_NAME, pname);
				req.put(LdSdk.PRODUCT_DESC, pdesc);
				req.put(LdSdk.PRODUCT_PRICE, pprice + "");
				req.put(LdSdk.PRODUCT_COUNT, pcount + "");
				req.put(LdSdk.PRODUCT_RATE, prate + "");
				req.put(LdSdk.ROLE_ID, uid);
				req.put(LdSdk.ROLE_NAME, uname);
				req.put(LdSdk.ROLE_GRADE, ulevel + "");
				req.put(LdSdk.ROLE_BALANCE, ubalance + "");
				req.put(LdSdk.SERVER_ID, serverId);
				req.put(LdSdk.EXT, ext);
				sdk.pay(req);
			}
		});
    }
	
	public static void showToolBar(final int place) {
		_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sdk.showToolBar(place);
			}
		});
	}
	
	public static void hideToolBar() {
		_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sdk.hideToolBar();
			}
		});
	}
    
    public static void createRoleStat(String roleId, String roleName, String roleLevel, String serverId, String serverName, String roleTime)
    {
    	Map<String, String> inf = new HashMap<String, String>();
    	inf.put(LdSdk.ROLE_ID, roleId);
    	inf.put(LdSdk.ROLE_NAME, roleName);
    	inf.put(LdSdk.ROLE_LEVEL, roleLevel);
    	inf.put(LdSdk.SERVER_ID, serverId);
    	inf.put(LdSdk.SERVER_NAME, serverName);
		inf.put(LdSdk.ROLE_TIME, roleTime);
    	sdk.createRole(inf);
    }
    
    public static void enterGameStat(String roleId, String roleName, String roleLevel, String serverId, String serverName, String roleTime)
    {
    	Map<String, String> inf = new HashMap<String, String>();
    	inf.put(LdSdk.ROLE_ID, roleId);
    	inf.put(LdSdk.ROLE_NAME, roleName);
    	inf.put(LdSdk.ROLE_LEVEL, roleLevel);
    	inf.put(LdSdk.SERVER_ID, serverId);
    	inf.put(LdSdk.SERVER_NAME, serverName);
		inf.put(LdSdk.ROLE_TIME, roleTime);
    	sdk.enterGame(inf);
    }
    
    public static void recordRoleInf(String roleId, String roleName, String roleLevel, String serverId, String serverName, String roleTime)
    {
    	Map<String, String> inf = new HashMap<String, String>();
    	inf.put(LdSdk.ROLE_ID, roleId);
    	inf.put(LdSdk.ROLE_NAME, roleName);
    	inf.put(LdSdk.ROLE_LEVEL, roleLevel);
    	inf.put(LdSdk.SERVER_ID, serverId);
    	inf.put(LdSdk.SERVER_NAME, serverName);
		inf.put(LdSdk.ROLE_TIME, roleTime);
    	sdk.recordRoleInf(inf);
    }
    
    public static void onActivityResult(int requestCode, int resultCode, Intent data)
	{
    	sdk.onActivityResult(requestCode, resultCode, data);
	}
	
	public static void onNewIntent(Intent intent)
	{
		sdk.onNewIntent(intent);
	}
	
	public static void onWindowFocusChanged(boolean hasFocus)
    {
		sdk.onWindowFocusChanged(hasFocus);
    }
	
	public static void onResume()
	{
		try {
			sdk.onResume();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void onPause()
	{
		sdk.onPause();
	}
	
	public static void onStart()
	{
		sdk.onStart();
	}
	
	public static void onStop()
	{
		sdk.onStop();
	}
	
	public static void onRestart()
	{
		sdk.onRestart();
	}
	
	public static void onDestroy()
	{
		sdk.destroy();
		sdk = null;
		_gameActivity = null;
	}
	
	public static native void initCb(int flags, String channel);
	public static native void loginCb(int flags, String channel, String userId, String session, String deviceid);
	public static native void YYBLoginCb(int flags, String channel, String userId, String session, String openId, String openKey, String payToken, String pf, String pfKey, String zoneId, String platformName);
    public static native void logoutCb(int flags);
    public static native void switchAccountCb(int flags, String channel, String userId, String session);
    public static native void payCb(int flags, String orderId);
    public static native void exitSdkCb();
    
    private static void showExitDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(_gameActivity);
		builder.setTitle("提示");
		builder.setMessage("确定退出吗?");
		builder.setPositiveButton("确   定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				exitSdkCb();
			}
		});
		
		builder.setNegativeButton("取  消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				
			}
		});
		Dialog notice = builder.create();
		notice.setCancelable(false);
		notice.setCanceledOnTouchOutside(false);
		notice.show();
	}
    
    public static void showDialog(final String title, final String content) {
    	
    	_gameActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(_gameActivity);
				builder.setTitle(title);
				builder.setMessage(content);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				
				Dialog notice = builder.create();
				notice.show();
			}
		});
    }
}
