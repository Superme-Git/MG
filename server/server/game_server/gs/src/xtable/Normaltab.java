package xtable;

// typed table access point
public class Normaltab {
	Normaltab() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().normaltab.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.NormalItem value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.NormalItem get(Long key) {
		return _Tables_.getInstance().normaltab.get(key);
	}

	public static xbean.NormalItem get(Long key, xbean.NormalItem value) {
		return _Tables_.getInstance().normaltab.get(key, value);
	}

	public static void insert(Long key, xbean.NormalItem value) {
		_Tables_.getInstance().normaltab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().normaltab.delete(key);
	}

	public static boolean add(Long key, xbean.NormalItem value) {
		return _Tables_.getInstance().normaltab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().normaltab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.NormalItem> getCache() {
		return _Tables_.getInstance().normaltab.getCache();
	}

	public static mkdb.TTable<Long, xbean.NormalItem> getTable() {
		return _Tables_.getInstance().normaltab;
	}

	public static xbean.NormalItem select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, xbean.NormalItem>() {
			public xbean.NormalItem get(xbean.NormalItem v) { return v.toData(); }
		});
	}

	public static Long selectId(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Long>() {
				public Long get(xbean.NormalItem v) { return v.getId(); }
			});
	}

	public static Integer selectFirstno(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Integer>() {
				public Integer get(xbean.NormalItem v) { return v.getFirstno(); }
			});
	}

	public static Integer selectTwono(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Integer>() {
				public Integer get(xbean.NormalItem v) { return v.getTwono(); }
			});
	}

	public static Integer selectThreeno(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Integer>() {
				public Integer get(xbean.NormalItem v) { return v.getThreeno(); }
			});
	}

	public static Long selectUniquid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Long>() {
				public Long get(xbean.NormalItem v) { return v.getUniquid(); }
			});
	}

	public static Integer selectKey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Integer>() {
				public Integer get(xbean.NormalItem v) { return v.getKey(); }
			});
	}

	public static Integer selectItemid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Integer>() {
				public Integer get(xbean.NormalItem v) { return v.getItemid(); }
			});
	}

	public static Long selectExtid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Long>() {
				public Long get(xbean.NormalItem v) { return v.getExtid(); }
			});
	}

	public static String selectName(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, String>() {
				public String get(xbean.NormalItem v) { return v.getName(); }
			});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Long>() {
				public Long get(xbean.NormalItem v) { return v.getRoleid(); }
			});
	}

	public static Integer selectLevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Integer>() {
				public Integer get(xbean.NormalItem v) { return v.getLevel(); }
			});
	}

	public static Integer selectNumber(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Integer>() {
				public Integer get(xbean.NormalItem v) { return v.getNumber(); }
			});
	}

	public static Integer selectPrice(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Integer>() {
				public Integer get(xbean.NormalItem v) { return v.getPrice(); }
			});
	}

	public static Integer selectAttentionnumber(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Integer>() {
				public Integer get(xbean.NormalItem v) { return v.getAttentionnumber(); }
			});
	}

	public static Long selectShowtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Long>() {
				public Long get(xbean.NormalItem v) { return v.getShowtime(); }
			});
	}

	public static Long selectExpiretime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NormalItem, Long>() {
				public Long get(xbean.NormalItem v) { return v.getExpiretime(); }
			});
	}

}
