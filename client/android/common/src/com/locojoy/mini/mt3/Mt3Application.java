package com.locojoy.mini.mt3;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
public class Mt3Application extends Application {
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		Log.e("Mt3Application", "attachBaseContext .......");
		MultiDex.install(this);
	}
}
