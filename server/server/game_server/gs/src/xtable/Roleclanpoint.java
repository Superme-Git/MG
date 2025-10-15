package xtable;

// typed table access point
public class Roleclanpoint {
	Roleclanpoint() {
	}

	public static xbean.RoleClanPoint get(Long key) {
		return _Tables_.getInstance().roleclanpoint.get(key);
	}

	public static xbean.RoleClanPoint get(Long key, xbean.RoleClanPoint value) {
		return _Tables_.getInstance().roleclanpoint.get(key, value);
	}

	public static void insert(Long key, xbean.RoleClanPoint value) {
		_Tables_.getInstance().roleclanpoint.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleclanpoint.delete(key);
	}

	public static boolean add(Long key, xbean.RoleClanPoint value) {
		return _Tables_.getInstance().roleclanpoint.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleclanpoint.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleClanPoint> getCache() {
		return _Tables_.getInstance().roleclanpoint.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleClanPoint> getTable() {
		return _Tables_.getInstance().roleclanpoint;
	}

	public static xbean.RoleClanPoint select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleClanPoint, xbean.RoleClanPoint>() {
			public xbean.RoleClanPoint get(xbean.RoleClanPoint v) { return v.toData(); }
		});
	}

	public static Integer selectFreezedclanpoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleClanPoint, Integer>() {
				public Integer get(xbean.RoleClanPoint v) { return v.getFreezedclanpoint(); }
			});
	}

	public static Integer selectCurrentclanpoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleClanPoint, Integer>() {
				public Integer get(xbean.RoleClanPoint v) { return v.getCurrentclanpoint(); }
			});
	}

	public static Integer selectHistoryclanpoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleClanPoint, Integer>() {
				public Integer get(xbean.RoleClanPoint v) { return v.getHistoryclanpoint(); }
			});
	}

	public static Integer selectWeekclanpoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleClanPoint, Integer>() {
				public Integer get(xbean.RoleClanPoint v) { return v.getWeekclanpoint(); }
			});
	}

	public static Integer selectPreweekclanpoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleClanPoint, Integer>() {
				public Integer get(xbean.RoleClanPoint v) { return v.getPreweekclanpoint(); }
			});
	}

	public static Integer selectOldhistoryclanpoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleClanPoint, Integer>() {
				public Integer get(xbean.RoleClanPoint v) { return v.getOldhistoryclanpoint(); }
			});
	}

	public static Integer selectOldfreezedclanpoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleClanPoint, Integer>() {
				public Integer get(xbean.RoleClanPoint v) { return v.getOldfreezedclanpoint(); }
			});
	}

}
