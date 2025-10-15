package xtable;

// typed table access point
public class Rolesfestivaldata {
	Rolesfestivaldata() {
	}

	public static xbean.FestivalData get(Long key) {
		return _Tables_.getInstance().rolesfestivaldata.get(key);
	}

	public static xbean.FestivalData get(Long key, xbean.FestivalData value) {
		return _Tables_.getInstance().rolesfestivaldata.get(key, value);
	}

	public static void insert(Long key, xbean.FestivalData value) {
		_Tables_.getInstance().rolesfestivaldata.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolesfestivaldata.delete(key);
	}

	public static boolean add(Long key, xbean.FestivalData value) {
		return _Tables_.getInstance().rolesfestivaldata.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolesfestivaldata.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.FestivalData> getCache() {
		return _Tables_.getInstance().rolesfestivaldata.getCache();
	}

	public static mkdb.TTable<Long, xbean.FestivalData> getTable() {
		return _Tables_.getInstance().rolesfestivaldata;
	}

	public static xbean.FestivalData select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FestivalData, xbean.FestivalData>() {
			public xbean.FestivalData get(xbean.FestivalData v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Long> selectRewardmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FestivalData, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.FestivalData v) { return v.getRewardmapAsData(); }
			});
	}

}
