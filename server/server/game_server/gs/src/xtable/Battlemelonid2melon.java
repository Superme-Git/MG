package xtable;

// typed table access point
public class Battlemelonid2melon {
	Battlemelonid2melon() {
	}

	public static xbean.ETeamMelon get(Long key) {
		return _Tables_.getInstance().battlemelonid2melon.get(key);
	}

	public static xbean.ETeamMelon get(Long key, xbean.ETeamMelon value) {
		return _Tables_.getInstance().battlemelonid2melon.get(key, value);
	}

	public static void insert(Long key, xbean.ETeamMelon value) {
		_Tables_.getInstance().battlemelonid2melon.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().battlemelonid2melon.delete(key);
	}

	public static boolean add(Long key, xbean.ETeamMelon value) {
		return _Tables_.getInstance().battlemelonid2melon.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().battlemelonid2melon.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ETeamMelon> getCache() {
		return _Tables_.getInstance().battlemelonid2melon.getCache();
	}

	public static mkdb.TTable<Long, xbean.ETeamMelon> getTable() {
		return _Tables_.getInstance().battlemelonid2melon;
	}

	public static xbean.ETeamMelon select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMelon, xbean.ETeamMelon>() {
			public xbean.ETeamMelon get(xbean.ETeamMelon v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.TeamMelon> selectMelonid2melons(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMelon, java.util.Map<Long, xbean.TeamMelon>>() {
				public java.util.Map<Long, xbean.TeamMelon> get(xbean.ETeamMelon v) { return v.getMelonid2melonsAsData(); }
			});
	}

	public static Integer selectMelontype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMelon, Integer>() {
				public Integer get(xbean.ETeamMelon v) { return v.getMelontype(); }
			});
	}

	public static Integer selectDataid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMelon, Integer>() {
				public Integer get(xbean.ETeamMelon v) { return v.getDataid(); }
			});
	}

	public static Long selectDataid2(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMelon, Long>() {
				public Long get(xbean.ETeamMelon v) { return v.getDataid2(); }
			});
	}

	public static java.util.List<Long> selectMelonerlist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMelon, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.ETeamMelon v) { return v.getMelonerlistAsData(); }
			});
	}

	public static java.util.List<Long> selectWatchmelonerlist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMelon, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.ETeamMelon v) { return v.getWatchmelonerlistAsData(); }
			});
	}

}
