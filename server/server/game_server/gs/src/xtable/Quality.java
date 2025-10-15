package xtable;

// typed table access point
public class Quality {
	Quality() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().quality.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.RecoveryAttr value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.RecoveryAttr get(Long key) {
		return _Tables_.getInstance().quality.get(key);
	}

	public static xbean.RecoveryAttr get(Long key, xbean.RecoveryAttr value) {
		return _Tables_.getInstance().quality.get(key, value);
	}

	public static void insert(Long key, xbean.RecoveryAttr value) {
		_Tables_.getInstance().quality.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().quality.delete(key);
	}

	public static boolean add(Long key, xbean.RecoveryAttr value) {
		return _Tables_.getInstance().quality.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().quality.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RecoveryAttr> getCache() {
		return _Tables_.getInstance().quality.getCache();
	}

	public static mkdb.TTable<Long, xbean.RecoveryAttr> getTable() {
		return _Tables_.getInstance().quality;
	}

	public static xbean.RecoveryAttr select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RecoveryAttr, xbean.RecoveryAttr>() {
			public xbean.RecoveryAttr get(xbean.RecoveryAttr v) { return v.toData(); }
		});
	}

	public static Integer selectQuality(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RecoveryAttr, Integer>() {
				public Integer get(xbean.RecoveryAttr v) { return v.getQuality(); }
			});
	}

}
