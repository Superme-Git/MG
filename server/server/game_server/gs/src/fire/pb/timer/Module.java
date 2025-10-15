
package fire.pb.timer;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import fire.log.Logger;
import fire.pb.SServerLevel;
import fire.pb.activity.ActivityConfNew;
import fire.pb.activity.award.ExpBili;
import fire.pb.battle.livedie.LiveDieTask;
import fire.pb.compensation.CompensationManager;
import fire.pb.fushi.redpack.RedPackTask;
import fire.pb.fushi.spotcheck.SpotCheckTask;
import fire.pb.game.GameManager;
import fire.pb.item.equip.diamond.EquipDiamondMgr;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.activelist.RoleLivenessManager;
import fire.pb.mission.instance.InstanceManager;
import fire.pb.mission.treasuremap.BaoTuMapManager;
import fire.pb.role.SServiceExpConfig;
import fire.pb.role.SServiceLevelConfig;
import fire.pb.title.TitleTask;
import fire.pb.util.DateValidate;
import fire.pb.util.NameManage;
import mkdb.Procedure;

public class Module implements ModuleInterface {
	private java.util.concurrent.ScheduledFuture<?> sf;
	public final static Logger logger = Logger.getLogger("SYSTEM");
	
	private static Map<Integer, LevelLast> levelLast = new TreeMap<Integer, LevelLast>(new Comparator<Integer>() {
		public int compare(Integer key1, Integer key2) {
			return key1 - key2;
		}
	});
	
	private final static Map<Integer, Double> expBili = new HashMap<Integer, Double>();
	private static java.util.Map<Integer, Set<Integer>> activeWeekRepeatMap = new java.util.HashMap<Integer, Set<Integer>>();
	
	public void exit() {
		sf.cancel(true);
		//定时活动全部暂停
//		ActivityManager.stopActivities();
	}

	@Override
	public void init() throws Exception {
		initOnce();
//		// 读取定时活动里的配置
		ActivityManager.init();
//		//系统补偿
		CompensationManager.getInstance().init();
		GameManager.getInstance().Init();
		Map<Integer, SServiceLevelConfig> mapServiceLevelConfig = ConfigManager.getInstance().getConf(SServiceLevelConfig.class);
		Map<Integer, SServiceExpConfig> mapServiceExpConfig = ConfigManager.getInstance().getConf(SServiceExpConfig.class);
		
		for (SServiceLevelConfig config : mapServiceLevelConfig.values()) {
			LevelLast lvLast = levelLast.get(config.getOpenday());
			if (lvLast == null) {
				lvLast = new LevelLast(config.getSlevel(), config.getLastday(), config.getOpenday());
				levelLast.put(config.getOpenday(), lvLast);
			}
		}
		
		for (SServiceExpConfig config : mapServiceExpConfig.values()) {
			expBili.put(config.getMidlevel(), config.getBili());
		}
	}

