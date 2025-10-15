package xtable;

// typed table access point
public class Festival {
	Festival() {
	}

	public static xbean.FestivalGift get(Long key) {
		return _Tables_.getInstance().festival.get(key);
	}

	public static xbean.FestivalGift get(Long key, xbean.FestivalGift value) {
		return _Tables_.getInstance().festival.get(key, value);
	}

	public static void insert(Long key, xbean.FestivalGift value) {
		_Tables_.getInstance().festival.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().festival.delete(key);
	}

	public static boolean add(Long key, xbean.FestivalGift value) {
		return _Tables_.getInstance().festival.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().festival.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.FestivalGift> getCache() {
		return _Tables_.getInstance().festival.getCache();
	}

	public static mkdb.TTable<Long, xbean.FestivalGift> getTable() {
		return _Tables_.getInstance().festival;
	}

	public static xbean.FestivalGift select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FestivalGift, xbean.FestivalGift>() {
			public xbean.FestivalGift get(xbean.FestivalGift v) { return v.toData(); }
		});
	}

	public static Long selectTime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FestivalGift, Long>() {
				public Long get(xbean.FestivalGift v) { return v.getTime(); }
			});
	}

	public static Long selectOnlinetotal(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FestivalGift, Long>() {
				public Long get(xbean.FestivalGift v) { return v.getOnlinetotal(); }
			});
	}

}
