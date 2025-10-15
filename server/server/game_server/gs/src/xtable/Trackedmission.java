package xtable;

// typed table access point
public class Trackedmission {
	Trackedmission() {
	}

	public static xbean.TrackedMission get(Long key) {
		return _Tables_.getInstance().trackedmission.get(key);
	}

	public static xbean.TrackedMission get(Long key, xbean.TrackedMission value) {
		return _Tables_.getInstance().trackedmission.get(key, value);
	}

	public static void insert(Long key, xbean.TrackedMission value) {
		_Tables_.getInstance().trackedmission.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().trackedmission.delete(key);
	}

	public static boolean add(Long key, xbean.TrackedMission value) {
		return _Tables_.getInstance().trackedmission.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().trackedmission.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TrackedMission> getCache() {
		return _Tables_.getInstance().trackedmission.getCache();
	}

	public static mkdb.TTable<Long, xbean.TrackedMission> getTable() {
		return _Tables_.getInstance().trackedmission;
	}

	public static xbean.TrackedMission select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TrackedMission, xbean.TrackedMission>() {
			public xbean.TrackedMission get(xbean.TrackedMission v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.Track> selectQuestids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TrackedMission, java.util.Map<Integer, xbean.Track>>() {
				public java.util.Map<Integer, xbean.Track> get(xbean.TrackedMission v) { return v.getQuestidsAsData(); }
			});
	}

}