	/**
	 * initOnce:服务器启动后只用初始化一次的操作
	 * @throws Exception
	 */
	private void initOnce() throws Exception {

		Calendar till = Calendar.getInstance();
		final long now = till.getTimeInMillis();
		till.set(Calendar.HOUR_OF_DAY, 23);
		till.set(Calendar.MINUTE, 55);
		till.set(Calendar.SECOND, 59);
		int weekInYear = till.get(Calendar.WEEK_OF_YEAR);
		int year = till.get(Calendar.YEAR);
		
		new Procedure() {
			@Override
			protected boolean process() throws Exception {
				int dayInWeek = till.get(Calendar.DAY_OF_WEEK);
				xbean.ServiceInfo serviceInfo = xtable.Serviceinfos.get(1);
				if (serviceInfo == null) {
					serviceInfo = xbean.Pod.newServiceInfo();
					serviceInfo.setYear(year);
					xtable.Serviceinfos.insert(1, serviceInfo);
				}
				else if (year != serviceInfo.getYear()) {
					//新的一年
					serviceInfo.setYear(year);
				}
				//周日在逻辑里不算新的一周开始
				if (dayInWeek == Calendar.SUNDAY) {
					serviceInfo.setWeeks(weekInYear - 1);
				}
				else {
					serviceInfo.setWeeks(weekInYear);
				}
				
				long updatetime = serviceInfo.getUpdatetime();
				if (!DateValidate.inTheSameDay(updatetime, now)) {
					serviceInfo.setUpdatetime(now);
					serviceInfo.setDays(serviceInfo.getDays() + 1);
				}
				
				RoleLivenessManager.setCurrentRecommend(serviceInfo.getActiverecommend());
				return true;
			}
			
		}.call();
		
		final long mtill = till.getTimeInMillis();
		final long timeSpace = (mtill - now) < 0 ? (mtill - now + 24*60*60*1000) : (mtill - now);
		final long sdelay = timeSpace/1000;
		
		logger.info("将在 " +sdelay+ " 秒后开始第一次EndOfDayTask");
		mkdb.Executor.getInstance().scheduleAtFixedRate(new EndOfDayTask(), sdelay, 86400, TimeUnit.SECONDS);
		mkdb.Executor.getInstance().scheduleAtFixedRate(new PerHourTask(), 1, 1, TimeUnit.HOURS);
		mkdb.Executor.getInstance().scheduleAtFixedRate(new PerQuarterTask(), 15, 15, TimeUnit.MINUTES);
		mkdb.Executor.getInstance().scheduleAtFixedRate(new PerFiveMinutesTask(), 5, 5, TimeUnit.MINUTES);
		
//		//每条凌晨打印一份排行榜数据(这个现在应该是没用的就是输出老的日志)
//		mkdb.Executor.getInstance().scheduleAtFixedRate(new Runnable() {
//			
//			@Override
//			public void run() {
//				new fire.pb.ranklist.PEndOfDayTask().submit();
//
//			}
//		}, sdelay-3, 86400, TimeUnit.SECONDS);
		
		//设置每天24：00：33开始执行的操作
		Calendar tick = Calendar.getInstance();
		final long thisTick = tick.getTimeInMillis();
		tick.set(Calendar.HOUR_OF_DAY, 0);
		tick.set(Calendar.MINUTE, 0);
		tick.set(Calendar.SECOND, 1);
		final long startTick = tick.getTimeInMillis();
		final long beginDayTimeSpace = (startTick - thisTick) < 0 ? (startTick - thisTick + 24*60*60*1000) : (startTick - thisTick);
		final long zDelay = beginDayTimeSpace / 1000;
		logger.info("将在 " +zDelay+ " 秒后开始第一次BeginOfDayTask");
		mkdb.Executor.getInstance().scheduleAtFixedRate(new BeginOfDayTask(), zDelay, 86400, TimeUnit.SECONDS);
		
		sf = mkdb.Mkdb.executor().scheduleAtFixedRate(new GameTimeTask(), 1, 150, TimeUnit.SECONDS);
		
		EquipDiamondMgr.getInstance().init();
		BaoTuMapManager.getInstance().Init();
		InstanceManager.getInstance().init();
		fire.pb.mission.instance.line.LineInstManager.getInstance().init();
		
		NameManage.init();
		mkdb.Mkdb.executor().scheduleAtFixedRate(new CheckXdbCache(), 2, 2, TimeUnit.HOURS);
		
		//处理公会药房物品刷新  30秒去刷新一次
		mkdb.Executor.getInstance().scheduleAtFixedRate(new ClanYaofangHourTask(), 1, 30, TimeUnit.SECONDS);
		
		//处理生死战逻辑
		mkdb.Executor.getInstance().scheduleAtFixedRate(new LiveDieTask(), 1, 600, TimeUnit.SECONDS);
		//处理红包逻辑
		mkdb.Executor.getInstance().scheduleAtFixedRate(new RedPackTask(), 1, 600, TimeUnit.SECONDS);
		mkdb.Executor.getInstance().scheduleAtFixedRate(new TitleTask(), 1, 180, TimeUnit.SECONDS);
		if(fire.pb.fushi.Module.GetPayServiceType() == 1){
			//点卡服，执行点卡交易订单退回逻辑
			mkdb.Executor.getInstance().scheduleAtFixedRate(new SpotCheckTask(), 1, 600, TimeUnit.SECONDS);
		}
		
		Map<Integer, ActivityConfNew> activityMap = RoleLiveness.getConfigActivity();
		for (ActivityConfNew activity : activityMap.values()) {
			String strWeekRepeat = activity.weekrepeat;
			String [] str = strWeekRepeat.split(",");
			
			if (str.length > 0) {
				for (String weekrepeat : str) {
					Integer weekindex = Integer.valueOf(weekrepeat);
					
					Set<Integer> weekRepeatList = activeWeekRepeatMap.get(weekindex);
					if (weekRepeatList == null) {
						weekRepeatList = new HashSet<Integer>();
						activeWeekRepeatMap.put(weekindex, weekRepeatList);
					}
					weekRepeatList.add(activity.id);
				}
				
			}
		}
	}
	
