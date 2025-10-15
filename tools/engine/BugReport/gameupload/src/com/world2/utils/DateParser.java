package com.world2.utils;

import java.text.*;
import java.util.*;;
/*
 * This class parse input parameters, convert
 * date range to a date string array in one day
 * interval. For example, 
 * input   "20070831","20070902"
 * output  "20070831","20070901","20070902"
 */
public class DateParser
{
	private static final DateParser instance = new DateParser();
	private static Hashtable<String,SimpleDateFormat> dateFormat;
	public static final String DATE_FORM = "yyyyMMdd";
	public static final String DATE_FORM2 = "yyyy-MM-dd";
	public static final String DATE_FORM3 = "yyyyMM";
	public static final String TIME_FORM = "HH:mm";
	public static final String DATETIME_FORM = "yyyyMMdd HH:mm";
	public static final String DATETIME_FORM2 = "yyyy-MM-dd-HH:mm:ss";

	private DateParser(){
		dateFormat = new Hashtable<String,SimpleDateFormat>();
		SimpleDateFormat temp = new SimpleDateFormat(DATE_FORM);
		dateFormat.put(DATE_FORM, temp);
		temp = new SimpleDateFormat(DATE_FORM2);
		dateFormat.put(DATE_FORM2, temp);
		temp = new SimpleDateFormat(DATE_FORM3);
		dateFormat.put(DATE_FORM3, temp);
		temp = new SimpleDateFormat(TIME_FORM);
		dateFormat.put(TIME_FORM, temp);
		temp = new SimpleDateFormat(DATETIME_FORM);
		dateFormat.put(DATETIME_FORM, temp);
		temp = new SimpleDateFormat(DATETIME_FORM2);
		dateFormat.put(DATETIME_FORM2, temp);
	}
	/**
	 * define static functions
	 */
	public static DateParser getInstance()
	{
		return instance;
	}
	
	/*define non-static member functions */
	public synchronized String[] parseRange(String startDate,String endDate)
	{
		String[] range={startDate,endDate};
		return parseRange(range);
	}
	
	public synchronized String[] parseRange(Calendar start,Calendar end)
	{
		Vector<String> dateArray=new Vector<String>();
		String[] result = null;
		if ( start == null || end == null )
			return null;
		try
		{
			Date date1 = start.getTime();
			Date date2 = end.getTime();
			Calendar caldr = Calendar.getInstance();
			Date now = dateFormat.get(DATE_FORM).parse( dateFormat.get(DATE_FORM).format(caldr.getTime()) );

			if ( date1.after(date2) )
			{
				Date tmp=date2;
				date2=date1;
				date1=tmp;
			}
			if ( date1.after(now) )
				return new String[0]; 

			if ( date2.after(now) )
			{
				date2 = dateFormat.get(DATE_FORM).parse( dateFormat.get(DATE_FORM).format(now) );
			}
			caldr.setTime(date1);
			while (caldr.getTime().before(date2) )
			{
				dateArray.add( dateFormat.get(DATE_FORM).format(caldr.getTime()) );
				caldr.add(Calendar.DATE,1);
			}
			dateArray.add( dateFormat.get(DATE_FORM).format(date2) );
		}
		catch ( ParseException pe )
		{
			pe.printStackTrace();
			return null;
		}
		result = new String[dateArray.size()];
		return dateArray.toArray(result);
	}
	
	public synchronized String[] parseRange(String[] input)
	{
		Vector<String> dateArray=new Vector<String>();
		String[] result=null;
		if ( input==null || input.length<2 )
			return null;
		try
		{
			Date date1=dateFormat.get(DATE_FORM).parse(input[0]);
			Date date2=dateFormat.get(DATE_FORM).parse(input[1]);
			Calendar caldr=Calendar.getInstance();
			Date now=dateFormat.get(DATE_FORM).parse( dateFormat.get(DATE_FORM).format(caldr.getTime()) );

			if ( date1.after(date2) )
			{
				Date tmp=date2;
				date2=date1;
				date1=tmp;
			}
			if ( date1.after(now) )
				return new String[0]; 

			if ( date2.after(now) )
			{
				date2 = dateFormat.get(DATE_FORM).parse( dateFormat.get(DATE_FORM).format(now) );
			}
			caldr.setTime(date1);
			while (caldr.getTime().before(date2) )
			{
				dateArray.add( dateFormat.get(DATE_FORM).format(caldr.getTime()) );
				caldr.add(Calendar.DATE,1);
			}
			dateArray.add( dateFormat.get(DATE_FORM).format(date2) );
		}
		catch ( ParseException pe )
		{
			pe.printStackTrace();
			return null;
		}
		result = new String[dateArray.size()];
		return dateArray.toArray(result);
	}

