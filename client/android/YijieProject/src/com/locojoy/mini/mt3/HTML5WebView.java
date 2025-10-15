package com.locojoy.mini.mt3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.locojoy.wojmt3.yj.R;

public class HTML5WebView extends WebView {
	
	private Context 							mContext;
	private MyWebChromeClient					mWebChromeClient;
	private View								mCustomView;
	private FrameLayout							mCustomViewContainer;
	private WebChromeClient.CustomViewCallback 	mCustomViewCallback;
	
	private FrameLayout							mContentView;
	private FrameLayout							mBrowserFrameLayout;
	private FrameLayout							mLayout;
	
	private Activity							mActivity;
    static final String							LOGTAG = "HTML5WebView";
    
    public boolean								mGoneBecauseHideWebview = false;
	
	static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
        new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    
    private static Handler mHandler = new Handler();

	@SuppressLint({ "SetJavaScriptEnabled", "ResourceAsColor" })
	private void init(Context context, int webViewWidth, int webViewHeight) {
		mContext = context;
		mActivity = (Activity) mContext;
		
		mBrowserFrameLayout = (FrameLayout) LayoutInflater.from(mActivity).inflate(R.layout.webview, null);
		mCustomViewContainer = (FrameLayout) mBrowserFrameLayout.findViewById(R.id.custom_content);
		mContentView = (FrameLayout) mBrowserFrameLayout.findViewById(R.id.main_content);
		
	    mContentView.addView(this);
		
		mLayout = new FrameLayout(context);
		mLayout.addView(mBrowserFrameLayout, COVER_SCREEN_PARAMS);
		
    	FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(webViewWidth, webViewHeight);
    	lytp.gravity = Gravity.CENTER;
    	mActivity.addContentView(mLayout, lytp);
	    
	    setBackgroundColor(0x00000000);
	    
	    this.setVisibility(View.GONE);

    	mWebChromeClient = new MyWebChromeClient();
	    setWebChromeClient(mWebChromeClient);
	    
	    setWebViewClient(new MyWebViewClient());
	    this.setVerticalScrollBarEnabled(false);
	    this.setHorizontalScrollBarEnabled(false);
	       
	    WebSettings s = getSettings();
	    s.setBuiltInZoomControls(true);
	    s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
	    s.setUseWideViewPort(true);
	    s.setLoadWithOverviewMode(true);
	    s.setSaveFormData(true);
	    s.setJavaScriptEnabled(true);
	}
	
	public HTML5WebView(Context context) {
		super(context);
	}

	public HTML5WebView(Context context, int webViewWidth, int webViewHeight) {
		super(context);
		init(context, webViewWidth, webViewHeight);
	}
	
	public FrameLayout getLayout() {
		return mLayout;
	}
	
    public boolean inCustomView() {
		return (mCustomView != null);
	}
    
    public void hideCustomView() {
		mWebChromeClient.onHideCustomView();
	}
    
    @Override
    public void destroy() {
    	ViewGroup vg = (ViewGroup)mLayout.getParent();
    	if (vg != null) {
    		vg.removeView(mLayout);
    	}
    	super.destroy();
    }

    private class MyWebChromeClient extends WebChromeClient {
    	@Override
		public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
		{
			Log.e("HTML5WebView", "MyWebChromeClient onShowCustomView()");
	        HTML5WebView.this.setVisibility(View.GONE);
	        
	        if (mCustomView != null) {
	            callback.onCustomViewHidden();
	            return;
	        }
	        
	        mCustomViewContainer.addView(view);
	        mCustomView = view;
	        mCustomViewCallback = callback;
	        mCustomViewContainer.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onHideCustomView()
		{
			Log.e("HTML5WebView", "MyWebChromeClient onHideCustomView()");
			if (mCustomView == null)
				return;	       
			
			mCustomView.setVisibility(View.GONE);
			
			mCustomViewContainer.removeView(mCustomView);
			mCustomView = null;
			mCustomViewContainer.setVisibility(View.GONE);
			mCustomViewCallback.onCustomViewHidden();
			
			HTML5WebView.this.setVisibility(View.VISIBLE);
		}
    	
    	 @Override
         public void onReceivedTitle(WebView view, String title) {
            ((Activity) mContext).setTitle(title);
         }

         @Override
         public void onProgressChanged(WebView view, int newProgress) {
        	 ((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress*100);
         }
    }
	
	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	Log.e("HTML5WebView", "shouldOverrideUrlLoading url = " + url);
	        //view.loadUrl(url);
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			GameApp.getApp().startActivity(it);
	        return true;
	    }

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			Log.e("HTML5WebView", "onPageStarted url = " + url);
			
			mHandler.postDelayed(new Thread() {
				@Override
				public void run() {
					if (HTML5WebView.this != null && !mGoneBecauseHideWebview) {
						HTML5WebView.this.setVisibility(View.VISIBLE);
					}
				}
			}, 1000);
		}
		
		@Override
		public void onPageFinished(WebView view, String url)
		{
			Log.e("HTML5WebView", "onPageFinished url = " + url);
		}
	}
}
