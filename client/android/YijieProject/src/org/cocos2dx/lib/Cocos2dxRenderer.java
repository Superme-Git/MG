/****************************************************************************
Copyright (c) 2010-2011 cocos2d-x.org

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

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.util.Log;

public class Cocos2dxRenderer implements GLSurfaceView.Renderer {
	private final static long NANOSECONDSPERSECOND = 1000000000L;
	private final static long NANOSECONDSPERMINISECOND = 1000000;
	private static long animationInterval = (long)(1.0 / 60 * NANOSECONDSPERSECOND);
	private long last;
	private int screenWidth;
	private int screenHeight;
	
	private static boolean isInBackground = false;
	
	public static boolean IsInBackground() { return isInBackground;}
	public static void SetInBackground(boolean flag) {  isInBackground = flag;}
	
    public void onSurfaceCreated(GL10 gl, EGLConfig config) { 
    	nativeInit(screenWidth, screenHeight); 
    	last = System.nanoTime();
    }
    
    public void setScreenWidthAndHeight(int w, int h){
    	this.screenWidth = w;
    	this.screenHeight = h;
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {  	
    }
    
    public void onDrawFrame(GL10 gl) {
    	
    	long now = System.nanoTime();
    	long interval = now - last;
    	
    	// should render a frame when onDrawFrame() is called
    	// or there is a "ghost"
    	nativeRender();   	
   	
    	// fps controlling
    	if (interval < animationInterval){ 
    		try {
    			// because we render it before, so we should sleep twice time interval
    			Thread.sleep((animationInterval - interval) * 2 / NANOSECONDSPERMINISECOND);
    		} catch (Exception e){}
    	}	
    	
    	last = now;
    }
    
    public void handleActionDown(int id, float x, float y)
    {
    	nativeTouchesBegin(id, x, y);
    }
    
    public void handleActionUp(int id, float x, float y)
    {
    	nativeTouchesEnd(id, x, y);
    }
    
    public void handleActionCancel(int[] id, float[] x, float[] y)
    {
    	nativeTouchesCancel(id, x, y);
    }
    
    public void handleActionMove(int[] id, float[] x, float[] y)
    {
    	nativeTouchesMove(id, x, y);
    }
    
    public void handleKeyDown(int keyCode)
    {
    	nativeKeyDown(keyCode);
    }
    
    public void handleOnPause(){
    	nativeOnPause();
    }
    
    public void handleOnResume(){
    	nativeOnResume();
    }
    
    public void handleMemoryWarning() {
    	nativeNotifyMemoryWarning();
    }
    
    public static void setAnimationInterval(double interval){
    	animationInterval = (long)(interval * NANOSECONDSPERSECOND);
    }
    private static native void nativeTouchesBegin(int id, float x, float y);
    private static native void nativeTouchesEnd(int id, float x, float y);
    private static native void nativeTouchesMove(int[] id, float[] x, float[] y);
    private static native void nativeTouchesCancel(int[] id, float[] x, float[] y);
    private static native boolean nativeKeyDown(int keyCode);
    private static native void nativeRender();
    private static native void nativeInit(int w, int h);
    private static native void nativeOnPause();
    private static native void nativeOnResume();
	private static native void nativeNotifyMemoryWarning();
    
    /////////////////////////////////////////////////////////////////////////////////
    // swapbuffers
    /////////////////////////////////////////////////////////////////////////////////
    
    static public void  swapBuffers()
    {
    	if(IsInBackground())
    		return;
    	try{
        	EGL10 curEgl = (EGL10)EGLContext.getEGL();
        	if(curEgl == null){
        		return;
        	}
        	EGLDisplay curDisplay = curEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        	if(curDisplay == EGL10.EGL_NO_DISPLAY)
        	{
        		Log.e("Cocos2dxRender", "No default display");
        		return;
        	}
        	
        	EGLSurface curSurface = curEgl.eglGetCurrentSurface(EGL10.EGL_READ);
        	if(curSurface == EGL10.EGL_NO_SURFACE)
        	{
        		Log.e("Cocos2dxRender", "No default surface");
        		return;
        	}
        	
        	curEgl.eglSwapBuffers(curDisplay, curSurface);
    	}    
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    /////////////////////////////////////////////////////////////////////////////////
    // handle input method edit message
    /////////////////////////////////////////////////////////////////////////////////
    
    public void handleInsertText(final String text) {
    	nativeInsertText(text);
    }
    
    public void handleDeleteBackward() {
    	nativeDeleteBackward();
    }

	public String getContentText() {
		return nativeGetContentText();
	}
	
    private static native void nativeInsertText(String text);
    private static native void nativeDeleteBackward();
    private static native String nativeGetContentText();
}