   /**
	*parse date to calendar
	*@date format:yyyyMM
	*/
	public synchronized LinkedList<String> monthRangeParse(String fromMonth,String toMonth)
	{
		if(null == fromMonth || null == toMonth)
			return new LinkedList<String>();
		String startMonth = fromMonth;
		if(fromMonth.length()>6)
			startMonth = fromMonth.substring(0,6);
		String endMonth = toMonth;
		if(toMonth.length()>6)
			endMonth = toMonth.substring(0,6);
		LinkedList<String> months = new LinkedList<String>();
		try
		{
			Calendar fromCal= Calendar.getInstance();
			Date from = dateFormat.get(DATE_FORM3).parse(startMonth);	
			Date to = dateFormat.get(DATE_FORM3).parse(endMonth);
			//Date now = new Date();
			
			/* to make sure from is smaller than to*/
			if(from.after(to))
			{
				Date temp = to;
				to = from;
				from = temp;
			}
			/*make sure form and to are both smaller than now*/	
			/*if(from.after(now))
				return months;	
			if(to.after(now))
				to.setMonth(now.getMonth());*/
			
			fromCal.setTime(from);
			while(fromCal.getTime().before(to))
			{
				months.add(dateFormat.get(DATE_FORM3).format(fromCal.getTime()));
				fromCal.add(Calendar.MONTH,1);
			}

			months.add(dateFormat.get(DATE_FORM3).format(to));
		}
		catch( ParseException pe)
		{
			pe.printStackTrace();
			return null;
		}
		return months;
	}
	/**
	 * ???????????????﹞?
	 * @param start   ????????"yyyyMMdd"
	 * @param end     ????????  "yyyyMMdd"
	 * @return        ?﹞?
	 */
	public synchronized LinkedList<String> monthOfDay(String start,String end){
		if(null == start || null == end)
			return null;
		if(start.length()<6 || end.length()<6)
			return null;
		return monthRangeParse(start.substring(0,6),end.substring(0,6));
	}

	public synchronized LinkedList<String> parseRange(String fromDate,String toDate,int dateType,int interval)
	{
		LinkedList<String> dates = new LinkedList<String>();
		try
		{
			Calendar fromCal= Calendar.getInstance();
			Date from = dateFormat.get(DATE_FORM).parse(fromDate);	
			Date to = dateFormat.get(DATE_FORM).parse(toDate);
			Date now = new Date();
			
			/* make sure from is smaller than to*/
			if(from.after(to))
			{
				Date temp = to;
				to = from;
				from = temp;
			}
			/*make sure form and to are both smaller than now*/	
			if(from.after(now))
				return dates;	
			if(to.after(now))
				to.setMonth(now.getMonth());
			
			fromCal.setTime(from);
			if(interval ==0 )
				interval = 1;
			while(fromCal.getTime().before(to))
			{
				dates.add(dateFormat.get(DATE_FORM).format(fromCal.getTime()));
				fromCal.add(dateType,interval);
			}
			if(from.equals(to))//from equal to to represent a single month
				dates.add(dateFormat.get(DATE_FORM).format(to));
		}
		catch( ParseException pe)
		{
			pe.printStackTrace();
			return null;
		}
		return dates;
	}	
 
	/**
	 * get today
	 * format used: yyyyMMdd
	 */
	public String getToday(){
		Calendar cal = Calendar.getInstance();
		return dateFormat.get(DATE_FORM).format(cal.getTime());
	}
	/**
	 * ?????long??string
	 * @param st
	 * @return  "yyyyMMdd"????????
	 */
	public synchronized String getToday(long st){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(st);
		return dateFormat.get(DATE_FORM).format(cal.getTime());
	}
	
	public synchronized String getToday(String format,Calendar cal){
		return dateFormat.get(format).format(cal.getTime());
	}
	
