package xtable;

// typed table access point
public class Showingmission {
	Showingmission() {
	}

	public static xbean.ShowingMission get(Long key) {
		return _Tables_.getInstance().showingmission.get(key);
	}

	public static xbean.ShowingMission get(Long key, xbean.ShowingMission value) {
		return _Tables_.getInstance().showingmission.get(key, value);
	}

	public static void insert(Long key, xbean.ShowingMission value) {
		_Tables_.getInstance().showingmission.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().showingmission.delete(key);
	}

	public static boolean add(Long key, xbean.ShowingMission value) {
		return _Tables_.getInstance().showingmission.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().showingmission.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ShowingMission> getCache() {
		return _Tables_.getInstance().showingmission.getCache();
	}

	public static mkdb.TTable<Long, xbean.ShowingMission> getTable() {
		return _Tables_.getInstance().showingmission;
	}

	public static xbean.ShowingMission select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ShowingMission, xbean.ShowingMission>() {
			public xbean.ShowingMission get(xbean.ShowingMission v) { return v.toData(); }
		});
	}

	public static Integer selectMissionid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ShowingMission, Integer>() {
				public Integer get(xbean.ShowingMission v) { return v.getMissionid(); }
			});
	}

	public static Boolean selectIsleader(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ShowingMission, Boolean>() {
				public Boolean get(xbean.ShowingMission v) { return v.getIsleader(); }
			});
	}

}
