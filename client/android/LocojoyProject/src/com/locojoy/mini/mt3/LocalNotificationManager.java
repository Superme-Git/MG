package com.locojoy.mini.mt3;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.locojoy.mini.mt3.locojoy.R;

@SuppressLint("SimpleDateFormat")
public class LocalNotificationManager {
	static String XMLPATH;
	static String CONFIGPATH;
	static String FILENAME = "3656893336";//"android_notify.xml";
	static Activity activity;
	static public void enableNotification(int flag){
		activity = GameApp.getApp();
//		XMLPATH = activity.getExternalFilesDir(null) + "/res/" + FILENAME;
//		CONFIGPATH = activity.getExternalFilesDir(null) + "/androidNotify.json";
		
		File df = new File(GameApp.getCacheOutDir() + "/res/" + FILENAME);

		if (df.exists()) {
			XMLPATH = GameApp.getCacheOutDir() + "/res/" + FILENAME;
		}
		else{
			XMLPATH = GameApp.getAssetsOutDir() + "/res/" + FILENAME;
		}
		
		CONFIGPATH = GameApp.getAssetsOutDir() + "/androidNotify.json";
		
		if (flag == 1){
			setNotification();
		}
		else if(flag == 2){
			regularSet();
		}
		else if(flag == 3){
			cancelNotification();
		}
		
	}
	static private void cancelNotification( ){
		String[] attrib = {"enableNotification"};
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(attrib[0], "false");
		writeConfig(map);
		if(copyNotificationFile()){
			Element root = getXmlRoot(XMLPATH);
			NodeList r = root.getElementsByTagName("item");
			int len = r.getLength();
			removeAlarm(len);
		}
		else{
			removeAlarm(10);
		}
		
	}
	static private void regularSet(){
		String[] attrib = {"enableNotification"};
		String[] result = null;
		result = readConfig(attrib);
		if(result == null || result[0] == null || result[0].equals("true")){
			if(!copyNotificationFile()){
				return;
			}
			addNotification();
		}
	}
	static private void setNotification(){
		String[] attrib = {"enableNotification"};
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(attrib[0], "true");
		writeConfig(map);
	}
	
	
	static private boolean copyNotificationFile(){
		File outFile = new File(XMLPATH);
		if (outFile.exists()){
			return true;
		}
		else{
			return false;
		}
		
		 /*
	    if(outFile.getParentFile().exists() == false){
			outFile.getParentFile().mkdirs();
		}
		InputStream in = null;
		OutputStream out = null;
		try{
		 	in = activity.getResources().getAssets().open(FILENAME);
			out = new FileOutputStream(outFile, false);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			return true;
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			try{
				in.close();
				out.close();
			}
			catch(Exception e){
				
			}
			finally{
				
			}
			
		}
			*/
	}
	
