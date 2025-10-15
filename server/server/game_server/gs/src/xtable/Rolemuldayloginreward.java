package xtable;

// typed table access point
public class Rolemuldayloginreward {
	Rolemuldayloginreward() {
	}

	public static xbean.MulDayLoginGift get(Long key) {
		return _Tables_.getInstance().rolemuldayloginreward.get(key);
	}

	public static xbean.MulDayLoginGift get(Long key, xbean.MulDayLoginGift value) {
		return _Tables_.getInstance().rolemuldayloginreward.get(key, value);
	}

	public static void insert(Long key, xbean.MulDayLoginGift value) {
		_Tables_.getInstance().rolemuldayloginreward.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolemuldayloginreward.delete(key);
	}

	public static boolean add(Long key, xbean.MulDayLoginGift value) {
		return _Tables_.getInstance().rolemuldayloginreward.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolemuldayloginreward.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.MulDayLoginGift> getCache() {
		return _Tables_.getInstance().rolemuldayloginreward.getCache();
	}

	public static mkdb.TTable<Long, xbean.MulDayLoginGift> getTable() {
		return _Tables_.getInstance().rolemuldayloginreward;
	}

	public static xbean.MulDayLoginGift select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MulDayLoginGift, xbean.MulDayLoginGift>() {
			public xbean.MulDayLoginGift get(xbean.MulDayLoginGift v) { return v.toData(); }
		});
	}

	public static Integer selectLogindays(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MulDayLoginGift, Integer>() {
				public Integer get(xbean.MulDayLoginGift v) { return v.getLogindays(); }
			});
	}

	public static Long selectLogintime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MulDayLoginGift, Long>() {
				public Long get(xbean.MulDayLoginGift v) { return v.getLogintime(); }
			});
	}

	public static java.util.Map<Integer, Long> selectRewardmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MulDayLoginGift, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.MulDayLoginGift v) { return v.getRewardmapAsData(); }
			});
	}

}
