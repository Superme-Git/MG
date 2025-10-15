
package fire.pb.timer;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.npc.SActivityNpcPos;
import fire.pb.statistics.StatisticUtil;
import mkdb.Procedure;
import mkdb.Transaction;
import mkdb.TTable.IWalk;


public class ActivityManager {

	static Properties prop = ConfigManager.getInstance()
			.getPropConf("activity");

	private static final Map<Integer, AbstractScheduledActivity> activityMap = new ConcurrentHashMap<Integer, AbstractScheduledActivity>();

	/**
	 * 类型和活动id映射
	 */
	private static final Map<Class<?>, Integer> TYPE_ACTIVE_ID = new ConcurrentHashMap<Class<?>, Integer>();

	private static Map<Integer, SActivityNpcPos> activityNpcPosMap = null;

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//每天答题活动 by changhao
	static private Map<Integer, ScheculedActivity> answerQuestionActivity = new java.util.HashMap<Integer, ScheculedActivity>();
	
	static private Map<Integer, ScheculedActivity> clanfightpreActivity = new java.util.HashMap<Integer, ScheculedActivity>(); //公会战准备 by changhao
	static private Map<Integer, ScheculedActivity> clanfightActivity = new java.util.HashMap<Integer, ScheculedActivity>(); //公会战开始 by changhao

	private static java.util.Map<Integer, ScheculedActivity> activeMap = new java.util.HashMap<Integer, ScheculedActivity>();
	
	@SuppressWarnings("null")
	public static void init() throws Exception {

		activityNpcPosMap = ConfigManager.getInstance().getConf(
				fire.pb.npc.SActivityNpcPos.class);
		// 之前如果有活动的话,都清除.配合热启动,但最好不要热启动活动,因为有些活动可能已经开始了,正在进行中
		for (AbstractScheduledActivity activity : activityMap.values()) {
			if (activity.startActivityFuture != null)
				activity.startActivityFuture.cancel(false);
			if (activity.endActivityFuture != null)
				activity.endActivityFuture.cancel(false);
			if (activity.resumeActivityFuture != null)
				activity.resumeActivityFuture.cancel(false);
			if (activity.tmpstartActivityFuture != null)
				activity.tmpstartActivityFuture.cancel(false);
			if (activity.tmpendActivityFuture != null)
				activity.tmpendActivityFuture.cancel(false);

		}

		activeMap.clear();
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			java.util.NavigableMap<Integer, SScheculedActivity> activities = ConfigManager.getInstance().getConf(fire.pb.timer.SScheculedActivity.class);
			activeMap.putAll(activities);
				
		}
		else { //点卡服
			java.util.NavigableMap<Integer, SScheculedActivityDianKa> activities = ConfigManager.getInstance().getConf(fire.pb.timer.SScheculedActivityDianKa.class);
			activeMap.putAll(activities);
		}
		