	/**
	 * change format to long
	 * format: DateParser's attribute
	 */
	public synchronized long getTimeLong(String format,String _t){
		if(null == _t)
			return 0;
		long time = 0;
		try {
			 time = dateFormat.get(format).parse(_t).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
		return time;
	}
	
	/**
	 * change format yyyy-MM-dd to yyyyMMdd
	 * @param day
	 * @return
	 */
	public synchronized String changeFormat(String day){
		SimpleDateFormat dateFmt = dateFormat.get(DATE_FORM2);
		try {
			return dateFormat.get(DATE_FORM).format(dateFmt.parse(day));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * ??Long????????????????
	 * @param day
	 * @return
	 */
	public synchronized String getDayTime(long day){
		return dateFormat.get(DATETIME_FORM).format(new Date(day));
	}
	
	public synchronized String getDayTme(String format,Date daytime){
		return dateFormat.get(format).format(daytime);
	}
	
	/**
	 * ?忪???????????????
	 * return 0:??????
	 *        1?????
	 *        -1????????????
	 */
	public synchronized int ifWeekDay(String day){
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(dateFormat.get(DATE_FORM).parse(day));
			int day_week = cal.get(Calendar.DAY_OF_WEEK);
			if(day_week==Calendar.SATURDAY || day_week==Calendar.SUNDAY)
				return 1;
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	/**
	 * ????????????快??????
	 * @param day       ????yyyyMMdd
	 * @return
	 */
	public synchronized int getWeekDay(String day){
		Calendar cal = Calendar.getInstance();
		long time = this.getTimeLong(DATE_FORM,day);
		if(0 == time)
			return -1;
		cal.setTimeInMillis(time);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * ?????????????????????
	 */
	public synchronized boolean checkDayForamt(String day){
		if(null == day)
			return false;
		try {
			dateFormat.get(DATE_FORM).parse(day);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	/**
	 * ???????????妊?
	 * @param time
	 * @return
	 */
	public synchronized String getHourFromLong(long time){
		long min = time/60000;
		long hour = min/60;
		min = min%60;
		if(min<30)
			return String.valueOf(hour);
		else
			return String.valueOf((double)hour+((double)min)/60.0);
	}
	/**
	 * ???????????妊????妊?
	 */
	public synchronized String getDoubleHour(long time){
		double hour = ((double)time)/3600000;
		DecimalFormat df  = new DecimalFormat("0.00");
		return df.format(hour);
	}
	
	/**
	 * ?????????????????????快??????
	 * @param day
	 * @return
	 */
	public synchronized int getDayOfMonth(String day){
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(dateFormat.get(DATE_FORM).parse(day));
			return cal.get(Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * ?????????????????
	 * @param time??????????
	 * @return ??? ?? 9:00,17:30
	 */
	public synchronized String getTimeFromLong(long time){
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTimeInMillis(time);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minut = cal.get(Calendar.MINUTE);
			StringBuilder builder = new StringBuilder();
			if(hour<10){
				builder.append("0");
			}
			builder.append(hour);
			builder.append(":");
			if(minut < 10)
				builder.append(0);
			builder.append(minut);
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * ??"yyyyMMdd" ????"yyyyMM"?快?????
	 * @param date   ????
	 * @return       ???
	 */
	public synchronized String getYear(String date){
		if(null == date)
			return null;
		if(date.length()<4)
			return null;
		return date.substring(0,4);
	}
	
	/**
	 * ??"yyyyMMdd" ????"yyyyMM"?快???﹞?
	 * @param date   ????
	 * @return       ?﹞?
	 */
	public synchronized String getYearMonth(String date){
		if(null == date)
			return null;
		if(date.length()<6)
			return null;
		return date.substring(0,6);
	}
	
	public synchronized String getMonth(long time){
		return dateFormat.get(DATE_FORM3).format(new Date(time));
	}
	/**
	 * ?????????????﹞??
	 * @param time    ?????
	 * @return        }???????﹞??
	 */
	public synchronized int SubMonth(long time){
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(time);
		return java.lang.Math.abs(cal.get(Calendar.MONTH)-cal2.get(Calendar.MONTH));
	}
	
	/**
	 * ?????????????
	 * @param   date    ????yyyyMMdd
	 */
	public synchronized double getWorkYears(String date){
		if(null == date)
			return 0;
		
		//??????
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.MONTH,-1);
		long time = this.getTimeLong(DATE_FORM,date);
		//??????
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(time);
		int oldyear = cal2.get(Calendar.YEAR);
		int thisyear = cal.get(Calendar.YEAR);
		double day = 0.0;
		while(cal2.before(cal)){
			cal2.add(Calendar.DAY_OF_YEAR,1);
			day += 1;
		}
		double year = day/365;
		if(year < 1)
			return 0;
		if( (oldyear+1) == thisyear )
			return 1;
		return year;
	}
	/**
	 * ???????????? day of year
	 * @param date   ???? ???yyyyMMdd
	 * @return       day of year
	 */
	public synchronized int getWorkOneYearDays(String date){
		if(null == date)
			return 0;
		Calendar cal = Calendar.getInstance();
		long time = this.getTimeLong(DATE_FORM,date);
		cal.setTimeInMillis(time);
		cal.add(Calendar.YEAR,1);
		return cal.get(Calendar.DAY_OF_YEAR);
	}
	/**
	 * ????﹞????????
	 * @param month     ???yyyyMM
	 * @return          ?米???????
	 */
	public synchronized int getMaxDayofMonth(String month){
		Calendar cal = Calendar.getInstance();
		long time = getTimeLong(DateParser.DATE_FORM3,month);
		if(time<=0)
			return 0;
		cal.setTimeInMillis(time);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	/**
	 * ??????????
	 * @return  ??????? yyyyMM
	 */
	public String getLastMonth(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		return dateFormat.get(DATE_FORM3).format(cal.getTime());
	}
	
	public static void main(String[] args){
		/*String start = "20081201";
		String end = "20081218";
		List<String> months = DateParser.getInstance().monthRangeParse(start,end);
		for(String month:months)
			System.out.println(month);*/
		System.out.println(DateParser.getInstance().getToday(DateParser.DATETIME_FORM2,Calendar.getInstance()));
	}
}
