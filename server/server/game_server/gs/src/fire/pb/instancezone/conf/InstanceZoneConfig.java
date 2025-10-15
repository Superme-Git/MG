package fire.pb.instancezone.conf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.instance.SInstaceConfig;
import fire.pb.instancezone.InstanceText;
import fire.pb.instancezone.event.Event;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.util.CalendarUtil;
import fire.pb.util.DateValidate;

public class InstanceZoneConfig {
	public SInstaceConfig sFubenconfig;

	public final int instZoneId;

	public final int beforeZoneId;

	public final String classname;

	/**
	 * 公会副本名字
	 */
	public final String name;

	public static final int TEAM_TYPE_SINGLE = 0;
	public static final int TEAM_TYPE_TEAM = 1;
	public static final int TEAM_TYPE_RAID = 2;
	public static final int TEAM_TYPE_ALL = 3;
	public final int teamtype;
	public final int minlevel;
	public final int maxlevel;
	public final int minrolenum;
	public final int maxrolenum;
	public final Map<Integer, MapConfig> maps = new HashMap<Integer, MapConfig>();

	public final int initMapid;

	public final int initPosx;

	public final int initPosy;

	public int outMapid;

	public int outPosx;

	public int outPosy;

	public static final int TIME_TYPE_NULL = 0;
	public static final int TIME_TYPE_DAY = 1;
	public static final int TIME_TYPE_WEEK = 2;
	public static final int TIME_TYPE_PERIOD = 3;
	public final int refreshType;
	public final List<Long> refreshTimes = new LinkedList<Long>();

	public final int enterTimeType;
	public final List<StartEndTime> enterTimes = new LinkedList<StartEndTime>();
	public String enterTimeString = null;

	public final List<Integer> initsaves = new LinkedList<Integer>();
	public final Integer cdsave;
	public final boolean createIncd;
	public final Map<Integer, InstanceSaveConfig> saves = new HashMap<Integer, InstanceSaveConfig>();
	public final Map<Integer, NpcConfig> npcs = new HashMap<Integer, NpcConfig>();

	public final Map<Integer, List<NpcConfig>> npcDynamic = new HashMap<Integer, List<NpcConfig>>();

	public final Map<Integer, NpcServiceConfig> npcServices = new HashMap<Integer, NpcServiceConfig>();
	public final Map<TriggerCondition, Integer> mazes = new HashMap<TriggerCondition, Integer>();
	public final Map<Integer, ActionConfig> acts = new HashMap<Integer, ActionConfig>();
	public final Map<Integer, EventConfig> events = new HashMap<Integer, EventConfig>();
	public final int xiezhannum;

	public InstanceZoneConfig(SInstaceConfig sFubenconfig) {
		this.sFubenconfig = sFubenconfig;
		this.instZoneId = sFubenconfig.id;
		this.beforeZoneId = sFubenconfig.afterid;
		this.classname = sFubenconfig.classname;
		this.name = sFubenconfig.name;
		this.teamtype = sFubenconfig.team;
		this.minlevel = sFubenconfig.minlevel;
		this.maxlevel = sFubenconfig.maxlevel;
		this.minrolenum = sFubenconfig.playernum;
		this.maxrolenum = sFubenconfig.maxnum;
		String[] mapstrs = sFubenconfig.mapids.split(";");
		for (String mapstr : mapstrs) {
			int mapid = Float.valueOf(mapstr).intValue();
			MapConfig mapcfg = ConfigManager.getInstance()
					.getConf(MapConfig.class).get(mapid);
			this.maps.put(mapid, mapcfg);
		}
		this.initMapid = sFubenconfig.mapidfirst;
		this.initPosx = sFubenconfig.posxfirst;
		this.initPosy = sFubenconfig.posyfirst;
		this.outMapid = sFubenconfig.mapidgoto;
		this.outPosx = sFubenconfig.posxgoto;
		this.outPosy = sFubenconfig.posygoto;
		this.refreshType = sFubenconfig.refreshtype;
		String[] timestrs = sFubenconfig.refreshtime.split(";");
		for (String str : timestrs)
			this.refreshTimes
					.add((long) (Float.valueOf(str).intValue()) * 60000);

		this.enterTimeType = sFubenconfig.gototype;
		if (this.enterTimeType > 0) {
			String[] times = sFubenconfig.gototime.split(";");
			for (String str : times) {
				String[] strs = str.split("-");
				this.enterTimes.add(new StartEndTime(
						Long.valueOf(strs[0]) * 60000,
						Long.valueOf(strs[1]) * 60000));
			}
		}
		this.initsaves.add(sFubenconfig.firstjindu);
		this.cdsave = sFubenconfig.lastjindu;
		this.createIncd = (sFubenconfig.iscreate == 1);
		this.xiezhannum = sFubenconfig.xiezhan;
	}

