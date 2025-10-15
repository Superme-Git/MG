package com.locojoy.mini.mt3;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import com.locojoy.mini.mt3.common.R;


@SuppressLint("SimpleDateFormat")
public class DateUtil {
	public static final long minuteMills = 1000 * 60;
	public static final long hourMills = 1000 * 60 * 60;
	public static final long dayMills = hourMills * 24;

	public static final long weekMills = dayMills * 7;
	
	public static final long TIME_ZONE_OFFSET = Calendar.getInstance().getTimeZone().getRawOffset();//时区导致的差值
	
	public static final long addMills = 1000 * 60 * 60 * 24 * 3;//Java时间的第一天（1970.1.1）是周四，这个是把一周的前三天补全的时间（以周一为第一天）
	
	public static int getCurrentDay(long time) {

		return (int) ((time + TIME_ZONE_OFFSET) / dayMills);
	}

	public static int getCurrentWeek(long time) {

		return (int) ((time + TIME_ZONE_OFFSET + addMills) / weekMills) + 1;
	}
	
	public static boolean inTheSameDay(long firstT,long secondT) {
		if (getCurrentDay(firstT) == getCurrentDay(secondT))
			return true;
		return false;
	}
	public static boolean inTheSameWeek(long firstT,long secondT) {
		if (getCurrentWeek(firstT) == getCurrentWeek(secondT))
			return true;
		return false;
	}
	//获得今天的最后一秒
	public static Calendar getLastSecondCalendar(){
		Calendar current = Calendar.getInstance();
		current.set(Calendar.HOUR_OF_DAY, 23);
		current.set(Calendar.MINUTE, 59);
		current.set(Calendar.SECOND, 59);
		return current;
	}
	
	//获取某天的第一秒 add by lc
	public static long getDayFirstSecond(long time){
		return time - (time + TIME_ZONE_OFFSET) % dayMills;
	}
	//获取某周的第一秒（以 周一算） add by lc
	public static long getWeekFirstSecond(long time){
		return time - (time + TIME_ZONE_OFFSET + addMills) % weekMills;
	}
	
	//获取时间是周几
//	public static int getCurrentWeekDay(){
//		long time = GameTime.currentTimeMillis();
//		return (int)((time - getWeekFirstSecond(time))/dayMills + 1);
//	}
	
	//获取时间是周几
	public static int getCurrentWeekDay(long time){
		return (int)((time - getWeekFirstSecond(time))/dayMills + 1);
	}
	
	//获取该月的第一秒
	public static long getMonthFirstSecond(long time){
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	//获取该月的最后一秒
	public static long getMonthLastSecond(long time){
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	public static boolean inTheSameMonth(long firstT, long secondT){
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal1.setTimeInMillis(firstT);
		cal2.setTimeInMillis(secondT);
		
		if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))
			return true;
		return false;
	}
	
	/**
	 * 获得指定日期的String值，用于客户端显示或者服务器做日期比较
	 * 
	 * @return
	 */
	public static String getCurrentStringFormat(long currentDay){
		String strYear = "年";
		String strMonth = "月";
		String strDay = "日";
		try{
			strYear = GameApp.getApp().getString(R.string.mt3_strinfo_year);
			strMonth = GameApp.getApp().getString(R.string.mt3_strinfo_month);
			strDay = GameApp.getApp().getString(R.string.mt3_strinfo_day);
		}catch(Exception e)
		{
			e.printStackTrace();
			strYear = "年";
			strMonth = "月";
			strDay = "日";
		}
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy"+strYear+"M"+strMonth+"d"+strDay);
		String currentTime = fomat.format(currentDay);
		
		return currentTime;
	}
	
	public static String getCurrentStringFormatEn(long currentTime){
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy-MM-dd");
		String sCurrentTime = fomat.format(currentTime);
		
		return sCurrentTime;
	}
	