		long currentTime = System.currentTimeMillis();
		for (ScheculedActivity activity : activeMap.values()) {
			List<Integer> zoneids = new ArrayList<Integer>();
			if (activity != null && activity.zoneid.length() > 3) {
				String[] strs = activity.zoneid.split(";");
				for (String str : strs) {
					zoneids.add(Integer.parseInt(str));
				}
			}
			
			if (activity.activityid == RoleLiveness.ANSWER_QUESTION)//加入每天的答题活动 by changhao
			{
				answerQuestionActivity.put(activity.weekrepeat, activity);
			}
			
			if (activity.activityid == RoleLiveness.CLANFIGHT_PRE)
			{
				clanfightpreActivity.put(activity.weekrepeat, activity);
			}
			
			if (activity.activityid == RoleLiveness.CLANFIGHT_START)
			{
				clanfightActivity.put(activity.weekrepeat, activity);
			}

			if (!zoneids.isEmpty()) {
				//if (!zoneids.contains(ConfigManager.getGsZoneId()))
				//	continue;

				// 这里是指定服务器的活动
				Integer sid = Integer.parseInt(Gs.serverid);
				if (!zoneids.contains(sid))
					continue;

				IScheduledActivity.logger.info("special activity id:" + activity.id
						+ " server id:" + Gs.serverid);
			}

			if (activity.weekrepeat > 0) {// 周期性活动
				scheduleRepeatActivity(activity);
				continue;
			}

			String startTimeStr = activity.startTime;
			Date startDate = sdf.parse(startTimeStr);
			String endTimeStr = activity.endTime;
			Date endDate = sdf.parse(endTimeStr);
			long startTime = startDate.getTime();
			long endTime = endDate.getTime();
			int id = activity.id;
			if (startTime >= endTime) {
				IScheduledActivity.logger
						.error("start time after end time.activity id:" + id);
				continue;
			}

			// 根据baseid,通过反射生成每个活动的主类,并放入map中
			int baseID = activity.activityid;
			String className = prop.getProperty(Integer.toString(baseID));
			if (className != null) {
				TYPE_ACTIVE_ID.put(Class.forName(className), baseID);
				
				if (startTime > currentTime) {
					if (xtable.Activitytable.select(id) != null){// 如果不为null,说明是之前执行过,需要resume的活动,在这里先不管{
						continue;
					}
					@SuppressWarnings("unchecked")
					Constructor<? extends AbstractScheduledActivity> con = (Constructor<? extends AbstractScheduledActivity>) Class
							.forName(className).getConstructor(int.class,
									int.class, long.class, long.class);
					AbstractScheduledActivity activityInstance = con
							.newInstance(baseID, id, startTime, endTime);
					activityMap.put(id, activityInstance);
					IScheduledActivity.logger
							.info("put activity to activityMap.activity id:"
									+ id);
					// 对每一个活动,都启动start 和 end的两个schedule task,时间到了之后自动执行
					long delayTime = startTime - currentTime;
					if (delayTime == 0) // 预防xdb的hungry 异常
						delayTime = 1000;
					ScheduledFuture<?> startFuture = mkdb.Mkdb.executor()
							.schedule(new ActivityStartThread(id, 0),
									delayTime, TimeUnit.MILLISECONDS);
					ScheduledFuture<?> endFuture = mkdb.Mkdb.executor().schedule(
							new ActivityEndThread(id, true),
							endTime - currentTime, TimeUnit.MILLISECONDS);
					activityInstance.setStartActivityFuture(startFuture);
					activityInstance.setEndActivityFuture(endFuture);
				}
			}
		}
		// 还有一种情况,有些活动在服务器停的时候关闭了,需要启动的时候继续.这种情况需要注意,在表里面不要配同时间段的相同活动,否则会有两个活动在跑
		final List<Integer> removeids = new ArrayList<Integer>();
		xtable.Activitytable.getTable().browse(
				new IWalk<Integer, xbean.ActivityStatus>() {

					@Override
					public boolean onRecord(Integer k, xbean.ActivityStatus v) {

						String className = prop.getProperty(Integer.toString(v
								.getBaseid()));
						try {
							if (activityMap.containsKey(v.getId())) { // 如果策划在配置表里配了同样id的活动,那么xdb表里的这个就不启动了,并且从表里删除
								removeids.add(v.getId());
								return true;
							}
							if (v.getId() == 0 || v.getId() == v.getBaseid()) // 如果是gm指令加的活动,不用管它
								return true;
							@SuppressWarnings("unchecked")
							Constructor<? extends AbstractScheduledActivity> con = (Constructor<? extends AbstractScheduledActivity>) Class
									.forName(className).getConstructor(
											int.class, int.class, long.class,
											long.class);
							AbstractScheduledActivity activityInstance;
							activityInstance = con.newInstance(v.getBaseid(),
									v.getId(), v.getInistarttime(),
									v.getIniendtime());
							activityMap.put(v.getId(), activityInstance);
							TYPE_ACTIVE_ID.put(Class.forName(className),
									v.getBaseid());
							IScheduledActivity.logger
									.info("put resume activity to activityMap.activity id:"
											+ v.getId());

							long delayTime = 100000;// 默认100秒之后继续活动,不过可以考虑下,多长时间合适呢
													// 是否适合所有活动?是否需要配表?
							ScheduledFuture<?> resumeFuture = mkdb.Mkdb
									.executor().schedule(
											new ActivityResumeThread(v.getId(),
													false,
													v.getLaststarttime(), v
															.getLastendtime(),
													v.getCosttime()),
											delayTime, TimeUnit.MILLISECONDS);

							activityInstance
									.setResumeActivityFuture(resumeFuture);
							
							return true;
						} catch (Exception e) {
							IScheduledActivity.logger.error(
									"resume activity failed.activity id"
											+ v.getId(), e);
							return true;
						}
					}
				});