	public Map<Integer, Integer> getInitInstanceSaves() {
		Map<Integer, Integer> initsavestates = new HashMap<Integer, Integer>();
		for (int saveId : initsaves)
			initsavestates.put(saveId, InstanceText.ACTIVE);
		return initsavestates;
	}

	public boolean matchEvent(Event curEvent) {
		for (EventConfig eventcfg : events.values()) {
			if (curEvent.match(eventcfg))
				return true;
		}
		return false;
	}

	public long getRefreshTime(long now) {
		long refresh = 0;
		for (long refreshTime : refreshTimes) {
			long tmp = 0;
			switch (refreshType) {
			case InstanceZoneConfig.TIME_TYPE_NULL:
				return -1;
			case InstanceZoneConfig.TIME_TYPE_DAY:
				tmp = genRefreshTime(DateValidate.getDayFirstSecond(now),
						DateValidate.dayMills, refreshTime, now);
				break;
			case InstanceZoneConfig.TIME_TYPE_WEEK:
				tmp = genRefreshTime(DateValidate.getWeekFirstSecond(now),
						DateValidate.weekMills, refreshTime, now);
				break;
			case InstanceZoneConfig.TIME_TYPE_PERIOD:
				tmp = now + refreshTime;
				break;
			}
			if (refresh == 0)
				refresh = tmp;
			else if (tmp > 0 && tmp < refresh)
				refresh = tmp;
		}
		return refresh;
	}

	public boolean inEnterTime(long now) {
		if (enterTimeType == InstanceZoneConfig.TIME_TYPE_NULL)
			return true;
		long firstsecond = 0;
		if (enterTimeType == InstanceZoneConfig.TIME_TYPE_DAY)
			firstsecond = DateValidate.getDayFirstSecond(now);
		else if (enterTimeType == InstanceZoneConfig.TIME_TYPE_WEEK)
			firstsecond = DateValidate.getWeekFirstSecond(now);
		else
			return false;
		long mod = now - firstsecond;
		for (StartEndTime entry : enterTimes) {
			if (mod >= entry.starttime && mod < entry.endtime)
				return true;
		}
		return false;
	}

	public long getEnterTime() {
		final long now = System.currentTimeMillis();
		if (enterTimeType == InstanceZoneConfig.TIME_TYPE_NULL)
			return 0;
		long firstsecond = 0;
		if (enterTimeType == InstanceZoneConfig.TIME_TYPE_DAY)
			firstsecond = DateValidate.getDayFirstSecond(now);
		else if (enterTimeType == InstanceZoneConfig.TIME_TYPE_WEEK)
			firstsecond = DateValidate.getWeekFirstSecond(now);
		else
			return 0;
		long mod = now - firstsecond;
		for (StartEndTime entry : enterTimes) {
			if (mod >= entry.starttime && mod < entry.endtime) {
				return firstsecond + entry.starttime;
			}
		}
		return 0;
	}

	public String getEnterTimeString() {
		if (enterTimeString != null)
			return enterTimeString;
		if (enterTimeType == InstanceZoneConfig.TIME_TYPE_DAY) {
			enterTimeString = "";
			int i = 0;
			for (StartEndTime entry : enterTimes) {
				enterTimeString += CalendarUtil.getDayTimePeriodString(
						entry.starttime, entry.endtime);
				i++;
				if (i < enterTimes.size())
					enterTimeString += "，";
			}
			return enterTimeString;
		}
		if (enterTimeType == InstanceZoneConfig.TIME_TYPE_WEEK) {
			enterTimeString = "";
			int i = 0;
			for (StartEndTime entry : enterTimes) {
				enterTimeString += CalendarUtil.getWeekTimePeriodString(
						entry.starttime, entry.endtime);
				i++;
				if (i < enterTimes.size())
					enterTimeString += "，";
			}
			return enterTimeString;
		}

		return null;
	}

	private long genRefreshTime(long firstmilli, long period,
			long refershperiod, long now) {
		if (now < firstmilli + refershperiod)
			return firstmilli + refershperiod;

		return firstmilli + refershperiod + period;
	}

	private static class StartEndTime {
		private final long starttime;
		private final long endtime;

		public StartEndTime(long starttime, long endtime) {
			this.starttime = starttime;
			this.endtime = endtime;
		}

	}
}
