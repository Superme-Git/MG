
package fire.pb.activity;

import java.util.Calendar;

import fire.pb.statistics.StatisticUtil;

public class ActivityCalendar {

	//当玩家完成某任务一天能做的所有次数后调用该方法,用于活动日历的显示
	public static void finishOnedayTask(long roleid, int activityCalendarid) {
       xbean.ActivityCalendarStatus status = xtable.Activitycalendar.get(roleid);
       if (status==null){
    	   status = xbean.Pod.newActivityCalendarStatus();
    	   xtable.Activitycalendar.insert(roleid, status);
       }
       Calendar now = Calendar.getInstance();
       int currentWeek=StatisticUtil.getCurrentWeek(now.getTimeInMillis());
       if (currentWeek!=status.getWeek()){
    	   status.getDaystatus().clear();
    	   status.setWeek(currentWeek);
       }
       int dayOfWeek=now.get(Calendar.DAY_OF_WEEK);
       int key = dayOfWeek-1;//减去1刚好就是key
       xbean.DayFinishTask dayFinishTask = status.getDaystatus().get(key);
       if (dayFinishTask==null){
    	   dayFinishTask = xbean.Pod.newDayFinishTask();
    	   status.getDaystatus().put(key, dayFinishTask);
       }
       dayFinishTask.getTasklist().add(activityCalendarid);
	}

	
}
