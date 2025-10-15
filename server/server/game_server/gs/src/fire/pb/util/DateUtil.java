package fire.pb.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;



public class DateUtil {
	public static final Logger LOG = Logger.getLogger("SYSTEM");
	
	
	public static String getCurrentAllTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		StringBuffer now = new StringBuffer();
		now.append(c.get(Calendar.YEAR));
		now.append('-');
		now.append(c.get(Calendar.MONTH) + 1);
		now.append('-');
		now.append(c.get(Calendar.DAY_OF_MONTH));
		now.append('_');
		now.append(c.get(Calendar.HOUR_OF_DAY)<10?"0"+c.get(Calendar.HOUR_OF_DAY):c.get(Calendar.HOUR_OF_DAY));
		now.append(':');
		now.append(c.get(Calendar.MINUTE)<10?"0"+c.get(Calendar.MINUTE):c.get(Calendar.MINUTE));
		now.append(':');
		now.append(c.get(Calendar.SECOND)<10?"0"+c.get(Calendar.SECOND):c.get(Calendar.SECOND));
		return now.toString();
	}
	
	
	public static String getCurrentTimeFomat(){
		SimpleDateFormat fomat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = fomat.format(System.currentTimeMillis());
		return currentTime;
	}
	
	
	
	public static String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		StringBuffer now = new StringBuffer();
		now.append(c.get(Calendar.MONTH) + 1);
		now.append('月');
		now.append(c.get(Calendar.DAY_OF_MONTH));
		now.append('日');
		now.append(c.get(Calendar.HOUR_OF_DAY));
		now.append('时');
		now.append(c.get(Calendar.MINUTE));
		now.append('分');
		return now.toString();		
	}

	
	public static int getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return c.get(Calendar.DAY_OF_MONTH);		
	}
	
	
	public static int getCurrentHour() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getCurrentHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	
	public static int getCurrentMinute() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return c.get(Calendar.MINUTE);
	}
	
	
	
	public static long getHourNumber(){
		return System.currentTimeMillis() / ONE_HOUR;
	}
	
	
	public static long getMsecNumber(){
		return System.currentTimeMillis();
	}
	
	
	public static String format(Date d, String format) {
        if (d == null)
            return "";
        SimpleDateFormat myFormatter = new SimpleDateFormat(format);
        return myFormatter.format(d);
    }

    public static long toLong(Date d) {
        if (d==null)
            return 0;
        else
            return d.getTime();
    }

    public static String toLongString(Date d) {
        return "" + toLong(d);
    }
  
    public static Date parse(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = null;
        try {
        	d = sdf.parse(time);
        } catch (Exception e) {
        	LOG.error("DateUtil.parse异常", e);
        }
        return d;
    }

    public static String format(Calendar cal, String format) {
        if (cal == null)
            return "";
        SimpleDateFormat myFormatter = new SimpleDateFormat(format);
        return myFormatter.format(cal.getTime());
    }

    
    public static Calendar add(java.util.Date d, int day) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, day);
        return cal;
    }

    public static Date addDate(java.util.Date d, int day) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    public static Date addHourDate(java.util.Date d, int h) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR, h);
        return cal.getTime();
    }

    public static Calendar addHour(java.util.Date d, int h) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR, h);
        return cal;
    }

    public static Date addMinuteDate(java.util.Date d, int m) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, m);
        return cal.getTime();
    }

    public static Calendar addMinute(java.util.Date d, int m) {
        if (d == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, m);
        return cal;
    }

    public static int compare(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return -1;
        long r = c1.getTimeInMillis() - c2.getTimeInMillis();
        if (r > 0)
            return 1;
        else if (r == 0)
            return 0;
        else
            return 2;
    }

    public static int compare(Date c1, Date c2) {
        if (c1 == null || c2 == null)
            return -1;
        long r = c1.getTime() - c2.getTime();
        
        if (r > 0)
            return 1;
        else if (r == 0)
            return 0;
        else
            return 2;
    }
    
    public static int datediff(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return -1;
        long r = c1.getTimeInMillis() - c2.getTimeInMillis();
        r = r / (24 * 60 * 60 * 1000);
        return (int) r;
    }
   
    public static int datediff(Date c1, Date c2) {
        if (c1 == null || c2 == null)
            return -1;
        long r = c1.getTime() - c2.getTime();
        r = r / (24 * 60 * 60 * 1000);
        return (int) r;
    }
   
    public static int datediffMinute(Date c1, Date c2) {
        if (c1 == null || c2 == null)
            return 0;
        long r = c1.getTime() - c2.getTime();
        r = r / (60 * 1000);
        return (int) r;
    }

    public static int datediffMinute(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return 0;
        long r = c1.getTimeInMillis() - c2.getTimeInMillis();
        r = r / (60 * 1000);
        return (int) r;
    }
    
    public static int getDayCount(int year, int month) {
        int daysInMonth[] = {
                            31, 28, 31, 30, 31, 30, 31, 31,
                            30, 31, 30, 31};
        
        if (1 == month)
            return ((0 == year % 4) && (0 != (year % 100))) ||
                    (0 == year % 400) ? 29 : 28;
        else
            return daysInMonth[month];
    }
    public static String getYMDDate(Date d)
    {
        Calendar c=Calendar.getInstance();
        c.setTime(d);
        return getChinessNumber(c.get(Calendar.YEAR))+"年"+getChinessNumber((c.get(Calendar.MONTH)+1))+"月" + getChinessNumber(c.get(Calendar.DAY_OF_MONTH))+"日";
    }
    public static String getChinessNumber(int number) {
        StringBuffer chiness = new StringBuffer();
        String temp = String.valueOf(number);
        for (int index = 0; index < temp.length(); index++) {
            String str = "";
            switch (temp.charAt(index)) {
            case '0':
                str = "零";
                break;
            case '1':
                str = "一";
                break;
            case '2':
                str = "二";
                break;
            case '3':
                str = "三";
                break;
            case '4':
                str = "四";
                break;
            case '5':
                str = "五";
                break;
            case '6':
                str = "六";
                break;
            case '7':
                str = "七";
                break;
            case '8':
                str = "八";
                break;
            case '9':
                str = "九";
                break;
            }
            chiness.append(str);
        }
        if (number <= 31) {
            if (number % 10 == 0 && number!=10) {
                chiness.delete(1, 2);
                chiness.replace(1, 1, "十");
            } else if (number > 20) {     
                chiness.insert(1, "十");
            } else if (number > 10) {
                chiness.replace(0, 1, "十");
            } else if (number == 10) {
                chiness.delete(0, 2);
                chiness.insert(0, "十");
            }
        }
        return chiness.toString();

    }
    public static String getFormatDate(Date d)
    {
        Calendar c=Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月" + c.get(Calendar.DAY_OF_MONTH)+"日"+"  " +c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+":"+c.get(Calendar.MILLISECOND);
    }
    public static String getFormatDataByShort(Date d)
    {
        Calendar c=Calendar.getInstance();
        c.setTime(d);
//        return c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + c.get(Calendar.DAY_OF_MONTH)+"  " +(c.get(Calendar.HOUR_OF_DAY)>=10?c.get(Calendar.HOUR_OF_DAY):"0"+c.get(Calendar.HOUR_OF_DAY))+":"+(c.get(Calendar.MINUTE)>=10?c.get(Calendar.MINUTE):"0"+c.get(Calendar.MINUTE));
        return c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-" + c.get(Calendar.DAY_OF_MONTH);
    }
    public static String getFormatDate(Calendar date)
    {
        Calendar c=date;
        return c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月" + c.get(Calendar.DAY_OF_MONTH)+"日"+"  " +c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+":"+c.get(Calendar.MILLISECOND);
    }
    public static String getFormatDate(long l)
    {
        Calendar c=Calendar.getInstance();
        c.setTime(new Date(l));
        return c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月" + c.get(Calendar.DAY_OF_MONTH)+"日"+"  " +c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+":"+c.get(Calendar.MILLISECOND);
    }
	public static String getNextDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1); 
		StringBuffer now = new StringBuffer();
		now.append(c.get(Calendar.YEAR));
		now.append('_');
		now.append(c.get(Calendar.MONTH) + 1);
		now.append('_');
		now.append(c.get(Calendar.DAY_OF_MONTH));
		return now.toString();		
	}
	
	public static boolean checkSunday() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);		 
	}
	
	public static int getWeekDay() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()));
		return c.get(Calendar.DAY_OF_WEEK)-1;		 
	}
	
	
	public static boolean isCompareByDate(long c1,long c2,int num){ //c1保存的使用时间，c2当前时间，num是不是在num天内

		long c = (c2-c1)/(24*60*60*1000);
		if(c>=num){			//如果过了num天，就是真  HCHC
			return true;
		}
		return false;
	}
	public static void main(String[] args){
//		Calendar cal1 = Calendar.getInstance();
//		for (int j=0;j<=7;j++){
//			System.out.println("------------比较天数"+j+"-----------");
//			for (int i=0;i<=7;i++){
//				Calendar cal2 = Calendar.getInstance();
//				cal2.add(Calendar.DATE, i);
//				System.out.println(get(cal1)+" vs "+get(cal2)+"----"+isCompareByDate(cal1.getTimeInMillis(),cal2.getTimeInMillis(),j));
//			}
//		}
	}
	public static String get(Calendar c) {
//		c.setTime(new Date(System.currentTimeMillis()));
		StringBuffer now = new StringBuffer();
		now.append(c.get(Calendar.MONTH) + 1);
		now.append('月');
		now.append(c.get(Calendar.DAY_OF_MONTH));
		now.append('日');
		now.append(c.get(Calendar.HOUR_OF_DAY));
		now.append('时');
		now.append(c.get(Calendar.MINUTE));
		now.append('分');
		return now.toString();		
	}
	public  static int getWeekInYear(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(System.currentTimeMillis()-1000 * 60 * 60*24));
		return c.get(Calendar.WEEK_OF_YEAR);
	}
	
	
	public static final long  ONE_DAY = 1000 * 60 * 60*24;
	
	public static final long ONE_HOUR=1000 * 60 * 60;
	
	public static final long ONE_MIN=1000*60;
	
	public static final long ONE_SECOND=1000;
	
	private static final int TICK_GIVE_GIFT=0; 
	
	public static final long  ONE_MONTH = ONE_DAY *30;
	
	public static final long Time_ZONE=8*60*60*1000;	
	public static long getGameDays(){
		return  (System.currentTimeMillis()+Time_ZONE-ONE_HOUR*TICK_GIVE_GIFT) / ONE_DAY;
	}
	public static long getSystemDays(){
		return  (System.currentTimeMillis()+Time_ZONE) / ONE_DAY;
	}
	public static long getSystemDays(long time){
		return  (time+Time_ZONE) / ONE_DAY;
	}
	
	public  static  int getSecondToHour(int hour){
		Calendar ca =Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, hour);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return (int)(ca.getTimeInMillis()-System.currentTimeMillis())/1000;
	}
	
	
	public  static long getNextDayTime(int hour,int minute){
		Calendar ca =Calendar.getInstance();
		ca.add(Calendar.DATE, 1);
		ca.set(Calendar.HOUR_OF_DAY, hour);
		ca.set(Calendar.MINUTE, minute);
		ca.set(Calendar.SECOND, 0);
		return ca.getTimeInMillis();
	}
	
	
	public  static long getNextDayTime(int hour){
		Calendar ca =Calendar.getInstance();
		ca.add(Calendar.DATE, 1);
		ca.set(Calendar.HOUR_OF_DAY, hour);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca.getTimeInMillis();
	}
	public static String getStrTime(long time){
		long hour=time/ONE_HOUR;
		long mint=time%ONE_HOUR/ONE_MIN;
		long sec=time%ONE_HOUR%ONE_MIN/ONE_SECOND;
		StringBuilder sb=new StringBuilder();
		String a=":";
		sb.append(hour);
		sb.append(a);
		if(mint<10){
			sb.append("0");
		}
		sb.append(mint);
		sb.append(a);
		if(sec<10){
			sb.append("0");
		}
		sb.append(sec);
		return sb.toString();
	}
	
	public  static long getTodayTime(int hour,int minute){
		Calendar ca =Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, hour);
		ca.set(Calendar.MINUTE, minute);
		ca.set(Calendar.SECOND, 0);
		return ca.getTimeInMillis();
	}
	
	public  static long getTodayTime(int hour){
		Calendar ca =Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, hour);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca.getTimeInMillis();
	}
	public static String getStrDayTime(long time){
		long day=time/ONE_DAY;
		long hour=time%ONE_DAY/ONE_HOUR;
		long mint=time%ONE_DAY%ONE_HOUR/ONE_MIN;
		StringBuilder sb=new StringBuilder();
		if(day>0){
			sb.append(day+"天");
		}
		if(hour>0){
			sb.append(hour+"小时");
		}else{
			if(mint>0){
				sb.append(hour+1+"小时");
			}
		}
		return sb.toString();
	}
	public static String getStrMonthTime(long time){
		String result="";
		time=System.currentTimeMillis()-time;
		long month=time/ONE_MONTH;
		long day=time/ONE_DAY;
		long hour=time/ONE_HOUR;
		if(month>0){
			result= month+"个月前";
		}else if(day>0){
			result= day+"天前";
		}else if(hour>0){
			result= hour+"小时前";
		}else{
			result="刚刚";
		}
		return result;
	}
	
	public static long getTime(int time){
		int year = Calendar.getInstance().get(Calendar.YEAR);			
		int month = Calendar.getInstance().get(Calendar.MONTH)+1;
		int day =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int curdate =year *10000 + month*100+day; 
		String s=time+"";
		if(s.equals("0")){
			s="0000";
		}
		String str = curdate+s;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		long millionSeconds =0;
		try {
			millionSeconds=sdf.parse(str).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
		}// 毫秒
		return millionSeconds;
	}
	
	public static long getTime(int curdate,int time){
		String s=time+"";
		if(s.equals("0")){
			s="0000";
		}
		String str = curdate+s;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		long millionSeconds =0;
		try {
			millionSeconds=sdf.parse(str).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOG.error("", e);
		}// 毫秒
		return millionSeconds;
	}
	
	public static boolean isSameDate(long time){
		long time1=getSystemDays(time);
		long time2=getSystemDays(System.currentTimeMillis());
		if(time1!=time2){
			return false;
		}
		return true;
		
	}
	public static int getDayNum(long time){
		if(time<0){
			return 0;
		}
		long day=time/ONE_DAY;	
		return (int)day;
	}
	public static int getHourNum(long time){
		if(time<0){
			return 0;
		}		
		long hour=time%ONE_DAY/ONE_HOUR;		
		return (int)hour;
	}
	public static int getMinitNum(long time){
		if(time<0){
			return 0;
		}
		long mint=time%ONE_DAY%ONE_HOUR/ONE_MIN;
		return (int) mint;
	}
	
	
	
	public static String getCreateMailTime(long creatTime){
		int time = (int) ((System.currentTimeMillis() - creatTime) /DateUtil.ONE_DAY);
		if(time>1){
			return time+"天前";
		}
		return DateUtil.getTimeShort(new Date(creatTime));
	}
	
	
	
	public static String getTimeShort(Date currentTime) {
	   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}

	public static String getTimeString(int time){
		//先转换成秒
		time=time/1000;
		String str="";
		int hour=time/(60*60);
		int mm=time%(60*60)/60;
		int ss=time%(60*60)%(60);
		if(hour!=0){
			str=hour+":";
		}
		str+=mm+":";
		if(ss==0){
			str+="00";
		}else if(ss<10){
			str+="0"+ss;
		}else{
			str+=ss;
		}
		return str;
	}
	public static String getActionDateString(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		StringBuffer now = new StringBuffer();
		now.append(c.get(Calendar.YEAR));
		now.append("/");
		now.append(c.get(Calendar.MONTH) + 1);
		now.append("/");
		now.append(c.get(Calendar.DAY_OF_MONTH));
		return now.toString();		
	}
	
	
	public static String getTimeByInt(int time){
		String str="";
		int hour=time/100;
		int mm=time%100;
		if(hour!=0){
			str=hour+":";
		}else{
			str="0:";
		}
		if(mm==0){
			str+="00";
		}else{
			str+=mm;
		}
		return str;
	}
	
	
	
	
}

