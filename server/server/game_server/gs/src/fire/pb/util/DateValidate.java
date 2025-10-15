package fire.pb.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fire.pb.statistics.StatisticUtil;


public class DateValidate {
	
	
	public static final long minuteMills = 1000 * 60;
	public static final long hourMills = 1000 * 60 * 60;
	public static final long dayMills = hourMills * 24;

	public static final long weekMills = dayMills * 7;
	
	public static final long TIME_ZONE_OFFSET = Calendar.getInstance().getTimeZone().getRawOffset();//时区导致的差值
	
	public static final long addMills = 1000 * 60 * 60 * 24 * 3;//Java时间的第一天（1970.1.1）是周四，这个是把一周的前三天补全的时间（以周一为第一天）
	
	private static final long TIME_ZONE       = 1000 * 60 * 60 * 8;//0点
	//周一凌晨的时间偏移
	private static final long WEEK_OFFSET     = 1000 * 60 * 60 * 24 * 3;
	//全部时间偏移
	private static final long TOTAL_OFFSET = TIME_ZONE + WEEK_OFFSET;
	//一周的毫秒数
	private static final long WEEK_TIME       = 1000 * 60 * 60 * 24 * 7;
	//一天的毫秒数
	private static final long DAY_TIME       = 1000 * 60 * 60 * 24;

