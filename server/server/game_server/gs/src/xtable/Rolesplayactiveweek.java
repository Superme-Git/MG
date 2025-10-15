package xtable;

// typed table access point
public class Rolesplayactiveweek {
	Rolesplayactiveweek() {
	}

	public static xbean.NewPlayActiveWeek get(Long key) {
		return _Tables_.getInstance().rolesplayactiveweek.get(key);
	}

	public static xbean.NewPlayActiveWeek get(Long key, xbean.NewPlayActiveWeek value) {
		return _Tables_.getInstance().rolesplayactiveweek.get(key, value);
	}

	public static void insert(Long key, xbean.NewPlayActiveWeek value) {
		_Tables_.getInstance().rolesplayactiveweek.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolesplayactiveweek.delete(key);
	}

	public static boolean add(Long key, xbean.NewPlayActiveWeek value) {
		return _Tables_.getInstance().rolesplayactiveweek.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolesplayactiveweek.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.NewPlayActiveWeek> getCache() {
		return _Tables_.getInstance().rolesplayactiveweek.getCache();
	}

	public static mkdb.TTable<Long, xbean.NewPlayActiveWeek> getTable() {
		return _Tables_.getInstance().rolesplayactiveweek;
	}

	public static xbean.NewPlayActiveWeek select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveWeek, xbean.NewPlayActiveWeek>() {
			public xbean.NewPlayActiveWeek get(xbean.NewPlayActiveWeek v) { return v.toData(); }
		});
	}

	public static Long selectTime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveWeek, Long>() {
				public Long get(xbean.NewPlayActiveWeek v) { return v.getTime(); }
			});
	}

	public static java.util.Map<Integer, xbean.PlayActiveData> selectActives(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveWeek, java.util.Map<Integer, xbean.PlayActiveData>>() {
				public java.util.Map<Integer, xbean.PlayActiveData> get(xbean.NewPlayActiveWeek v) { return v.getActivesAsData(); }
			});
	}

}
