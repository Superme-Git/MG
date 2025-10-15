package xtable;

// typed table access point
public class Activeroletable {
	Activeroletable() {
	}

	public static xbean.ActiveRoleInfo get(Long key) {
		return _Tables_.getInstance().activeroletable.get(key);
	}

	public static xbean.ActiveRoleInfo get(Long key, xbean.ActiveRoleInfo value) {
		return _Tables_.getInstance().activeroletable.get(key, value);
	}

	public static void insert(Long key, xbean.ActiveRoleInfo value) {
		_Tables_.getInstance().activeroletable.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().activeroletable.delete(key);
	}

	public static boolean add(Long key, xbean.ActiveRoleInfo value) {
		return _Tables_.getInstance().activeroletable.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().activeroletable.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ActiveRoleInfo> getCache() {
		return _Tables_.getInstance().activeroletable.getCache();
	}

	public static mkdb.TTable<Long, xbean.ActiveRoleInfo> getTable() {
		return _Tables_.getInstance().activeroletable;
	}

	public static xbean.ActiveRoleInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, xbean.ActiveRoleInfo>() {
			public xbean.ActiveRoleInfo get(xbean.ActiveRoleInfo v) { return v.toData(); }
		});
	}

	public static Integer selectCreatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getCreatetime(); }
			});
	}

	public static Long selectEnterworldtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Long>() {
				public Long get(xbean.ActiveRoleInfo v) { return v.getEnterworldtime(); }
			});
	}

	public static Integer selectDayonline(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getDayonline(); }
			});
	}

	public static Integer selectDayonlinetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getDayonlinetime(); }
			});
	}

	public static Integer selectWeekonline(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getWeekonline(); }
			});
	}

	public static Integer selectWeekonlinetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getWeekonlinetime(); }
			});
	}

	public static Integer selectLasttimeactiveday1(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getLasttimeactiveday1(); }
			});
	}

	public static Integer selectLasttimeactiveday1onlinetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getLasttimeactiveday1onlinetime(); }
			});
	}

	public static Integer selectLasttimeactiveday2(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getLasttimeactiveday2(); }
			});
	}

	public static Integer selectLasttimeactiveweek1(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getLasttimeactiveweek1(); }
			});
	}

	public static Integer selectLasttimeactiveweek1onlinetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getLasttimeactiveweek1onlinetime(); }
			});
	}

	public static Integer selectLasttimeactiveweek2(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getLasttimeactiveweek2(); }
			});
	}

	public static Integer selectFirsttimeactiveday(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getFirsttimeactiveday(); }
			});
	}

	public static Integer selectFirsttimeactiveweek(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Integer>() {
				public Integer get(xbean.ActiveRoleInfo v) { return v.getFirsttimeactiveweek(); }
			});
	}

	public static Long selectDealmoneyinplatform(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Long>() {
				public Long get(xbean.ActiveRoleInfo v) { return v.getDealmoneyinplatform(); }
			});
	}

	public static Long selectTmpmoneyinplatform(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Long>() {
				public Long get(xbean.ActiveRoleInfo v) { return v.getTmpmoneyinplatform(); }
			});
	}

	public static Long selectMoneyincofc(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveRoleInfo, Long>() {
				public Long get(xbean.ActiveRoleInfo v) { return v.getMoneyincofc(); }
			});
	}

}
