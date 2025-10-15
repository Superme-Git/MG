package xtable;

// typed table access point
public class Rolerecruitaward {
	Rolerecruitaward() {
	}

	public static xbean.RecruitAward get(Long key) {
		return _Tables_.getInstance().rolerecruitaward.get(key);
	}

	public static xbean.RecruitAward get(Long key, xbean.RecruitAward value) {
		return _Tables_.getInstance().rolerecruitaward.get(key, value);
	}

	public static void insert(Long key, xbean.RecruitAward value) {
		_Tables_.getInstance().rolerecruitaward.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolerecruitaward.delete(key);
	}

	public static boolean add(Long key, xbean.RecruitAward value) {
		return _Tables_.getInstance().rolerecruitaward.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolerecruitaward.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RecruitAward> getCache() {
		return _Tables_.getInstance().rolerecruitaward.getCache();
	}

	public static mkdb.TTable<Long, xbean.RecruitAward> getTable() {
		return _Tables_.getInstance().rolerecruitaward;
	}

	public static xbean.RecruitAward select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RecruitAward, xbean.RecruitAward>() {
			public xbean.RecruitAward get(xbean.RecruitAward v) { return v.toData(); }
		});
	}

	public static Integer selectGetnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RecruitAward, Integer>() {
				public Integer get(xbean.RecruitAward v) { return v.getGetnum(); }
			});
	}

	public static Long selectGettime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RecruitAward, Long>() {
				public Long get(xbean.RecruitAward v) { return v.getGettime(); }
			});
	}

}
