package xtable;

// typed table access point
public class Rolesplayactive {
	Rolesplayactive() {
	}

	public static xbean.NewPlayActiveDay get(Long key) {
		return _Tables_.getInstance().rolesplayactive.get(key);
	}

	public static xbean.NewPlayActiveDay get(Long key, xbean.NewPlayActiveDay value) {
		return _Tables_.getInstance().rolesplayactive.get(key, value);
	}

	public static void insert(Long key, xbean.NewPlayActiveDay value) {
		_Tables_.getInstance().rolesplayactive.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolesplayactive.delete(key);
	}

	public static boolean add(Long key, xbean.NewPlayActiveDay value) {
		return _Tables_.getInstance().rolesplayactive.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolesplayactive.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.NewPlayActiveDay> getCache() {
		return _Tables_.getInstance().rolesplayactive.getCache();
	}

	public static mkdb.TTable<Long, xbean.NewPlayActiveDay> getTable() {
		return _Tables_.getInstance().rolesplayactive;
	}

	public static xbean.NewPlayActiveDay select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveDay, xbean.NewPlayActiveDay>() {
			public xbean.NewPlayActiveDay get(xbean.NewPlayActiveDay v) { return v.toData(); }
		});
	}

	public static Long selectTime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveDay, Long>() {
				public Long get(xbean.NewPlayActiveDay v) { return v.getTime(); }
			});
	}

	public static Float selectActiveness(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveDay, Float>() {
				public Float get(xbean.NewPlayActiveDay v) { return v.getActiveness(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectChests(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveDay, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.NewPlayActiveDay v) { return v.getChestsAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.PlayActiveData> selectActives(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveDay, java.util.Map<Integer, xbean.PlayActiveData>>() {
				public java.util.Map<Integer, xbean.PlayActiveData> get(xbean.NewPlayActiveDay v) { return v.getActivesAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectActivescount(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveDay, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.NewPlayActiveDay v) { return v.getActivescountAsData(); }
			});
	}

	public static Long selectYingfutime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveDay, Long>() {
				public Long get(xbean.NewPlayActiveDay v) { return v.getYingfutime(); }
			});
	}

	public static Long selectYingfuexp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NewPlayActiveDay, Long>() {
				public Long get(xbean.NewPlayActiveDay v) { return v.getYingfuexp(); }
			});
	}

}
