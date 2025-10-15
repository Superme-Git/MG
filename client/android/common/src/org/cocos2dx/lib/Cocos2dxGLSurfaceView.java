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

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.locojoy.mini.mt3.GameApp;
import com.locojoy.sdk.GameSDK;

class TextInputWraper implements TextWatcher, OnEditorActionListener {
	
	private Cocos2dxGLSurfaceView mMainView;
	private String mText = "";
	
	private Boolean isFullScreenEdit() {
		InputMethodManager imm = (InputMethodManager)mMainView.getTextField().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isFullscreenMode();
	}

	public TextInputWraper(Cocos2dxGLSurfaceView view) {
		mMainView = view;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		if (isFullScreenEdit()) {
			return;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mText = s.toString();
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			if (mText.isEmpty()) { mText = " "; }
			mMainView.clearText();
			mMainView.insertText(mText);
			mMainView.finishImeInput();
		}
		return false;
	}
}

@SuppressLint("HandlerLeak")
public class Cocos2dxGLSurfaceView extends GLSurfaceView {
    
    static private Cocos2dxGLSurfaceView mainView;

    private Cocos2dxRenderer mRenderer;
	
    ///////////////////////////////////////////////////////////////////////////
    // for initialize
	///////////////////////////////////////////////////////////////////////////
    public Cocos2dxGLSurfaceView(Context context) {
        super(context);
        initView();
    }

    public Cocos2dxGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    
    public void setCocos2dxRenderer(Cocos2dxRenderer renderer){
    	mRenderer = renderer;
    	setRenderer(mRenderer);
    }