	/**
	 * 获得一段时间的描述，如: X天X时X分
	 * 
	 * @return
	 */
	public static String getPeriodStringFormat(long period){
		
		long daynum = period /  dayMills;
		long dayret = period %  dayMills;
		
		long hournum = dayret / hourMills;
		long hourret = dayret % hourMills;
		
		long minutenum = hourret / minuteMills;
		
		
		String strTian = "天";
		String strHour = "时";
		String strMinute = "分";
		try{
			strTian = GameApp.getApp().getString(R.string.mt3_strinfo_tian);
			strHour = GameApp.getApp().getString(R.string.mt3_strinfo_hour);
			strMinute = GameApp.getApp().getString(R.string.mt3_strinfo_minute);
		}catch(Exception e)
		{
			e.printStackTrace();
			strTian = "天";
			strHour = "时";
			strMinute = "分";
		}
		return daynum + strTian + hournum + strHour + minutenum + strMinute;
	}
	
	/**
	 * 获得一段时间的短描述，如: X天X小时X分钟，高级的单位没有时则不存在，例如小于1天则变成X小时X分钟
	 * 
	 * @return
	 */
	public static String getPeriodShortFormat(long period){
		
		long daynum = period /  dayMills;
		long dayret = period %  dayMills;
		
		long hournum = dayret / hourMills;
		long hourret = dayret % hourMills;
		
		long minutenum = hourret / minuteMills;
		
		String strTian = "天";
		String strDetailHour = "小时";
		String strDetailMinute = "分钟";
		try{
			strTian = GameApp.getApp().getString(R.string.mt3_strinfo_tian);
			strDetailHour = GameApp.getApp().getString(R.string.mt3_strinfo_detail_hour);
			strDetailMinute = GameApp.getApp().getString(R.string.mt3_strinfo_detail_minute);
		}catch(Exception e)
		{
			e.printStackTrace();
			strTian = "天";
			strDetailHour = "小时";
			strDetailMinute = "分钟";
		}
		
		if(daynum > 0)
			return daynum + strTian + hournum + strDetailHour + minutenum + strDetailMinute;
		else if(hournum > 0)
			return hournum + strDetailHour + minutenum + strDetailMinute;
		else if(minutenum > 0)
			return minutenum + strDetailMinute;
		else
			return "1"+strDetailMinute;
	}
	
	/**
	 * 获得指定日期的String值"yyyy-M-d HH:mm:ss"，用于客户端显示或者服务器做日期比较
	 * 
	 * @return
	 */
	public static String getStringFormat2Second(long currentDay){
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
		String currentTime = fomat.format(currentDay);
		
		return currentTime;
	}
	
	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的时间转化为long
	 * @param str
	 * @return
	 */
	public static long parseDate(String str) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return simpleDateFormat.parse(str).getTime();
		} catch (Exception e) {
			throw new RuntimeException("时间格式错误：" + str, e);
		}
	}
	
	/**
	 * 传入一个开始时间和结束时间，判断当前时间是否在之间
	 * @param startTime 开始时间 =null("")表示没有开始时间
	 * @param endTime 结束时间 =null("")表示没有结束时间
	 * @return
	 */
//	public static boolean isRunning(String startTime, String endTime) {
//		long now = GameTime.currentTimeMillis();
//		try {
//			if(null == startTime || ("").equals(startTime)) {//没有开始时间
//				if(null == endTime || ("").equals(endTime)) {//没有结束时间
//					return true;
//				} else if(now <= parseDate(endTime)) {//在结束时间之前
//					return true;
//				} else {//在结束时间之后
//					return false;
//				}
//			} else {//有开始时间
//				if(null == endTime || ("").equals(endTime)) {//没有结束时间
//					if(now >= parseDate(startTime)) {//在开始时间之后
//						return true;
//					} else {//在开始时间之前
//						return false;
//					}
//				} else {//有结束时间
//					if(now < parseDate(startTime)
//							|| now > parseDate(endTime)) {
//						return false;
//					} else {
//						return true;
//					}
//				}
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
}
