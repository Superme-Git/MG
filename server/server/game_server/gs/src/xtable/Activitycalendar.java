package xtable;

// typed table access point
public class Activitycalendar {
	Activitycalendar() {
	}

	public static xbean.ActivityCalendarStatus get(Long key) {
		return _Tables_.getInstance().activitycalendar.get(key);
	}

	public static xbean.ActivityCalendarStatus get(Long key, xbean.ActivityCalendarStatus value) {
		return _Tables_.getInstance().activitycalendar.get(key, value);
	}

	public static void insert(Long key, xbean.ActivityCalendarStatus value) {
		_Tables_.getInstance().activitycalendar.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().activitycalendar.delete(key);
	}

	public static boolean add(Long key, xbean.ActivityCalendarStatus value) {
		return _Tables_.getInstance().activitycalendar.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().activitycalendar.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ActivityCalendarStatus> getCache() {
		return _Tables_.getInstance().activitycalendar.getCache();
	}

	public static mkdb.TTable<Long, xbean.ActivityCalendarStatus> getTable() {
		return _Tables_.getInstance().activitycalendar;
	}

	public static xbean.ActivityCalendarStatus select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityCalendarStatus, xbean.ActivityCalendarStatus>() {
			public xbean.ActivityCalendarStatus get(xbean.ActivityCalendarStatus v) { return v.toData(); }
		});
	}

	public static Integer selectWeek(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityCalendarStatus, Integer>() {
				public Integer get(xbean.ActivityCalendarStatus v) { return v.getWeek(); }
			});
	}

	public static java.util.Map<Integer, xbean.DayFinishTask> selectDaystatus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityCalendarStatus, java.util.Map<Integer, xbean.DayFinishTask>>() {
				public java.util.Map<Integer, xbean.DayFinishTask> get(xbean.ActivityCalendarStatus v) { return v.getDaystatusAsData(); }
			});
	}

}