	static private boolean addNotification(){
		try{
			Element root = getXmlRoot(XMLPATH);
			if (root != null){
				NodeList r = root.getElementsByTagName("item");
				HashMap<String, String> map = null;
				String attr[] = {"day", "hour", "minute", "type"};
				long systime = System.currentTimeMillis();
				int len = r.getLength();
				removeAlarm(len);
				for (int i = 1;i <= len;i++){
					map = getAttribute(root,attr, String.valueOf(i));
					if ( map.get(attr[0]) != null && map.get(attr[1]) != null && map.get(attr[2]) != null  && map.get(attr[3]) != null){
						
						boolean Ingore = false;
						for (int j = 0; j < 8; j++) {
							int type = Integer.parseInt(map.get(attr[3]));
							int set = GameApp.nativeGetNotifyEnable(j + 1);
							if (set == 0 && type == j + 1) {
								Ingore = true;
								break;
							}
						}
						
						if (Ingore) {
							continue;
						}
						Calendar cal = Calendar.getInstance();
						cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
						
						int tmp = Calendar.MONDAY - 1;
						int setday = Integer.parseInt(map.get(attr[0])) + tmp;
						
						cal.set(Calendar.DAY_OF_WEEK, setday);
						
						cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(map.get(attr[1])));
						
						cal.set(Calendar.MINUTE, Integer.parseInt(map.get(attr[2])));
						cal.set(Calendar.SECOND, 0);
						
						long curtime = cal.getTimeInMillis();
						
						if (curtime < systime) {
							cal.add(Calendar.DAY_OF_MONTH, 7);
						}
						
						setAlarm(cal.getTimeInMillis(),String.valueOf(i));
					}
				}
				return true;
			}		
			return false;
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	static private String[] readConfig(String[] attrib){
		if (attrib == null || attrib.length == 0 || attrib.equals(""))return null;
		File cfgFile = new File(CONFIGPATH);
		if(cfgFile.exists() == false)return null;
		FileInputStream cfgIn = null;
		String str = "";
		String[] result = null;
		try{
			cfgIn = new FileInputStream(cfgFile);
			byte[] bytes = new byte[(int) cfgFile.length()];
			cfgIn.read(bytes);
			str = new String(bytes);
			JSONObject json = new JSONObject(str);
			result = new String[attrib.length];
			for (int i = 0 ; i < attrib.length ; i++){
				if(json.has(attrib[i])){
					result[i] = String.valueOf(json.get(attrib[i]));
				}
				else{
					result[i] = null;
				}
			}
			
			return result;
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			return result;
			
		}
		finally{
			try{
				cfgIn.close();
		
			}
			catch(Exception e){
				e.printStackTrace();
				return result;
			}
		
		}
	}
	static private boolean writeConfig (HashMap<String, String> map){
		File cfgFile = new File(CONFIGPATH);
		OutputStream cfgOut = null;
		try{
			if(cfgFile.getParentFile().exists() == false){
				cfgFile.getParentFile().mkdirs();
			}
			cfgOut = new FileOutputStream(cfgFile,false);
			JSONObject json = new JSONObject();
			for(Map.Entry<String, String> entry : map.entrySet()){
				json.put(entry.getKey(),entry.getValue());
			}
			cfgOut.write(json.toString().getBytes());
			cfgOut.flush();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		finally{
			try{
			cfgOut.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	static public  HashMap<String,String> getAttribute(Element root,String attrib[],String id){
		HashMap<String, String> result = new HashMap<String, String>();
		if (attrib == null || attrib.length == 0 )return result;
		try{
			if (root != null){
				NodeList r = root.getElementsByTagName("item");
				Node n = null;
				String itemid = "";
				int len = r.getLength();
				for (int i = 0;i < len;i++){
					n = r.item(i);
					if(n.getNodeType() == Node.ELEMENT_NODE){
						itemid = ((Element)n).getAttribute("id"); 
						if (id.equals(itemid)){
							for(String str : attrib){
								result.put(str,((Element)n).getAttribute(str));
							}
							break;
						}
						
					}
				}
			}				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	static public void setAlarm(long time,String alarmid){
		AlarmManager alarmMgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);  
		Intent intent = new Intent(activity.getApplicationContext(), LocalNotificationBroadcastReceiver.class);  
		intent.putExtra("id", alarmid);
		intent.putExtra("xmlpath", XMLPATH);
		intent.putExtra("classname", activity.getClass().getCanonicalName());
		intent.putExtra("iconid", R.drawable.icon);
		intent.setAction(alarmid);

		PendingIntent pendIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); 
	 
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time, 7*24*3600*1000, pendIntent);	
	}
	
	static public void removeAlarm(int len){
		if(len <= 0 )return;
		try{
			for (int i = 1;i<=len;i++){
				AlarmManager alarmMgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);  
				Intent intent = new Intent(activity.getApplicationContext(), LocalNotificationBroadcastReceiver.class); 
				intent.setAction(String.valueOf(i));
				PendingIntent pendIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmMgr.cancel(pendIntent);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

	static public Element getXmlRoot(String path) {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		InputStream inputstream = null;
		File file = new File(path);
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			inputstream = new FileInputStream(file);
			document = builder.parse(inputstream);
			Element root = document.getDocumentElement();
			return root;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputstream != null)
					inputstream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}





}