	public long getFirstRemainTime(){
		
		long twenty = DateValidate.minuteMills*20;
		long cur = System.currentTimeMillis();
		Calendar current = Calendar.getInstance();
		current.setTimeInMillis(cur);
		int hour = current.get(Calendar.HOUR_OF_DAY);
		
		long remain = 0;
		String ymd = DateValidate.getTodayStr(cur);
		String biaozhunStr = ymd+" "+hour+":40:00";
		long biaozhun = DateValidate.parseDate(biaozhunStr);
		
		long chazhi = cur - biaozhun;
		if(chazhi >0){
			remain = biaozhun + twenty - cur;
		}else{
			chazhi = Math.abs(chazhi);
			if(chazhi >twenty){
				remain = biaozhun - cur - twenty;
			}else{
				remain = chazhi;
			}
		}
		return remain;
	}
	

	@Override
	public ReloadResult reload() throws Exception
	{
		return new ReloadResult(false,"module" + this.getClass().getName() + "not support reload");
	}
	
	public static int getServerLevel() {
		xbean.ServiceInfo serviceInfo = xtable.Serviceinfos.select(1);
		if (serviceInfo == null) {
			return 0;
		}
		
		int days = serviceInfo.getDays();
		
		LevelLast tmpLast = levelLast.get(days);
		if (tmpLast != null)
			return tmpLast.serverlv;
		
		for (LevelLast lvLast : levelLast.values()) {
			if (days <= (lvLast.lastdays + lvLast.opendays))
				return lvLast.serverlv;
		}
		
		Map<Integer, SServiceLevelConfig> mapServiceLevelConfig = ConfigManager.getInstance().getConf(SServiceLevelConfig.class);
		SServiceLevelConfig serviceLv = mapServiceLevelConfig.get(mapServiceLevelConfig.size());
		
		return serviceLv.getSlevel();
	}
	
	public static LevelLast getLevelLast() {
		xbean.ServiceInfo serviceInfo = xtable.Serviceinfos.select(1);
		if (serviceInfo == null) {
			return null;
		}
		
		int days = serviceInfo.getDays();
		
		LevelLast tmpLast = levelLast.get(days);
		if (tmpLast != null)
			return tmpLast;
		
		for (LevelLast lvLast : levelLast.values()) {
			if (days <= (lvLast.lastdays + lvLast.opendays))
				return lvLast;
		}
		
		Map<Integer, SServiceLevelConfig> mapServiceLevelConfig = ConfigManager.getInstance().getConf(SServiceLevelConfig.class);
		SServiceLevelConfig serviceLv = mapServiceLevelConfig.get(mapServiceLevelConfig.size());
		
		return new LevelLast(serviceLv.getSlevel(), serviceLv.getLastday(), serviceLv.getOpenday());
	}
	
	public static Double getExpBili(final int expLv) {
		Double bili = expBili.get(expLv);
		if (bili == null) {
			Map<Integer, SServiceExpConfig> mapServiceExpConfig = ConfigManager.getInstance().getConf(SServiceExpConfig.class);
			if (expLv > 0) {
				SServiceExpConfig expConfig = mapServiceExpConfig.get(1);
				return expConfig.bili;
			}
			else if (expLv < 0) {
				SServiceExpConfig expConfig = mapServiceExpConfig.get(mapServiceExpConfig.size());
				return expConfig.bili;
			}
		}
		
		return bili;
	}
	
	//根据服务器等级和角色等级进行经验修正
	public static long getExpRevise(final int expLv, final long exp) {
		//求出经验加成和衰减的比例
		Double bili = fire.pb.timer.Module.getExpBili(expLv);
		return (long)(exp * bili);
	}
	
	public static int getMidLv(final int roleLv, final int awardId) {
		int serverLv = fire.pb.timer.Module.getServerLevel();
		//判断玩家等级和服务器等级是否需要加成和衰减
		int midLv = roleLv - serverLv;
		if (midLv == 0)
			return 0;
		
		ExpBili bili = fire.pb.activity.award.RewardMgr.getInstance().getBili(awardId);
		if (bili == null)
			return 0;
		
		if (midLv > 0) {
			//判断是否会衰减
			if (bili.expweaken == 0)
				return 0;
		}
		else {
			//判断是否会加成
			if (bili.expaddition == 0)
				return 0;
			
			if (serverLv < 70 || roleLv < 35)
				return 0;
		}
		
		return midLv;
	}
	
	public static SServerLevel getServerLvData() {
		LevelLast lLast = fire.pb.timer.Module.getLevelLast();
		if (lLast == null)
			return null;
		
		xbean.ServiceInfo serviceInfo = xtable.Serviceinfos.select(1);
		if (serviceInfo == null) {
			return null;
		}
		
		int days = serviceInfo.getDays();
		
		SServerLevel sLevel = new SServerLevel();
		sLevel.slevel = lLast.serverlv;
		sLevel.newleveldays = (lLast.lastdays + lLast.opendays) - days;
		
		return sLevel;
	}

	public static Map<Integer, Set<Integer>> getWeekRepeatMap() {
		return activeWeekRepeatMap;
	}
}