    protected void initView() {   
        setFocusableInTouchMode(true);
        
        textInputWraper = new TextInputWraper(this);

        Cocos2dxGLSurfaceView.handler = new Handler(){
        	public void handleMessage(Message msg){
        		switch(msg.what){
        		case HANDLER_OPEN_IME_KEYBOARD:
        			if (mTextField != null && mCancelBtn != null && mConfirmBtn != null) {
                        mTextField.setVisibility(View.VISIBLE);
                        mCancelBtn.setVisibility(View.VISIBLE);
                        mConfirmBtn.setVisibility(View.VISIBLE);
            			if (mTextField.requestFocus()) {
            				mTextField.removeTextChangedListener(textInputWraper);
            				mTextField.setText("");
            				String text = (String)msg.obj;
            				mTextField.append(text);
            				mOriginText = text;
            				mTextField.addTextChangedListener(textInputWraper);
                            InputMethodManager imm = (InputMethodManager)mainView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(mTextField, 0);
            			}
        			}
        			break;
        			
        		case HANDLER_CLOSE_IME_KEYBOARD:
        			if (null != mTextField && mCancelBtn != null && mConfirmBtn != null) {
        				mTextField.removeTextChangedListener(textInputWraper);
                        InputMethodManager imm = (InputMethodManager)mainView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mTextField.getWindowToken(), 0);
                        Cocos2dxGLSurfaceView.this.requestFocus();
                        mTextField.setVisibility(View.INVISIBLE);
                        mCancelBtn.setVisibility(View.INVISIBLE);
                        mConfirmBtn.setVisibility(View.INVISIBLE);
        			}
        			break;
        		}
        	}
        };
        mainView = this;        
    }
    
    public void onPause(){
    	if(mRenderer != null)
    		Cocos2dxRenderer.SetInBackground(true);
    	queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.handleOnPause();
            }
        });
    	
    	this.setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
    
    public void onResume(){
    	
    	if(mRenderer != null)
        		Cocos2dxRenderer.SetInBackground(false);
    	
    	super.onResume();
    	this.setRenderMode(RENDERMODE_CONTINUOUSLY);
    	
    	queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.handleOnResume();
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // for text input
	///////////////////////////////////////////////////////////////////////////
    private final static int HANDLER_OPEN_IME_KEYBOARD = 2;
    private final static int HANDLER_CLOSE_IME_KEYBOARD = 3;
    private static Handler handler;
    private static TextInputWraper textInputWraper;
    private Cocos2dxEditText mTextField;
    private Button mCancelBtn;	// 取消按钮
    private Button mConfirmBtn;	// 确认按钮
	private String mOriginText = "";
    
    public TextView getTextField() {
    	return mTextField;
    }
    
    public void setInputField(Cocos2dxEditText editText, Button btnCancel, Button btnConfirm) {
    	mTextField = editText;
    	if (null != mTextField && null != textInputWraper) {
    		mTextField.setOnEditorActionListener(textInputWraper);
            mTextField.setVisibility(View.INVISIBLE);
    		this.requestFocus();
    	}
        
    	mCancelBtn = btnCancel;
    	if (null != mCancelBtn) {
            mCancelBtn.setVisibility(View.INVISIBLE);
            mCancelBtn.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
    				finishImeInput();
    			}
    		});
    	}
        
    	mConfirmBtn = btnConfirm;
    	if (null != mConfirmBtn) {
            mConfirmBtn.setVisibility(View.INVISIBLE);
            mConfirmBtn.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
    				String text = mTextField.getText().toString();
    				if (text.isEmpty()) { text = " "; }
    				clearText();
    				insertText(text);
    				finishImeInput();
    			}
    		});
    	}
    }
    
    public static void openIMEKeyboard() {
    	Message msg = new Message();
    	msg.what = HANDLER_OPEN_IME_KEYBOARD;
    	msg.obj = mainView.getContentText();
    	handler.sendMessage(msg);
    	
    }
    
    private String getContentText() {
		return mRenderer.getContentText();
	}

	public static void closeIMEKeyboard() {
    	Message msg = new Message();
    	msg.what = HANDLER_CLOSE_IME_KEYBOARD;
    	handler.sendMessage(msg);
    }
    
	public void clearText() {
		for (int i = mOriginText.length() + 2; i > 0; --i) {
			deleteBackward();
		}
	}
	
    public void insertText(final String text) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.handleInsertText(text);
            }
        });
    }
    
    public void deleteBackward() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.handleDeleteBackward();
            }
        });
    }
    
    public void finishImeInput() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
            	mRenderer.handleActionDown(0, -1, -1);
            	mRenderer.handleActionUp(0, -1, -1);
            }
        });
    }
    
    public void notifyMemoryWarning() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
            	mRenderer.handleMemoryWarning();
            }
        });
    }

	///////////////////////////////////////////////////////////////////////////
    // for touch event
    ///////////////////////////////////////////////////////////////////////////

    public boolean onTouchEvent(final MotionEvent event) {
    	// these data are used in ACTION_MOVE and ACTION_CANCEL
    	final int pointerNumber = event.getPointerCount();
    	final int[] ids = new int[pointerNumber];
    	final float[] xs = new float[pointerNumber];
    	final float[] ys = new float[pointerNumber];

    	for (int i = 0; i < pointerNumber; i++) {
            ids[i] = event.getPointerId(i);
            xs[i] = event.getX(i);
            ys[i] = event.getY(i);
        }
    	
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_POINTER_DOWN:
        	final int indexPointerDown = event.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        	final int idPointerDown = event.getPointerId(indexPointerDown);
            final float xPointerDown = event.getX(indexPointerDown);
            final float yPointerDown = event.getY(indexPointerDown);

            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mRenderer.handleActionDown(idPointerDown, xPointerDown, yPointerDown);
                }
            });
            break;
            
        case MotionEvent.ACTION_DOWN:
        	// there are only one finger on the screen
        	final int idDown = ids[0];
            final float xDown = xs[0];
            final float yDown = ys[0];
            
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mRenderer.handleActionDown(idDown, xDown, yDown);
                }
            });
            break;

        case MotionEvent.ACTION_MOVE:
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mRenderer.handleActionMove(ids, xs, ys);
                }
            });
            break;

        case MotionEvent.ACTION_POINTER_UP:
        	final int indexPointUp = event.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        	final int idPointerUp = event.getPointerId(indexPointUp);
            final float xPointerUp = event.getX(indexPointUp);
            final float yPointerUp = event.getY(indexPointUp);
            
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mRenderer.handleActionUp(idPointerUp, xPointerUp, yPointerUp);
                }
            });
            break;
            
        case MotionEvent.ACTION_UP:  
        	// there are only one finger on the screen
        	final int idUp = ids[0];
            final float xUp = xs[0];
            final float yUp = ys[0];
            
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mRenderer.handleActionUp(idUp, xUp, yUp);
                }
            });
            break;

        case MotionEvent.ACTION_CANCEL:
            queueEvent(new Runnable() {
               @Override
                public void run() {
                    mRenderer.handleActionCancel(ids, xs, ys);
                }
            });
            break;
        }
      
        return true;
    }
    
    /*
     * This function is called before Cocos2dxRenderer.nativeInit(), so the width and height is correct.
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
    	this.mRenderer.setScreenWidthAndHeight(w, h);
    }
    
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	final int kc = keyCode;
    	if(keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		if (GameApp.SDK_TAG.equals("LOCOJOY")) {
    			GameSDK.exitSdk();
    		} else if (GameApp.SDK_TAG.equals("YIJIE")) {
        		GameApp.getApp().runOnGLThread(new Runnable() {
        			public void run() {
        	    		GameApp.nativeExitGame();
        			}
        		});
    		} else {
        		GameApp.showExitDialog();
    		}
    	}
    	if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) 
        {
   	    	queueEvent(new Runnable() {
   	    		@Override
            	public void run() {
   	    			mRenderer.handleKeyDown(kc);
    	    	}
    	    });
    	   	return true;
        }
    	if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
    		GameApp.mAudioManager.adjustStreamVolume(
					AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, 
					AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
			return true;
		}
    	if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
    		GameApp.mAudioManager.adjustStreamVolume(
					AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, 
					AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
			return true;
    	}
        return super.onKeyDown(keyCode, event);
    }
}
