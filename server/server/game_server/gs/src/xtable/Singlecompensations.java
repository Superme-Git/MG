package xtable;

// typed table access point
public class Singlecompensations {
	Singlecompensations() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().singlecompensations.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.SingleCompensation value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.SingleCompensation get(Long key) {
		return _Tables_.getInstance().singlecompensations.get(key);
	}

	public static xbean.SingleCompensation get(Long key, xbean.SingleCompensation value) {
		return _Tables_.getInstance().singlecompensations.get(key, value);
	}

	public static void insert(Long key, xbean.SingleCompensation value) {
		_Tables_.getInstance().singlecompensations.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().singlecompensations.delete(key);
	}

	public static boolean add(Long key, xbean.SingleCompensation value) {
		return _Tables_.getInstance().singlecompensations.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().singlecompensations.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.SingleCompensation> getCache() {
		return _Tables_.getInstance().singlecompensations.getCache();
	}

	public static mkdb.TTable<Long, xbean.SingleCompensation> getTable() {
		return _Tables_.getInstance().singlecompensations;
	}

	public static xbean.SingleCompensation select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, xbean.SingleCompensation>() {
			public xbean.SingleCompensation get(xbean.SingleCompensation v) { return v.toData(); }
		});
	}

	public static Long selectTime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, Long>() {
				public Long get(xbean.SingleCompensation v) { return v.getTime(); }
			});
	}

	public static Long selectValidtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, Long>() {
				public Long get(xbean.SingleCompensation v) { return v.getValidtime(); }
			});
	}

	public static String selectTitle(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, String>() {
				public String get(xbean.SingleCompensation v) { return v.getTitle(); }
			});
	}

	public static String selectContent(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, String>() {
				public String get(xbean.SingleCompensation v) { return v.getContent(); }
			});
	}

	public static java.util.List<xbean.SingleCompensationAward> selectAwards(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, java.util.List<xbean.SingleCompensationAward>>() {
				public java.util.List<xbean.SingleCompensationAward> get(xbean.SingleCompensation v) { return v.getAwardsAsData(); }
			});
	}

	public static Integer selectGivetype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, Integer>() {
				public Integer get(xbean.SingleCompensation v) { return v.getGivetype(); }
			});
	}

	public static java.util.List<Long> selectRoleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.SingleCompensation v) { return v.getRoleidsAsData(); }
			});
	}

	public static java.util.List<Long> selectRoleidsofget(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.SingleCompensation v) { return v.getRoleidsofgetAsData(); }
			});
	}

	public static java.util.List<Long> selectRoleidsofexcept(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.SingleCompensation v) { return v.getRoleidsofexceptAsData(); }
			});
	}

	public static java.util.List<xbean.SingleCompensationCond> selectConditions(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, java.util.List<xbean.SingleCompensationCond>>() {
				public java.util.List<xbean.SingleCompensationCond> get(xbean.SingleCompensation v) { return v.getConditionsAsData(); }
			});
	}

	public static String selectOpid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, String>() {
				public String get(xbean.SingleCompensation v) { return v.getOpid(); }
			});
	}

	public static String selectSign(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SingleCompensation, String>() {
				public String get(xbean.SingleCompensation v) { return v.getSign(); }
			});
	}

}
