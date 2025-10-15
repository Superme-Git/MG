package com.locojoy.mini.mt3;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
public class Mt3Application extends com.snowfish.cn.ganga.helper.SFOnlineApplication{
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		Log.e("Mt3Application", "attachBaseContext .......");
		MultiDex.install(this);
	}
}
