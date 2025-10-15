/****************************************************************************
Copyright (c) 2014 Chukong Technologies Inc.

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 ****************************************************************************/

package org.cocos2dx.lib;

import java.lang.ref.WeakReference;

import org.cocos2dx.lib.Cocos2dxVideoView.OnVideoEventListener;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class Cocos2dxVideoHelper {

    private FrameLayout mLayout = null;
    private RelativeLayout mVideoLayout = null;
    private Cocos2dxActivity mActivity = null;
    private VideoActivity mVideoActivity = null;
    private SparseArray<Cocos2dxVideoView> sVideoViews = null;
    static VideoHandler mVideoHandler = null;
    static Cocos2dxVideoHelper mVideoHelper = null;
    
    public static Cocos2dxVideoHelper getInstance() 
    {
    	return mVideoHelper;
    }
    
    public Cocos2dxVideoHelper(Cocos2dxActivity activity,FrameLayout layout)
    {
    	mActivity = activity;
        mLayout = layout;
        
        mVideoHandler = new VideoHandler(this);
        sVideoViews = new SparseArray<Cocos2dxVideoView>();
        
        if (mVideoHelper == null)
        	mVideoHelper = this;
    }
    
    public void setVideoFrameLayout(VideoActivity activity, RelativeLayout layout) 
    {
    	Log.d("LJ", "Cocos2dxVideoHelper::setVideoFrameLayout()");
    	
    	if (mVideoActivity == null)
    		mVideoActivity = activity;
    	
    	if (mVideoLayout == null)
    		mVideoLayout = layout;
    	
    	if (mVideoLayout != null) {
    		for (int i = 0; i < sVideoViews.size(); i++) {
                int key = sVideoViews.keyAt(i);
                Cocos2dxVideoView videoView = sVideoViews.get(key);   
                if (videoView != null) {
                	if (mVideoLayout.findViewWithTag(key) == null) {
                    	mVideoLayout.addView(videoView);
                    	Log.d("LJ", "Cocos2dxVideoHelper::setVideoFrameLayout("+key+")");
                    	if (mVideoActivity != null)
                    		mVideoActivity.refreshButton();
                    }	
                }
    		}
    	}
    }
    
    public void unsetVideoFrameLayout(boolean notify)
    {
    	Log.d("LJ", "Cocos2dxVideoHelper::unsetVideoFrameLayout()");
    	
		for (int i = 0; i < sVideoViews.size(); i++) {
            int key = sVideoViews.keyAt(i);
            Cocos2dxVideoView videoView = sVideoViews.get(key);   
            if (videoView != null) {
            	videoView.stopPlayback();
            	if (mVideoLayout != null && mVideoLayout.findViewWithTag(key) != null) {
                	mVideoLayout.removeView(videoView);
                	if (notify && videoEventListener != null) {
                		videoEventListener.onVideoEvent(key,Cocos2dxVideoView.EVENT_TERMINATION);
                    }
                	Log.d("LJ", "Cocos2dxVideoHelper::unsetVideoFrameLayout("+key+")");
                }
            }
		}
		sVideoViews.clear();
		
    	mVideoLayout = null;
    	mVideoActivity = null;
    }
    
    public void onOrientationChanged(int orientation)
    {
    	Log.d("LJ", "Cocos2dxVideoHelper::onOrientationChanged("+orientation+")");
    	
    	for (int i = 0; i < sVideoViews.size(); i++) {
            int key = sVideoViews.keyAt(i);
            Cocos2dxVideoView videoView = sVideoViews.get(key);
            if (videoView != null) {
            	videoView.onOrientationChanged(orientation);
            }
    	}
    }
    
    public void onConfigurationChanged(Configuration newConfig)
    {
    	Log.d("LJ", "Cocos2dxVideoHelper::onConfigurationChanged("+newConfig.orientation+")");
    	
    	for (int i = 0; i < sVideoViews.size(); i++) {
            int key = sVideoViews.keyAt(i);
            Cocos2dxVideoView videoView = sVideoViews.get(key);
            if (videoView != null) {
            	videoView.onConfigurationChanged(newConfig);
            }
    	}
    }
    
    private static int videoTag = 0;
    private final static int VideoTaskCreate = 0;
    private final static int VideoTaskRemove = 1;
    private final static int VideoTaskSetSource = 2;
    private final static int VideoTaskSetRect = 3;
    private final static int VideoTaskStart = 4;
    private final static int VideoTaskPause = 5;
    private final static int VideoTaskResume = 6;
    private final static int VideoTaskStop = 7;
    private final static int VideoTaskSeek = 8;
    private final static int VideoTaskSetVisible = 9;
    private final static int VideoTaskRestart = 10;
    private final static int VideoTaskKeepRatio = 11;
    private final static int VideoTaskFullScreen = 12;
    private final static int VideoTaskCreateActivity = 13;
    private final static int VideoTaskDestroyActivity = 14;
    final static int KeyEventBack = 1000;
    
    static class VideoHandler extends Handler{
        WeakReference<Cocos2dxVideoHelper> mReference;
        
        VideoHandler(Cocos2dxVideoHelper helper){
            mReference = new WeakReference<Cocos2dxVideoHelper>(helper);
        }
        
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case VideoTaskCreateActivity: {
            	Cocos2dxVideoHelper helper = mReference.get();
                helper._createVideoActivity();
            	break;
            }
            case VideoTaskDestroyActivity: {
            	Cocos2dxVideoHelper helper = mReference.get();
                helper._destroyVideoActivity();
            	break;
            }
            case VideoTaskCreate: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper._createVideoView(msg.arg1);
                break;
            }
            case VideoTaskRemove: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper._removeVideoView(msg.arg1);
                break;
            }
            case VideoTaskSetSource: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper._setVideoURL(msg.arg1, msg.arg2, (String)msg.obj);
                break;
            }
            case VideoTaskStart: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper._startVideo(msg.arg1);
                break;
            }
            case VideoTaskSetRect: {
                Cocos2dxVideoHelper helper = mReference.get();
                Rect rect = (Rect)msg.obj;
                helper._setVideoRect(msg.arg1, rect.left, rect.top, rect.right, rect.bottom);
                break;
            }
            case VideoTaskFullScreen:{
                Cocos2dxVideoHelper helper = mReference.get();
                Rect rect = (Rect)msg.obj;
                if (msg.arg2 == 1) {
                    helper._setFullScreenEnabled(msg.arg1, true, rect.right, rect.bottom);
                } else {
                    helper._setFullScreenEnabled(msg.arg1, false, rect.right, rect.bottom);
                }
                break;
            }
            case VideoTaskPause: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper._pauseVideo(msg.arg1);
                break;
            }
            case VideoTaskResume: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper._resumeVideo(msg.arg1);
                break;
            }
            case VideoTaskStop: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper._stopVideo(msg.arg1);
                break;
            }
            case VideoTaskSeek: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper._seekVideoTo(msg.arg1, msg.arg2);
                break;
            }
            case VideoTaskSetVisible: {
                Cocos2dxVideoHelper helper = mReference.get();
                if (msg.arg2 == 1) {
                    helper._setVideoVisible(msg.arg1, true);
                } else {
                    helper._setVideoVisible(msg.arg1, false);
                }
                break;
            }
            case VideoTaskRestart: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper._restartVideo(msg.arg1);
                break;
            }
            case VideoTaskKeepRatio: {
                Cocos2dxVideoHelper helper = mReference.get();
                if (msg.arg2 == 1) {
                    helper._setVideoKeepRatio(msg.arg1, true);
                } else {
                    helper._setVideoKeepRatio(msg.arg1, false);
                }
                break;
            }
            case KeyEventBack: {
                Cocos2dxVideoHelper helper = mReference.get();
                helper.onBackKeyEvent();
                break;
            }
            default:
                break;
            }
            
            super.handleMessage(msg);
        }
    }
    
    private class VideoEventRunnable implements Runnable
    {
        private int mVideoTag;
        private int mVideoEvent;
        
        public VideoEventRunnable(int tag,int event) {
            mVideoTag = tag;
            mVideoEvent = event;
        }
        @Override
        public void run() {
        	
        	Log.d("LJ", "Cocos2dxVideoHelper::VideoEventRunnable::run("+mVideoTag+","+mVideoEvent+")");
        	
        	if (mVideoEvent == Cocos2dxVideoView.EVENT_COMPLETED) {
        		Cocos2dxVideoHelper.destroyVideoActivity();
        	}
        	
            nativeExecuteVideoCallback(mVideoTag, mVideoEvent);
        }
        
    }
    
    public static native void nativeExecuteVideoCallback(int index,int event);
    
    OnVideoEventListener videoEventListener = new OnVideoEventListener() {
        
        @Override
        public void onVideoEvent(int tag,int event) {
            mActivity.runOnGLThread(new VideoEventRunnable(tag, event));
        }
    };
    
    public static void createVideoActivity() {
    	Message msg = new Message();
        msg.what = VideoTaskCreateActivity;
        mVideoHandler.sendMessage(msg);
    }
    
    private void _createVideoActivity() {
    	Log.d("LJ", "Cocos2dxVideoHelper::_createVideoActivity()");
		Intent intent = new Intent();
		intent.setClass(mActivity, VideoActivity.class);
		mActivity.startActivity(intent);
		mActivity.overridePendingTransition(0, 0);
    }
    
    public static void destroyVideoActivity() {
    	Message msg = new Message();
        msg.what = VideoTaskDestroyActivity;
        mVideoHandler.sendMessage(msg);
    }
    
    public void _destroyVideoActivity() {
    	Log.d("LJ", "Cocos2dxVideoHelper::_destroyVideoActivity()");
    	if (mVideoActivity != null) {
    		mVideoActivity.finish();
    		mVideoActivity.overridePendingTransition(0, 0);
    		unsetVideoFrameLayout(false);
    		mVideoActivity = null;
    		mVideoLayout = null;
    	}
    }
    
    public static int createVideoWidget() {
        Message msg = new Message();
        msg.what = VideoTaskCreate;
        msg.arg1 = videoTag;
        mVideoHandler.sendMessage(msg);
        
        return videoTag++;
    }
    
    private void _createVideoView(int index) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_createVideoView()");
        Cocos2dxVideoView videoView = new Cocos2dxVideoView(mActivity,index);
        sVideoViews.put(index, videoView);
        FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        if (mVideoLayout != null) {
        	mVideoLayout.addView(videoView, lParams);
        	Log.d("LJ", "Cocos2dxVideoHelper::_createVideoView() mVideoLayout.addView");
        	if (mVideoActivity != null)
        		mVideoActivity.refreshButton();
        }
        videoView.setTag(index);
        // videoView.setZOrderOnTop(true);
        videoView.setOnCompletionListener(videoEventListener);
        
        // ycl 错误监听器
        videoView.setOnErrorListener(new OnErrorListener(){

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				
				for (int i = 0; i < sVideoViews.size(); i++)
		    	{
		    		Cocos2dxVideoView videoView = sVideoViews.valueAt(i);
		    		if(videoView != null)  // recreate Cocos2dxVideoView
		    		{
		    	        if(videoView.getMediaPlayer() == mp)
		    	        {
		    	        	int viewIndex = videoView.getViewTag();
		    	        	mActivity.runOnGLThread(new VideoEventRunnable(viewIndex, Cocos2dxVideoView.EVENT_COMPLETED));
		    	        	break;
		    	        }
		    		}
		    	}
				
				Cocos2dxVideoHelper.destroyVideoActivity();
				
				return true;
			}
        	
        });
    }
    
    public static void removeVideoWidget(int index){
        Message msg = new Message();
        msg.what = VideoTaskRemove;
        msg.arg1 = index;
        mVideoHandler.sendMessage(msg);
    }
    
    private void _removeVideoView(int index) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_removeVideoView()");
        Cocos2dxVideoView view = sVideoViews.get(index);
        if (view != null) {
            view.stopPlayback();
            sVideoViews.remove(index);
            
            if (mVideoLayout != null & mVideoLayout.findViewWithTag(index) != null) {
            	mVideoLayout.removeView(view);
            	Log.d("LJ", "Cocos2dxVideoHelper::_removeVideoView() mVideoLayout.removeView");
            }
        }
    }
    
    public static void setVideoUrl(int index, int videoSource, String videoUrl) {
        Message msg = new Message();
        msg.what = VideoTaskSetSource;
        msg.arg1 = index;
        msg.arg2 = videoSource;
        msg.obj = videoUrl;
        mVideoHandler.sendMessage(msg);
    }
    
    private void _setVideoURL(int index, int videoSource, String videoUrl) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_setVideoURL() videoUrl=" + videoUrl);
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            switch (videoSource) {
            case 0:
                videoView.setVideoFileName(videoUrl);
                break;
            case 1:
                videoView.setVideoURL(videoUrl);
                break;
            default:
                break;
            }
        }
    }
    
    public static void setVideoRect(int index, int left, int top, int maxWidth, int maxHeight) {
        Message msg = new Message();
        msg.what = VideoTaskSetRect;
        msg.arg1 = index;
        msg.obj = new Rect(left, top, maxWidth, maxHeight);
        mVideoHandler.sendMessage(msg);
    }
    
    private void _setVideoRect(int index, int left, int top, int maxWidth, int maxHeight) {
    	if(maxWidth == 0 || maxHeight == 0){
        	DisplayMetrics disMetrics = new DisplayMetrics();
        	try {
    			mActivity.getWindowManager().getDefaultDisplay().getMetrics(disMetrics);
    			maxWidth = disMetrics.widthPixels;
    			maxHeight = disMetrics.heightPixels;
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
    	Log.d("LJ", "Cocos2dxVideoHelper::_setVideoRect("+left+","+top+","+maxWidth+","+maxHeight+")");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            videoView.setVideoRect(left,top,maxWidth,maxHeight);
        }
    }
    
    public static void setFullScreenEnabled(int index, boolean enabled, int width, int height) {
        Message msg = new Message();
        msg.what = VideoTaskFullScreen;
        msg.arg1 = index;
        if (enabled) {
            msg.arg2 = 1;
        } else {
            msg.arg2 = 0;
        }
        msg.obj = new Rect(0, 0, width, height);
        mVideoHandler.sendMessage(msg);
    }
    
    private void _setFullScreenEnabled(int index, boolean enabled, int width,int height) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_setFullScreenEnabled()");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            videoView.setFullScreenEnabled(enabled, width, height);
        }
    }
    
    private void onBackKeyEvent() {
    	Log.d("LJ", "Cocos2dxVideoHelper::onBackKeyEvent()");
        int viewCount = sVideoViews.size();
        for (int i = 0; i < viewCount; i++) {
            int key = sVideoViews.keyAt(i);
            Cocos2dxVideoView videoView = sVideoViews.get(key);
            if (videoView != null) {
                videoView.setFullScreenEnabled(false, 0, 0);
                mActivity.runOnGLThread(new VideoEventRunnable(key, KeyEventBack));
            }
        }
    }
    
    public static void startVideo(int index) {
        Message msg = new Message();
        msg.what = VideoTaskStart;
        msg.arg1 = index;
        mVideoHandler.sendMessage(msg);
    }
    
    private void _startVideo(int index) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_startVideo()");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            videoView.start();
        }
    }
    
    public static void pauseVideo(int index) {
        Message msg = new Message();
        msg.what = VideoTaskPause;
        msg.arg1 = index;
        mVideoHandler.sendMessage(msg);
    }
    
    private void _pauseVideo(int index) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_pauseVideo()");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            videoView.pause();
        }
    }

    public static void resumeVideo(int index) {
        Message msg = new Message();
        msg.what = VideoTaskResume;
        msg.arg1 = index;
        mVideoHandler.sendMessage(msg);
    }
    
    private void _resumeVideo(int index) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_resumeVideo()");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            videoView.resume();
        }
    }
    
    public static void stopVideo(int index) {
        Message msg = new Message();
        msg.what = VideoTaskStop;
        msg.arg1 = index;
        mVideoHandler.sendMessage(msg);
    }
    
    private void _stopVideo(int index) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_stopVideo()");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            videoView.stop();
        }
    }
    
    public static void restartVideo(int index) {
        Message msg = new Message();
        msg.what = VideoTaskRestart;
        msg.arg1 = index;
        mVideoHandler.sendMessage(msg);
    }
    
    private void _restartVideo(int index) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_restartVideo()");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            videoView.restart();
        }
    }
    
    public static void seekVideoTo(int index,int msec) {
        Message msg = new Message();
        msg.what = VideoTaskSeek;
        msg.arg1 = index;
        msg.arg2 = msec;
        mVideoHandler.sendMessage(msg);
    }
    
    private void _seekVideoTo(int index,int msec) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_seekVideoTo()");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            videoView.seekTo(msec);
        }
    }
    
    public static void setVideoVisible(int index, boolean visible) {
        Message msg = new Message();
        msg.what = VideoTaskSetVisible;
        msg.arg1 = index;
        if (visible) {
            msg.arg2 = 1;
        } else {
            msg.arg2 = 0;
        }
        
        mVideoHandler.sendMessage(msg);
    }
    
    private void _setVideoVisible(int index, boolean visible) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_setVideoVisible()");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            if (visible) {
                videoView.fixSize();
                videoView.setVisibility(View.VISIBLE);
            } else {
                videoView.setVisibility(View.INVISIBLE);
            }
        }
    }
    
    public static void setVideoKeepRatioEnabled(int index, boolean enable) {
        Message msg = new Message();
        msg.what = VideoTaskKeepRatio;
        msg.arg1 = index;
        if (enable) {
            msg.arg2 = 1;
        } else {
            msg.arg2 = 0;
        }
        mVideoHandler.sendMessage(msg);
    }
    
    private void _setVideoKeepRatio(int index, boolean enable) {
    	Log.d("LJ", "Cocos2dxVideoHelper::_setVideoKeepRatio()");
        Cocos2dxVideoView videoView = sVideoViews.get(index);
        if (videoView != null) {
            videoView.setKeepRatio(enable);
        }
    }
    
    public void onAppActive(){
    	Log.d("LJ", "Cocos2dxVideoHelper.onAppActive");
    	
    	for (int i = 0; i < sVideoViews.size(); i++)
    	{
    		Cocos2dxVideoView videoView = sVideoViews.valueAt(i);
    		if(videoView != null)  // recreate Cocos2dxVideoView
    		{
    	        videoView.resume();
    		}
    	}
    }
    
    public void onAppDeactive()
    {
    	Log.d("LJ", "Cocos2dxVideoHelper.onAppDeactive");
    	
    	for (int i = 0; i < sVideoViews.size(); i++)
    	{
    		Cocos2dxVideoView videoView = sVideoViews.valueAt(i);
    		if(videoView != null)
    		{
    			videoView.pause();
    			videoView.backupSeekPosition();
    		}
    	}
    }
}
