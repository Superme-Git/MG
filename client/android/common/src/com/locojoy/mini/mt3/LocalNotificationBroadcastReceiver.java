package com.locojoy.mini.mt3;

 
import java.util.HashMap;

import org.w3c.dom.Element;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;

public class LocalNotificationBroadcastReceiver extends BroadcastReceiver {
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String id = intent.getStringExtra("id");
		String xmlpath = intent.getStringExtra("xmlpath");
		String classname = intent.getStringExtra("classname");
		int iconid = intent.getIntExtra("iconid", 0);
		if (id == null || id.equals("") || xmlpath == null || xmlpath.equals("") || classname == null || classname.equals("") || iconid == 0)return;

		try{
			Element root = LocalNotificationManager.getXmlRoot(xmlpath);
			HashMap<String, String> map = null;
			String attr[] = {"day", "bar", "title", "content"};
			map =  LocalNotificationManager.getAttribute(root,attr, id);
			
			int NOTIFICATION_BASE_NUMBER=110;
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			  
			  try{
				  Intent pintent = new Intent();
				  pintent.setClassName(context.getPackageName(), classname);
				  PendingIntent contentIndent = PendingIntent.getActivity(context, 0, pintent, 0); 
				  Notification notification = null;
				  if(VERSION.SDK_INT < 11)
				  {
					  notification = new Notification();
					  notification.icon = iconid;
					  notification.defaults = Notification.DEFAULT_SOUND;
					  notification.setLatestEventInfo(context, map.get("title"), map.get("content"), contentIndent);
				  }
				  else if (VERSION.SDK_INT >= 11 && VERSION.SDK_INT < 16)
				  {
					  Notification.Builder builder = new Notification.Builder(context)
					  .setAutoCancel(true)
					  .setContentTitle(map.get("title"))
					  .setContentText(map.get("content"))
					  .setContentIntent(contentIndent)
					  .setSmallIcon(iconid)
					  .setDefaults(Notification.DEFAULT_SOUND)
					  .setWhen(System.currentTimeMillis())
					  .setOngoing(true);
					  notification = builder.getNotification();
				  }
				  else
				  {
					  notification = new Notification.Builder(context)
					  .setAutoCancel(true)
					  .setContentTitle(map.get("title"))
					  .setContentText(map.get("content"))
					  .setContentIntent(contentIndent)
					  .setSmallIcon(iconid)
					  .setWhen(System.currentTimeMillis())
					  .setDefaults(Notification.DEFAULT_SOUND)
					  .build();
				  }
				 nm.notify(NOTIFICATION_BASE_NUMBER, notification);
				  
				AlarmManager alarmMgr = (AlarmManager) GameApp.getApp().getSystemService(Context.ALARM_SERVICE);  
				alarmMgr.cancel(contentIndent);
			  }
			  catch(Exception e){
				  e.printStackTrace();
			  }
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