	public static boolean inTheSameDay(long firstT,long secondT) {
		if (StatisticUtil.getCurrentDay(firstT) == StatisticUtil.getCurrentDay(secondT))
			return true;
		return false;
	}
	public static boolean inTheSameWeek(long firstT,long secondT) {
		if (StatisticUtil.getCurrentWeek(firstT) == StatisticUtil.getCurrentWeek(secondT))
			return true;
		return false;
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
	
	//获得今天的最后一秒
	public static Calendar getLastSecondCalendar(){
		Calendar current = Calendar.getInstance();
		current.set(Calendar.HOUR_OF_DAY, 23);
		current.set(Calendar.MINUTE, 59);
		current.set(Calendar.SECOND, 59);
		return current;
	}
	
	
	//获得今天的最后一秒
	public static Calendar getcurrentCalendar(){
		Calendar current = Calendar.getInstance();
		current.setTimeInMillis(System.currentTimeMillis());
		return current;
	}
	
	//获取某天的第一秒
	public static long getDayFirstSecond(long time){
		return time - (time + TIME_ZONE_OFFSET) % dayMills;
	}
	//获取某周的第一秒（以 周一算）
	public static long getWeekFirstSecond(long time){
		return time - (time + TIME_ZONE_OFFSET + addMills) % weekMills;
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
	
	public static String formatTimeByType(long time, String format){
		String myFormat = null == format ? "yyyy年M月d日    HH:mm:ss" : format;
		SimpleDateFormat fomat = new SimpleDateFormat(myFormat);
		return fomat.format(time);
	}
	
	public static boolean inTheSameYear(long firstT, long secondT){
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		cal1.setTimeInMillis(firstT);
		cal2.setTimeInMillis(secondT);
		
		if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
			return true;
		return false;
	}
	
	
	public static boolean isInTimeSubsection(String begin, String end){
		try{
			begin = begin.replace("：", ":");
			end = end.replace("：", ":");
			
			String[] beginArray = begin.split(":");
			String[] endArray = end.split(":");
			if(beginArray.length<2 || endArray.length <2){
				throw new RuntimeException("时间段格式错误：");
			}
			
			int beginHour = Integer.parseInt(beginArray[0]);
			int beginMin = Integer.parseInt(beginArray[1]);
			int endHour = Integer.parseInt(endArray[0]);
			int endMin = Integer.parseInt(endArray[1]);			
			
			if(beginHour>endHour ||
					beginHour<0 || beginHour>24 ||
					endHour<0 || endHour > 24){
				throw new RuntimeException("结束时间段 应当大于开始时间段");
			}
			
			Calendar calendar = Calendar.getInstance(); 
			calendar.clear(Calendar.HOUR_OF_DAY);   
			calendar.clear(Calendar.MINUTE);
			calendar.set(Calendar.HOUR_OF_DAY, beginHour);   
			calendar.set(Calendar.MINUTE, beginMin);
			long startTime = calendar.getTimeInMillis();
			
			calendar.clear(Calendar.HOUR_OF_DAY);   
			calendar.clear(Calendar.MINUTE);
			calendar.set(Calendar.HOUR_OF_DAY, endHour);   
			calendar.set(Calendar.MINUTE, endMin);
			long endTime = calendar.getTimeInMillis();
			long now = System.currentTimeMillis();
			if(startTime <=now  && endTime>=now){
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static String getTodayStr(long cur){
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat1.format(new Date(cur));
	}
	
	public static long parseDate(String str) {
		try {
			str = str.replace("：", ":");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return simpleDateFormat.parse(str).getTime();
		} catch (Exception e) {
			throw new RuntimeException("时间格式错误：" + str, e);
		}
	}
	
	
	public static int getMonthSpace(long time1, long time2){
		Calendar calendar1 = Calendar.getInstance(); 
		Calendar calendar2 = Calendar.getInstance(); 
		calendar1.setTimeInMillis(time1);
		calendar2.setTimeInMillis(time2);
		
		int year = calendar1.get(Calendar.YEAR)-calendar2.get(Calendar.YEAR);
		int mounth = calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH);
		
		int result = year *12 + mounth;
		return Math.abs(result);
	}
	
	
	
	public static int getDaysBetween(long time1, long time2){
		Calendar d1 = Calendar.getInstance(); 
		Calendar d2 = Calendar.getInstance(); 
		d1.setTimeInMillis(time1);
		d2.setTimeInMillis(time2);
		
		if (d1.after(d2)) {
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR)- d1.get(java.util.Calendar.DAY_OF_YEAR);
		int y2 = d2.get(java.util.Calendar.YEAR);
		if (d1.get(java.util.Calendar.YEAR) != y2) {
			d1 = (java.util.Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				d1.add(java.util.Calendar.YEAR, 1);
			} while (d1.get(java.util.Calendar.YEAR) != y2);
		}
		return Math.abs(days);
	}
	
	
	
	public static int getDaysBetween1(long time1, long time2){
		long remain = Math.abs(time1 - time2);
		return (int)(remain/dayMills);
	}
	
	
	public static int getCurrentMonthDays() {
		Calendar c = Calendar.getInstance();
		return c.getActualMaximum(Calendar.DATE);
	}
	
	
	public static String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		StringBuffer now = new StringBuffer();
		now.append(c.get(Calendar.YEAR));
		now.append('-');
		now.append(c.get(Calendar.MONTH) + 1);
		now.append('-');
		now.append(c.get(Calendar.DAY_OF_MONTH));
		return now.toString();		
	}
	
	public static String getTimeString(long time) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(time));
		StringBuffer now = new StringBuffer();
		now.append(c.get(Calendar.YEAR));
		now.append('-');
		now.append(c.get(Calendar.MONTH) + 1);
		now.append('-');
		now.append(c.get(Calendar.DAY_OF_MONTH));
		return now.toString();		
	}
	
	
	public static long getWeekNumber(){
		return (System.currentTimeMillis() + TOTAL_OFFSET) / WEEK_TIME;
	}
	
	/**
	 * 获得当前的天数
	 * @return
	 */
	public static long getDayNumber(){
		return (System.currentTimeMillis() + TOTAL_OFFSET) / DAY_TIME;
	}
	
	
	public static void main(String[] args){
		System.out.println((System.currentTimeMillis() + TOTAL_OFFSET) / DAY_TIME);
		System.out.println((System.currentTimeMillis() + TOTAL_OFFSET) / WEEK_TIME);
	}
	

	
}
