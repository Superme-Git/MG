package com.locojoy.sdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.mob.tools.utils.UIHandler;

import android.content.Context;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeySharePage;
import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;
import cn.sharesdk.socialization.Comment;
import cn.sharesdk.socialization.CommentListener;
import cn.sharesdk.socialization.LikeListener;
import cn.sharesdk.socialization.Socialization;
import cn.sharesdk.socialization.component.ReplyTooFrequentlyException;

import com.locojoy.mini.mt3.GameApp;
import com.locojoy.mini.mt3.common.R;

public class SDKShare extends OnekeySharePage implements Callback {

	private static final String PlatformName[] = {
		"SinaWeibo",
		"TencentWeibo",
		"Douban",
		"QZone",
		"Renren",
		"KaiXin",
		"Facebook",
		"Twitter",
		"Evernote",
		"FourSquare",
		"GooglePlus",
		"Instagram",
		"LinkedIn",
		"Tumblr",
		"Email",
		"ShortMessage",
		"Wechat",
		"WechatMoments",
		"QQ",
		"Instapaper",
		"Pocket",
		"YouDao",
		"Pinterest",
		"Flickr",
		"Dropbox",
		"VKontakte",
		"WechatFavorite",
		"Yixin",
		"YixinMoments",
		"Mingdao",
		"Line",
		"WhatsApp",
		"KakaoTalk",
	};
	
	public static final int eShareType_Text = 1;
	public static final int eShareType_Picture = 2;
	public static final int eShareType_WebUrl = 3;
	
	private static final int INIT_SDK = 1;
	private static final int AFTER_LIKE = 2;
	
	public static SDKShare mine;

	private HashMap<String, Platform> m_mapPlatform = new HashMap<String, Platform>();
	private Context m_Context;

	public SDKShare(OnekeyShareThemeImpl impl) {
		super(impl);
		mine = this;
	}

	public void init(Context context)
	{
		m_Context = context;
		
		new Thread() {
			public void run() {
				UIHandler.sendEmptyMessageDelayed(INIT_SDK, 100, SDKShare.this);
			}
		}.start();
	}

