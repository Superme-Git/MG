package com.locojoy.mini.mt3;

import org.cocos2dx.lib.Cocos2dxActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class SystemUIUtil {

	static Cocos2dxActivity mt3 = null;

	public static void setActivity(Cocos2dxActivity _mt3) {
		mt3 = _mt3;
	}

	public static void showDefaultAlert(String p_sMessage) {
		//Cocos2dxActivity.showMessageBox("", p_sMessage);
	}

	public static void openUrl(String url) {
		Uri l_uri = Uri.parse(url);
		Intent l_it = new Intent(Intent.ACTION_VIEW, l_uri);
		mt3.startActivity(l_it);
	}

	public static boolean isWifiEnabled() {
		Context l_contex = mt3.getApplicationContext();
		if (l_contex != null) {
			ConnectivityManager l_CM = (ConnectivityManager) l_contex.getSystemService(Context.CONNECTIVITY_SERVICE);
			TelephonyManager l_telManager = (TelephonyManager) l_contex.getSystemService(Context.TELEPHONY_SERVICE);

			return ((l_CM.getActiveNetworkInfo() != null) && (l_CM.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || (l_telManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS));
		}
		return false;
	}

	public static boolean is3GEnabled() {
		Context l_contex = mt3.getApplicationContext();
		if (l_contex != null) {
			ConnectivityManager l_CM = (ConnectivityManager) l_contex.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo l_NetworkInfo = l_CM.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			// getActiveNetworkInfo();
			if (l_NetworkInfo != null) {
				return l_NetworkInfo.isAvailable();
			}
		}

		return false;
	}

	public static String getTimeStr(long p_lCurrentTime) {
		return DateUtil.getStringFormat2Second(p_lCurrentTime);
	}

	public static String getVersionStr() {
		String l_version = "";
		try {
			PackageManager l_pManager = mt3.getApplicationContext().getPackageManager();
			PackageInfo l_pInfo = l_pManager.getPackageInfo(mt3.getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
			if (l_pInfo != null && !TextUtils.isEmpty(l_pInfo.versionName)) {
				l_version = l_pInfo.versionName;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return l_version;
	}

	public static void showDialog(String title, String message, String yesStr, OnClickListener yesListener, String noStr, OnClickListener noListener, Boolean haveCancelButton) {
		Builder dialogBuilder = new AlertDialog.Builder(mt3);
		dialogBuilder.setTitle(title);
		dialogBuilder.setMessage(message);
		dialogBuilder.setPositiveButton(yesStr, yesListener);
		dialogBuilder.setNegativeButton(noStr, noListener);
		dialogBuilder.setCancelable(haveCancelButton);
		Dialog dialog = dialogBuilder.create();
		dialog.show();
	}
}
