package xtable;

// typed table access point
public class Preselltab {
	Preselltab() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().preselltab.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.PresellItem value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.PresellItem get(Long key) {
		return _Tables_.getInstance().preselltab.get(key);
	}

	public static xbean.PresellItem get(Long key, xbean.PresellItem value) {
		return _Tables_.getInstance().preselltab.get(key, value);
	}

	public static void insert(Long key, xbean.PresellItem value) {
		_Tables_.getInstance().preselltab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().preselltab.delete(key);
	}

	public static boolean add(Long key, xbean.PresellItem value) {
		return _Tables_.getInstance().preselltab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().preselltab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.PresellItem> getCache() {
		return _Tables_.getInstance().preselltab.getCache();
	}

	public static mkdb.TTable<Long, xbean.PresellItem> getTable() {
		return _Tables_.getInstance().preselltab;
	}

	public static xbean.PresellItem select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PresellItem, xbean.PresellItem>() {
			public xbean.PresellItem get(xbean.PresellItem v) { return v.toData(); }
		});
	}

	public static java.util.List<Long> selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PresellItem, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.PresellItem v) { return v.getRoleidAsData(); }
			});
	}

	public static Long selectExpiretime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PresellItem, Long>() {
				public Long get(xbean.PresellItem v) { return v.getExpiretime(); }
			});
	}

}
