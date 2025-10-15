package com.locojoy.mini.mt3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.widget.Toast;

import com.locojoy.wojmt3.yj.R;

public class FileDownloader {
	
	public static int m_DownloadedSize = 0;

	static boolean DownloadOneFile(final String url, final String dst, boolean notify) {
		for (int i = 0; i < 1; i++) {
			boolean ret = DownloadOneFileIMP(url, dst, notify);
			if (ret)
				return true;

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	static boolean DownloadOneFileIMP(String src, String dst, boolean notify) {
		InputStream inputstream = null;
		RandomAccessFile cachedfile = null;
		OutputStream output = null;
		try {
			long size = 0;
			// if not exist , create dir
			String[] dirArray = dst.split("/");
			String dirString = "";
			for (int i = 0; i < dirArray.length - 1; i++) {
				dirString += dirArray[i] + "/";
			}
			File file = new File(dirString);
			if (!file.exists()) {
				file.mkdirs();
			} else if (notify) {
				File file1 = new File(dst);
				if (file1.exists()) {
					size = file1.length();
				}
			}

			// long size = GetDownloadFileSize(src);
			long totalsize = GetDownloadFileSize(src);
			
			if (totalsize == -1) {//获取失败
				return false;
			}
			
			if (totalsize <= size) {
				//********************************
				//this is only commit with UTF-8, waiting to cresh, by yangbin 2015.12.4
				//********************************
				// 如果进到这里
				// 下载完了还下载，说明时文件校验是吧了，需要重新下载。
				// 长度置0就可以了，后续代码会对长度为0的文件删除重建。
				size = 0;
			}
			URL url = new URL(src);
			HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
			urlconn.setRequestMethod("GET");
			urlconn.setDoOutput(false);
			urlconn.setRequestProperty("Content-Type", "application/octet-stream");
			// urlconn.setRequestProperty("Connection", "Keep-Alive");

			urlconn.setConnectTimeout(30000);
			urlconn.setReadTimeout(30000);

			urlconn.setRequestProperty("User-Agent", "");
			urlconn.setRequestProperty("Accept", "*/*");
			if (size != 0) {
				urlconn.addRequestProperty("Range", "bytes=" + size + "-");
			}

			urlconn.connect();

			int status = urlconn.getResponseCode();
			String resonseMes = urlconn.getResponseMessage();
			if (status == 404) {
				Log.e("filedownload", "status: " + status + " " + resonseMes);
				return false;
			}
			inputstream = urlconn.getInputStream();
			String dstfilename = dirArray[dirArray.length - 1];
			File dstFile = new File(dirString, dstfilename);

			try {
				if (size == 0) {
					if (dstFile.exists()) {
						GameApp.RemoveDir(dirString + "/" + dstfilename);
					}
					dstFile.createNewFile();
				}
			} catch (Exception e) {
				if (!dstFile.exists()) {
					FileUtils.touch(dstFile);
				}
				e.printStackTrace();
			}

			RandomAccessFile fos = new RandomAccessFile(dstFile, "rw");
			fos.seek(size);
			int length = 0;
			byte[] buffer = new byte[1024];
			while ((length = inputstream.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
//				output.flush();
//				if (notify) {
//					size += length;
//					
//					m_DownloadedSize += length;
//					String str;
//					//str = String.format("%d kb/%d kb", size / 1024, totalsize / 1024);
//					str = String.format("%dKB/", m_DownloadedSize/1024);
//					//GameApp.NotifyStep((int) (100 * size / totalsize));
//					GameApp.NotifyDownLoadSize(str);
//				}
			}
			fos.close();
			
		} catch (Exception e) {
			if (IOException.class == e.getClass()) {
				if (e.toString().indexOf("No space left on device") != -1) {
					String strTips = "存储空间不足！";
					try {
						strTips = GameApp.getApp().getString(R.string.mt3_no_space_tip);
					} catch (Exception ex) {
						ex.printStackTrace();
						strTips = "存储空间不足！";
					}
					Toast.makeText(GameApp.getApp(), strTips, Toast.LENGTH_SHORT).show();
				}
			}
			File dstFile = new File(dst);
			if (dstFile.exists()) {
				final File to = new File(dstFile.getAbsolutePath() + System.currentTimeMillis());
				dstFile.renameTo(to);
				to.delete();
			}
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (cachedfile != null)
					cachedfile.close();
				if (inputstream != null)
					inputstream.close();
				if (output != null)
					output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	static long GetDownloadFileSize(String url) {
		long size = 0;
		try {
			HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
			
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			
			size = conn.getContentLength();
			
			int status = conn.getResponseCode();
			String resonseMes = conn.getResponseMessage();
			if (status == 404) {
				Log.e("filedownload", "status: " + status + " " + resonseMes);
				return -1;
			}
			
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

}
