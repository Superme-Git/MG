package fire.pb.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class CalendarUtil {
	
	
	public static long getEndTimeByStartTime(int validityTime){
		long currentTime = System.currentTimeMillis();
		long validityLongTime = 1000*60*60*24*(long)validityTime;
		
		return currentTime + validityLongTime;
	}
	
	public static long getTimeByContinueDays(int days) {
		long timeret = 1000*60*60*24*(long)days;
		return timeret;
	}
	
	
	public static String getTodayStringFormat(){
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy年M月d日");
		String currentTime = fomat.format(System.currentTimeMillis());
		
		return currentTime;
	}
	
	
	public static int getMondayPlus(){
		Calendar cd = Calendar.getInstance();
		
		//获取今天是一周的第几天
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) -1;
		if(1 == dayOfWeek){
			return 0;
			
		}else{
			return 1 - dayOfWeek;
		}
		
	}
	
	
	public static int getSundayPlus(){
		Calendar cd = Calendar.getInstance();
		
		//获取今天是一周的第几天
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) -1;
		if (0 == dayOfWeek) {
			return dayOfWeek;
			
		}
		else {
			return 7 - dayOfWeek;
		}
		
	}
	
	
	public static int getHourValueInCurrentDay(){
		Calendar cd = Calendar.getInstance();
		
		return cd.get(Calendar.HOUR_OF_DAY);
	}
	
	
	
	public static long getNextWeekMonday(){
		int mondayPlus = getMondayPlus();
		
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + 7);

		return currentDate.getTimeInMillis();
	}
	
	
	public static long getThisWeekSunday() {
		int sundayPlus = getSundayPlus();
		
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, sundayPlus);
		
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);

		return currentDate.getTimeInMillis();
	}
	
	
	public static String getCurrentStringFormat(long currentDay){
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy年M月d日");
		String currentTime = fomat.format(currentDay);
		
		return currentTime;
	}
	

	
	public static Integer getCurrentDay(long currentDay){
		SimpleDateFormat fomat = new SimpleDateFormat("yyyyMMdd");
		String currentTime = fomat.format(currentDay);
		return Integer.valueOf(currentTime);
	}
	
	
	public static int getLastListFormat(long period) {
		long currentTime = System.currentTimeMillis();
		long lasttime = period - currentTime;
		
		if (lasttime < 0)
			return 0;
		
		int minutenum = (int)(lasttime / DateValidate.minuteMills);
		return minutenum;
	}
	
	public static long getLastListSecFormat(long period) {
		long currentTime = System.currentTimeMillis();
		long lasttime = period - currentTime;
		
		if (lasttime < 0)
			return 0;
		
		int minutenum = (int)(lasttime / 1000);
		return minutenum;
	}
	
	public static List<Integer> getPeriodListFormat(long period){
		
		List<Integer> retList = new ArrayList<Integer>();
		long daynum = period /  DateValidate.dayMills;
		long dayret = period %  DateValidate.dayMills;
		
		long hournum = dayret / DateValidate.hourMills;
		long hourret = dayret % DateValidate.hourMills;
		
		long minutenum = hourret / DateValidate.minuteMills;
		
		retList.add((int)daynum);
		retList.add((int)hournum);
		retList.add((int)minutenum);
		
		return retList;
	}
	
	
	
	
	public static String getPeriodStringFormat(long period){
		
		long daynum = period /  DateValidate.dayMills;
		long dayret = period %  DateValidate.dayMills;
		
		long hournum = dayret / DateValidate.hourMills;
		long hourret = dayret % DateValidate.hourMills;
		
		long minutenum = hourret / DateValidate.minuteMills;
		
		return daynum + "天" + hournum + "时" + minutenum + "分";
	}
	
	
	public static String getPeriodShortFormat(long period){
		
		long daynum = period /  DateValidate.dayMills;
		long dayret = period %  DateValidate.dayMills;
		
		long hournum = dayret / DateValidate.hourMills;
		long hourret = dayret % DateValidate.hourMills;
		
		long minutenum = hourret / DateValidate.minuteMills;
		if(daynum > 0)
			return daynum + StringConstant.ID2String("天", 256) + hournum + StringConstant.ID2String("小时",257) + minutenum + StringConstant.ID2String("分钟",258);
		else if(hournum > 0)
			return hournum + StringConstant.ID2String("小时",257) + minutenum + StringConstant.ID2String("分钟",258);
		else if(minutenum > 0)
			return minutenum + StringConstant.ID2String("分钟",258);
		else
			return "1"+StringConstant.ID2String("分钟",258);
	}
	
	
	public static String getCurrentStringFormat2Second(long currentDay){
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
		String currentTime = fomat.format(currentDay);
		
		return currentTime;
	}
	
	
	public static String getCurrentStringFormat2Second() {
		Date now = new Date();
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
		String currentTime = fomat.format(now);
		
		return currentTime;
	}
	
	
	public static long getCurrentWeekMonday(){
		Calendar current = Calendar.getInstance();
		
		int mondayPlus = current.get(Calendar.DAY_OF_WEEK);
		GregorianCalendar currentData = new GregorianCalendar();
		currentData.add(Calendar.DATE, mondayPlus -7);
		
		return currentData.getTimeInMillis();
	}
	
	
	public static long getCurrentMonthFristDay(){
		Calendar current = Calendar.getInstance();
		
		int dayOfMonth = current.get(Calendar.DAY_OF_MONTH) - 1;
		GregorianCalendar currentData = new GregorianCalendar();
		currentData.add(Calendar.DAY_OF_MONTH, -dayOfMonth);
		
		return currentData.getTimeInMillis();
		
	}
	
	
	public static long getCurrentMonthEndDay(){
		Calendar current = Calendar.getInstance();
		
		int dayOfMonth = current.get(Calendar.DAY_OF_MONTH);
		int endDay = current.getActualMaximum(Calendar.DAY_OF_MONTH);
		int space = endDay - dayOfMonth;
		GregorianCalendar currentData = new GregorianCalendar();
		currentData.add(Calendar.DAY_OF_MONTH, space);
		
		
		return currentData.getTimeInMillis();
	}
	
	
	public static String getDayTimePeriodString(long start, long end)
	{
		if(start > end)
			return null;
		if(end >  DateValidate.dayMills)
			return null;
		return "每日"+getClockTime(start)+"到"+getClockTime(end);
	}
	
	
	public static String getWeekTimePeriodString(long start, long end)
	{
		if(start > end)
			return null;
		if(end >  DateValidate.weekMills)
			return null;
		String startstr = getWeekClockTime(start);
		String endstr = getWeekClockTime(end);
		if(startstr.substring(0, 2).equals(endstr.substring(0,2)))
			endstr = endstr.substring(2);//如果是同一天，省略后一个周X的描述
		return startstr+"到"+endstr;
	}
	
	public static String getClockTime(long time)
	{
		if(time >= DateValidate.dayMills)
			return null;
		long hournum = time / DateValidate.hourMills;
		long hourret = time % DateValidate.hourMills;
		long minutenum = hourret / DateValidate.minuteMills;
		
		return String.format("%02d:%02d",hournum,minutenum);
	}
	
	public static String getWeekClockTime(long time)
	{
		if(time >= DateValidate.weekMills)
			return null;
		int daynum = (int)(time /  DateValidate.dayMills);
		long dayret = time %  DateValidate.dayMills;
		String clocktime = getClockTime(dayret);
		switch(daynum)
		{
		case 0:
			return "周一"+clocktime;
		case 1:
			return "周二"+clocktime;
		case 2:
			return "周三"+clocktime;
		case 3:
			return "周四"+clocktime;
		case 4:
			return "周五"+clocktime;
		case 5:
			return "周六"+clocktime;
		case 6:
			return "周日"+clocktime;
		}
		return null;
	}
	
	public static void main(String[] args)
	{
//		long days = getEndTimeByStartTime(30);
//		System.out.println(days);
		//System.out.println(getWeekTimePeriodString(2700 * 60 * 1000, 2820 * 60 * 1000));1452141000000
		long timme = System.currentTimeMillis() - 1452141000000L;
		System.out.println(getPeriodStringFormat(timme));
	}
}