	public boolean handleMessage(Message msg)
	{
		switch (msg.what) {
		case INIT_SDK:
			initOnekeyShare();
			break;
		case AFTER_LIKE:
			if(msg.arg1 == 1){
				//success
				int resId = com.mob.tools.utils.R.getStringRes(m_Context, "like_success");
				if (resId > 0) {
					Toast.makeText(m_Context, resId, Toast.LENGTH_SHORT).show();
				}
			}else {
				//fail
				int resId = com.mob.tools.utils.R.getStringRes(m_Context, "like_fail");
				if (resId > 0) {
					Toast.makeText(m_Context, resId, Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case OnekeyShare.SHARE_SUCCEED:
			Log.e("mt3", "SDKShare handleMessage OnekeyShare.SHARE_SUCCEED!");
			GameApp.getApp().runOnGLThread(new Runnable(){
				public void run() {
					GameApp.shareSucceed();
				}
			});
			break;
		case OnekeyShare.SHARE_FAILED:
			Log.e("mt3", "SDKShare handleMessage OnekeyShare.SHARE_FAILED!");
			break;
		case OnekeyShare.SHARE_CANCELED:
			Log.e("mt3", "SDKShare handleMessage OnekeyShare.SHARE_CANCELED!");
			break;
		default:
			break;

		}

		return false;
	}

	//初始化一键分享
	private void initOnekeyShare()
	{
		//初始化SDK
		ShareSDK.initSDK(m_Context);
		ShareSDK.registerService(Socialization.class);

		//初始化所有可分享的平台
		Platform[] platforms = ShareSDK.getPlatformList();
		for (Platform p : platforms) {
			m_mapPlatform.put(p.getName(), p);
			System.out.printf("%s = %d\n", p.getName(), p.getId());
		}

		//设置Comment监听
		Socialization.setCommentListener(new CommentListener() {

			@Override
			public void onSuccess(Comment comment) {
				int resId = com.mob.tools.utils.R.getStringRes(m_Context, "ssdk_socialization_reply_succeeded");
				if (resId > 0) {
					Toast.makeText(m_Context, resId, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFail(Comment comment) {
				Toast.makeText(m_Context, comment.getFileCodeString(m_Context), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(Throwable throwable) {
				if (throwable instanceof ReplyTooFrequentlyException) {
					int resId = com.mob.tools.utils.R.getStringRes(m_Context, "ssdk_socialization_replay_too_frequently");
					if (resId > 0) {
						Toast.makeText(m_Context, resId, Toast.LENGTH_SHORT).show();
					}
				} else {
					throwable.printStackTrace();
				}
			}
		});

		//设置Like监听
		Socialization.setLikeListener(new LikeListener() {

			@Override
			public void onSuccess(String topicId, String topicTitle, String commentId) {
				Message msg = new Message();
				msg.what = AFTER_LIKE;
				msg.arg1 = 1;
				UIHandler.sendMessage(msg, SDKShare.this);
			}

			@Override
			public void onFail(String topicId, String topicTitle, String commentId, String error) {
				Message msg = new Message();
				msg.what = AFTER_LIKE;
				msg.arg1 = 2;
				UIHandler.sendMessage(msg, SDKShare.this);
			}

		});
	}
	
	// 处理一键分享
	public static void doOnekeyShare(int ePlatform, int eType,
			String title, String text, String imgPath, String webUrl) {
		if (PlatformName[ePlatform - 1].equals("WechatMoments")) {
			shareToWechatMoments(eType, title, text, imgPath, webUrl);
		} 
		else if (PlatformName[ePlatform - 1].equals("Wechat")) {
			shareToWechat(eType, title, text, imgPath, webUrl);
		}
		else if (PlatformName[ePlatform - 1].equals("QQ")) {
			shareToQQ(eType, title, text, imgPath, webUrl);
		} 
		else if (PlatformName[ePlatform - 1].equals("SinaWeibo")) {
			shareToSinaWeibo(title, text, imgPath, webUrl);
		} 
		else if (PlatformName[ePlatform - 1].equals("QZone")) {
			shareToQZone(title, text, imgPath, webUrl);
		} 
	}
	
	//目前除了图片，其他设置了都没有用，不知道什么情况
	private static void shareToSinaWeibo(String title, String text, String imgPath, String webUrl)
	{
		String strText = text.equals("") ? mine.m_Context.getString(R.string.app_name) : text;
		String strImgPath = imgPath.equals("") ? "" : imgPath;
		String strUrl = webUrl.equals("") ? "http://mt3.locojoy.com/share/wbandroidlocojoy/index.html" : webUrl;
		
		OnekeyShare oks = new OnekeyShare();
		if (strImgPath.startsWith("http")) {
			oks.setImageUrl(strImgPath);
		} else {
			oks.setImagePath(strImgPath);
		}
		oks.setText(strText);
		oks.setUrl(strUrl);
		
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
		oks.setPlatform("SinaWeibo");
		oks.show(mine.m_Context, mine);
	}
	
	// QQ：只能分享链接的方式，不能分享图片（等正式测试时再确认一下）
	//（1）必须要有setTitleUrl，否则分享不成功
	//（2）不能只有setTitleUrl，否则分享不成功
	//（3）strTitle和strText不起作用
	//（4）setImagePath会在消息的图标中显示本地图片
	private static void shareToQQ(int eType, String title, String text, String imgPath, String webUrl)
	{
		String strTitle = title.equals("") ? mine.m_Context.getString(R.string.app_name) : title;
		String strText = text.equals("") ? mine.m_Context.getString(R.string.app_name) : text;
		String strImgPath = imgPath.equals("") ? "" : imgPath;
		String strUrl = webUrl.equals("") ? "http://mt3.locojoy.com/share/qqandroidlocojoy/index.html" : webUrl;
		
		OnekeyShare oks = new OnekeyShare();
		oks.setTitle(strTitle);
		oks.setText(strText);
		if (strImgPath.startsWith("http")) {
			oks.setImageUrl(strImgPath);
		} else {
			oks.setImagePath(strImgPath);
		}
		oks.setTitleUrl(strUrl);
		
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
		oks.setPlatform("QQ");
		oks.show(mine.m_Context, mine);
	}
	
	private static void shareToQZone(String title, String text, String imgPath, String webUrl)
	{
		String strTitle = title.equals("") ? mine.m_Context.getString(R.string.app_name) : title;
		String strText = text.equals("") ? mine.m_Context.getString(R.string.app_name) : text;
		String strSite = mine.m_Context.getString(R.string.app_name);
		String strImgPath = imgPath.equals("") ? "" : imgPath;
		String strUrl = webUrl.equals("") ? "http://mt3.locojoy.com/share/qqandroidlocojoy/index.html" : webUrl;
		
		OnekeyShare oks = new OnekeyShare();
		oks.setTitle(strTitle);
		oks.setTitleUrl(strUrl);
		
		oks.setText(strText);
		
		if (strImgPath.startsWith("http")) {
			oks.setImageUrl(strImgPath);
		} else {
			oks.setImagePath(strImgPath);
		}
		
		oks.setSite(strSite);
		oks.setSiteUrl(strUrl);
		
		oks.setUrl(strUrl);
		
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
		oks.setPlatform("QZone");
		oks.show(mine.m_Context, mine);
	}
	
	private static void shareToWechatMoments(int eType, String title, String text, String imgPath, String webUrl)
	{
		String strTitle = title.equals("") ? mine.m_Context.getString(R.string.app_name) : title;
		String strText = text.equals("") ? mine.m_Context.getString(R.string.app_name) : text;
		String strUrl = webUrl.equals("") ? "http://mt3.locojoy.com/share/wxandroidlocojoy/index.html" : webUrl;
		
		OnekeyShare oks = new OnekeyShare();
		
		if (eType == eShareType_Picture) {
			String strImgPath = imgPath;
			if (strImgPath.startsWith("http")) {
				oks.setImageUrl(strImgPath);
			} else {
				oks.setImagePath(strImgPath);
			}
		} else if (eType == eShareType_WebUrl) {
			oks.setTitle(strTitle);
			oks.setText(strText);
			if (imgPath.startsWith("http")) {
				oks.setImageUrl(imgPath);
			} else {
				String strImgPath = imgPath.equals("") ? copyImg("res/1162650629") : copyImg(imgPath);
				oks.setImagePath(strImgPath);
			}
			oks.setUrl(strUrl);
		}
		
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
		oks.setPlatform("WechatMoments");
		oks.show(mine.m_Context, mine);
	}
	
	private static void shareToWechat(int eType, String title, String text, String imgPath, String webUrl)
	{
		String strTitle = title.equals("") ? mine.m_Context.getString(R.string.app_name) : title;
		String strText = text.equals("") ? mine.m_Context.getString(R.string.app_name) : text;
		String strUrl = webUrl.equals("") ? "http://mt3.locojoy.com/share/wxandroidlocojoy/index.html" : webUrl;
		
		OnekeyShare oks = new OnekeyShare();
		
		if (eType == eShareType_Picture) {
			String strImgPath = imgPath;
			if (strImgPath.startsWith("http")) {
				oks.setImageUrl(strImgPath);
			} else {
				oks.setImagePath(strImgPath);
			}
		} else if (eType == eShareType_WebUrl) {
			oks.setTitle(strTitle);
			oks.setText(strText);
			if (imgPath.startsWith("http")) {
				oks.setImageUrl(imgPath);
			} else {
				String strImgPath = imgPath.equals("") ? copyImg("res/1162650629") : copyImg(imgPath);
				oks.setImagePath(strImgPath);
			}
			oks.setUrl(strUrl);
		}
		
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
		oks.setPlatform("Wechat");
		oks.show(mine.m_Context, mine);
	}
	
	private static String copyImg(String imgPath)
	{
		// 如果是自动更新的图标，则直接用
        if (imgPath.startsWith("/")) {
        	return imgPath;
        }
        
		String targetPath = GameApp.getAssetsOutDir() + "/" + imgPath;
		try
		{
			File outFile = new File(targetPath);
			if (!outFile.exists()) {
				InputStream in = GameApp.getApp().getAssets().open(imgPath);
				OutputStream out = new FileOutputStream(outFile, true);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				in.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return targetPath;
	}

}
