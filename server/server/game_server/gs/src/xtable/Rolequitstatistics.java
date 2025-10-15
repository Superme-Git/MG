package xtable;

// typed table access point
public class Rolequitstatistics {
	Rolequitstatistics() {
	}

	public static xbean.RoleQuitStatistics get(Long key) {
		return _Tables_.getInstance().rolequitstatistics.get(key);
	}

	public static xbean.RoleQuitStatistics get(Long key, xbean.RoleQuitStatistics value) {
		return _Tables_.getInstance().rolequitstatistics.get(key, value);
	}

	public static void insert(Long key, xbean.RoleQuitStatistics value) {
		_Tables_.getInstance().rolequitstatistics.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolequitstatistics.delete(key);
	}

	public static boolean add(Long key, xbean.RoleQuitStatistics value) {
		return _Tables_.getInstance().rolequitstatistics.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolequitstatistics.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleQuitStatistics> getCache() {
		return _Tables_.getInstance().rolequitstatistics.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleQuitStatistics> getTable() {
		return _Tables_.getInstance().rolequitstatistics;
	}

	public static xbean.RoleQuitStatistics select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleQuitStatistics, xbean.RoleQuitStatistics>() {
			public xbean.RoleQuitStatistics get(xbean.RoleQuitStatistics v) { return v.toData(); }
		});
	}

	public static Long selectMoney(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleQuitStatistics, Long>() {
				public Long get(xbean.RoleQuitStatistics v) { return v.getMoney(); }
			});
	}

	public static Integer selectExp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleQuitStatistics, Integer>() {
				public Integer get(xbean.RoleQuitStatistics v) { return v.getExp(); }
			});
	}

	public static java.util.Map<Integer, Long> selectCurrency(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleQuitStatistics, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.RoleQuitStatistics v) { return v.getCurrencyAsData(); }
			});
	}

	public static Long selectLastcountdate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleQuitStatistics, Long>() {
				public Long get(xbean.RoleQuitStatistics v) { return v.getLastcountdate(); }
			});
	}

	public static Integer selectLastrewardidx(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleQuitStatistics, Integer>() {
				public Integer get(xbean.RoleQuitStatistics v) { return v.getLastrewardidx(); }
			});
	}

	public static Long selectRewarddate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleQuitStatistics, Long>() {
				public Long get(xbean.RoleQuitStatistics v) { return v.getRewarddate(); }
			});
	}

}