		if (removeids.isEmpty())
			return;
		Procedure proc = new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				for (Integer actid : removeids) {
					xtable.Activitytable.remove(actid);
				}
				return true;
			}

		};
		if (Transaction.current() == null)
			proc.submit();
		else
			proc.call();
	}

	private static void scheduleRepeatActivity(ScheculedActivity activity)
			throws Exception {

		Long curTime = System.currentTimeMillis();
		Long startDelay = calDelay(activity.startTime, activity.weekrepeat,
				curTime);
		Long endDelay = calDelay(activity.endTime, activity.weekrepeat, curTime);
		int id = activity.id;
		if (startDelay >= endDelay) {
			endDelay = endDelay + StatisticUtil.weekMills;
			if (endDelay < startDelay
					|| endDelay - startDelay > StatisticUtil.dayMills) {
				IScheduledActivity.logger
						.error("start time after end time.activity id:" + id);
				throw new RuntimeException(
						"start time after end time.activity id:" + id);
			}
		}
		if (startDelay < 0 || endDelay < 0) {
			IScheduledActivity.logger
					.error("start time after end time.activity id:" + id);
			throw new RuntimeException("start time after end time.activity id:"
					+ id);
		}
		// 根据baseid,通过反射生成每个活动的主类,并放入map中
		int baseID = activity.activityid;
		
		// 如果是点卡服,过滤掉氪金和瑟银宝箱[230,231]	add by yebin
		if(fire.pb.fushi.Module.GetPayServiceType() == 1){
			if(baseID == 230 || baseID == 231){
				if (IScheduledActivity.logger.isInfoEnabled()) {
					IScheduledActivity.logger.info("点卡服取消氪金和瑟银宝箱活动. 活动Id=" + baseID);
				}
				return;
			}
		}
		
		String className = prop.getProperty(Integer.toString(baseID));
		if (className != null) {
			TYPE_ACTIVE_ID.put(Class.forName(className), baseID);
			@SuppressWarnings("unchecked")
			Constructor<? extends AbstractScheduledActivity> con = (Constructor<? extends AbstractScheduledActivity>) Class
					.forName(className).getConstructor(int.class, int.class,
							long.class, long.class);
			AbstractScheduledActivity activityInstance = con.newInstance(
					baseID, id, curTime + startDelay, curTime + endDelay);
			activityMap.put(id, activityInstance);
			IScheduledActivity.logger
					.info("put activity to activityMap.activity id:" + id);
			// 对每一个活动,都启动start 和 end的两个schedule task,时间到了之后自动执行
			if (startDelay == 0) // 预防xdb的hungry 异常
				startDelay = 1000L;
			startActivityIfNeed(activity, activityInstance);// 对于周期性的活动,即使当前时间在starttime和endtime之间,也开启活动
			ScheduledFuture<?> startFuture = mkdb.Mkdb.executor()
					.scheduleAtFixedRate(
							new ActivityStartThread(id, endDelay - startDelay),
							startDelay, StatisticUtil.weekMills,
							TimeUnit.MILLISECONDS);
			ScheduledFuture<?> endFuture = mkdb.Mkdb.executor()
					.scheduleAtFixedRate(new ActivityEndThread(id, false),
							endDelay, StatisticUtil.weekMills,
							TimeUnit.MILLISECONDS);
			activityInstance.setStartActivityFuture(startFuture);
			activityInstance.setEndActivityFuture(endFuture);
		}

	}


	//对于周期性的活动,即使当前时间在starttime和endtime之间,也开启活动
	private static void startActivityIfNeed(ScheculedActivity activity,
			AbstractScheduledActivity activityInstance) {

		Calendar cal = Calendar.getInstance();
		int curDay = cal.get(Calendar.DAY_OF_WEEK);

		if (activity.weekrepeat <= 0 || activity.weekrepeat >= 8)
			return;
		int day = 0;
		if (activity.weekrepeat == 7)
			day = 1;
		else
			day = activity.weekrepeat + 1;
		if (day != curDay)
			return;

		Calendar startCalendar = Calendar.getInstance();
		String[] hhmmss = activity.startTime.split(":");
		if (hhmmss.length != 3)
			return;
		int hour = Integer.parseInt(hhmmss[0]);
		int min = Integer.parseInt(hhmmss[1]);
		int sec = Integer.parseInt(hhmmss[2]);
		startCalendar.set(Calendar.HOUR_OF_DAY, hour);
		startCalendar.set(Calendar.MINUTE, min);
		startCalendar.set(Calendar.SECOND, sec);

		Calendar endCalendar = Calendar.getInstance();
		hhmmss = activity.endTime.split(":");
		if (hhmmss.length != 3)
			return;
		hour = Integer.parseInt(hhmmss[0]);
		min = Integer.parseInt(hhmmss[1]);
		sec = Integer.parseInt(hhmmss[2]);
		endCalendar.set(Calendar.HOUR_OF_DAY, hour);
		endCalendar.set(Calendar.MINUTE, min);
		endCalendar.set(Calendar.SECOND, sec);

		if (startCalendar.before(cal) && cal.before(endCalendar)) {
			ScheduledFuture<?> startFuture = mkdb.Mkdb.executor().schedule(
					new ActivityStartThread(activity.id,
							endCalendar.getTimeInMillis()
									- cal.getTimeInMillis()), 1000,
					TimeUnit.MILLISECONDS);
			ScheduledFuture<?> endFuture = mkdb.Mkdb.executor().schedule(
					new ActivityEndThread(activity.id, false),
					endCalendar.getTimeInMillis() - cal.getTimeInMillis(),
					TimeUnit.MILLISECONDS);
			activityInstance.setTmpstartActivityFuture(startFuture);
			activityInstance.setTmpendActivityFuture(endFuture);
		}
	}

	private static Long calDelay(String startTime, int dayOfWeek, Long curTime) {

		Calendar cal = Calendar.getInstance();

		String[] hhmmss = startTime.split(":");
		if (hhmmss.length != 3)
			return null;
		if (dayOfWeek <= 0 || dayOfWeek >= 8)
			return null;
		int hour = Integer.parseInt(hhmmss[0]);
		int min = Integer.parseInt(hhmmss[1]);
		int sec = Integer.parseInt(hhmmss[2]);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);

		if (dayOfWeek == 7)
			cal.set(Calendar.DAY_OF_WEEK, 1);
		else
			cal.set(Calendar.DAY_OF_WEEK, dayOfWeek + 1);

		Long delay = cal.getTimeInMillis() - curTime;
		if (delay <= 0) {
			cal.setTimeInMillis(cal.getTimeInMillis() + StatisticUtil.weekMills);
		}

		return cal.getTimeInMillis() - curTime;
	}

	public static Map<Integer, AbstractScheduledActivity> getActivitymap() {

		return activityMap;
	}

	public static Map<Integer, SActivityNpcPos> getActivityNpcPosMap() {

		return activityNpcPosMap;
	}
	
	public static Map<Integer, ScheculedActivity> getAnswerQuestionActivity() {

		return answerQuestionActivity;
	}
	
	public static Map<Integer, ScheculedActivity> getClanFightPreActivity() {

		return clanfightpreActivity;
	}
	
	public static Map<Integer, ScheculedActivity> getClanFightActivity() {

		return clanfightActivity;
	}
	
	public static int activeId(Class<?> className) {
		if (TYPE_ACTIVE_ID.containsKey(className)) {
			return TYPE_ACTIVE_ID.get(className);
		}
		return -1;
	}

	public static void stopActivities() {

		xtable.Activitytable.getTable().browse(
				new IWalk<Integer, xbean.ActivityStatus>() {

					@Override
					public boolean onRecord(Integer k, xbean.ActivityStatus v) {

						AbstractScheduledActivity activity = activityMap.get(v
								.getId());
						if (activity != null)
							try {
								activity.stop(true);
							} catch (Exception e) {

								e.printStackTrace();
							}
						return true;
					}
				});

	}
}
