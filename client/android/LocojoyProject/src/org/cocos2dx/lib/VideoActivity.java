package org.cocos2dx.lib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.Button;
import android.widget.RelativeLayout;

//import com.locojoy.loading.R;

@SuppressLint("HandlerLeak")
public class VideoActivity extends Activity {

	public final static int ORIENTATION_EVENT = 0X4321;
	public final static int APP_ACTIVE_EVENT = 0x4322;
	public final static int APP_DEACTIVE_EVENT = 0x4323;
	
	protected RelativeLayout relativelayout = null;
	protected Button button = null;
	// 手机方向监听
    OrientationEventListener mOrientationListener;
    
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
//			case ORIENTATION_EVENT:
//				Log.d("LJ", "VideoActivity::setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)");
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);;
//				break;
			case APP_ACTIVE_EVENT:
				Log.d("LJ",	"VideoActivity handle APP_ACTIVE_EVENT");
				Cocos2dxVideoHelper.getInstance().onAppActive();
				break;
				
			case APP_DEACTIVE_EVENT:
				Log.d("LJ",	"VideoActivity handle APP_DEACTIVE_EVENT");
				Cocos2dxVideoHelper.getInstance().onAppDeactive();
				break;
				
			default:
				break;
			}
		};
	};
	
	boolean mbPaused = false;
	
	// private OrientationEventListener mOrientationEventListener; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("LJ", "VideoActivity::onCreate");
		
		// FrameLayout
		RelativeLayout.LayoutParams framelayout_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		
		relativelayout = new RelativeLayout(this);
		relativelayout.setLayoutParams(framelayout_params);
		setContentView(relativelayout);
		
		if (Cocos2dxVideoHelper.getInstance() != null)
			Cocos2dxVideoHelper.getInstance().setVideoFrameLayout(this, relativelayout);
		
		mbPaused = false;
		
		// 侦听手机方向变化
		mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL)
		{
		   	@Override
		   	public void onOrientationChanged(int orientation) {
		    	//Log.d("LJ", String.format("onOrientationChanged orientation=%d", orientation));
		
		    	if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
		        	//Log.d("LJ", "手机平放");
		    	} else if (orientation < 10 || orientation > 350) {
		        	//Log.d("LJ", "手机顶部向上");
		    	} else if (orientation < 100 && orientation > 80) {
		        	//Log.d("LJ", "手机右边向上");
		        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		    	} else if (orientation < 190 && orientation > 170) {
		        	//Log.d("LJ", "手机底部向上");
		    	} else if (orientation < 280 && orientation > 260) {
		        	//Log.d("LJ", "手机左边向上");
		        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		    	} else {
		        	//Log.d("LJ", "不需要处理的方向");
		    	}
		   	}
		};
		   // 若可以侦听，则开启侦听，否则关闭
		if (mOrientationListener.canDetectOrientation()) {
			//Log.i("GameApp", "Can detect orientation");
			mOrientationListener.enable();
		} else {
			//Log.i("GameApp", "Cannot detect orientation");
			mOrientationListener.disable();
		}
	}
	
	public void refreshButton() {
		
//		if (relativelayout == null)
//			return;
//		
//		Log.d("LJ", "VideoActivity::refreshButton");
//		
//		if (button == null) {
//			
//			RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
//					RelativeLayout.LayoutParams.WRAP_CONTENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT);
//			lParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//			lParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//	        lParams.bottomMargin = 30;
//	        lParams.rightMargin = 0;
//	        
//	        button = new Button(this);
//	        button.setTag("abc");
//	        button.setLayoutParams(lParams);
//	        button.setWidth(240);
//	        button.setHeight(80);
//	        button.setText(R.string.skipvideo);
//	        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24);
//			button.setTextColor(Color.argb(125,246,213,139));
//			button.setBackgroundResource(R.drawable.clear);
//			button.setOnClickListener( new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Log.d("LJ", "VideoActivity::Button::onClick()");
//					if (Cocos2dxVideoHelper.getInstance() != null)
//						Cocos2dxVideoHelper.getInstance().unsetVideoFrameLayout(true);
//					finish();
//				}
//			});
//		}
//		
//		if (relativelayout.findViewWithTag("abc") == null) {
//			relativelayout.addView(button);
//		}
//		
//		button.bringToFront();
	}
	
	@Override
	protected void onResume() {
	
		Log.d("LJ", "VideoActivity::onResume");
		
		super.onResume();
		
		if(mbPaused)
		{
			handler.sendEmptyMessageDelayed(APP_ACTIVE_EVENT, 1000);
		}
		mbPaused = false;
	}
	
	@Override
	protected void onPause() {
		Log.d("LJ", "VideoActivity.onPause");
		
		super.onPause();
		
		Cocos2dxVideoHelper.getInstance().onAppDeactive();
		
		mbPaused = true;
	}

	@Override
	protected void onDestroy() {

		Log.d("LJ", "VideoActivity::onDestroy");
		
		super.onDestroy();
		
		// 关闭游戏时，关闭侦听
		mOrientationListener.disable();
	}
	
	@Override
	public void onBackPressed() {

		Log.d("LJ", "VideoActivity::onBackPressed");
		
		if (Cocos2dxVideoHelper.getInstance() != null)
			Cocos2dxVideoHelper.getInstance().unsetVideoFrameLayout(true);
		
		super.onBackPressed();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

//	    Log.d("LJ", "VideoActivity::onConfigurationChanged("+newConfig.orientation+")");
//	    
//	    // Checks the orientation of the screen
//	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//	        // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//	        // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//	    }
//	    
//	    if (Cocos2dxVideoHelper.getInstance() != null)
//			Cocos2dxVideoHelper.getInstance().onConfigurationChanged(newConfig);
	}
}
