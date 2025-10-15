package xtable;

// typed table access point
public class Roleid2monthcard {
	Roleid2monthcard() {
	}

	public static xbean.EMonthCard get(Long key) {
		return _Tables_.getInstance().roleid2monthcard.get(key);
	}

	public static xbean.EMonthCard get(Long key, xbean.EMonthCard value) {
		return _Tables_.getInstance().roleid2monthcard.get(key, value);
	}

	public static void insert(Long key, xbean.EMonthCard value) {
		_Tables_.getInstance().roleid2monthcard.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2monthcard.delete(key);
	}

	public static boolean add(Long key, xbean.EMonthCard value) {
		return _Tables_.getInstance().roleid2monthcard.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2monthcard.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.EMonthCard> getCache() {
		return _Tables_.getInstance().roleid2monthcard.getCache();
	}

	public static mkdb.TTable<Long, xbean.EMonthCard> getTable() {
		return _Tables_.getInstance().roleid2monthcard;
	}

	public static xbean.EMonthCard select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EMonthCard, xbean.EMonthCard>() {
			public xbean.EMonthCard get(xbean.EMonthCard v) { return v.toData(); }
		});
	}

	public static Long selectEndtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EMonthCard, Long>() {
				public Long get(xbean.EMonthCard v) { return v.getEndtime(); }
			});
	}

	public static Long selectGrabtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EMonthCard, Long>() {
				public Long get(xbean.EMonthCard v) { return v.getGrabtime(); }
			});
	}

	public static Long selectFirstprompttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EMonthCard, Long>() {
				public Long get(xbean.EMonthCard v) { return v.getFirstprompttime(); }
			});
	}

}
