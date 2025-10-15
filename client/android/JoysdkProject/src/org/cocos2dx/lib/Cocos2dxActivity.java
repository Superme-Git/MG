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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import com.locojoy.mini.mt3.GameApp;

@SuppressLint({ "HandlerLeak", "SdCardPath", "SimpleDateFormat" })
public class Cocos2dxActivity extends Activity {
	private static Cocos2dxMusic backgroundMusicPlayer;
	private static Cocos2dxSound soundPlayer;
	private static Cocos2dxAccelerometer accelerometer;
	private static boolean accelerometerEnabled = false;
	private static Handler handler;
	private final static int HANDLER_SHOW_DIALOG = 1;
	private static String packageName;

	private static native void nativeSetPaths(String apkPath, final AssetManager pAssetManager);

	private String flieName;
	
	protected Cocos2dxGLSurfaceView mGLView = null;
	
	public static int REQUEST_CODE_CAMERA = 10;
	public static int REQUEST_CODE_ALBUM = 11;
	
	private static Cocos2dxActivity msInstance = null;
	private static String mCameraPhotoFilename = "";
	
	public Cocos2dxActivity()
	{
		msInstance = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get frame size
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		accelerometer = new Cocos2dxAccelerometer(this);

		// init media player and sound player
		backgroundMusicPlayer = new Cocos2dxMusic(this);
		soundPlayer = new Cocos2dxSound(this);

		// init bitmap context
		Cocos2dxBitmap.setContext(this);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HANDLER_SHOW_DIALOG:
					showDialog(((DialogMessage) msg.obj).title, ((DialogMessage) msg.obj).message);
					break;
				}
			}
		};
	}

	public static String getCurrentLanguage() {
		String languageName = java.util.Locale.getDefault().getLanguage();
		return languageName;
	}

	public static void showMessageBox(String title, String message) {
		Message msg = new Message();
		msg.what = HANDLER_SHOW_DIALOG;
		msg.obj = new DialogMessage(title, message);

		handler.sendMessage(msg);
	}

	public static void enableAccelerometer() {
		accelerometerEnabled = true;
		accelerometer.enable();
	}

	public static void disableAccelerometer() {
		accelerometerEnabled = false;
		accelerometer.disable();
	}

	public static void preloadBackgroundMusic(String path) {
		backgroundMusicPlayer.preloadBackgroundMusic(path);
	}

	public static void playBackgroundMusic(String path, boolean isLoop) {
		backgroundMusicPlayer.playBackgroundMusic(path, isLoop);
	}

	public static void stopBackgroundMusic() {
		backgroundMusicPlayer.stopBackgroundMusic();
	}

	public static void pauseBackgroundMusic() {
		backgroundMusicPlayer.pauseBackgroundMusic();
	}

	public static void resumeBackgroundMusic() {
		backgroundMusicPlayer.resumeBackgroundMusic();
	}

	public static void rewindBackgroundMusic() {
		backgroundMusicPlayer.rewindBackgroundMusic();
	}

	public static boolean isBackgroundMusicPlaying() {
		return backgroundMusicPlayer.isBackgroundMusicPlaying();
	}

	public static float getBackgroundMusicVolume() {
		return backgroundMusicPlayer.getBackgroundVolume();
	}

	public static void setBackgroundMusicVolume(float volume) {
		backgroundMusicPlayer.setBackgroundVolume(volume);
	}

	public static int playEffect(String path, boolean isLoop) {
		return soundPlayer.playEffect(path, isLoop, 1.f, 0.f, 1.f);
	}

	public static void stopEffect(int soundId) {
		soundPlayer.stopEffect(soundId);
	}
	
	public static void stopEffectByPath(String path) {
		soundPlayer.stopEffectByPath(path);
	}
	
	public static boolean getEffectIsPlaying(String path) {
		return soundPlayer.getEffectIsPlaying(path);
	}

	public static void pauseEffect(int soundId) {
		soundPlayer.pauseEffect(soundId);
	}

	public static void resumeEffect(int soundId) {
		soundPlayer.resumeEffect(soundId);
	}

	public static float getEffectsVolume() {
		return soundPlayer.getEffectsVolume();
	}

	public static void setEffectsVolume(float volume) {
		soundPlayer.setEffectsVolume(volume);
	}

	public static void preloadEffect(String path) {
		soundPlayer.preloadEffect(path);
	}

	public static void unloadEffect(String path) {
		soundPlayer.unloadEffect(path);
	}

	public static void stopAllEffects() {
		soundPlayer.stopAllEffects();
	}

	public static void pauseAllEffects() {
		soundPlayer.pauseAllEffects();
	}

	public static void resumeAllEffects() {
		soundPlayer.resumeAllEffects();
	}
	
	public static void setEffectVolume(int streamId, float fVolume)
	{
		soundPlayer.setEffectVolume(streamId, fVolume);
	}
	
	public static boolean hasEffect(int streamId)
	{
		return soundPlayer.hasEffect(streamId);
	}
	
	public static boolean isEffectPlaying(int streamId)
	{
		return soundPlayer.isEffectPlaying(streamId);
	}

	public static void end() {
		backgroundMusicPlayer.end();
		soundPlayer.end();
	}

	public static String getCocos2dxPackageName() {
		return packageName;
	}

	public static void terminateProcess() {
//		JniProxy.release();
		GameApp.getApp().finish();
		System.exit(0);
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (accelerometerEnabled) {
			accelerometer.enable();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (accelerometerEnabled) {
			accelerometer.disable();
		}
	}

	protected void setPackageName(String packageName) {
		Cocos2dxActivity.packageName = packageName;

		String apkFilePath = "";
		ApplicationInfo appInfo = null;
		PackageManager packMgmr = getApplication().getPackageManager();
		try {
			appInfo = packMgmr.getApplicationInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to locate assets, aborting...");
		}
		apkFilePath = appInfo.sourceDir;

		// add this link at the renderer class
		nativeSetPaths(apkFilePath, this.getAssets());
	}

	private void showDialog(String title, String message) {
		Dialog dialog = new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		}).create();

		dialog.show();
	}

	// /////////////////////////////////////////////
	// copy and unzip assets to sd or /data/data/xxx/

	private void CopyAssets(String assetDir, String dir) {
		String[] files;
		try {
			files = this.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(dir);
		// if this directory does not exists, make one.
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {

			}
		}

		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				// we make sure file name not contains '.' to be a folder.
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						CopyAssets(fileName, dir + fileName + "/");
					} else {
						CopyAssets(assetDir + "/" + fileName, dir + fileName + "/");
					}
					continue;
				}

				String outfileName = fileName;
				if (fileName.contains(".pfs")) {
					outfileName = fileName.substring(0, fileName.indexOf(".")) + ".pfs";
				}
				File outFile = new File(mWorkingPath, outfileName);
				// if(outFile.exists())
				// outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length())
					in = getAssets().open(assetDir + "/" + fileName);
				else
					in = getAssets().open(fileName);

				OutputStream out = new FileOutputStream(outFile, true);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getAssetsOutDir() {

		if (GameApp.getApp().getExternalFilesDir(null) != null) {
			return GameApp.getApp().getExternalFilesDir(null) + "/gamedata";
		} else {
			return "/data/data/" + getCocos2dxPackageName() + "/assets";
		}
	}

	public void unzipAndCopyAssetsTo() {
		String outDir = getAssetsOutDir();
		File outfile = new File(outDir);
		deleteFile(outfile);
		CopyAssets("", outDir + "/");
	}

	public void deleteFile(File file) {
		if (!file.exists())
			return;
		if (file.isDirectory()) {
			for (File f : file.listFiles())
				deleteFile(f);
		}
		file.delete();

	}
	
	public void runOnGLThread(Runnable action){
		if (mGLView != null) {
			mGLView.queueEvent(action);
		} else {
			runOnUiThread(action);
		}
	}
	
	public static boolean openCamera()
	{
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED))
		{
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			
			File cameraAlbumDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
			if(!cameraAlbumDir.exists())
			{
				cameraAlbumDir.mkdirs();
			}
			
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String filename = "IMG_" + dateFormat.format(date) + ".jpg";
			
			File cameraPhotoFile = new File(cameraAlbumDir, filename);
			Uri photoOutUri = Uri.fromFile(cameraPhotoFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutUri);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.8);
			
			mCameraPhotoFilename = cameraPhotoFile.getAbsolutePath();
			
			msInstance.startActivityForResult(intent, REQUEST_CODE_CAMERA);
			return true;
		}
		return false;
	}
	
	public static boolean openAlbum()
	{
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		msInstance.startActivityForResult(intent, REQUEST_CODE_ALBUM);  
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_CODE_CAMERA || requestCode == REQUEST_CODE_ALBUM)
		{
			String path = "";
			if(resultCode == Activity.RESULT_OK)
			{
				if(requestCode == REQUEST_CODE_CAMERA)
				{
					path = mCameraPhotoFilename;
				}
				else if(requestCode == REQUEST_CODE_ALBUM)
				{
					Uri selectedImage = data.getData();

	                String[] filePathColumn = { MediaStore.Images.Media.DATA };
	                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	                cursor.moveToFirst();
	                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	                path = cursor.getString(columnIndex);
	                cursor.close();
				}
			}
			nativeAddSelectedPhoto(path);
		}
	}
	
	public static native void nativeAddSelectedPhoto(String path);
	
	// /////////////////////////////////////////////
}

class DialogMessage {
	public String title;
	public String message;

	public DialogMessage(String title, String message) {
		this.message = message;
		this.title = title;
	}
}
